/*
 * Copyright (C) 2018 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.xuexiang.xui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.xuexiang.xui.R;
import com.xuexiang.xui.utils.DensityUtils;
import com.xuexiang.xui.utils.KeyboardUtils;

/**
 * 监听键盘弹出，自动滚动
 *
 * @author xuexiang
 * @since 2018/12/19 下午11:27
 */
public class XUIKeyboardScrollView extends ScrollView implements KeyboardUtils.SoftKeyboardToggleListener {

    /**
     * 默认键盘弹出时滚动的高度
     */
    private final static int DEFAULT_SCROLL_HEIGHT = 40;
    /**
     * 是否自动滚动，默认false
     */
    private boolean mAutoScroll;
    /**
     * 键盘弹出时滚动的高度
     */
    private int mScrollHeight;

    /**
     * 滚动延迟
     */
    private int mScrollDelay;

    /**
     * 滚动是否隐藏键盘，默认true
     */
    private boolean mScrollHide;

    public XUIKeyboardScrollView(Context context) {
        super(context);
        mScrollHide = true;
    }

    public XUIKeyboardScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    public XUIKeyboardScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    /**
     * 初始化属性
     *
     * @param context
     * @param attrs
     */
    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.XUIKeyboardScrollView);
        if (ta != null) {
            mAutoScroll = ta.getBoolean(R.styleable.XUIKeyboardScrollView_ksv_auto_scroll, false);
            mScrollHeight = ta.getDimensionPixelSize(R.styleable.XUIKeyboardScrollView_ksv_scroll_height, DensityUtils.dp2px(getContext(), DEFAULT_SCROLL_HEIGHT));
            mScrollDelay = ta.getInt(R.styleable.XUIKeyboardScrollView_ksv_scroll_delay, 100);
            mScrollHide = ta.getBoolean(R.styleable.XUIKeyboardScrollView_ksv_scroll_hide, true);
            ta.recycle();
        }
        if (mAutoScroll) {
            KeyboardUtils.addKeyboardToggleListener(this, this);
        }
    }

    @Override
    public void onToggleSoftKeyboard(boolean isVisible) {
        if (isVisible) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    smoothScrollTo(0, getScrollY() + mScrollHeight);
                }
            }, mScrollDelay);
        } else {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    smoothScrollTo(0, 0);
                }
            }, mScrollDelay);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mAutoScroll) {
            KeyboardUtils.removeKeyboardToggleListener(this);
        }
        super.onDetachedFromWindow();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mScrollHide) {
            KeyboardUtils.hideSoftInput(this);
        }
    }

    /**
     * 设置滚动是否隐藏键盘
     *
     * @param isScrollHideKeyboard
     * @return
     */
    public XUIKeyboardScrollView setIsScrollHideKeyboard(boolean isScrollHideKeyboard) {
        mScrollHide = isScrollHideKeyboard;
        return this;
    }
}
