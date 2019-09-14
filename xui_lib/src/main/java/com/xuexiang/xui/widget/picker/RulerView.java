package com.xuexiang.xui.widget.picker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import android.support.annotation.Nullable;

import com.xuexiang.xui.R;
import com.xuexiang.xui.utils.ResUtils;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;

import uk.co.chrisjenx.calligraphy.HasTypeface;

/**
 * 刻度尺控件
 *
 * @author XUE
 * @since 2019/4/2 16:33
 */
public class RulerView extends View implements HasTypeface {
    /**
     * 2个大刻度之间间距，默认为1
     */
    private int scaleLimit = 1;
    /**
     * 尺子高度
     */
    private int rulerHeight = 50;
    /**
     * 尺子和屏幕顶部以及结果之间的高度
     */
    private int rulerToResultGap = rulerHeight / 4;
    /**
     * 刻度平分多少份
     */
    private int scaleCount = 10;
    /**
     * 刻度间距
     */
    private int scaleGap = 10;
    /**
     * 刻度最小值
     */
    private int minScale = 0;
    /**
     * 第一次显示的刻度
     */
    private float firstScale = 50f;
    /**
     * 刻度最大值
     */
    private int maxScale = 100;

    /**
     * 背景颜色
     */
    private int bgColor;
    /**
     * 小刻度的颜色
     */
    private int smallScaleColor;
    /**
     * 中刻度的颜色
     */
    private int midScaleColor;
    /**
     * 大刻度的颜色
     */
    private int largeScaleColor;
    /**
     * 刻度颜色
     */
    private int scaleNumColor;
    /**
     * 结果值颜色
     */
    private int resultNumColor;
    /**
     * kg颜色
     */
    private String unit = "kg";
    /**
     * kg颜色
     */
    private int unitColor;
    /**
     * 小刻度粗细大小
     */
    private int smallScaleStroke = 2;
    /**
     * 中刻度粗细大小
     */
    private int midScaleStroke = 3;
    /**
     * 大刻度粗细大小
     */
    private int largeScaleStroke = 5;
    /**
     * 结果字体大小
     */
    private int resultNumTextSize = 20;
    /**
     * 刻度字体大小
     */
    private int scaleNumTextSize = 16;
    /**
     * 单位字体大小
     */
    private int unitTextSize = 13;
    /**
     * 是否显示刻度结果
     */
    private boolean showScaleResult = true;
    /**
     * 是否背景显示圆角
     */
    private boolean isBgRoundRect = true;
    /**
     * 圆角大小
     */
    private int roundRadius = 10;
    /**
     * 结果回调
     */
    private OnChooseResultListener onChooseResultListener;
    /**
     * 滑动选择刻度
     */
    private float computeScale = -1;
    /**
     * 当前刻度
     */
    public float currentScale = firstScale;

    private ValueAnimator valueAnimator;
    private VelocityTracker velocityTracker = VelocityTracker.obtain();
    private String resultText = String.valueOf(firstScale);
    private Paint bgPaint;
    private Paint smallScalePaint;
    private Paint midScalePaint;
    private Paint lagScalePaint;
    private TextPaint scaleNumPaint;
    private TextPaint resultNumPaint;
    private TextPaint unitPaint;
    private Rect scaleNumRect;
    private Rect resultNumRect;
    private Rect kgRect;
    private RectF bgRect;
    private int height, width;
    private int smallScaleHeight;
    private int midScaleHeight;
    private int lagScaleHeight;
    private int rulerRight = 0;
    private int resultNumRight;
    private float downX;
    private float moveX = 0;
    private float currentX;
    private float lastMoveX = 0;
    private boolean isUp = false;
    private int leftScroll;
    private int rightScroll;
    private int xVelocity;

    public RulerView(Context context) {
        this(context, null, R.attr.RulerViewStyle);
    }

    public RulerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.RulerViewStyle);
    }

    public RulerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化属性
     *
     * @param attrs
     * @param defStyleAttr
     */
    private void initAttrs(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.RulerView, defStyleAttr, 0);
        scaleLimit = a.getInt(R.styleable.RulerView_rv_scaleLimit, scaleLimit);
        rulerHeight = a.getDimensionPixelSize(R.styleable.RulerView_rv_rulerHeight, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rulerHeight, getResources().getDisplayMetrics()));
        rulerToResultGap = a.getDimensionPixelSize(R.styleable.RulerView_rv_rulerToResultGap, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rulerToResultGap, getResources().getDisplayMetrics()));
        scaleCount = a.getInt(R.styleable.RulerView_rv_scaleCount, scaleCount);
        scaleGap = a.getDimensionPixelSize(R.styleable.RulerView_rv_scaleGap, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, scaleGap, getResources().getDisplayMetrics()));
        minScale = a.getInt(R.styleable.RulerView_rv_minScale, minScale) / scaleLimit;
        firstScale = a.getFloat(R.styleable.RulerView_rv_firstScale, firstScale) / scaleLimit;
        maxScale = a.getInt(R.styleable.RulerView_rv_maxScale, maxScale) / scaleLimit;
        bgColor = a.getColor(R.styleable.RulerView_rv_bgColor, ResUtils.getColor(R.color.default_ruler_view_bg_color));
        smallScaleColor = a.getColor(R.styleable.RulerView_rv_smallScaleColor, ResUtils.getColor(R.color.default_ruler_view_small_scale_color));
        midScaleColor = a.getColor(R.styleable.RulerView_rv_midScaleColor, ResUtils.getColor(R.color.default_ruler_view_mid_scale_color));
        largeScaleColor = a.getColor(R.styleable.RulerView_rv_largeScaleColor, ResUtils.getColor(R.color.default_ruler_view_large_scale_color));
        scaleNumColor = a.getColor(R.styleable.RulerView_rv_scaleNumColor, ResUtils.getColor(R.color.default_ruler_view_scale_num_color));
        resultNumColor = a.getColor(R.styleable.RulerView_rv_resultNumColor, ResUtils.getColor(R.color.default_ruler_view_result_num_color));
        unitColor = a.getColor(R.styleable.RulerView_rv_unitColor, ResUtils.getColor(R.color.default_ruler_view_unit_color));
        String tempUnit = unit;
        unit = a.getString(R.styleable.RulerView_rv_unit);
        if (TextUtils.isEmpty(unit)) {
            unit = tempUnit;
        }
        smallScaleStroke = a.getDimensionPixelSize(R.styleable.RulerView_rv_smallScaleStroke, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, smallScaleStroke, getResources().getDisplayMetrics()));
        midScaleStroke = a.getDimensionPixelSize(R.styleable.RulerView_rv_midScaleStroke, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, midScaleStroke, getResources().getDisplayMetrics()));
        largeScaleStroke = a.getDimensionPixelSize(R.styleable.RulerView_rv_largeScaleStroke, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, largeScaleStroke, getResources().getDisplayMetrics()));
        resultNumTextSize = a.getDimensionPixelSize(R.styleable.RulerView_rv_resultNumTextSize, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, resultNumTextSize, getResources().getDisplayMetrics()));
        scaleNumTextSize = a.getDimensionPixelSize(R.styleable.RulerView_rv_scaleNumTextSize, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, scaleNumTextSize, getResources().getDisplayMetrics()));
        unitTextSize = a.getDimensionPixelSize(R.styleable.RulerView_rv_unitTextSize, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, unitTextSize, getResources().getDisplayMetrics()));
        showScaleResult = a.getBoolean(R.styleable.RulerView_rv_showScaleResult, showScaleResult);
        isBgRoundRect = a.getBoolean(R.styleable.RulerView_rv_isBgRoundRect, isBgRoundRect);
        roundRadius = a.getDimensionPixelSize(R.styleable.RulerView_rv_roundRadius, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, roundRadius, getResources().getDisplayMetrics()));
        a.recycle();
    }


    private void init() {
        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        smallScalePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        midScalePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        lagScalePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        scaleNumPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        resultNumPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        unitPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

        bgPaint.setColor(bgColor);
        smallScalePaint.setColor(smallScaleColor);
        midScalePaint.setColor(midScaleColor);
        lagScalePaint.setColor(largeScaleColor);
        scaleNumPaint.setColor(scaleNumColor);
        resultNumPaint.setColor(resultNumColor);
        unitPaint.setColor(unitColor);

        resultNumPaint.setStyle(Paint.Style.FILL);
        unitPaint.setStyle(Paint.Style.FILL);
        bgPaint.setStyle(Paint.Style.FILL);
        smallScalePaint.setStyle(Paint.Style.FILL);
        midScalePaint.setStyle(Paint.Style.FILL);
        lagScalePaint.setStyle(Paint.Style.FILL);

        lagScalePaint.setStrokeCap(Paint.Cap.ROUND);
        midScalePaint.setStrokeCap(Paint.Cap.ROUND);
        smallScalePaint.setStrokeCap(Paint.Cap.ROUND);

        smallScalePaint.setStrokeWidth(smallScaleStroke);
        midScalePaint.setStrokeWidth(midScaleStroke);
        lagScalePaint.setStrokeWidth(largeScaleStroke);

        resultNumPaint.setTextSize(resultNumTextSize);
        unitPaint.setTextSize(unitTextSize);
        scaleNumPaint.setTextSize(scaleNumTextSize);

        bgRect = new RectF();
        resultNumRect = new Rect();
        scaleNumRect = new Rect();
        kgRect = new Rect();

        resultNumPaint.getTextBounds(resultText, 0, resultText.length(), resultNumRect);
        unitPaint.getTextBounds(resultText, 0, 1, kgRect);

        smallScaleHeight = rulerHeight / 4;
        midScaleHeight = rulerHeight / 2;
        lagScaleHeight = rulerHeight / 2 + 5;
        valueAnimator = new ValueAnimator();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int heightModule = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        switch (heightModule) {
            case MeasureSpec.AT_MOST:
                height = rulerHeight + (showScaleResult ? resultNumRect.height() : 0) + rulerToResultGap * 2 + getPaddingTop() + getPaddingBottom();
                break;
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.EXACTLY:
                height = heightSize + getPaddingTop() + getPaddingBottom();
                break;
            default:
                break;
        }

        width = widthSize + getPaddingLeft() + getPaddingRight();

        setMeasuredDimension(width, height);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBg(canvas);
        drawScaleAndNum(canvas);
        drawResultText(canvas, resultText);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getParent() != null) {
            //拦截事件，不让父布局获取
            getParent().requestDisallowInterceptTouchEvent(true);
        }

        currentX = event.getX();
        isUp = false;
        velocityTracker.computeCurrentVelocity(500);
        velocityTracker.addMovement(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //按下时如果属性动画还没执行完,就终止,记录下当前按下点的位置
                if (valueAnimator != null && valueAnimator.isRunning()) {
                    valueAnimator.end();
                    valueAnimator.cancel();
                }
                downX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                //滑动时候,通过假设的滑动距离,做超出左边界以及右边界的限制。
                moveX = currentX - downX + lastMoveX;
                if (moveX >= width / 2) {
                    moveX = width / 2;
                } else if (moveX <= getWhichScaleMoveX(maxScale)) {
                    moveX = getWhichScaleMoveX(maxScale);
                }
                break;
            case MotionEvent.ACTION_UP:
                //手指抬起时候制造惯性滑动
                lastMoveX = moveX;
                xVelocity = (int) velocityTracker.getXVelocity();
                autoVelocityScroll(xVelocity);
                velocityTracker.clear();
                break;
            default:
                break;
        }
        invalidate();
        return true;
    }

    private void autoVelocityScroll(int xVelocity) {
        //惯性滑动的代码,速率和滑动距离,以及滑动时间需要控制的很好,应该网上已经有关于这方面的算法了吧。。这里是经过N次测试调节出来的惯性滑动
        if (Math.abs(xVelocity) < 50) {
            isUp = true;
            return;
        }
        if (valueAnimator.isRunning()) {
            return;
        }
        valueAnimator = ValueAnimator.ofInt(0, xVelocity / 20).setDuration(Math.abs(xVelocity / 10));
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                moveX += (int) animation.getAnimatedValue();
                if (moveX >= width / 2) {
                    moveX = width / 2;
                } else if (moveX <= getWhichScaleMoveX(maxScale)) {
                    moveX = getWhichScaleMoveX(maxScale);
                }
                lastMoveX = moveX;
                invalidate();
            }

        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isUp = true;
                invalidate();
            }
        });

        valueAnimator.start();
    }

    private float getWhichScaleMoveX(float scale) {
        return width / 2 - scaleGap * scaleCount * (scale - minScale);
    }

    private void drawScaleAndNum(Canvas canvas) {
        canvas.translate(0, (showScaleResult ? resultNumRect.height() : 0) + rulerToResultGap);//移动画布到结果值的下面

        int num1;//确定刻度位置
        float num2;

        if (firstScale != -1) {   //第一次进来的时候计算出默认刻度对应的假设滑动的距离moveX
            moveX = getWhichScaleMoveX(firstScale);          //如果设置了默认滑动位置，计算出需要滑动的距离
            lastMoveX = moveX;
            firstScale = -1;                                //将结果置为-1，下次不再计算初始位置
        }

        if (computeScale != -1) {//弹性滑动到目标刻度
            lastMoveX = moveX;
            if (valueAnimator != null && !valueAnimator.isRunning()) {
                valueAnimator = ValueAnimator.ofFloat(getWhichScaleMoveX(currentScale), getWhichScaleMoveX(computeScale));
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        moveX = (float) animation.getAnimatedValue();
                        lastMoveX = moveX;
                        invalidate();
                    }
                });
                valueAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        //这里是滑动结束时候回调给使用者的结果值
                        computeScale = -1;
                    }
                });
                valueAnimator.setDuration(Math.abs((long) ((getWhichScaleMoveX(computeScale) - getWhichScaleMoveX(currentScale)) / 100)));
                valueAnimator.start();
            }
        }

        num1 = -(int) (moveX / scaleGap);                   //滑动刻度的整数部分
        num2 = (moveX % scaleGap);                         //滑动刻度的小数部分

        canvas.save();                                      //保存当前画布

        rulerRight = 0;                                    //准备开始绘制当前屏幕,从最左面开始

        if (isUp) {   //这部分代码主要是计算手指抬起时，惯性滑动结束时，刻度需要停留的位置
            num2 = ((moveX - width / 2 % scaleGap) % scaleGap);     //计算滑动位置距离整点刻度的小数部分距离
            if (num2 <= 0) {
                num2 = scaleGap - Math.abs(num2);
            }
            leftScroll = (int) Math.abs(num2);                        //当前滑动位置距离左边整点刻度的距离
            rightScroll = (int) (scaleGap - Math.abs(num2));         //当前滑动位置距离右边整点刻度的距离

            float moveX2 = num2 <= scaleGap / 2 ? moveX - leftScroll : moveX + rightScroll; //最终计算出当前位置到整点刻度位置需要滑动的距离

            if (valueAnimator != null && !valueAnimator.isRunning()) {      //手指抬起，并且当前没有惯性滑动在进行，创建一个惯性滑动
                valueAnimator = ValueAnimator.ofFloat(moveX, moveX2);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        moveX = (float) animation.getAnimatedValue();            //不断滑动去更新新的位置
                        lastMoveX = moveX;
                        invalidate();
                    }
                });
                valueAnimator.addListener(new AnimatorListenerAdapter() {       //增加一个监听，用来返回给使用者滑动结束后的最终结果刻度值
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        //这里是滑动结束时候回调给使用者的结果值
                        if (onChooseResultListener != null) {
                            onChooseResultListener.onEndResult(resultText);
                        }
                    }
                });
                valueAnimator.setDuration(300);
                valueAnimator.start();
                isUp = false;
            }

            num1 = (int) -(moveX / scaleGap);                //重新计算当前滑动位置的整数以及小数位置
            num2 = (moveX % scaleGap);
        }


        canvas.translate(num2, 0);    //不加该偏移的话，滑动时刻度不会落在0~1之间只会落在整数上面,其实这个都能设置一种模式了，毕竟初衷就是指针不会落在小数上面

        //这里是滑动时候不断回调给使用者的结果值
        currentScale = new WeakReference<>(new BigDecimal(((width / 2 - moveX) / (scaleGap * scaleCount) + minScale) * scaleLimit)).get().setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();

        resultText = String.valueOf(currentScale);


        if (onChooseResultListener != null) {
            onChooseResultListener.onScrollResult(resultText); //接口不断回调给使用者结果值
        }
        //绘制当前屏幕可见刻度,不需要裁剪屏幕,while循环只会执行·屏幕宽度/刻度宽度·次,大部分的绘制都是if(curDis<width)这样子内存暂用相对来说会比较高。。
        while (rulerRight < width) {
            if (num1 % scaleCount == 0) {    //绘制整点刻度以及文字
                if ((moveX >= 0 && rulerRight < moveX - scaleGap) || width / 2 - rulerRight <= getWhichScaleMoveX(maxScale + 1) - moveX) {
                    //当滑动出范围的话，不绘制，去除左右边界
                } else {
                    //绘制刻度，绘制刻度数字
                    canvas.drawLine(0, 0, 0, midScaleHeight, midScalePaint);
                    scaleNumPaint.getTextBounds(num1 / scaleGap + minScale + "", 0, (num1 / scaleGap + minScale + "").length(), scaleNumRect);
                    canvas.drawText((num1 / scaleCount + minScale) * scaleLimit + "", -scaleNumRect.width() / 2, lagScaleHeight +
                            (rulerHeight - lagScaleHeight) / 2 + scaleNumRect.height(), scaleNumPaint);

                }

            } else {   //绘制小数刻度
                if ((moveX >= 0 && rulerRight < moveX) || width / 2 - rulerRight < getWhichScaleMoveX(maxScale) - moveX) {
                    //当滑动出范围的话，不绘制，去除左右边界
                } else {
                    //绘制小数刻度
                    canvas.drawLine(0, 0, 0, smallScaleHeight, smallScalePaint);
                }
            }
            ++num1;  //刻度加1
            rulerRight += scaleGap;  //绘制屏幕的距离在原有基础上+1个刻度间距
            canvas.translate(scaleGap, 0); //移动画布到下一个刻度
        }

        canvas.restore();
        //绘制屏幕中间用来选中刻度的最大刻度
        canvas.drawLine(width / 2, 0, width / 2, lagScaleHeight, lagScalePaint);

    }

    //绘制上面的结果 结果值+单位
    private void drawResultText(Canvas canvas, String resultText) {
        if (!showScaleResult) {   //判断用户是否设置需要显示当前刻度值，如果否则取消绘制
            return;
        }
        canvas.translate(0, -resultNumRect.height() - rulerToResultGap / 2);  //移动画布到正确的位置来绘制结果值
        resultNumPaint.getTextBounds(resultText, 0, resultText.length(), resultNumRect);
        canvas.drawText(resultText, width / 2 - resultNumRect.width() / 2, resultNumRect.height(), //绘制当前刻度结果值
                resultNumPaint);
        resultNumRight = width / 2 + resultNumRect.width() / 2 + 10;
        canvas.drawText(unit, resultNumRight, kgRect.height() + 2, unitPaint);            //在当前刻度结果值的又面10px的位置绘制单位
    }

    private void drawBg(Canvas canvas) {
        bgRect.set(0, 0, width, height);
        if (isBgRoundRect) {
            //椭圆的用于圆形角x-radius
            canvas.drawRoundRect(bgRect, roundRadius, roundRadius, bgPaint);
        } else {
            canvas.drawRect(bgRect, bgPaint);
        }
    }

    private void computeScrollTo(float scale) {
        scale = scale / scaleLimit;
        if (scale < minScale || scale > maxScale) {
            return;
        }
        computeScale = scale;
        invalidate();
    }

    @Override
    public void setTypeface(Typeface typeface) {
        if (resultNumPaint != null) {
            resultNumPaint.setTypeface(typeface);
        }
        if (unitPaint != null) {
            unitPaint.setTypeface(typeface);
        }
        if (scaleNumPaint != null) {
            scaleNumPaint.setTypeface(typeface);
        }
    }

    /**
     * 选择结果监听
     */
    public interface OnChooseResultListener {
        /**
         * 滑动选择结束
         *
         * @param result
         */
        void onEndResult(String result);

        /**
         * 开始滑动选择
         *
         * @param result
         */
        void onScrollResult(String result);
    }

    public void setRulerHeight(int rulerHeight) {
        this.rulerHeight = rulerHeight;
        invalidate();
    }

    public void setRulerToResultGap(int rulerToResultGap) {
        this.rulerToResultGap = rulerToResultGap;
        invalidate();
    }

    public void setScaleCount(int scaleCount) {
        this.scaleCount = scaleCount;
        invalidate();
    }

    public void setScaleGap(int scaleGap) {
        this.scaleGap = scaleGap;
        invalidate();
    }

    public void setMinScale(int minScale) {
        this.minScale = minScale;
        invalidate();
    }

    public void setFirstScale(float firstScale) {
        this.firstScale = firstScale;
        invalidate();
    }

    public void setMaxScale(int maxScale) {
        this.maxScale = maxScale;
        invalidate();
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
        invalidate();
    }

    public void setSmallScaleColor(int smallScaleColor) {
        this.smallScaleColor = smallScaleColor;
        invalidate();
    }

    public void setMidScaleColor(int midScaleColor) {
        this.midScaleColor = midScaleColor;
        invalidate();
    }

    public void setLargeScaleColor(int largeScaleColor) {
        this.largeScaleColor = largeScaleColor;
    }

    public void setScaleNumColor(int scaleNumColor) {
        this.scaleNumColor = scaleNumColor;
        invalidate();
    }

    public void setResultNumColor(int resultNumColor) {
        this.resultNumColor = resultNumColor;
        invalidate();
    }

    public void setUnit(String unit) {
        this.unit = unit;
        invalidate();
    }

    public void setUnitColor(int unitColor) {
        this.unitColor = unitColor;
        invalidate();
    }

    public void setSmallScaleStroke(int smallScaleStroke) {
        this.smallScaleStroke = smallScaleStroke;
        invalidate();
    }

    public void setMidScaleStroke(int midScaleStroke) {
        this.midScaleStroke = midScaleStroke;
        invalidate();
    }

    public void setLargeScaleStroke(int largeScaleStroke) {
        this.largeScaleStroke = largeScaleStroke;
        invalidate();
    }

    public void setResultNumTextSize(int resultNumTextSize) {
        this.resultNumTextSize = resultNumTextSize;
        invalidate();
    }

    public void setScaleNumTextSize(int scaleNumTextSize) {
        this.scaleNumTextSize = scaleNumTextSize;
        invalidate();
    }

    public void setUnitTextSize(int unitTextSize) {
        this.unitTextSize = unitTextSize;
        invalidate();
    }

    public void setShowScaleResult(boolean showScaleResult) {
        this.showScaleResult = showScaleResult;
        invalidate();
    }

    public void setIsBgRoundRect(boolean bgRoundRect) {
        isBgRoundRect = bgRoundRect;
        invalidate();
    }

    public void setScaleLimit(int scaleLimit) {
        this.scaleLimit = scaleLimit;
        invalidate();
    }

    /**
     * 设置刻度尺的值
     *
     * @param value
     */
    public void setCurrentValue(float value) {
        computeScrollTo(value);
    }

    /**
     * 获取当前刻度尺的刻度
     *
     * @return
     */
    public float getCurrentValue() {
        return currentScale;
    }

    /**
     * 获取选择值
     *
     * @return
     */
    public String getSelectValue() {
        return resultText;
    }

    /**
     * 设置选择监听
     *
     * @param onChooseResultListener
     * @return
     */
    public RulerView setOnChooseResultListener(OnChooseResultListener onChooseResultListener) {
        this.onChooseResultListener = onChooseResultListener;
        return this;
    }
}
