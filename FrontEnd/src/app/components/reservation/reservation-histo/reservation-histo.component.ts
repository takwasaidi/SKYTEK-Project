import { Component, OnInit } from '@angular/core';
import { Reservation } from 'src/app/models/reservation';
import { Salle } from 'src/app/models/Salle';
import { ReservationService } from 'src/app/services/reservation.service';
import { SalleService } from 'src/app/services/salle.service';

@Component({
  selector: 'app-reservation-histo',
  templateUrl: './reservation-histo.component.html',
  styleUrls: ['./reservation-histo.component.css']
})
export class ReservationHistoComponent implements OnInit {


p: number = 1;
allReservations: Reservation[] = [];
  reservations: Reservation[] = [];
  showAllEntreprise = false;
salles:Salle[] = []
  constructor(private reservationService: ReservationService, private salleService :SalleService) {}

  ngOnInit() {
    this.loadReservations();
  }

  loadReservations() {
    this.reservationService.getReservationsI(this.showAllEntreprise)
      .subscribe(data => {
        this.reservations = data;
       
      });
  }

  toggleView() {
    this.showAllEntreprise = !this.showAllEntreprise;
    this.loadReservations();
  }

getReservationColor(res: Reservation): string {
  // Exemple simple : si showAllEntreprise => rose sinon vert
  if (this.showAllEntreprise) {
    return 'rgb(234,46,94)'; // rose fuchsia
  } else {
    return 'rgb(123,190,75)'; // vert quota OK
  }
}

annulerReservation(id: number) {
  if (confirm("Voulez-vous vraiment annuler cette réservation ?")) {
    this.reservationService.annulerReservation(id).subscribe({
      next: () => {
        //alert("Votre réservation a été annulée avec succès ");
        this.loadReservations(); 
      },
      error: (err) => {
        console.error(err);
        alert("Erreur lors de l'annulation ");
      }
    });
  }
}

canCancel(res: Reservation): boolean {
  const now = new Date();
  const reservationDate = new Date(res.dateReservation + 'T' + res.heureDebut);

  // Différence en millisecondes
  const diffMs = reservationDate.getTime() - now.getTime();

  // Convertir en heures
  const diffHours = diffMs / (1000 * 60 * 60);

  return diffHours > 2; // ✅ Annulation possible seulement si > 2h
}



}
