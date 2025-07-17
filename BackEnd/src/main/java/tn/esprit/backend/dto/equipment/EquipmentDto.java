package tn.esprit.backend.dto.equipment;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class EquipmentDto {
    private Integer id;
    private String nom;
    private List<Integer> salleIds;
}
