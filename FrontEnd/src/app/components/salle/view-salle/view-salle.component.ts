import { Component, OnInit } from '@angular/core';
import { Equipment } from 'src/app/models/equipment';
import { Salle } from 'src/app/models/Salle';
import { EquipmentService } from 'src/app/services/equipment.service';
import { SalleService } from 'src/app/services/salle.service';

@Component({
  selector: 'app-view-salle',
  templateUrl: './view-salle.component.html',
  styleUrls: ['./view-salle.component.css'],
})
export class ViewSalleComponent implements OnInit{
salles: Salle[] = [];
 selectedSalle: Salle | null = null;
equipments: Equipment[] = [];

  constructor(private salleService: SalleService , private equipmentService: EquipmentService) {}

  ngOnInit(): void {
   this.loadSalle();
    this.loadEquipments();
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






}
