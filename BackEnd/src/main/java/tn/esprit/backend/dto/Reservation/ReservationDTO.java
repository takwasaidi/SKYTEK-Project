package tn.esprit.backend.dto.Reservation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
public class ReservationDTO {
    private Integer id;
    private LocalDate dateReservation;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private boolean estFacturable;
    private Double coutTotal;
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private Integer userId;

    private Integer salleId;
    public ReservationDTO() {
    }
}
