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
import { NavComponent } from './components/nav/nav.component';
import { SignInComponent } from './components/sign-in/sign-in.component';
import { SignUpComponent } from './components/sign-up/sign-up.component';
import { ReservationComponent } from './components/reservation/reservation/reservation.component';
import { AuthLayoutComponent } from './layouts/auth-layout/auth-layout.component';
import { MainLayoutComponent } from './layouts/main-layout/main-layout.component';
import { AdminLayoutComponent } from './layouts/admin-layout/admin-layout.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { ReservationCalenderComponent } from './components/reservation/reservation-calender/reservation-calender.component';
import { EntrepriseComponent } from './components/entreprise/entreprise.component';
import { UserComponent } from './components/user/user.component';
import { QuotaComponent } from './components/quota/quota.component';
import { QuotaAlertsComponent } from './components/quota-alerts/quota-alerts.component';
import { ReservationHistoComponent } from './components/reservation/reservation-histo/reservation-histo.component';
import { HomeComponent } from './components/home/home.component';
import { FilterdSalleComponent } from './components/salle/filterd-salle/filterd-salle.component';
import { QuotaUserComponent } from './components/reservation/quota-user/quota-user.component';
import { ReclamationComponent } from './components/reclamations/reclamation/reclamation.component';
import { ReclamationhistoComponent } from './components/reclamations/reclamationhisto/reclamationhisto.component';
import { ReclamationsComponent } from './components/reclamations/reclamations.component';
import { PaiementsComponent } from './components/paiements/paiements.component';
import { EditSalleComponent } from './components/salle/edit-salle/edit-salle.component';
import { AuthGuard } from './guards/auth.guard';

const routes: Routes = [
  {
    path: '',
    component: MainLayoutComponent,
    children: [

   {
    path: 'home',
    component: HomeComponent
  },
  {
    path: 'welcome',
    component: WelcomeComponent
  },
   { path: 'mfa', component: MfaComponent }, 
   { path: 'forgot-password', component: ForgetPasswordComponent },
    {
    path: 'reservation',
    component: ReservationComponent
  },
   {
    path: 'Filtred-salles',
    component: FilterdSalleComponent
  },
    {
    path: 'calenderRes',
    component: ReservationCalenderComponent
  },
    { path: 'reservation-salle', component: ReservationFormComponent },
    { path: 'quota', component: QuotaUserComponent },
   {
    path: 'reservationhisto',
    component: ReservationHistoComponent
  },
   {
    path: 'reclamation',
    component: ReclamationComponent
  },
   {
    path: 'reclamations',
    component: ReclamationhistoComponent
  },
  {
    path: 'paiement',
    component: PaiementsComponent
  },
   { path: '', redirectTo: '/home', pathMatch: 'full' },
    ]
  },
  {
    path: '',
    component: AuthLayoutComponent,
    children: [
        {
    path: 'signin',
    component: SignInComponent
  },
  {
    path: 'signup',
    component: SignUpComponent
  }
      // no nav/footer
    ]
  },
   {
    path: '',
    component: AdminLayoutComponent,
    children: [

{
    path: 'salles',
    component: ListSalleComponent
  },
  {
    path: 'salles-view',
    component: ViewSalleComponent
  },
  { path: 'salle/new', component: AddSalleComponent },
  { path: 'salle/edit/:id', component: EditSalleComponent } ,
  {
    path: 'equipments',
    component: ListEquipmentComponent
  },
   {
    path: 'entreprises',
    component: EntrepriseComponent
  },
  { path: 'equipment/add', component: EquipmentFormComponent },
  { path: 'equipment/edit/:id', component: EquipmentFormComponent },


  { path: 'sidebar', component: SidebarComponent },
   { path: 'navbar', component: NavbarComponent },
  {
    path: 'calender',
    component: CalendarComponent
  },
   {
    path: 'config',
    component: AdminConfigCalendarComponent
  },
   {
    path: 'config-horaire',
    component: ConfigurationHoraireComponent
  },
  {
    path: 'utilisateurs',
    component: UserComponent
  },
  {
    path: 'admin/quota',
    component: QuotaComponent
  },
  { path: 'admin/quota-alerts', component: QuotaAlertsComponent },
   { path: 'admin/reclamations', component: ReclamationsComponent },

    
  
    ]
  },
   { path: '', redirectTo: '/utilisateurs', pathMatch: 'full' },
  { path: '**', redirectTo: 'utilisateurs' }
];









@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
