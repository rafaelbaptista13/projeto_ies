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
  loginForm: FormGroup;
  logged = false;

  constructor(private loginService: LoginService, private formBuilder: FormBuilder, private router: Router ) {

  }

  initForm(): void{
    this.loginForm = new FormGroup({
      codigo_acesso: new FormControl('', [Validators.required]),
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
    this.initForm();
  }

  // Função chamada quando é submetido um novo formulário de filtros
  onSubmit(): void {
    if (this.loginForm.valid){
      // Alteração do valor para o diferente tipo de casos, de acordo com os filtros inseridos
      this.loginService.LoginValidation(this.loginForm.value).
      subscribe(logged => {console.log(logged); this.router.navigate(['/home']);  }, error => { this.ngOnInit(); } );
    }
  }
}
