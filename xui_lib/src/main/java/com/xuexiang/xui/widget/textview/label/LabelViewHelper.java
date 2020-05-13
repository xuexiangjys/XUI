package com.xuexiang.xui.widget.textview.label;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.xuexiang.xui.R;
import com.xuexiang.xui.XUI;
import com.xuexiang.xui.utils.Utils;

/**
 * 标签控件
 *
 * @author xuexiang
 * @since 2018/12/2 下午11:59
 */
public final class LabelViewHelper {

    private static final int LEFT_TOP = 1;
    private static final int RIGHT_TOP = 2;
    private static final int LEFT_BOTTOM = 3;
    private static final int RIGHT_BOTTOM = 4;

    private static final int DEFAULT_DISTANCE = 40;
    private static final int DEFAULT_HEIGHT = 20;
    /**
     * 默认边框颜色为透明
     */
    private static final int DEFAULT_STROKE_COLOR = 0x00000000;
    private static final int DEFAULT_STROKE_WIDTH = 1;
    private static final int DEFAULT_TEXT_SIZE = 14;
    private static final int DEFAULT_BACKGROUND_COLOR = 0x9F27CDC0;
    private static final int DEFAULT_TEXT_COLOR = 0xFFFFFFFF;
    private static final int DEFAULT_ORIENTATION = LEFT_TOP;
    private static final int DEFAULT_TEXT_STYLE = 0;

    private Context mContext;
    private int mAlpha;

    private int mDistance;
    private int mHeight;
    private int mBackgroundColor;

    private int mStrokeWidth;
    private int mStrokeColor;

    private String mText;
    private int mTextSize;
    private int mTextStyle;
    private String mTextFont;
    private int mTextColor;

    private boolean mVisual;
    private int mOrientation;

    private Paint mRectPaint;
    private Paint mRectStrokePaint;
    // simulator
    private Path mRectPath;
    private Path mTextPath;
    private Paint mTextPaint;
    private Rect mTextBound;

    public LabelViewHelper(Context context, AttributeSet attrs, int defStyleAttr) {

        mContext = context;

        initAttrs(context, attrs, defStyleAttr);

        initPaints();
    }

    private void initAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.LabelView, defStyleAttr, 0);
        mDistance = array.getDimensionPixelSize(R.styleable.LabelView_label_distance, dip2Px(DEFAULT_DISTANCE));
        mHeight = array.getDimensionPixelSize(R.styleable.LabelView_label_height, dip2Px(DEFAULT_HEIGHT));
        mBackgroundColor = array.getColor(R.styleable.LabelView_label_backgroundColor, DEFAULT_BACKGROUND_COLOR);
        mStrokeWidth = array.getDimensionPixelSize(R.styleable.LabelView_label_strokeWidth, dip2Px(DEFAULT_STROKE_WIDTH));
        mStrokeColor = array.getColor(R.styleable.LabelView_label_strokeColor, DEFAULT_STROKE_COLOR);
        mText = array.getString(R.styleable.LabelView_label_text);
        mTextSize = array.getDimensionPixelSize(R.styleable.LabelView_label_textSize, dip2Px(DEFAULT_TEXT_SIZE));
        mTextStyle = array.getInt(R.styleable.LabelView_label_textStyle, DEFAULT_TEXT_STYLE);
        mTextFont = array.getString(R.styleable.LabelView_label_textFont);
        mTextColor = array.getColor(R.styleable.LabelView_label_textColor, DEFAULT_TEXT_COLOR);
        mVisual = array.getBoolean(R.styleable.LabelView_label_visual, true);
        mOrientation = array.getInteger(R.styleable.LabelView_label_orientation, DEFAULT_ORIENTATION);
        array.recycle();
    }

    private void initPaints() {
        mRectPaint = new Paint();
        mRectPaint.setDither(true);
        mRectPaint.setAntiAlias(true);
        mRectPaint.setStyle(Paint.Style.FILL);

        mRectStrokePaint = new Paint();
        mRectStrokePaint.setDither(true);
        mRectStrokePaint.setAntiAlias(true);
        mRectStrokePaint.setStyle(Paint.Style.STROKE);

        mRectPath = new Path();
        mRectPath.reset();

        mTextPath = new Path();
        mTextPath.reset();

        mTextPaint = new Paint();
        mTextPaint.setDither(true);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStrokeJoin(Paint.Join.ROUND);
        mTextPaint.setStrokeCap(Paint.Cap.SQUARE);

        mTextBound = new Rect();
    }

    public void onDraw(Canvas canvas, int measuredWidth, int measuredHeight) {
        if (!mVisual || mText == null) {
            return;
        }

        float actualDistance = mDistance + mHeight / 2F;
        calcOffset(measuredWidth, measuredHeight);

        mRectPaint.setColor(mBackgroundColor);
        if (mAlpha != 0) {
            mRectPaint.setAlpha(mAlpha);
        }

        mRectStrokePaint.setColor(mStrokeColor);
        mRectStrokePaint.setStrokeWidth(mStrokeWidth);

        canvas.drawPath(mRectPath, mRectPaint);
        canvas.drawPath(mRectPath, mRectStrokePaint);

        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mTextColor);
        mTextPaint.getTextBounds(mText, 0, mText.length(), mTextBound);
        if (Utils.isNullOrEmpty(mTextFont)) {
            mTextPaint.setTypeface(Typeface.defaultFromStyle(mTextStyle));
        } else {
            mTextPaint.setTypeface(XUI.getDefaultTypeface(mTextFont));
        }

        float begin_w_offset = (1.4142135f * actualDistance) / 2 - mTextBound.width() / 2F;
        if (begin_w_offset < 0) {
            begin_w_offset = 0;
        }

        canvas.drawTextOnPath(mText, mTextPath, begin_w_offset, mTextBound.height() / 2F, mTextPaint);
    }

    /**
     * 计算偏移量
     *
     * @param measuredWidth
     * @param measuredHeight
     */
    private void calcOffset(int measuredWidth, int measuredHeight) {
        float startPosX = measuredWidth - mDistance - mHeight;
        float endPosX = measuredWidth;
        float startPosY = measuredHeight - mDistance - mHeight;
        float endPosY = measuredHeight;

        float middle = mHeight / 2F;

        switch (mOrientation) {
            case LEFT_TOP: // LEFT_TOP

                mRectPath.reset();
                mRectPath.moveTo(0, mDistance);
                mRectPath.lineTo(mDistance, 0);
                mRectPath.lineTo(mDistance + mHeight, 0);
                mRectPath.lineTo(0, mDistance + mHeight);
                mRectPath.close();

                mTextPath.reset();
                mTextPath.moveTo(0, mDistance + middle);
                mTextPath.lineTo(mDistance + middle, 0);
                mTextPath.close();

                break;
            case RIGHT_TOP: // RIGHT_TOP

                mRectPath.reset();
                mRectPath.moveTo(startPosX, 0);
                mRectPath.lineTo(startPosX + mHeight, 0);
                mRectPath.lineTo(endPosX, mDistance);
                mRectPath.lineTo(endPosX, mDistance + mHeight);
                mRectPath.close();

                mTextPath.reset();
                mTextPath.moveTo(startPosX + middle, 0);
                mTextPath.lineTo(endPosX, mDistance + middle);
                mTextPath.close();

                break;
            case LEFT_BOTTOM: // LEFT_BOTTOM

                mRectPath.reset();
                mRectPath.moveTo(0, startPosY);
                mRectPath.lineTo(mDistance + mHeight, endPosY);
                mRectPath.lineTo(mDistance, endPosY);
                mRectPath.lineTo(0, startPosY + mHeight);
                mRectPath.close();

                mTextPath.reset();
                mTextPath.moveTo(0, startPosY + middle);
                mTextPath.lineTo(mDistance + middle, endPosY);
                mTextPath.close();

                break;
            case RIGHT_BOTTOM: // RIGHT_BOTTOM

                mRectPath.reset();
                mRectPath.moveTo(startPosX, endPosY);
                mRectPath.lineTo(measuredWidth, startPosY);
                mRectPath.lineTo(measuredWidth, startPosY + mHeight);
                mRectPath.lineTo(startPosX + mHeight, endPosY);
                mRectPath.close();

                mTextPath.reset();
                mTextPath.moveTo(startPosX + middle, endPosY);
                mTextPath.lineTo(endPosX, startPosY + middle);
                mTextPath.close();

                break;
            default:
                break;
        }
    }

    private int dip2Px(float dip) {
        return (int) (dip * mContext.getResources().getDisplayMetrics().density + 0.5f);
    }

    private int px2Dip(float px) {
        return (int) (px / mContext.getResources().getDisplayMetrics().density + 0.5f);
    }

    public void setLabelHeight(View view, int height) {
        if (mHeight != dip2Px(height)) {
            mHeight = dip2Px(height);
            view.invalidate();
        }
    }

    public int getLabelHeight() {
        return px2Dip(mHeight);
    }

    public void setLabelDistance(View view, int distance) {
        if (mDistance != dip2Px(distance)) {
            mDistance = dip2Px(distance);
            view.invalidate();
        }
    }

    public int getLabelStrokeWidth() {
        return px2Dip(mStrokeWidth);
    }

    public void setLabelStrokeWidth(View view, int strokeWidth) {
        if (mStrokeWidth != dip2Px(strokeWidth)) {
            mStrokeWidth = dip2Px(strokeWidth);
            view.invalidate();
        }
    }

    public int getLabelDistance() {
        return px2Dip(mDistance);
    }

    public boolean isLabelVisual() {
        return mVisual;
    }

    public void setLabelVisual(View view, boolean visual) {
        if (mVisual != visual) {
            mVisual = visual;
            view.invalidate();
        }
    }

    public int getLabelOrientation() {
        return mOrientation;
    }

    public void setLabelOrientation(View view, int orientation) {
        if (mOrientation != orientation && orientation <= RIGHT_BOTTOM && orientation >= LEFT_TOP) {
            mOrientation = orientation;
            view.invalidate();
        }
    }

    public int getLabelTextColor() {
        return mTextColor;
    }

    public void setLabelTextColor(View view, int textColor) {
        if (mTextColor != textColor) {
            mTextColor = textColor;
            view.invalidate();
        }
    }

    public int getLabelBackgroundColor() {
        return mBackgroundColor;
    }

    public void setLabelBackgroundColor(View view, int backgroundColor) {
        if (mBackgroundColor != backgroundColor) {
            mBackgroundColor = backgroundColor;
            view.invalidate();
        }
    }

    public int getLabelStrokeColor() {
        return mStrokeColor;
    }

    public void setLabelStrokeColor(View view, int strokeColor) {
        if (mStrokeColor != strokeColor) {
            mStrokeColor = strokeColor;
            view.invalidate();
        }
    }


    public void setLabelBackgroundAlpha(View view, int alpha) {
        if (mAlpha != alpha) {
            mAlpha = alpha;
            view.invalidate();
        }
    }

    public String getLabelText() {
        return mText;
    }

    public void setLabelText(View view, String text) {
        if (mText == null || !mText.equals(text)) {
            mText = text;
            view.invalidate();
        }
    }

    public int getLabelTextSize() {
        return px2Dip(mTextSize);
    }

    public void setLabelTextSize(View view, int textSize) {
        if (mTextSize != textSize) {
            mTextSize = textSize;
            view.invalidate();
        }
    }

    public int getLabelTextStyle() {
        return mTextStyle;
    }

    public void setLabelTextStyle(View view, int textStyle) {
        if (mTextStyle == textStyle) {
            return;
        }
        mTextStyle = textStyle;
        view.invalidate();
    }

    public void setLabelTextFont(View view, String textFont) {
        mTextFont = textFont;
        view.invalidate();
    }

    public String getLabelTextFont() {
        return mTextFont;
    }
}