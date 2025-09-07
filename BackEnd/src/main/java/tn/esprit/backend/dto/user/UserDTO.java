package tn.esprit.backend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import tn.esprit.backend.dto.entreprise.EntrepriseDTO;

@Data
@Builder

public class UserDTO {
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String user_type;
    private EntrepriseDTO entreprise;

    public UserDTO(Integer id, String firstname, String lastname, String email, String phone, String user_type ,EntrepriseDTO entreprise) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
        this.user_type = user_type;
        this.entreprise = entreprise;
    }
    public UserDTO(Integer id, String firstname, String email) {
        this.id = id;
        this.firstname = firstname;

        this.email = email;
    }
}
