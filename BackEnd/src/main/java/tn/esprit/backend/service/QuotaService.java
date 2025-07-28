package tn.esprit.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tn.esprit.backend.entity.Entreprise;
import tn.esprit.backend.entity.Quota;
import tn.esprit.backend.repository.EntrepriseRepository;
import tn.esprit.backend.repository.QuotaRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuotaService {
    private final QuotaRepository quotaRepository;
    private final EntrepriseRepository entrepriseRepository;

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
}
