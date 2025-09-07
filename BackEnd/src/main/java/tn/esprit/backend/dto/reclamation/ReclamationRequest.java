package tn.esprit.backend.dto.reclamation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.backend.dto.user.UserDTO;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReclamationRequest {
    private Integer id;
    private String sujet;
    private String description;
    private List<FichierDTO> fichiers;
    private UserDTO user;
}
