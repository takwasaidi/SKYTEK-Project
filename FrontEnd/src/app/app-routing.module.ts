import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { WelcomeComponent } from './components/welcome/welcome.component';
import { MfaComponent } from './components/mfa/mfa.component';
import { ForgetPasswordComponent } from './components/forget-password/forget-password.component';
import { ListSalleComponent } from './components/salle/list-salle/list-salle.component';
import { AddSalleComponent } from './components/salle/add-salle/add-salle.component';

const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'register',
    component: RegisterComponent
  },
  {
    path: 'welcome',
    component: WelcomeComponent
  },
   { path: 'mfa', component: MfaComponent }, 
   { path: 'forgot-password', component: ForgetPasswordComponent },
{
    path: 'salles',
    component: ListSalleComponent
  },
  { path: 'salle/new', component: AddSalleComponent },
   // redirection par défaut vers login
  { path: '', redirectTo: '/login', pathMatch: 'full' },

  // Optionnel : redirection wildcard vers login si route inconnue
  { path: '**', redirectTo: '/login' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
