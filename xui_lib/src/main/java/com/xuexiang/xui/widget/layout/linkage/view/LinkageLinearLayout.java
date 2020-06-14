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
import android.widget.LinearLayout;

import com.xuexiang.xui.widget.layout.linkage.ChildLinkageEvent;
import com.xuexiang.xui.widget.layout.linkage.ILinkageScroll;
import com.xuexiang.xui.widget.layout.linkage.LinkageScrollHandler;
import com.xuexiang.xui.widget.layout.linkage.LinkageScrollHandlerAdapter;

/**
 * 置于联动容器的LinearLayout
 *
 * @author xuexiang
 * @since 2020/3/11 6:14 PM
 */
public class LinkageLinearLayout extends LinearLayout implements ILinkageScroll {

    public LinkageLinearLayout(Context context) {
        this(context, null);
    }

    public LinkageLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public LinkageLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setChildLinkageEvent(ChildLinkageEvent event) {
        //因为不可滚动，所以无需实现
    }

    @Override
    public LinkageScrollHandler provideScrollHandler() {
        return new LinkageScrollHandlerAdapter() {
            @Override
            public boolean isScrollable() {
                //不可滚动
                return false;
            }

            @Override
            public int getVerticalScrollExtent() {
                return getHeight();
            }

            @Override
            public int getVerticalScrollOffset() {
                //无滚动条
                return 0;
            }

            @Override
            public int getVerticalScrollRange() {
                return getHeight();
            }
        };
    }
}
