import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-forget-password',
  templateUrl: './forget-password.component.html',
  styleUrls: ['./forget-password.component.css']
})
export class ForgetPasswordComponent {
step = 1;
  email = '';
  otp = '';
  newPassword = '';
  confirmPassword = '';
  errorMessage = '';
  successMessage = '';

  constructor(private http: HttpClient,private router: Router) {}

  sendOtp() {
    this.http.post(`http://localhost:8087/api/forgetPassword/verifyMail/${this.email}`, null)
      .subscribe({
      next: (response) => {
    console.log("Réponse succès :", response);
    this.step = 2; // passe à l’étape suivante
    this.errorMessage = '';
    this.successMessage = 'Code envoyé à votre adresse e-mail.';
  },
  error: (error) => {
    console.error("Erreur :", error);
    this.errorMessage = 'Erreur lors de l’envoi du code.';
  }
      });
  }

  verifyOtp() {
    this.http.post(`http://localhost:8087/api/forgetPassword/verifyOTP/${this.otp}/${this.email}`, null)
      .subscribe({
        next: () => {
          this.step = 3;
          this.errorMessage = '';
          this.successMessage = 'Code vérifié avec succès.';
        },
        error: err => {
          this.errorMessage = err.error.message || 'Code invalide ou expiré.';
        }
      });
  }

  changePassword() {
    if (this.newPassword !== this.confirmPassword) {
      this.errorMessage = 'Les mots de passe ne correspondent pas.';
      return;
    }

    const request = {
      newPassword: this.newPassword,
      confirmationPassword: this.confirmPassword
    };

    this.http.post(`http://localhost:8087/api/forgetPassword/changePassword/${this.email}`, request)
      .subscribe({
        next: () => {
          this.successMessage = 'Mot de passe changé avec succès !';
          this.errorMessage = '';
        // Redirection vers la page login
        this.router.navigate(['/login']);
        },
        error: err => {
          this.errorMessage = err.error.message || 'Erreur lors du changement de mot de passe.';
        }
      });
  }
}
