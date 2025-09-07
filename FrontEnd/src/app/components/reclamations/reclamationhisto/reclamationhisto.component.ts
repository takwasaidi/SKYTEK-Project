import { Component } from '@angular/core';
import { ReclamationService } from 'src/app/services/reclamation.service';

@Component({
  selector: 'app-reclamationhisto',
  templateUrl: './reclamationhisto.component.html',
  styleUrls: ['./reclamationhisto.component.css']
})
export class ReclamationhistoComponent {
 reclamations: any[] = [];       // toutes les réclamations
  pagedReclamations: any[] = [];  // réclamations affichées sur la page actuelle
  page: number = 1;               // page actuelle
  pageSize: number = 3;           // éléments par page
  totalPages: number = 0;

  constructor(private reclamationService: ReclamationService) {}

  ngOnInit(): void {
    this.loadReclamations();
  }

  loadReclamations() {
    this.reclamationService.getAllByUser().subscribe({
      next: (data) => {
        this.reclamations = data;
        this.totalPages = Math.ceil(this.reclamations.length / this.pageSize);
        this.setPage(this.page);
      },
      error: (err) => console.error(err)
    });
  }

  setPage(page: number) {
    this.page = page;
    const start = (page - 1) * this.pageSize;
    const end = start + this.pageSize;
    this.pagedReclamations = this.reclamations.slice(start, end);
  }

  nextPage() {
    if (this.page < this.totalPages) {
      this.setPage(this.page + 1);
    }
  }

  prevPage() {
    if (this.page > 1) {
      this.setPage(this.page - 1);
    }
  }
  download(fichier: any) {
  this.reclamationService.downloadFile(fichier.id, fichier.nomFichier)
    .subscribe({
      next: () => console.log('Téléchargement terminé'),
      error: err => console.error(err)
    });
}

cancelReclamation(reclamation: any) {
  this.reclamationService.cancelReclamation(reclamation)
    .subscribe({
      next: () => {
        console.log('Réclamation annulée');
        // Optionnel : retirer la réclamation de la liste affichée
        this.reclamations = this.reclamations.filter(r => r.id !== reclamation.id);
      
      },
      error: err => console.error(err)
    });
}

}
