import { Component } from '@angular/core';
import { CalendarOptions, DateSelectArg, EventApi, EventClickArg } from '@fullcalendar/core';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import interactionPlugin from '@fullcalendar/interaction';
import { ConfigurationHoraireService } from 'src/app/services/configuration-horaire.service';
import { ConfigurationHoraire } from '../../models/configurationHoraire';

@Component({
  selector: 'app-admin-config-calendar',
  templateUrl: './admin-config-calendar.component.html',
  styleUrls: ['./admin-config-calendar.component.css']
})
export class AdminConfigCalendarComponent {

  events: any[] = [];
joursFermes: Set<number> = new Set(); 
calendarOptions!: CalendarOptions;
  constructor(private configHoraireService: ConfigurationHoraireService) {}
 
  ngOnInit(): void {
    this.loadHoraires();
  }

loadHoraires(): void {
  this.configHoraireService.getAll().subscribe(data => {
    this.joursFermes = new Set(
      data.filter(h => !h.estOuvert).map(h => this.getDayNumber(h.jour))
    );

    this.events = data
      .filter(h => h.estOuvert)
      .map(h => ({
        id: h.id?.toString(),
        title: 'Disponible',
        startRecur: '2024-01-01',
        daysOfWeek: [this.getDayNumber(h.jour)],
        startTime: h.heureDebut,
        endTime: h.heureFin,
        backgroundColor: '#5bc0de',
        borderColor: '#5bc0de'
      }));

    // Maintenant on peut définir calendarOptions ici
    this.calendarOptions = {
      plugins: [dayGridPlugin, timeGridPlugin, interactionPlugin],
      initialView: 'timeGridWeek',
      editable: true,
      selectable: true,
      selectMirror: true,
      allDaySlot: false,
      weekends: true,

      selectAllow: (selectInfo) => {
        const day = selectInfo.start.getDay();
        return !this.joursFermes.has(day);
      },

      dayCellDidMount: (info) => {
        if (this.joursFermes.has(info.date.getDay())) {
          info.el.style.backgroundColor = '#f0f0f0';
          info.el.style.pointerEvents = 'none';
          info.el.style.opacity = '0.6';
        }
      },
       eventResize: (eventResizeInfo) => {
    this.handleUpdateHoraire(eventResizeInfo.event);
  },

  eventDrop: (eventDropInfo) => {
    this.handleUpdateHoraire(eventDropInfo.event);
  },

      select: this.handleDateSelect.bind(this),
      eventClick: this.handleEventClick.bind(this),
      events: [...this.events]
    };
  });
}
  getDayNumber(jour: string): number {
    const jours: any = {
      'MONDAY': 1,
      'TUESDAY': 2,
      'WEDNESDAY': 3,
      'THURSDAY': 4,
      'FRIDAY': 5,
      'SATURDAY': 6,
      'SUNDAY': 0
    };
    return jours[jour.toUpperCase()];
  }
handleDateSelect(selectInfo: DateSelectArg) {
  const start = selectInfo.start; // Date complète sélectionnée
  const end = selectInfo.end;

  // Obtenir le jour en string (ex: MONDAY)
  const jours = ['SUNDAY','MONDAY','TUESDAY','WEDNESDAY','THURSDAY','FRIDAY','SATURDAY'];
  const jour = jours[start.getDay()];

  // Extraire les heures 'HH:mm' depuis la date
  const heureDebut = start.toTimeString().slice(0,5);
  const heureFin = end.toTimeString().slice(0,5);

  // Construire l'objet à sauvegarder
  const nouvelleConfig = {
    jour,
    heureDebut,
    heureFin,
    estOuvert: true
  };

  // Appel backend
  this.configHoraireService.save(nouvelleConfig).subscribe(() => {
    this.loadHoraires(); // recharger pour mise à jour
  });
}


 handleEventClick(clickInfo: EventClickArg) {
  if(confirm('Supprimer ce créneau ?')) {
    const id = clickInfo.event.id;
    this.configHoraireService.deleteHoraire(parseInt(id)).subscribe(() => {
      this.loadHoraires(); // recharger après suppression
    });
  }
}
handleUpdateHoraire(event: EventApi): void {
  const updatedId = Number(event.id);
  const startTime = event.start?.toTimeString().substring(0, 5); // format 'HH:mm'
  const endTime = event.end?.toTimeString().substring(0, 5);

  const jour = this.getDayName(event.start?.getDay() || 0); // par exemple "Lundi"

  const updatedHoraire: ConfigurationHoraire = {
    id: updatedId,
    jour: jour,
    heureDebut: startTime || '',
    heureFin: endTime || '',
    estOuvert: true
  };

  this.configHoraireService.update(updatedHoraire).subscribe({
    next: () => {
      console.log('Horaire mis à jour avec succès');
    },
    error: (err) => {
      console.error('Erreur lors de la mise à jour', err);
      alert("La mise à jour a échoué.");
    }
  });
}
getDayName(day: number): string {
  const days = ['SUNDAY', 'MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY'];
  return days[day];
}


 
}
