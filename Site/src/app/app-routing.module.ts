import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { StatisticsComponent } from './statistics/statistics.component';
import { HomeComponent } from './home/home.component';
import { LoginComponent} from './login/login.component';
import {HospitalComponent} from "./hospital/hospital.component";

//Paths disponiveis
const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'statistics', component: StatisticsComponent},
  {path: 'home', component: HomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'hospital', component: HospitalComponent}
]

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
