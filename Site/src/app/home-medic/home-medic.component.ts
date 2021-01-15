import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup} from "@angular/forms";
import {MedicService} from "../medic.service";
import {Router} from '@angular/router';
import {PacienteService} from '../paciente.service';

@Component({
  selector: 'app-home-medic',
  templateUrl: './home-medic.component.html',
  styleUrls: ['./home-medic.component.css']
})
export class HomeMedicComponent implements OnInit {
  medicoLogado: boolean;
  medicoId: number;
  filterForm: FormGroup;
  estados = ['Ativos', 'Confinamento Domiciliário', 'Internado', 'Cuidados Intensivos', 'Recuperado', 'Óbito'];
  pacientes = {};

  constructor(private router: Router, private medicService: MedicService, private pacienteService: PacienteService) { }

  ngOnInit(): void {
    if (localStorage.getItem('codigo_acesso') != null) {
      this.medicoLogado = true;
      this.medicoId = Number(localStorage.getItem('codigo_acesso'));
    } else {
      this.router.navigate(['/login']);
    }

    // Função para colapsar navbar
    (function($) {
      $(document).ready(function () {
        $('#sidebarCollapse').on('click', function () {
          $('#sidebar').toggleClass('active');
          $(this).toggleClass('active');
        });
      });
    })(jQuery);

    // Função para colapsar aba de filtros
    (function($) {
      $(document).ready(function () {
        $('#filterCollapse').on('click', function () {
          $('#filters').toggle(500);
        });
      });
    })(jQuery);
    const medico_id = localStorage.getItem('codigo_acesso');
    this.medicService.getPacientsFilter(medico_id, '', '', 'Ativos').subscribe(pacientes => {this.pacientes = pacientes; },
      error => {
        this.router.navigate(['/login']);
      });
    this.initForm(true);
  }
  initForm(inicial: boolean): void {
    if (inicial) {
      this.filterForm = new FormGroup(
        {
          num_paciente: new FormControl(''),
          nome: new FormControl(''),
          estado: new FormControl('Ativos'),
        }
      );
    } else {
      this.filterForm = new FormGroup(
        {
          num_paciente: new FormControl(''),
          nome: new FormControl(''),
          estado: new FormControl(''),
        }
      );
    }
  }

  onSubmit(filterData): void {
    if (this.filterForm.valid){
      const medico_id = localStorage.getItem('codigo_acesso');
      this.medicService.getPacientsFilter(medico_id, filterData.num_paciente, filterData.nome, filterData.estado).subscribe(pacientes => {this.pacientes = pacientes; },
        error => {
          this.router.navigate(['/login']);
        } );
      this.initForm(false);
    }
  }

  eliminar(id: number): void{
    this.pacienteService.deletePacient(id).subscribe(sucesso => {this.ngOnInit()},
      error => {
        this.router.navigate(['/login']);
      });
  }

}
