import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Equipment } from 'src/app/models/equipment';
import { Salle } from 'src/app/models/Salle';
import { EquipmentService } from 'src/app/services/equipment.service';
import { SalleService } from 'src/app/services/salle.service';

@Component({
  selector: 'app-edit-salle',
  templateUrl: './edit-salle.component.html',
  styleUrls: ['./edit-salle.component.css']
})
export class EditSalleComponent implements OnInit {
  salle: any ; // initialisation pour ngModel
  equipments: Equipment[] = [];
  equipmentError = false;
  message = '';

  constructor(
    private salleService: SalleService,
    private equipmentService: EquipmentService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    const salleId = +this.route.snapshot.paramMap.get('id')!;

    // Charger la salle existante
    this.salleService.getById(salleId).subscribe({
      next: (data) => this.salle = data,
      error: (err) => console.error('Erreur chargement salle', err)
    });

    // Charger tous les équipements
    this.equipmentService.getAll().subscribe({
      next: (data) => this.equipments = data,
      error: (err) => console.error('Erreur chargement équipements', err)
    });
  }

  // Gestion des images
  onFileSelected1(event: any) {
    const files = event.target.files;
    for (let i = 0; i < files.length; i++) {
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.salle.salleImages.push({ file: files[i], url: e.target.result });
      };
      reader.readAsDataURL(files[i]);
    }
  }

  removeImages(index: number) {
    this.salle.salleImages.splice(index, 1);
  }

  // Gestion des équipements
  toggleEquipment(equipmentId: number) {
    const index = this.salle.equipmentIds.indexOf(equipmentId);
    if (index > -1) {
      this.salle.equipmentIds.splice(index, 1);
    } else {
      this.salle.equipmentIds.push(equipmentId);
    }
  }

  onSubmit(): void {
    if (this.salle.equipmentIds.length === 0) {
      this.equipmentError = true;
      return;
    }
    this.equipmentError = false;

    this.salleService.updateSalle(this.salle.id!, this.salle).subscribe({
      next: () => {
        this.message = 'Salle mise à jour avec succès';
        this.router.navigate(['/salles']);
      },
      error: (err) => console.error('Erreur maj salle', err)
    });
  }
}