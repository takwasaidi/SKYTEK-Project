package tn.esprit.backend.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.backend.dto.reclamation.ReclamationRequest;
import tn.esprit.backend.entity.Reclamation;
import tn.esprit.backend.entity.ReclamationFichier;
import tn.esprit.backend.entity.User;
import tn.esprit.backend.repository.ReclamationFichierRepository;
import tn.esprit.backend.repository.ReclamationRepository;
import tn.esprit.backend.repository.UserRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReclamationService {
    public final ReclamationRepository reclamationRepository;
    private final ReclamationFichierRepository fichierRepository;
    private final UserRepository userRepository ;
    private final NotificationService notificationService;


    // Ajouter une réclamation avec fichiers
    public Reclamation addReclamation(String sujet,
                                      String description,
                                      Integer userId,
                                      List<MultipartFile> fichiers
    ) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        Reclamation rec = new Reclamation();
        rec.setSujet(sujet);
        rec.setDescription(description);
        rec.setUser(user);

        // sauvegarde initiale
        rec = reclamationRepository.save(rec);

        // sauvegarder les fichiers si présents
        if (fichiers != null && !fichiers.isEmpty()) {
            for (MultipartFile file : fichiers) {
                ReclamationFichier rf = new ReclamationFichier();
                rf.setNomFichier(file.getOriginalFilename());
                rf.setTypeMime(file.getContentType());
                rf.setContenu(file.getBytes());
                rf.setReclamation(rec);

                fichierRepository.save(rf);
                rec.getFichiers().add(rf);
                String msg = "Votre réclamtion est bien récu ";
                notificationService.notifyNewReservation(msg);

            }
        }

        return rec;
    }

    // Récupérer toutes les réclamations
    public List<Reclamation> getAll() {
        return reclamationRepository.findAll();
    }
    // Récupérer toutes les réclamations
    public List<Reclamation> getAllByUser(Integer userId) {
        return reclamationRepository.findByUserId(userId);
    }

    // Récupérer une réclamation spécifique
    public Optional<Reclamation> getById(Integer id) {
        return reclamationRepository.findById(id);
    }
    public ReclamationFichier getFileById(Integer id) {
        return fichierRepository.findById(id).orElse(null);
    }
    // Annuler une réclamation
    public boolean cancelReclamation(Integer id) {
        if (reclamationRepository.existsById(id)) {
            reclamationRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
