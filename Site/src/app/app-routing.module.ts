import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { StatisticsComponent } from './statistics/statistics.component';
import { HomeComponent } from './home/home.component';
import {HospitalComponent} from "./hospital/hospital.component";
import {LoginComponent} from './login/login.component';
import {HomeMedicComponent} from "./home-medic/home-medic.component";
import {PacienteComponent} from "./paciente/paciente.component";
import {InserirpacienteComponent} from './inserirpaciente/inserirpaciente.component';
import {AboutusComponent} from './aboutus/aboutus.component';

//Paths disponiveis
const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'statistics', component: StatisticsComponent},
  {path: 'home', component: HomeComponent},
  {path: 'hospital', component: HospitalComponent},
  {path: 'login', component: LoginComponent},
  {path: 'homeMedic', component: HomeMedicComponent},
  {path: 'pacient', component: InserirpacienteComponent},
  {path: 'pacient/:id', component: PacienteComponent},
  {path: 'aboutus', component: AboutusComponent},
];

@NgModule({
  declarations: [],
  imports: [
    RouterModule.forRoot(routes)
  ],
  exports: [
    RouterModule
  ]
})
export class AppRoutingModule { }
