package tn.esprit.backend.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ForgetPasswordRequest {
    private String newPassword;
    private String confirmationPassword;
}
