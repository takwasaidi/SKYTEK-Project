package tn.esprit.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.backend.dto.Reservation.QuotaAlertDTO;
import tn.esprit.backend.entity.QuotaAlert;
import tn.esprit.backend.service.QuotaAlertService;

import java.util.List;

@RestController
@RequestMapping("/api/quota-alerts")
public class QuotaAlertController {

    @Autowired
    private QuotaAlertService quotaAlertService;

    @GetMapping("/unread")
    public List<QuotaAlertDTO> getUnreadAlerts() {
        return quotaAlertService.getUnreadAlerts();
    }

    @PostMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        quotaAlertService.markAlertAsRead(id);
        return ResponseEntity.ok().build();
    }
    @GetMapping
    public List<QuotaAlertDTO> getAll() {
        return quotaAlertService.getAll();
    }
    // Optionnel: marquer toutes les notifications comme lues
    @PutMapping("/mark-all-as-read")
    public ResponseEntity<Void> markAllAsRead() {
        quotaAlertService.markAllAsRead();
        return ResponseEntity.ok().build();
    }

}
