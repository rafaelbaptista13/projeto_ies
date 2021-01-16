import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  medicoLogado: boolean;
  medicoId: number;

  constructor(private router: Router) {}

  ngOnInit(): void {
    if (localStorage.getItem('codigo_acesso') != null) {
      this.medicoLogado = true;
      this.medicoId = Number(localStorage.getItem('codigo_acesso'));
    } else {
      this.medicoLogado = false;
    }
  }

  logout(): void {
    localStorage.removeItem('codigo_acesso');
    localStorage.removeItem('token');
    this.ngOnInit();
  }
}
