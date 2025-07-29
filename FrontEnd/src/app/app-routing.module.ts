import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { WelcomeComponent } from './components/welcome/welcome.component';
import { MfaComponent } from './components/mfa/mfa.component';
import { ForgetPasswordComponent } from './components/forget-password/forget-password.component';
import { ListSalleComponent } from './components/salle/list-salle/list-salle.component';
import { AddSalleComponent } from './components/salle/add-salle/add-salle.component';
import { ListEquipmentComponent } from './components/equipment/list-equipment/list-equipment.component';
import { EquipmentFormComponent } from './components/equipment/equipment-form/equipment-form.component';
import { SidebarComponent } from './components/sidebar/sidebar.component';
import { ReservationFormComponent } from './components/reservation/reservation-form/reservation-form.component';
import { CalendarComponent } from './components/calendar/calendar.component';
import { ViewSalleComponent } from './components/salle/view-salle/view-salle.component';
import { ConfigurationHoraireComponent } from './components/configuration-horaire/configuration-horaire.component';
import { AdminConfigCalendarComponent } from './components/admin-config-calendar/admin-config-calendar.component';

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
  {
    path: 'salles-view',
    component: ViewSalleComponent
  },
  { path: 'salle/new', component: AddSalleComponent },
  {
    path: 'equipments',
    component: ListEquipmentComponent
  },
  { path: 'equipment/add', component: EquipmentFormComponent },
  { path: 'equipment/edit/:id', component: EquipmentFormComponent },
  { path: 'reservation/add', component: ReservationFormComponent },
   // redirection par défaut vers login
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'sidebar', component: SidebarComponent },
  {
    path: 'calender',
    component: CalendarComponent
  },
   {
    path: 'config',
    component: AdminConfigCalendarComponent
  },
   {
    path: 'config1',
    component: ConfigurationHoraireComponent
  },
  // Optionnel : redirection wildcard vers login si route inconnue
  { path: '**', redirectTo: '/login' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
