/*
 * Copyright (C) 2019 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xuexiang.xui.widget.picker.widget.adapter;

import com.xuexiang.xui.widget.picker.wheelview.adapter.WheelAdapter;

import java.util.List;

/**
 * 简单的数组滚轮适配器
 *
 * @author xuexiang
 * @since 2019/1/1 下午6:34
 */
public class ArrayWheelAdapter<T> implements WheelAdapter {

    private List<T> mItems;

    /**
     * Constructor
     *
     * @param items the mItems
     */
    public ArrayWheelAdapter(List<T> items) {
       mItems = items;
    }

    @Override
    public Object getItem(int index) {
        if (index >= 0 && index < mItems.size()) {
            return mItems.get(index);
        }
        return "";
    }

    @Override
    public int getItemsCount() {
        return mItems.size();
    }

    @Override
    public int indexOf(Object o) {
        return mItems.indexOf(o);
    }

}
