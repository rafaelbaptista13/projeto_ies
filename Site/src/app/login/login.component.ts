import { Component, OnInit } from '@angular/core';
import {FormBuilder} from '@angular/forms';
import { LoginService} from "../login.service";

declare var jQuery: any;


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  loginForm;
  logged = false;

  constructor(private loginService: LoginService, private formBuilder: FormBuilder ) {
    this.loginForm = this.formBuilder.group({
      codigo : '',
      numero_medico: ''
    });
  }

  ngOnInit(): void {
    // Função para colapsar navbar
    (function($) {
      $(document).ready(function () {
        $('#sidebarCollapse').on('click', function () {
          $('#sidebar').toggleClass('active');
          $(this).toggleClass('active');
        });
      })
    })(jQuery);
  }

  // Função chamada quando é submetido um novo formulário de filtros
  onSubmit(loginData): void {
    console.log(loginData);
    // Alteração do valor para o diferente tipo de casos, de acordo com os filtros inseridos
    this.loginService.LoginValidation(loginData.numero_medico, loginData.codigo_acesso).
    subscribe(logged => this.logged = logged);
  }
}
