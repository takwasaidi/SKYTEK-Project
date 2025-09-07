package tn.esprit.backend.controller;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.backend.dto.reclamation.ReclamationRequest;
import tn.esprit.backend.entity.Reclamation;
import tn.esprit.backend.entity.ReclamationFichier;
import tn.esprit.backend.entity.User;
import tn.esprit.backend.mappers.ReclamationMapper;
import tn.esprit.backend.service.ReclamationService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/reclamation")
@RequiredArgsConstructor
public class ReclamationController {
    public final ReclamationService reclamationService;

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<ReclamationRequest> addReclamation(
            @RequestParam String sujet,
            @RequestParam String description,
            @RequestParam(required = false) List<MultipartFile> fichiers
    ) throws IOException {

        // Récupérer l'utilisateur connecté
        User loggedInUser = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        // Créer la réclamation
        Reclamation rec = reclamationService.addReclamation(
                sujet,
                description,
                loggedInUser.getId(),
                fichiers
        );

        // Mapper en DTO pour la réponse
        ReclamationRequest recDto = ReclamationMapper.mapToDTO(rec);

        return ResponseEntity.ok(recDto);
    }

@GetMapping
    public List<ReclamationRequest> getAll() {
        List<Reclamation> recs = reclamationService.getAll();
        return recs.stream()
                .map(ReclamationMapper::mapToDTO)
                .toList();
    }
    @GetMapping("/allByUser")
    public List<ReclamationRequest> getAllByUser() {
        // Récupérer l'utilisateur connecté
        User loggedInUser = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        List<Reclamation> recs = reclamationService.getAllByUser(loggedInUser.getId());
        return recs.stream()
                .map(ReclamationMapper::mapToDTO)
                .toList();
    }

    // Télécharger un fichier par son id
    @GetMapping("/files/{id}")
    public ResponseEntity<Resource> getFileById(@PathVariable Integer id) {
        // Récupérer le fichier depuis la base de données ou le système de fichiers
        ReclamationFichier fichier = reclamationService.getFileById(id);

        if (fichier == null) {
            return ResponseEntity.notFound().build();
        }

        // Convertir le contenu en Resource
        ByteArrayResource resource = new ByteArrayResource(fichier.getContenu());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fichier.getNomFichier() + "\"")
                .contentType(MediaType.parseMediaType(fichier.getTypeMime()))
                .contentLength(fichier.getContenu().length)
                .body(resource);
    }

    // Supprimer / annuler une réclamation
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelReclamation(@PathVariable Integer id) {
        boolean deleted = reclamationService.cancelReclamation(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
