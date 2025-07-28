import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

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
}
