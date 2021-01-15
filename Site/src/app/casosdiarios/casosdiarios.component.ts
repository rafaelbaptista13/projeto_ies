import { Component, OnInit } from '@angular/core';
import {interval, Subscription} from 'rxjs';
import {CasosService} from '../casos.service';
import {FormBuilder} from '@angular/forms';

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
  constructor(private casosService: CasosService, private formBuilder: FormBuilder) {
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

    //Get Probabilidades idades e chamada da função de criação de charts
    this.casosService.getProbabilidadeGraficoCurvatura('','','','','',
      '','','','').subscribe(prob => { this.probabilidadesGraficoCurva = prob; this.chartCurva(this.probabilidadesGraficoCurva); });

    //Inicialização do valor das variáveis sem filtros, com chamada á API para obter os valores
    this.casosService.getNumeroCasos('ativos', '', '', '', '', '', '',
      '', '', '').subscribe(casosAtivos => this.casosAtivos = casosAtivos);
    this.casosService.getNumeroCasos('Recuperado', '', '', '', '', '', '',
      '', '', '').subscribe(casosRecuperados => this.casosRecuperados = casosRecuperados);
    this.casosService.getNumeroCasos('Cuidados+Intensivos', '', '', '', '', '', '',
      '', '', '').subscribe(casosCuidadosIntensivos => this.casosCuidadosIntensivos = casosCuidadosIntensivos);
    this.casosService.getNumeroCasos('Óbito', '', '', '', '', '', '',
      '', '', '').subscribe(casosMortos => this.casosMortos = casosMortos);
    this.casosService.getNumeroCasos('Internado', '', '', '', '', '', '',
      '', '', '').subscribe(casosInternados => this.casosInternados = casosInternados);

    const source = interval(10000);
    this.subscription = source.subscribe(val => this.updateGraficos());

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
    this.casosService.getProbabilidadeGraficoCurvatura(filterData.idade_min, filterData.idade_max, filterData.genero, filterData.regiao,
      filterData.nacionalidade, filterData.altura_min, filterData.altura_max, filterData.peso_min, filterData.peso_max).
    subscribe(prob => { this.probabilidadesGraficoCurva = prob; this.chartCurva(this.probabilidadesGraficoCurva); });
    this.casosService.getNumeroCasos('ativos', filterData.idade_min, filterData.idade_max, filterData.genero, filterData.regiao,
      filterData.nacionalidade, filterData.altura_min, filterData.altura_max, filterData.peso_min, filterData.peso_max).
    subscribe(casosAtivos => this.casosAtivos = casosAtivos);
    this.casosService.getNumeroCasos('Recuperado', filterData.idade_min, filterData.idade_max, filterData.genero, filterData.regiao,
      filterData.nacionalidade, filterData.altura_min, filterData.altura_max, filterData.peso_min, filterData.peso_max).
    subscribe(casosRecuperados => this.casosRecuperados = casosRecuperados);
    this.casosService.getNumeroCasos('Cuidados+Intensivos', filterData.idade_min, filterData.idade_max, filterData.genero, filterData.regiao,
      filterData.nacionalidade, filterData.altura_min, filterData.altura_max, filterData.peso_min, filterData.peso_max).
    subscribe(casosCuidadosIntensivos => this.casosCuidadosIntensivos = casosCuidadosIntensivos);
    this.casosService.getNumeroCasos('Óbito', filterData.idade_min, filterData.idade_max, filterData.genero, filterData.regiao,
      filterData.nacionalidade, filterData.altura_min, filterData.altura_max, filterData.peso_min, filterData.peso_max).
    subscribe(casosMortos => this.casosMortos = casosMortos);
    this.casosService.getNumeroCasos('Internado', filterData.idade_min, filterData.idade_max, filterData.genero, filterData.regiao,
      filterData.nacionalidade, filterData.altura_min, filterData.altura_max, filterData.peso_min, filterData.peso_max).
    subscribe(casosInternados => this.casosInternados = casosInternados);

  }

  updateGraficos(): void {
    console.log("entrei");
    //Inicialização do valor das variáveis sem filtros, com chamada á API para obter os valores
    this.casosService.getNumeroCasos('ativos', this.idademin, this.idademax, this.genero, this.regiao, this.nacionalidade, this.alturamin,
      this.alturamax, this.pesomin, this.pesomax).subscribe(casosAtivos => this.casosAtivos = casosAtivos);
    this.casosService.getNumeroCasos('Recuperado', this.idademin, this.idademax, this.genero, this.regiao, this.nacionalidade, this.alturamin,
      this.alturamax, this.pesomin, this.pesomax).subscribe(casosRecuperados => this.casosRecuperados = casosRecuperados);
    this.casosService.getNumeroCasos('Cuidados+Intensivos', this.idademin, this.idademax, this.genero, this.regiao, this.nacionalidade, this.alturamin,
      this.alturamax, this.pesomin, this.pesomax).subscribe(casosCuidadosIntensivos => this.casosCuidadosIntensivos = casosCuidadosIntensivos);
    this.casosService.getNumeroCasos('Óbito', this.idademin, this.idademax, this.genero, this.regiao, this.nacionalidade, this.alturamin,
      this.alturamax, this.pesomin, this.pesomax).subscribe(casosMortos => this.casosMortos = casosMortos);
    this.casosService.getNumeroCasos('Internado', this.idademin, this.idademax, this.genero, this.regiao, this.nacionalidade, this.alturamin,
      this.alturamax, this.pesomin, this.pesomax).subscribe(casosInternados => this.casosInternados = casosInternados);

    this.casosService.getProbabilidadeGraficoCurvaturaDaily(this.idademin, this.idademax, this.genero, this.regiao, this.nacionalidade, this.alturamin,
      this.alturamax, this.pesomin, this.pesomax).subscribe(prob => {
      var length = this.chartCurvaObj.options.data[0].dataPoints.length;
      this.chartCurvaObj.options.data[0].dataPoints[length - 1].y = prob;
      this.chartCurvaObj.render();
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
}
