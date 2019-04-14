
package com.xuexiang.xuidemo.fragment.expands.chart.radar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.xuexiang.xuidemo.R;

import java.text.DecimalFormat;

/**
 * 标识雷达图上各点的数字控件
 *
 * @author xuexiang
 * @since 2019/4/10 上午12:18
 */
@SuppressLint("ViewConstructor")
public class RadarMarkerView extends MarkerView {

    private final TextView tvContent;
    private final DecimalFormat format = new DecimalFormat("##0");

    public RadarMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);

        tvContent = findViewById(R.id.tvContent);
    }

    // runs every time the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        tvContent.setText(String.format("%s %%", format.format(e.getY())));

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight() - 10);
    }
}
