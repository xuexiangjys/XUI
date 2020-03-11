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

import androidx.appcompat.widget.AppCompatTextView;

import com.xuexiang.xui.widget.layout.linkage.ChildLinkageEvent;
import com.xuexiang.xui.widget.layout.linkage.ILinkageScroll;
import com.xuexiang.xui.widget.layout.linkage.LinkageScrollHandler;
import com.xuexiang.xui.widget.layout.linkage.LinkageScrollHandlerAdapter;

/**
 * 置于联动容器的TextView
 *
 * @author xuexiang
 * @since 2020/3/11 7:10 PM
 */
public class LinkageTextView extends AppCompatTextView implements ILinkageScroll {

    public LinkageTextView(Context context) {
        this(context, null);
    }

    public LinkageTextView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public LinkageTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setChildLinkageEvent(ChildLinkageEvent event) {

    }

    @Override
    public LinkageScrollHandler provideScrollHandler() {
        return new LinkageScrollHandlerAdapter() {
            @Override
            public boolean isScrollable() {
                return false;
            }

            @Override
            public int getVerticalScrollExtent() {
                return getHeight();
            }

            @Override
            public int getVerticalScrollOffset() {
                return 0;
            }

            @Override
            public int getVerticalScrollRange() {
                return getHeight();
            }
        };
    }
}
