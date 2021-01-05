import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import { LoginService} from "../login.service";

declare var jQuery: any;


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  logged = false;

  constructor(private loginService: LoginService, private formBuilder: FormBuilder ) {
    this.initForm();
  }

  initForm(): void{
    this.loginForm = new FormGroup({
      codigo: new FormControl('', [Validators.required]),
      numero_medico: new FormControl('', [Validators.required])
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
      });
    })(jQuery);
  }

  // Função chamada quando é submetido um novo formulário de filtros
  onSubmit(): void {
    if (this.loginForm.valid){
      // Alteração do valor para o diferente tipo de casos, de acordo com os filtros inseridos
      this.loginService.LoginValidation(this.loginForm.value).
      subscribe(logged => this.logged = logged);
    }
  }
}
