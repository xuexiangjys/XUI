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

package com.xuexiang.xuidemo.utils.sdkinit;

import com.google.android.cameraview.Size;
import com.google.android.cameraview.strategy.ICameraStrategy;

import java.util.SortedSet;

/**
 * 自适应
 *
 * @author XUE
 * @since 2019/4/8 12:32
 */
public class AutoCameraStrategy implements ICameraStrategy {

    private long mTargetPicturePixels;

    public AutoCameraStrategy(long targetPicturePixels) {
        mTargetPicturePixels = targetPicturePixels;
    }

    /**
     * 找到最合适的尺寸(拍摄的照片）
     *
     * @param sizes
     * @return
     */
    @Override
    public Size chooseOptimalPictureSize(SortedSet<Size> sizes) {
        //从小到大排序
        for (Size size : sizes) {
            //尺寸不要超过指定的PicturePixels.
            if (((long) size.getWidth()) * ((long) size.getHeight()) >= mTargetPicturePixels) {
                return size;
            }
        }
        //找不到就选择最大的尺寸
        return sizes.last();
    }

}
