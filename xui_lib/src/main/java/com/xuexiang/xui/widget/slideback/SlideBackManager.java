/*
 * Copyright (C) 2019 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.xuexiang.xui.widget.slideback;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.xuexiang.xui.widget.slideback.callback.SlideBackCallBack;
import com.xuexiang.xui.widget.slideback.callback.SlideCallBack;
import com.xuexiang.xui.widget.slideback.dispatcher.ISlideTouchEventDispatcher;
import com.xuexiang.xui.widget.slideback.dispatcher.OnSlideUpdateListener;
import com.xuexiang.xui.widget.slideback.dispatcher.impl.DefaultSlideTouchDispatcher;
import com.xuexiang.xui.widget.slideback.widget.SlideBackIconView;
import com.xuexiang.xui.widget.slideback.widget.SlideBackInterceptLayout;

import static com.xuexiang.xui.widget.slideback.SlideBack.EDGE_BOTH;
import static com.xuexiang.xui.widget.slideback.SlideBack.EDGE_LEFT;
import static com.xuexiang.xui.widget.slideback.SlideBack.EDGE_RIGHT;

/**
 * SlideBack管理器
 *
 * @author xuexiang
 * @since 2019-08-30 9:31
 */
public class SlideBackManager implements OnSlideUpdateListener {
    private SlideBackIconView mSlideBackIconViewLeft;
    private SlideBackIconView mSlideBackIconViewRight;

    private Activity mActivity;
    private boolean mHaveScroll;
    /**
     * 侧滑事件监听
     */
    private SlideCallBack mCallBack;
    /**
     * 侧滑信息
     */
    private SlideInfo mSlideInfo;

    SlideBackManager(Activity activity, boolean isFixedSize) {
        mActivity = activity;
        mHaveScroll = false;

        // 获取屏幕信息，初始化控件设置
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        mSlideInfo = new SlideInfo()
                .setArrowSize(dp2px(5))
                .setScreenWidth(dm.widthPixels)
                .setDragRate(3)
                .setAllowEdgeLeft(true)
                .setAllowEdgeRight(false);
        if (isFixedSize) {
            mSlideInfo.setBackViewHeight(dm.heightPixels / 5f)
                    .setMaxSlideLength(dp2px(24))
                    .setSideSlideLength(dp2px(12));
        } else {
            mSlideInfo.setBackViewHeight(dm.heightPixels / 4f)
                    .setMaxSlideLength((float) dm.widthPixels / 12)
                    .setSideSlideLength((float) dm.widthPixels / 24);
        }
    }

    SlideBackManager(Activity activity, SlideInfo slideInfo) {
        mActivity = activity;
        mHaveScroll = false;
        mSlideInfo = slideInfo;
    }

    /**
     * 是否包含滑动控件 默认false
     */
    public SlideBackManager haveScroll(boolean haveScroll) {
        mHaveScroll = haveScroll;
        return this;
    }

    /**
     * 回调
     */
    public SlideBackManager callBack(SlideBackCallBack callBack) {
        mCallBack = new SlideCallBack(callBack) {
            @Override
            public void onSlide(int edgeFrom) {
                onSlideBack();
            }
        };
        return this;
    }

    /**
     * 回调 适用于新的左右模式
     */
    public SlideBackManager callBack(SlideCallBack callBack) {
        mCallBack = callBack;
        return this;
    }

    /**
     * 设置侧滑信息
     *
     * @param slideInfo 侧滑信息
     * @return this
     */
    public SlideBackManager setSlideInfo(SlideInfo slideInfo) {
        mSlideInfo = slideInfo;
        return this;
    }

    /**
     * 控件高度 默认屏高/4
     */
    public SlideBackManager viewHeight(float backViewHeightDp) {
        mSlideInfo.setBackViewHeight(dp2px(backViewHeightDp));
        return this;
    }

    /**
     * 箭头大小 默认5dp
     */
    public SlideBackManager arrowSize(float arrowSizeDp) {
        mSlideInfo.setArrowSize(dp2px(arrowSizeDp));
        return this;
    }

    /**
     * 最大拉动距离（控件最大宽度） 默认屏宽/12
     */
    public SlideBackManager maxSlideLength(float maxSlideLengthDp) {
        mSlideInfo.setMaxSlideLength(dp2px(maxSlideLengthDp));
        return this;
    }

    /**
     * 侧滑响应距离 默认控件最大宽度/2
     */
    public SlideBackManager sideSlideLength(float sideSlideLengthDp) {
        mSlideInfo.setSideSlideLength(dp2px(sideSlideLengthDp));
        return this;
    }

    /**
     * 阻尼系数 默认3（越小越灵敏）
     */
    public SlideBackManager dragRate(float dragRate) {
        mSlideInfo.setDragRate(dragRate);
        return this;
    }

    /**
     * 边缘侧滑模式 默认左
     */
    public SlideBackManager edgeMode(@SlideBack.EdgeMode int edgeMode) {
        switch (edgeMode) {
            case EDGE_LEFT:
                mSlideInfo.setEdgeMode(true, false);
                break;
            case EDGE_RIGHT:
                mSlideInfo.setEdgeMode(false, true);
                break;
            case EDGE_BOTH:
                mSlideInfo.setEdgeMode(true, true);
                break;
            default:
                throw new RuntimeException("未定义的边缘侧滑模式值：EdgeMode = " + edgeMode);
        }
        return this;
    }

    /**
     * 需要使用滑动的页面注册
     */
    public void register() {
        register(new DefaultSlideTouchDispatcher().init(mSlideInfo, mCallBack, this));
    }

    /**
     * 需要使用滑动的页面注册
     */
    @SuppressLint("ClickableViewAccessibility")
    public void register(ISlideTouchEventDispatcher dispatcher) {
        if (mSlideInfo.isAllowEdgeLeft()) {
            // 初始化SlideBackIconView 左侧
            mSlideBackIconViewLeft = new SlideBackIconView(mActivity);
            mSlideBackIconViewLeft.setBackViewHeight(mSlideInfo.getBackViewHeight());
            mSlideBackIconViewLeft.setArrowSize(mSlideInfo.getArrowSize());
            mSlideBackIconViewLeft.setMaxSlideLength(mSlideInfo.getMaxSlideLength());
        }
        if (mSlideInfo.isAllowEdgeRight()) {
            // 初始化SlideBackIconView - Right
            mSlideBackIconViewRight = new SlideBackIconView(mActivity);
            mSlideBackIconViewRight.setBackViewHeight(mSlideInfo.getBackViewHeight());
            mSlideBackIconViewRight.setArrowSize(mSlideInfo.getArrowSize());
            mSlideBackIconViewRight.setMaxSlideLength(mSlideInfo.getMaxSlideLength());
            // 右侧侧滑 需要旋转180°
            mSlideBackIconViewRight.setRotationY(180);
        }

        // 获取decorView并设置OnTouchListener监听
        FrameLayout container = (FrameLayout) mActivity.getWindow().getDecorView();
        if (mHaveScroll) {
            SlideBackInterceptLayout interceptLayout = new SlideBackInterceptLayout(mActivity);
            interceptLayout.setSideSlideLength(mSlideInfo.getSideSlideLength());
            addInterceptLayout(container, interceptLayout);
        }
        if (mSlideInfo.isAllowEdgeLeft()) {
            container.addView(mSlideBackIconViewLeft);
        }
        if (mSlideInfo.isAllowEdgeRight()) {
            container.addView(mSlideBackIconViewRight);
        }
        container.setOnTouchListener(dispatcher);
    }

    /**
     * 页面销毁时记得解绑
     * 其实就是置空防止内存泄漏
     */
    @SuppressLint("ClickableViewAccessibility")
    void unregister() {
        mActivity = null;
        mCallBack = null;
        mSlideBackIconViewLeft = null;
        mSlideBackIconViewRight = null;
    }

    /**
     * 给根布局包上一层事件拦截处理Layout
     */
    private void addInterceptLayout(ViewGroup decorView, SlideBackInterceptLayout interceptLayout) {
        View rootLayout = decorView.getChildAt(0); // 取出根布局
        decorView.removeView(rootLayout); // 先移除根布局
        // 用事件拦截处理Layout将原根布局包起来，再添加回去
        interceptLayout.addView(rootLayout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        decorView.addView(interceptLayout);
    }

    /**
     * 将根布局还原，移除SlideBackInterceptLayout
     */
    private void removeInterceptLayout(ViewGroup decorView) {
        FrameLayout rootLayout = (FrameLayout) decorView.getChildAt(0); // 取出根布局
        decorView.removeView(rootLayout); // 先移除根布局
        // 将根布局的第一个布局(原根布局)取出放回decorView
        View oriLayout = rootLayout.getChildAt(0);
        rootLayout.removeView(oriLayout);
        decorView.addView(oriLayout);
    }

    /**
     * 给SlideBackIconView设置topMargin，起到定位效果
     *
     * @param view     SlideBackIconView
     * @param position 触点位置
     */
    private void setSlideBackPosition(SlideBackIconView view, int position) {
        // 触点位置减去SlideBackIconView一半高度即为topMargin
        int topMargin = (int) (position - (view.getBackViewHeight() / 2));
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(view.getLayoutParams());
        layoutParams.topMargin = topMargin;
        view.setLayoutParams(layoutParams);
    }

    private float dp2px(float dpValue) {
        return dpValue * mActivity.getResources().getDisplayMetrics().density + 0.5f;
    }

    /**
     * 更新侧滑长度
     *
     * @param isLeft 是否是左侧
     * @param length 长度
     */
    @Override
    public void updateSlideLength(boolean isLeft, float length) {
        if (isLeft) {
            mSlideBackIconViewLeft.updateSlideLength(length);
        } else {
            mSlideBackIconViewRight.updateSlideLength(length);
        }
    }

    /**
     * 更新侧滑位置
     *
     * @param isLeft   是否是左侧
     * @param position 位置
     */
    @Override
    public void updateSlidePosition(boolean isLeft, int position) {
        if (isLeft) {
            setSlideBackPosition(mSlideBackIconViewLeft, position);
        } else {
            setSlideBackPosition(mSlideBackIconViewRight, position);
        }
    }
}