package tn.esprit.backend.dto.salle;

import lombok.Builder;
import lombok.Data;
import tn.esprit.backend.entity.ImageModel;

import java.util.List;
import java.util.Set;


@Data
@Builder
public class SalleDto {
    private Integer id;
    private String nom;
    private Integer capacite;
    private Double tarifHoraire;
    private String emplacement;
    private String titre;
    private String description;
    private boolean estDisponible;
    private boolean enMaitenance;

    private List<Integer> equipmentIds;
    private Set<ImageModel> salleImages;
}
