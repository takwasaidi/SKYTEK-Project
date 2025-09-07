package tn.esprit.backend.dto.Reservation;

import lombok.AllArgsConstructor;
import lombok.Data;
import tn.esprit.backend.dto.entreprise.EntrepriseDTO;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
public class QuotaAlertDTO {
    private Long id;
    private String type;
    private LocalDateTime dateAlert;
    private EntrepriseDTO entreprise;
    private boolean lu;

    public QuotaAlertDTO() {

    }
}
