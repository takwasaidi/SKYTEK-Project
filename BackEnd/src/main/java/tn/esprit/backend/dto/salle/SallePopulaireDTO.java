package tn.esprit.backend.dto.salle;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SallePopulaireDTO {
    private String nom;
    private int reservations;


}
