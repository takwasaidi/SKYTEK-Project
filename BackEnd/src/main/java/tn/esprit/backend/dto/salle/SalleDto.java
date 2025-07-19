package tn.esprit.backend.dto.salle;

import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
public class SalleDto {
    private String nom;
    private Integer capacite;
    private Double tarifHoraire;
    private String emplacement;
    private boolean estDisponible;
    private boolean enMaitenance;
    private List<Integer> equipmentIds;
}
