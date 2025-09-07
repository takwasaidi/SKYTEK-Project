import { Component, OnInit } from '@angular/core';
import { QuotaAlert, QuotaAlertService } from 'src/app/services/quota-alert.service';

@Component({
  selector: 'app-quota-alerts',
  templateUrl: './quota-alerts.component.html',
  styleUrls: ['./quota-alerts.component.css']
})
export class QuotaAlertsComponent implements OnInit {
  alerts: QuotaAlert[] = [];

  constructor(private quotaAlertService: QuotaAlertService) { }

  ngOnInit(): void {
    this.loadAlerts();
  }

  loadAlerts() {
    this.quotaAlertService.getUnread().subscribe(alerts => {
      this.alerts = alerts;
    });
  }

  markRead(alert: QuotaAlert) {
    this.quotaAlertService.markAsRead(alert.id).subscribe(() => {
      this.loadAlerts();
    });
  }
}
