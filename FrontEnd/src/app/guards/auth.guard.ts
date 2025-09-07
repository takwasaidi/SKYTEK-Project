import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router } from '@angular/router';
import { AuthenticationService } from '../services/authentication.service';
import { UserService } from '../services/user.service';
import { User } from '../models/user';


@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

userRole: string = '';
  constructor(private userService :UserService,private authService :AuthenticationService, private router: Router) {
    this.getUserRole()
  }

  canActivate(route: ActivatedRouteSnapshot): boolean {
    const expectedRoles = route.data['roles'] as Array<string>; // rôles attendus depuis les routes

    if (this.authService.isLoggedIn() && expectedRoles.includes(this.userRole)) {
      return true;
    } else {
      this.router.navigate(['/']);
      return false;
    }
  }
  
   getUserRole() {
      this.userService.getCurrentUser().subscribe({
        next: (user: User) => {
         this.userRole = user.user_type
        },
        error: (err) => {
          console.error("Erreur lors de la récupération de l'utilisateur :", err);
         
        }
      });
    }
}