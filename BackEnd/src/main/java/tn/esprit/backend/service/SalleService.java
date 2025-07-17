package tn.esprit.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.backend.dto.salle.SalleDto;
import tn.esprit.backend.entity.Equipment;
import tn.esprit.backend.entity.Salle;
import tn.esprit.backend.mappers.SalleMapper;
import tn.esprit.backend.repository.EquipmentRepository;
import tn.esprit.backend.repository.SalleRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalleService {
    private final SalleRepository salleRepository;
    private final EquipmentRepository equipmentRepository;
    public SalleDto save(SalleDto dto) {
        List<Equipment> equipments = equipmentRepository.findAllById(dto.getEquipmentIds());
        Salle salle = SalleMapper.toEntity(dto, equipments);
        return SalleMapper.toDto(salleRepository.save(salle));
    }

    public List<SalleDto> findAll() {
        return salleRepository.findAll()
                .stream()
                .map(SalleMapper::toDto)
                .collect(Collectors.toList());
    }

    public SalleDto findById(Integer id) {
        return salleRepository.findById(id)
                .map(SalleMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Salle not found"));
    }

    public void delete(Integer id) {
        salleRepository.deleteById(id);
    }

    public SalleDto update(Integer id, SalleDto dto) {
        Salle existing = salleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Salle not found"));

        existing.setNom(dto.getNom());
        existing.setCapacite(dto.getCapacite());
        existing.setTarifHoraire(dto.getTarifHoraire());
        existing.setEmplacement(dto.getEmplacement());
        existing.setImagesUrls(dto.getImagesUrls());
        existing.setEstDisponible(dto.isEstDisponible());
        existing.setEnMaitenance(dto.isEnMaitenance());

        if (dto.getEquipmentIds() != null) {
            List<Equipment> equipments = equipmentRepository.findAllById(dto.getEquipmentIds());
            existing.setEquipments(equipments);
        }

        return SalleMapper.toDto(salleRepository.save(existing));
    }
}
