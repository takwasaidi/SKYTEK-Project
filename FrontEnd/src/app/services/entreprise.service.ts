import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Entreprise } from '../models/entreprise';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EntrepriseService {

 
 
  private apiUrl = 'http://localhost:8087/api/entreprise';
 
   constructor(private http: HttpClient) {}
 
   getAll(): Observable<Entreprise[]> {
     return this.http.get<Entreprise[]>(this.apiUrl);
   }
    getEntrepriseById(id: number): Observable<Entreprise> {
     return this.http.get<Entreprise>(`${this.apiUrl}/${id}`);
   }
 
   addEntreprise(equipment: Entreprise): Observable<Entreprise> {
     return this.http.post<Entreprise>(this.apiUrl, equipment);
   }
 
   updateEntreprise(id: number, equipment: Entreprise): Observable<Entreprise> {
     return this.http.put<Entreprise>(`${this.apiUrl}/${id}`, equipment);
   }
 
   deleteEntreprise(id: number): Observable<void> {
     return this.http.delete<void>(`${this.apiUrl}/${id}`);
   }
}
