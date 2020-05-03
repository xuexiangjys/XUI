package com.xuexiang.xuidemo.fragment.expands.chart.bar;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.fragment.expands.chart.BaseChartFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author xuexiang
 * @since 2019/4/11 下午10:59
 */
@Page(name = "SimpleBarChart\n默认的柱状图样式")
public class SimpleBarChartFragment extends BaseChartFragment {

    @BindView(R.id.chart1)
    BarChart chart;

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chart_bar;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        initChartStyle();
        initChartLabel();
        setChartData(10, 100);

        // 设置Y轴进入动画
        chart.animateY(1500);
    }

    /**
     * 初始化图表的样式
     */
    @Override
    protected void initChartStyle() {
        //关闭描述
        chart.getDescription().setEnabled(false);
        //设置显示值时，最大的柱数量
        chart.setMaxVisibleValueCount(60);

        //设置不能同时在x轴和y轴上缩放
        chart.setPinchZoom(false);

        chart.setDrawBarShadow(false);
        //设置不画背景网格
        chart.setDrawGridBackground(false);

        //设置X轴样式
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        chart.getAxisLeft().setDrawGridLines(false);
    }

    /**
     * 初始化图表的 标题 样式
     */
    @Override
    protected void initChartLabel() {
        //不显示图标 标题
        chart.getLegend().setEnabled(false);
    }

    /**
     * 设置图表数据
     *
     * @param count 柱状图中柱的数量
     * @param range
     */
    @Override
    protected void setChartData(int count, float range) {
        List<BarEntry> values = new ArrayList<>();
        //设置数据源
        for (int i = 0; i < count; i++) {
            float multi = (range + 1);
            float val = (float) (Math.random() * multi) + multi / 3;
            values.add(new BarEntry(i, val));
        }

        BarDataSet set1;
        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(values, "Data Set");
            set1.setColors(ColorTemplate.VORDIPLOM_COLORS);
            set1.setDrawValues(false);

            List<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            chart.setData(data);
            chart.setFitBars(true);
        }
        chart.invalidate();
    }
}
