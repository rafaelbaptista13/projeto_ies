import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {HospitalService} from '../hospital.service';

@Component({
  selector: 'app-hospital',
  templateUrl: './hospital.component.html',
  styleUrls: ['./hospital.component.css']
})
export class HospitalComponent implements OnInit {
  medicoLogado: boolean;
  medicoId: number;
  regioes = ['Norte', 'Lisboa e Vale do Tejo', 'Centro', 'Alentejo', 'Algarve', 'Açores', 'Madeira'];
  filterForm: FormGroup;
  hospitais: {};
  constructor(private hospitalService: HospitalService) { }

  ngOnInit(): void {
    if (localStorage.getItem('codigo_acesso') != null) {
      this.medicoLogado = true;
      this.medicoId = Number(localStorage.getItem('codigo_acesso'));
    } else {
      this.medicoLogado = false;
    }
    //Função para colapsar navbar
    (function($) {
      $(document).ready(function () {
        $('#sidebarCollapse').on('click', function () {
          $('#sidebar').toggleClass('active');
          $(this).toggleClass('active');
        });
      })
    })(jQuery);

    //Função para colapsar aba de filtros
    (function($) {
      $(document).ready(function () {
        $('#filterCollapse').on('click', function () {
          $('#filters').toggle(500);
        });
      })
    })(jQuery);
    this.initForm();
    this.hospitais = this.hospitalService.getHospitaisFilter('','','','');
  }

  initForm(): void {
    this.filterForm = new FormGroup(
      {
        nome: new FormControl(''),
        regiao: new FormControl(''),
        taxaocupacao_min: new FormControl(''),
        taxaocupacao_max: new FormControl(''),
      }
    )
  }

  onSubmit(filterData): void {
    this.hospitais = this.hospitalService.getHospitaisFilter(filterData.nome, filterData.regiao, filterData.taxaocupacao_min, filterData.taxaocupacao_max);
    this.initForm();
  }
}
