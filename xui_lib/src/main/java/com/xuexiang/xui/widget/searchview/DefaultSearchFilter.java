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

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 默认的搜索过滤器
 *
 * @author xuexiang
 * @since 2019-12-03 00:00
 */
public class DefaultSearchFilter extends AbstractSearchFilter {

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults filterResults = new FilterResults();
        if (!TextUtils.isEmpty(constraint)) {

            // Retrieve the autocomplete results.
            List<String> searchData = new ArrayList<>();

            for (String suggestion : getSuggestions()) {
                if (filter(suggestion, constraint.toString())) {
                    searchData.add(suggestion);
                }
            }

            // Assign the data to the FilterResults
            filterResults.values = searchData;
            filterResults.count = searchData.size();
        }
        return filterResults;
    }


    /**
     * 过滤搜索条件【以输入为开头的过滤】
     *
     * @param suggestion 目标
     * @param input      输入
     * @return
     */
    protected boolean filter(String suggestion, String input) {
        return suggestion.toLowerCase().startsWith(input.toLowerCase());
    }


}
