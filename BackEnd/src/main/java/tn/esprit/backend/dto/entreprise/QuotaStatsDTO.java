package tn.esprit.backend.dto.entreprise;

import lombok.AllArgsConstructor;
import lombok.Data;
import tn.esprit.backend.dto.Reservation.ReservationDTO;

import java.util.List;

@Data
@AllArgsConstructor
public class QuotaStatsDTO {
    private Integer entrepriseId;
    private String name;
    private String email;
    private int quota;          // quota total configuré
    private int quotaUtilise;   // heures utilisées
    private int quotaRestant;   // calculé = quota - quotaUtilise
    private boolean quotaDepasse; // quotaUtilise >= quota
    private double montantDepassement; // en euros ou autre
    private List<ReservationDTO> reservations; // si besoin dans la même requête

    public QuotaStatsDTO() {

    }
}
