import { Component } from '@angular/core';

@Component({
  selector: 'app-reservation',
  templateUrl: './reservation.component.html',
  styleUrls: ['./reservation.component.css']
})
export class ReservationComponent {
 room = {
    name: 'Salle Avant-Première',
    capacity: 60,
    equipment: 'Vidéoprojecteur, Wi-Fi, Climatisation',
    description: 'Un espace polyvalent pour conférences, formations, et événements professionnels.',
    imageUrl: 'assets/salles/salle1-1.jpg'
  };

  availability = [
    { date: 'Lundi 2 Août', time: '09:00 à 12:00', available: true },
    { date: 'Lundi 2 Août', time: '14:00 à 17:00', available: false },
    { date: 'Mardi 3 Août', time: '09:00 à 12:00', available: true },
    { date: 'Mardi 3 Août', time: '14:00 à 17:00', available: true }
  ];

  reserve() {
    alert('Réservation effectuée ! (fonctionnalité à implémenter)');
  }
}
