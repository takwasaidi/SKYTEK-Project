package tn.esprit.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDate dateReservation;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private boolean estFacturable;
    private Double coutTotal;
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;
  @ManyToOne
    private User user;
    @ManyToOne
    private Salle salle;
    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", dateReservation=" + dateReservation +
                ", heureDebut=" + heureDebut +
                ", heureFin=" + heureFin +
                ", estFacturable=" + estFacturable +
                ", coutTotal=" + coutTotal +
                ", user=" + (user != null ? user.getEmail() : "null") +
                ", salle=" + (salle != null ? salle.getNom() : "null") +
                '}';
    }

}
