package tn.esprit.backend.dto.salle;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
@Data
@AllArgsConstructor
public class SalleSearchDTO {
    private LocalDate date;
    private LocalTime heureDebut;
    private LocalTime heureFin;
}
