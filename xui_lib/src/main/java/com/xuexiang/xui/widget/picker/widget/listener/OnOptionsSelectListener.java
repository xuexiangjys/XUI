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

package com.xuexiang.xui.widget.picker.widget.listener;

import android.view.View;

/**
 * 条件选择的监听器
 *
 * @author xuexiang
 * @since 2019/1/1 下午7:05
 */
public interface OnOptionsSelectListener {

    /**
     * @param view
     * @param options1 选项1
     * @param options2 选项2
     * @param options3 选项3
     * @return true：拦截，不消失；false：不拦截，消失
     */
    boolean onOptionsSelect(View view, int options1, int options2, int options3);

}
