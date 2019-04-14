package com.xuexiang.xuidemo.fragment.expands.chart.line;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.fragment.expands.chart.BaseChartFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author xuexiang
 * @since 2019/4/14 下午1:03
 */
@Page(name = "MultiLineChart\n多组数据的折线图")
public class MultiLineChartFragment extends BaseChartFragment {

    @BindView(R.id.chart1)
    LineChart chart;
    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chart_line;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        initChartStyle();
        initChartLabel();
        setChartData(20, 100);
    }

    /**
     * 初始化图表的样式
     */
    @Override
    protected void initChartStyle() {
        chart.setDrawGridBackground(false);
        chart.getDescription().setEnabled(false);
        chart.setDrawBorders(false);

        //禁用Y轴左侧
        chart.getAxisLeft().setEnabled(false);
        //禁用Y轴右侧轴线
        chart.getAxisRight().setDrawAxisLine(false);
        //不画网格线
        chart.getAxisRight().setDrawGridLines(false);
        chart.getXAxis().setDrawAxisLine(false);
        chart.getXAxis().setDrawGridLines(false);

        // 开启手势触摸
        chart.setTouchEnabled(true);
        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false);

    }

    /**
     * 初始化图表的 标题 样式
     */
    @Override
    protected void initChartLabel() {
        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
    }

    /**
     * 设置图表数据
     *
     * @param count 一组数据的数量
     * @param range
     */
    @Override
    protected void setChartData(int count, float range) {
        List<ILineDataSet> dataSets = new ArrayList<>();
        //三组数据
        for (int z = 0; z < 3; z++) {
            List<Entry> values = new ArrayList<>();
            //设置数据源
            for (int i = 0; i < count; i++) {
                double val = (Math.random() * range) + 3;
                values.add(new Entry(i, (float) val));
            }

            LineDataSet d = new LineDataSet(values, "DataSet " + (z + 1));
            d.setLineWidth(2.5f);
            d.setCircleRadius(4f);

            int color = colors[z % colors.length];
            d.setColor(color);
            d.setCircleColor(color);
            dataSets.add(d);
        }

        // 设置第一组数据源为虚线
        ((LineDataSet) dataSets.get(0)).enableDashedLine(10, 10, 0);
        ((LineDataSet) dataSets.get(0)).setColors(ColorTemplate.VORDIPLOM_COLORS);
        ((LineDataSet) dataSets.get(0)).setCircleColors(ColorTemplate.VORDIPLOM_COLORS);

        LineData data = new LineData(dataSets);
        chart.setData(data);
        chart.invalidate();
    }

    private final int[] colors = new int[] {
            ColorTemplate.VORDIPLOM_COLORS[0],
            ColorTemplate.VORDIPLOM_COLORS[1],
            ColorTemplate.VORDIPLOM_COLORS[2]
    };


}
