import { Component } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { UserService } from './services/user.service';
import { User } from './models/user';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
    sidebarOpen: boolean = true;
     isLoginPage: boolean = false;
     isAdmin: boolean = false;
  
 ngOnInit(): void {
    this.getUserRole();
  }
  toggleSidebar() {
    this.sidebarOpen = !this.sidebarOpen;
  }
   

  
  constructor(private router: Router,private userService:UserService) {
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        const noLayoutRoutes = ['/login', '/forgot-password', '/register'];
        this.isLoginPage = noLayoutRoutes.includes(event.urlAfterRedirects);
        
      }
    });
  }
   getUserRole() {
    this.userService.getCurrentUser().subscribe({
      next: (user: User) => {
        this.isAdmin = user.user_type === 'ADMIN'; // adapte selon la valeur réelle
      },
      error: (err) => {
        console.error("Erreur lors de la récupération de l'utilisateur :", err);
        this.isAdmin = false;
      }
    });
  }
}
