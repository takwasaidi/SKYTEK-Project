package tn.esprit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.backend.entity.Reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation,Integer> {
    boolean existsBySalleIdAndDateReservationAndHeureDebutLessThanAndHeureFinGreaterThan(
            Integer salleId, LocalDate date, LocalTime heureFin, LocalTime heureDebut);

    // Trouve toutes les réservations des utilisateurs appartenant à une entreprise donnée
    //List<Reservation> findByUserEntrepriseId(Integer entrepriseId);

    // Trouve toutes les réservations d'un utilisateur donné
    List<Reservation> findByUserId(Integer userId);
    // Toutes les réservations d'une salle donnée (par son Id)
    List<Reservation> findBySalleId(Integer salleId);

    // Toutes les réservations des utilisateurs appartenant à une entreprise donnée ET pour une salle donnée
    List<Reservation> findBySalleIdAndUserEntrepriseId(Integer salleId, Integer entrepriseId);

    // Toutes les réservations d'un utilisateur donné ET pour une salle donnée
    List<Reservation> findBySalleIdAndUserId(Integer salleId, Integer userId);

    List<Reservation> findBySalleIdAndDateReservation(Integer salleId,LocalDate date);

    List<Reservation> findByUserEntrepriseId(Integer entrepriseId);
    List<Reservation> findByUserEntrepriseIdOrderByDateReservationDesc(Integer entrepriseId);
    int countBySalleId(Integer salleId);



}
