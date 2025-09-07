package tn.esprit.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.backend.dto.Reservation.QuotaAlertDTO;
import tn.esprit.backend.entity.Entreprise;
import tn.esprit.backend.entity.QuotaAlert;
import tn.esprit.backend.mappers.EntrepriseMapper;
import tn.esprit.backend.mappers.ReservationMapper;
import tn.esprit.backend.repository.QuotaAlertRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class QuotaAlertService {

    @Autowired
    private QuotaAlertRepository quotaAlertRepository;

    public QuotaAlert createAlert(Entreprise entreprise, String type) {
        QuotaAlert alert = new QuotaAlert();
        alert.setEntreprise(entreprise);
        alert.setType(type);
        alert.setDateAlert(LocalDateTime.now());
        alert.setLu(false);
        return quotaAlertRepository.save(alert);
    }


    public List<QuotaAlertDTO> getUnreadAlerts() {
        List<QuotaAlert> alerts = quotaAlertRepository.findByLuFalse();
        return alerts.stream()
                .map(this::mapQuotaAlertToDto)
                .toList();
    }
    public List<QuotaAlertDTO> getAll() {
        return quotaAlertRepository.findAll().stream()
                .map(this::mapQuotaAlertToDto)
                .toList();
    }


    public void markAlertAsRead(Long alertId) {
        QuotaAlert alert = quotaAlertRepository.findById(alertId)
                .orElseThrow(() -> new RuntimeException("Alert not found"));
        alert.setLu(true);
        quotaAlertRepository.save(alert);
    }
    @Transactional
    public void markAllAsRead() {
        quotaAlertRepository.updateAllAsRead();
    }

    private QuotaAlertDTO mapQuotaAlertToDto(QuotaAlert alert) {
        QuotaAlertDTO dto = new QuotaAlertDTO();
        dto.setId(alert.getId());
        dto.setType(alert.getType());
        dto.setDateAlert(alert.getDateAlert());
        dto.setLu(alert.isLu());
        dto.setEntreprise(EntrepriseMapper.toDto(alert.getEntreprise()));
        return dto;
    }
}

