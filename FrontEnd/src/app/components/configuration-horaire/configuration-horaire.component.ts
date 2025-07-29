import { Component, OnInit } from '@angular/core';
import { ConfigurationHoraire } from 'src/app/models/configurationHoraire';
import { ConfigurationHoraireService } from 'src/app/services/configuration-horaire.service';


@Component({
  selector: 'app-configuration-horaire',
  templateUrl: './configuration-horaire.component.html',
  styleUrls: ['./configuration-horaire.component.css']
})
export class ConfigurationHoraireComponent implements OnInit {
  configurations: ConfigurationHoraire[] = [];

  constructor(private configService: ConfigurationHoraireService) {}

  ngOnInit(): void {
    this.loadConfigurations();
  }

  loadConfigurations(): void {
    this.configService.getAll().subscribe(data => {
      this.configurations = data;
    });
  }

  onToggleOuverture(config: ConfigurationHoraire): void {
    config.estOuvert = !config.estOuvert;
    this.save(config);
  }

  onChangeHeure(config: ConfigurationHoraire): void {
    this.save(config);
  }

  save(config: ConfigurationHoraire): void {
    this.configService.update(config).subscribe(() => {
      console.log('Mise à jour réussie');
    });
  }






  ///////
  config: ConfigurationHoraire = {
    jour: 'MONDAY',
    heureDebut: '08:00',
    heureFin: '19:00',
    estOuvert: true
  };

  jours: string[] = [
    'MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY'
  ];


  saveConfig() {
    this.configService.save(this.config).subscribe({
      next: () => alert('Configuration enregistrée !'),
      error: () => alert('Erreur lors de l’enregistrement.')
    });
  }
}
