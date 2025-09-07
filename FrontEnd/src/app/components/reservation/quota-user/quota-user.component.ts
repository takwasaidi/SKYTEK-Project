import { Component, OnInit } from '@angular/core';
import { Entreprise } from 'src/app/models/entreprise';
import { User } from 'src/app/models/user';
import { ReservationService } from 'src/app/services/reservation.service';
import { SalleService } from 'src/app/services/salle.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-quota-user',
  templateUrl: './quota-user.component.html',
  styleUrls: ['./quota-user.component.css']
})
export class QuotaUserComponent implements OnInit {
entreprise: Entreprise = { nom: '', email: '' };

  quotaTotal = 0;
  quotaUtilise = 0;
  pourcentage = 0;

  quotas: { nom: string, total: number, utilise: number }[] = [];

    // ✅ Déclarer les nouvelles propriétés
  usersOverQuota: any[] = [];
  sallesPopulaires: any[] = [];

  constructor(
    private userService: UserService,
    private reservationService: ReservationService,
    private salleService: SalleService
  ) {}

  ngOnInit(): void {
    this.loadUserCompany();

  }

  loadUserCompany() {
    this.userService.getCurrentUser().subscribe(user => {
      this.entreprise = user.entreprise;
       // Charger les salles les plus réservées pour cette entreprise
        if (this.entreprise.id) {
          this.loadSallesPopulaires(this.entreprise.id);
          this.loadUsersOverQuota(this.entreprise.id)
        }
      this.quotaTotal = this.entreprise.quota?.quota ?? 0;
      this.quotaUtilise = this.entreprise.quota?.quotaUtilise ?? 0;
     this.pourcentage = this.quotaTotal > 0
  ? Math.min(Math.round((this.quotaUtilise / this.quotaTotal) * 100), 100)
  : 0;


      this.quotas = [
        { nom: 'Quota global', total: this.quotaTotal, utilise: this.quotaUtilise }
      ];
    });
  }
loadUsersOverQuota(entrepriseId: number): void {
  this.userService.getUsersOverQuota(entrepriseId).subscribe({
    next: (users) => {
      this.usersOverQuota = users;
    },
    error: (err) => console.error('Erreur chargement utilisateurs over-quota', err)
  });
}

  loadSallesPopulaires(entrepriseId: number) {
    this.salleService.getSallesPopulaires(entrepriseId).subscribe({
      next: (salles) => {
        this.sallesPopulaires = salles;
      },
      error: (err) => console.error('Erreur récupération salles populaires :', err)
    });
  }


  getRestant(total: number, utilise: number) {
    return Math.max(total - utilise, 0);
  }
getPourcentage(total: number, utilise: number): number {
  if (total <= 0) return 0;
  return Math.min(Math.round((utilise / total) * 100), 100);
}

}