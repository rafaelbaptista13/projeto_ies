window.onload = function () {
    
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
                { x: new Date(2020, 10, 01), y: 430 },
                { x: new Date(2020, 10, 02), y: 493 },
                { x: new Date(2020, 10, 03), y: 329 },
                { x: new Date(2020, 10, 04), y: 830 },
                { x: new Date(2020, 10, 05), y: 634 },
                { x: new Date(2020, 10, 06), y: 534 },
                { x: new Date(2020, 10, 07), y: 644 },
                { x: new Date(2020, 10, 08), y: 569 },
                { x: new Date(2020, 10, 09), y: 695 },
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
    
    // Grafico Pie 
    var chart = new CanvasJS.Chart("piegraph", {
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

}

function explodePie (e) {
    if(typeof (e.dataSeries.dataPoints[e.dataPointIndex].exploded) === "undefined" || !e.dataSeries.dataPoints[e.dataPointIndex].exploded) {
        e.dataSeries.dataPoints[e.dataPointIndex].exploded = true;
    } else {
        e.dataSeries.dataPoints[e.dataPointIndex].exploded = false;
    }
    e.chart.render();

}

