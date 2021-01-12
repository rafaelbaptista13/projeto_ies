import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup} from "@angular/forms";
import {MedicService} from "../medic.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-home-medic',
  templateUrl: './home-medic.component.html',
  styleUrls: ['./home-medic.component.css']
})
export class HomeMedicComponent implements OnInit {
  filterForm: FormGroup;
  estados = ['Ativo', 'Óbito', 'Internado', 'Intesivo', 'Recuperado'];
  public pacientes: {};
  constructor(private medicService: MedicService, private router: Router) { }

  ngOnInit(): void {
    this.medicService.getPacientsFilter().subscribe(pacientes => this.pacientes = pacientes);
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
    this.initForm();
  }
  initForm(): void {
    this.filterForm = new FormGroup(
      {
        num_paciente: new FormControl(''),
        nome: new FormControl(''),
        estado: new FormControl(''),
      }
    );
  }

  onSubmit(filterData): void {
    if (this.filterForm.valid){
      this.medicService.getPacientsFilter(filterData.num_paciente, filterData.nome, filterData.estado).subscribe(pacientes => this.pacientes = pacientes);
      this.initForm();
    }
  }

  addPacient(): void{
    this.router.navigate(['/pacient']);
  }
}
