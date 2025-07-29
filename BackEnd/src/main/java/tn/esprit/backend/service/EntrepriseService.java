package tn.esprit.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.backend.entity.Entreprise;
import tn.esprit.backend.entity.Quota;
import tn.esprit.backend.repository.EntrepriseRepository;
import tn.esprit.backend.repository.QuotaRepository;

import java.util.List;
import java.util.Optional;
@Service
public class EntrepriseService {
    @Autowired
    private EntrepriseRepository entrepriseRepository;
    @Autowired
    private QuotaRepository quotaRepository;

    public List<Entreprise> getAllEntreprises() {
        return entrepriseRepository.findAll();
    }

    public Optional<Entreprise> getEntrepriseById(Integer id) {
        return entrepriseRepository.findById(id);
    }

    public Entreprise createEntreprise(Entreprise entreprise) {
        // Créer un nouveau quota unique pour cette entreprise
        Quota quota = new Quota();
        quota.setQuota(20); // valeur initiale par défaut
        quota.setQuotaUtilise(0);
        quota.setEntreprise(entreprise); // liaison inverse

        entreprise.setQuota(quota); // liaison directe

        // Sauvegarde d'abord l'entreprise (cascade possible si bien configuré)
        return entrepriseRepository.save(entreprise);
    }

    public Entreprise updateEntreprise(Integer id, Entreprise updatedEntreprise) {
        return entrepriseRepository.findById(id).map(e -> {
            e.setNom(updatedEntreprise.getNom());
            e.setEmail(updatedEntreprise.getEmail());
            e.setQuota(updatedEntreprise.getQuota());
            return entrepriseRepository.save(e);
        }).orElseThrow(() -> new RuntimeException("Entreprise not found with id " + id));
    }

    public void deleteEntreprise(Integer id) {
        entrepriseRepository.deleteById(id);
    }
}
