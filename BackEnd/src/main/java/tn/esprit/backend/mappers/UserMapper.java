package tn.esprit.backend.mappers;

import tn.esprit.backend.dto.salle.SalleDto;
import tn.esprit.backend.dto.user.UserDTO;
import tn.esprit.backend.entity.Equipment;
import tn.esprit.backend.entity.Salle;
import tn.esprit.backend.entity.User;

import java.util.stream.Collectors;

public class UserMapper {
    public static UserDTO toDto(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .user_type(user.getUser_type())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .phone(user.getPhone())
                .email(user.getEmail())
                .entreprise(EntrepriseMapper.toDto(user.getEntreprise()))
                .build();
    }
}
