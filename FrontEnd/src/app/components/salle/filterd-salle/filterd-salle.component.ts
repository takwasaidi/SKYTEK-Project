import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Salle } from 'src/app/models/Salle';
import { SalleSearchDTO } from 'src/app/models/salle-searchDTO';
import { SalleService } from 'src/app/services/salle.service';

@Component({
  selector: 'app-filterd-salle',
  templateUrl: './filterd-salle.component.html',
  styleUrls: ['./filterd-salle.component.css']
})
export class FilterdSalleComponent implements OnInit{
salles: Salle[] = [];
searchDTO:SalleSearchDTO = {
  date: '',
  heureDebut: '',
  heureFin: ''
};
  constructor(private route: ActivatedRoute, private salleService: SalleService,private router: Router) { }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      const searchDTO: SalleSearchDTO = {
        date: params['date'],
        heureDebut: params['heureDebut'],
        heureFin: params['heureFin']
      };

      this.salleService.searchAvailableSalles(searchDTO)
        .subscribe(res => this.salles = res);
       this.searchDTO = {
        date : params['date'],
        heureDebut:  params['heureDebut'] ,
  heureFin:params['heureFin']
       }
    });

  }
   goToReservation(salleId: number) {
 

  this.router.navigate(['/reservation-salle'], {
    queryParams: {
      salleId: salleId,
      date: this.searchDTO.date,
      heureDebut: this.searchDTO.heureDebut,
      heureFin: this.searchDTO.heureFin
    }
  });
}
}
