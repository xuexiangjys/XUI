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

package com.xuexiang.xui.widget.imageview.preview.enitity;

import android.graphics.Rect;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import java.io.Serializable;

/**
 * 图片预览接口
 *
 * @author xuexiang
 * @since 2018/12/5 上午11:04
 */
public interface IPreviewInfo extends Parcelable {
    /****
     * @return 图片地址
     * ****/
    String getUrl();

    /**
     * 记录坐标
     *
     * @return Rect
     ***/
    Rect getBounds();


    /**
     * 获取视频链接
     ***/
    @Nullable
    String getVideoUrl();


}
