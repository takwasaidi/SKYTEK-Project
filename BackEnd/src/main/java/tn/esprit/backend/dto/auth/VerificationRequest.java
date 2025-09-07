package tn.esprit.backend.dto.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VerificationRequest {
    private String email;
    private String code;
    private boolean rememberMe;
}
