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

/**
 * 侧滑信息
 *
 * @author xuexiang
 * @since 2019-08-30 11:13
 */
public class SlideInfo {

    /**
     * 控件高度
     */
    private float mBackViewHeight;
    /**
     * 箭头图标大小
     */
    private float mArrowSize;
    /**
     * 最大拉动距离
     */
    private float mMaxSlideLength;

    /**
     * 侧滑响应距离
     */
    private float mSideSlideLength;
    /**
     * 阻尼系数
     */
    private float mDragRate;

    /**
     * 使用左侧侧滑
     */
    private boolean mIsAllowEdgeLeft;
    /**
     * 使用右侧侧滑
     */
    private boolean mIsAllowEdgeRight;

    /**
     * 屏幕宽
     */
    private float mScreenWidth;

    public float getBackViewHeight() {
        return mBackViewHeight;
    }

    public SlideInfo setBackViewHeight(float backViewHeight) {
        mBackViewHeight = backViewHeight;
        return this;
    }

    public float getArrowSize() {
        return mArrowSize;
    }

    public SlideInfo setArrowSize(float arrowSize) {
        mArrowSize = arrowSize;
        return this;
    }

    public float getMaxSlideLength() {
        return mMaxSlideLength;
    }

    public SlideInfo setMaxSlideLength(float maxSlideLength) {
        mMaxSlideLength = maxSlideLength;
        return this;
    }

    public float getSideSlideLength() {
        return mSideSlideLength;
    }

    public SlideInfo setSideSlideLength(float sideSlideLength) {
        mSideSlideLength = sideSlideLength;
        return this;
    }

    public float getDragRate() {
        return mDragRate;
    }

    public SlideInfo setDragRate(float dragRate) {
        mDragRate = dragRate;
        return this;
    }

    public boolean isAllowEdgeLeft() {
        return mIsAllowEdgeLeft;
    }

    public SlideInfo setAllowEdgeLeft(boolean allowEdgeLeft) {
        mIsAllowEdgeLeft = allowEdgeLeft;
        return this;
    }

    public boolean isAllowEdgeRight() {
        return mIsAllowEdgeRight;
    }

    public SlideInfo setAllowEdgeRight(boolean allowEdgeRight) {
        mIsAllowEdgeRight = allowEdgeRight;
        return this;
    }

    public SlideInfo setEdgeMode(boolean allowEdgeLeft, boolean allowEdgeRight) {
        mIsAllowEdgeLeft = allowEdgeLeft;
        mIsAllowEdgeRight = allowEdgeRight;
        return this;
    }

    public float getScreenWidth() {
        return mScreenWidth;
    }

    public SlideInfo setScreenWidth(float screenWidth) {
        mScreenWidth = screenWidth;
        return this;
    }

    @Override
    public String toString() {
        return "SlideInfo{" +
                "mBackViewHeight=" + mBackViewHeight +
                ", mArrowSize=" + mArrowSize +
                ", mMaxSlideLength=" + mMaxSlideLength +
                ", mSideSlideLength=" + mSideSlideLength +
                ", mDragRate=" + mDragRate +
                ", mIsAllowEdgeLeft=" + mIsAllowEdgeLeft +
                ", mIsAllowEdgeRight=" + mIsAllowEdgeRight +
                ", mScreenWidth=" + mScreenWidth +
                '}';
    }
}
