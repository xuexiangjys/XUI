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

package com.xuexiang.xui.widget.slideback.dispatcher;

import android.view.View;

import android.support.annotation.NonNull;

import com.xuexiang.xui.widget.slideback.SlideInfo;
import com.xuexiang.xui.widget.slideback.callback.SlideCallBack;

/**
 * 侧滑触摸事件分发器
 *
 * @author xuexiang
 * @since 2019-08-30 15:42
 */
public interface ISlideTouchEventDispatcher extends View.OnTouchListener {

    /**
     * 初始化分发器
     *
     * @param slideInfo 侧滑信息
     * @param callBack  侧滑事件回调
     * @param listener  侧滑更新监听
     * @return
     */
    ISlideTouchEventDispatcher init(@NonNull SlideInfo slideInfo, @NonNull SlideCallBack callBack, @NonNull OnSlideUpdateListener listener);

    /**
     * 更新侧滑长度
     *
     * @param isLeft 是否是左侧
     * @param length 长度
     */
    void updateSlideLength(boolean isLeft, float length);

    /**
     * 更新侧滑位置
     *
     * @param isLeft   是否是左侧
     * @param position 位置
     */
    void updateSlidePosition(boolean isLeft, int position);
}
