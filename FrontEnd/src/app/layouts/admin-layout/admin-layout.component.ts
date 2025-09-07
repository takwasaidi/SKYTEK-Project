import { Component } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { User } from 'src/app/models/user';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { QuotaAlert, QuotaAlertService } from 'src/app/services/quota-alert.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-admin-layout',
  templateUrl: './admin-layout.component.html',
  styleUrls: ['./admin-layout.component.css']
})
export class AdminLayoutComponent {
   notifications: QuotaAlert[] = [];
  unreadCount = 0;
  showNotifications = false;
alertCount: number = 0;
 userName: string = '';
menuOpen = false;

 ngOnInit(): void {
    this.getUserName();
    this.quotaAlertService.getUnread().subscribe(alerts => {
    this.alertCount = alerts.length;
  });

  // Charger toutes les notifications et trier pour mettre les non lues en premier
  this.quotaAlertService.getAll().subscribe(alerts => {
    this.notifications = alerts.sort((a, b) => {
      if (a.lu === b.lu) return 0;
      return a.lu ? 1 : -1; // non lus (lu=false) en premier
    });
    this.updateUnreadCount();
  });
  }


  constructor(private router: Router,private userService:UserService , private authService:AuthenticationService ,private quotaAlertService: QuotaAlertService) {
   
  }
   getUserName() {
    this.userService.getCurrentUser().subscribe({
      next: (user: User) => {
       this.userName = user.firstname
      },
      error: (err) => {
        console.error("Erreur lors de la récupération de l'utilisateur :", err);
       
      }
    });
  }

  



toggleMenu() {
  this.menuOpen = !this.menuOpen;
}

logout() {
  // Clear tokens / session
  localStorage.clear();
  this.router.navigate(['/signin']);
}


  toggleNotifications() {
    this.showNotifications = !this.showNotifications;
    this.markAll() 
  }
  updateUnreadCount() {
    this.unreadCount = this.notifications.filter(n => !n.lu).length;
  }

    markAll() {
    this.quotaAlertService.markAllAsRead().subscribe(() => {
      this.notifications.forEach(n => n.lu = true);
    });
  }

}
