package com.xuexiang.xuidemo.fragment.expands.chart.pie;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.widget.LinearLayout;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.utils.DensityUtils;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.fragment.expands.chart.BaseChartFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author xuexiang
 * @since 2019/4/13 下午5:57
 */
@Page(name = "HalfPieChart\n半个饼图")
public class HalfPieChartFragment extends BaseChartFragment {

    @BindView(R.id.chart1)
    PieChart chart;

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chart_pie;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        moveOffScreen();

        initChartStyle();
        initChartLabel();
        setChartData(4, 100);

        chart.animateY(1400, Easing.EaseInOutQuad);
    }

    /**
     * 初始化图表的样式
     */
    @Override
    protected void initChartStyle() {
        chart.setBackgroundColor(Color.WHITE);
        //使用百分百显示
        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);

        //设置图标中心文字
        chart.setCenterText(generateCenterSpannableText());
        chart.setDrawCenterText(true);
        chart.setCenterTextOffset(0, -20);
        //设置图标中心空白，空心
        chart.setDrawHoleEnabled(true);
        chart.setHoleRadius(58f);
        chart.setHoleColor(Color.WHITE);
        //设置透明弧的样式
        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);
        chart.setTransparentCircleRadius(61f);

        //设置不可以旋转
        chart.setRotationEnabled(false);
        chart.setHighlightPerTapEnabled(true);

        //====设置半圆弧===//
        chart.setMaxAngle(180f);
        chart.setRotationAngle(180f);
    }

    @Override
    protected void initChartLabel() {
        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        chart.setEntryLabelColor(Color.WHITE);
        chart.setEntryLabelTextSize(12f);
    }

    /**
     * 设置图表数据
     *
     * @param count 一组数据的数量
     * @param range
     */
    @Override
    protected void setChartData(int count, float range) {
        List<PieEntry> values = new ArrayList<>();
        //设置数据源
        for (int i = 0; i < count; i++) {
            values.add(new PieEntry((float) ((Math.random() * range) + range / 5), parties[i % parties.length]));
        }
        PieDataSet dataSet = new PieDataSet(values, "Election Results");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        chart.setData(data);

        chart.invalidate();
    }

    /**
     * 生成饼图中间的文字
     * @return
     */
    private SpannableString generateCenterSpannableText() {
        SpannableString s = new SpannableString("MPAndroidChart\ndeveloped by Philipp Jahoda");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 14, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
        s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
        return s;
    }

    /**
     * 进行偏移，将宁外一半的圆弧隐藏掉
     */
    private void moveOffScreen() {
        int height = DensityUtils.getDisplayMetrics().heightPixels;

        /* percent to move */
        int offset = (int)(height * 0.65);

        LinearLayout.LayoutParams rlParams = (LinearLayout.LayoutParams) chart.getLayoutParams();
        rlParams.setMargins(0, 0, 0, -offset);
        chart.setLayoutParams(rlParams);
    }



}
