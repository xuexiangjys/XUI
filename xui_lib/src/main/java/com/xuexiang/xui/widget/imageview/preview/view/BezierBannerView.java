/*
 * Copyright (C) 2018 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xuexiang.xui.widget.imageview.preview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import com.xuexiang.xui.R;


/**
 * 指示器控件
 *
 * @author xuexiang
 * @since 2018/12/5 上午11:22
 */
public class BezierBannerView extends View implements ViewPager.OnPageChangeListener {
    //选中画笔
    private Paint mCirclePaint;
    //背景画笔
    private Paint mCirclePaint2;
    //选中路径
    private Path mPath = new Path();
    //背景路径
    private Path mPath2 = new Path();
    //选中颜色
    private int mSelectedColor;
    //未选中颜色
    private int mUnSelectedColor;
    //圆点之间距离
    private float distance = 80;
    //起始圆初始半径
    private float mRadius = 30;
    //起始圆变化半径
    private float mChangeRadius;
    //背景圆初始半径
    private float mNomarlRadius = 20;
    //背景圆变化半径 背景圆都用这个
    private float mChangeBgRadius;
    //起始圆辅助圆变化半径
    private float mSupportChangeRadius;
    //将要到达位置的背景圆的辅助圆变化半径
    private float mSupport_Next_ChangeRadius;
    //起始圆圆心坐标
    float mCenterPointX;
    float mCenterPointY;
    //起始圆辅助圆圆心坐标
    float mSupportCircleX;
    float mSupportCircleY;
    //当前背景圆圆心坐标
    float mSupport_next_centerX;
    float mSupport_next_centerY;
    //将要到达位置的背景圆圆心坐标
    float mbgNextPointX;
    float mbgNextPointY;

    //是否进入自动移动状态
    private boolean autoMove = false;
    //第一阶段运动进度
    private float mProgress = 0;
    //第二阶段运动进度
    private float mProgress2 = 0;
    //整体运动进度 也是原始进度
    private float mOriginProgress;

    //当前选中的位置
    private int mSelectedIndex = 0;
    private int count;

    //第一阶段运动
    private int MOVE_STEP_ONE = 1;
    //第二阶段运动
    private int MOVE_STEP_TWO = 2;
    //控制点坐标
    float controlPointX;
    float controlPointY;
    //起点坐标
    float mStartX;
    float mStartY;
    //终点坐标
    float endPointX;
    float endPointY;
    private int mDrection;
    //向右滑 向左滚动
    public static int DIRECTION_LEFT = 1;
    //向左滑 向右滚动
    public static int DIRECTION_RIGHT = 2;
    private static final String TAG = BezierBannerView.class.getName();

    Interpolator accelerateinterpolator = new AccelerateDecelerateInterpolator();


    public BezierBannerView(Context context) {
        this(context, null);
    }

    public BezierBannerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BezierBannerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
        initPaint();
    }


    private void initPaint() {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(mSelectedColor);
        p.setStyle(Paint.Style.FILL);
        p.setAntiAlias(true);
        p.setDither(true);
        mCirclePaint = p;

        Paint p1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        p1.setColor(mUnSelectedColor);
        p1.setStyle(Paint.Style.FILL);
        p1.setAntiAlias(true);
        p1.setDither(true);
        mCirclePaint2 = p1;

    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.BezierBannerView);
        mSelectedColor = typedArray.getColor(R.styleable.BezierBannerView_bbv_selectedColor, 0xFFFFFFFF);
        mUnSelectedColor = typedArray.getColor(R.styleable.BezierBannerView_bbv_unSelectedColor, 0xFFAAAAAA);
        mRadius = typedArray.getDimension(R.styleable.BezierBannerView_bbv_selectedRadius, mRadius);
        mNomarlRadius = typedArray.getDimension(R.styleable.BezierBannerView_bbv_unSelectedRadius, mNomarlRadius);
        distance = typedArray.getDimension(R.styleable.BezierBannerView_bbv_spacing, distance);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        //宽度等于所有圆点宽度+之间的间隔+padding;要留出当左右两个是大圆点的左右边距
        int width = (int) (mNomarlRadius * 2 * count + (mRadius - mNomarlRadius) * 2 + (count - 1) * distance + getPaddingLeft() + getPaddingRight());
        int height = (int) (2 * mRadius + getPaddingTop() + getPaddingBottom());

        int mHeight, mWidth;

        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            mWidth = Math.min(widthSize, width);
        } else {
            mWidth = widthSize;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            mHeight = Math.min(heightSize, height);
        } else {
            mHeight = heightSize;
        }

        setMeasuredDimension(mWidth, mHeight);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        canvas.translate(getPaddingLeft(), getPaddingTop());

        //画暂不活动的背景圆
        for (int i = 0; i < count; i++) {
            if (mDrection == DIRECTION_RIGHT) {
                if (i == mSelectedIndex || i == mSelectedIndex + 1) {
                    //活动的就不用画了
                } else {
                    canvas.drawCircle(getCenterPointAt(i), mRadius, mNomarlRadius, mCirclePaint2);
                }

            } else if (mDrection == DIRECTION_LEFT) {
                if (i == mSelectedIndex || i == mSelectedIndex - 1) {
                    //活动的就不用画了
                } else {
                    canvas.drawCircle(getCenterPointAt(i), mRadius, mNomarlRadius, mCirclePaint2);
                }
            }
        }

        //画活动背景圆
        canvas.drawCircle(mSupport_next_centerX, mSupport_next_centerY, mSupport_Next_ChangeRadius, mCirclePaint2);
        canvas.drawCircle(mbgNextPointX, mbgNextPointY, mChangeBgRadius, mCirclePaint2);
        canvas.drawPath(mPath2, mCirclePaint2);
        //画选中圆
        canvas.drawCircle(mSupportCircleX, mSupportCircleY, mSupportChangeRadius, mCirclePaint);
        canvas.drawCircle(mCenterPointX, mCenterPointY, mChangeRadius, mCirclePaint);
        canvas.drawPath(mPath, mCirclePaint);

        canvas.restore();

    }

    /**
     * 转化整体进度值使两个阶段的运动进度都是0-1
     *
     * @param progress 当前整体进度
     */
    public void setProgress(float progress) {
        //viewpager滑动完毕返回的0不需要，拦截掉
        if (progress == 0) {
            return;
        }
        mOriginProgress = progress;
        if (progress <= 0.5) {
            mProgress = progress / 0.5f;
            mProgress2 = 0;
        } else {
            mProgress2 = (progress - 0.5f) / 0.5f;
            mProgress = 1;

        }
        if (mDrection == DIRECTION_RIGHT) {
            moveToNext();
        } else {
            moveToPrivous();
        }
        invalidate();
    }

    /**
     * 向右移动
     */
    private void moveToNext() {
        //重置路径
        mPath.reset();
        mPath2.reset();
        //使用一个插值器使圆的大小变化两边慢中间快
        float mRadiusProgress = accelerateinterpolator.getInterpolation(mOriginProgress);
        //----------------------选中圆--------------------------------
        //起始圆圆心
        mCenterPointX = getValue(getCenterPointAt(mSelectedIndex), getCenterPointAt(mSelectedIndex + 1) - mRadius, MOVE_STEP_TWO);
        mCenterPointY = mRadius;
        //起始圆半径
        mChangeRadius = getValue(mRadius, 0, mRadiusProgress);

        //起点与起始圆圆心间的角度
        double radian = Math.toRadians(getValue(45, 0, MOVE_STEP_ONE));
        //X轴距离圆心距离
        float mX = (float) (Math.sin(radian) * mChangeRadius);
        //Y轴距离圆心距离
        float mY = (float) (Math.cos(radian) * mChangeRadius);

        //辅助圆
        mSupportCircleX = getValue(getCenterPointAt(mSelectedIndex) + mRadius, getCenterPointAt(mSelectedIndex + 1), MOVE_STEP_ONE);
        mSupportCircleY = mRadius;
        mSupportChangeRadius = getValue(0, mRadius, mRadiusProgress);

        //终点与辅助圆圆心间的角度
        double supportradian = Math.toRadians(getValue(0, 45, MOVE_STEP_TWO));
        //X轴距离圆心距离
        float msupportradianX = (float) (Math.sin(supportradian) * mSupportChangeRadius);
        //Y轴距离圆心距离
        float msupportradianY = (float) (Math.cos(supportradian) * mSupportChangeRadius);

        //起点
        mStartX = mCenterPointX + mX;
        mStartY = mCenterPointY - mY;

        //终点
        endPointX = mSupportCircleX - msupportradianX;
        endPointY = mRadius - msupportradianY;

        //控制点
        controlPointX = getValueForAll(getCenterPointAt(mSelectedIndex) + mRadius, getCenterPointAt(mSelectedIndex + 1) - mRadius);
        controlPointY = mRadius;

        //移动到起点
        mPath.moveTo(mStartX, mStartY);

        //形成闭合区域
        mPath.quadTo(controlPointX, controlPointY, endPointX, endPointY);
        mPath.lineTo(endPointX, mRadius + msupportradianY);
        mPath.quadTo(controlPointX, mRadius, mStartX, mStartY + 2 * mY);
        mPath.lineTo(mStartX, mStartY);

        //----------------------背景圆反方向移动--------------------------------
        //起始圆圆心
        mbgNextPointX = getValue(getCenterPointAt(mSelectedIndex + 1), getCenterPointAt(mSelectedIndex) + mNomarlRadius, MOVE_STEP_TWO);
        mbgNextPointY = mRadius;
        //起始圆半径
        mChangeBgRadius = getValue(mNomarlRadius, 0, mRadiusProgress);

        //起点与起始圆圆心间的角度
        double m_Next_radian = Math.toRadians(getValue(45, 0, MOVE_STEP_ONE));
        float mX_next = (float) (Math.sin(m_Next_radian) * mChangeBgRadius);
        float mY_next = (float) (Math.cos(m_Next_radian) * mChangeBgRadius);
        //辅助圆圆心
        mSupport_next_centerX = getValue(getCenterPointAt(mSelectedIndex + 1) - mNomarlRadius, getCenterPointAt(mSelectedIndex), MOVE_STEP_ONE);
        mSupport_next_centerY = mRadius;
        //辅助圆半径
        mSupport_Next_ChangeRadius = getValue(0, mNomarlRadius, mRadiusProgress);

        //终点与辅助圆圆心间的角度
        double mSupport_Next_radian = Math.toRadians(getValue(0, 45, MOVE_STEP_TWO));
        float mSupport_Next_radianX = (float) (Math.sin(mSupport_Next_radian) * mSupport_Next_ChangeRadius);
        float mSupport_Next_radianY = (float) (Math.cos(mSupport_Next_radian) * mSupport_Next_ChangeRadius);

        //起点
        float startPoint_support_nextX = mbgNextPointX - mX_next;
        float startPoint_support_nextY = mbgNextPointY - mY_next;

        //终点
        float endPoint_support_nextX = mSupport_next_centerX + mSupport_Next_radianX;
        float endPoint_support_nextY = mSupport_next_centerY - mSupport_Next_radianY;

        //控制点
        float controlPointX_Next = getValueForAll(getCenterPointAt(mSelectedIndex + 1) - mNomarlRadius, getCenterPointAt(mSelectedIndex) + mNomarlRadius);
        float controlPointY_Next = mRadius;

        //移动到起点
        mPath2.moveTo(startPoint_support_nextX, startPoint_support_nextY);
        //形成闭合区域
        mPath2.quadTo(controlPointX_Next, controlPointY_Next, endPoint_support_nextX, endPoint_support_nextY);
        mPath2.lineTo(endPoint_support_nextX, mRadius + mSupport_Next_radianY);
        mPath2.quadTo(controlPointX_Next, controlPointY_Next, startPoint_support_nextX, startPoint_support_nextY + 2 * mY_next);
        mPath2.lineTo(startPoint_support_nextX, startPoint_support_nextY);

    }


    /**
     * 向左移动(与向右过程大致相同)
     */
    private void moveToPrivous() {
        mPath.reset();
        mPath2.reset();

        float mRadiusProgress = accelerateinterpolator.getInterpolation(mOriginProgress);

        //----------------------选中圆--------------------------------
        mCenterPointX = getValue(getCenterPointAt(mSelectedIndex), getCenterPointAt(mSelectedIndex - 1) + mRadius, MOVE_STEP_TWO);
        mCenterPointY = mRadius;
        mChangeRadius = getValue(mRadius, 0, mRadiusProgress);
        //起点与起始圆圆心间的角度
        double radian = Math.toRadians(getValue(45, 0, MOVE_STEP_ONE));
        //X轴距离圆心距离
        float mX = (float) (Math.sin(radian) * mChangeRadius);
        //Y轴距离圆心距离
        float mY = (float) (Math.cos(radian) * mChangeRadius);

        //辅助圆
        mSupportCircleX = getValue(getCenterPointAt(mSelectedIndex) - mRadius, getCenterPointAt(mSelectedIndex - 1), MOVE_STEP_ONE);
        mSupportCircleY = mRadius;
        mSupportChangeRadius = getValue(0, mRadius, mRadiusProgress);


        //终点与辅助圆圆心间的角度
        double supportradian = Math.toRadians(getValue(0, 45, MOVE_STEP_TWO));
        //X轴距离圆心距离
        float msupportradianX = (float) (Math.sin(supportradian) * mSupportChangeRadius);
        //Y轴距离圆心距离
        float msupportradianY = (float) (Math.cos(supportradian) * mSupportChangeRadius);

        mStartX = mCenterPointX - mX;
        mStartY = mCenterPointY - mY;

        endPointX = mSupportCircleX + msupportradianX;
        endPointY = mRadius - msupportradianY;

        controlPointX = getValueForAll(getCenterPointAt(mSelectedIndex) - mRadius, getCenterPointAt(mSelectedIndex - 1) + mRadius);
        controlPointY = mRadius;

        mPath.moveTo(mStartX, mStartY);
        mPath.quadTo(controlPointX, controlPointY, endPointX, endPointY);
        mPath.lineTo(endPointX, mRadius + msupportradianY);
        mPath.quadTo(controlPointX, mRadius, mStartX, mStartY + 2 * mY);
        mPath.lineTo(mStartX, mStartY);


        //----------------------背景圆反方向移动--------------------------------
        mbgNextPointX = getValue(getCenterPointAt(mSelectedIndex - 1), getCenterPointAt(mSelectedIndex) - mNomarlRadius, MOVE_STEP_TWO);
        mbgNextPointY = mRadius;
        mChangeBgRadius = getValue(mNomarlRadius, 0, mRadiusProgress);
        //起点与起始圆圆心间的角度
        double m_Next_radian = Math.toRadians(getValue(45, 0, MOVE_STEP_ONE));
        //X轴距离圆心距离
        float mX_next = (float) (Math.sin(m_Next_radian) * mChangeBgRadius);
        //Y轴距离圆心距离
        float mY_next = (float) (Math.cos(m_Next_radian) * mChangeBgRadius);

        mSupport_next_centerX = getValue(getCenterPointAt(mSelectedIndex - 1) + mNomarlRadius, getCenterPointAt(mSelectedIndex), MOVE_STEP_ONE);
        mSupport_next_centerY = mRadius;
        mSupport_Next_ChangeRadius = getValue(0, mNomarlRadius, mRadiusProgress);

        //终点与辅助圆圆心间的角度
        double mSupport_Next_radian = Math.toRadians(getValue(0, 45, MOVE_STEP_TWO));
        //X轴距离圆心距离
        float mSupport_Next_radianX = (float) (Math.sin(mSupport_Next_radian) * mSupport_Next_ChangeRadius);
        //Y轴距离圆心距离
        float mSupport_Next_radianY = (float) (Math.cos(mSupport_Next_radian) * mSupport_Next_ChangeRadius);

        float startPoint_support_nextX = mbgNextPointX + mX_next;
        float startPoint_support_nextY = mbgNextPointY - mY_next;

        float endPoint_support_nextX = mSupport_next_centerX - mSupport_Next_radianX;
        float endPoint_support_nextY = mSupport_next_centerY - mSupport_Next_radianY;

        float controlPointX_Next = getValueForAll(getCenterPointAt(mSelectedIndex - 1) + mNomarlRadius, getCenterPointAt(mSelectedIndex) - mNomarlRadius);
        float controlPointY_Next = mRadius;

        mPath2.moveTo(startPoint_support_nextX, startPoint_support_nextY);
        mPath2.quadTo(controlPointX_Next, controlPointY_Next, endPoint_support_nextX, endPoint_support_nextY);
        mPath2.lineTo(endPoint_support_nextX, mRadius + mSupport_Next_radianY);
        mPath2.quadTo(controlPointX_Next, controlPointY_Next, startPoint_support_nextX, startPoint_support_nextY + 2 * mY_next);
        mPath2.lineTo(startPoint_support_nextX, startPoint_support_nextY);

    }


    /**
     * 获取当前值(适用分阶段变化的值)
     *
     * @param start 初始值
     * @param end   终值
     * @param step  第几活动阶段
     * @return float
     */
    public float getValue(float start, float end, int step) {
        if (step == MOVE_STEP_ONE) {
            return start + (end - start) * mProgress;
        } else {
            return start + (end - start) * mProgress2;
        }
    }

    /**
     * 获取当前值（适用全过程变化的值）
     *
     * @param start 初始值
     * @param end   终值
     * @return float
     */
    public float getValueForAll(float start, float end) {
        return start + (end - start) * mOriginProgress;
    }

    /**
     * 通过进度获取当前值
     *
     * @param start    初始值
     * @param end      终值
     * @param progress 当前进度
     * @return float
     */
    public float getValue(float start, float end, float progress) {
        return start + (end - start) * progress;
    }

    /**
     * 获取圆心X坐标
     *
     * @param index 第几个圆
     * @return float
     */
    private float getCenterPointAt(int index) {
        if (index == 0) {
            return mRadius;
        }
        return index * (distance + 2 * mNomarlRadius) + mNomarlRadius + (mRadius - mNomarlRadius);
    }

    public void setDirection(int direction) {
        mDrection = direction;
    }

    /**
     * 重置进度
     */
    public void resetProgress() {
        mProgress = 0;
        mProgress2 = 0;
        mOriginProgress = 0;
    }

    /**
     * 绑定viewpager
     *
     * @param viewPager viewPager
     */
    public void attachToViewpager(ViewPager viewPager) {
        viewPager.addOnPageChangeListener(this);
        if (viewPager.getAdapter() != null) {
            count = viewPager.getAdapter().getCount();
        }
        mSelectedIndex = viewPager.getCurrentItem();
        moveToNext();
        mDrection = DIRECTION_RIGHT;
        invalidate();
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //偏移量为0 说明运动停止
        if (positionOffset == 0) {
            mSelectedIndex = position;
            Log.d(TAG, "到达");
            resetProgress();
        }
        //向左滑，指示器向右移动
        if (position + positionOffset - mSelectedIndex > 0) {
            mDrection = DIRECTION_RIGHT;
            //向左快速滑动 偏移量不归0 但是position发生了改变 需要更新当前索引
            if (position + positionOffset > mSelectedIndex + 1) {
                mSelectedIndex = position;
                Log.d(TAG, "向左快速滑动");
            } else {
                setProgress(positionOffset);
            }
        } else if (position + positionOffset - mSelectedIndex < 0) { //向右滑，指示器向左移动
            mDrection = DIRECTION_LEFT;
            //向右快速滑动
            if (position + positionOffset < mSelectedIndex - 1) {
                mSelectedIndex = position;
                Log.d(TAG, "向右快速滑动");
            } else {
                setProgress(1 - positionOffset);
            }
        }

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}