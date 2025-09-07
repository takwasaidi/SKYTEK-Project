package tn.esprit.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.backend.entity.ConfigurationHoraire;
import tn.esprit.backend.repository.ConfigurationHoraireRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConfigurationHoraireService {
    private final ConfigurationHoraireRepository repository;

    public List<ConfigurationHoraire> getAllConfigurations() {
        return repository.findAll();
    }
    public ConfigurationHoraire updateConfiguration(ConfigurationHoraire config) {
        return repository.save(config);
    }

}
