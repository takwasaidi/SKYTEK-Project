import { Component, OnInit } from '@angular/core';
import { Entreprise } from 'src/app/models/entreprise';
import { EntrepriseService } from 'src/app/services/entreprise.service';
declare var bootstrap: any; 
@Component({
  selector: 'app-entreprise',
  templateUrl: './entreprise.component.html',
  styleUrls: ['./entreprise.component.css']
})
export class EntrepriseComponent implements OnInit {
entreprises: Entreprise[] = [];
selectedEntreprise: Entreprise | null = null;
 constructor(private entrepriseService: EntrepriseService) {}
  ngOnInit(): void {
    this.loadEntreprise();
    
  }

  loadEntreprise() {
    this.entrepriseService.getAll().subscribe(data => this.entreprises = data);
    console.log(this.entreprises)
  }

newEntreprise: Entreprise = {
  nom: '',
  email: ''
};
openAddEntrepriseModal() {
  this.selectedEntreprise = null;
  this.newEntreprise = { nom: '', email: '' };

  const modalElement = document.getElementById('entrepriseModal');
  if (modalElement) {
    const modal = bootstrap.Modal.getOrCreateInstance(modalElement);
    modal.show();
  }
}

openEditEntrepriseModal(entreprise: Entreprise) {
  this.selectedEntreprise = entreprise;
  this.newEntreprise = { ...entreprise }; // clone pour éviter la modification directe

  const modalElement = document.getElementById('entrepriseModal');
  if (modalElement) {
    const modal = bootstrap.Modal.getOrCreateInstance(modalElement);
    modal.show();
  }
}

saveEntreprise() {
  const modalElement = document.getElementById('entrepriseModal');
  const modal = modalElement ? bootstrap.Modal.getInstance(modalElement) : null;

  if (this.selectedEntreprise) {
    this.entrepriseService.updateEntreprise(this.selectedEntreprise.id!,this.newEntreprise).subscribe(() => {
      this.loadEntreprise();
      modal?.hide();
    });
  } else {
    this.entrepriseService.addEntreprise(this.newEntreprise).subscribe(() => {
      this.loadEntreprise();
      modal?.hide();
    });
  }

  this.newEntreprise = { nom: '', email: '' };
  this.selectedEntreprise = null;
}
  deleteEntreprise(id: number) {
    this.entrepriseService.deleteEntreprise(id).subscribe(() => {
      this.entreprises = this.entreprises.filter(e => e.id !== id);
    });
  }
  
currentPage: number = 1;
itemsPerPage: number = 5;

get paginatedSalles() {
  const startIndex = (this.currentPage - 1) * this.itemsPerPage;
  return this.entreprises.slice(startIndex, startIndex + this.itemsPerPage);
}

get totalPages() {
  return Math.ceil(this.entreprises.length / this.itemsPerPage);
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
} 
