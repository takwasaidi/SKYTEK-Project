package tn.esprit.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.backend.dto.Reservation.ReservationDTO;
import tn.esprit.backend.entity.*;
import tn.esprit.backend.mappers.ReservationMapper;
import tn.esprit.backend.repository.QuotaRepository;
import tn.esprit.backend.repository.ReservationRepository;
import tn.esprit.backend.repository.SalleRepository;
import tn.esprit.backend.repository.UserRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {
    @Autowired
            private UserRepository userRepository;
    @Autowired
            private SalleRepository salleRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private QuotaRepository quotaRepository;

    public ReservationDTO addReservation(ReservationDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Salle salle = salleRepository.findById(dto.getSalleId())
                .orElseThrow(() -> new RuntimeException("Salle not found"));

        // Calcul de la durée de réservation en heures (double)
        long dureeMinutes = Duration.between(dto.getHeureDebut(), dto.getHeureFin()).toMinutes();
        double dureeHeures = dureeMinutes / 60.0;

        // Récupérer le quota de l'entreprise de l'utilisateur
        Quota quota = user.getEntreprise().getQuota();
        if (quota == null) {
            throw new RuntimeException("Quota non configuré pour l'entreprise");
        }

        double quotaRestant = quota.getQuota() - quota.getQuotaUtilise();

        double heuresGratuites = Math.min(dureeHeures, quotaRestant);
        double heuresFacturables = Math.max(0, dureeHeures - quotaRestant);

        // Mise à jour du quota utilisé uniquement si utilisateur interne
        if ("INTERN_USER".equalsIgnoreCase(user.getUser_type())) {
            quota.setQuotaUtilise(quota.getQuotaUtilise() + (int) Math.ceil(heuresGratuites));
            quotaRepository.save(quota);
        }

        // Calcul coût total en fonction des heures facturables
        double coutTotal = heuresFacturables * salle.getTarifHoraire();

        // Créer la réservation avec les infos mises à jour
        Reservation reservation = Reservation.builder()
                .dateReservation(dto.getDateReservation())
                .heureDebut(dto.getHeureDebut())
                .heureFin(dto.getHeureFin())
                .estFacturable(heuresFacturables > 0 || !"interne".equalsIgnoreCase(user.getUser_type()))
                .coutTotal(coutTotal)
                .dateCreation(LocalDateTime.now())
                .dateModification(LocalDateTime.now())
                .user(user)
                .salle(salle)
                .build();

        reservation = reservationRepository.save(reservation);

        return ReservationMapper.toDto(reservation);
    }
    public List<ReservationDTO> getAll() {
        return reservationRepository.findAll()
                .stream()
                .map(ReservationMapper::toDto)
                .collect(Collectors.toList());
    }


}
