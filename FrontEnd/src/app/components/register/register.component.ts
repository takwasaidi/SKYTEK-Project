import { Router } from '@angular/router';
import { AuthenticationService } from './../../services/authentication.service';
import { Component } from '@angular/core';
import { AuthenticationResponse } from 'src/app/models/authentication-response';
import { RegisterRequest } from 'src/app/models/register-request';
import { VerificationRequest } from 'src/app/models/verification-request';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
 registerRequest: RegisterRequest = {};
  authResponse: AuthenticationResponse = {};
  

  constructor(
    private authService: AuthenticationService,
    private router: Router
  ) {
  }

  registerUser() {
    
    this.authService.register(this.registerRequest)
      .subscribe({
        next: res => {
       // localStorage.setItem('token', res.accessToken!);
       if (!res.accessToken) {
  alert("Erreur : aucun token reçu");
  return;
}

localStorage.setItem('token', res.accessToken);
        this.router.navigate(['/login']);
      },
      error: err => {
        alert('Inscription échouée.');
        console.error(err);
      }
      });

  }
  generateStrongPassword(): void {
  const charset = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789@#$%&*!";
  let password = "";
  for (let i = 0; i < 12; i++) {
    const randomIndex = Math.floor(Math.random() * charset.length);
    password += charset[randomIndex];
  }
  this.registerRequest.password = password;
  console.log(password)
}

    

  
}
