import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { interval, Observable, switchMap } from 'rxjs';
import { Entreprise } from '../models/entreprise';
import { ToastService } from './toast-service.service';


export interface QuotaAlert {
  id: number;
  entreprise:Entreprise
  type: string;
  dateAlert: string;
  lu: boolean;
}

@Injectable({
  providedIn: 'root'
})

export class QuotaAlertService {

  private apiUrl = 'http://localhost:8087/api/quota-alerts';
notifications: QuotaAlert[] = [];

  constructor(private http: HttpClient, private toast: ToastService) {
    // Vérifier toutes les 30s
    interval(30000).pipe(
      switchMap(() => this.getUnread())
    ).subscribe(alerts => {
      alerts.forEach(alert => {
        this.toast.show(`⚠️ Quota dépassé pour ${alert.entreprise.nom}!`);
        this.notifications.unshift(alert); // On stocke la notif en mémoire
      });
    });
  }

  getUnread() {
    return this.http.get<QuotaAlert[]>(`${this.apiUrl}/unread`);
  }

  getAll() {
    return this.http.get<QuotaAlert[]>(this.apiUrl);
  }

  markAsRead(id: number) {
    return this.http.post(`${this.apiUrl}//mark-read/${id}`, {});
  }
  markAllAsRead() {
    return this.http.put(`${this.apiUrl}/mark-all-as-read`, {});
  }
}