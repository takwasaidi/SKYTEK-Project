
import { Component } from '@angular/core';
import { CalendarOptions } from '@fullcalendar/core';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import interactionPlugin from '@fullcalendar/interaction';
import { ReservationService } from 'src/app/services/reservation.service';

@Component({
  selector: 'app-calendar',
  templateUrl: './calendar.component.html',
  styleUrls: ['./calendar.component.css']
})
export class CalendarComponent {
  calendarOptions: CalendarOptions = {
    plugins: [dayGridPlugin, timeGridPlugin, interactionPlugin],
    initialView: 'timeGridWeek',
    events: [],
    headerToolbar: {
      left: 'prev,next today',
      center: 'title',
      right: 'dayGridMonth,timeGridWeek,timeGridDay'
    },
    editable: false,
    selectable: true,
     eventClick: this.handleEventClick.bind(this),
  };
    selectedEvent: any = null;
    showModal: boolean = false;
  constructor(private reservationService: ReservationService) {}

  ngOnInit(): void {
    this.reservationService.getAllReservations().subscribe(events => {
      this.calendarOptions.events = events;
    });
  }


handleEventClick(arg: any) {
  this.selectedEvent = {
    ...arg.event.extendedProps, // clone les extendedProps
    title: arg.event.title,
    start: arg.event.start,
    end: arg.event.end
  };
  this.showModal = true;
}
}
