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
    estDisponible: false,
    enMaitenance: false,
    equipmentIds: [],
    salleImages:[]
  };

  selectedFiles: File[] = [];
  uploadProgress: number = -1;
  message: string = '';
 equipments: Equipment[] = [];

  constructor(
    private salleService: SalleService,
    private equipmentService: EquipmentService,
    private sanitizer : DomSanitizer
  ) {}

   ngOnInit(): void {
    this.loadEquipments();
  }

  loadEquipments(): void {
    this.equipmentService.getAll().subscribe((data) => {
      this.equipments = data;
    });
  }

  toggleEquipment(id: number, event: any): void {
    if (event.target.checked) {
      this.salle.equipmentIds.push(id);
    } else {
      this.salle.equipmentIds = this.salle.equipmentIds.filter((eid) => eid !== id);
    }
  }
imagePreviews: string[] = [];

onFileSelected(event: any) {
  const files = Array.from(event.target.files as FileList) as File[];

  // Ajout des fichiers sélectionnés aux fichiers existants
  this.selectedFiles = this.selectedFiles.concat(files);

  for (let file of files) {
    const reader = new FileReader();
    reader.onload = (e: any) => {
      this.imagePreviews.push(e.target.result);
    };
    reader.readAsDataURL(file);
  }
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
    
 const salleFormData = this.prepereFormData(this.salle,this.salle.equipmentIds);
    this.salleService.addSalle(salleFormData).subscribe(
      event => {
       this.message = 'Salle ajoutée avec succès !';
      this.salle = {
          nom: '',
          capacite: 0,
          tarifHoraire: 0,
          emplacement: '',
          estDisponible: false,
          enMaitenance: false,
          equipmentIds: [],
          salleImages: []
        };
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




  ///ili tekhdem deja 

/*   onSubmit() {
    this.uploadProgress = 0;
    this.message = '';

    this.salleService.addSalleWithImages(this.salle, this.selectedFiles, this.salle.equipmentIds).subscribe(event => {
      if (event.type === HttpEventType.UploadProgress) {
        this.uploadProgress = Math.round(event.loaded / (event.total || 1) * 100);
      } else if (event.type === HttpEventType.Response) {
        this.message = 'Salle ajoutée avec succès !';
        this.uploadProgress = -1;

        this.salle = {
          nom: '',
          capacite: 0,
          tarifHoraire: 0,
          emplacement: '',
          estDisponible: false,
          enMaitenance: false,
          equipmentIds: []
        };
        this.selectedFiles = [];
      }
    }, error => {
      this.message = 'Erreur lors de l\'ajout de la salle.';
      this.uploadProgress = -1;
      console.error(error);
    });
  }
   */
  onEquipmentChange(event: any) {
  const equipmentId = +event.target.value;
  if (event.target.checked) {
    if (!this.salle.equipmentIds.includes(equipmentId)) {
      this.salle.equipmentIds.push(equipmentId);
    }
  } else {
    this.salle.equipmentIds = this.salle.equipmentIds.filter(id => id !== equipmentId);
  }
}
removeImages(i:number){
this.salle.salleImages.splice(i, 1);
}


}
