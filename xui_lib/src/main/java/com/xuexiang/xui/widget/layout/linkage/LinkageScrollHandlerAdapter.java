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

import android.view.View;

/**
 * 提供LinkageScrollHandler接口的缺省实现
 *
 * @author xuexiang
 * @since 2020/3/11 5:46 PM
 */
public class LinkageScrollHandlerAdapter implements LinkageScrollHandler {

    @Override
    public void flingContent(View target, int velocityY) {

    }

    @Override
    public void scrollContentToTop() {

    }

    @Override
    public void scrollContentToBottom() {

    }

    @Override
    public void stopContentScroll(View target) {

    }

    @Override
    public boolean canScrollVertically(int direction) {
        return false;
    }

    @Override
    public boolean isScrollable() {
        return false;
    }

    @Override
    public int getVerticalScrollExtent() {
        return 0;
    }

    @Override
    public int getVerticalScrollOffset() {
        return 0;
    }

    @Override
    public int getVerticalScrollRange() {
        return 0;
    }
}
