import { Component, OnInit } from '@angular/core';
import { Salle } from 'src/app/models/Salle';
import { SalleService } from 'src/app/services/salle.service';

@Component({
  selector: 'app-list-salle',
  templateUrl: './list-salle.component.html',
  styleUrls: ['./list-salle.component.css']
})
export class ListSalleComponent implements OnInit{
 salles: Salle[] = [];

  constructor(private salleService: SalleService) {}

  ngOnInit(): void {
    this.salleService.getAll().subscribe(data => {
      this.salles = data;
    });
  }

  deleteSalle(id: number): void {
    this.salleService.delete(id).subscribe(() => {
      this.salles = this.salles.filter(s => s.id !== id);
    });
  }
}
