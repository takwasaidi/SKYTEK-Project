package tn.esprit.backend.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import tn.esprit.backend.entity.User;
import tn.esprit.backend.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class EmailVerificationService {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private UserRepository userRepository;


    public String generateAndSendCode(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("No user found with email: " + email));
        String code = String.valueOf(new Random().nextInt(900000) + 100000); // 6 digits
        // 2. Définir l'expiration (par exemple : 10 minutes)
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(10);
        // 3. Sauvegarder dans l'entité utilisateur
        user.setEmailVerificationCode(code);
        user.setCodeExpiration(expirationTime);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Votre code de vérification");
        message.setText("Voici votre code de vérification : " + code);
        userRepository.save(user);
        mailSender.send(message);
        return code;
    }


}
