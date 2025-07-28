import { SalleService } from './../../../services/salle.service';
import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { ReservationService } from 'src/app/services/reservation.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-reservation-form',
  templateUrl: './reservation-form.component.html',
  styleUrls: ['./reservation-form.component.css']
})
export class ReservationFormComponent implements OnInit {
reservationForm!: FormGroup;

  salles: any[] = []; // à remplir avec l'appel API

  constructor(private salleService:SalleService , private fb: FormBuilder, private reservationService: ReservationService) {
    this.reservationForm = this.fb.group({
      dateReservation: [null, Validators.required],
      heureDebut: [null, Validators.required],
      heureFin: [null, Validators.required],
      salleId: [null, Validators.required],
       userId: [{ value: 29, disabled: true }]  // Désactivé depuis TS
    });

  }

  ngOnInit() {
    this.loadSalles();
  }

  loadSalles() {
     this.salleService.getAll().subscribe((data) => {
      this.salles = data;
    });
  }

  onSubmit() {
    if (this.reservationForm.valid) {
      const formValue = this.reservationForm.value;

      const reservationData = {
        ...formValue,
        userId: 29// ID statique pour le moment
      };

      this.reservationService.createReservation(reservationData).subscribe({
        next: res => alert('Réservation effectuée avec succès'),
        error: err => alert('Erreur lors de la réservation')
      });
    }
  }
}
