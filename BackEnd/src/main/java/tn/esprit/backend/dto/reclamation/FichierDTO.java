package tn.esprit.backend.dto.reclamation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FichierDTO {
    private Integer id;
    private String nomFichier;
    private String typeMime;


}
