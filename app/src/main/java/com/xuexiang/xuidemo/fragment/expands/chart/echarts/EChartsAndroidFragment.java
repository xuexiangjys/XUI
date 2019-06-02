/*
 * Copyright (C) 2019 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.xuexiang.xuidemo.fragment.expands.chart.echarts;

import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.FrameLayout;

import com.github.abel533.echarts.Legend;
import com.github.abel533.echarts.Title;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.Trigger;
import com.github.abel533.echarts.data.PieData;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Bar;
import com.github.abel533.echarts.series.Line;
import com.github.abel533.echarts.series.Pie;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.webview.BaseWebViewFragment;
import com.xuexiang.xuidemo.utils.Utils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author xuexiang
 * @since 2019/5/28 8:48
 */
@Page(name = "ECharts\nAndroid原生调用")
public class EChartsAndroidFragment extends BaseWebViewFragment {

    @BindView(R.id.fl_container)
    FrameLayout flContainer;

    private ChartInterface mChartInterface;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_echarts_android;
    }

    @Override
    protected void initViews() {
        //目前Echarts-Java只支持3.x
        mAgentWeb = Utils.createAgentWeb(this, flContainer, "file:///android_asset/chart/src/template.html");

        //注入接口,供JS调用
        mAgentWeb.getJsInterfaceHolder().addJavaObject("Android", mChartInterface = new ChartInterface());

    }

    @SingleClick
    @OnClick({R.id.btn_bar_chart, R.id.btn_line_chart, R.id.btn_pie_chart})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_bar_chart:
                initBarChart();
                break;
            case R.id.btn_line_chart:
                initLineChart();
                break;
            case R.id.btn_pie_chart:
                initPieChart();
                break;
        }
    }

    private void initBarChart() {
        mAgentWeb.getJsAccessEntrace().quickCallJs("loadChartView", "chart", mChartInterface.makeBarChartOptions());
    }

    private void initLineChart() {
        mAgentWeb.getJsAccessEntrace().quickCallJs("loadChartView", "chart", mChartInterface.makeLineChartOptions());
    }

    private void initPieChart() {
        mAgentWeb.getJsAccessEntrace().quickCallJs("loadChartView", "chart", mChartInterface.makePieChartOptions());
    }


    /**
     * 注入到JS里的对象接口
     */
    public class ChartInterface {

        @JavascriptInterface
        public String makeBarChartOptions() {
            GsonOption option = new GsonOption();
            option.setTitle(new Title().text("柱状图"));
            option.setLegend(new Legend().data("销量"));
            option.xAxis(new CategoryAxis().data("衬衫", "羊毛衫", "雪纺衫", "裤子", "高跟鞋", "袜子"));
            option.yAxis();

            Bar bar = new Bar("销量");
            bar.data(5, 20, 36, 10, 10, 20);
            option.series(bar);

            return option.toString();
        }

        @JavascriptInterface
        public String makeLineChartOptions() {
            GsonOption option = new GsonOption();
            option.legend("高度(km)与气温(°C)变化关系");
            option.toolbox().show(false);
            option.calculable(true);
            option.tooltip().trigger(Trigger.axis).formatter("Temperature : <br/>{b}km : {c}°C");

            ValueAxis valueAxis = new ValueAxis();
            valueAxis.axisLabel().formatter("{value} °C");
            option.xAxis(valueAxis);

            CategoryAxis categoryAxis = new CategoryAxis();
            categoryAxis.axisLine().onZero(false);
            categoryAxis.axisLabel().formatter("{value} km");
            categoryAxis.boundaryGap(false);
            categoryAxis.data(0, 10, 20, 30, 40, 50, 60, 70, 80);
            option.yAxis(categoryAxis);

            Line line = new Line();
            line.smooth(true).name("高度(km)与气温(°C)变化关系").data(15, -50, -56.5, -46.5, -22.1, -2.5, -27.7, -55.7, -76.5).itemStyle().normal().lineStyle().shadowColor("rgba(0,0,0,0.4)");
            option.series(line);
            return option.toString();
        }

        @JavascriptInterface
        public String makePieChartOptions() {
            GsonOption option = new GsonOption();
            option.tooltip().trigger(Trigger.item).formatter("{a} <br/>{b} : {c} ({d}%)");
            option.legend().data("直接访问", "邮件营销", "联盟广告", "视频广告", "搜索引擎");

            Pie pie = new Pie("访问来源").data(
                    new PieData("直接访问", 335),
                    new PieData("邮件营销", 310),
                    new PieData("联盟广告", 274),
                    new PieData("视频广告", 235),
                    new PieData("搜索引擎", 400)
            ).center("50%", "45%").radius("50%");
            pie.label().normal().show(true).formatter("{b}{c}({d}%)");
            option.series(pie);
            return option.toString();
        }
    }

}
