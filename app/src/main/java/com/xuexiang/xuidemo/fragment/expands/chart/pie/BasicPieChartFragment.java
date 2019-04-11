package com.xuexiang.xuidemo.fragment.expands.chart.pie;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.xuexiang.xaop.annotation.Permission;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.dialog.bottomsheet.BottomSheet;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xutil.tip.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.xuexiang.xaop.consts.PermissionConsts.STORAGE;

/**
 * @author xuexiang
 * @since 2019/4/11 下午11:32
 */
@Page(name = "BasicPieChart\n基础饼图，详细API")
public class BasicPieChartFragment extends BaseFragment implements OnChartValueSelectedListener {

    @BindView(R.id.chart1)
    PieChart chart;

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pie_chart;
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
        //使用百分百显示
        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);

        //设置拖拽的阻尼，0为立即停止
        chart.setDragDecelerationFrictionCoef(0.95f);

        //设置图标中心文字
        chart.setCenterText(generateCenterSpannableText());
        chart.setDrawCenterText(true);
        //设置图标中心空白，空心
        chart.setDrawHoleEnabled(true);
        //设置空心圆的弧度百分比，最大100
        chart.setHoleRadius(58f);
        chart.setHoleColor(Color.WHITE);
        //设置透明弧的样式
        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);
        chart.setTransparentCircleRadius(61f);

        //设置可以旋转
        chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);

        chart.setOnChartValueSelectedListener(this);

        setChartData(4, 10);

        chart.animateY(1400, Easing.EaseInOutQuad);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        chart.setEntryLabelColor(Color.WHITE);
        chart.setEntryLabelTextSize(12f);
    }

    protected final String[] parties = new String[] {
            "Party A", "Party B", "Party C", "Party D", "Party E", "Party F", "Party G", "Party H",
            "Party I", "Party J", "Party K", "Party L", "Party M", "Party N", "Party O", "Party P",
            "Party Q", "Party R", "Party S", "Party T", "Party U", "Party V", "Party W", "Party X",
            "Party Y", "Party Z"
    };


    /**
     * 设置图表数据
     *
     * @param count 柱状图中柱的数量
     * @param range
     */
    private void setChartData(int count, float range) {
        List<PieEntry> entries = new ArrayList<>();
        for (int i = 0; i < count ; i++) {
            //设置数据源
            entries.add(new PieEntry((float) ((Math.random() * range) + range / 5), parties[i % parties.length], getResources().getDrawable(R.drawable.ic_star_green)));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Election Results");
        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);


        List<Integer> colors = new ArrayList<>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(c);
        }
        for (int c : ColorTemplate.JOYFUL_COLORS) {
            colors.add(c);
        }
        for (int c : ColorTemplate.COLORFUL_COLORS) {
            colors.add(c);
        }
        for (int c : ColorTemplate.LIBERTY_COLORS) {
            colors.add(c);
        }
        for (int c : ColorTemplate.PASTEL_COLORS) {
            colors.add(c);
        }
        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(chart));
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        chart.setData(data);

        // undo all highlights
        chart.highlightValues(null);
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

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        ToastUtils.toast("选中了,  x轴值:" + e.getX() + ", y轴值:" + e.getY());

    }

    @Override
    public void onNothingSelected() {

    }

    private void showBottomSheetList() {
        new BottomSheet.BottomListSheetBuilder(getActivity())
                .addItem(getResources().getString(R.string.chart_toggle_x_values))
                .addItem(getResources().getString(R.string.chart_toggle_y_values))
                .addItem(getResources().getString(R.string.chart_toggle_icons))
                .addItem(getResources().getString(R.string.chart_toggle_percent))
                .addItem(getResources().getString(R.string.chart_toggle_hole))
                .addItem(getResources().getString(R.string.chart_toggle_center_text))
                .addItem(getResources().getString(R.string.chart_toggle_curved_slices))
                .addItem(getResources().getString(R.string.chart_spin_animation))
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
                                chart.setDrawEntryLabels(!chart.isDrawEntryLabelsEnabled());
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
                                chart.setUsePercentValues(!chart.isUsePercentValuesEnabled());
                                chart.invalidate();
                                break;
                            case 4:
                                chart.setDrawHoleEnabled(!chart.isDrawHoleEnabled());
                                chart.invalidate();
                                break;
                            case 5:
                                chart.setDrawCenterText(!chart.isDrawCenterTextEnabled());
                                chart.invalidate();
                                break;
                            case 6:
                                boolean toSet = !chart.isDrawRoundedSlicesEnabled() || !chart.isDrawHoleEnabled();
                                chart.setDrawRoundedSlices(toSet);
                                if (toSet && !chart.isDrawHoleEnabled()) {
                                    chart.setDrawHoleEnabled(true);
                                }
                                if (toSet && chart.isDrawSlicesUnderHoleEnabled()) {
                                    chart.setDrawSlicesUnderHole(false);
                                }
                                chart.invalidate();
                                break;
                            case 7:
                                chart.spin(1000, chart.getRotationAngle(), chart.getRotationAngle() + 360, Easing.EaseInOutCubic);
                                break;
                            case 8:
                                chart.animateX(1400);
                                break;
                            case 9:
                                chart.animateY(1400);
                                break;
                            case 10:
                                chart.animateXY(1400, 1400);
                                break;
                            case 11:
                                saveToGallery(chart, "SimpleBarChart");
                                break;
                            default:
                                break;
                        }
                    }
                })
                .build()
                .show();

    }

    /**
     * 图表保存
     *
     * @param chart
     * @param name
     */
    @Permission(STORAGE)
    protected void saveToGallery(Chart chart, String name) {
        if (chart.saveToGallery(name + "_" + System.currentTimeMillis(), 70)) {
            ToastUtils.toast("保存成功!");

        } else {
            ToastUtils.toast("保存失败!");
        }
    }
}
