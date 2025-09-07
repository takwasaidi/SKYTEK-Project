package tn.esprit.backend.controller.auth;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.backend.dto.auth.AuthenticationRequest;
import tn.esprit.backend.dto.auth.AuthenticationResponse;
import tn.esprit.backend.dto.auth.RegisterRequest;
import tn.esprit.backend.dto.auth.VerificationRequest;
import tn.esprit.backend.entity.User;
import tn.esprit.backend.repository.UserRepository;
import tn.esprit.backend.service.AuthService;
import tn.esprit.backend.service.JwtService;
import tn.esprit.backend.service.UserService;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthService authService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    @PostMapping("/login/{MfaType}")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request,@PathVariable("MfaType") String MfaType) {
        AuthenticationResponse response = authService.login(request,MfaType);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
       authService.refreshToken(request,response);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
     AuthenticationResponse response = authService.register(request);

        return ResponseEntity.ok(response);
    }

//    @PostMapping("/verify")
//    public ResponseEntity<?> verifyCode(
//            @RequestBody VerificationRequest verificationRequest
//    ) {
//        return ResponseEntity.ok(userService.verifyCode(verificationRequest));
//    }
@PostMapping("/mfa/setup")
public ResponseEntity<String> setupMfa(@RequestParam String email) {
    String otpAuthUrl = authService.generateMfaSetup(email);
    return ResponseEntity.ok(otpAuthUrl);
}
//    @PostMapping("/mfa/verify")
//    public ResponseEntity<?> verifyMfa(@RequestBody VerificationRequest request) {
//        boolean valid = userService.verifyMfaCode(request.getEmail(), request.getCode());
//
//        if (!valid) {
//            return ResponseEntity.status(401).body("Code MFA invalide");
//        }
//
//        // Le code est valide → retourne un JWT
//        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
//        String token = jwtService.generateToken(user);
//        return ResponseEntity.ok(AuthenticationResponse.builder().accessToken(token).build());
//    }
@PostMapping("/verify")
public ResponseEntity<?> verifyCode(
        @RequestBody VerificationRequest verificationRequest
) {
    return ResponseEntity.ok(authService.verifyCode(verificationRequest));
}


//testtt//
@PostMapping("/login1")
public ResponseEntity<AuthenticationResponse> login1(@RequestBody AuthenticationRequest request) {
    AuthenticationResponse response = authService.login1(request);
    return ResponseEntity.ok(response);
}
    @PostMapping("/initiate-mfa")
    public ResponseEntity<?> initiateMfa(
            @RequestParam String email,
            @RequestParam String mfaType
    ) {
        return authService.initiateMfa(email, mfaType);
    }
    @PostMapping("/verify-mfa")
    public ResponseEntity<AuthenticationResponse> verifyMfa(@RequestBody VerificationRequest request) {
        AuthenticationResponse response = authService.verifyCode1(request);
        return ResponseEntity.ok(response);
    }


    /////



}
