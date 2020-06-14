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

/**
 * LinkageScrollLayout {@link LinkageScrollLayout}的所有子布局必须要实现的接口
 *
 * @author xuexiang
 * @since 2020/3/11 4:57 PM
 */
public interface ILinkageScroll {
    /**
     * 设置内联事件的回调接口
     *
     * @param event ChildLinkageEvent that the top/bottom view holds
     */
    void setChildLinkageEvent(ChildLinkageEvent event);

    /**
     * 获取子布局向 LinkageScrollLayout {@link LinkageScrollLayout} 提供的滚动处理接口
     */
    LinkageScrollHandler provideScrollHandler();
}
