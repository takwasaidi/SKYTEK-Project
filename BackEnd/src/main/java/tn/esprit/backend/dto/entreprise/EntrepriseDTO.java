package tn.esprit.backend.dto.entreprise;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EntrepriseDTO {
    private Integer id;
    private String nom;
    private String email;
    private QuotaDTO quota;
}
