package com.xuexiang.xuidemo.fragment.expands.chart.bar;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author xuexiang
 * @since 2019/4/11 下午11:15
 */
@Page(name = "HorizontalBarChart\n水平横向柱状图")
public class HorizontalBarChartFragment extends BaseFragment {

    @BindView(R.id.chart1)
    HorizontalBarChart chart;

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_horizontal_barchart;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        chart.setDrawBarShadow(false);
        //开启在柱状图顶端显示值
        chart.setDrawValueAboveBar(true);
        //关闭描述
        chart.getDescription().setEnabled(false);
        //设置显示值时，最大的柱数量
        chart.setMaxVisibleValueCount(60);
        //设置不能同时在x轴和y轴上缩放
        chart.setPinchZoom(false);
        //设置不画背景网格
        chart.setDrawGridBackground(false);

        //设置X轴样式
        XAxis xl = chart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(false);
        xl.setGranularity(10f);

        //设置Y轴的左侧样式
        YAxis yl = chart.getAxisLeft();
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(true);
        yl.setAxisMinimum(0f);

        //设置Y轴的右侧样式
        YAxis yr = chart.getAxisRight();
        yr.setDrawAxisLine(true);
        yr.setDrawGridLines(false);
        yr.setAxisMinimum(0f);

        chart.setFitBars(true);
        chart.animateY(2500);

        // setting data
        setChartData(12, 50);

        //设置图表 标题 的样式
        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setXEntrySpace(4f);
    }


    /**
     * 设置图表数据
     *
     * @param count 柱状图中柱的数量
     * @param range
     */
    private void setChartData(int count, float range) {
        float barWidth = 9f;
        float spaceForBar = 10f;
        List<BarEntry> values = new ArrayList<>();
        //设置数据源
        for (int i = 0; i < count; i++) {
            float val = (float) (Math.random() * range);
            values.add(new BarEntry(i * spaceForBar, val, getResources().getDrawable(R.drawable.ic_star_green)));
        }

        BarDataSet set1;
        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(values, "DataSet 1");
            set1.setDrawIcons(false);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(barWidth);
            chart.setData(data);
        }
    }



}
