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

package com.xuexiang.xui.widget.popupwindow.status;

/**
 * 状态枚举
 *
 * @author xuexiang
 * @since 2018/12/27 下午5:34
 */
public enum Status {
    /**
     * 无状态
     */
    NONE,
    /**
     * 加载
     */
    LOADING,
    /**
     * 出错
     */
    ERROR,
    /**
     * 完成
     */
    COMPLETE,
    /**
     * 自定义
     */
    CUSTOM
}
