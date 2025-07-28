package tn.esprit.backend.mappers;

import tn.esprit.backend.dto.entreprise.EntrepriseDTO;
import tn.esprit.backend.entity.Entreprise;

public class EntrepriseMapper {
    public static EntrepriseDTO toDto(Entreprise entreprise) {
        return EntrepriseDTO.builder()
                .id(entreprise.getId())
                .nom(entreprise.getNom())
                .email(entreprise.getEmail())
                .quota(entreprise.getQuota() != null ? entreprise.getQuota().getQuota() : null)
                .build();
    }
}
