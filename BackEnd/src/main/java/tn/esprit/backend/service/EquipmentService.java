package tn.esprit.backend.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.backend.dto.equipment.EquipmentDto;
import tn.esprit.backend.entity.Equipment;
import tn.esprit.backend.entity.Salle;
import tn.esprit.backend.mappers.EquipmentMapper;
import tn.esprit.backend.repository.EquipmentRepository;
import tn.esprit.backend.repository.SalleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EquipmentService {
    private final EquipmentRepository equipmentRepository;
    private final SalleRepository salleRepository;
    public List<EquipmentDto> getAllEquipments() {
        return equipmentRepository.findAll().stream()
                .map(EquipmentMapper::toDto)
                .collect(Collectors.toList());
    }


    public EquipmentDto getEquipmentById(Integer id) {
        return EquipmentMapper.toDto(
                equipmentRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Not found"))
        );
    }


    public EquipmentDto addEquipment(EquipmentDto dto) {
        List<Salle> salles = dto.getSalleIds() != null
                ? salleRepository.findAllById(dto.getSalleIds())
                : new ArrayList<>();

        Equipment equipment = EquipmentMapper.toEntity(dto, salles);
        return EquipmentMapper.toDto(equipmentRepository.save(equipment));
    }


    public EquipmentDto updateEquipment(Integer id, EquipmentDto dto) {
        Equipment existing = equipmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Equipment not found"));

        existing.setNom(dto.getNom());  // Seul le nom est mis à jour

        // On ignore dto.getSalleIds()
        return EquipmentMapper.toDto(equipmentRepository.save(existing));
    }



    public void deleteEquipment(Integer id) {
        equipmentRepository.deleteById(id);
    }
}
