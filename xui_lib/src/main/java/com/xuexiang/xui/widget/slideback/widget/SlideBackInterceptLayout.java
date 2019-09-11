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

package com.xuexiang.xui.widget.slideback.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * 处理事件拦截的Layout
 *
 * @author xuexiang
 * @since 2019-08-30 9:31
 */
public class SlideBackInterceptLayout extends FrameLayout {

    private float mSideSlideLength = 0; // 边缘滑动响应距离

    public SlideBackInterceptLayout(Context context) {
        this(context, null);
    }

    public SlideBackInterceptLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlideBackInterceptLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return ev.getAction() == MotionEvent.ACTION_DOWN && (ev.getRawX() <= mSideSlideLength || ev.getRawX() >= getWidth() - mSideSlideLength);
    }

    public void setSideSlideLength(float sideSlideLength) {
        mSideSlideLength = sideSlideLength;
    }
}