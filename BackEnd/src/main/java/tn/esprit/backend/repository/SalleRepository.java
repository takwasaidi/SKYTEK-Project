package tn.esprit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.backend.entity.Salle;

public interface SalleRepository extends JpaRepository<Salle,Integer> {
}
