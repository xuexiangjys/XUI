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

package com.xuexiang.xui.widget.picker.widget.utils;

import android.view.Gravity;

import com.xuexiang.xui.R;

/**
 * 选择器动画
 *
 * @author xuexiang
 * @since 2019/1/1 下午7:00
 */
public class PickerViewAnimateUtils {

    private static final int INVALID = -1;

    private PickerViewAnimateUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Get default animation resource when not defined by the user
     *
     * @param gravity       the animGravity of the dialog
     * @param isInAnimation determine if is in or out animation. true when is is
     * @return the id of the animation resource
     */
    public static int getAnimationResource(int gravity, boolean isInAnimation) {
        switch (gravity) {
            case Gravity.BOTTOM:
                return isInAnimation ? R.anim.picker_view_slide_in_bottom : R.anim.picker_view_slide_out_bottom;
        }
        return INVALID;
    }
}
