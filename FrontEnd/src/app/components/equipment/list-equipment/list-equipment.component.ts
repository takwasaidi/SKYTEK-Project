import { Component, OnInit } from '@angular/core';
import { Equipment } from 'src/app/models/equipment';
import { EquipmentService } from 'src/app/services/equipment.service';
declare var bootstrap: any; 
@Component({
  selector: 'app-list-equipment',
  templateUrl: './list-equipment.component.html',
  styleUrls: ['./list-equipment.component.css']
})
export class ListEquipmentComponent implements OnInit {

  equipments: Equipment[] = [];
newEquipmentName: string = '';
selectedEquipment: any = null; // null = mode ajout, sinon = mode édition


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
  
currentPage: number = 1;
itemsPerPage: number = 5;

get paginatedSalles() {
  const startIndex = (this.currentPage - 1) * this.itemsPerPage;
  return this.equipments.slice(startIndex, startIndex + this.itemsPerPage);
}

get totalPages() {
  return Math.ceil(this.equipments.length / this.itemsPerPage);
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
openAddEquipmentModal() {
  this.selectedEquipment = null; // Mode ajout
  this.newEquipmentName = '';
  const modalElement = document.getElementById('addEquipmentModal');
  if (modalElement) {
    const modal = bootstrap.Modal.getOrCreateInstance(modalElement);
    modal.show();
  }
}

openEditEquipmentModal(equipment: any) {
  this.selectedEquipment = equipment; // Mode édition
  this.newEquipmentName = equipment.nom;
  const modalElement = document.getElementById('addEquipmentModal');
  if (modalElement) {
    const modal = bootstrap.Modal.getOrCreateInstance(modalElement);
    modal.show();
  }
}saveEquipment() {
  if (!this.newEquipmentName.trim()) return;

  const modalElement = document.getElementById('addEquipmentModal');
  const modal = modalElement ? bootstrap.Modal.getInstance(modalElement) : null;

  if (this.selectedEquipment) {
    // Mode édition
    const updatedEquipment = {
      ...this.selectedEquipment,
      nom: this.newEquipmentName.trim()
    };

    this.equipmentService.updateEquipment(this.selectedEquipment.id!,updatedEquipment).subscribe(() => {
      this.loadEquipments();
      modal?.hide();
    });
  } else {
    // Mode ajout
    const newEquip = { nom: this.newEquipmentName.trim() };
    this.equipmentService.addEquipment(newEquip).subscribe(() => {
      this.loadEquipments();
      modal?.hide();
    });
  }

  this.newEquipmentName = '';
  this.selectedEquipment = null;
}



  
}
