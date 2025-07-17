package tn.esprit.backend.controller.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import tn.esprit.backend.dto.user.ChangePasswordRequest;
import tn.esprit.backend.dto.user.ForgetPasswordRequest;
import tn.esprit.backend.entity.User;
import tn.esprit.backend.repository.UserRepository;
import tn.esprit.backend.service.ForgetPasswordService;

@RestController
@RequestMapping("/api/forgetPassword")
public class ForgetPasswordController {
    @Autowired
    private ForgetPasswordService forgetPasswordService;

    @PostMapping("/verifyMail/{email}")
    public ResponseEntity<?> verifyEmail(@PathVariable String email){
        forgetPasswordService.verifyEmail(email);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/verifyOTP/{otp}/{email}")
    public ResponseEntity<?> verifyOtp(@PathVariable Integer otp ,@PathVariable String email){
        forgetPasswordService.verifyOtp(otp, email);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/changePassword/{email}")
    public ResponseEntity<?> changePassword
            (@RequestBody ForgetPasswordRequest request,
              @PathVariable String email)
    {
        forgetPasswordService.changePassword(request, email);
        return ResponseEntity.ok().build();
    }

}
