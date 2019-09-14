/*
 * Copyright (C) 2018 xuexiangjys(xuexiangjys@163.com)
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

package com.xuexiang.xui.widget.imageview.preview.loader;


import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;

/**
 * 图片加载回调状态接口
 *
 * @author xuexiang
 * @since 2018/12/5 上午11:08
 */
public interface ISimpleTarget {
   /**
     * Callback when an image has been successfully loaded.
     * <p>
     * <strong>Note:</strong> You must not recycle the bitmap.
     *
     */
    void onResourceReady();

    /**
     * Callback indicating the image could not be successfully loaded.
     *
     * @param errorRes 内容
     */
    void onLoadFailed(@Nullable Drawable errorRes);


}
