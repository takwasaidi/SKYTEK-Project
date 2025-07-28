import { Component, ElementRef, EventEmitter, HostListener, Input, Output } from '@angular/core';
import { AuthenticationService } from 'src/app/services/authentication.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {
  @Input() sidebarOpen: boolean = true;
  @Output() toggleSidebar = new EventEmitter<void>();

  isDropdownOpen = false;

  constructor(private eRef: ElementRef ,private authService : AuthenticationService) {}

toggleDropdown() {
  this.isDropdownOpen = !this.isDropdownOpen;
  console.log('Dropdown toggled:', this.isDropdownOpen);
}


  @HostListener('document:click', ['$event'])
  clickOutside(event: any) {
    if (!this.eRef.nativeElement.contains(event.target)) {
      this.isDropdownOpen = false;
    }
  }
  isSearchVisible: boolean = false;
searchText: string = '';

toggleSearch() {
  this.isSearchVisible = !this.isSearchVisible;
}
  onLogout() {
    this.authService.logout();
  }
}
