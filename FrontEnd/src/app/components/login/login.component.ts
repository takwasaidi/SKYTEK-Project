import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationRequest } from 'src/app/models/authentication-request';
import { AuthenticationService } from 'src/app/services/authentication.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
credentials: AuthenticationRequest = {};
 mfaRequired: boolean = false;
  emailForMfa: string = '';
  mfaCode: string = '';

  constructor(private authService: AuthenticationService, private router: Router) {}

  onLogin() {
    this.authService.login(this.credentials).subscribe({
      next: res => {

        // localStorage.setItem('token', res.accessToken!);
        // this.router.navigate(['/welcome']);
        if (res.mfaRequired) {
          // L'utilisateur a activé MFA, demander le code
          this.mfaRequired = true;
          this.emailForMfa = this.credentials.email!;
        } else if (res.accessToken) {
          // Authentification classique
          localStorage.setItem('token', res.accessToken);
          this.router.navigate(['/salles']);
        }
      },
      error: err => {
        alert('Connexion échouée.');
        console.error(err);
      }
    });
  }
onVerifyMfa() {
    // this.authService.verifyMfaCode(this.emailForMfa, this.mfaCode).subscribe({
    //   next: res => {
    //     localStorage.setItem('token', res.accessToken!);
    //     alert('MFA validé, bienvenue !');
    //     this.router.navigate(['/welcome']);
    //   },
    //   error: err => {
    //     alert('Code MFA invalide.');
    //     console.error(err);
    //   }
    // });
  }

}
