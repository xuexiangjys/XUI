package com.xuexiang.xui.widget.textview.supertextview;

import android.content.Context;
import android.graphics.Typeface;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import uk.co.chrisjenx.calligraphy.HasTypeface;

/**
 * 基础TextView
 *
 * @author xuexiang
 * @since 2019/1/14 下午10:09
 */
public class BaseTextView extends LinearLayout implements HasTypeface {

    private Context mContext;

    private TextView topTextView, centerTextView, bottomTextView;

    private LayoutParams topTVParams, centerTVParams, bottomTVParams;

    public BaseTextView(Context context) {
        super(context);
        init(context);
    }

    public BaseTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BaseTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.setOrientation(VERTICAL);

        mContext = context;

        initView();
    }

    private void initView() {
        initTopView();
        initCenterView();
        initBottomView();
    }

    private void initTopView() {
        if (topTVParams == null) {
            topTVParams = getParams(topTVParams);
        }
        if (topTextView == null) {
            topTextView = initTextView(topTVParams, topTextView);
        }
    }

    private void initCenterView() {
        if (centerTVParams == null) {
            centerTVParams = getParams(centerTVParams);
        }
        if (centerTextView == null) {
            centerTextView = initTextView(centerTVParams, centerTextView);
        }
    }

    private void initBottomView() {
        if (bottomTVParams == null) {
            bottomTVParams = getParams(bottomTVParams);
        }
        if (bottomTextView == null) {
            bottomTextView = initTextView(bottomTVParams, bottomTextView);
        }
    }


    private TextView initTextView(LayoutParams params, TextView textView) {

        textView = getTextView(textView, params);
        textView.setGravity(Gravity.CENTER);
        addView(textView);
        return textView;
    }


    /**
     * 初始化textView
     *
     * @param textView     对象
     * @param layoutParams 对象
     * @return 返回
     */
    public TextView getTextView(TextView textView, LayoutParams layoutParams) {
        if (textView == null) {
            textView = new TextView(mContext);
            textView.setLayoutParams(layoutParams);
            textView.setVisibility(GONE);
        }
        return textView;
    }

    /**
     * 初始化Params
     *
     * @param params 对象
     * @return 返回
     */
    public LayoutParams getParams(LayoutParams params) {
        if (params == null) {
            // TODO: 2017/7/21 问题记录 ：之前设置 MATCH_PARENT导致每次重新设置string的时候，textView的宽度都以第一次为准，在列表中使用的时候复用出现混乱，特此记录一下，以后处理好布局之间套用时候设置WRAP_CONTENT和MATCH_PARENT出现问题
            params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        }
        return params;
    }


    private void setTextString(TextView textView, CharSequence textString) {
        textView.setText(textString);
        if (!TextUtils.isEmpty(textString)) {
            textView.setVisibility(VISIBLE);
        }
    }

    public void setTopTextString(CharSequence s) {
        setTextString(topTextView, s);
    }


    public void setCenterTextString(CharSequence s) {
        setTextString(centerTextView, s);
    }

    public void setBottomTextString(CharSequence s) {
        setTextString(bottomTextView, s);
    }

    public TextView getTopTextView() {
        return topTextView;
    }

    public TextView getCenterTextView() {
        return centerTextView;
    }

    public TextView getBottomTextView() {
        return bottomTextView;
    }

    public void setMaxEms(int topMaxEms, int centerMaxEms, int bottomMaxEms) {

        topTextView.setEllipsize(TextUtils.TruncateAt.END);
        centerTextView.setEllipsize(TextUtils.TruncateAt.END);
        bottomTextView.setEllipsize(TextUtils.TruncateAt.END);

        topTextView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(topMaxEms)});
        centerTextView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(centerMaxEms)});
        bottomTextView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(bottomMaxEms)});
    }

    public void setCenterSpaceHeight(int centerSpaceHeight) {
        topTVParams.setMargins(0, 0, 0, centerSpaceHeight / 2);
        centerTVParams.setMargins(0, centerSpaceHeight / 2, 0, centerSpaceHeight / 2);
        bottomTVParams.setMargins(0, centerSpaceHeight / 2, 0, 0);
    }

    @Override
    public void setTypeface(Typeface typeface) {
        topTextView.setTypeface(typeface);
        centerTextView.setTypeface(typeface);
        bottomTextView.setTypeface(typeface);
    }
}
