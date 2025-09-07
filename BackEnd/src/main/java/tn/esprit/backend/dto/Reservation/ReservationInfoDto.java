package tn.esprit.backend.dto.Reservation;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class ReservationInfoDto {
    private Integer reservationId;
    private String salleNom;
    private double salleTarifHoraire;
    private LocalDate dateReservation;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private int dureeHeures;
    private boolean estFacturable;
    private double coutTotal;
    public ReservationInfoDto() {

    }
}

