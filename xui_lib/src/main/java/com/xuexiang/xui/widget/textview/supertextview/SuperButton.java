package com.xuexiang.xui.widget.textview.supertextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;

import android.support.v7.widget.AppCompatButton;

import com.xuexiang.xui.R;
import com.xuexiang.xui.widget.alpha.IAlphaViewHelper;
import com.xuexiang.xui.widget.alpha.XUIAlphaViewHelper;

/**
 * 超级按钮  实现shape所有的属性
 *
 * @author xuexiang
 * @since 2019/1/14 下午10:10
 */
public class SuperButton extends AppCompatButton {

    private Context mContext;

    private int defaultColor = 0x20000000;
    private int defaultSelectorColor = 0x20000000;

    private int solidColor;
    private int selectorPressedColor;
    private int selectorDisableColor;
    private int selectorNormalColor;

    private float cornersRadius;
    private float cornersTopLeftRadius;
    private float cornersTopRightRadius;
    private float cornersBottomLeftRadius;
    private float cornersBottomRightRadius;

    private int strokeWidth;
    private int strokeColor;

    private float strokeDashWidth;
    private float strokeDashGap;

    private int sizeWidth;
    private int sizeHeight;

    private int gradientOrientation;

    private int gradientAngle;
    private int gradientCenterX;
    private int gradientCenterY;
    private int gradientGradientRadius;

    private int gradientStartColor;
    private int gradientCenterColor;
    private int gradientEndColor;

    private int gradientType;

    //"linear"	线形渐变。这也是默认的模式
    private static final int linear = 0;
    //"radial"	辐射渐变。startColor即辐射中心的颜色
    private static final int radial = 1;
    //"sweep"	扫描线渐变。
    private static final int sweep = 2;

    private boolean gradientUseLevel;

    private boolean useSelector;


    //shape的样式
    public static final int RECTANGLE = 0;
    public static final int OVAL = 1;
    public static final int LINE = 2;
    public static final int RING = 3;


    //渐变色的显示方式
    public static final int TOP_BOTTOM = 0;
    public static final int TR_BL = 1;
    public static final int RIGHT_LEFT = 2;
    public static final int BR_TL = 3;
    public static final int BOTTOM_TOP = 4;
    public static final int BL_TR = 5;
    public static final int LEFT_RIGHT = 6;
    public static final int TL_BR = 7;

    //文字显示的位置方式
    public static final int TEXT_GRAVITY_CENTER = 0;
    public static final int TEXT_GRAVITY_LEFT = 1;
    public static final int TEXT_GRAVITY_RIGHT = 2;
    public static final int TEXT_GRAVITY_TOP = 3;
    public static final int TEXT_GRAVITY_BOTTOM = 4;


    private int shapeType;

    private int gravity;

    private GradientDrawable gradientDrawable;

    public SuperButton(Context context) {
        super(context);
        initAttrs(context, null);
    }

    public SuperButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    public SuperButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        mContext = context;
        getAttr(attrs);
        init();
    }

    private void getAttr(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.SuperButton);

        gravity = typedArray.getInt(R.styleable.SuperButton_sGravity, 0);

        shapeType = typedArray.getInt(R.styleable.SuperButton_sShapeType, GradientDrawable.RECTANGLE);

        solidColor = typedArray.getColor(R.styleable.SuperButton_sSolidColor, defaultColor);

        selectorPressedColor = typedArray.getColor(R.styleable.SuperButton_sSelectorPressedColor, defaultSelectorColor);
        selectorDisableColor = typedArray.getColor(R.styleable.SuperButton_sSelectorDisableColor, defaultSelectorColor);
        selectorNormalColor = typedArray.getColor(R.styleable.SuperButton_sSelectorNormalColor, defaultSelectorColor);

        cornersRadius = typedArray.getDimensionPixelSize(R.styleable.SuperButton_sCornersRadius, 0);
        cornersTopLeftRadius = typedArray.getDimensionPixelSize(R.styleable.SuperButton_sCornersTopLeftRadius, 0);
        cornersTopRightRadius = typedArray.getDimensionPixelSize(R.styleable.SuperButton_sCornersTopRightRadius, 0);
        cornersBottomLeftRadius = typedArray.getDimensionPixelSize(R.styleable.SuperButton_sCornersBottomLeftRadius, 0);
        cornersBottomRightRadius = typedArray.getDimensionPixelSize(R.styleable.SuperButton_sCornersBottomRightRadius, 0);

        strokeWidth = typedArray.getDimensionPixelSize(R.styleable.SuperButton_sStrokeWidth, 0);
        strokeDashWidth = typedArray.getDimensionPixelSize(R.styleable.SuperButton_sStrokeDashWidth, 0);
        strokeDashGap = typedArray.getDimensionPixelSize(R.styleable.SuperButton_sStrokeDashGap, 0);

        strokeColor = typedArray.getColor(R.styleable.SuperButton_sStrokeColor, defaultColor);

        sizeWidth = typedArray.getDimensionPixelSize(R.styleable.SuperButton_sSizeWidth, 0);
        sizeHeight = typedArray.getDimensionPixelSize(R.styleable.SuperButton_sSizeHeight, dip2px(mContext, 48));

        gradientOrientation = typedArray.getInt(R.styleable.SuperButton_sGradientOrientation, -1);

        gradientAngle = typedArray.getDimensionPixelSize(R.styleable.SuperButton_sGradientAngle, 0);
        gradientCenterX = typedArray.getDimensionPixelSize(R.styleable.SuperButton_sGradientCenterX, 0);
        gradientCenterY = typedArray.getDimensionPixelSize(R.styleable.SuperButton_sGradientCenterY, 0);
        gradientGradientRadius = typedArray.getDimensionPixelSize(R.styleable.SuperButton_sGradientGradientRadius, 0);

        gradientStartColor = typedArray.getColor(R.styleable.SuperButton_sGradientStartColor, -1);
        gradientCenterColor = typedArray.getColor(R.styleable.SuperButton_sGradientCenterColor, -1);
        gradientEndColor = typedArray.getColor(R.styleable.SuperButton_sGradientEndColor, -1);

        gradientType = typedArray.getInt(R.styleable.SuperButton_sGradientType, linear);
        gradientUseLevel = typedArray.getBoolean(R.styleable.SuperButton_sGradientUseLevel, false);

        useSelector = typedArray.getBoolean(R.styleable.SuperButton_sUseSelector, false);

        typedArray.recycle();
    }

    private void init() {
        setClickable(true);

        if (Build.VERSION.SDK_INT < 16) {
            setBackgroundDrawable(useSelector ? getSelector() : getDrawable(0));
        } else {
            setBackground(useSelector ? getSelector() : getDrawable(0));
        }

        setSGravity();
    }


    /**
     * 获取设置之后的Selector
     *
     * @return stateListDrawable
     */
    public StateListDrawable getSelector() {

        StateListDrawable stateListDrawable = new StateListDrawable();

        //注意该处的顺序，只要有一个状态与之相配，背景就会被换掉
        //所以不要把大范围放在前面了，如果sd.addState(new[]{},normal)放在第一个的话，就没有什么效果了
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, getDrawable(android.R.attr.state_pressed));
        stateListDrawable.addState(new int[]{-android.R.attr.state_enabled}, getDrawable(-android.R.attr.state_enabled));
        stateListDrawable.addState(new int[]{}, getDrawable(android.R.attr.state_enabled));

        return stateListDrawable;
    }

    /**
     * 设置GradientDrawable
     *
     * @param state 按钮状态
     * @return gradientDrawable
     */
    public GradientDrawable getDrawable(int state) {
        gradientDrawable = new GradientDrawable();

        setShape();
        setOrientation();
        setSize();
        setBorder();
        setRadius();
        setSelectorColor(state);

        return gradientDrawable;
    }

    /**
     * 设置文字对其方式
     */
    private void setSGravity() {
        switch (gravity) {
            case 0:
                setGravity(Gravity.CENTER);
                break;
            case 1:
                setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                break;
            case 2:
                setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
                break;
            case 3:
                setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                break;
            case 4:
                setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
                break;
        }
    }

    /**
     * 设置Selector的不同状态的颜色
     *
     * @param state 按钮状态
     */
    private void setSelectorColor(int state) {
        if (gradientOrientation == -1) {
            switch (state) {
                case android.R.attr.state_pressed:
                    gradientDrawable.setColor(selectorPressedColor);
                    break;
                case -android.R.attr.state_enabled:
                    gradientDrawable.setColor(selectorDisableColor);
                    break;
                case android.R.attr.state_enabled:
                    gradientDrawable.setColor(selectorNormalColor);
                    break;
            }
        }

    }

    /**
     * 设置背景颜色
     * 如果设定的有Orientation 就默认为是渐变色的Button，否则就是纯色的Button
     */
    private void setOrientation() {
        if (isUseGradientColor()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                gradientDrawable.setOrientation(getOrientation(gradientOrientation));

                if (gradientCenterColor == -1) {
                    gradientDrawable.setColors(new int[]{gradientStartColor, gradientEndColor});
                } else {
                    gradientDrawable.setColors(new int[]{gradientStartColor, gradientCenterColor, gradientEndColor});
                }

                switch (gradientType) {
                    case linear:
                        gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
                        break;
                    case radial:
                        gradientDrawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);
                        gradientDrawable.setGradientRadius(gradientGradientRadius);

                        break;
                    case sweep:
                        gradientDrawable.setGradientType(GradientDrawable.SWEEP_GRADIENT);
                        break;
                }


                gradientDrawable.setUseLevel(gradientUseLevel);

                if (gradientCenterX != 0 && gradientCenterY != 0) {
                    gradientDrawable.setGradientCenter(gradientCenterX, gradientCenterY);
                }

            }
        } else {
            gradientDrawable.setColor(solidColor);
        }
    }


    /**
     * 设置颜色渐变类型
     *
     * @param gradientOrientation gradientOrientation
     * @return Orientation
     */
    private GradientDrawable.Orientation getOrientation(int gradientOrientation) {
        GradientDrawable.Orientation orientation = null;
        switch (gradientOrientation) {
            case TOP_BOTTOM:
                orientation = GradientDrawable.Orientation.TOP_BOTTOM;
                break;
            case TR_BL:
                orientation = GradientDrawable.Orientation.TR_BL;
                break;
            case RIGHT_LEFT:
                orientation = GradientDrawable.Orientation.RIGHT_LEFT;
                break;
            case BR_TL:
                orientation = GradientDrawable.Orientation.BR_TL;
                break;
            case BOTTOM_TOP:
                orientation = GradientDrawable.Orientation.BOTTOM_TOP;
                break;
            case BL_TR:
                orientation = GradientDrawable.Orientation.BL_TR;
                break;
            case LEFT_RIGHT:
                orientation = GradientDrawable.Orientation.LEFT_RIGHT;
                break;
            case TL_BR:
                orientation = GradientDrawable.Orientation.TL_BR;
                break;
        }
        return orientation;
    }

    /**
     * 设置shape类型
     */
    private void setShape() {

        switch (shapeType) {
            case RECTANGLE:
                gradientDrawable.setShape(GradientDrawable.RECTANGLE);
                break;
            case OVAL:
                gradientDrawable.setShape(GradientDrawable.OVAL);
                break;
            case LINE:
                gradientDrawable.setShape(GradientDrawable.LINE);
                break;
            case RING:
                gradientDrawable.setShape(GradientDrawable.RING);
                break;
        }
    }


    private void setSize() {
        if (shapeType == RECTANGLE) {
            gradientDrawable.setSize(sizeWidth, sizeHeight);
        }
    }

    /**
     * 设置边框  宽度  颜色  虚线  间隙
     */
    private void setBorder() {
        gradientDrawable.setStroke(strokeWidth, strokeColor, strokeDashWidth, strokeDashGap);
    }

    /**
     * 只有类型是矩形的时候设置圆角半径才有效
     */
    private void setRadius() {
        if (shapeType == GradientDrawable.RECTANGLE) {
            if (cornersRadius != 0) {
                gradientDrawable.setCornerRadius(cornersRadius);//设置圆角的半径
            } else {
                //1、2两个参数表示左上角，3、4表示右上角，5、6表示右下角，7、8表示左下角
                gradientDrawable.setCornerRadii(
                        new float[]
                                {
                                        cornersTopLeftRadius, cornersTopLeftRadius,
                                        cornersTopRightRadius, cornersTopRightRadius,
                                        cornersBottomRightRadius, cornersBottomRightRadius,
                                        cornersBottomLeftRadius, cornersBottomLeftRadius
                                }
                );
            }
        }
    }


    /////////////////对外暴露的方法//////////////

    /**
     * 设置Shape类型
     *
     * @param type 类型
     * @return 对象
     */
    public SuperButton setShapeType(int type) {
        this.shapeType = type;
        return this;
    }

    /**
     * 设置文字对其方式
     *
     * @param gravity 对齐方式
     * @return 对象
     */
    public SuperButton setTextGravity(int gravity) {
        this.gravity = gravity;
        return this;
    }

    /**
     * 设置按下的颜色
     *
     * @param color 颜色
     * @return 对象
     */
    public SuperButton setShapeSelectorPressedColor(int color) {
        this.selectorPressedColor = color;
        return this;
    }

    /**
     * 设置正常的颜色
     *
     * @param color 颜色
     * @return 对象
     */
    public SuperButton setShapeSelectorNormalColor(int color) {
        this.selectorNormalColor = color;
        return this;
    }

    /**
     * 设置不可点击的颜色
     *
     * @param color 颜色
     * @return 对象
     */
    public SuperButton setShapeSelectorDisableColor(int color) {
        this.selectorDisableColor = color;
        return this;
    }

    /**
     * 设置填充的颜色
     *
     * @param color 颜色
     * @return 对象
     */
    public SuperButton setShapeSolidColor(int color) {
        this.solidColor = color;
        return this;
    }

    /**
     * 设置边框宽度
     *
     * @param strokeWidth 边框宽度值
     * @return 对象
     */
    public SuperButton setShapeStrokeWidth(int strokeWidth) {
        this.strokeWidth = dip2px(mContext, strokeWidth);
        return this;
    }

    /**
     * 设置边框颜色
     *
     * @param strokeColor 边框颜色
     * @return 对象
     */
    public SuperButton setShapeStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
        return this;
    }

    /**
     * 设置边框虚线宽度
     *
     * @param strokeDashWidth 边框虚线宽度
     * @return 对象
     */
    public SuperButton setShapeStrokeDashWidth(float strokeDashWidth) {
        this.strokeDashWidth = dip2px(mContext, strokeDashWidth);
        return this;
    }

    /**
     * 设置边框虚线间隙
     *
     * @param strokeDashGap 边框虚线间隙值
     * @return 对象
     */
    public SuperButton setShapeStrokeDashGap(float strokeDashGap) {
        this.strokeDashGap = dip2px(mContext, strokeDashGap);
        return this;
    }

    /**
     * 设置圆角半径
     *
     * @param radius 半径
     * @return 对象
     */
    public SuperButton setShapeCornersRadius(float radius) {
        this.cornersRadius = dip2px(mContext, radius);
        return this;
    }

    /**
     * 设置左上圆角半径
     *
     * @param radius 半径
     * @return 对象
     */
    public SuperButton setShapeCornersTopLeftRadius(float radius) {
        this.cornersTopLeftRadius = dip2px(mContext, radius);
        return this;
    }

    /**
     * 设置右上圆角半径
     *
     * @param radius 半径
     * @return 对象
     */
    public SuperButton setShapeCornersTopRightRadius(float radius) {
        this.cornersTopRightRadius = dip2px(mContext, radius);
        return this;
    }

    /**
     * 设置左下圆角半径
     *
     * @param radius 半径
     * @return 对象
     */
    public SuperButton setShapeCornersBottomLeftRadius(float radius) {
        this.cornersBottomLeftRadius = dip2px(mContext, radius);
        return this;
    }

    /**
     * 设置右下圆角半径
     *
     * @param radius 半径
     * @return 对象
     */
    public SuperButton setShapeCornersBottomRightRadius(float radius) {
        this.cornersBottomRightRadius = dip2px(mContext, radius);
        return this;
    }

    /**
     * 设置shape的宽度
     *
     * @param sizeWidth 宽
     * @return 对象
     */
    public SuperButton setShapeSizeWidth(int sizeWidth) {
        this.sizeWidth = sizeWidth;
        return this;
    }

    /**
     * 设置shape的高度
     *
     * @param sizeHeight 高
     * @return 对象
     */
    public SuperButton setShapeSizeHeight(int sizeHeight) {
        this.sizeHeight = sizeHeight;
        return this;
    }

    /**
     * 设置背景渐变方式
     *
     * @param gradientOrientation 渐变类型
     * @return 对象
     */
    public SuperButton setShapeGradientOrientation(int gradientOrientation) {
        this.gradientOrientation = gradientOrientation;
        return this;
    }

    /**
     * 设置渐变中心X
     *
     * @param gradientCenterX 中心x
     * @return 对象
     */
    public SuperButton setShapeGradientCenterX(int gradientCenterX) {
        this.gradientCenterX = gradientCenterX;
        return this;
    }

    /**
     * 设置渐变中心Y
     *
     * @param gradientCenterY 中心y
     * @return 对象
     */
    public SuperButton setShapeGradientCenterY(int gradientCenterY) {
        this.gradientCenterY = gradientCenterY;
        return this;
    }

    /**
     * 设置渐变半径
     *
     * @param gradientGradientRadius 渐变半径
     * @return 对象
     */
    public SuperButton setShapeGradientGradientRadius(int gradientGradientRadius) {
        this.gradientGradientRadius = gradientGradientRadius;
        return this;
    }

    /**
     * 设置渐变开始的颜色
     *
     * @param gradientStartColor 开始颜色
     * @return 对象
     */
    public SuperButton setShapeGradientStartColor(int gradientStartColor) {
        this.gradientStartColor = gradientStartColor;
        return this;
    }

    /**
     * 设置渐变中间的颜色
     *
     * @param gradientCenterColor 中间颜色
     * @return 对象
     */
    public SuperButton setShapeGradientCenterColor(int gradientCenterColor) {
        this.gradientCenterColor = gradientCenterColor;
        return this;
    }

    /**
     * 设置渐变结束的颜色
     *
     * @param gradientEndColor 结束颜色
     * @return 对象
     */
    public SuperButton setShapeGradientEndColor(int gradientEndColor) {
        this.gradientEndColor = gradientEndColor;
        return this;
    }

    /**
     * 设置渐变类型
     *
     * @param gradientType 类型
     * @return 对象
     */
    public SuperButton setShapeGradientType(int gradientType) {
        this.gradientType = gradientType;
        return this;
    }

    /**
     * 设置是否使用UseLevel
     *
     * @param gradientUseLevel true  or  false
     * @return 对象
     */
    public SuperButton setShapeGradientUseLevel(boolean gradientUseLevel) {
        this.gradientUseLevel = gradientUseLevel;
        return this;
    }

    /**
     * 是否使用selector
     *
     * @param useSelector true  or  false
     * @return 对象
     */
    public SuperButton setShapeUseSelector(boolean useSelector) {
        this.useSelector = useSelector;
        return this;
    }

    /**
     * 使用shape
     * 所有与shape相关的属性设置之后调用此方法才生效
     */
    public void setUseShape() {
        init();
    }


    /**
     * 单位转换工具类
     *
     * @param context  上下文对象
     * @param dipValue 值
     * @return 返回值
     */
    private int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    private IAlphaViewHelper mAlphaViewHelper;
    private IAlphaViewHelper getAlphaViewHelper() {
        if (mAlphaViewHelper == null) {
            mAlphaViewHelper = new XUIAlphaViewHelper(this);
        }
        return mAlphaViewHelper;
    }

    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);
        if (isUseGradientColor()) {
            getAlphaViewHelper().onPressedChanged(this, pressed);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (isUseGradientColor()) {
            getAlphaViewHelper().onEnabledChanged(this, enabled);
        }
    }

    /**
     * 是否使用渐变色
     * @return
     */
    private boolean isUseGradientColor() {
        return gradientOrientation != -1;
    }

    /**
     * 设置是否要在 press 时改变透明度
     *
     * @param changeAlphaWhenPress 是否要在 press 时改变透明度
     */
    public void setChangeAlphaWhenPress(boolean changeAlphaWhenPress) {
        if (isUseGradientColor()) {
            getAlphaViewHelper().setChangeAlphaWhenPress(changeAlphaWhenPress);
        }
    }

    /**
     * 设置是否要在 disabled 时改变透明度
     *
     * @param changeAlphaWhenDisable 是否要在 disabled 时改变透明度
     */
    public void setChangeAlphaWhenDisable(boolean changeAlphaWhenDisable) {
        if (isUseGradientColor()) {
            getAlphaViewHelper().setChangeAlphaWhenDisable(changeAlphaWhenDisable);
        }
    }

}
