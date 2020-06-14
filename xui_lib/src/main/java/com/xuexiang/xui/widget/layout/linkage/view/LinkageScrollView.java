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

package com.xuexiang.xui.widget.layout.linkage.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.widget.NestedScrollView;

import com.xuexiang.xui.widget.layout.linkage.ChildLinkageEvent;
import com.xuexiang.xui.widget.layout.linkage.ILinkageScroll;
import com.xuexiang.xui.widget.layout.linkage.LinkageScrollHandler;

/**
 * 置于联动容器中的ScrollView，业务方需要继承此ScrollView
 *
 * @author xuexiang
 * @since 2020/3/11 7:06 PM
 */
public class LinkageScrollView extends NestedScrollView implements ILinkageScroll {

    /**
     * 联动滚动事件
     */
    private ChildLinkageEvent mLinkageChildrenEvent;

    public LinkageScrollView(Context context) {
        this(context, null);
    }

    public LinkageScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LinkageScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        // 禁止滚动条
        setVerticalScrollBarEnabled(false);

        // 滚动监听，将必要事件传递给联动容器
        if (!canScrollVertically(-1)) {
            if (mLinkageChildrenEvent != null) {
                mLinkageChildrenEvent.onContentScrollToTop(this);
            }
        }

        if (!canScrollVertically(1)) {
            if (mLinkageChildrenEvent != null) {
                mLinkageChildrenEvent.onContentScrollToBottom(this);
            }
        }

        if (mLinkageChildrenEvent != null) {
            mLinkageChildrenEvent.onContentScroll(this);
        }
    }

    @Override
    public void setChildLinkageEvent(ChildLinkageEvent event) {
        mLinkageChildrenEvent = event;

        if (mLinkageChildrenEvent != null) {
            mLinkageChildrenEvent.onContentScroll(this);
        }
    }

    @Override
    public LinkageScrollHandler provideScrollHandler() {
        return new LinkageScrollHandler() {

            @Override
            public void flingContent(View target, int velocityY) {
                LinkageScrollView.this.fling(velocityY);
            }

            @Override
            public void scrollContentToTop() {
                LinkageScrollView.this.scrollTo(0, 0);
            }

            @Override
            public void scrollContentToBottom() {
                LinkageScrollView.this.scrollTo(0, getVerticalScrollRange());
            }

            @Override
            public void stopContentScroll(View target) {
                LinkageScrollView.this.fling(0);
            }

            @Override
            public boolean canScrollVertically(int direction) {
                return LinkageScrollView.this.canScrollVertically(direction);
            }

            @Override
            public boolean isScrollable() {
                return true;
            }

            @SuppressLint("RestrictedApi")
            @Override
            public int getVerticalScrollExtent() {
                return computeVerticalScrollExtent();
            }

            @SuppressLint("RestrictedApi")
            @Override
            public int getVerticalScrollOffset() {
                return computeVerticalScrollOffset();
            }

            @SuppressLint("RestrictedApi")
            @Override
            public int getVerticalScrollRange() {
                return computeVerticalScrollRange();
            }
        };
    }
}
