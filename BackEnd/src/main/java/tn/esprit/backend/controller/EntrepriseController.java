package tn.esprit.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.backend.dto.entreprise.EntrepriseDTO;
import tn.esprit.backend.entity.Entreprise;
import tn.esprit.backend.entity.Quota;
import tn.esprit.backend.mappers.EntrepriseMapper;
import tn.esprit.backend.service.EntrepriseService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/entreprise")
public class EntrepriseController {

    @Autowired
    private EntrepriseService entrepriseService;

    @GetMapping
    public List<EntrepriseDTO> getAllEntreprises() {
        return entrepriseService.getAllEntreprises().stream()
                .map(EntrepriseMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public Entreprise getEntrepriseById(@PathVariable Integer id) {
        return entrepriseService.getEntrepriseById(id)
                .orElseThrow(() -> new RuntimeException("Entreprise not found with id " + id));
    }

    @PostMapping
    public Entreprise createEntreprise(@RequestBody Entreprise entreprise) {
        return entrepriseService.createEntreprise(entreprise);
    }

    @PutMapping("/{id}")
    public Entreprise updateEntreprise(@PathVariable Integer id, @RequestBody Entreprise entreprise) {
        return entrepriseService.updateEntreprise(id, entreprise);
    }

    @DeleteMapping("/{id}")
    public void deleteEntreprise(@PathVariable Integer id) {
        entrepriseService.deleteEntreprise(id);
    }
}
