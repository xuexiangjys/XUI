package com.xuexiang.xuidemo.fragment.expands.chart.line;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import android.view.View;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;
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
 * @since 2019/4/13 下午6:46
 */
@Page(name = "BasicLineChart\n基础折线图，详细API")
public class BasicLineChartFragment extends BaseChartFragment implements OnChartValueSelectedListener {

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
        setChartData(30, 180);

        chart.animateX(1500);
        chart.setOnChartValueSelectedListener(this);
    }

    /**
     * 初始化图表的样式
     */
    @Override
    protected void initChartStyle() {
        chart.setBackgroundColor(Color.WHITE);
        //关闭描述
        chart.getDescription().setEnabled(false);
        //设置不画背景网格
        chart.setDrawGridBackground(false);

        CustomMarkerView mv = new CustomMarkerView(getContext(), R.layout.marker_view_xy);
        mv.setChartView(chart);
        chart.setMarker(mv);

        //开启所有触摸手势
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setPinchZoom(true);

        initXYAxisStyle();
    }

    /**
     * 初始化图表X、Y轴的样式
     */
    private void initXYAxisStyle() {
        XAxis xAxis = chart.getXAxis();
        // 设置垂直的网格线
        xAxis.enableGridDashedLine(10f, 10f, 0f);

        YAxis yAxis = chart.getAxisLeft();
        // 设置水平的网格线
        yAxis.enableGridDashedLine(10f, 10f, 0f);
        // axis range
        yAxis.setAxisMaximum(200f);
        yAxis.setAxisMinimum(-50f);
        // 关闭Y轴右侧
        chart.getAxisRight().setEnabled(false);

        LimitLine upperLimit = new LimitLine(150f, "Upper Limit");
        upperLimit.setLineWidth(4f);
        upperLimit.enableDashedLine(10f, 10f, 0f);
        upperLimit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        upperLimit.setTextSize(10f);

        LimitLine lowerLimit = new LimitLine(-30f, "Lower Limit");
        lowerLimit.setLineWidth(4f);
        lowerLimit.enableDashedLine(10f, 10f, 0f);
        lowerLimit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        lowerLimit.setTextSize(10f);

        // draw limit lines behind data instead of on top
        xAxis.setDrawLimitLinesBehindData(true);
        yAxis.setDrawLimitLinesBehindData(true);

        // add limit lines
        yAxis.addLimitLine(upperLimit);
        yAxis.addLimitLine(lowerLimit);
    }

    /**
     * 初始化图表的 标题 样式
     */
    @Override
    protected void initChartLabel() {
        Legend legend = chart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
    }

    /**
     * 设置图表数据
     *
     * @param count 一组数据的数量
     * @param range
     */
    @Override
    protected void setChartData(int count, float range) {
        List<Entry> values = new ArrayList<>();
        //设置数据源
        for (int i = 0; i < count; i++) {
            float val = (float) (Math.random() * range) - 30;
            values.add(new Entry(i, val, getResources().getDrawable(R.drawable.ic_star_green)));
        }
        LineDataSet set1;

        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            set1.notifyDataSetChanged();
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            set1 = new LineDataSet(values, "DataSet 1");
            set1.setDrawIcons(false);
            // 设置画虚线
            set1.enableDashedLine(10f, 5f, 0f);
            // 设置线的样式
            set1.setColor(Color.BLACK);
            set1.setLineWidth(1f);
            //设置点的样式
            set1.setCircleColor(Color.BLACK);
            set1.setCircleRadius(3f);
            // 设置不画空心圆
            set1.setDrawCircleHole(false);

            // 设置数据组 标题的样式
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);

            // text size of values
            set1.setValueTextSize(9f);
            // draw selection line as dashed
            set1.enableDashedHighlightLine(10f, 5f, 0f);

            // 设置折线图的填充区域
            set1.setDrawFilled(true);
            set1.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    if (chart == null) return 0;
                    return chart.getAxisLeft().getAxisMinimum();
                }
            });

            // 设置折线图的填充颜色
            if (Utils.getSDKInt() >= 18) {
                // drawables only supported on api level 18 and above
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.bg_fade_red);
                set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(Color.BLACK);
            }

            List<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            LineData data = new LineData(dataSets);
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
                .addItem(getResources().getString(R.string.chart_toggle_values))
                .addItem(getResources().getString(R.string.chart_toggle_icons))
                .addItem(getResources().getString(R.string.chart_toggle_filled))
                .addItem(getResources().getString(R.string.chart_toggle_circles))
                .addItem(getResources().getString(R.string.chart_toggle_cubic))
                .addItem(getResources().getString(R.string.chart_toggle_pinch_zoom))
                .addItem(getResources().getString(R.string.chart_toggle_highlight))
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
                                for (IDataSet<?> set : chart.getData().getDataSets()) {
                                    set.setDrawValues(!set.isDrawValuesEnabled());
                                }
                                chart.invalidate();
                                break;
                            case 1:
                                for (IDataSet set : chart.getData().getDataSets()) {
                                    set.setDrawIcons(!set.isDrawIconsEnabled());
                                }
                                chart.invalidate();
                                break;
                            case 2:
                                for (ILineDataSet iSet : chart.getData().getDataSets()) {
                                    LineDataSet set = (LineDataSet) iSet;
                                    set.setDrawFilled(!set.isDrawFilledEnabled());
                                }
                                chart.invalidate();
                                break;
                            case 3:
                                for (ILineDataSet iSet : chart.getData().getDataSets()) {
                                    LineDataSet set = (LineDataSet) iSet;
                                    set.setDrawCircles(!set.isDrawCirclesEnabled());
                                }
                                chart.invalidate();
                                break;
                            case 4:
                                for (ILineDataSet iSet : chart.getData().getDataSets()) {
                                    LineDataSet set = (LineDataSet) iSet;
                                    set.setMode(set.getMode() == LineDataSet.Mode.CUBIC_BEZIER
                                            ? LineDataSet.Mode.LINEAR
                                            : LineDataSet.Mode.CUBIC_BEZIER);
                                }
                                chart.invalidate();
                                break;
                            case 5:
                                chart.setPinchZoom(!chart.isPinchZoomEnabled());
                                chart.invalidate();
                                break;
                            case 6:
                                if (chart.getData() != null) {
                                    chart.getData().setHighlightEnabled(!chart.getData().isHighlightEnabled());
                                    chart.invalidate();
                                }
                                break;
                            case 7:
                                chart.animateX(2000);
                                break;
                            case 8:
                                chart.animateY(2000, Easing.EaseInCubic);
                                break;
                            case 9:
                                chart.animateXY(2000, 2000);
                                break;
                            case 10:
                                saveToGallery(chart, "BasicLineChart");
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
