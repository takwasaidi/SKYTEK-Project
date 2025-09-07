package tn.esprit.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import tn.esprit.backend.dto.Reservation.ReservationDTO;
import tn.esprit.backend.dto.Reservation.Reservation_salleDto;
import tn.esprit.backend.entity.*;
import tn.esprit.backend.mappers.ReservationMapper;
import tn.esprit.backend.repository.*;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final UserRepository userRepository;
    private final SalleRepository salleRepository;
    private final  ReservationRepository reservationRepository;
    private final QuotaRepository quotaRepository;
    private final ConfigurationHoraireRepository plageHoraireRepository;
    private final QuotaAlertService quotaAlertService;
private final NotificationService notificationService;
    private final EmailService emailService;

    public ReservationDTO addReservation(ReservationDTO dto,Integer userId) {

        DayOfWeek jour = dto.getDateReservation().getDayOfWeek();

        // 1. Vérification que la réservation est dans la plage horaire admin
        ConfigurationHoraire plage = plageHoraireRepository.findByJour(jour)
                .orElseThrow(() -> new RuntimeException("Plage horaire non définie pour ce jour"));

        if (dto.getHeureDebut().isBefore(plage.getHeureDebut()) || dto.getHeureFin().isAfter(plage.getHeureFin())) {
            throw new RuntimeException("La réservation doit être dans la plage autorisée : " +
                    plage.getHeureDebut() + " - " + plage.getHeureFin());
        }

        // 2. Vérification que les heures sont rondes (pas de 07:15 par ex)
        if (dto.getHeureDebut().getMinute() != 0 || dto.getHeureFin().getMinute() != 0) {
            throw new RuntimeException("La réservation doit commencer et finir à une heure ronde (ex: 08:00)");
        }

        // 3. Vérification qu’il n’y a pas de conflit
        boolean conflit = reservationRepository.existsBySalleIdAndDateReservationAndHeureDebutLessThanAndHeureFinGreaterThan(
                dto.getSalleId(), dto.getDateReservation(), dto.getHeureFin(), dto.getHeureDebut()
        );
        // 4.Vérification que l'heure de debut avant l'heure fin
        if (dto.getHeureDebut().isAfter(dto.getHeureFin()) || dto.getHeureDebut().equals(dto.getHeureFin())) {
            throw new RuntimeException("L'heure de début doit être strictement avant l'heure de fin");
        }


        if (conflit) {
            throw new RuntimeException("Il existe déjà une réservation dans ce créneau");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Salle salle = salleRepository.findById(dto.getSalleId())
                .orElseThrow(() -> new RuntimeException("Salle not found"));

        // Calcul de la durée de réservation en heures
        long dureeMinutes = Duration.between(dto.getHeureDebut(), dto.getHeureFin()).toMinutes();
        double dureeHeures = dureeMinutes / 60.0;

        double heuresGratuites = 0;
        double heuresFacturables = dureeHeures;

        if ("INTERN_USER".equalsIgnoreCase(user.getUser_type())) {
            // Cas utilisateur interne
            Entreprise entreprise = user.getEntreprise();
            if (entreprise == null) {
                throw new RuntimeException("Entreprise non définie pour l'utilisateur interne");
            }

            Quota quota = entreprise.getQuota();
            if (quota == null) {
                throw new RuntimeException("Quota non configuré pour l'entreprise");
            }

            int quotaTotal = quota.getQuota();
            int quotaUtiliseAvant = quota.getQuotaUtilise();

            double quotaRestant = quotaTotal - quotaUtiliseAvant;

            if (quotaRestant <= 0) {
                heuresGratuites = 0;
                heuresFacturables = dureeHeures;
            } else {
                heuresGratuites = Math.min(dureeHeures, quotaRestant);
                heuresFacturables = Math.max(0, dureeHeures - quotaRestant);
            }

            quota.setQuotaUtilise(quotaUtiliseAvant + (int) Math.ceil(dureeHeures));
            quotaRepository.save(quota);
            if (quota.getQuotaUtilise() > quota.getQuota()) {
                quotaAlertService.createAlert(entreprise, "DEPASSEMENT_QUOTA");
            } else if (quota.getQuotaUtilise() >= quota.getQuota() * 0.9) {
                quotaAlertService.createAlert(entreprise, "PROCHE_DEPASSEMENT");
            }


        }
        System.out.println(heuresFacturables);
        // Coût total uniquement sur les heures facturables
        double coutTotal = heuresFacturables * salle.getTarifHoraire();

        Reservation reservation = Reservation.builder()
                .dateReservation(dto.getDateReservation())
                .heureDebut(dto.getHeureDebut())
                .heureFin(dto.getHeureFin())
                .estFacturable(heuresFacturables > 0) // même les externes paient dès la 1ère heure
                .coutTotal(coutTotal)
                .dateCreation(LocalDateTime.now())
                .dateModification(LocalDateTime.now())
                .user(user)
                .salle(salle) 
                .build();
        System.out.println("Reservation ID: " + reservation.getId() +
                ", Date: " + reservation.getDateReservation() +
                ", Heure début: " + reservation.getHeureDebut() +
                ", Heure fin: " + reservation.getHeureFin() +
                ", Cout total: " + reservation.getCoutTotal());

        reservation = reservationRepository.save(reservation);

        // Envoyer notification WebSocket
        String msg = "Nouvelle réservation : " + reservation.getUser().getFirstname() +
                " " + reservation.getUser().getLastname() + " - " +
                reservation.getSalle().getNom();
        notificationService.notifyNewReservation(msg);

        // 📧 Notification Email
        String subject = "Confirmation de votre réservation";
        StringBuilder bodyBuilder = new StringBuilder();

        bodyBuilder.append("<div style='font-family:Arial,sans-serif; background:#f7f7f7; padding:20px;'>")
                .append("<div style='max-width:600px; margin:auto; background:white; padding:20px; border-radius:8px; ")
                .append("box-shadow:0 2px 6px rgba(0,0,0,0.1);'>")

                // Header
                .append("<h2 style='color:black; text-align:center;'>")
                .append(" Réservation Confirmée</h2>")
                .append("<hr style='border:0; border-top:3px solid color:black;; width:60px; margin:auto; margin-bottom:20px;'>")

                // Message
                .append("<p style='color:black; font-size:15px;'>Bonjour <b>")
                .append(user.getFirstname()).append(" ").append(user.getLastname())
                .append("</b>,<br>Votre réservation a été effectuée avec succès </p>")

                // Détails
                .append("<ul style='list-style:none; padding:0; font-size:14px; color:#333;'>")
                .append("<li> <b>Salle :</b> ").append(salle.getNom()).append("</li>")
                .append("<li> <b>Date :</b> ").append(reservation.getDateReservation()).append("</li>")
                .append("<li> <b>Horaire :</b> ").append(reservation.getHeureDebut())
                .append(" - ").append(reservation.getHeureFin()).append("</li>");

// 👉 Condition pour le coût
        if (reservation.isEstFacturable() && reservation.getCoutTotal() != 0) {
            bodyBuilder.append("<li> <b>Coût total :</b> ")
                    .append(reservation.getCoutTotal()).append(" TND</li>");
        }

        bodyBuilder.append("</ul>")

                // Annulation
                .append("<p style='font-size:14px; color:#444;'>")
                .append("Si vous souhaitez annuler votre réservation, cliquez sur le bouton ci-dessous :</p>")

                .append("<div style='text-align:center; margin:20px;'>")
                .append("<a href='http://localhost:4200/reservationhisto' ")
                .append("style='display:inline-block; padding:12px 24px; background:rgb(14,190,255); color:white; ")
                .append("text-decoration:none; border-radius:6px; font-weight:bold;'> Annuler ma réservation</a>")
                .append("</div>")

                // Footer
                .append("<p style='font-size:12px; color:gray; text-align:center;'>")
                .append("Merci de votre confiance.<br>© 2025 Votre Société</p>")

                .append("</div></div>");

        emailService.sendEmail(user.getEmail(), subject, bodyBuilder.toString(), true);


        return ReservationMapper.toDto(reservation);
    }


    public List<ReservationDTO> getAll() {
        return reservationRepository.findAll()
                .stream()
                .map(ReservationMapper::toDto)
                .collect(Collectors.toList());
    }
    public List<Reservation_salleDto> getReservations(Boolean allByEntreprise, Integer userId){
        if (Boolean.TRUE.equals(allByEntreprise)) {
            // Récupérer l'utilisateur
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            Entreprise entreprise = user.getEntreprise();
            if (entreprise == null) {
                throw new RuntimeException("L'utilisateur n'a pas d'entreprise");
            }
            // Retourner toutes les réservations liées à cette entreprise
            return reservationRepository.findByUserEntrepriseId(entreprise.getId())
                    .stream()
                    .map(ReservationMapper::toDto1).toList();
        } else {
            // Retourner uniquement les réservations de l'utilisateur
            return reservationRepository.findByUserId(userId)
                    .stream()
                    .map(ReservationMapper::toDto1).toList();
        }
    }

    public List<Reservation> getReservationsByCompanyETSalle(Integer companyId,Integer salleId) {
        return reservationRepository.findBySalleIdAndUserEntrepriseId(salleId,companyId);
    }

    public List<Reservation> getReservationsByUserETSalle(Integer userId,Integer salleId) {
        return reservationRepository.findBySalleIdAndUserId(salleId,userId);
    }

    public void annulerReservation(Integer id ,Integer userId) {
        if (!reservationRepository.existsById(id)) {
            throw new RuntimeException("Réservation introuvable avec l'id: " + id);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Reservation res = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation introuvable"));

        // Calcul de la durée de réservation en heures
        long dureeMinutes = Duration.between(res.getHeureDebut(), res.getHeureFin()).toMinutes();
        double dureeHeures = dureeMinutes / 60.0;

        if ("INTERN_USER".equalsIgnoreCase(user.getUser_type())) {
            Entreprise entreprise = user.getEntreprise();
            if (entreprise == null) {
                throw new RuntimeException("Entreprise non définie pour l'utilisateur interne");
            }

            Quota quota = entreprise.getQuota();
            if (quota == null) {
                throw new RuntimeException("Quota non configuré pour l'entreprise");
            }

            int quotaActuel = quota.getQuotaUtilise() != null ? quota.getQuotaUtilise() : 0;

            // ⚡ On ajoute les heures annulées au quota
            int nouveauQuota = quotaActuel - (int) Math.ceil(dureeHeures);

            quota.setQuotaUtilise(nouveauQuota);
            quotaRepository.save(quota);
        }

        reservationRepository.deleteById(id);
    }



    @Scheduled(fixedRate = 60000) // vérifie chaque minute
    public void checkReminders() {
        System.out.println("▶ Checking reminders... " + LocalDateTime.now());

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime target = now.plusMinutes(10);

        List<Reservation> reservations = reservationRepository.findAll();
        System.out.println("🔎 Nombre de réservations trouvées : " + reservations.size());

        for (Reservation r : reservations) {
            LocalDateTime start = LocalDateTime.of(r.getDateReservation(), r.getHeureDebut());

            System.out.println("➡ Reservation ID=" + r.getId() +
                    " | Salle=" + r.getSalle().getNom() +
                    " | Début=" + start +
                    " | User=" + r.getUser().getUsername());

            if (start.isAfter(now) && start.isBefore(target)) {
                System.out.println("✅ Reminder déclenché pour la réservation ID=" + r.getId());
                // Envoyer notification WebSocket
                String msg = "Votre réservation dans la salle " + r.getSalle().getNom() + " commence dans 10 minutes.";
                notificationService.notifyNewReservation(msg);

            } else {
                System.out.println("❌ Pas de reminder : réservation ID=" + r.getId() +
                        " (diff=" + java.time.Duration.between(now, start).toMinutes() + " min)");
            }
        }
    }





}
