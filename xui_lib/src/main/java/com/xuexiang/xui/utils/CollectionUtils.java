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

package com.xuexiang.xui.utils;

import androidx.annotation.Nullable;

import java.util.Collection;
import java.util.List;

/**
 * 集合工具类
 *
 * @author xuexiang
 * @since 2020/8/25 11:58 PM
 */
public final class CollectionUtils {

    private CollectionUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 集合的索引是否有效
     *
     * @param collection 集合
     * @param index      索引
     * @return true: 有效，false：无效
     */
    public static <E> boolean isIndexValid(final Collection<E> collection, final int index) {
        return collection != null && index >= 0 && index < collection.size();
    }

    /**
     * 获取集合指定索引的元素
     *
     * @param list  集合
     * @param index 索引
     * @param <E>
     * @return 集合指定索引的元素
     */
    @Nullable
    public static <E> E getListItem(final List<E> list, final int index) {
        return isIndexValid(list, index) ? list.get(index) : null;
    }

    /**
     * 获取集合的大小
     *
     * @param collection 集合
     * @return 集合的大小
     */
    public static <E> int getSize(final Collection<E> collection) {
        return collection != null ? collection.size() : 0;
    }

}
