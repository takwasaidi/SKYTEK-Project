package tn.esprit.backend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserQuotaDTO {
    private String firstname;
    private String lastname;
    private int utilise; // heures utilisées
    private int quota;   // quota max

}
