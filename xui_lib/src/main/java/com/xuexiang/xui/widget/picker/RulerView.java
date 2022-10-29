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
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.Nullable;

import com.xuexiang.xui.R;
import com.xuexiang.xui.utils.ResUtils;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;

import io.github.inflationx.calligraphy3.HasTypeface;

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
    private int mScaleLimit = 1;
    /**
     * 尺子高度
     */
    private int mRulerHeight = 50;
    /**
     * 尺子和屏幕顶部以及结果之间的高度
     */
    private int mRulerToResultGap = mRulerHeight / 4;
    /**
     * 刻度平分多少份
     */
    private int mScaleCount = 10;
    /**
     * 刻度间距
     */
    private int mScaleGap = 10;
    /**
     * 刻度最小值
     */
    private int mMinScale = 0;
    /**
     * 第一次显示的刻度
     */
    private float mFirstScale = 50f;
    /**
     * 刻度最大值
     */
    private int mMaxScale = 100;

    /**
     * 背景颜色
     */
    private int mBgColor;
    /**
     * 小刻度的颜色
     */
    private int mSmallScaleColor;
    /**
     * 中刻度的颜色
     */
    private int mMidScaleColor;
    /**
     * 大刻度的颜色
     */
    private int mLargeScaleColor;
    /**
     * 刻度颜色
     */
    private int mScaleNumColor;
    /**
     * 结果值颜色
     */
    private int mResultNumColor;
    /**
     * kg颜色
     */
    private String mUnit = "kg";
    /**
     * kg颜色
     */
    private int mUnitColor;
    /**
     * 小刻度粗细大小
     */
    private int mSmallScaleStroke = 2;
    /**
     * 中刻度粗细大小
     */
    private int mMidScaleStroke = 3;
    /**
     * 大刻度粗细大小
     */
    private int mLargeScaleStroke = 5;
    /**
     * 结果字体大小
     */
    private int mResultNumTextSize = 20;
    /**
     * 刻度字体大小
     */
    private int mScaleNumTextSize = 16;
    /**
     * 单位字体大小
     */
    private int mUnitTextSize = 13;
    /**
     * 是否显示刻度结果
     */
    private boolean mShowScaleResult = true;
    /**
     * 是否背景显示圆角
     */
    private boolean mIsBgRoundRect = true;
    /**
     * 圆角大小
     */
    private int mRoundRadius = 10;
    /**
     * 结果回调
     */
    private OnChooseResultListener mOnChooseResultListener;
    /**
     * 滑动选择刻度
     */
    private float mComputeScale = -1;
    /**
     * 当前刻度
     */
    public float mCurrentScale = mFirstScale;

    private ValueAnimator mValueAnimator;
    private VelocityTracker mVelocityTracker = VelocityTracker.obtain();
    private String mResultText = String.valueOf(mFirstScale);
    private Paint mBgPaint;
    private Paint mSmallScalePaint;
    private Paint mMidScalePaint;
    private Paint mLagScalePaint;
    private TextPaint mScaleNumPaint;
    private TextPaint mResultNumPaint;
    private TextPaint mUnitPaint;
    private Rect mScaleNumRect;
    private Rect mResultNumRect;
    private Rect mKgRect;
    private RectF mBgRect;
    private int mHeight, mWidth;
    private int mSmallScaleHeight;
    private int mMidScaleHeight;
    private int mLagScaleHeight;
    private float mDownX;
    private float mMoveX = 0;
    private float mLastMoveX = 0;
    private boolean mIsUp = false;

    public RulerView(Context context) {
        this(context, null);
    }

    public RulerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.RulerViewStyle);
    }

    public RulerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化属性
     *
     * @param attrs
     * @param defStyleAttr
     */
    private void initAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RulerView, defStyleAttr, 0);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        mScaleLimit = a.getInt(R.styleable.RulerView_rv_scaleLimit, mScaleLimit);
        mRulerHeight = a.getDimensionPixelSize(R.styleable.RulerView_rv_rulerHeight, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mRulerHeight, displayMetrics));
        mRulerToResultGap = a.getDimensionPixelSize(R.styleable.RulerView_rv_rulerToResultGap, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mRulerToResultGap, displayMetrics));
        mScaleCount = a.getInt(R.styleable.RulerView_rv_scaleCount, mScaleCount);
        mScaleGap = a.getDimensionPixelSize(R.styleable.RulerView_rv_scaleGap, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mScaleGap, displayMetrics));
        mMinScale = a.getInt(R.styleable.RulerView_rv_minScale, mMinScale) / mScaleLimit;
        mFirstScale = a.getFloat(R.styleable.RulerView_rv_firstScale, mFirstScale) / mScaleLimit;
        mMaxScale = a.getInt(R.styleable.RulerView_rv_maxScale, mMaxScale) / mScaleLimit;
        mBgColor = a.getColor(R.styleable.RulerView_rv_bgColor, ResUtils.getColor(context, R.color.default_ruler_view_bg_color));
        mSmallScaleColor = a.getColor(R.styleable.RulerView_rv_smallScaleColor, ResUtils.getColor(context, R.color.default_ruler_view_small_scale_color));
        mMidScaleColor = a.getColor(R.styleable.RulerView_rv_midScaleColor, ResUtils.getColor(context, R.color.default_ruler_view_mid_scale_color));
        mLargeScaleColor = a.getColor(R.styleable.RulerView_rv_largeScaleColor, ResUtils.getColor(context, R.color.default_ruler_view_large_scale_color));
        mScaleNumColor = a.getColor(R.styleable.RulerView_rv_scaleNumColor, ResUtils.getColor(context, R.color.default_ruler_view_scale_num_color));
        mResultNumColor = a.getColor(R.styleable.RulerView_rv_resultNumColor, ResUtils.getColor(context, R.color.default_ruler_view_result_num_color));
        mUnitColor = a.getColor(R.styleable.RulerView_rv_unitColor, ResUtils.getColor(context, R.color.default_ruler_view_unit_color));
        String tempUnit = mUnit;
        mUnit = a.getString(R.styleable.RulerView_rv_unit);
        if (TextUtils.isEmpty(mUnit)) {
            mUnit = tempUnit;
        }
        mSmallScaleStroke = a.getDimensionPixelSize(R.styleable.RulerView_rv_smallScaleStroke, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mSmallScaleStroke, displayMetrics));
        mMidScaleStroke = a.getDimensionPixelSize(R.styleable.RulerView_rv_midScaleStroke, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mMidScaleStroke, displayMetrics));
        mLargeScaleStroke = a.getDimensionPixelSize(R.styleable.RulerView_rv_largeScaleStroke, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mLargeScaleStroke, displayMetrics));
        mResultNumTextSize = a.getDimensionPixelSize(R.styleable.RulerView_rv_resultNumTextSize, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mResultNumTextSize, displayMetrics));
        mScaleNumTextSize = a.getDimensionPixelSize(R.styleable.RulerView_rv_scaleNumTextSize, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mScaleNumTextSize, displayMetrics));
        mUnitTextSize = a.getDimensionPixelSize(R.styleable.RulerView_rv_unitTextSize, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mUnitTextSize, displayMetrics));
        mShowScaleResult = a.getBoolean(R.styleable.RulerView_rv_showScaleResult, mShowScaleResult);
        mIsBgRoundRect = a.getBoolean(R.styleable.RulerView_rv_isBgRoundRect, mIsBgRoundRect);
        mRoundRadius = a.getDimensionPixelSize(R.styleable.RulerView_rv_roundRadius, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mRoundRadius, displayMetrics));
        a.recycle();
    }


    private void init() {
        mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSmallScalePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMidScalePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLagScalePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mScaleNumPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mResultNumPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mUnitPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

        mBgPaint.setColor(mBgColor);
        mSmallScalePaint.setColor(mSmallScaleColor);
        mMidScalePaint.setColor(mMidScaleColor);
        mLagScalePaint.setColor(mLargeScaleColor);
        mScaleNumPaint.setColor(mScaleNumColor);
        mResultNumPaint.setColor(mResultNumColor);
        mUnitPaint.setColor(mUnitColor);

        mResultNumPaint.setStyle(Paint.Style.FILL);
        mUnitPaint.setStyle(Paint.Style.FILL);
        mBgPaint.setStyle(Paint.Style.FILL);
        mSmallScalePaint.setStyle(Paint.Style.FILL);
        mMidScalePaint.setStyle(Paint.Style.FILL);
        mLagScalePaint.setStyle(Paint.Style.FILL);

        mLagScalePaint.setStrokeCap(Paint.Cap.ROUND);
        mMidScalePaint.setStrokeCap(Paint.Cap.ROUND);
        mSmallScalePaint.setStrokeCap(Paint.Cap.ROUND);

        mSmallScalePaint.setStrokeWidth(mSmallScaleStroke);
        mMidScalePaint.setStrokeWidth(mMidScaleStroke);
        mLagScalePaint.setStrokeWidth(mLargeScaleStroke);

        mResultNumPaint.setTextSize(mResultNumTextSize);
        mUnitPaint.setTextSize(mUnitTextSize);
        mScaleNumPaint.setTextSize(mScaleNumTextSize);

        mBgRect = new RectF();
        mResultNumRect = new Rect();
        mScaleNumRect = new Rect();
        mKgRect = new Rect();

        mResultNumPaint.getTextBounds(mResultText, 0, mResultText.length(), mResultNumRect);
        mUnitPaint.getTextBounds(mResultText, 0, 1, mKgRect);

        mSmallScaleHeight = mRulerHeight / 4;
        mMidScaleHeight = mRulerHeight / 2;
        mLagScaleHeight = mRulerHeight / 2 + 5;
        mValueAnimator = new ValueAnimator();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int heightModule = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        switch (heightModule) {
            case MeasureSpec.AT_MOST:
                mHeight = mRulerHeight + (mShowScaleResult ? mResultNumRect.height() : 0) + mRulerToResultGap * 2 + getPaddingTop() + getPaddingBottom();
                break;
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.EXACTLY:
                mHeight = heightSize + getPaddingTop() + getPaddingBottom();
                break;
            default:
                break;
        }

        mWidth = widthSize + getPaddingLeft() + getPaddingRight();

        setMeasuredDimension(mWidth, mHeight);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBg(canvas);
        drawScaleAndNum(canvas);
        drawResultText(canvas, mResultText);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getParent() != null) {
            //拦截事件，不让父布局获取
            getParent().requestDisallowInterceptTouchEvent(true);
        }

        float currentX = event.getX();
        mIsUp = false;
        mVelocityTracker.computeCurrentVelocity(500);
        mVelocityTracker.addMovement(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //按下时如果属性动画还没执行完,就终止,记录下当前按下点的位置
                if (mValueAnimator != null && mValueAnimator.isRunning()) {
                    mValueAnimator.end();
                    mValueAnimator.cancel();
                }
                mDownX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                //滑动时候,通过假设的滑动距离,做超出左边界以及右边界的限制。
                mMoveX = currentX - mDownX + mLastMoveX;
                if (mMoveX >= mWidth / 2F) {
                    mMoveX = mWidth / 2F;
                } else if (mMoveX <= getWhichScaleMoveX(mMaxScale)) {
                    mMoveX = getWhichScaleMoveX(mMaxScale);
                }
                break;
            case MotionEvent.ACTION_UP:
                //手指抬起时候制造惯性滑动
                mLastMoveX = mMoveX;
                int xVelocity = (int) mVelocityTracker.getXVelocity();
                autoVelocityScroll(xVelocity);
                mVelocityTracker.clear();
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
            mIsUp = true;
            return;
        }
        if (mValueAnimator.isRunning()) {
            return;
        }
        mValueAnimator = ValueAnimator.ofInt(0, xVelocity / 20).setDuration(Math.abs(xVelocity / 10));
        mValueAnimator.setInterpolator(new DecelerateInterpolator());
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mMoveX += (int) animation.getAnimatedValue();
                if (mMoveX >= mWidth / 2F) {
                    mMoveX = mWidth / 2F;
                } else if (mMoveX <= getWhichScaleMoveX(mMaxScale)) {
                    mMoveX = getWhichScaleMoveX(mMaxScale);
                }
                mLastMoveX = mMoveX;
                invalidate();
            }

        });
        mValueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mIsUp = true;
                invalidate();
            }
        });

        mValueAnimator.start();
    }

    private float getWhichScaleMoveX(float scale) {
        return mWidth / 2F - mScaleGap * mScaleCount * (scale - mMinScale);
    }

    private void drawScaleAndNum(Canvas canvas) {
        canvas.translate(0, (mShowScaleResult ? mResultNumRect.height() : 0) + mRulerToResultGap);//移动画布到结果值的下面

        int num1;//确定刻度位置
        float num2;

        if (mFirstScale != -1) {   //第一次进来的时候计算出默认刻度对应的假设滑动的距离moveX
            mMoveX = getWhichScaleMoveX(mFirstScale);          //如果设置了默认滑动位置，计算出需要滑动的距离
            mLastMoveX = mMoveX;
            mFirstScale = -1;                                //将结果置为-1，下次不再计算初始位置
        }

        if (mComputeScale != -1) {//弹性滑动到目标刻度
            mLastMoveX = mMoveX;
            if (mValueAnimator != null && !mValueAnimator.isRunning()) {
                mValueAnimator = ValueAnimator.ofFloat(getWhichScaleMoveX(mCurrentScale), getWhichScaleMoveX(mComputeScale));
                mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        mMoveX = (float) animation.getAnimatedValue();
                        mLastMoveX = mMoveX;
                        invalidate();
                    }
                });
                mValueAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        //这里是滑动结束时候回调给使用者的结果值
                        mComputeScale = -1;
                    }
                });
                mValueAnimator.setDuration(Math.abs((long) ((getWhichScaleMoveX(mComputeScale) - getWhichScaleMoveX(mCurrentScale)) / 100)));
                mValueAnimator.start();
            }
        }

        num1 = -(int) (mMoveX / mScaleGap);                   //滑动刻度的整数部分
        num2 = (mMoveX % mScaleGap);                         //滑动刻度的小数部分

        canvas.save();                                      //保存当前画布

        int rulerRight = 0;                                    //准备开始绘制当前屏幕,从最左面开始

        if (mIsUp) {   //这部分代码主要是计算手指抬起时，惯性滑动结束时，刻度需要停留的位置
            num2 = ((mMoveX - mWidth / 2F % mScaleGap) % mScaleGap);     //计算滑动位置距离整点刻度的小数部分距离
            if (num2 <= 0) {
                num2 = mScaleGap - Math.abs(num2);
            }
            int leftScroll = (int) Math.abs(num2);                        //当前滑动位置距离左边整点刻度的距离
            int rightScroll = (int) (mScaleGap - Math.abs(num2));         //当前滑动位置距离右边整点刻度的距离

            float moveX2 = num2 <= mScaleGap / 2F ? mMoveX - leftScroll : mMoveX + rightScroll; //最终计算出当前位置到整点刻度位置需要滑动的距离

            if (mValueAnimator != null && !mValueAnimator.isRunning()) {      //手指抬起，并且当前没有惯性滑动在进行，创建一个惯性滑动
                mValueAnimator = ValueAnimator.ofFloat(mMoveX, moveX2);
                mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        mMoveX = (float) animation.getAnimatedValue();            //不断滑动去更新新的位置
                        mLastMoveX = mMoveX;
                        invalidate();
                    }
                });
                mValueAnimator.addListener(new AnimatorListenerAdapter() {       //增加一个监听，用来返回给使用者滑动结束后的最终结果刻度值
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        //这里是滑动结束时候回调给使用者的结果值
                        if (mOnChooseResultListener != null) {
                            mOnChooseResultListener.onEndResult(mResultText);
                        }
                    }
                });
                mValueAnimator.setDuration(300);
                mValueAnimator.start();
                mIsUp = false;
            }
            //重新计算当前滑动位置的整数以及小数位置
            num1 = (int) -(mMoveX / mScaleGap);
            num2 = (mMoveX % mScaleGap);
        }

        //不加该偏移的话，滑动时刻度不会落在0~1之间只会落在整数上面,其实这个都能设置一种模式了，毕竟初衷就是指针不会落在小数上面
        canvas.translate(num2, 0);

        //这里是滑动时候不断回调给使用者的结果值
        mCurrentScale = new WeakReference<>(new BigDecimal(((mWidth / 2F - mMoveX) / (mScaleGap * mScaleCount) + mMinScale) * mScaleLimit)).get().setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();

        mResultText = String.valueOf(mCurrentScale);


        if (mOnChooseResultListener != null) {
            mOnChooseResultListener.onScrollResult(mResultText); //接口不断回调给使用者结果值
        }
        //绘制当前屏幕可见刻度,不需要裁剪屏幕,while循环只会执行·屏幕宽度/刻度宽度·次,大部分的绘制都是if(curDis<mWidth)这样子内存暂用相对来说会比较高。。
        while (rulerRight < mWidth) {
            if (num1 % mScaleCount == 0) {    //绘制整点刻度以及文字
                if ((mMoveX >= 0 && rulerRight < mMoveX - mScaleGap) || mWidth / 2F - rulerRight <= getWhichScaleMoveX(mMaxScale + 1) - mMoveX) {
                    //当滑动出范围的话，不绘制，去除左右边界
                } else {
                    //绘制刻度，绘制刻度数字
                    canvas.drawLine(0, 0, 0, mMidScaleHeight, mMidScalePaint);
                    mScaleNumPaint.getTextBounds(num1 / mScaleGap + mMinScale + "", 0, (num1 / mScaleGap + mMinScale + "").length(), mScaleNumRect);
                    canvas.drawText((num1 / mScaleCount + mMinScale) * mScaleLimit + "", -mScaleNumRect.width() / 2F, mLagScaleHeight +
                            (mRulerHeight - mLagScaleHeight) / 2F + mScaleNumRect.height(), mScaleNumPaint);

                }

            } else {   //绘制小数刻度
                if ((mMoveX >= 0 && rulerRight < mMoveX) || mWidth / 2F - rulerRight < getWhichScaleMoveX(mMaxScale) - mMoveX) {
                    //当滑动出范围的话，不绘制，去除左右边界
                } else {
                    //绘制小数刻度
                    canvas.drawLine(0, 0, 0, mSmallScaleHeight, mSmallScalePaint);
                }
            }
            ++num1;  //刻度加1
            rulerRight += mScaleGap;  //绘制屏幕的距离在原有基础上+1个刻度间距
            canvas.translate(mScaleGap, 0); //移动画布到下一个刻度
        }

        canvas.restore();
        //绘制屏幕中间用来选中刻度的最大刻度
        canvas.drawLine(mWidth / 2F, 0, mWidth / 2F, mLagScaleHeight, mLagScalePaint);

    }

    //绘制上面的结果 结果值+单位
    private void drawResultText(Canvas canvas, String resultText) {
        if (!mShowScaleResult) {   //判断用户是否设置需要显示当前刻度值，如果否则取消绘制
            return;
        }
        canvas.translate(0, -mResultNumRect.height() - mRulerToResultGap / 2F);  //移动画布到正确的位置来绘制结果值
        mResultNumPaint.getTextBounds(resultText, 0, resultText.length(), mResultNumRect);
        canvas.drawText(resultText, mWidth / 2F - mResultNumRect.width() / 2F, mResultNumRect.height(), //绘制当前刻度结果值
                mResultNumPaint);
        int resultNumRight = mWidth / 2 + mResultNumRect.width() / 2 + 10;
        canvas.drawText(mUnit, resultNumRight, mKgRect.height() + 2, mUnitPaint);            //在当前刻度结果值的又面10px的位置绘制单位
    }

    private void drawBg(Canvas canvas) {
        mBgRect.set(0, 0, mWidth, mHeight);
        if (mIsBgRoundRect) {
            //椭圆的用于圆形角x-radius
            canvas.drawRoundRect(mBgRect, mRoundRadius, mRoundRadius, mBgPaint);
        } else {
            canvas.drawRect(mBgRect, mBgPaint);
        }
    }

    private void computeScrollTo(float scale) {
        scale = scale / mScaleLimit;
        if (scale < mMinScale || scale > mMaxScale) {
            return;
        }
        mComputeScale = scale;
        invalidate();
    }

    @Override
    public void setTypeface(Typeface typeface) {
        if (mResultNumPaint != null) {
            mResultNumPaint.setTypeface(typeface);
        }
        if (mUnitPaint != null) {
            mUnitPaint.setTypeface(typeface);
        }
        if (mScaleNumPaint != null) {
            mScaleNumPaint.setTypeface(typeface);
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
        this.mRulerHeight = rulerHeight;
        invalidate();
    }

    public void setRulerToResultGap(int rulerToResultGap) {
        this.mRulerToResultGap = rulerToResultGap;
        invalidate();
    }

    public void setScaleCount(int scaleCount) {
        this.mScaleCount = scaleCount;
        invalidate();
    }

    public void setScaleGap(int scaleGap) {
        this.mScaleGap = scaleGap;
        invalidate();
    }

    public void setMinScale(int minScale) {
        this.mMinScale = minScale;
        invalidate();
    }

    public void setFirstScale(float firstScale) {
        this.mFirstScale = firstScale;
        invalidate();
    }

    public void setMaxScale(int maxScale) {
        this.mMaxScale = maxScale;
        invalidate();
    }

    public void setBgColor(int bgColor) {
        this.mBgColor = bgColor;
        invalidate();
    }

    public void setSmallScaleColor(int smallScaleColor) {
        this.mSmallScaleColor = smallScaleColor;
        invalidate();
    }

    public void setMidScaleColor(int midScaleColor) {
        this.mMidScaleColor = midScaleColor;
        invalidate();
    }

    public void setLargeScaleColor(int largeScaleColor) {
        this.mLargeScaleColor = largeScaleColor;
    }

    public void setScaleNumColor(int scaleNumColor) {
        this.mScaleNumColor = scaleNumColor;
        invalidate();
    }

    public void setResultNumColor(int resultNumColor) {
        this.mResultNumColor = resultNumColor;
        invalidate();
    }

    public void setUnit(String unit) {
        this.mUnit = unit;
        invalidate();
    }

    public void setUnitColor(int unitColor) {
        this.mUnitColor = unitColor;
        invalidate();
    }

    public void setSmallScaleStroke(int smallScaleStroke) {
        this.mSmallScaleStroke = smallScaleStroke;
        invalidate();
    }

    public void setMidScaleStroke(int midScaleStroke) {
        this.mMidScaleStroke = midScaleStroke;
        invalidate();
    }

    public void setLargeScaleStroke(int largeScaleStroke) {
        this.mLargeScaleStroke = largeScaleStroke;
        invalidate();
    }

    public void setResultNumTextSize(int resultNumTextSize) {
        this.mResultNumTextSize = resultNumTextSize;
        invalidate();
    }

    public void setScaleNumTextSize(int scaleNumTextSize) {
        this.mScaleNumTextSize = scaleNumTextSize;
        invalidate();
    }

    public void setUnitTextSize(int unitTextSize) {
        this.mUnitTextSize = unitTextSize;
        invalidate();
    }

    public void setShowScaleResult(boolean showScaleResult) {
        this.mShowScaleResult = showScaleResult;
        invalidate();
    }

    public void setIsBgRoundRect(boolean bgRoundRect) {
        mIsBgRoundRect = bgRoundRect;
        invalidate();
    }

    public void setScaleLimit(int scaleLimit) {
        this.mScaleLimit = scaleLimit;
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
        return mCurrentScale;
    }

    /**
     * 获取选择值
     *
     * @return
     */
    public String getSelectValue() {
        return mResultText;
    }

    /**
     * 设置选择监听
     *
     * @param onChooseResultListener
     * @return
     */
    public RulerView setOnChooseResultListener(OnChooseResultListener onChooseResultListener) {
        this.mOnChooseResultListener = onChooseResultListener;
        return this;
    }
}
