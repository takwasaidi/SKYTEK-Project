package tn.esprit.backend.mappers;

import tn.esprit.backend.dto.salle.SalleDto;
import tn.esprit.backend.entity.Equipment;
import tn.esprit.backend.entity.ImageModel;
import tn.esprit.backend.entity.Salle;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SalleMapper {


    public static SalleDto toDto(Salle salle) {
        return SalleDto.builder()
                .id(salle.getId())
                .nom(salle.getNom())
                .capacite(salle.getCapacite())
                .tarifHoraire(salle.getTarifHoraire())
                .emplacement(salle.getEmplacement())
                .estDisponible(salle.isEstDisponible())
                .enMaitenance(salle.isEnMaitenance())
                .titre(salle.getTitre())
                .description(salle.getDescription())
                .equipmentIds(
                        salle.getEquipments() != null ?
                                salle.getEquipments().stream()
                                        .map(Equipment::getId)
                                        .collect(Collectors.toList()) : null
                )
                .salleImages(salle.getSalleImages()) // si tu veux exposer les images directement
                .build();
    }

    public static Salle toEntity(SalleDto dto, List<Equipment> equipments, Set<ImageModel> salleImages) {
        return Salle.builder()
                .id(dto.getId()) // important si c’est pour update
                .nom(dto.getNom())
                .capacite(dto.getCapacite())
                .tarifHoraire(dto.getTarifHoraire())
                .emplacement(dto.getEmplacement())
                .estDisponible(dto.isEstDisponible())
                .enMaitenance(dto.isEnMaitenance())
                .equipments(equipments)
                .salleImages(salleImages)
                .build();
    }
}
