package tn.esprit.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.backend.dto.equipment.EquipmentDto;
import tn.esprit.backend.service.EquipmentService;

import java.util.List;

@RestController
@RequestMapping("/api/equipment")
@RequiredArgsConstructor
public class EquipmentController {
    private final EquipmentService equipmentService;
    @GetMapping
    public ResponseEntity<List<EquipmentDto>> getAll() {
        return ResponseEntity.ok(equipmentService.getAllEquipments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipmentDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(equipmentService.getEquipmentById(id));
    }

    @PostMapping
    public ResponseEntity<EquipmentDto> create(@RequestBody EquipmentDto dto) {
        return ResponseEntity.ok(equipmentService.addEquipment(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipmentDto> update(@PathVariable Integer id, @RequestBody EquipmentDto dto) {
        return ResponseEntity.ok(equipmentService.updateEquipment(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        equipmentService.deleteEquipment(id);
        return ResponseEntity.noContent().build();
    }
}
