import { Component, OnInit } from '@angular/core';
import { Salle } from 'src/app/models/Salle';
import { SalleService } from 'src/app/services/salle.service';

@Component({
  selector: 'app-view-salle',
  templateUrl: './view-salle.component.html',
  styleUrls: ['./view-salle.component.css']
})
export class ViewSalleComponent implements OnInit{
salles: Salle[] = [];

  constructor(private salleService: SalleService) {}

  ngOnInit(): void {
   this.loadSalle();
  }
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
  selectedSalle: Salle | null = null;
 
  openDetails(salle: Salle) {
    this.selectedSalle = salle;
  }

  closeModal() {
    this.selectedSalle = null;
  }




}
