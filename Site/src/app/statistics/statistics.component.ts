import { Component, OnInit } from '@angular/core';

declare var jQuery: any;
declare const CanvasJS: any;


@Component({
  selector: 'app-statistics',
  templateUrl: './statistics.component.html',
  styleUrls: ['./statistics.component.css']
})
export class StatisticsComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
    (function($) {
      $(document).ready(function () {
        $('#sidebarCollapse').on('click', function () {
          $('#sidebar').toggleClass('active');
          $(this).toggleClass('active');
        });
      })
    })(jQuery);
    
    charts()
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



  // grafico casos ativos
  var chart = new CanvasJS.Chart("activecasesgraph", {
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


}

function explodePie (e) {
  if(typeof (e.dataSeries.dataPoints[e.dataPointIndex].exploded) === "undefined" || !e.dataSeries.dataPoints[e.dataPointIndex].exploded) {
    e.dataSeries.dataPoints[e.dataPointIndex].exploded = true;
  } else {
    e.dataSeries.dataPoints[e.dataPointIndex].exploded = false;
  }
  e.chart.render();

}
