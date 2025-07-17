package tn.esprit.backend.mappers;

import tn.esprit.backend.dto.salle.SalleDto;
import tn.esprit.backend.entity.Equipment;
import tn.esprit.backend.entity.Salle;

import java.util.List;
import java.util.stream.Collectors;

public class SalleMapper {

    public static SalleDto toDto(Salle salle) {
        return SalleDto.builder()
                .id(salle.getId())
                .nom(salle.getNom())
                .capacite(salle.getCapacite())
                .tarifHoraire(salle.getTarifHoraire())
                .emplacement(salle.getEmplacement())
                .imagesUrls(salle.getImagesUrls())
                .estDisponible(salle.isEstDisponible())
                .enMaitenance(salle.isEnMaitenance())
                .equipmentIds(
                        salle.getEquipments() != null ?
                                salle.getEquipments().stream()
                                        .map(Equipment::getId)
                                        .collect(Collectors.toList()) : null
                )
                .build();
    }

    public static Salle toEntity(SalleDto dto, List<Equipment> equipments) {
        return Salle.builder()
                .id(dto.getId())
                .nom(dto.getNom())
                .capacite(dto.getCapacite())
                .tarifHoraire(dto.getTarifHoraire())
                .emplacement(dto.getEmplacement())
                .imagesUrls(dto.getImagesUrls())
                .estDisponible(dto.isEstDisponible())
                .enMaitenance(dto.isEnMaitenance())
                .equipments(equipments)
                .build();
    }
}
