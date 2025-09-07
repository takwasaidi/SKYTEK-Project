import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { Salle } from '../models/Salle';
import { User } from '../models/user';
import { RegisterRequest } from '../models/register-request';
import { AuthenticationResponse } from '../models/authentication-response';
 export interface UserQuota {
  firstname: string;
  lastname: string;
  utilise: number;
  quota: number;
}

@Injectable({
  providedIn: 'root'
})
export class UserService {


  private apiUrl = 'http://localhost:8087/api/user';

  constructor(private http: HttpClient) {}

 getUsers() {
    return this.http.get<User[]>(this.apiUrl);
  }

   getCurrentUser(): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/me`);
  }
  
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

   createUserFromAdmin(registerRequest: RegisterRequest) {
      return this.http.post<AuthenticationResponse>
      (`${this.apiUrl}/createUserFromAdmin`, registerRequest);
    }
  getUsersOverQuota(entrepriseId: number): Observable<UserQuota[]> {
  return this.http.get<UserQuota[]>(`${this.apiUrl}/users-over-quota/${entrepriseId}`);
}

}
