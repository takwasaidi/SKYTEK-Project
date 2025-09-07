import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { NotificationService } from 'src/app/services/notification.service';

@Component({
  selector: 'app-notification',
  templateUrl: './notification.component.html',
  styleUrls: ['./notification.component.css']
})
export class NotificationComponent implements OnInit{ messages: string[] = [];

  constructor(
    private notifService: NotificationService,
    private cd: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.notifService.connect();

    this.notifService.reservationObservable$.subscribe(msg => {
      this.showMessage(msg);
    });

    this.notifService.quotaObservable$.subscribe(msg => {
      this.showMessage(msg);
    });
  }

  showMessage(msg: string) {
    this.messages.push(msg);
    this.cd.detectChanges(); // forcer la mise à jour de l'UI

    // Supprime automatiquement le message après 5 secondes
    setTimeout(() => {
      this.messages = this.messages.filter(m => m !== msg);
      this.cd.detectChanges(); // mettre à jour l'UI après suppression
    }, 5000);
  }
  
  
}
