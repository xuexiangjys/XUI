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

package com.xuexiang.xui.widget.slideback.callback;

/**
 * 侧滑事件监听（可监听是左滑还是右滑）
 *
 * @author xuexiang
 * @since 2019-08-30 9:32
 */
public abstract class SlideCallBack implements SlideBackCallBack {
    private SlideBackCallBack mCallback;

    public SlideCallBack() {
    }

    public SlideCallBack(SlideBackCallBack callBack) {
        mCallback = callBack;
    }

    @Override
    public void onSlideBack() {
        if (null != mCallback) {
            mCallback.onSlideBack();
        }
    }

    /**
     * 滑动来源： <br>
     * EDGE_LEFT    左侧侧滑 <br>
     * EDGE_RIGHT   右侧侧滑 <br>
     */
    public abstract void onSlide(int edgeFrom);
}