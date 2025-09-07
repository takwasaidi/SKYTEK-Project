

import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { WelcomeComponent } from './components/welcome/welcome.component';
import {HTTP_INTERCEPTORS, HttpClient, HttpClientModule} from "@angular/common/http";
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MfaComponent } from './components/mfa/mfa.component';
import { ForgetPasswordComponent } from './components/forget-password/forget-password.component';
import { HttpTokenInterceptor } from './interceptor/http-token.interceptor';
import { ListSalleComponent } from './components/salle/list-salle/list-salle.component';
import { AddSalleComponent } from './components/salle/add-salle/add-salle.component';
import { MatGridListModule } from '@angular/material/grid-list';
import { ListEquipmentComponent } from './components/equipment/list-equipment/list-equipment.component';
import { EquipmentFormComponent } from './components/equipment/equipment-form/equipment-form.component';
import { SidebarComponent } from './components/sidebar/sidebar.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { NgxPaginationModule } from 'ngx-pagination';
import { ReservationFormComponent } from './components/reservation/reservation-form/reservation-form.component';
import { CalendarComponent } from './components/calendar/calendar.component';
import { FullCalendarModule } from '@fullcalendar/angular';
import { ViewSalleComponent } from './components/salle/view-salle/view-salle.component';
import { ConfigurationHoraireComponent } from './components/configuration-horaire/configuration-horaire.component';
import { AdminConfigCalendarComponent } from './components/admin-config-calendar/admin-config-calendar.component';
import { NavComponent } from './components/nav/nav.component';
import { SignInComponent } from './components/sign-in/sign-in.component';
import { SignUpComponent } from './components/sign-up/sign-up.component';
import { FooterComponent } from './components/footer/footer.component';
import { ReservationComponent } from './components/reservation/reservation/reservation.component';
import { MainLayoutComponent } from './layouts/main-layout/main-layout.component';
import { AuthLayoutComponent } from './layouts/auth-layout/auth-layout.component';
import { AdminLayoutComponent } from './layouts/admin-layout/admin-layout.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { ReservationCalenderComponent } from './components/reservation/reservation-calender/reservation-calender.component';
import { EntrepriseComponent } from './components/entreprise/entreprise.component';
import { UserComponent } from './components/user/user.component';
import { QuotaComponent } from './components/quota/quota.component';
import { QuotaAlertsComponent } from './components/quota-alerts/quota-alerts.component';
import { ToastrModule } from 'ngx-toastr';
import { ToastComponent } from './components/toast/toast.component';
import { ReservationHistoComponent } from './components/reservation/reservation-histo/reservation-histo.component';
import { HomeComponent } from './components/home/home.component';
import { FilterdSalleComponent } from './components/salle/filterd-salle/filterd-salle.component';
import { CommonModule } from '@angular/common';
import { QuotaUserComponent } from './components/reservation/quota-user/quota-user.component';
import { NotificationComponent } from './components/notification/notification.component';

import { ReclamationsComponent } from './components/reclamations/reclamations.component';
import { ReclamationComponent } from './components/reclamations/reclamation/reclamation.component';
import { ReclamationhistoComponent } from './components/reclamations/reclamationhisto/reclamationhisto.component';
import { PaiementsComponent } from './components/paiements/paiements.component';
import { EditSalleComponent } from './components/salle/edit-salle/edit-salle.component';


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    WelcomeComponent,
    MfaComponent,
    ForgetPasswordComponent,
    ListSalleComponent,
    AddSalleComponent,
    ListEquipmentComponent,
    EquipmentFormComponent,
    SidebarComponent,
    NavbarComponent,
    ReservationFormComponent,
    CalendarComponent,
    ViewSalleComponent,
    ConfigurationHoraireComponent,
    AdminConfigCalendarComponent,
    NavComponent,
    SignInComponent,
    SignUpComponent,
    FooterComponent,
    ReservationComponent,
    MainLayoutComponent,
    AuthLayoutComponent,
    AdminLayoutComponent,
    ReservationCalenderComponent,
    EntrepriseComponent,
    UserComponent,
    QuotaComponent,
    QuotaAlertsComponent,
    ToastComponent,
    ReservationHistoComponent,
    HomeComponent,
    FilterdSalleComponent,
    QuotaUserComponent,
    NotificationComponent,
    ReclamationComponent,
    ReclamationhistoComponent,
    ReclamationsComponent,
    PaiementsComponent,
    EditSalleComponent
  
   
  ],
  imports: [
      CommonModule,
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule,
    MatGridListModule,
    NgxPaginationModule,
    FullCalendarModule,
    BrowserAnimationsModule,
    MatSnackBarModule,
    ToastrModule.forRoot(),
   
  ],
  providers: [
     {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpTokenInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent],
   schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AppModule { }
