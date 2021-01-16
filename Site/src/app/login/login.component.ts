import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import { LoginService} from "../login.service";
import {Router} from '@angular/router';

declare var jQuery: any;


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})

export class LoginComponent implements OnInit {
  erroCredenciais: boolean;
  loginForm: FormGroup;
  public medicoLogado: boolean = false;

  constructor(private loginService: LoginService, private formBuilder: FormBuilder, private router: Router ) {

  }

  initForm(): void{
    this.loginForm = new FormGroup({
      codigo_acesso: new FormControl('', [Validators.required]),
      numero_medico: new FormControl('', [Validators.required])
    });
  }
  ngOnInit(): void {
    this.erroCredenciais = false;
    // Função para colapsar navbar
    (function($) {
      $(document).ready(function () {
        $('#sidebarCollapse').on('click', function () {
          $('#sidebar').toggleClass('active');
          $(this).toggleClass('active');
        });
      });
    })(jQuery);

    this.initForm();
  }

  // Função chamada quando é submetido um novo formulário de filtros
  onSubmit(): void {
    if (this.loginForm.valid){
      // Alteração do valor para o diferente tipo de casos, de acordo com os filtros inseridos
      this.loginService.LoginValidation(this.loginForm.value).
      subscribe(logged => {console.log(localStorage.getItem('token')); this.router.navigate(['/homeMedic']); this.medicoLogado = true; }, error => {this.erroCredenciais = true; this.initForm(); } );
    }
  }

  logout(): void {
    localStorage.removeItem('codigo_acesso');
    localStorage.removeItem('token');
    this.router.navigate(['/home']);
  }
}
