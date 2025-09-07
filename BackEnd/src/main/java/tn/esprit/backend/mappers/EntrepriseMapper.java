package tn.esprit.backend.mappers;

import tn.esprit.backend.dto.entreprise.EntrepriseDTO;
import tn.esprit.backend.dto.entreprise.QuotaDTO;
import tn.esprit.backend.entity.Entreprise;

public class EntrepriseMapper {
    public static EntrepriseDTO toDto(Entreprise entreprise) {
        if (entreprise == null) {
            return null;
        }

        QuotaDTO quotaDto = null;
        if (entreprise.getQuota() != null) {
            quotaDto = QuotaDTO.builder()
                    .id(entreprise.getQuota().getId())
                    .quota(entreprise.getQuota().getQuota())
                    .quotaUtilise(entreprise.getQuota().getQuotaUtilise())
                    .build();
        }

        return EntrepriseDTO.builder()
                .id(entreprise.getId())
                .nom(entreprise.getNom())
                .email(entreprise.getEmail())
                .quota(quotaDto)
                .build();
    }

}
