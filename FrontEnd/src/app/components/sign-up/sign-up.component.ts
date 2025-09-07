import { Component } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthenticationResponse } from 'src/app/models/authentication-response';
import { RegisterRequest } from 'src/app/models/register-request';
import { AuthenticationService } from 'src/app/services/authentication.service';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css']
})
export class SignUpComponent {
 registerForm: FormGroup;
  registerError: string | null = null;

  constructor(
    private fb: FormBuilder,
    private authService: AuthenticationService,
    private router: Router
  ) {
    this.registerForm = this.fb.group({
      nom: ['', Validators.required],
      prenom: ['', Validators.required],
      telephone: ['', [Validators.required, Validators.pattern(/^([0-9]{8,15})$/)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  onRegister() {
    if (this.registerForm.invalid) {
      this.registerError = 'Veuillez remplir tous les champs correctement.';
      return;
    }

    const formValues = this.registerForm.value;

    const registerRequest: RegisterRequest = {
      firstname: formValues.nom,
      lastname: formValues.prenom,
      phone: formValues.telephone,
      email: formValues.email,
      password: formValues.password
    };

    this.authService.register(registerRequest).subscribe({
      next: (res: AuthenticationResponse) => {
        if (!res.accessToken) {
          alert('Erreur : aucun token reçu');
          return;
        }

        localStorage.setItem('token', res.accessToken);
        this.router.navigate(['/signin']);
      },
      error: err => {
        this.registerError = 'Inscription échouée.';
        console.error(err);
      }
    });
  }


  showPassword: boolean = false;

togglePasswordVisibility() {
  this.showPassword = !this.showPassword;
}

generateStrongPassword() {
  const charset = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()";
  let password = "";
  for (let i = 0; i < 12; i++) {
    const randomIndex = Math.floor(Math.random() * charset.length);
    password += charset[randomIndex];
  }
  this.registerForm.get('password')?.setValue(password);
}


}
