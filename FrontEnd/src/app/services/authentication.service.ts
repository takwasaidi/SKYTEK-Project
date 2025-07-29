import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { RegisterRequest } from '../models/register-request';
import { AuthenticationResponse } from '../models/authentication-response';
import { AuthenticationRequest } from '../models/authentication-request';
import { VerificationRequest } from '../models/verification-request';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  private baseUrl = 'http://localhost:8087/api/auth'

  constructor(private http: HttpClient,private router: Router) { }

   register(registerRequest: RegisterRequest) {
    return this.http.post<AuthenticationResponse>
    (`${this.baseUrl}/register`, registerRequest);
  }

  login(authRequest: AuthenticationRequest) {
    return this.http.post<AuthenticationResponse>
    (`${this.baseUrl}/login`, authRequest);
  }
logout(): void {
  // Appel au backend si nécessaire (par exemple pour invalider un token côté serveur)
  this.http.post(`${this.baseUrl}/logout`, {}).subscribe({
    next: () => {
      // Nettoyage local après réponse backend
      localStorage.removeItem('token');
      sessionStorage.clear();
      this.router.navigate(['/login']);
    },
    error: (err) => {
      console.error('Erreur lors du logout côté backend', err);
      // Même en cas d'erreur, on déconnecte localement
      localStorage.removeItem('token');
      sessionStorage.clear();
      this.router.navigate(['/login']);
    }
  });
}


  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }

  
   getMfaQrCode(email: string) {
  return this.http.post<{ secretImageUri: string }>(`${this.baseUrl}/mfa/setup`, { email });
  }


verifyMfaCode(email: string, code: string): Observable<AuthenticationResponse> {
  // Si le backend renvoie un AuthenticationResponse (avec accessToken)
  return this.http.post<AuthenticationResponse>(`${this.baseUrl}/verify`, { email, code });
}



}
