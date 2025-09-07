import { Component, Input } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { AuthenticationRequest } from 'src/app/models/authentication-request';
import { Equipment } from 'src/app/models/equipment';
import { Salle } from 'src/app/models/Salle';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { EquipmentService } from 'src/app/services/equipment.service';
import { SalleService } from 'src/app/services/salle.service';

@Component({
  selector: 'app-welcome',
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.css']
})
export class WelcomeComponent {

salles: Salle[] = [];
 selectedSalle: Salle | null = null;
equipments: Equipment[] = [];

isAuthenticated: boolean = false;
  private authSubscription!: Subscription;
  constructor(private salleService: SalleService ,private router: Router, private equipmentService: EquipmentService,private authService : AuthenticationService) {}

  ngOnInit(): void {
   this.loadSalle();
    this.loadEquipments();
    this.authSubscription = this.authService.isAuthenticated$.subscribe(
      (status) => {
        this.isAuthenticated = status;
      }
    );
  }
  //salees
  loadSalle(){
     this.salleService.getAll().subscribe({
      next: (data) => {
        this.salles = data;
      },
      error: (err) => {
        console.error('Erreur lors du chargement des salles', err);
      }
    });
  } 
  //equipement
   loadEquipments(): void {
    this.equipmentService.getAll().subscribe((data) => {
      this.equipments = data;
    });
  }
 
 
  openDetails(salle: Salle) {
     console.log("Salle sélectionnée :", salle);
    this.selectedSalle = salle;
  }

  closeModal() {
    this.selectedSalle = null;
  }
  getEquipementsDeLaSalle(): Equipment[] {
  if (!this.selectedSalle) return [];
  // Filtrer en s'assurant que e.id est défini
  return this.equipments.filter(e => e.id !== undefined && this.selectedSalle!.equipmentIds.includes(e.id));
}
handleReservation() {
  if (!this.isAuthenticated) {
    this.router.navigate(['/signin']);
  } else {
    this.router.navigate(['/calenderRes'], {
      queryParams: { salleId: this.selectedSalle!.id }
    });
  }
}

}
