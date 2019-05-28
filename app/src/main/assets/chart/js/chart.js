 function loadChart() {
     setChart('bar', makeBarChartOption());

     setChart('line', makeLineChartOption());

     setChart('pie', makePieChartOption());

 }

 function makeBarChartOption() {
     // 指定图表的配置项和数据
     var option = {
         title: {
             text: '柱状图'
         },
         tooltip: {},
         legend: {
             data: ['销量']
         },
         xAxis: {
             data: ["衬衫", "羊毛衫", "雪纺衫", "裤子", "高跟鞋", "袜子"]
         },
         yAxis: {},
         series: [{
             name: '销量',
             type: 'bar',
             data: [5, 20, 36, 10, 10, 20]
         }]
     };
     return option;
 }

 function makeLineChartOption() {
     var data = [
         ["06-05", 116],
         ["06-06", 129],
         ["06-07", 135],
         ["06-08", 86],
         ["06-09", 73],
         ["06-10", 85],
         ["06-11", 73],
         ["06-12", 68],
         ["06-13", 92],
         ["06-14", 130],
         ["06-15", 245],
         ["06-16", 139],
         ["06-17", 115],
         ["06-18", 111],
         ["06-19", 309],
         ["06-20", 206],
         ["06-21", 137],
         ["06-22", 128],
         ["06-23", 85],
         ["06-24", 94],
         ["06-25", 71],
         ["06-26", 106],
         ["06-27", 84],
         ["06-28", 93],
         ["06-29", 85],
         ["06-30", 73],
         ["07-01", 83],
         ["07-02", 125],
         ["07-03", 107],
         ["07-04", 82],
         ["07-05", 44],
         ["07-06", 72],
     ];
     var dateList = data.map(function (item) {
         return item[0];
     });
     var valueList = data.map(function (item) {
         return item[1];
     });
     var option = {
         visualMap: {
             show: false,
             type: 'continuous',
             seriesIndex: 0,
             min: 0,
             max: 400
         },
         title: {
             left: 'left',
             text: '折线图'
         },
         tooltip: {
             trigger: 'axis'
         },
         xAxis: {
             data: dateList,
             splitArea: {
                 show: true,
                 interval: 1
             },
             axisTick: {
                 show: false
             }
         },
         yAxis: {
             splitLine: {
                 show: false
             }
         },
         series: [{
             type: 'line',
             smooth: true,
             showSymbol: false,
             data: valueList
         }]
     };
     return option;
 }

 function makePieChartOption() {
     var data = [{
             value: 335,
             name: '直接访问'
         },
         {
             value: 310,
             name: '邮件营销'
         },
         {
             value: 234,
             name: '联盟广告'
         },
         {
             value: 135,
             name: '视频广告'
         },
         {
             value: 1548,
             name: '搜索引擎'
         }
     ];
     var option = {
         tooltip: {
             trigger: 'item',
             formatter: "{a} <br/>{b}: {c} ({d}%)"
         },
         legend: {
             orient: 'vertical',
             x: 'left',
             data: ['直接访问', '邮件营销', '联盟广告', '视频广告', '搜索引擎']
         },
         series: [{
             name: '访问来源',
             type: 'pie',
             radius: ['50%', '70%'],
             avoidLabelOverlap: false,
             label: {
                 normal: {
                     show: false,
                     position: 'center'
                 },
                 emphasis: {
                     show: true,
                     textStyle: {
                         fontSize: '30',
                         fontWeight: 'bold'
                     }
                 }
             },
             labelLine: {
                 normal: {
                     show: false
                 }
             },
             data: data
         }]
     };
     return option;
 }


 function setChart(id, option) {
     // 基于准备好的dom，初始化echarts实例
     var myChart = echarts.init(document.getElementById(id));
     // 使用刚指定的配置项和数据显示图表。
     myChart.setOption(option);
 }

