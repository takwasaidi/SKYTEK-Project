import { Component } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { BehaviorSubject } from 'rxjs';
import { User } from 'src/app/models/user';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-sign-in',
  templateUrl: './sign-in.component.html',
  styleUrls: ['./sign-in.component.css']
})
export class SignInComponent {

    loginForm: FormGroup;
  loginError: string | null = null;

  mfaRequired: boolean = false;
  emailForMfa: string = '';
  mfaCode: string = '';
    isAdmin: boolean = false;

selectedMfaType: string = '';
mfaStep: 'APP_SETUP' | 'NONE' | 'CHOOSE' | 'VERIFY' = 'NONE';




  constructor(private fb: FormBuilder,
              private authService: AuthenticationService,
              private router: Router,
            private userService:UserService) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
      rememberMe: [false]
    });
  }

onLogin() {
  if (this.loginForm.invalid) {
    this.loginError = 'Veuillez remplir tous les champs correctement.';
    return;
  }

  this.loginError = null;
  const { email, password } = this.loginForm.value;

  this.authService.login({ email, password }).subscribe({
    next: (res) => {
      if (res.mfaRequired) {
        this.emailForMfa = email;
        this.mfaStep = 'CHOOSE'; // Show the choice screen
      } else if (res.accessToken) {
        localStorage.setItem('token', res.accessToken);
        this.getUserRole();
      }
    },
    error: (err) => {
      this.loginError = 'Adresse e-mail ou mot de passe incorrect.';
      console.error(err);
    }
  });
}
qrCodeUri: string | null = null;


onChooseMfaType(type: string) {
  this.selectedMfaType = type;

  this.authService.initiateMfa(this.emailForMfa, type).subscribe({
    next: (res) => {
      this.mfaStep = 'VERIFY';
     if (res?.secretImageUri) {
        // Stocker le QR code URI reçu
        this.qrCodeUri = res.secretImageUri;
        this.mfaStep = 'APP_SETUP'; // Nouvel écran avant VERIFY
      } else {
        this.mfaStep = 'VERIFY';
      }
    },
    error: (err) => {
      console.error('Erreur MFA:', err);
      alert('Erreur lors de l\'initialisation MFA');
    }
  });
}

onVerifyMfa() {
  if (!this.mfaCode) {
    alert('Veuillez saisir le code MFA.');
    return;
  }
  const rememberMe = this.loginForm.get('rememberMe')?.value;
  const payload = {
    email: this.emailForMfa,
    code: this.mfaCode,
    rememberMe: rememberMe 
  };

  this.authService.verifyMfaCode(payload).subscribe({
    next: (res) => {
      localStorage.setItem('token', res.accessToken!);
      alert('MFA validé, bienvenue !');
      this.getUserRole();
    },
    error: (err) => {
      alert('Code MFA invalide.');
      console.error(err);
    }
  });
}



    getUserRole() {
  this.userService.getCurrentUser().subscribe({
    next: (user: User) => {
      this.isAdmin = user.user_type === 'ADMIN';
      console.log("isAdmin", this.isAdmin);

      // Redirection ici, après avoir reçu le rôle
      if (this.isAdmin) {
        this.router.navigate(['/salles']);
      } else {
        this.router.navigate(['/home']);
      }
    },
    error: (err) => {
      console.error("Erreur lors de la récupération de l'utilisateur :", err);
      this.isAdmin = false;
      this.router.navigate(['/home']); // Par défaut
    }
  });
}

}
