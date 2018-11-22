package com.xuexiang.xui.widget.button.shadowbutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.xuexiang.xui.R;


/**
 * 可设置阴影效果的ImageView
 *
 * @author XUE
 * @date 2017/9/13 14:22
 */
public class ShadowImageView extends AppCompatImageView {

    private int mWidth;
    private int mHeight;
    private int mPaintAlpha = 48;

    private int mPressedColor;
    private Paint mPaint;
    private int mShapeType;
    private int mRadius;

    public ShadowImageView(Context context) {
        super(context);
        init(context, null);
    }

    public ShadowImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ShadowImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(final Context context, final AttributeSet attrs) {
        if (isInEditMode()) {
            return;
        }
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShadowButton);
        mPressedColor = typedArray.getColor(R.styleable.ShadowButton_sb_color_pressed, getResources().getColor(R.color.default_shadow_button_color_pressed));
        mPaintAlpha = typedArray.getInteger(R.styleable.ShadowButton_sb_alpha_pressed, mPaintAlpha);
        mShapeType = typedArray.getInt(R.styleable.ShadowButton_sb_shape_type, 1);
        mRadius = typedArray.getDimensionPixelSize(R.styleable.ShadowButton_sb_radius, getResources().getDimensionPixelSize(R.dimen.default_shadow_button_radius));
        typedArray.recycle();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mPressedColor);
        this.setWillNotDraw(false);
        mPaint.setAlpha(0);
        mPaint.setAntiAlias(true);
        this.setDrawingCacheEnabled(true);
        this.setClickable(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mPaint == null) {
            return;
        }
        if (mShapeType == 0) {
            canvas.drawCircle(mWidth / 2, mHeight / 2, mWidth / 2.1038f, mPaint);
        } else {
            RectF rectF = new RectF();
            rectF.set(0, 0, mWidth, mHeight);
            canvas.drawRoundRect(rectF, mRadius, mRadius, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPaint.setAlpha(mPaintAlpha);
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mPaint.setAlpha(0);
                invalidate();
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    public int getPressedColor() {
        return mPressedColor;
    }

    /**
     * Set the pressed color.
     *
     * @param pressedColor pressed color
     */
    public void setPressedColor(int pressedColor) {
        mPaint.setColor(mPressedColor);
        invalidate();
    }
}
