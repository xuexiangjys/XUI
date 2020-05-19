package com.xuexiang.xui.widget.textview.supertextview;

import android.content.Context;
import android.graphics.Typeface;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.github.inflationx.calligraphy3.HasTypeface;

/**
 * 基础TextView
 *
 * @author xuexiang
 * @since 2019/1/14 下午10:09
 */
public class BaseTextView extends LinearLayout implements HasTypeface {

    private Context mContext;

    private TextView mTopTextView, mCenterTextView, mBottomTextView;

    private LayoutParams mTopTVParams, mCenterTVParams, mBottomTVParams;

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
        if (mTopTVParams == null) {
            mTopTVParams = getParams(mTopTVParams);
        }
        if (mTopTextView == null) {
            mTopTextView = initTextView(mTopTVParams, mTopTextView);
        }
    }

    private void initCenterView() {
        if (mCenterTVParams == null) {
            mCenterTVParams = getParams(mCenterTVParams);
        }
        if (mCenterTextView == null) {
            mCenterTextView = initTextView(mCenterTVParams, mCenterTextView);
        }
    }

    private void initBottomView() {
        if (mBottomTVParams == null) {
            mBottomTVParams = getParams(mBottomTVParams);
        }
        if (mBottomTextView == null) {
            mBottomTextView = initTextView(mBottomTVParams, mBottomTextView);
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
        setTextString(mTopTextView, s);
    }


    public void setCenterTextString(CharSequence s) {
        setTextString(mCenterTextView, s);
    }

    public void setBottomTextString(CharSequence s) {
        setTextString(mBottomTextView, s);
    }

    public TextView getTopTextView() {
        return mTopTextView;
    }

    public TextView getCenterTextView() {
        return mCenterTextView;
    }

    public TextView getBottomTextView() {
        return mBottomTextView;
    }

    public void setMaxEms(int topMaxEms, int centerMaxEms, int bottomMaxEms) {

        mTopTextView.setEllipsize(TextUtils.TruncateAt.END);
        mCenterTextView.setEllipsize(TextUtils.TruncateAt.END);
        mBottomTextView.setEllipsize(TextUtils.TruncateAt.END);

        mTopTextView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(topMaxEms)});
        mCenterTextView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(centerMaxEms)});
        mBottomTextView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(bottomMaxEms)});
    }

    public void setCenterSpaceHeight(int centerSpaceHeight) {
        mTopTVParams.setMargins(0, 0, 0, centerSpaceHeight / 2);
        mCenterTVParams.setMargins(0, centerSpaceHeight / 2, 0, centerSpaceHeight / 2);
        mBottomTVParams.setMargins(0, centerSpaceHeight / 2, 0, 0);
    }

    @Override
    public void setTypeface(Typeface typeface) {
        if (mTopTextView != null) {
            mTopTextView.setTypeface(typeface);
        }
        if (mCenterTextView != null) {
            mCenterTextView.setTypeface(typeface);
        }
        if (mBottomTextView != null) {
            mBottomTextView.setTypeface(typeface);
        }
    }



}
