package tn.esprit.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.backend.dto.salle.SalleDto;
import tn.esprit.backend.service.SalleService;

import java.util.List;

@RestController
@RequestMapping("/api/salle")
@RequiredArgsConstructor
public class SalleController {
    private final SalleService salleService;
    @PostMapping
    public ResponseEntity<SalleDto> save(@RequestBody SalleDto dto) {
        return ResponseEntity.ok(salleService.save(dto));
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
