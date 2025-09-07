
import { Component, OnInit, ViewChild } from '@angular/core';
import { CalendarOptions, EventInput } from '@fullcalendar/core';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import interactionPlugin from '@fullcalendar/interaction';
import { ReservationService } from 'src/app/services/reservation.service';
import { FullCalendarComponent } from '@fullcalendar/angular';
import { FullCalendarModule } from '@fullcalendar/angular';
import listPlugin from '@fullcalendar/list';
import bootstrap5Plugin from '@fullcalendar/bootstrap5';

@Component({
  selector: 'app-calendar',
  templateUrl: './calendar.component.html',
  styleUrls: ['./calendar.component.css']
})
export class CalendarComponent {
   
  // calendarOptions: CalendarOptions = {
  //     plugins: [dayGridPlugin, timeGridPlugin, interactionPlugin,listPlugin,bootstrap5Plugin],
  //   initialView: 'dayGridMonth',
  //   headerToolbar: false, // We'll make our own header
  //   events: this.events,
  //   dayHeaderFormat: { weekday: 'long' },
  //   dateClick: this.handleDateClick.bind(this),
  //   eventClick: this.handleEventClick.bind(this),
  //   eventClassNames: this.customEventClass.bind(this),
  //   height: 'auto',
  //   selectable: true,
  //   editable: true,
  //   dayMaxEvents: true
  // };

 calendarOptions: CalendarOptions = {
  plugins: [dayGridPlugin, timeGridPlugin, interactionPlugin],
  initialView: 'dayGridMonth',
  initialDate: '2025-08-01',
  firstDay: 0, // Sunday
  headerToolbar: {
    left: 'title',                        // Month Title à gauche
    center: 'dayGridMonth,timeGridWeek,timeGridDay', // Tabs month/week/day au centre
    right: 'prev,next today'              // Navigation buttons à droite
  },
  buttonText: {
    today: 'today',
    month: 'month',
    week: 'week',
    day: 'day'
  },
  events: [
    { title: 'LBD Launch', start: '2025-08-03T10:30:00', color: '#4e73df' },
    { title: 'Meeting', start: '2025-08-05T12:00:00', color: '#fd7e14' },
    { title: 'Lunch Break', start: '2025-08-06T13:00:00', color: '#e83e8c' },
    { title: 'Brainstorm', start: '2025-08-10T09:00:00', color: '#f6c23e' },
    { title: 'Social Event', start: '2025-08-15T18:00:00', color: '#1cc88a' }
  ]
};

}


  

  

