import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { User } from 'src/app/models/user';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-nav',
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.css']
})
export class NavComponent {

 // In navbar.component.ts
isAuthenticated: boolean = false;
firstname: string = '';
lastname: string = '';

  private authSubscription!: Subscription;
  
 constructor(
    private router: Router,private authService : AuthenticationService , private userService:UserService
  ) {
  }
    ngOnInit(): void {
    this.authSubscription = this.authService.isAuthenticated$.subscribe(
      (status) => {
        this.isAuthenticated = status;
      }
    );
    this.getUserName()
  }

 
    onLogin() {
    // Redirect to login route
     this.router.navigate(['/signin']);
    console.log('Redirecting to login...');
  }

  onRegister() {
    // Redirect to register route
    this.router.navigate(['/signup']);
    console.log('Redirecting to register...');
  }

  onLogout() {
    this.isAuthenticated = false;
    console.log('Logged out');
       this.authService.logout();
  }
   ngOnDestroy(): void {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }
  getUserName() {
    this.userService.getCurrentUser().subscribe({
      next: (user: User) => {
       this.firstname = user.firstname
       this.lastname = user.lastname
      },
      error: (err) => {
        console.error("Erreur lors de la récupération de l'utilisateur :", err);
       
      }
    });
  }
isMenuOpen = false;
isUserMenuOpen = false;

toggleMenu() {
  this.isMenuOpen = !this.isMenuOpen;
}

toggleUserMenu() {
  this.isUserMenuOpen = !this.isUserMenuOpen;
}


 
}
