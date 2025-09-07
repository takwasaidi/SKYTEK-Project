package tn.esprit.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.backend.dto.equipment.EquipmentDto;
import tn.esprit.backend.dto.salle.SalleDto;
import tn.esprit.backend.dto.salle.SallePopulaireDTO;
import tn.esprit.backend.dto.salle.SalleSearchDTO;
import tn.esprit.backend.entity.Equipment;
import tn.esprit.backend.entity.ImageModel;
import tn.esprit.backend.entity.Salle;
import tn.esprit.backend.mappers.SalleMapper;
import tn.esprit.backend.repository.EquipmentRepository;
import tn.esprit.backend.service.SalleService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/salle")
@RequiredArgsConstructor
public class SalleController {
    private final SalleService salleService;
    private final EquipmentRepository equipmentRepository;


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Salle> create(@RequestPart("salle") Salle salle,
                                        @RequestPart(value = "imagesFile", required = false) MultipartFile[] files ,
                                        @RequestParam("equipmentIds") List<Integer> equipmentIds) {
        try {
            // Récupération des équipements à partir des IDs
            List<Equipment> equipments = equipmentRepository.findAllById(equipmentIds);
            salle.setEquipments(equipments);
            if (files != null && files.length > 0) {
                Set<ImageModel> images = uploadImage(files);
                salle.setSalleImages(images);
            }
            Salle savedSalle = salleService.save(salle);
            return ResponseEntity.ok(savedSalle);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    public Set<ImageModel> uploadImage(MultipartFile[] multipartFiles) throws IOException {
        Set<ImageModel> imageModels = new HashSet<>();
        for(MultipartFile file: multipartFiles){
            ImageModel imageModel = new ImageModel(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes()
            );
            imageModels.add(imageModel);
        }
        return imageModels;
    }
    @GetMapping("/disponibilites")
    public List<LocalTime> getDisponibilites(@RequestParam Integer salleId, @RequestParam String date) {
        LocalDate localDate = LocalDate.parse(date);
        return salleService.getCreneauxDisponibles(salleId, localDate);
    }
    @GetMapping("/{salleId}/disponibilites-mois")
    public List<DisponibiliteJourDTO> getDisponibilitesMois(
            @PathVariable Integer salleId,
            @RequestParam int year,
            @RequestParam int month) {

        YearMonth ym = YearMonth.of(year, month);
        List<DisponibiliteJourDTO> result = new ArrayList<>();

        for (int day = 1; day <= ym.lengthOfMonth(); day++) {
            LocalDate date = ym.atDay(day);
            List<LocalTime> creneaux = salleService.getCreneauxDisponibles(salleId, date);
            // DTO simple
            result.add(new DisponibiliteJourDTO(date, creneaux));
        }

        return result;
    }


    @GetMapping
    public ResponseEntity<List<SalleDto>> findAll() {
        return ResponseEntity.ok(salleService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalleDto> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(salleService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SalleDto> updateSalle(
            @PathVariable Integer id,
            @RequestBody SalleDto salleDto
    ) {
        return salleService.findSalleById(id)
                .map(existingSalle -> {
                    // 🔹 mettre à jour les champs simples
                    existingSalle.setNom(salleDto.getNom());
                    existingSalle.setCapacite(salleDto.getCapacite());
                    existingSalle.setTarifHoraire(salleDto.getTarifHoraire());
                    existingSalle.setEmplacement(salleDto.getEmplacement());
                    existingSalle.setTitre(salleDto.getTitre());
                    existingSalle.setDescription(salleDto.getDescription());
                    existingSalle.setEstDisponible(salleDto.isEstDisponible());
                    existingSalle.setEnMaitenance(salleDto.isEnMaitenance());

                    // ⚠️ si tu veux gérer aussi les équipements / images, tu peux le faire ici
                    // Exemple pour équipements si dans SalleDto tu as une liste d'IDs :
                /*
                List<Equipment> equipments = equipmentRepository.findAllById(salleDto.getEquipmentIds());
                existingSalle.setEquipments(equipments);
                */

                    Salle updated = salleService.save(existingSalle);

                    // 🔹 renvoyer en DTO
                    return ResponseEntity.ok(SalleMapper.toDto(updated));
                })
                .orElseThrow(() -> new RuntimeException("Salle not found"));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        salleService.delete(id);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/search")
    public ResponseEntity<List<SalleDto>> searchAvailableSalles(@RequestBody SalleSearchDTO searchDTO) {
        List<SalleDto> sallesDisponibles = salleService.findAvailableSalles(
                searchDTO.getDate(),
                searchDTO.getHeureDebut(),
                searchDTO.getHeureFin()
        );

        return ResponseEntity.ok(sallesDisponibles);
    }
    // DTO
    public static class DisponibiliteJourDTO {
        private LocalDate date;
        private List<LocalTime> heures;

        public DisponibiliteJourDTO(LocalDate date, List<LocalTime> heures) {
            this.date = date;
            this.heures = heures;
        }

        public LocalDate getDate() {
            return date;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }

        public List<LocalTime> getHeures() {
            return heures;
        }

        public void setHeures(List<LocalTime> heures) {
            this.heures = heures;
        }
    }
    @GetMapping("/salles-populaires/{entrepriseId}")
    public ResponseEntity<List<SallePopulaireDTO>> getSallesPopulaires(
            @PathVariable Integer entrepriseId) {
        List<SallePopulaireDTO> sallesPopulaires = salleService.getSallesPopulaires(entrepriseId);
        return ResponseEntity.ok(sallesPopulaires);
    }
}
