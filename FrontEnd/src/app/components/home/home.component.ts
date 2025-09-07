import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { Equipment } from 'src/app/models/equipment';
import { Salle } from 'src/app/models/Salle';
import { SalleSearchDTO } from 'src/app/models/salle-searchDTO';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { EquipmentService } from 'src/app/services/equipment.service';
import { SalleService } from 'src/app/services/salle.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {



salles: Salle[] = [];
 selectedSalle: Salle | null = null;
equipments: Equipment[] = [];

isAuthenticated: boolean = false;
  private authSubscription!: Subscription;

  searchDTO: SalleSearchDTO = {
    date: '',
    heureDebut: '',
    heureFin: ''
  };


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
 
onSearch() {
  if (!this.searchDTO.date || !this.searchDTO.heureDebut || !this.searchDTO.heureFin) return;

  // Redirection vers la page de résultats avec les paramètres
  this.router.navigate(['/Filtred-salles'], {
    queryParams: {
      date: this.searchDTO.date,
      heureDebut: this.searchDTO.heureDebut,
      heureFin: this.searchDTO.heureFin
    }
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
