import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Salle } from 'src/app/models/Salle';
import { SalleService } from 'src/app/services/salle.service';

@Component({
  selector: 'app-list-salle',
  templateUrl: './list-salle.component.html',
  styleUrls: ['./list-salle.component.css']
})
export class ListSalleComponent implements OnInit{
 salles: Salle[] = [];
  message: string = '';

  constructor(private salleService: SalleService , private router:Router) {}

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

  
  editSalle(id: number) {
  this.router.navigate(['/salle/edit', id]);
}

deleteSalle(id: number | undefined): void {
  if (id === undefined) {
    console.error("Erreur : ID de salle est undefined.");
    return;
  }

  this.salleService.delete(id).subscribe({
    next: () => {
      this.message = 'Salle supprimée avec succès.';
 this.loadSalle();
    },
    error: err => {
      console.error("Erreur lors de la suppression :", err);
      this.message = "Erreur lors de la suppression de la salle.";
    }
  });
}

currentPage: number = 1;
itemsPerPage: number = 5;

get paginatedSalles() {
  const startIndex = (this.currentPage - 1) * this.itemsPerPage;
  return this.salles.slice(startIndex, startIndex + this.itemsPerPage);
}

get totalPages() {
  return Math.ceil(this.salles.length / this.itemsPerPage);
}

nextPage() {
  if (this.currentPage < this.totalPages) {
    this.currentPage++;
  }
}

prevPage() {
  if (this.currentPage > 1) {
    this.currentPage--;
  }
}
setMaintenance(salle: Salle): void {
  // Mettre à jour les champs côté front
  salle.enMaitenance = true;
  salle.estDisponible = false;

  // Appel au service pour mettre à jour la salle côté backend
  this.salleService.updateSalle(salle.id!, salle).subscribe({
    next: () => {
      console.log('Salle mise en maintenance avec succès');
      // Optionnel : afficher un message ou rafraîchir la liste
    },
    error: (err) => console.error('Erreur lors de la mise en maintenance', err)
  });
}



}
