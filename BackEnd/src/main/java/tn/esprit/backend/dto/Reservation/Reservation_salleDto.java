package tn.esprit.backend.dto.Reservation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import tn.esprit.backend.dto.salle.SalleDto;
import tn.esprit.backend.dto.user.UserDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
@Data
@AllArgsConstructor
@Builder
public class Reservation_salleDto {
    private Integer id;
    private LocalDate dateReservation;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private boolean estFacturable;
    private Double coutTotal;
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private UserDTO user;
    private SalleDto salle;
}
