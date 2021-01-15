import { Component, OnInit } from '@angular/core';
import {CasosService} from '../casos.service';
import {FormBuilder} from '@angular/forms';
import {filter} from 'rxjs/operators';
import { interval, Subscription } from 'rxjs';

declare var jQuery: any;
declare const CanvasJS: any;


@Component({
  selector: 'app-statistics',
  templateUrl: './statistics.component.html',
  styleUrls: ['./statistics.component.css']
})
export class StatisticsComponent implements OnInit {
  //Declaração de variáveis
  medicoLogado: boolean;
  medicoId: number;

  probabilidadesGraficoIdades: number[];
  probabilidadesGraficoRegioes: number[];
  probabilidadesGraficoGeneros: number[];
  probabilidadesGraficoAlturas: number[];
  probabilidadesGraficoPesos: number[];
  nacionalidades = ['Alemã', 'Espanhola', 'Francesa', 'Italiana', 'Inglesa', 'Brasileira', 'Belga', 'Russa', 'Americana', 'Chinesa', 'Angolana',
    'Moçambicana', 'Holandesa', 'Polaca', 'Cabo-Verdiana', 'Albanesa', 'Austríaca', 'Búlgara', 'Croata', 'Dinamarquesa', 'Eslovaca',
    'Eslovena', 'Finlandesa', 'Grega', 'Húngara', 'Islandesa', 'Irlandesa', 'Lituana', 'Luxemburguesa', 'Norueguesa', 'Romena', 'Sueca',
    'Suíça', 'Turca', 'Ucraniana', 'Argentina', 'Canadiana', 'Mexicana', 'Japonesa', 'Portuguesa'].sort();
  regioes = ['Norte', 'Lisboa e Vale do Tejo', 'Centro', 'Alentejo', 'Algarve', 'Açores', 'Madeira'];
  filterForm;
  chartIdadesObj: any;
  chartRegioesObj: any;
  chartGenerosObj: any;
  chartAlturasObj: any;
  chartPesosObj: any;
  idademin = '';
  idademax = '';
  genero = '';
  regiao = '';
  nacionalidade = '';
  alturamin = '';
  alturamax = '';
  pesomin = '';
  pesomax = '';
  subscription2: Subscription;

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
    this.subscription2.unsubscribe();
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
    this.casosService.getProbabilidadeGraficoIdades('','','','','',
      '','','','').subscribe(prob => { this.probabilidadesGraficoIdades = prob; this.chartIdades(this.probabilidadesGraficoIdades); });

    this.casosService.getProbabilidadeGraficoRegiao('','','','','',
      '','','','').subscribe(prob => { this.probabilidadesGraficoRegioes = prob; this.chartRegioes(this.probabilidadesGraficoRegioes); });

    this.casosService.getProbabilidadeGraficoGenero('','','','','',
      '','','','').subscribe(prob => { this.probabilidadesGraficoGeneros = prob; this.chartGeneros(this.probabilidadesGraficoGeneros); });

    this.casosService.getProbabilidadeGraficoAltura('','','','','',
      '','','','').subscribe(prob => { this.probabilidadesGraficoAlturas = prob; this.chartAlturas(this.probabilidadesGraficoAlturas); });

    this.casosService.getProbabilidadeGraficoPeso('','','','','',
      '','','','').subscribe(prob => { this.probabilidadesGraficoPesos = prob; this.chartPesos(this.probabilidadesGraficoPesos); });

    const source2 = interval(15000);
    this.subscription2 = source2.subscribe(val => this.updateGraficos2());

  }

  //Função chamada quando é submetido um novo formulário de filtros
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
    this.casosService.getProbabilidadeGraficoIdades(filterData.idade_min, filterData.idade_max, filterData.genero, filterData.regiao,
      filterData.nacionalidade, filterData.altura_min, filterData.altura_max, filterData.peso_min, filterData.peso_max).
    subscribe(prob => { this.probabilidadesGraficoIdades = prob; this.chartIdades(this.probabilidadesGraficoIdades); });
    this.casosService.getProbabilidadeGraficoRegiao(filterData.idade_min, filterData.idade_max, filterData.genero, filterData.regiao,
      filterData.nacionalidade, filterData.altura_min, filterData.altura_max, filterData.peso_min, filterData.peso_max).
    subscribe(prob => { this.probabilidadesGraficoRegioes = prob; this.chartRegioes(this.probabilidadesGraficoRegioes); });
    this.casosService.getProbabilidadeGraficoGenero(filterData.idade_min, filterData.idade_max, filterData.genero, filterData.regiao,
      filterData.nacionalidade, filterData.altura_min, filterData.altura_max, filterData.peso_min, filterData.peso_max).
    subscribe(prob => { this.probabilidadesGraficoGeneros = prob; this.chartGeneros(this.probabilidadesGraficoGeneros); });
    this.casosService.getProbabilidadeGraficoAltura(filterData.idade_min, filterData.idade_max, filterData.genero, filterData.regiao,
      filterData.nacionalidade, filterData.altura_min, filterData.altura_max, filterData.peso_min, filterData.peso_max).
    subscribe(prob => { this.probabilidadesGraficoAlturas = prob; this.chartAlturas(this.probabilidadesGraficoAlturas); });
    this.casosService.getProbabilidadeGraficoPeso(filterData.idade_min, filterData.idade_max, filterData.genero, filterData.regiao,
      filterData.nacionalidade, filterData.altura_min, filterData.altura_max, filterData.peso_min, filterData.peso_max).
    subscribe(prob => { this.probabilidadesGraficoPesos = prob; this.chartPesos(this.probabilidadesGraficoPesos); });

  }

  updateGraficos2(): void {
    console.log('entrei2')
    this.casosService.getProbabilidadeGraficoIdades(this.idademin, this.idademax, this.genero, this.regiao, this.nacionalidade, this.alturamin,
      this.alturamax, this.pesomin, this.pesomax).subscribe(prob => {
      let array_data = [
        { y: prob[0], name: "1-10"},
        { y: prob[1], name: "11-20"},
        { y: prob[2], name: "21-30" },
        { y: prob[3], name: "31-40" },
        { y: prob[4], name: "41-50" },
        { y: prob[5], name: "51-60" },
        { y: prob[6], name: "61-70" },
        { y: prob[7], name: "71-80", exploded: true },
        { y: prob[8], name: "+81" },
      ];
      this.chartIdadesObj.options.data[0].dataPoints = array_data; this.chartIdadesObj.render();
    });

    this.casosService.getProbabilidadeGraficoRegiao(this.idademin, this.idademax, this.genero, this.regiao, this.nacionalidade, this.alturamin,
      this.alturamax, this.pesomin, this.pesomax).subscribe(prob => {
      let array_data = [
        { y: prob[0], name: "Norte", exploded: true},
        { y: prob[1], name: "Centro"},
        { y: prob[2], name: "Lisboa e Vale do Tejo" },
        { y: prob[3], name: "Alentejo" },
        { y: prob[4], name: "Algarve" },
        { y: prob[5], name: "Açores" },
        { y: prob[6], name: "Madeira" },
      ];
      this.chartRegioesObj.options.data[0].dataPoints = array_data; this.chartRegioesObj.render();
    });

    this.casosService.getProbabilidadeGraficoGenero(this.idademin, this.idademax, this.genero, this.regiao, this.nacionalidade, this.alturamin,
      this.alturamax, this.pesomin, this.pesomax).subscribe(prob => {
      let array_data = [
        { y: prob[0], label: "Masculino" },
        { y: prob[1], label: "Feminino" }
      ];

      this.chartGenerosObj.options.data[0].dataPoints = array_data; this.chartGenerosObj.render();
    });

    this.casosService.getProbabilidadeGraficoAltura(this.idademin, this.idademax, this.genero, this.regiao, this.nacionalidade, this.alturamin,
      this.alturamax, this.pesomin, this.pesomax).subscribe(prob => {
      let array_data = [
        { y: prob[0], label: "-1,50" },
        { y: prob[1], label: "1,51-1,60" },
        { y: prob[2], label: "1,61-1,70", exploded: true },
        { y: prob[3], label: "1,71-1,80"},
        { y: prob[4], label: "1,81-1,90" },
        { y: prob[5], label: "1,91-2,00" },
        { y: prob[6], label: "+2,01" },
      ];

      this.chartAlturasObj.options.data[0].dataPoints = array_data; this.chartAlturasObj.render();
    });

    this.casosService.getProbabilidadeGraficoPeso(this.idademin, this.idademax, this.genero, this.regiao, this.nacionalidade, this.alturamin,
      this.alturamax, this.pesomin, this.pesomax).subscribe(prob => {
      let array_data = [
        { y: prob[0], label: "-20" },
        { y: prob[1], label: "21-30" },
        { y: prob[2], label: "31-40" },
        { y: prob[3], label: "41-50" },
        { y: prob[4], label: "51-60" },
        { y: prob[5], label: "61-70", exploded: true},
        { y: prob[6], label: "71-80" },
        { y: prob[7], label: "81-90" },
        { y: prob[8], label: "+91" },
      ];

      this.chartPesosObj.options.data[0].dataPoints = array_data; this.chartPesosObj.render();
    });
  }

  chartIdades(probabilidadesGraficoIdades): void {
    // Grafico Pie por faixa etaria
    this.chartIdadesObj = new CanvasJS.Chart("agepiegraph", {
      exportEnabled: true,
      animationEnabled: true,
      title:{
        text: ""
      },
      legend:{
        cursor: "pointer",
        itemclick: this.explodePie
      },
      data: [{
        type: "pie",
        showInLegend: true,
        toolTipContent: "{name} anos: <strong>{y}%</strong>",
        indexLabel: "{name} - {y}%",
        dataPoints: [
          { y: probabilidadesGraficoIdades[0], name: "1-10"},
          { y: probabilidadesGraficoIdades[1], name: "11-20"},
          { y: probabilidadesGraficoIdades[2], name: "21-30" },
          { y: probabilidadesGraficoIdades[3], name: "31-40" },
          { y: probabilidadesGraficoIdades[4], name: "41-50" },
          { y: probabilidadesGraficoIdades[5], name: "51-60" },
          { y: probabilidadesGraficoIdades[6], name: "61-70" },
          { y: probabilidadesGraficoIdades[7], name: "71-80", exploded: true },
          { y: probabilidadesGraficoIdades[8], name: "+81" },
        ]
      }]
    });
    this.chartIdadesObj.render();
  }

  chartRegioes(probabilidadesGraficoRegioes): void {

    // Grafico Pie por regiao
    this.chartRegioesObj = new CanvasJS.Chart("regionpiegraph", {
      exportEnabled: true,
      animationEnabled: true,
      legend:{
        cursor: "pointer",
        itemclick: this.explodePie
      },
      data: [{
        type: "pie",
        showInLegend: true,
        toolTipContent: "{name}: <strong>{y}%</strong>",
        indexLabel: "{name} - {y}%",
        dataPoints: [
          { y: probabilidadesGraficoRegioes[0], name: "Norte", exploded: true},
          { y: probabilidadesGraficoRegioes[1], name: "Centro"},
          { y: probabilidadesGraficoRegioes[2], name: "Lisboa e Vale do Tejo" },
          { y: probabilidadesGraficoRegioes[3], name: "Alentejo" },
          { y: probabilidadesGraficoRegioes[4], name: "Algarve" },
          { y: probabilidadesGraficoRegioes[5], name: "Açores" },
          { y: probabilidadesGraficoRegioes[6], name: "Madeira" },
        ]
      }]
    });
    this.chartRegioesObj.render();

  }

  chartGeneros(probabilidadesGraficoGeneros): void {

    // Grafico por genero
    this.chartGenerosObj = new CanvasJS.Chart("chartContainer", {
      animationEnabled: true,
      data: [{
        type: "doughnut",
        startAngle: 180,
        //innerRadius: 60,
        indexLabelFontSize: 16,
        indexLabel: "{label} - #percent%",
        toolTipContent: "<b>{label}:</b> {y} (#percent%)",
        dataPoints: [
          { y: probabilidadesGraficoGeneros[0], label: "Masculino" },
          { y: probabilidadesGraficoGeneros[1], label: "Feminino" }
        ]
      }]
    });
    this.chartGenerosObj.render();

  }

  chartAlturas(probabilidadesGraficoAlturas): void {

    // Grafico por altura
    this.chartAlturasObj = new CanvasJS.Chart("heightpiegraph", {
      animationEnabled: true,
      data: [{
        type: "doughnut",
        startAngle: 180,
        //innerRadius: 60,
        indexLabelFontSize: 16,
        indexLabel: "{label} - #percent%",
        toolTipContent: "<b>{label}:</b> {y} (#percent%)",
        dataPoints: [
          { y: probabilidadesGraficoAlturas[0], label: "-1,50" },
          { y: probabilidadesGraficoAlturas[1], label: "1,51-1,60" },
          { y: probabilidadesGraficoAlturas[2], label: "1,61-1,70", exploded: true },
          { y: probabilidadesGraficoAlturas[3], label: "1,71-1,80"},
          { y: probabilidadesGraficoAlturas[4], label: "1,81-1,90" },
          { y: probabilidadesGraficoAlturas[5], label: "1,91-2,00" },
          { y: probabilidadesGraficoAlturas[6], label: "+2,01" },
        ]
      }]
    });
    this.chartAlturasObj.render();

  }


  chartPesos(probabilidadesGraficoPesos): void {

    // Grafico por peso
    this.chartPesosObj = new CanvasJS.Chart("weightpiegraph", {
      animationEnabled: true,
      data: [{
        type: "doughnut",
        startAngle: 180,
        //innerRadius: 60,
        indexLabelFontSize: 16,
        indexLabel: "{label} - #percent%",
        toolTipContent: "<b>{label}:</b> {y} (#percent%)",
        dataPoints: [
          { y: probabilidadesGraficoPesos[0], label: "-20" },
          { y: probabilidadesGraficoPesos[1], label: "21-30" },
          { y: probabilidadesGraficoPesos[2], label: "31-40" },
          { y: probabilidadesGraficoPesos[3], label: "41-50" },
          { y: probabilidadesGraficoPesos[4], label: "51-60" },
          { y: probabilidadesGraficoPesos[5], label: "61-70", exploded: true},
          { y: probabilidadesGraficoPesos[6], label: "71-80" },
          { y: probabilidadesGraficoPesos[7], label: "81-90" },
          { y: probabilidadesGraficoPesos[8], label: "+91" },
        ]
      }]
    });
    this.chartPesosObj.render();
  }


  explodePie (e): void {
    if(typeof (e.dataSeries.dataPoints[e.dataPointIndex].exploded) === "undefined" || !e.dataSeries.dataPoints[e.dataPointIndex].exploded) {
      e.dataSeries.dataPoints[e.dataPointIndex].exploded = true;
    } else {
      e.dataSeries.dataPoints[e.dataPointIndex].exploded = false;
    }
    e.chart.render();

  }

}



