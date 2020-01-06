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

package com.xuexiang.xui.widget.button.shinebutton;

import android.animation.ValueAnimator;

import com.xuexiang.xui.widget.button.shinebutton.interpolator.Ease;
import com.xuexiang.xui.widget.button.shinebutton.interpolator.EasingInterpolator;

/**
 * @author xuexiang
 * @since 2020-01-06 11:55
 */
public class ShineAnimator extends ValueAnimator {

    private static final float DEFAULT_MAX_VALUE = 1.5f;
    private static final long DEFAULT_ANIM_DURATION = 1500;

    public ShineAnimator() {
        setFloatValues(1f, DEFAULT_MAX_VALUE);
        setDuration(DEFAULT_ANIM_DURATION);
        setStartDelay(200);
        setInterpolator(new EasingInterpolator(Ease.QUART_OUT));
    }

    public ShineAnimator(long duration, float maxValue, long delay) {
        setFloatValues(1f, maxValue);
        setDuration(duration);
        setStartDelay(delay);
        setInterpolator(new EasingInterpolator(Ease.QUART_OUT));
    }
}
