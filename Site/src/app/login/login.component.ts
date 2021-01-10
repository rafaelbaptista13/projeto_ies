import { Component, OnInit } from '@angular/core';
import {FormBuilder} from '@angular/forms';
import {CasosService} from "../casos.service";

declare var jQuery: any;


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})

export class LoginComponent implements OnInit {
  loginForm;

  constructor(private formBuilder: FormBuilder ) {
    this.loginForm = this.formBuilder.group({
      codigo : '',
      numero_medico: ''
    });
  }

  ngOnInit(): void {
  }

}
