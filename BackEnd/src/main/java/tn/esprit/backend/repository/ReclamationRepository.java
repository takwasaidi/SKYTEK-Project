package tn.esprit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.backend.entity.Reclamation;
import tn.esprit.backend.entity.Reservation;

import java.util.List;

public interface ReclamationRepository extends JpaRepository<Reclamation, Integer> {
    // Toutes les réservations d'un utilisateur donné ET pour une salle donnée
    List<Reclamation> findByUserId(Integer userId);
}
