package tn.esprit.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tn.esprit.backend.dto.auth.AuthenticationRequest;
import tn.esprit.backend.dto.auth.AuthenticationResponse;
import tn.esprit.backend.dto.auth.RegisterRequest;
import tn.esprit.backend.dto.auth.VerificationRequest;
import tn.esprit.backend.dto.user.ChangePasswordRequest;
import tn.esprit.backend.entity.*;
import tn.esprit.backend.repository.EntrepriseRepository;
import tn.esprit.backend.repository.TokenRepository;
import tn.esprit.backend.repository.UserRepository;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EntrepriseRepository entrepriseRepository;
    private final TwoFactorAuthenticationService tfaService;
    private final EmailVerificationService emailVerificationService;

    public AuthenticationResponse register(RegisterRequest request) {
        String email = request.getEmail();
        String domain = email.substring(email.indexOf("@") + 1);

        User.UserBuilder userBuilder = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .phone(request.getPhone())
                .email(email)
                .mfaEnabled(true)
                .password(passwordEncoder.encode(request.getPassword()));

        // Recherche d'une entreprise correspondant au domaine
        Entreprise matchingEntreprise = entrepriseRepository.findAll().stream()
                .filter(e -> {
                    String entrepriseDomain = e.getEmail().substring(e.getEmail().indexOf("@") + 1);
                    return domain.equalsIgnoreCase(entrepriseDomain);
                })
                .findFirst()
                .orElse(null);

        if (matchingEntreprise != null) {
            // Utilisateur interne
            userBuilder.entreprise(matchingEntreprise);
            userBuilder.user_type("INTERN_USER");
            userBuilder.role(Role.INTERN_USER);
        } else {
            // Utilisateur externe
            userBuilder.user_type("EXTERN_USER");
            userBuilder.role(Role.EXTERN_USER);

            if (request.getEntrepriseId() != null) {
                Entreprise entreprise = entrepriseRepository.findById(request.getEntrepriseId())
                        .orElseThrow(() -> new RuntimeException("Entreprise introuvable"));
                userBuilder.entreprise(entreprise);
            }
        }

        User user = userBuilder.build();
        User savedUser = userRepository.save(user);

        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }
    public AuthenticationResponse login(AuthenticationRequest request,String mfaType) {
        // Authentification classique (email + mot de passe)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        if (user.isMfaEnabled()) {
            if (Objects.equals(mfaType, "APP")) {
                user.setMfaType(MfaType.APP);
                // Si MFA est activé, mais secret non généré (première connexion après activation MFA)
                if (user.getSecret() == null || user.getSecret().isEmpty()) {
                    // Générer un nouveau secret MFA
                    String secret = tfaService.generateNewSecret();
                    user.setSecret(secret);
                    userRepository.save(user);

                    // Générer l'URI du QR code (image à afficher au frontend)
                    String qrCodeUri = tfaService.generateQrCodeImageUri(secret);

                    // Retourner la réponse avec flag mfaRequired = true et l'image QR
                    return AuthenticationResponse.builder()
                            .mfaRequired(true)
                            .secretImageUri(qrCodeUri)
                            .build();
                }

                // Sinon MFA activé, secret déjà généré => demande du code MFA sans générer à nouveau
                return AuthenticationResponse.builder()
                        .mfaRequired(true)
                        .build();
            }
            else if (Objects.equals(mfaType, "EMAIL")) {
                user.setMfaType(tn.esprit.backend.entity.MfaType.EMAIL);
                // Nouvelle logique : envoi d’un code par email
                emailVerificationService.generateAndSendCode(user.getEmail());

                return AuthenticationResponse.builder()
                        .mfaRequired(true)
                        .build();
            }
        }

        // MFA non activé : génération tokens classiques et renvoi
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .mfaRequired(false)
                .build();
    }
////teste ////
public AuthenticationResponse login1(AuthenticationRequest request) {
    authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
            )
    );

    var user = userRepository.findByEmail(request.getEmail())
            .orElseThrow();

    // ✅ Skip MFA if trusted period is still valid
    if (user.getMfaTrustedUntil() != null && user.getMfaTrustedUntil().isAfter(LocalDateTime.now())) {
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .mfaRequired(false)
                .build();
    }

    // ✅ If MFA is enabled but not trusted
    if (user.isMfaEnabled()) {
        return AuthenticationResponse.builder()
                .mfaRequired(true)
                .build();
    }

    // ✅ Normal login (MFA disabled)
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);

    return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .mfaRequired(false)
            .build();
}


    public ResponseEntity<?> initiateMfa(String email, String mfaType) {
        var user = userRepository.findByEmail(email)
                .orElseThrow();

        if (!user.isMfaEnabled()) {
            return ResponseEntity.badRequest().body("MFA is not enabled for this user.");
        }

        if ("APP".equalsIgnoreCase(mfaType)) {

            user.setMfaType(MfaType.APP);
            // Si MFA est activé, mais secret non généré (première connexion après activation MFA)
            if (user.getSecret() == null || user.getSecret().isEmpty()) {
                // Générer un nouveau secret MFA
                String secret = tfaService.generateNewSecret();
                user.setSecret(secret);
                userRepository.save(user);

                // Générer l'URI du QR code (image à afficher au frontend)
                String qrCodeUri = tfaService.generateQrCodeImageUri(secret);

                // Retourner la réponse avec flag mfaRequired = true et l'image QR
                return ResponseEntity.ok(
                        AuthenticationResponse.builder()
                        .mfaRequired(true)
                        .secretImageUri(qrCodeUri)
                        .build()
                );
            }
            userRepository.save(user);
            // Sinon MFA activé, secret déjà généré => demande du code MFA sans générer à nouveau
            return ResponseEntity.ok(
                    AuthenticationResponse.builder()
                    .mfaRequired(true)
                    .build()
            );
        }

        if ("EMAIL".equalsIgnoreCase(mfaType)) {
            user.setMfaType(MfaType.EMAIL);

            // Generate + send code
            String code = emailVerificationService.generateAndSendCode(user.getEmail());
            user.setEmailVerificationCode(code);
            user.setCodeExpiration(LocalDateTime.now().plusMinutes(5));
            userRepository.save(user);

            return ResponseEntity.ok(
                    AuthenticationResponse.builder()
                            .mfaRequired(true)
                            .build()
            );
        }

        return ResponseEntity.badRequest().body("Invalid MFA type");
    }
    public AuthenticationResponse verifyCode1(VerificationRequest verificationRequest) {
        User user = userRepository.findByEmail(verificationRequest.getEmail())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("No user found with email: %s", verificationRequest.getEmail())));

        // Vérifier si le code est correct selon le type MFA
        switch (user.getMfaType()) {
            case APP:
                if (!tfaService.isOtpValid(user.getSecret(), verificationRequest.getCode())) {
                    throw new BadCredentialsException("Code is not correct");
                }
                break;

            case EMAIL:
                if (user.getEmailVerificationCode() == null || user.getCodeExpiration() == null) {
                    throw new BadCredentialsException("No verification code found. Please request a new one.");
                }
                if (!user.getEmailVerificationCode().equals(verificationRequest.getCode())) {
                    throw new BadCredentialsException("Email code is incorrect");
                }
                if (user.getCodeExpiration().isBefore(LocalDateTime.now())) {
                    throw new BadCredentialsException("Email code has expired");
                }
                break;

            default:
                throw new IllegalStateException("Unsupported 2FA type");
        }

        // ✅ Appliquer "Remember Me" uniquement après validation MFA
        if (verificationRequest.isRememberMe()) {
            user.setMfaTrustedUntil(LocalDateTime.now().plusDays(3));
            userRepository.save(user);
        }

        // Générer les tokens
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .mfaRequired(false)  // Le MFA est validé, plus besoin de vérification
                .build();
    }




    //////tetset ///
    private void revokeAllUserTokens(User user){
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if(validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(t ->{
            t.setExpired(true);
            t.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();
        tokenRepository.save(token);
    }
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.userRepository.findByEmail(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }



    ///
    public String generateMfaSetup(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        GoogleAuthenticatorKey key = gAuth.createCredentials();

        user.setSecret(key.getKey());
        user.setMfaEnabled(true);
        userRepository.save(user);

        return GoogleAuthenticatorQRGenerator.getOtpAuthURL("MonAppSecure", user.getEmail(), key);
    }
    public boolean verifyMfaCode(String email, int code) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        if (!user.isMfaEnabled()) {
            throw new IllegalStateException("MFA non activée pour cet utilisateur.");
        }

        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        return gAuth.authorize(user.getSecret(), code);
    }

    public AuthenticationResponse verifyCode(VerificationRequest verificationRequest) {
        User user = userRepository.findByEmail(verificationRequest.getEmail())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("No user found with %s", verificationRequest.getEmail())));

        switch (user.getMfaType()) {
            case APP:
                if (!tfaService.isOtpValid(user.getSecret(), verificationRequest.getCode())) {
                    throw new BadCredentialsException("Code is not correct");
                }
                break;
            case EMAIL:
                if (user.getEmailVerificationCode() == null || user.getCodeExpiration() == null) {
                    throw new BadCredentialsException("No verification code found. Please request a new one.");
                }
                if (!user.getEmailVerificationCode().equals(verificationRequest.getCode())) {
                    throw new BadCredentialsException("Email code is incorrect");
                }
                if (user.getCodeExpiration().isBefore(LocalDateTime.now())) {
                    throw new BadCredentialsException("Email code has expired");
                }
                break;
                default:
                    throw new IllegalStateException("Unsupported 2FA type");

        }
//        if (!tfaService.isOtpValid(user.getSecret(), verificationRequest.getCode())) {
//            throw new BadCredentialsException("Code is not correct");
//        }


        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .mfaRequired(false)  // MFA est validé
                .build();
    }

    public String generateCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 6 chiffres
        return String.valueOf(code);
    }


}
