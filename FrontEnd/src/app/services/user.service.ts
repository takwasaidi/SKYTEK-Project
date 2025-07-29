import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { Salle } from '../models/Salle';
import { User } from '../models/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {


  private apiUrl = 'http://localhost:8087/api/user';

  constructor(private http: HttpClient) {}

 getUsers() {
    return this.http.get<any[]>(this.apiUrl);
  }
   getCurrentUser(): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/me`);
  }

}
