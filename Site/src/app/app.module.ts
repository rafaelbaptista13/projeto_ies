import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';

import { AppComponent } from './app.component';
import { StatisticsComponent } from './statistics/statistics.component';
import { AppRoutingModule } from './app-routing.module';
import { HomeComponent } from './home/home.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { HospitalComponent } from './hospital/hospital.component';

import * as $ from 'jquery';
import { LoginComponent } from './login/login.component';
import { HomeMedicComponent } from './home-medic/home-medic.component';
import { PacienteComponent } from './paciente/paciente.component';
import { AuthInterceptor } from './AuthInterceptor';
import { InserirpacienteComponent } from './inserirpaciente/inserirpaciente.component';
import { AboutusComponent } from './aboutus/aboutus.component';

@NgModule({
  declarations: [
    AppComponent,
    StatisticsComponent,
    HomeComponent,
    HospitalComponent,
    LoginComponent,
    HomeMedicComponent,
    PacienteComponent,
    InserirpacienteComponent,
    AboutusComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
