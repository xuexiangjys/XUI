package com.xuexiang.xui.widget.textview.marqueen;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xuexiang.xui.R;

/**
 * 复合字幕
 * @author XUE
 * @date 2017/9/13 10:07
 */
public class ComplexViewMF extends MarqueeFactory<RelativeLayout, ComplexItemEntity> {
    private LayoutInflater inflater;

    public ComplexViewMF(Context context) {
        super(context);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RelativeLayout generateMarqueeItemView(ComplexItemEntity data) {
        RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.xui_layout_complex_view, null);
        ((TextView) view.findViewById(R.id.title)).setText(data.getTitle());
        ((TextView) view.findViewById(R.id.subTitle)).setText(data.getSubTitle());
        ((TextView) view.findViewById(R.id.time)).setText(data.getTime());
        return view;
    }
}