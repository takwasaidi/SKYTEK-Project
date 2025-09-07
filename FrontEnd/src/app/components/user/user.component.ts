import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { RegisterRequest } from 'src/app/models/register-request';
import { User } from 'src/app/models/user';
import { UserService } from 'src/app/services/user.service';
import { register } from 'swiper/element/bundle';
import { AuthenticationService } from '../../services/authentication.service';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent {
users: User[] = [];
  message: string = '';
  constructor(private userService: UserService,private fb: FormBuilder,private authService: AuthenticationService) {}

  ngOnInit(): void {
   this.loadSalle();
  }
  loadSalle(){
     this.userService.getUsers().subscribe({
      next: (data) => {
        this.users = data;
      },
      error: (err) => {
        console.error('Erreur lors du chargement des salles', err);
      }
    });
  }

deleteUser(id: number | undefined): void {
  if (id === undefined) {
    console.error("Erreur : ID de salle est undefined.");
    return;
  }

  this.userService.delete(id).subscribe({
    next: () => {
      this.message = 'Salle supprimée avec succès.';
 this.loadSalle();
    },
    error: err => {
      console.error("Erreur lors de la suppression :", err);
      this.message = "Erreur lors de la suppression de la salle.";
    }
  });
}

currentPage: number = 1;
itemsPerPage: number = 5;

get paginatedSalles() {
  const startIndex = (this.currentPage - 1) * this.itemsPerPage;
  return this.users.slice(startIndex, startIndex + this.itemsPerPage);
}

get totalPages() {
  return Math.ceil(this.users.length / this.itemsPerPage);
}

nextPage() {
  if (this.currentPage < this.totalPages) {
    this.currentPage++;
  }
}

prevPage() {
  if (this.currentPage > 1) {
    this.currentPage--;
  }
}







//modale
showAddUserModal = false;

addUserForm = this.fb.group({
   firstname: ['', Validators.required],
  lastname: ['', Validators.required],
  email: ['', [Validators.required, Validators.email]],
  phone: ['']
});

openModal() {
  this.showAddUserModal = true;
}

closeModal() {
  this.showAddUserModal = false;
}

submitAddUser() {
  if (this.addUserForm.invalid) return;

  const newUser = this.addUserForm.value;
  const registerRequest: RegisterRequest = {
        firstname: newUser.firstname!,
        lastname: newUser.lastname!,
        phone: newUser.phone!,
        email: newUser.email!,
        password: ''
      };
  

  this.userService.createUserFromAdmin(registerRequest).subscribe({
    next: () => {
      alert("Utilisateur ajouté avec succès !");
      this.closeModal();
      this.loadSalle(); // recharge les users
    },
    error: (err) => {
      alert("Erreur lors de l'ajout de l'utilisateur");
      console.error(err);
    }
  });
}

}
