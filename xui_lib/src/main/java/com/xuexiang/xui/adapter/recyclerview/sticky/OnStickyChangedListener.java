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

package com.xuexiang.xui.adapter.recyclerview.sticky;

/**
 * 粘顶布局滚动变化监听
 *
 * @author xuexiang
 * @since 2020/5/2 11:22 AM
 */
public interface OnStickyChangedListener {
    /**
     * 滚动中
     *
     * @param offset 滚动偏移
     */
    void onScrolling(int offset);

    /**
     * 不可见
     */
    void onInVisible();

    /**
     * 当高度不够滑动切换时
     */
    void onNotEnoughHighScroll();
}