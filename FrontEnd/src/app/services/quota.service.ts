import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { EntrepriseQuotaDto } from '../models/entrepriseQuotaDto';
import { ReservationInfoDto } from '../models/ReservationInfoDto';

@Injectable({
  providedIn: 'root'
})
export class QuotaService {

  private apiUrl = 'http://localhost:8087/api/quota';

  constructor(private http: HttpClient) {}

  getAll(): Observable<EntrepriseQuotaDto[]> {
    return this.http.get<EntrepriseQuotaDto[]>(this.apiUrl);
  }

  updateQuota(entrepriseId: number, newQuota: number) {
    // PUT /api/admin/quotas/{id}?quota=VALUE
    return this.http.put<EntrepriseQuotaDto>(`${this.apiUrl}/${entrepriseId}?quota=${newQuota}`, {});
  }

  resetUsage(entrepriseId: number) {
    return this.http.post<EntrepriseQuotaDto>(`${this.apiUrl}/${entrepriseId}/reset`, {});
  }
  getReservations(entrepriseId: number) {
  return this.http.get<ReservationInfoDto[]>(`${this.apiUrl}/${entrepriseId}/reservations`);
}

}
