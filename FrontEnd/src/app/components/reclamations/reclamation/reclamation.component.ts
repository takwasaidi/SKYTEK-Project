import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ReclamationDTO, ReclamationService } from 'src/app/services/reclamation.service';

@Component({
  selector: 'app-reclamation',
  templateUrl: './reclamation.component.html',
  styleUrls: ['./reclamation.component.css']
})
export class ReclamationComponent {
  sujet: string = '';
  description: string = '';
  fichiers: File[] = [];
  savedReclamation?: ReclamationDTO;

  constructor(private reclamationService: ReclamationService , private router: Router) {}

  onFileSelected(event: any) {
    this.fichiers = Array.from(event.target.files);
  }

submitReclamation() {
  this.reclamationService
    .addReclamation(this.sujet, this.description, this.fichiers)
    .subscribe({
      next: (rec) => {
        console.log('Réclamation ajoutée:', rec);
        // Redirection vers le composant d'affichage
        this.router.navigate(['/reclamations']); 
      },
      error: (err) => console.error(err)
    });
}

}
