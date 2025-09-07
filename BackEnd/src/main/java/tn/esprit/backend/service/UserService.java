package tn.esprit.backend.service;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import tn.esprit.backend.dto.auth.AuthenticationResponse;
import tn.esprit.backend.dto.auth.RegisterRequest;
import tn.esprit.backend.dto.user.ChangePasswordRequest;
import tn.esprit.backend.dto.user.UserDTO;
import tn.esprit.backend.dto.user.UserQuotaDTO;
import tn.esprit.backend.entity.*;
import tn.esprit.backend.mappers.SalleMapper;
import tn.esprit.backend.mappers.UserMapper;
import tn.esprit.backend.repository.EntrepriseRepository;
import tn.esprit.backend.repository.ReservationRepository;
import tn.esprit.backend.repository.TokenRepository;
import tn.esprit.backend.repository.UserRepository;

import java.security.Principal;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntrepriseRepository entrepriseRepository;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final ReservationRepository reservationRepository;
    @Autowired
    private JavaMailSender mailSender;

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
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }
    public User getCurrentUser() {
        // Récupérer l'email de l'utilisateur connecté
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // Chercher l'utilisateur dans la base
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec l'email : " + email));
    }

    public AuthenticationResponse createUserFromAdmin(RegisterRequest request) {
        String email = request.getEmail();
        String domain = email.substring(email.indexOf("@") + 1);

        // Générer un mot de passe aléatoire si le champ est vide
        String rawPassword = request.getPassword();
        if (rawPassword == null || rawPassword.isBlank()) {
            rawPassword = generateRandomPassword(); // méthode à créer
        }

        User.UserBuilder userBuilder = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .phone(request.getPhone())
                .email(email)
                .mfaEnabled(true)
                .password(passwordEncoder.encode(rawPassword));

        // Associer une entreprise si le domaine correspond
        Entreprise matchingEntreprise = entrepriseRepository.findAll().stream()
                .filter(e -> {
                    String entrepriseDomain = e.getEmail().substring(e.getEmail().indexOf("@") + 1);
                    return domain.equalsIgnoreCase(entrepriseDomain);
                })
                .findFirst()
                .orElse(null);

        if (matchingEntreprise != null) {
            userBuilder.entreprise(matchingEntreprise);
            userBuilder.user_type("INTERN_USER");
            userBuilder.role(Role.INTERN_USER);
        } else {
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

        // ✅ Envoi de l’email d’invitation
        sendInvitationEmail(savedUser.getEmail(), rawPassword, savedUser.getFirstname());

        // Génération token (optionnel pour usage direct)
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void sendInvitationEmail(String toEmail, String password, String firstname) {
        String subject = "Bienvenue sur notre plateforme";
        String text = String.format("""
        Bonjour %s,

        Un compte vient d'être créé pour vous sur notre plateforme.

        Voici vos identifiants :
        Email : %s
        Mot de passe : %s

        Nous vous recommandons de vous connecter et de changer votre mot de passe dès que possible.

        Bienvenue à bord !
        """, firstname, toEmail, password);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }
    private String generateRandomPassword() {
        int length = 10;
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#";
        Random random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }

        return sb.toString();
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
    public List<UserQuotaDTO> getUsersOverQuota(Integer entrepriseId) {
        Entreprise entreprise = entrepriseRepository.findById(entrepriseId)
                .orElseThrow(() -> new RuntimeException("Entreprise non trouvée"));
        int quotaGlobal = entreprise.getQuota().getQuota();

        List<Reservation> reservations = reservationRepository.findByUserEntrepriseId(entrepriseId);

        Map<User, Integer> heuresParUtilisateur = new HashMap<>();
        for (Reservation r : reservations) {
            int heures = r.getHeureFin().getHour() - r.getHeureDebut().getHour();
            heuresParUtilisateur.put(
                    r.getUser(),
                    heuresParUtilisateur.getOrDefault(r.getUser(), 0) + heures
            );
        }

        int quotaParUtilisateur = quotaGlobal; // tu peux adapter

        return heuresParUtilisateur.entrySet().stream()
                .filter(entry -> entry.getValue() > quotaParUtilisateur)
                .map(entry -> new UserQuotaDTO(
                        entry.getKey().getFirstname(),
                        entry.getKey().getLastname(),
                        entry.getValue(),
                        quotaParUtilisateur
                ))
                .collect(Collectors.toList());
    }


}
