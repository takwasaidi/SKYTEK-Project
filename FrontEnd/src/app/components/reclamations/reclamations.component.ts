import { Component, OnInit } from '@angular/core';
import { ReclamationService } from 'src/app/services/reclamation.service';

@Component({
  selector: 'app-reclamations',
  templateUrl: './reclamations.component.html',
  styleUrls: ['./reclamations.component.css']
})
export class ReclamationsComponent implements OnInit {
  reclamations: any[] = [];
  filteredReclamations: any[] = [];
  paginatedReclamations: any[] = [];
  searchTerm: string = '';

  currentPage: number = 1;
  pageSize: number = 5;
  totalPages: number = 1;

  showModal: boolean = false;
  selectedReclamation: any = null;

  constructor(private reclamationService: ReclamationService) {}

  ngOnInit(): void {
    this.loadReclamations();
  }

  loadReclamations() {
    this.reclamationService.getAllReclamations().subscribe({
      next: (data: any[]) => {
        this.reclamations = data;
        this.filteredReclamations = [...this.reclamations];
        this.updatePagination();
      },
      error: err => console.error(err)
    });
  }

  filterReclamations() {
    if (!this.searchTerm.trim()) {
      this.filteredReclamations = [...this.reclamations];
    } else {
      this.filteredReclamations = this.reclamations.filter(rec =>
        rec.sujet.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
        rec.description.toLowerCase().includes(this.searchTerm.toLowerCase())
      );
    }
    this.currentPage = 1;
    this.updatePagination();
  }

  updatePagination() {
    this.totalPages = Math.ceil(this.filteredReclamations.length / this.pageSize);
    const start = (this.currentPage - 1) * this.pageSize;
    const end = start + this.pageSize;
    this.paginatedReclamations = this.filteredReclamations.slice(start, end);
  }

  nextPage() {
    if (this.currentPage < this.totalPages) {
      this.currentPage++;
      this.updatePagination();
    }
  }

  prevPage() {
    if (this.currentPage > 1) {
      this.currentPage--;
      this.updatePagination();
    }
  }

  toggleSelectAll(event: any) {
    const checked = event.target.checked;
    this.paginatedReclamations.forEach(rec => rec.selected = checked);
  }

  cancelReclamation(id: number) {
    this.reclamationService.cancelReclamation(id).subscribe({
      next: () => {
        this.loadReclamations();
      },
      error: err => console.error(err)
    });
  }

  viewReclamation(rec: any) {
    this.selectedReclamation = rec;
    this.showModal = true;
  }

  closeModal() {
    this.showModal = false;
    this.selectedReclamation = null;
  }

  download(fichier: any) {
    this.reclamationService.downloadFile(fichier.id, fichier.nomFichier).subscribe({
      next: () => console.log("Téléchargement terminé"),
      error: err => console.error(err)
    });
  }
}
