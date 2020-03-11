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

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.xuexiang.xui.widget.layout.linkage.ChildLinkageEvent;
import com.xuexiang.xui.widget.layout.linkage.ILinkageScroll;
import com.xuexiang.xui.widget.layout.linkage.LinkageScrollHandler;

/**
 * 置于联动容器的RecyclerView
 *
 * @author xuexiang
 * @since 2020/3/11 6:17 PM
 */
public class LinkageRecyclerView extends RecyclerView implements ILinkageScroll {

    private ChildLinkageEvent mLinkageEvent;

    public LinkageRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public LinkageRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public LinkageRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // 滚动监听，将必要事件传递给联动容器
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (!canScrollVertically(-1)) {
                    if (mLinkageEvent != null) {
                        mLinkageEvent.onContentScrollToTop(LinkageRecyclerView.this);
                    }
                }

                if (!canScrollVertically(1)) {
                    if (mLinkageEvent != null) {
                        mLinkageEvent.onContentScrollToBottom(LinkageRecyclerView.this);
                    }
                }

                if (mLinkageEvent != null) {
                    mLinkageEvent.onContentScroll(LinkageRecyclerView.this);
                }
            }
        });
    }

    @Override
    public void setChildLinkageEvent(ChildLinkageEvent event) {
        mLinkageEvent = event;
    }

    @Override
    public LinkageScrollHandler provideScrollHandler() {
        return new LinkageScrollHandler() {
            @Override
            public void flingContent(View target, int velocityY) {
                LinkageRecyclerView.this.fling(0, velocityY);
            }

            @Override
            public void scrollContentToTop() {
                LinkageRecyclerView.this.scrollToPosition(0);
            }

            @Override
            public void scrollContentToBottom() {
                Adapter adapter = LinkageRecyclerView.this.getAdapter();
                if (adapter != null && adapter.getItemCount() > 0) {
                    LinkageRecyclerView.this.scrollToPosition(adapter.getItemCount() - 1);
                }
            }

            @Override
            public void stopContentScroll(View target) {
                LinkageRecyclerView.this.stopScroll();
            }

            @Override
            public boolean canScrollVertically(int direction) {
                return LinkageRecyclerView.this.canScrollVertically(direction);
            }

            @Override
            public boolean isScrollable() {
                return true;
            }

            @Override
            public int getVerticalScrollExtent() {
                return computeVerticalScrollExtent();
            }

            @Override
            public int getVerticalScrollOffset() {
                return computeVerticalScrollOffset();
            }

            @Override
            public int getVerticalScrollRange() {
                return computeVerticalScrollRange();
            }
        };
    }
}
