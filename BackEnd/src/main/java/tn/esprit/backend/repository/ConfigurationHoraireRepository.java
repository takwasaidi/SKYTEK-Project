package tn.esprit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.backend.entity.ConfigurationHoraire;

import java.time.DayOfWeek;
import java.util.Optional;

public interface ConfigurationHoraireRepository extends JpaRepository<ConfigurationHoraire,Integer> {
    Optional<ConfigurationHoraire> findByJour(DayOfWeek jour);
}
