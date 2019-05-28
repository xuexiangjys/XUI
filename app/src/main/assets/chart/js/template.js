function loadChartView(id, option) {
    var myChart = echarts.init(document.getElementById(id));
    myChart.setOption(option);
}

function initChart() {
    var option = JSON.parse(window.Android.makeBarChartOptions());
    loadChartView('chart', option)
}
