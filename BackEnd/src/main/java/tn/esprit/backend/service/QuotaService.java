package tn.esprit.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.backend.dto.Reservation.ReservationDTO;
import tn.esprit.backend.dto.Reservation.ReservationInfoDto;
import tn.esprit.backend.dto.entreprise.QuotaStatsDTO;
import tn.esprit.backend.entity.Entreprise;
import tn.esprit.backend.entity.Quota;
import tn.esprit.backend.entity.Reservation;
import tn.esprit.backend.mappers.ReservationMapper;
import tn.esprit.backend.repository.EntrepriseRepository;
import tn.esprit.backend.repository.QuotaRepository;
import tn.esprit.backend.repository.ReservationRepository;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuotaService {
    private final QuotaRepository quotaRepository;
    private final EntrepriseRepository entrepriseRepository;
    private final ReservationRepository reservationRepository;
    /**
     * Réinitialiser tous les quotas utilisés chaque 1er jour du mois à minuit.
     */
    @Scheduled(cron = "0 0 0 1 * ?") // à 00:00 le 1er de chaque mois
    public void resetQuotasMensuellement() {
        List<Quota> allQuotas = quotaRepository.findAll();
        for (Quota quota : allQuotas) {
            quota.setQuotaUtilise(0);
        }
        quotaRepository.saveAll(allQuotas);
        System.out.println("QuotaUtilise réinitialisé pour tous les quotas");
    }

    public List<QuotaStatsDTO> listAll() {
        return entrepriseRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private QuotaStatsDTO toDto(Entreprise e) {
        Quota q = e.getQuota();
        int quota = q != null ? q.getQuota() : 0;
        int used = q != null ? q.getQuotaUtilise() : 0;
        int rest = quota - used;
        boolean depasse = used > quota;

        double montantDepassement = 0.0;
        if (depasse) {
            int heuresDepasse = used - quota;
            double coutHoraireMoyen = calculerCoutHoraireMoyen(e.getId());
            montantDepassement = heuresDepasse * coutHoraireMoyen;
        }
        System.out.println(montantDepassement);
        QuotaStatsDTO dto = new QuotaStatsDTO();
        dto.setEntrepriseId(e.getId());
        dto.setName(e.getNom());
        dto.setEmail(e.getEmail());
        dto.setQuota(quota);
        dto.setQuotaUtilise(used);
        dto.setQuotaRestant(rest);
        dto.setQuotaDepasse(depasse);
        dto.setMontantDepassement(montantDepassement);
        return dto;
    }
    private double calculerCoutHoraireMoyen(Integer entrepriseId) {
        List<Reservation> reservations = reservationRepository.findByUserEntrepriseId(entrepriseId);

        double totalCout = 0.0;
        double totalHeures = 0.0;

        for (Reservation r : reservations) {
            if (r.isEstFacturable()) {
                System.out.println("oui");
                double dureeHeures = Duration.between(r.getHeureDebut(), r.getHeureFin()).toMinutes() / 60.0;
                totalCout += r.getCoutTotal();
                totalHeures += dureeHeures;
                System.out.println(totalCout);
                System.out.println(totalHeures);
            }
        }


        if (totalHeures == 0) {
            return 0.0; // Évite la division par zéro
        }

        return totalCout/totalHeures;
    }

    @Transactional
    public QuotaStatsDTO updateQuota(Integer entrepriseId, int newQuota) {
        Entreprise e = entrepriseRepository.findById(entrepriseId)
                .orElseThrow(() -> new RuntimeException("Entreprise introuvable"));
        Quota q = e.getQuota();
        if (q == null) {
            q = new Quota();
            q.setEntreprise(e);
        }
        q.setQuota(newQuota);
        // optionally do not change quotaUtilise
        quotaRepository.save(q);
        e.setQuota(q);
        entrepriseRepository.save(e);
        return toDto(e);
    }

    @Transactional
    public QuotaStatsDTO resetUsage(Integer entrepriseId) {
        Entreprise e = entrepriseRepository.findById(entrepriseId)
                .orElseThrow(() -> new RuntimeException("Entreprise introuvable"));
        Quota q = e.getQuota();
        if (q == null) throw new RuntimeException("Quota non configuré pour l'entreprise");
        q.setQuotaUtilise(0);
        quotaRepository.save(q);
        e.setQuota(q);
        entrepriseRepository.save(e);
        return toDto(e);
    }

    public List<ReservationInfoDto> getReservationsByEntreprise(Integer entrepriseId) {
        List<Reservation> reservations = reservationRepository
                .findByUserEntrepriseIdOrderByDateReservationDesc(entrepriseId);

        return reservations.stream()
                .map(ReservationMapper::toInfoDto)
                .collect(Collectors.toList());
    }


}
