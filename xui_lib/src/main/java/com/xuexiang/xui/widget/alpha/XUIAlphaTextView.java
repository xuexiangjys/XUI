package com.xuexiang.xui.widget.alpha;

import android.content.Context;
import android.util.AttributeSet;

import android.support.v7.widget.AppCompatTextView;

/**
 * 在 pressed 和 disabled 时改变 View 的透明度
 *
 * @author xuexiang
 * @since 2018/11/30 下午1:12
 */
public class XUIAlphaTextView extends AppCompatTextView {

    private IAlphaViewHelper mAlphaViewHelper;

    public XUIAlphaTextView(Context context) {
        super(context);
    }

    public XUIAlphaTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XUIAlphaTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private IAlphaViewHelper getAlphaViewHelper() {
        if (mAlphaViewHelper == null) {
            mAlphaViewHelper = new XUIAlphaViewHelper(this);
        }
        return mAlphaViewHelper;
    }

    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);
        getAlphaViewHelper().onPressedChanged(this, pressed);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        getAlphaViewHelper().onEnabledChanged(this, enabled);
    }

    /**
     * 设置是否要在 press 时改变透明度
     *
     * @param changeAlphaWhenPress 是否要在 press 时改变透明度
     */
    public void setChangeAlphaWhenPress(boolean changeAlphaWhenPress) {
        getAlphaViewHelper().setChangeAlphaWhenPress(changeAlphaWhenPress);
    }

    /**
     * 设置是否要在 disabled 时改变透明度
     *
     * @param changeAlphaWhenDisable 是否要在 disabled 时改变透明度
     */
    public void setChangeAlphaWhenDisable(boolean changeAlphaWhenDisable) {
        getAlphaViewHelper().setChangeAlphaWhenDisable(changeAlphaWhenDisable);
    }
}
