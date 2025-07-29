package tn.esprit.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.backend.dto.equipment.EquipmentDto;
import tn.esprit.backend.dto.salle.SalleDto;
import tn.esprit.backend.entity.Equipment;
import tn.esprit.backend.entity.ImageModel;
import tn.esprit.backend.entity.Salle;
import tn.esprit.backend.repository.EquipmentRepository;
import tn.esprit.backend.service.SalleService;

import java.io.IOException;
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




    @GetMapping
    public ResponseEntity<List<SalleDto>> findAll() {
        return ResponseEntity.ok(salleService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalleDto> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(salleService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SalleDto> update(@PathVariable Integer id, @RequestBody SalleDto dto) {
        return ResponseEntity.ok(salleService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        salleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
