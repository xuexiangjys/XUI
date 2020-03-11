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
 * LinkageScrollLayout {@link LinkageScrollLayout}的子布局内联事件的回调接口
 *
 * @author xuexiang
 * @since 2020/3/11 4:47 PM
 */
public interface ChildLinkageEvent {
    /**
     * 当子控件滑动至其顶部，通知LinkageScrollLayout
     *
     * @param target
     */
    void onContentScrollToTop(View target);

    /**
     * 当子控件滑动至其底部，通知LinkageScrollLayout
     *
     * @param target
     */
    void onContentScrollToBottom(View target);

    /**
     * 当子控件在滑动时，通知LinkageScrollLayout
     *
     * @param target
     */
    void onContentScroll(View target);
}
