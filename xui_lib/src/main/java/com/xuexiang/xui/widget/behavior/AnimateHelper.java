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

package com.xuexiang.xui.widget.behavior;

/**
 * @author xuexiang
 * @since 2019-05-10 01:03
 */
public interface AnimateHelper {
    /**
     * 显示
     */
    int STATE_SHOW = 1;
    /**
     * 隐藏
     */
    int STATE_HIDE = 0;

    void show();

    void hide();

    void setStartY(float y);

    /**
     * 设置当前的模式
     * @param mode
     */
    void setMode(int mode);

    /**
     * 获取当前的状态
     * @return
     */
    int getState();
}
