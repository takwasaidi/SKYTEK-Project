package tn.esprit.backend.dto.salle;

import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
public class SalleDto {
    private Integer id;
    private String nom;
    private Integer capacite;
    private Double tarifHoraire;
    private String emplacement;
    private List<String> imagesUrls;
    private boolean estDisponible;
    private boolean enMaitenance;
    private List<Integer> equipmentIds;
}
