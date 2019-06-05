package com.xuexiang.xuidemo.fragment.expands.chart.bar;

import androidx.core.content.ContextCompat;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.model.GradientColor;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.dialog.bottomsheet.BottomSheet;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.fragment.expands.chart.BaseChartFragment;
import com.xuexiang.xutil.tip.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author xuexiang
 * @since 2019/4/10 下午11:03
 */
@Page(name = "BasicBarChart\n基础的柱状图，详细API")
public class BasicBarChartFragment extends BaseChartFragment implements OnChartValueSelectedListener {

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

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.addAction(new TitleBar.ImageAction(R.drawable.icon_topbar_overflow) {
            @Override
            public void performAction(View view) {
                showBottomSheetList();
            }
        });
        return titleBar;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        initChartStyle();
        initChartLabel();
        setChartData(12, 50);

        chart.setOnChartValueSelectedListener(this);
    }

    /**
     * 初始化图表的样式
     */
    @Override
    protected void initChartStyle() {
        //关闭描述
        chart.getDescription().setEnabled(false);
        chart.setDrawBarShadow(false);

        //开启在柱状图顶端显示值
        chart.setDrawValueAboveBar(true);
        //设置显示值时，最大的柱数量
        chart.setMaxVisibleValueCount(60);

        //设置不能同时在x轴和y轴上缩放
        chart.setPinchZoom(false);
        //设置不画背景网格
        chart.setDrawGridBackground(false);

        initXYAxisStyle();
    }

    /**
     * 初始化图表X、Y轴的样式
     */
    private void initXYAxisStyle() {
        //设置X轴样式
        ValueFormatter xAxisFormatter = new DayAxisValueFormatter(chart);
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        //间隔一天显示
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);

        //设置Y轴的左侧样式
        ValueFormatter yAxisFormatter = new MoneyValueFormatter("$");
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(yAxisFormatter);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        //设置Y轴的右侧样式
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setLabelCount(8, false);
        rightAxis.setValueFormatter(yAxisFormatter);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        //设置图表的数值指示器
        XYMarkerView mv = new XYMarkerView(getContext(), xAxisFormatter, yAxisFormatter);
        mv.setChartView(chart);
        chart.setMarker(mv);
    }

    /**
     * 初始化图表的 标题 样式
     */
    @Override
    protected void initChartLabel() {
        //设置图表 标题 的样式
        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
    }

    /**
     * 设置图表数据
     *
     * @param count 柱状图中柱的数量
     * @param range
     */
    @Override
    protected void setChartData(int count, float range) {
        float start = 1f;
        List<BarEntry> values = new ArrayList<>();
        //设置数据源
        for (int i = (int) start; i < start + count; i++) {
            float val = (float) (Math.random() * (range + 1));
            if (Math.random() * 100 < 25) {
                //设置图表，标星
                values.add(new BarEntry(i, val, getResources().getDrawable(R.drawable.ic_star_green)));
            } else {
                values.add(new BarEntry(i, val));
            }
        }

        BarDataSet set1;

        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();

        } else {
            set1 = new BarDataSet(values, "The year 2019");

            //设置是否画出图标
            set1.setDrawIcons(false);

            int startColor1 = ContextCompat.getColor(getContext(), android.R.color.holo_orange_light);
            int startColor2 = ContextCompat.getColor(getContext(), android.R.color.holo_blue_light);
            int startColor3 = ContextCompat.getColor(getContext(), android.R.color.holo_orange_light);
            int startColor4 = ContextCompat.getColor(getContext(), android.R.color.holo_green_light);
            int startColor5 = ContextCompat.getColor(getContext(), android.R.color.holo_red_light);
            int endColor1 = ContextCompat.getColor(getContext(), android.R.color.holo_blue_dark);
            int endColor2 = ContextCompat.getColor(getContext(), android.R.color.holo_purple);
            int endColor3 = ContextCompat.getColor(getContext(), android.R.color.holo_green_dark);
            int endColor4 = ContextCompat.getColor(getContext(), android.R.color.holo_red_dark);
            int endColor5 = ContextCompat.getColor(getContext(), android.R.color.holo_orange_dark);

            List<GradientColor> gradientColors = new ArrayList<>();
            gradientColors.add(new GradientColor(startColor1, endColor1));
            gradientColors.add(new GradientColor(startColor2, endColor2));
            gradientColors.add(new GradientColor(startColor3, endColor3));
            gradientColors.add(new GradientColor(startColor4, endColor4));
            gradientColors.add(new GradientColor(startColor5, endColor5));

            //设置渐变色
            set1.setGradientColors(gradientColors);

            //这里只设置了一组数据
            List<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(0.9f);

            chart.setData(data);
        }
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        ToastUtils.toast("选中了,  x轴值:" + e.getX() + ", y轴值:" + e.getY());
    }

    @Override
    public void onNothingSelected() {

    }


    private void showBottomSheetList() {
        new BottomSheet.BottomListSheetBuilder(getActivity())
                .addItem(getResources().getString(R.string.chart_toggle_bar_borders))
                .addItem(getResources().getString(R.string.chart_toggle_values))
                .addItem(getResources().getString(R.string.chart_toggle_icons))
                .addItem(getResources().getString(R.string.chart_animate_x))
                .addItem(getResources().getString(R.string.chart_animate_y))
                .addItem(getResources().getString(R.string.chart_animate_xy))
                .addItem(getResources().getString(R.string.chart_save))
                .setOnSheetItemClickListener(new BottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(BottomSheet dialog, View itemView, int position, String tag) {
                        dialog.dismiss();
                        switch (position) {
                            case 0:
                                for (IBarDataSet set : chart.getData().getDataSets()) {
                                    ((BarDataSet) set).setBarBorderWidth(set.getBarBorderWidth() == 1.f ? 0.f : 1.f);
                                }
                                chart.invalidate();
                                break;
                            case 1:
                                for (IDataSet<?> set : chart.getData().getDataSets()) {
                                    set.setDrawValues(!set.isDrawValuesEnabled());
                                }
                                chart.invalidate();
                                break;
                            case 2:
                                for (IDataSet set : chart.getData().getDataSets()) {
                                    set.setDrawIcons(!set.isDrawIconsEnabled());
                                }
                                chart.invalidate();
                                break;
                            case 3:
                                chart.animateX(2000);
                                break;
                            case 4:
                                chart.animateY(2000);
                                break;
                            case 5:
                                chart.animateXY(2000, 2000);
                                break;
                            case 6:
                                saveToGallery(chart, "BasicBarChart");
                                break;
                            default:
                                break;
                        }
                    }
                })
                .build()
                .show();

    }
}
