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
 * 子布局向 LinkageScrollLayout {@link LinkageScrollLayout} 提供的滚动处理实现接口
 *
 * @author xuexiang
 * @since 2020/3/11 5:46 PM
 */
public interface LinkageScrollHandler {

    /**
     * fling（抛，快速滑动后产生的动作) target with a velocity(速度）
     *
     * @param target
     * @param velocityY
     */
    void flingContent(View target, int velocityY);

    /**
     * 滚动到顶部
     */
    void scrollContentToTop();

    /**
     * 滚动到底部
     */
    void scrollContentToBottom();

    /**
     * 停止滚动
     *
     * @param target
     */
    void stopContentScroll(View target);

    /**
     * 是否可以垂直滚动
     *
     * @param direction
     * @return
     */
    boolean canScrollVertically(int direction);

    /**
     * 子布局是否可滚动
     *
     * @return
     */
    boolean isScrollable();

    /**
     * 获取垂直滚动条的大小
     *
     * @return extent
     */
    int getVerticalScrollExtent();

    /**
     * 获取垂直滚动条在垂直范围内的偏移
     *
     * @return extent
     */
    int getVerticalScrollOffset();

    /**
     * 获取垂直滚动条的滚动范围
     *
     * @return extent
     */
    int getVerticalScrollRange();
}
