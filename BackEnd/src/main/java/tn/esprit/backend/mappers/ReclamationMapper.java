package tn.esprit.backend.mappers;

import tn.esprit.backend.dto.reclamation.FichierDTO;
import tn.esprit.backend.dto.reclamation.ReclamationRequest;
import tn.esprit.backend.dto.user.UserDTO;
import tn.esprit.backend.entity.Reclamation;

import java.util.List;

public class ReclamationMapper {

    public static ReclamationRequest mapToDTO(Reclamation rec) {
        ReclamationRequest dto = new ReclamationRequest();
        dto.setId(rec.getId());
        dto.setSujet(rec.getSujet());
        dto.setDescription(rec.getDescription());

        // Appel correct de UserMapper
        dto.setUser(UserMapper.toDto(rec.getUser()));

        // Mapper les fichiers
        List<FichierDTO> fichiers = rec.getFichiers().stream()
                .map(f -> new FichierDTO(f.getId(),f.getNomFichier(), f.getTypeMime()))
                .toList();
        dto.setFichiers(fichiers);

        return dto;
    }


}
