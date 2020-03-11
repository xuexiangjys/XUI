/*
 * Copyright (C) 2020 xuexiangjys(xuexiangjys@163.com)
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

package com.xuexiang.xui.widget.layout.linkage;

import android.graphics.PointF;
import android.view.MotionEvent;

import com.xuexiang.xui.logs.UILog;

/**
 * 跟踪触摸事件位置的辅助类
 *
 * @author xuexiang
 * @since 2020/3/11 6:12 PM
 */
public class PosIndicator {
    /**
     * 手指滑动方向
     */
    private static int SCROLL_ORIENTATION_NONE = -1;
    /**
     * 手指滑动方向 -- 垂直
     */
    private static int SCROLL_ORIENTATION_VERTICAL = 0;
    /**
     * 手指滑动方向 -- 水平
     */
    private static int SCROLL_ORIENTATION_HORIZONTAL = 1;
    /**
     * TAG
     */
    public static final String TAG = "PosIndicator";
    /**
     * 初始位置，在的onSizeChanged()中确定
     */
    private int mStartPos = 0;
    /**
     * 结束位置，在的onSizeChanged()中确定
     */
    public int mEndPos = 0;
    /**
     * 手指移动坐标
     */
    private PointF mLastMovePoint = new PointF();
    /**
     * 手指按下坐标
     */
    private PointF mDownPoint = new PointF();
    /**
     * 手指释放坐标
     */
    private PointF mReleasePoint = new PointF();
    /**
     * 联动容器中，子view的当前位置: [mStartPos, mEndPos]
     */
    private int mCurrentPos;
    /**
     * 联动容器中，子view的上次位置: [mStartPos, mEndPos]
     */
    private int mLastPos;
    /**
     * 手指按下时，联动容器中，子view的位置: [mStartPos, mEndPos]
     */
    private int mPressedPos;
    /**
     * 手指move的offset
     */
    private float mOffsetX;
    /**
     * 手指move的offset
     */
    private float mOffsetY;
    /**
     * 手指move后离按下点的distance
     */
    private float mDistanceToDownX;
    /**
     * 手指move后离按下点的distance
     */
    private float mDistanceToDownY;
    /**
     * 是否处于touch中
     */
    private boolean mIsUnderTouch;
    /**
     * flag：横竖屏切换
     */
    private boolean mConfigurationHasChanged;
    /**
     * 当前mCurrentPos在mStartPos和mEndPos之间的比例值
     */
    private float mSaveCurrentPosRatio;
    /**
     * MotionEvent.XXX_ACTION
     */
    private int mTouchAction = -1;
    /**
     * touch slop
     */
    private int mTouchSlop;
    /**
     * 是否处于拖拽状态
     */
    private boolean mIsDragging;
    /**
     * 手指滑动方向
     */
    private int mScrollOrientation = SCROLL_ORIENTATION_NONE;

    /**
     * 初始化Layout Container 位置信息
     *
     * @param startPos
     * @param endPos
     */
    public void initStartAndEndPos(int startPos, int endPos) {
        mStartPos = startPos;
        mEndPos = endPos;
    }

    /**
     * 跟踪手指坐标： 按下
     *
     * @param x
     * @param y
     */
    public void onDown(float x, float y) {
        mIsUnderTouch = true;
        mPressedPos = mCurrentPos;
        mLastMovePoint.set(x, y);
        mDownPoint.set(x, y);
        mTouchAction = MotionEvent.ACTION_DOWN;
    }

    /**
     * 跟踪手指坐标： 移动
     *
     * @param x
     * @param y
     */
    public void onMove(float x, float y) {
        float offsetX = x - mLastMovePoint.x;
        float offsetY = y - mLastMovePoint.y;
        if (!mIsDragging && Math.abs(offsetY) > mTouchSlop) {
            mIsDragging = true;
            if (offsetY < 0) {
                offsetY += mTouchSlop;
            } else {
                offsetY -= mTouchSlop;
            }
            mScrollOrientation = SCROLL_ORIENTATION_VERTICAL;
        }
        if (!mIsDragging && Math.abs(offsetX) > mTouchSlop) {
            mIsDragging = true;
            if (offsetX < 0) {
                offsetX += mTouchSlop;
            } else {
                offsetX -= mTouchSlop;
            }
            mScrollOrientation = SCROLL_ORIENTATION_HORIZONTAL;
        }
        if (mIsDragging) {
            setOffset(offsetX, offsetY);
            setDistanceToDown(x, y);
            mLastMovePoint.set(x, y);
            mTouchAction = MotionEvent.ACTION_MOVE;
        }
    }

    /**
     * 是否是垂直滑动
     *
     * @return
     */
    public boolean isScrollVertical() {
        return mScrollOrientation == SCROLL_ORIENTATION_VERTICAL;
    }

    /**
     * 是否是水平滑动
     *
     * @return
     */
    public boolean isScrollHorizontal() {
        return mScrollOrientation == SCROLL_ORIENTATION_HORIZONTAL;
    }

    /**
     * 手指上滑
     *
     * @return
     */
    public boolean isMoveUp() {
        return getOffsetY() < 0;
    }

    /**
     * 手指下滑
     *
     * @return
     */
    public boolean isMoveDown() {
        return getOffsetY() > 0;
    }

    /**
     * 是否处于dragging状态
     *
     * @return
     */
    public boolean isDragging() {
        return mIsDragging;
    }

    /**
     * 跟踪手指坐标： 抬起
     *
     * @param x
     * @param y
     */
    public void onRelease(float x, float y) {
        mIsUnderTouch = false;
        mIsDragging = false;
        mReleasePoint.set(x, y);
        mTouchAction = MotionEvent.ACTION_UP;
        mScrollOrientation = SCROLL_ORIENTATION_NONE;
    }

    public void onPointerDown(float x, float y) {
        mLastMovePoint.set(x, y);
    }

    public void onPointerUp(float x, float y) {
        mLastMovePoint.set(x, y);
    }

    /**
     * 获取手指按下点坐标
     *
     * @return
     */
    public PointF getFingerDownPoint() {
        return mDownPoint;
    }

    /**
     * 获取手指move坐标
     *
     * @return
     */
    public PointF getFingerMovePoint() {
        return mLastMovePoint;
    }

    /**
     * 获取手指抬起坐标
     *
     * @return
     */
    public PointF getFingerReleasePoint() {
        return mReleasePoint;
    }

    /**
     * 获取TouchAction
     *
     * @return {MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE, MotionEvent.ACTION_UP}
     */
    public int getTouchAction() {
        return mTouchAction;
    }

    /**
     * 设置touch slop
     *
     * @param touchSlop
     */
    public void setTouchSlop(int touchSlop) {
        this.mTouchSlop = touchSlop;
    }

    /**
     * 获取touch slop
     *
     * @return
     */
    public int getTouchSlop() {
        return mTouchSlop;
    }

    private void setDistanceToDown(float x, float y) {
        mDistanceToDownX = x - mDownPoint.x;
        mDistanceToDownY = y - mDownPoint.y;
    }

    /**
     * 手指是否touch中
     *
     * @return
     */
    public boolean isUnderTouch() {
        return mIsUnderTouch;
    }

    /**
     * 按下后，联动容器{@link }的位置是否发生了改变
     *
     * @return
     */
    public boolean hasMovedAfterPressedDown() {
        return mCurrentPos != mPressedPos;
    }

    private void setOffset(float x, float y) {
        mOffsetX = x;
        mOffsetY = y;
    }

    /**
     * 手指Move的偏移量
     *
     * @return
     */
    public float getOffsetX() {
        return mOffsetX;
    }

    /**
     * 手指Move的偏移量
     *
     * @return
     */
    public float getOffsetY() {
        return mOffsetY;
    }

    /**
     * 获取距离手指按下点的distance
     *
     * @return
     */
    public float getDistanceToDownX() {
        return mDistanceToDownX;
    }

    /**
     * 获取距离手指按下点的distance
     *
     * @return
     */
    public float getDistanceToDownY() {
        return mDistanceToDownY;
    }

    /**
     * 联动容器{@link }是否处于起始位置
     *
     * @return
     */
    public boolean isInStartPos() {
        return mCurrentPos == mStartPos;
    }

    /**
     * 联动容器{@link }是否处于结束位置
     *
     * @return
     */
    public boolean isInEndPos() {
        return mCurrentPos == mEndPos;
    }

    /**
     * 联动容器{@link }已经离开StartPosition
     *
     * @return
     */
    public boolean hasLeftStartPos() {
        return mCurrentPos > mStartPos;
    }

    /**
     * 联动容器{@link }刚刚离开StartPosition
     *
     * @return
     */
    public boolean hasJustLeftStartPos() {
        return mLastPos == mStartPos && hasLeftStartPos();
    }

    /**
     * 联动容器{@link }刚刚回到StartPosition
     *
     * @return
     */
    public boolean hasJustBackStartPos() {
        return mLastPos != mStartPos && isInStartPos();
    }

    /**
     * 联动容器{@link }离开EndPosition位置
     *
     * @return
     */
    public boolean hasLeftEndPos() {
        return mCurrentPos < mEndPos;
    }

    /**
     * 联动容器{@link }的位置刚刚离开终点位置
     *
     * @return
     */
    public boolean hasJustLeftEndPos() {
        return mLastPos == mEndPos && hasLeftEndPos();
    }

    /**
     * 当前位置刚刚到达终点位置
     *
     * @return
     */
    public boolean hasJustBackEndPos() {
        return mLastPos != mEndPos && isInEndPos();
    }

    /**
     * position 边界检查，保证容器位置的可靠性。
     *
     * @return
     */
    public int checkPosBoundary(int to) {
        to = Math.max(to, mStartPos);
        to = Math.min(to, mEndPos);

        return to;
    }

    public void setCurrentPos(int currentPos) {
        mLastPos = mCurrentPos;
        mCurrentPos = currentPos;
    }

    public int getCurrentPos() {
        return mCurrentPos;
    }

    public int getLastPos() {
        return mLastPos;
    }

    public boolean willOverStartPos() {
        int to = (int) (mCurrentPos - mOffsetY);
        return to < mStartPos;
    }

    public boolean willOverEndPos() {
        int to = (int) (mCurrentPos - mOffsetY);
        return to > mEndPos;
    }

    /**
     * @return
     */
    public int getStartPos() {
        return mStartPos;
    }

    /**
     * 获取联动容器{@link }的终点位置.
     *
     * @return
     */
    public int getEndPos() {
        return mEndPos;
    }

    /**
     * 获取联动容器{@link }当前位置相对于mStartPos的距离。
     *
     * @return
     */
    public int getPosDistanceFromStart() {
        return mCurrentPos - mStartPos;
    }

    /**
     * 获取联动容器{@link }位置的偏移量，相对于上次移动的位置：mLastPos
     *
     * @return
     */
    public int getPosOffset() {
        return mCurrentPos - mLastPos;
    }


    /**
     * 记录横竖屏切换时当前Position的比例值。用于横竖屏切换后位置信息的恢复。
     */
    public void savePosOnConfigurationChanged() {
        mSaveCurrentPosRatio =
                (mCurrentPos - mStartPos) * 1.0f / (mEndPos - mStartPos);
        // 保留一位小数
        mSaveCurrentPosRatio = Math.round(mSaveCurrentPosRatio * 10) * 1.0f / 10;
        UILog.dTag(TAG, "savePosOnConfigurationChanged, mSaveCurrentPosRation: " + mSaveCurrentPosRatio);

    }

    /**
     * 保存position
     */
    private void restorePosIfNeeded() {
        if (mConfigurationHasChanged) {
            mCurrentPos =
                    (int) (mStartPos + ((mEndPos - mStartPos) * mSaveCurrentPosRatio));
            UILog.dTag(TAG, "Need restore current pos, mCurrentPos: " + mCurrentPos);
        }
    }


    @Override
    public String toString() {
        return "mCurrentPos: " + mCurrentPos
                + ", mLastPos: " + mLastPos
                + ", mPressedPos: " + mPressedPos
                + ", isInStartPos: " + this.isInStartPos()
                + ", isInEndPos: " + this.isInEndPos();
    }

}
