import { Component, OnInit } from '@angular/core';
import {interval, Subscription} from 'rxjs';
import {CasosService} from '../casos.service';
import {FormBuilder} from '@angular/forms';
import {Router} from '@angular/router';
import {MedicService} from '../medic.service';

declare var jQuery: any;
declare const CanvasJS: any;


@Component({
  selector: 'app-casosdiarios',
  templateUrl: './casosdiarios.component.html',
  styleUrls: ['./casosdiarios.component.css']
})
export class CasosdiariosComponent implements OnInit {

  //Declaração de variáveis
  medicoLogado: boolean;
  medicoId: number;
  nomeMedico: string;

  notification_show: boolean;
  closed: boolean;

  casosAtivos: number;
  casosRecuperados: number;
  casosCuidadosIntensivos: number;
  casosMortos: number;
  casosInternados: number;
  probabilidadesGraficoIdades: number[];
  probabilidadesGraficoCurva: {};
  nacionalidades = ['Alemã', 'Espanhola', 'Francesa', 'Italiana', 'Inglesa', 'Brasileira', 'Belga', 'Russa', 'Americana', 'Chinesa', 'Angolana',
    'Moçambicana', 'Holandesa', 'Polaca', 'Cabo-Verdiana', 'Albanesa', 'Austríaca', 'Búlgara', 'Croata', 'Dinamarquesa', 'Eslovaca',
    'Eslovena', 'Finlandesa', 'Grega', 'Húngara', 'Islandesa', 'Irlandesa', 'Lituana', 'Luxemburguesa', 'Norueguesa', 'Romena', 'Sueca',
    'Suíça', 'Turca', 'Ucraniana', 'Argentina', 'Canadiana', 'Mexicana', 'Japonesa', 'Portuguesa'].sort();
  regioes = ['Norte', 'Lisboa e Vale do Tejo', 'Centro', 'Alentejo', 'Algarve', 'Açores', 'Madeira'];
  filterForm;
  chartCurvaObj: any;
  idademin = '';
  idademax = '';
  genero = '';
  regiao = '';
  nacionalidade = '';
  alturamin = '';
  alturamax = '';
  pesomin = '';
  pesomax = '';
  subscription: Subscription;

  //Inicialização com todos os filtros vazios
  constructor(private router: Router, private casosService: CasosService, private formBuilder: FormBuilder, private medicoService: MedicService) {
    this.filterForm = this.formBuilder.group({
      idade_min: '',
      idade_max: '',
      genero: '',
      regiao: '',
      nacionalidade: '',
      altura_min: '',
      altura_max: '',
      peso_min: '',
      peso_max: ''
    });

  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  ngOnInit(): void {
    this.notification_show = false;
    this.closed = false;
    if (localStorage.getItem('codigo_acesso') != null) {
      this.medicoLogado = true;
      this.medicoId = Number(localStorage.getItem('codigo_acesso'));
      this.medicoService.getMedicoById(this.medicoId).subscribe(resultado => {
        this.nomeMedico = 'Dr. ' + resultado.nome.split(' ')[0] + ' ' + resultado.nome.split(' ')[resultado.nome.split(' ').length - 1];
      });
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

    const dropdown = document.getElementsByClassName('dropdown-btn');
    let i;

    for (i = 0; i < dropdown.length; i++) {
      dropdown[i].addEventListener('click', function(): void {
        this.classList.toggle('active');
        const dropdownContent = this.nextElementSibling;
        if (dropdownContent.style.display === 'block') {
          dropdownContent.style.display = 'none';
        } else {
          dropdownContent.style.display = 'block';
        }
      });
    }

    //Get Probabilidades idades e chamada da função de criação de charts
    this.casosService.getProbabilidadeGraficoCurvatura('','','','','',
      '','','','').subscribe(prob => { this.probabilidadesGraficoCurva = prob; this.chartCurva(this.probabilidadesGraficoCurva); });

    //Inicialização do valor das variáveis sem filtros, com chamada á API para obter os valores
    this.casosService.getNumerosPandemia( '', '', '', '', '', '',
      '', '', '').subscribe(resultado => {
        this.casosAtivos = resultado['Ativos'];
        this.casosCuidadosIntensivos = resultado['Cuidados Intensivos'];
        this.casosInternados = resultado['Internados'];
        this.casosMortos = resultado['Óbitos'];
        this.casosRecuperados = resultado['Recuperados'];
    });

    this.casosService.getLastCasosDiariosEvent().subscribe(result => {
      let today = new Date();
      let dd = String(today.getDate()).padStart(2, '0');
      let mm = String(today.getMonth() + 1).padStart(2, '0');
      var yyyy = today.getFullYear();
      let today1 = yyyy +'-'+ mm +'-'+ dd;
      if (result.data.split('T')[0] === today1) {
        this.notification_show = true;
      }

    });

    const source = interval(4000);
    this.subscription = source.subscribe(val => {this.updateGraficos();});

  }

  toggleDropdown(): void{
    const dropdownContent = document.getElementById('c_dropdown');

    if (dropdownContent.style.display === 'block') {
      dropdownContent.style.display = 'none';
    } else {
      dropdownContent.style.display = 'block';
    }
  }

  onSubmit(filterData): void {
    this.idademin = filterData.idade_min;
    this.idademax = filterData.idade_max;
    this.genero = filterData.genero;
    this.regiao = filterData.regiao;
    this.nacionalidade = filterData.nacionalidade;
    this.alturamin = filterData.altura_min;
    this.alturamax = filterData.altura_max;
    this.pesomin = filterData.peso_min;
    this.pesomax = filterData.peso_max;
    //Alteração do valor para o diferente tipo de casos, de acordo com os filtros inseridos
    this.casosService.getNumerosPandemia( filterData.idade_min, filterData.idade_max, filterData.genero, filterData.regiao,
      filterData.nacionalidade, filterData.altura_min, filterData.altura_max, filterData.peso_min, filterData.peso_max).subscribe(resultado => {
      this.casosAtivos = resultado['Ativos'];
      this.casosCuidadosIntensivos = resultado['Cuidados Intensivos'];
      this.casosInternados = resultado['Internados'];
      this.casosMortos = resultado['Óbitos'];
      this.casosRecuperados = resultado['Recuperados'];
    });

    this.casosService.getProbabilidadeGraficoCurvatura(filterData.idade_min, filterData.idade_max, filterData.genero, filterData.regiao,
      filterData.nacionalidade, filterData.altura_min, filterData.altura_max, filterData.peso_min, filterData.peso_max).
    subscribe(prob => { this.probabilidadesGraficoCurva = prob; this.chartCurva(this.probabilidadesGraficoCurva); });

  }

  updateGraficos(): void {
    //Inicialização do valor das variáveis sem filtros, com chamada á API para obter os valores
    this.casosService.getNumerosPandemia( this.idademin, this.idademax, this.genero, this.regiao, this.nacionalidade, this.alturamin,
      this.alturamax, this.pesomin, this.pesomax).subscribe(resultado => {
      this.casosAtivos = resultado['Ativos'];
      this.casosCuidadosIntensivos = resultado['Cuidados Intensivos'];
      this.casosInternados = resultado['Internados'];
      this.casosMortos = resultado['Óbitos'];
      this.casosRecuperados = resultado['Recuperados'];
    });

    this.casosService.getProbabilidadeGraficoCurvaturaDaily(this.idademin, this.idademax, this.genero, this.regiao, this.nacionalidade, this.alturamin,
      this.alturamax, this.pesomin, this.pesomax).subscribe(prob => {
      var length = this.chartCurvaObj.options.data[0].dataPoints.length;
      this.chartCurvaObj.options.data[0].dataPoints[length - 1].y = prob;
      this.chartCurvaObj.render();
    });

    this.casosService.getLastCasosDiariosEvent().subscribe(result => {
      let today = new Date();
      let dd = String(today.getDate()).padStart(2, '0');
      let mm = String(today.getMonth() + 1).padStart(2, '0');
      var yyyy = today.getFullYear();
      let today1 = yyyy +'-'+ mm +'-'+ dd;
      if (result.data.split('T')[0] === today1 && this.closed === false) {
        this.notification_show = true;
      }

    });
  }

  //Função para especificar valores dos charts
  chartCurva(probabilidadesGraficoCurva): void {

    // grafico todos os casos diarios
    var array_valores = [];
    for (let date in probabilidadesGraficoCurva) {
      array_valores.push({x: new Date(date), y: probabilidadesGraficoCurva[date]});
    }
    array_valores = array_valores.sort((n1, n2) => {
      if (n1.x > n2.x) {
        return 1;
      }

      if (n1.x < n2.x) {
        return -1;
      }

      return 0;
    });

    this.chartCurvaObj = new CanvasJS.Chart("allcasesgraph", {
      animationEnabled: true,
      title:{
        text: ""
      },
      axisX:{
        valueFormatString: "DD MMM",
        crosshair: {
          enabled: true,
          snapToDataPoint: true
        }
      },
      axisY: {
        title: "Número de casos",
        valueFormatString: "##",
        crosshair: {
          enabled: true,
          snapToDataPoint: true,
          labelFormatter: function(e) {
            return CanvasJS.formatNumber(e.value, "##") + " infeções";
          }
        }
      },
      data: [{
        type: "area",
        xValueFormatString: "DD MMM",
        yValueFormatString: "##",
        dataPoints: array_valores
      }]
    });
    this.chartCurvaObj.render();

  }

  close_notification(): void {
    this.notification_show = false;
    this.closed = true;
  }

  logout(): void {
    localStorage.removeItem('codigo_acesso');
    localStorage.removeItem('token');
    this.router.navigate(['/home']);
  }

}
