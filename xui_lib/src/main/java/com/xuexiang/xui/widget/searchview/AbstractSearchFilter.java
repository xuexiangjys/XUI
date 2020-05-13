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

package com.xuexiang.xui.widget.searchview;

import android.widget.Filter;

import java.util.List;

/**
 * 抽象的搜索过滤器
 *
 * @author xuexiang
 * @since 2019-12-03 00:04
 */
public abstract class AbstractSearchFilter extends Filter {

    private String[] mSuggestions;

    private OnFilterResultListener mOnFilterResultListener;

    public AbstractSearchFilter setSuggestions(String[] suggestions) {
        mSuggestions = suggestions;
        return this;
    }

    public AbstractSearchFilter setOnFilterResultListener(OnFilterResultListener onFilterResultListener) {
        mOnFilterResultListener = onFilterResultListener;
        return this;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        if (results.values != null && mOnFilterResultListener != null) {
            mOnFilterResultListener.publishResults((List<String>) results.values);
        }
    }

    public String[] getSuggestions() {
        return mSuggestions;
    }

    /**
     * 过滤搜索结果的监听
     */
    public interface OnFilterResultListener {

        /**
         * 过滤结果
         *
         * @param results 搜索结果
         */
        void publishResults(List<String> results);
    }

}
