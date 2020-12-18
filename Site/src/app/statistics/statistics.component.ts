import { Component, OnInit } from '@angular/core';
import {CasosService} from '../casos.service';
import {FormBuilder} from '@angular/forms';
import {filter} from 'rxjs/operators';

declare var jQuery: any;
declare const CanvasJS: any;


@Component({
  selector: 'app-statistics',
  templateUrl: './statistics.component.html',
  styleUrls: ['./statistics.component.css']
})
export class StatisticsComponent implements OnInit {
  casosAtivos: number;
  casosRecuperados: number;
  casosCuidadosIntensivos: number;
  casosMortos: number;
  casosInternados: number;
  nacionalidades = ['Alemã', 'Espanhola', 'Francesa', 'Italiana', 'Inglesa', 'Brasileira', 'Belga', 'Russa', 'Americana', 'Chinesa', 'Angolana',
    'Moçambicana', 'Holandesa', 'Polaca', 'Cabo-Verdiana', 'Albanesa', 'Austríaca', 'Búlgara', 'Croata', 'Dinamarquesa', 'Eslovaca',
    'Eslovena', 'Finlandesa', 'Grega', 'Húngara', 'Islandesa', 'Irlandesa', 'Lituana', 'Luxemburguesa', 'Norueguesa', 'Romena', 'Sueca',
    'Suíça', 'Turca', 'Ucraniana', 'Argentina', 'Canadiana', 'Mexicana', 'Japonesa', 'Portuguesa'].sort();
  regioes = ['Norte', 'Lisboa e Vale do Tejo', 'Centro', 'Alentejo', 'Algarve', 'Açores', 'Madeira'];
  filterForm;
  alturaMin: number;

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

  ngOnInit(): void {

    (function($) {
      $(document).ready(function () {
        $('#sidebarCollapse').on('click', function () {
          $('#sidebar').toggleClass('active');
          $(this).toggleClass('active');
        });
      })
    })(jQuery);

    (function($) {
      $(document).ready(function () {
        $('#filterCollapse').on('click', function () {
          $('#filters').toggle(500);
        });
      })
    })(jQuery);


    charts();
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
  }

  onSubmit(filterData): void {
    console.log(filterData);

    this.casosService.getNumeroCasos('ativos', filterData.idade_min, filterData.idade_max, filterData.genero, filterData.regiao,
      filterData.nacionalidade, filterData.altura_min, filterData.altura_max, filterData.peso_min, filterData.peso_max).
      subscribe(casosAtivos => this.casosAtivos = casosAtivos);
    this.casosService.getNumeroCasos('Recuperado', filterData.idade_min, filterData.idade_max, filterData.genero, filterData.regiao,
      filterData.nacionalidade, filterData.altura_min, filterData.altura_max, filterData.peso_min, filterData.peso_max).
    subscribe(casosAtivos => this.casosAtivos = casosAtivos);
    this.casosService.getNumeroCasos('Cuidados+Intensivos', filterData.idade_min, filterData.idade_max, filterData.genero, filterData.regiao,
      filterData.nacionalidade, filterData.altura_min, filterData.altura_max, filterData.peso_min, filterData.peso_max).
    subscribe(casosAtivos => this.casosAtivos = casosAtivos);
    this.casosService.getNumeroCasos('Óbito', filterData.idade_min, filterData.idade_max, filterData.genero, filterData.regiao,
      filterData.nacionalidade, filterData.altura_min, filterData.altura_max, filterData.peso_min, filterData.peso_max).
    subscribe(casosAtivos => this.casosAtivos = casosAtivos);
    this.casosService.getNumeroCasos('Internado', filterData.idade_min, filterData.idade_max, filterData.genero, filterData.regiao,
      filterData.nacionalidade, filterData.altura_min, filterData.altura_max, filterData.peso_min, filterData.peso_max).
    subscribe(casosAtivos => this.casosAtivos = casosAtivos);
  }
}

function charts() {

  // grafico todos os casos diarios
  var chart = new CanvasJS.Chart("allcasesgraph", {
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
      dataPoints: [
        { x: new Date(2020, 10, 1), y: 430 },
        { x: new Date(2020, 10, 2), y: 493 },
        { x: new Date(2020, 10, 3), y: 329 },
        { x: new Date(2020, 10, 4), y: 830 },
        { x: new Date(2020, 10, 5), y: 634 },
        { x: new Date(2020, 10, 6), y: 534 },
        { x: new Date(2020, 10, 7), y: 644 },
        { x: new Date(2020, 10, 8), y: 569 },
  { x: new Date(2020, 10, 9), y: 695 },
  { x: new Date(2020, 10, 10), y: 938 },
  { x: new Date(2020, 10, 11), y: 1340 },
  { x: new Date(2020, 10, 12), y: 1032 },
  { x: new Date(2020, 10, 13), y: 983 },
  { x: new Date(2020, 10, 14), y: 1332 },
  { x: new Date(2020, 10, 15), y: 1284 },
  { x: new Date(2020, 10, 16), y: 1623 },
  { x: new Date(2020, 10, 17), y: 2303 },
  { x: new Date(2020, 10, 18), y: 1032 },
  { x: new Date(2020, 10, 19), y: 2032 },
  { x: new Date(2020, 10, 20), y: 3032 },
  { x: new Date(2020, 10, 21), y: 3432 },
  { x: new Date(2020, 10, 22), y: 4973 },
  { x: new Date(2020, 10, 23), y: 6948 },
  { x: new Date(2020, 10, 24), y: 4938 },
  { x: new Date(2020, 10, 25), y: 5829 },
  { x: new Date(2020, 10, 26), y: 6483 },
  { x: new Date(2020, 10, 27), y: 7282 },
  { x: new Date(2020, 10, 28), y: 5647 },
  { x: new Date(2020, 10, 29), y: 4479 }
]
}]
});
  chart.render();

  // Grafico Pie por idade
  var chart = new CanvasJS.Chart("agepiegraph", {
    exportEnabled: true,
    animationEnabled: true,
    title:{
      text: ""
    },
    legend:{
      cursor: "pointer",
      itemclick: explodePie
    },
    data: [{
      type: "pie",
      showInLegend: true,
      toolTipContent: "{name} anos: <strong>{y}%</strong>",
      indexLabel: "{name} - {y}%",
      dataPoints: [
        { y: 11, name: "1-10"},
        { y: 11, name: "10-20"},
        { y: 17, name: "20-30" },
        { y: 7, name: "30-40" },
        { y: 3, name: "40-50" },
        { y: 5, name: "50-60" },
        { y: 20, name: "60-70" },
        { y: 26, name: "+70", exploded: true }
      ]
    }]
  });
  chart.render();


  // Grafico Pie por regiao
  var chart = new CanvasJS.Chart("regionpiegraph", {
    exportEnabled: true,
    animationEnabled: true,
    legend:{
      cursor: "pointer",
      itemclick: explodePie
    },
    data: [{
      type: "pie",
      showInLegend: true,
      toolTipContent: "{name}: <strong>{y}%</strong>",
      indexLabel: "{name} - {y}%",
      dataPoints: [
        { y: 28, name: "Norte", exploded: true},
        { y: 12, name: "Centro"},
        { y: 24, name: "Lisboa e Vale do Tejo" },
        { y: 9, name: "Alentejo" },
        { y: 13, name: "Algarve" },
        { y: 8, name: "Açores" },
        { y: 6, name: "Madeira" },
      ]
    }]
  });
  chart.render();

  // Grafico por sexo
  var chart = new CanvasJS.Chart("chartContainer", {
    animationEnabled: true,
    data: [{
      type: "doughnut",
      startAngle: 180,
      //innerRadius: 60,
      indexLabelFontSize: 16,
      indexLabel: "{label} - #percent%",
      toolTipContent: "<b>{label}:</b> {y} (#percent%)",
      dataPoints: [
        { y: 47, label: "Masculino" },
        { y: 53, label: "Feminino" }
      ]
    }]
  });
  chart.render();

  // Grafico por altura
  var chart = new CanvasJS.Chart("heightpiegraph", {
    animationEnabled: true,
    data: [{
      type: "doughnut",
      startAngle: 180,
      //innerRadius: 60,
      indexLabelFontSize: 16,
      indexLabel: "{label} - #percent%",
      toolTipContent: "<b>{label}:</b> {y} (#percent%)",
      dataPoints: [
        { y: 1, label: "1,10-1,20" },
        { y: 2, label: "1,20-1,30" },
        { y: 2, label: "1,30-1,40" },
        { y: 2, label: "1,40-1,50" },
        { y: 5, label: "1,50-1,60" },
        { y: 25, label: "1,60-1,70" },
        { y: 31, label: "1,70-1,80", exploded: true},
        { y: 23, label: "1,80-1,90" },
        { y: 8, label: "1,90-2,00" },
        { y: 1, label: "+2,00" },
      ]
    }]
  });
  chart.render();

  // Grafico por altura
  var chart = new CanvasJS.Chart("weightpiegraph", {
    animationEnabled: true,
    data: [{
      type: "doughnut",
      startAngle: 180,
      //innerRadius: 60,
      indexLabelFontSize: 16,
      indexLabel: "{label} - #percent%",
      toolTipContent: "<b>{label}:</b> {y} (#percent%)",
      dataPoints: [
        { y: 3, label: "0-10" },
        { y: 2, label: "10-20" },
        { y: 1, label: "20-30" },
        { y: 2, label: "30-40" },
        { y: 10, label: "40-50" },
        { y: 22, label: "50-60" },
        { y: 40, label: "70-80", exploded: true},
        { y: 15, label: "80-90" },
        { y: 4, label: "90-100" },
        { y: 0.75, label: "100-110" },
        { y: 0.25, label: "+110" },
      ]
    }]
  });
  chart.render();
}

function explodePie (e) {
  if(typeof (e.dataSeries.dataPoints[e.dataPointIndex].exploded) === "undefined" || !e.dataSeries.dataPoints[e.dataPointIndex].exploded) {
    e.dataSeries.dataPoints[e.dataPointIndex].exploded = true;
  } else {
    e.dataSeries.dataPoints[e.dataPointIndex].exploded = false;
  }
  e.chart.render();

}
