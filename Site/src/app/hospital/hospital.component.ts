import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {HospitalService} from '../hospital.service';
import {Router} from '@angular/router';
import {MedicService} from '../medic.service';
import {interval, Subscription} from 'rxjs';

@Component({
  selector: 'app-hospital',
  templateUrl: './hospital.component.html',
  styleUrls: ['./hospital.component.css']
})
export class HospitalComponent implements OnInit {
  medicoLogado: boolean;
  medicoId: number;
  nomeMedico: string;
  regioes = ['Norte', 'Lisboa e Vale do Tejo', 'Centro', 'Alentejo', 'Algarve', 'Açores', 'Madeira'];
  filterForm: FormGroup;
  hospitais: {};


  nome = '';
  regiao = '';
  taxaocupacao_min = '';
  taxaocupacao_max = '';
  subscription: Subscription;

  constructor(private router: Router, private hospitalService: HospitalService, private medicoService: MedicService) { }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  ngOnInit(): void {
    if (localStorage.getItem('codigo_acesso') != null) {
      this.medicoLogado = true;
      this.medicoId = Number(localStorage.getItem('codigo_acesso'));
      this.medicoService.getMedicoById(this.medicoId).subscribe(resultado => {
        this.nomeMedico = 'Dr. ' + resultado.nome.split(' ')[0] + ' ' + resultado.nome.split(' ')[resultado.nome.split(' ').length - 1];
      });
    } else {
      this.medicoLogado = false;
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
      });
    })(jQuery);
    this.initForm();
    this.hospitais = this.hospitalService.getHospitaisFilter('','','','');

    const source = interval(10000);
    this.subscription = source.subscribe(val => {this.updateHospitais();});
  }

  updateHospitais() {
    this.hospitais = this.hospitalService.getHospitaisFilter(this.nome, this.regiao, this.taxaocupacao_min, this.taxaocupacao_max);
  }

  toggleDropdown(): void{
    const dropdownContent = document.getElementById('c_dropdown');

    if (dropdownContent.style.display === 'block') {
      dropdownContent.style.display = 'none';
    } else {
      dropdownContent.style.display = 'block';
    }
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
    this.nome = filterData.nome;
    this.regiao = filterData.regiao;
    this.taxaocupacao_min = filterData.taxaocupacao_min;
    this.taxaocupacao_max = filterData.taxaocupacao_max;
    this.hospitais = this.hospitalService.getHospitaisFilter(filterData.nome, filterData.regiao, filterData.taxaocupacao_min, filterData.taxaocupacao_max);
    this.initForm();
  }

  logout(): void {
    localStorage.removeItem('codigo_acesso');
    localStorage.removeItem('token');
    this.router.navigate(['/home']);
  }
}
