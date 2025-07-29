package tn.esprit.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tn.esprit.backend.dto.user.ChangePasswordRequest;
import tn.esprit.backend.dto.user.ForgetPasswordRequest;
import tn.esprit.backend.entity.ForgetPassword;
import tn.esprit.backend.entity.User;
import tn.esprit.backend.repository.ForgetPasswordRepository;
import tn.esprit.backend.repository.UserRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class ForgetPasswordService {
    @Autowired
    private ForgetPasswordRepository forgetPasswordRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    public void verifyEmail(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("Please provide an valid email"));
        int otp = otpGenerate();
        ForgetPassword fp = ForgetPassword.builder()
                .otp(otp)
                .expirationTime(LocalDateTime.now().plusMinutes(5))
                .email(user.getEmail())
                .build();
        forgetPasswordRepository.save(fp);
        String emailContent = generateEmailContent(otp);
        emailService.sendEmail(user.getEmail(), "Réinitialisation de mot de passe", emailContent, true);
    }
    public Integer otpGenerate() {
        Random random = new Random();
        return 100000 + random.nextInt(900000);
    }
    public void verifyOtp(Integer otp , String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("Please provide an valid email"));
        ForgetPassword fp = forgetPasswordRepository.findByOtpAndEmail(otp,user.getEmail())
                .orElseThrow(()-> new UsernameNotFoundException("Invalid OTP for email: " + user.getEmail()));
    if(fp.getExpirationTime().isBefore(LocalDateTime.now())){
         forgetPasswordRepository.deleteById(fp.getId());
        throw new IllegalStateException("OTP has expired");
    }
    }
    public String changePassword(ForgetPasswordRequest request , String email){
        // check if the two new passwords are the same
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Les mots de passe ne correspondent pas.");
        }
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("Aucun utilisateur trouvé avec cet email."));

        // update the password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // save the new password
        userRepository.save(user);
        return "Mot de passe changé avec succès !";
    }
    private String generateEmailContent(int otp) {
        return "<div style='font-family: Arial, sans-serif; padding: 20px; color: #333;'>"
                + "<h2 style='color: #0056b3;'>Réinitialisation de votre mot de passe</h2>"
                + "<p>Bonjour,</p>"
                + "<p>Vous avez demandé la réinitialisation de votre mot de passe. Veuillez utiliser le code suivant :</p>"
                + "<h3 style='color: #d9534f;'>" + otp + "</h3>"
                + "<p>Ce code est valable pendant <strong>5 minutes</strong>.</p>"
                + "<br>"
                + "<p>Si vous n'avez pas fait cette demande, veuillez ignorer cet email.</p>"
                + "<br>"
                + "<p>Cordialement,</p>"
                + "<p><strong>L'équipe de support</strong></p>"
                + "</div>";
    }
}
