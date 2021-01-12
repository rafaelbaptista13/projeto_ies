import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {FormControl, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-paciente',
  templateUrl: './paciente.component.html',
  styleUrls: ['./paciente.component.css']
})
export class PacienteComponent implements OnInit {
  private id: string;
  formPaciente: FormGroup;
  constructor(private route: ActivatedRoute) {
    this.id = route.snapshot.paramMap.get('id');
    if (this.id){
      this.editPaciente();
    }
    else{
      this.addPaciente();
    }
  }

  ngOnInit(): void {
    //Função para colapsar navbar
    (function($) {
      $(document).ready(function () {
        $('#sidebarCollapse').on('click', function () {
          $('#sidebar').toggleClass('active');
          $(this).toggleClass('active');
        });
      });
    })(jQuery);

    //Função para colapsar aba de filtros
    (function($) {
      $(document).ready(function () {
        $('#filterCollapse').on('click', function () {
          $('#filters').toggle(500);
        });
      });
    })(jQuery);
  }

  editPaciente(): void{
    // Chamar serviço e ir buscar os valores e inicializar
    this.formPaciente = new FormGroup(
      {
        nome: new FormControl(''),
        regiao: new FormControl(''),
        taxaocupacao_min: new FormControl(''),
        taxaocupacao_max: new FormControl(''),
      }
    );
  }

  addPaciente(): void{
    this.formPaciente = new FormGroup(
      {
        nome: new FormControl(''),
        regiao: new FormControl(''),
        taxaocupacao_min: new FormControl(''),
        taxaocupacao_max: new FormControl(''),
      }
    );
  }
}
