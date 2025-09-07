package tn.esprit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.backend.entity.Salle;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface SalleRepository extends JpaRepository<Salle,Integer> {
    @Query("SELECT r.salle.id FROM Reservation r " +
            "WHERE r.dateReservation = :date " +
            "AND ((:heureDebut < r.heureFin) AND (:heureFin > r.heureDebut))")
    List<Integer> findOccupiedSalleIds(@Param("date") LocalDate date,
                                    @Param("heureDebut") LocalTime heureDebut,
                                    @Param("heureFin") LocalTime heureFin);

}
