window.onload = function () {
            
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
