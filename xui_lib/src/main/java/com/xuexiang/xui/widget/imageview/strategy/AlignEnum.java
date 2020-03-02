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

package com.xuexiang.xui.widget.imageview.strategy;

/**
 * 对齐方式
 *
 * @author xuexiang
 * @since 2020/3/2 12:20 PM
 */
public enum AlignEnum {

    /**
     * 默认方式
     */
    DEFAULT,

    /**
     * 中心裁剪（将图片放在ImageView的中心点，然后对图片进行等比例缩放，等比例缩放图片的宽和高均不小于控件对应的宽高）
     */
    CENTER_CROP,

    /**
     * 中心填充（将图片放在ImageView的中心点，然后对图片进行等比例缩放,等比例缩放图片的宽和高均不大于控件对应的宽高）
     */
    CENTER_INSIDE,

    /**
     * 中心适应（将图片放在ImageView的中心点，对图片进行等比例缩放从而完整地显示图片，使得图片的宽高中至少有一个值恰好等于控件的宽或者高）
     */
    FIT_CENTER,

    /**
     * 圆形裁剪
     */
    CIRCLE_CROP,


}
