import { Router } from '@angular/router';
import { FileHandle } from './../../../models/file-handle';
import { HttpEventType } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { Equipment } from 'src/app/models/equipment';
import { Salle } from 'src/app/models/Salle';
import { EquipmentService } from 'src/app/services/equipment.service';
import { SalleService } from 'src/app/services/salle.service';

@Component({
  selector: 'app-add-salle',
  templateUrl: './add-salle.component.html',
  styleUrls: ['./add-salle.component.css']
})
export class AddSalleComponent implements OnInit{
  salle: Salle = {
    nom: '',
    capacite: 0,
    tarifHoraire: 0,
    emplacement: '',
     titre :'',
          description:'',
    estDisponible: false,
    enMaitenance: false,
    equipmentIds: [],
    salleImages:[]
  };
  selectedFiles: File[] = [];
  uploadProgress: number = -1;
  message: string = '';
 equipments: Equipment[] = [];
selectedEquipments: number[] = [];
equipmentError: boolean = false;
showGlobalError: boolean = false;

  constructor(
    private salleService: SalleService,
    private equipmentService: EquipmentService,
    private sanitizer : DomSanitizer,
    private router : Router
  ) {}

   ngOnInit(): void {
    this.loadEquipments();
  }

  loadEquipments(): void {
    this.equipmentService.getAll().subscribe((data) => {
      this.equipments = data;
    });
  }
onEquipmentsSelected(event: Event): void {
  const selectElement = event.target as HTMLSelectElement;
  const selectedOptions = Array.from(selectElement.selectedOptions).map(opt => +opt.value);
  this.selectedEquipments = selectedOptions;

  // Si tu veux lier directement à salle.equipements
  this.salle.equipmentIds = this.selectedEquipments;
}

 


onFileSelected1(event: any) {
  if (event.target.files && event.target.files.length > 0) {
    const file = event.target.files[0];

    const fileHandle: FileHandle = {
      file: file,
      url: this.sanitizer.bypassSecurityTrustUrl(
        window.URL.createObjectURL(file)
      )
    };

    this.salle.salleImages.push(fileHandle);
  }
}
 onSubmit() {
    this.equipmentError = !this.salle.equipmentIds || this.salle.equipmentIds.length === 0;
  if (this.equipmentError) {
    return; // ne soumet pas le formulaire si pas d’équipement choisi
  }
 const salleFormData = this.prepereFormData(this.salle,this.salle.equipmentIds);
    this.salleService.addSalle(salleFormData).subscribe(
      event => {
       this.message = 'Salle ajoutée avec succès !';
      this.salle = {
          nom: '',
          capacite: 0,
          tarifHoraire: 0,
          emplacement: '',
          titre :'',
          description:'',
          estDisponible: false,
          enMaitenance: false,
          equipmentIds: [],
          salleImages: []
        };
       this.router.navigate(['/salles']).then(ok => console.log('Navigation result:', ok));

    }, error => {
      this.message = 'Erreur lors de l\'ajout de la salle.';
      console.error(error);
    });
  }
  
  prepereFormData(salle: Salle, equipmentIds: number[]) : FormData{
    const formData = new FormData();
      // Ajouter l'objet salle en JSON
      formData.append('salle', new Blob([JSON.stringify(salle)], {
        type: 'application/json'
      }));
       // Ajouter les IDs des équipements
     equipmentIds.forEach(id => {
       formData.append('equipmentIds', id.toString());
     });
     //ajouter les images
     for(var i=0 ;i<salle.salleImages.length; i++){
        formData.append(
          'imagesFile',
          salle.salleImages[i].file,
          salle.salleImages[i].file.name,
        );
     }
     return formData;
  }

  
removeImages(i:number){
this.salle.salleImages.splice(i, 1);
}

toggleEquipment(equipmentId: number): void {
  const index = this.salle.equipmentIds.indexOf(equipmentId);

  if (index > -1) {
    // déjà sélectionné → on le retire
    this.salle.equipmentIds.splice(index, 1);
  } else {
    // pas encore sélectionné → on l’ajoute
    this.salle.equipmentIds.push(equipmentId);
  }
}






}
