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

package com.xuexiang.xui.widget.imageview.crop;

import android.graphics.Rect;

/**
 * 裁剪窗口上可按可拖动的句柄的枚举
 *
 * @author xuexiang
 * @since 2020/5/13 7:58 PM
 */
public enum Handle {

    /**
     * 边框左侧顶部
     */
    TOP_LEFT(new CornerHandleHelper(Edge.TOP, Edge.LEFT)),
    /**
     * 边框右侧顶部
     */
    TOP_RIGHT(new CornerHandleHelper(Edge.TOP, Edge.RIGHT)),
    /**
     * 边框左侧底部
     */
    BOTTOM_LEFT(new CornerHandleHelper(Edge.BOTTOM, Edge.LEFT)),
    /**
     * 边框右侧底部
     */
    BOTTOM_RIGHT(new CornerHandleHelper(Edge.BOTTOM, Edge.RIGHT)),
    /**
     * 边框左侧
     */
    LEFT(new VerticalHandleHelper(Edge.LEFT)),
    /**
     * 边框顶部
     */
    TOP(new HorizontalHandleHelper(Edge.TOP)),
    /**
     * 边框右侧
     */
    RIGHT(new VerticalHandleHelper(Edge.RIGHT)),
    /**
     * 边框底部
     */
    BOTTOM(new HorizontalHandleHelper(Edge.BOTTOM)),
    /**
     * 中心
     */
    CENTER(new CenterHandleHelper());

    // Member Variables ////////////////////////////////////////////////////////

    private HandleHelper mHelper;

    // Constructors ////////////////////////////////////////////////////////////

    Handle(HandleHelper helper) {
        mHelper = helper;
    }

    // Public Methods //////////////////////////////////////////////////////////

    public void updateCropWindow(float x,
                                 float y,
                                 Rect imageRect,
                                 float snapRadius) {

        mHelper.updateCropWindow(x, y, imageRect, snapRadius);
    }

    public void updateCropWindow(float x,
                                 float y,
                                 float targetAspectRatio,
                                 Rect imageRect,
                                 float snapRadius) {

        mHelper.updateCropWindow(x, y, targetAspectRatio, imageRect, snapRadius);
    }
}
