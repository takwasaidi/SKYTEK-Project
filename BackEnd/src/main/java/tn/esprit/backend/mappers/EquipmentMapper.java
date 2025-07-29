package tn.esprit.backend.mappers;

import tn.esprit.backend.dto.equipment.EquipmentDto;
import tn.esprit.backend.entity.Equipment;
import tn.esprit.backend.entity.Salle;

import java.util.List;
import java.util.stream.Collectors;

public class EquipmentMapper {
    public static EquipmentDto toDto(Equipment equipment) {
        return EquipmentDto.builder()
                .id(equipment.getId())
                .nom(equipment.getNom())
                .salleIds(
                        equipment.getSalles() != null ?
                                equipment.getSalles().stream().map(Salle::getId).collect(Collectors.toList()) :
                                List.of()
                )
                .build();
    }

    public static Equipment toEntity(EquipmentDto dto, List<Salle> salles) {
        return Equipment.builder()
                .id(dto.getId())
                .nom(dto.getNom())
                .salles(salles != null ? salles : List.of())
                .build();
    }
}
