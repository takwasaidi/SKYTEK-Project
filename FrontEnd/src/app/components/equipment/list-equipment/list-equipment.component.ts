import { Component, OnInit } from '@angular/core';
import { Equipment } from 'src/app/models/equipment';
import { EquipmentService } from 'src/app/services/equipment.service';

@Component({
  selector: 'app-list-equipment',
  templateUrl: './list-equipment.component.html',
  styleUrls: ['./list-equipment.component.css']
})
export class ListEquipmentComponent implements OnInit {

  equipments: Equipment[] = [];

  constructor(private equipmentService: EquipmentService) {}

  ngOnInit(): void {
    this.loadEquipments();
  }

  loadEquipments() {
    this.equipmentService.getAll().subscribe(data => this.equipments = data);
  }

  deleteEquipment(id: number) {
    this.equipmentService.deleteEquipment(id).subscribe(() => {
      this.equipments = this.equipments.filter(e => e.id !== id);
    });
  }
  
}
