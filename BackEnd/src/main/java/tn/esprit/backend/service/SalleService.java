package tn.esprit.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.backend.dto.salle.SalleDto;
import tn.esprit.backend.entity.Equipment;
import tn.esprit.backend.entity.Salle;
import tn.esprit.backend.mappers.SalleMapper;
import tn.esprit.backend.repository.EquipmentRepository;
import tn.esprit.backend.repository.SalleRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalleService {
    private final SalleRepository salleRepository;
    private final EquipmentRepository equipmentRepository;


   public Salle save(Salle salle) {
        return salleRepository.save(salle);
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
        existing.setEstDisponible(dto.isEstDisponible());
        existing.setEnMaitenance(dto.isEnMaitenance());

        if (dto.getEquipmentIds() != null) {
            List<Equipment> equipments = equipmentRepository.findAllById(dto.getEquipmentIds());
            existing.setEquipments(equipments);
        }

        return SalleMapper.toDto(salleRepository.save(existing));
    }
}
