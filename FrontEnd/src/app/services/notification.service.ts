import { Injectable, OnInit } from '@angular/core';
import * as Stomp from '@stomp/stompjs';
import { Subject } from 'rxjs';
import * as SockJS from 'sockjs-client';
import { UserService } from './user.service';
import { User } from '../models/user';
@Injectable({
  providedIn: 'root'
})
export class NotificationService{
   private client: Stomp.Client;
firstname! : String;
  private reservationSubject = new Subject<string>();
  private quotaSubject = new Subject<string>();
   private reminderSubject = new Subject<string>();

  reservationObservable$ = this.reservationSubject.asObservable();
  quotaObservable$ = this.quotaSubject.asObservable();

  constructor(private userService:UserService) {
    this.client = new Stomp.Client({
      // Utiliser SockJS pour créer le WebSocket
      webSocketFactory: () => new SockJS('http://localhost:8087/ws'),
      reconnectDelay: 5000,
      debug: (str) => { console.log(str); }
    });
    this.getCurrentUser()
  }

  connect() {
    this.client.onConnect = (frame) => {
      console.log('Connecté au WS STOMP :', frame);

     this.client.subscribe('/topic/reservations', (msg) => {
    console.log('Message reservation reçu:', msg.body); // <- DEBUG
    this.reservationSubject.next(msg.body);
});

this.client.subscribe('/topic/quota', (msg) => {
    console.log('Message quota reçu:', msg.body); // <- DEBUG
    this.quotaSubject.next(msg.body);
});


 // 👉 écoute les reminders pour l'utilisateur courant
      this.client.subscribe('/topic/reminder' , (message) => {
         this.reminderSubject.next(message.body);
      });

      
    };

    this.client.onStompError = (frame) => {
      console.error('Erreur STOMP :', frame);
    };

    this.client.activate(); // démarre la connexion
  }

  disconnect() {
    if (this.client && this.client.connected) {
      this.client.deactivate();
      console.log('Déconnecté du WS');
    }
  }
    getCurrentUser() {
      this.userService.getCurrentUser().subscribe({
        next: (user: User) => {
         this.firstname = user.firstname;
        },
        error: (err) => {
          console.error("Erreur lors de la récupération de l'utilisateur :", err);
         
        }
      });
    }
}
