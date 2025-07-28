import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Equipment } from 'src/app/models/equipment';
import { EquipmentService } from 'src/app/services/equipment.service';

@Component({
  selector: 'app-equipment-form',
  templateUrl: './equipment-form.component.html',
  styleUrls: ['./equipment-form.component.css']
})
export class EquipmentFormComponent {
 equipment: Equipment = { nom: '' };
  isEditMode = false;
  submitted = false;


  constructor(
    private equipmentService: EquipmentService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode = true;
      this.equipmentService.getEquipmentById(+id).subscribe(e => {
        this.equipment = e;
      });
    }
  }
  save(form: NgForm) {
  this.submitted = true;

  if (form.invalid) {
    return; // Ne fait rien si formulaire invalide
  }

  if (this.isEditMode) {
    this.equipmentService.updateEquipment(this.equipment.id!, this.equipment).subscribe(() => {
      this.router.navigate(['/equipment']);
    });
  } else {
    this.equipmentService.addEquipment(this.equipment).subscribe(() => {
      this.router.navigate(['/equipment']);
    });
  }
}

}
