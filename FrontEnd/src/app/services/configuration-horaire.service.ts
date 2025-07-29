import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ConfigurationHoraire } from '../models/configurationHoraire';

@Injectable({
  providedIn: 'root'
})
export class ConfigurationHoraireService {
private apiUrl = 'http://localhost:8087/api/configurations-horaires';

  constructor(private http: HttpClient) {}

  getAll(): Observable<ConfigurationHoraire[]> {
    return this.http.get<ConfigurationHoraire[]>(this.apiUrl);
  }

  update(config: ConfigurationHoraire): Observable<ConfigurationHoraire> {
    return this.http.put<ConfigurationHoraire>(this.apiUrl, config);
  }
   save(config: ConfigurationHoraire): Observable<ConfigurationHoraire> {
    return this.http.post<ConfigurationHoraire>(this.apiUrl, config);
  }
   deleteHoraire(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
