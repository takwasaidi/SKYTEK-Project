import { Component, OnInit } from '@angular/core';
import { ReservationService } from 'src/app/services/reservation.service';
import { CalendarOptions, DateSelectArg, EventApi, EventClickArg } from '@fullcalendar/core';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import bootstrap5Plugin from '@fullcalendar/bootstrap5';
import frLocale from '@fullcalendar/core/locales/fr';

import interactionPlugin, { DateClickArg } from '@fullcalendar/interaction';
import { ActivatedRoute, Router } from '@angular/router';
import { SalleService } from '../../../services/salle.service';
import { ConfigurationHoraireService } from 'src/app/services/configuration-horaire.service';
import { UserService } from 'src/app/services/user.service';
import { User } from 'src/app/models/user';
import { Entreprise } from 'src/app/models/entreprise';
import { Equipment } from 'src/app/models/equipment';
import { EquipmentService } from 'src/app/services/equipment.service';

@Component({
  selector: 'app-reservation-calender',
  templateUrl: './reservation-calender.component.html',
  styleUrls: ['./reservation-calender.component.css']
})
export class ReservationCalenderComponent implements OnInit {
  currentUserType: string = '';
calendarOptions!: CalendarOptions; 

  //for the form 
  selectedDate1: string = new Date().toISOString().slice(0,10); // date format yyyy-MM-dd
  selectedHeure: string | null = null;

  reservationData = {
    dateReservation: this.selectedDate1,
    heureDebut: '',
    heureFin: ''
  };

 selectedDate: string = ''; // date sélectionnée, format yyyy-mm-dd
  selectedSalle: any = null; // salle sélectionnée, tu peux préciser son type
  disponibilites: string[] = []; // liste des heures disponibles
joursFermes: Set<number> = new Set(); 

equipments:Equipment[]=[];

entreprise : Entreprise = {
  nom : '',
  email:''
};


 constructor(private reservationService: ReservationService,
  private route: ActivatedRoute,
   private salleService :SalleService ,
   private configHoraireService:ConfigurationHoraireService,
  private userService : UserService,
private equipmentService: EquipmentService,
private router: Router) {}

salleId: number | null = null;
  ngOnInit(): void {
 this.userService.getCurrentUser().subscribe({
    next: (user: User) => {
      this.currentUserType = user.user_type; // "INTERN_USER" ou "EXTERN_USER"
    }
  });
     this.loadEquipments()
    this.route.queryParams.subscribe(params => {
      this.salleId = +params['salleId']; // le + convertit en number
      if (this.salleId) {
        this.loadCalendarEvents(this.salleId);
      
      }
    });
    // Exemple : tu récupères la salle via un service ou à partir de la route
this.salleService.getById(this.salleId!).subscribe(salle => {
  this.selectedSalle = salle;

this.getUserCompany()
});

 // Charger les jours fermés
  this.configHoraireService.getAll().subscribe(data => {
  this.joursFermes = new Set(
    data.filter(h => !h.estOuvert).map(h => this.getDayNumber(h.jour))
  )
});

 this.calendarOptions = {
     plugins: [dayGridPlugin, timeGridPlugin, interactionPlugin,bootstrap5Plugin],
      themeSystem: 'bootstrap5',
      locale: frLocale,
  initialView: 'dayGridMonth',
  height: 'auto',              // pour que la hauteur s’adapte au contenu
  aspectRatio: 1.35,           // ratio largeur/hauteur (ajuste à ta convenance)
  events: [],                  // tes événements
  headerToolbar: {
    left: 'prev,next today',
    center: 'title',
    right: 'dayGridMonth,timeGridWeek,timeGridDay'
  },
  selectable: true,                    // 🔸 active la sélection
  selectMirror: true,
  select: this.handleTimeSelect.bind(this),  // 🔸 ta fonction pour gérer la sélection
  dayMaxEvents: 3,             // limite nombre d’événements affichés dans un jour (le reste "+X more")
  eventTextColor: '#fff',
 eventDisplay: 'auto',     
  dateClick: this.handleDateClick.bind(this),
    // ⏰ Configuration des heures visibles (uniquement pour les vues timeGrid)
  slotMinTime: '08:00:00',
  slotMaxTime: '19:00:00',
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

};

  }

  loadCalendarEvents(salleId: number): void {
    // Ici tu fais appel à ton service pour charger les réservations de cette salle
    this.reservationService.getReservations(salleId).subscribe(events => {
      this.calendarOptions.events = events;
    });
  }

handleDateClick(info: DateClickArg) {
  this.selectedDate = info.dateStr;

  if (this.salleId) {
    this.loadDisponibilites(info.dateStr);
  } else {
    console.warn('Aucune salle sélectionnée, impossible de charger les disponibilités');
  }
}


loadDisponibilites(date: string) {
  this.salleService.getDisponibilites(this.salleId!, date).subscribe({
    next: (data: string[]) => {
      this.disponibilites = data;
    },
    error: (err) => {
      console.error('Erreur lors du chargement des disponibilités', err);
    }
  });
}


handleTimeSelect(selectInfo: any) {
  const startStr = selectInfo.startStr; // e.g., "2025-08-03T10:00:00"
  const endStr = selectInfo.endStr;

  const salleId= this.selectedSalle?.id;


  const confirmed = confirm(`Voulez-vous réserver de ${startStr} à ${endStr} ?`);

  if (!confirmed) return;

  // 🔄 Transformation
  const dateReservation = startStr.split('T')[0]; // => "2025-08-03"
  const heureDebut = startStr.split('T')[1].substring(0, 5); // => "10:00"
  const heureFin = endStr.split('T')[1].substring(0, 5);     // => "11:00"

  const newReservation = {
    dateReservation,
    heureDebut,
    heureFin,
    salleId,
  };
  console.log(newReservation)

  this.reservationService.createReservation(newReservation).subscribe({
    next: () => {
      alert('Réservation effectuée avec succès !');

      // recharge ou ajoute visuellement l’événement :
      this.calendarOptions.events = [
        ...(Array.isArray(this.calendarOptions.events) ? this.calendarOptions.events : []),
        {
          title: 'Réservé',
          start: startStr,
          end: endStr
        }
      ];
    },
    error: (err) => {
      console.error('Erreur lors de la réservation', err);
      alert(err?.error?.message || 'Erreur inconnue lors de la réservation.');
    }
  });
}


//for the form 
 // Méthode appelée quand on clique sur un bouton heure dispo
  reserverHeure(heure: string) {
    this.selectedHeure = heure;
    this.reservationData.dateReservation = this.selectedDate;
    this.reservationData.heureDebut = heure;
    // Par défaut on peut mettre heure fin = heure debut + 1h
    const [h, m] = heure.split(':').map(Number);
    let finH = h + 1;
    if (finH > 23) finH = 23;
    this.reservationData.heureFin = finH.toString().padStart(2, '0') + ':00';
  }

  // Méthode pour formater l'heure (ex: "08:00:00" => "08:00")
  formatHeure(heure: string): string {
    return heure.substring(0,5);
  }
  

submitReservation() {
  if (!this.reservationData.dateReservation || !this.reservationData.heureDebut || !this.reservationData.heureFin) {
    alert('Veuillez remplir tous les champs');
    return;
  }

  this.userService.getCurrentUser().subscribe({
    next: (user: User) => {
      if (user.user_type === 'EXTERN_USER') {
        // 🔄 Redirection vers paiement AVANT réservation
        this.router.navigate(['/paiement'], {
          queryParams: {
            dateReservation: this.reservationData.dateReservation,
            heureDebut: this.reservationData.heureDebut,
            heureFin: this.reservationData.heureFin,
            salleId: this.salleId,
            cout: this.selectedSalle?.tarifHoraire
          }
        });
      } else {
        // 🟢 User interne → créer directement la réservation
        this.reservationService.createReservation({
          dateReservation: this.reservationData.dateReservation,
          heureDebut: this.reservationData.heureDebut,
          heureFin: this.reservationData.heureFin,
          salleId: this.salleId
        }).subscribe({
          next: () => {
            alert('Réservation enregistrée avec succès !');
            this.loadCalendarEvents(this.salleId!);
            this.loadDisponibilites(this.selectedDate);
          },
          error: err => {
            alert('Erreur lors de la réservation : ' + err.error.message || err.message);
          }
        });
      }
    }
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


  quotaStatusMessage: string | null = null;
quotaWarningType: 'warning' | 'danger' | null = null;

// Exemple de méthode pour définir ce message (à appeler après chargement des données quota)
checkQuotaStatus(quotaUtilise: number, quotaAlloue: number) {
  const seuilAlerte = quotaAlloue * 0.9;

  if (quotaUtilise > quotaAlloue) {
   this.quotaStatusMessage = "⚠️ Attention : Le quota global de votre entreprise est dépassé ! Toutes les réservations supplémentaires seront facturées.";

    this.quotaWarningType = 'danger';
  } else if (quotaUtilise === quotaAlloue) {
    this.quotaStatusMessage = "ℹ️ Le quota global de votre entreprise est entièrement utilisé. Les réservations supplémentaires seront facturées.";
    this.quotaWarningType = 'warning';
  } else if (quotaUtilise >= seuilAlerte) {
    this.quotaStatusMessage = "⚠️ Attention : Votre entreprise approche de son quota global !";
    this.quotaWarningType = 'warning';
  } else {
    this.quotaStatusMessage = null;
    this.quotaWarningType = null;
  }
}


 getUserCompany() {
  this.userService.getCurrentUser().subscribe({
    next: (user: User) => {
      this.entreprise = user.entreprise;
      console.log(this.entreprise);
console.log(this.entreprise.quota?.quota);
console.log(this.entreprise.quota?.quotaUtilise);
      // Appel après récupération pour éviter undefined
      this.checkQuotaStatus(
        this.entreprise.quota?.quotaUtilise ?? 0,
        this.entreprise.quota?.quota ?? 0
      );
    },
    error: (err) => {
      console.error("Erreur lors de la récupération de l'utilisateur :", err);
    }
  });
}
//equipement
   loadEquipments(): void {
    this.equipmentService.getAll().subscribe((data) => {
      this.equipments = data;
    });
  }

 getEquipementsDeLaSalle(): Equipment[] {
  if (!this.selectedSalle) return [];
  // Filtrer en s'assurant que e.id est défini
  return this.equipments.filter(e => e.id !== undefined && this.selectedSalle!.equipmentIds.includes(e.id));

 }


}
