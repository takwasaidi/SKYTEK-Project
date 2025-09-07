import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { RegisterRequest } from '../models/register-request';
import { AuthenticationResponse } from '../models/authentication-response';
import { AuthenticationRequest } from '../models/authentication-request';
import { VerificationRequest } from '../models/verification-request';
import { BehaviorSubject, Observable } from 'rxjs';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  private baseUrl = 'http://localhost:8087/api/auth'

  constructor(private http: HttpClient,private router: Router) { }

private isAuthenticatedSubject = new BehaviorSubject<boolean>(this.isLoggedIn());
  isAuthenticated$ = this.isAuthenticatedSubject.asObservable();


   register(registerRequest: RegisterRequest) {
    return this.http.post<AuthenticationResponse>
    (`${this.baseUrl}/register`, registerRequest);
  }

  login1(authRequest: AuthenticationRequest) {
    return this.http.post<AuthenticationResponse>
    (`${this.baseUrl}/login`, authRequest);
  }
 login(authRequest: AuthenticationRequest): Observable<AuthenticationResponse> {
    return new Observable(observer => {
      this.http.post<AuthenticationResponse>(`${this.baseUrl}/login1`, authRequest).subscribe({
        next: (res) => {
          this.isAuthenticatedSubject.next(true); // ✅ Notify subscribers
          observer.next(res);
          observer.complete();
        },
        error: (err) => {
          observer.error(err);
        }
      });
    });
  }

initiateMfa(email: string, mfaType: string) {
  return this.http.post<AuthenticationResponse>(`${this.baseUrl}/initiate-mfa`, null, {
    params: { email, mfaType }
  });
}

verifyMfaCode(payload: { email: string, code: string, rememberMe: boolean }) {
  return this.http.post<AuthenticationResponse>(`${this.baseUrl}/verify-mfa`, payload);
}



  
logout(): void {
  // Appel au backend si nécessaire (par exemple pour invalider un token côté serveur)
  this.http.post(`${this.baseUrl}/logout`, {}).subscribe({
    next: () => {
       this.clearSession();
    },
    error: (err) => {
      console.error('Erreur lors du logout côté backend', err);
        this.clearSession();
    }
  });
}

 private clearSession(): void {
    localStorage.removeItem('token');
    sessionStorage.clear();
    this.isAuthenticatedSubject.next(false); // ✅ Notify subscribers
    this.router.navigate(['/signin']);
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }

  
   getMfaQrCode(email: string) {
  return this.http.post<{ secretImageUri: string }>(`${this.baseUrl}/mfa/setup`, { email });
  }


// verifyMfaCode(email: string, code: string): Observable<AuthenticationResponse> {
//   // Si le backend renvoie un AuthenticationResponse (avec accessToken)
//   return this.http.post<AuthenticationResponse>(`${this.baseUrl}/verify`, { email, code });
// }

//test//

//test//



}
