

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
    AdminConfigCalendarComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule,
    MatGridListModule,
    NgxPaginationModule,
    FullCalendarModule,
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
