import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent {
  constructor(private router: Router) {}
  isCollapsed = false;

  toggleSidebar() {
    this.isCollapsed = !this.isCollapsed;
  }
  navigateTo(route: string): void {
  this.router.navigate([route]);
}
//users
usersMenuOpen: boolean = false;

toggleUsersMenu() {
  this.usersMenuOpen = !this.usersMenuOpen;
}
//les salle 
sallesMenuOpen = false;

toggleSallesMenu() {
  this.sallesMenuOpen = !this.sallesMenuOpen;
}
horaireMenuOpen: boolean = false;

toggleHoraireMenu() {
  this.horaireMenuOpen = !this.horaireMenuOpen;
}


}
