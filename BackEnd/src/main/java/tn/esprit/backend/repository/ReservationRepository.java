package tn.esprit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.backend.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation,Integer> {
}
