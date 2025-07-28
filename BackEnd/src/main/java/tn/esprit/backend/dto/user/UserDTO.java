package tn.esprit.backend.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;

    public UserDTO(Integer id, String firstname, String lastname, String email, String phone, String user_type) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
        this.user_type = user_type;
    }

    private String user_type;
}
