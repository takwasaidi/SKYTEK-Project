package tn.esprit.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tn.esprit.backend.dto.Reservation.ReservationInfoDto;
import tn.esprit.backend.dto.entreprise.QuotaStatsDTO;
import tn.esprit.backend.entity.Entreprise;
import tn.esprit.backend.entity.Quota;
import tn.esprit.backend.repository.EntrepriseRepository;
import tn.esprit.backend.repository.QuotaRepository;
import tn.esprit.backend.service.QuotaService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/quota")
@RequiredArgsConstructor
public class QuotaController {
    private final QuotaService quotaService;

    @GetMapping
    public List<QuotaStatsDTO> getAllQuotas() {
        return quotaService.listAll();
    }

    @PutMapping("/{entrepriseId}")
    public QuotaStatsDTO updateQuota(@PathVariable Integer entrepriseId,
                                          @RequestParam("quota") int quota) {
        return quotaService.updateQuota(entrepriseId, quota);
    }

    @PostMapping("/{entrepriseId}/reset")
    public QuotaStatsDTO resetUsage(@PathVariable Integer entrepriseId) {
        return quotaService.resetUsage(entrepriseId);
    }
    @GetMapping("/{entrepriseId}/reservations")
    public List<ReservationInfoDto> getReservations(@PathVariable Integer entrepriseId) {
        return quotaService.getReservationsByEntreprise(entrepriseId);
    }


}
