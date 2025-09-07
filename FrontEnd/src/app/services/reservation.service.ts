import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Reservation } from '../models/reservation';

@Injectable({
  providedIn: 'root'
})
export class ReservationService {

    private apiUrl = 'http://localhost:8087/api/reservation'

  constructor(private http: HttpClient) {}

  createReservation(data: any) {
    return this.http.post(this.apiUrl, data);
  }
  getAllReservations(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/events`);
  }
getReservations(salleId: number): Observable<any[]> {
  return this.http.get<any[]>(`${this.apiUrl}/all/${salleId}`);
}

getReservationsI(allByEntreprise: boolean): Observable<Reservation[]> {
  let params = new HttpParams()
  if (allByEntreprise) {
    params = params.set('allByEntreprise', 'true');
  }
  return this.http.get<Reservation[]>(`${this.apiUrl}/reservations`, { params });
}
annulerReservation(id: number): Observable<any> {
  return this.http.delete(`${this.apiUrl}/${id}`);
}

}
