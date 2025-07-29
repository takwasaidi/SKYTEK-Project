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
    public AuthenticationResponse register(RegisterRequest request) {
        String email = request.getEmail();
        String domain = email.substring(email.indexOf("@") + 1);

        User.UserBuilder userBuilder = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .phone(request.getPhone())
                .email(email)
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
    public AuthenticationResponse login(AuthenticationRequest request) {
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

    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // check if the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        // check if the two new passwords are the same
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }

        // update the password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // save the new password
        userRepository.save(user);
    }
//    public AuthenticationResponse verifyCode(
//            VerificationRequest verificationRequest
//    ) {
//        User user = userRepository
//                .findByEmail(verificationRequest.getEmail())
//                .orElseThrow(() -> new EntityNotFoundException(
//                        String.format("No user found with %S", verificationRequest.getEmail()))
//                );
//
//        var jwtToken = jwtService.generateToken(user);
//        return AuthenticationResponse.builder()
//                .accessToken(jwtToken)
//                .build();
//    }

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

        if (!tfaService.isOtpValid(user.getSecret(), verificationRequest.getCode())) {
            throw new BadCredentialsException("Code is not correct");
        }


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

}
