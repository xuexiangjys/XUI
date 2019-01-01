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

package com.xuexiang.xui.widget.picker.widget.configure;

/**
 * 时间选择器的类型<br>
 * <p>
 * 分别控制“年”“月”“日”“时”“分”“秒”的显示或隐藏。
 *
 * @author xuexiang
 * @since 2019/1/1 下午8:58
 */
public enum TimePickerType {

    /**
     * 显示“年”“月”“日”
     */
    DEFAULT(new boolean[]{true, true, true, false, false, false}),
    /**
     * 显示“年”“月”“日”“时”“分”“秒”
     */
    ALL(new boolean[]{true, true, true, true, true, true}),
    /**
     * 显示“时”“分”“秒”
     */
    TIME(new boolean[]{false, false, false, true, true, true}),
    /**
     * 显示“年”“月”“日”“时”“分”
     */
    DATE(new boolean[]{true, true, true, true, true, false});


    private final boolean[] mType;

    TimePickerType(boolean[] type) {
        mType = type;
    }

    public boolean[] getType() {
        return mType;
    }
}

