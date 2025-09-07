package tn.esprit.backend.dto.entreprise;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuotaDTO {
    private Integer id;
    private Integer quota;
    private Integer quotaUtilise;
}
