package tn.esprit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.backend.entity.ReclamationFichier;

public interface ReclamationFichierRepository extends JpaRepository<ReclamationFichier, Integer> {
}
