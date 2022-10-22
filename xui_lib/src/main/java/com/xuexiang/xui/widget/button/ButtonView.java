package com.xuexiang.xui.widget.button;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;

import com.xuexiang.xui.R;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.SpanUtils;

/**
 * ButtonView
 * 自定义按钮控件
 *
 * @author xx
 */
public class ButtonView extends AppCompatTextView {

    private GradientDrawable gradientDrawable;
    private int mNormalSolidColor, mSelectedSolidColor;

    public ButtonView(Context context) {
        super(context);

    }

    public ButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setAttributeSet(context, attrs);
    }

    public ButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAttributeSet(context, attrs);
    }

    private void setAttributeSet(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.ButtonView);
        mNormalSolidColor = typedArray.getColor(
                R.styleable.ButtonView_textSolidColor, Color.TRANSPARENT);
        mSelectedSolidColor = typedArray.getColor(
                R.styleable.ButtonView_textSelectedSolidColor,
                Color.TRANSPARENT);
        int strokeColor = typedArray.getColor(
                R.styleable.ButtonView_textStrokeColor, Color.TRANSPARENT);
        int radius = typedArray.getDimensionPixelSize(
                R.styleable.ButtonView_textRadius, 0);
        int leftTopRadius = typedArray.getDimensionPixelSize(
                R.styleable.ButtonView_textLeftTopRadius, 0);
        int leftBottomRadius = typedArray.getDimensionPixelSize(
                R.styleable.ButtonView_textLeftBottomRadius, 0);
        int rightTopRadius = typedArray.getDimensionPixelSize(
                R.styleable.ButtonView_textRightTopRadius, 0);
        int rightBottomRadius = typedArray.getDimensionPixelSize(
                R.styleable.ButtonView_textRightBottomRadius, 0);
        int strokeWidth = typedArray.getDimensionPixelSize(
                R.styleable.ButtonView_textStrokeWidth, 0);
        Drawable textDrawable = ResUtils.getDrawableAttrRes(context, typedArray, R.styleable.ButtonView_textDrawable);
        int normalTextColor = typedArray.getColor(
                R.styleable.ButtonView_textNormalTextColor,
                Color.TRANSPARENT);
        int selectedTextColor = typedArray.getColor(
                R.styleable.ButtonView_textSelectedTextColor,
                Color.TRANSPARENT);

        typedArray.recycle();

        gradientDrawable = new GradientDrawable();
        gradientDrawable.setStroke(strokeWidth, strokeColor);
        gradientDrawable.setColor(mNormalSolidColor);

        if (radius > 0) {
            gradientDrawable.setCornerRadius(radius);
        } else if (leftTopRadius > 0 || leftBottomRadius > 0
                || rightTopRadius > 0 || rightBottomRadius > 0) {
            gradientDrawable.setCornerRadii(new float[]{leftTopRadius,
                    leftTopRadius, rightTopRadius, rightTopRadius,
                    rightBottomRadius, rightBottomRadius, leftBottomRadius,
                    leftBottomRadius});
        }

        setBackgroundDrawable(gradientDrawable);

        if (textDrawable != null) {
            setTextDrawable(textDrawable, 20);
        }

        if (normalTextColor != 0 && selectedTextColor != 0) {
            // 设置state_selected状态时，和正常状态时文字的颜色
            int[][] states = new int[3][1];
            states[0] = new int[]{android.R.attr.state_selected};
            states[1] = new int[]{android.R.attr.state_pressed};
            states[2] = new int[]{};
            ColorStateList textColorSelect = new ColorStateList(states,
                    new int[]{selectedTextColor, selectedTextColor,
                            normalTextColor});
            setTextColor(textColorSelect);
        }

        setClickable(selectedTextColor != 0 || mSelectedSolidColor != 0);
    }

    /**
     * 设置填充图片
     *
     * @param drawableId drawable id
     */
    public ButtonView setTextDrawable(int drawableId, int space) {
        if (drawableId != 0) {
            Drawable textDrawable = getResources().getDrawable(drawableId);
            setTextDrawable(textDrawable, space);
        }
        return this;
    }

    /**
     * 设置填充图片
     *
     * @param textDrawable drawable
     * @param textDrawable space 空隙
     */
    public ButtonView setTextDrawable(@NonNull Drawable textDrawable, int space) {
        setText(new SpanUtils()
                .appendImage(textDrawable, SpanUtils.ALIGN_BASELINE)
                .appendSpace(space, Color.TRANSPARENT)
                .append(getText())
                .setBackgroundColor(Color.TRANSPARENT)
                .create());
        return this;
    }

    /**
     * 设置填充颜色
     *
     * @param colorId 颜色id
     */
    public ButtonView setSolidColor(int colorId) {
        gradientDrawable.setColor(colorId);
        setBackgroundDrawable(gradientDrawable);
        return this;
    }

    /**
     * 设置圆角弧度
     *
     * @param leftTopRadius     左上角弧度
     * @param leftBottomRadius  左下角弧度
     * @param rightTopRadius    右上角弧度
     * @param rightBottomRadius 右下角弧度
     */
    public ButtonView setRadius(int leftTopRadius, int leftBottomRadius,
                                int rightTopRadius, int rightBottomRadius) {
        gradientDrawable.setCornerRadii(new float[]{leftTopRadius,
                leftTopRadius, rightTopRadius, rightTopRadius,
                rightBottomRadius, rightBottomRadius, leftBottomRadius,
                leftBottomRadius});
        setBackgroundDrawable(gradientDrawable);
        return this;
    }

    /**
     * 设置边框颜色及宽度
     *
     * @param strokeWidth 边框宽度
     * @param colorId     边框颜色 id
     */
    public ButtonView setStrokeColorAndWidth(int strokeWidth, int colorId) {
        gradientDrawable.setStroke(strokeWidth, colorId);
        return this;
    }

    /**
     * 设置textView选中状态颜色
     *
     * @param normalTextColor   正常状态颜色
     * @param selectedTextColor 按下状态颜色
     */
    public ButtonView setSelectedTextColor(int normalTextColor, int selectedTextColor) {
        if (normalTextColor != 0 && selectedTextColor != 0) {
            // 设置state_selected状态时，和正常状态时文字的颜色
            setClickable(true);
            int[][] states = new int[3][1];
            states[0] = new int[]{android.R.attr.state_selected};
            states[1] = new int[]{android.R.attr.state_pressed};
            states[2] = new int[]{};
            ColorStateList textColorSelect = new ColorStateList(states,
                    new int[]{selectedTextColor, selectedTextColor,
                            normalTextColor});
            setTextColor(textColorSelect);
        } else {
            setClickable(false);
        }
        return this;
    }

    /**
     * 设置textView选中状态颜色
     *
     * @param selectedSolidColor 按下状态颜色
     */
    public ButtonView setSelectedSolidColor(int selectedSolidColor) {
        if (selectedSolidColor != 0) {
            setClickable(true);
            mSelectedSolidColor = selectedSolidColor;
        } else {
            setClickable(false);
        }
        return this;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (mSelectedSolidColor != Color.TRANSPARENT && isEnabled()) {
                gradientDrawable.setColor(mSelectedSolidColor);
                setBackgroundDrawable(gradientDrawable);
                postInvalidate();
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP
                || event.getAction() == MotionEvent.ACTION_CANCEL) {
            if (mSelectedSolidColor != Color.TRANSPARENT) {
                gradientDrawable.setColor(mNormalSolidColor);
                setBackgroundDrawable(gradientDrawable);
            }
        }
        return super.onTouchEvent(event);
    }

}
