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

package com.xuexiang.xui.adapter.listview;

/**
 * 集合条目监听
 * @author XUE
 *
 * @param <T>
 */
public interface OnListItemListener<T> {

    /**
     * 点击
     * @param position
     * @param model
     * @param tag
     */
    void onItemClick(int position, T model, int tag);

    /**
     * 长按
     * @param position
     * @param model
     * @param tag
     */
    void onItemLongClick(int position, T model, int tag);
}
