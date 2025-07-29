package tn.esprit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.backend.entity.ForgetPassword;

import java.util.Optional;

public interface ForgetPasswordRepository extends JpaRepository<ForgetPassword,Integer> {

    Optional<ForgetPassword> findByOtpAndEmail(Integer otp,String email);
}
