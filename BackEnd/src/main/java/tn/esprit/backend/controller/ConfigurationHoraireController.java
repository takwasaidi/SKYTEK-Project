package tn.esprit.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.backend.entity.ConfigurationHoraire;
import tn.esprit.backend.repository.ConfigurationHoraireRepository;
import tn.esprit.backend.service.ConfigurationHoraireService;

import java.util.List;

@RestController
@RequestMapping("/api/configurations-horaires")
@RequiredArgsConstructor
public class ConfigurationHoraireController {
    private final ConfigurationHoraireService service;
    private final ConfigurationHoraireRepository repository;

    @GetMapping
    public List<ConfigurationHoraire> getAll() {
        return service.getAllConfigurations();
    }

    @PutMapping
    public ConfigurationHoraire update(@RequestBody ConfigurationHoraire config) {
        return service.updateConfiguration(config);
    }

    @PostMapping
    public ResponseEntity<?> saveConfig(@RequestBody ConfigurationHoraire config) {
        return ResponseEntity.ok(repository.save(config));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteConfig(@PathVariable Integer id) {
        repository.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
