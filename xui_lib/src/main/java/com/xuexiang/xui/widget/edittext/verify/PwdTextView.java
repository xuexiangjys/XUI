package com.xuexiang.xui.widget.edittext.verify;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * 可显示文字或密码
 *
 * @author XUE
 * @since 2019/5/7 11:21
 */
public class PwdTextView extends AppCompatTextView {

    private float mRadius;
    private boolean mHasPassword;

    private Paint mPaint;

    public PwdTextView(Context context) {
        this(context, null);
    }

    public PwdTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PwdTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        // 画一个黑色的圆
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mHasPassword) {
            canvas.drawCircle(getWidth() >> 1, getHeight() >> 1, mRadius, mPaint);
        } else {
            super.onDraw(canvas);
        }
    }

    /**
     * 清除密码
     */
    public void clearPassword() {
        mHasPassword = false;
        invalidate();
    }

    /**
     * 绘制密码
     *
     * @param radius
     */
    public void drawPassword(float radius) {
        mHasPassword = true;
        if (radius == 0) {
            mRadius = getWidth() >> 2; //除以4
        } else {
            mRadius = radius;
        }
        invalidate();
    }

}
