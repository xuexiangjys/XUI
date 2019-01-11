/*
 * Copyright (C) 2018 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.xuexiang.xui.widget.progress.loading;

/**
 * 消息loading加载者
 *
 * @author xuexiang
 * @since 2019/1/11 下午3:58
 */
public interface IMessageLoader {

    /**
     * 更新提示信息
     *
     * @param tipMessage
     * @return
     */
    void updateMessage(String tipMessage);

    /**
     * 更新提示信息
     *
     * @param tipMessageId
     * @return
     */
    void updateMessage(int tipMessageId);

    /**
     * 显示加载
     */
    void show();

    /**
     * 隐藏加载
     */
    void dismiss();

    /**
     * 资源释放
     */
    void recycle();

    /**
     * 是否在加载
     *
     * @return
     */
    boolean isLoading();

    /**
     * 设置是否可取消
     *
     * @param cancelable
     */
    void setCancelable(boolean cancelable);

    /**
     * 设置取消的回掉监听
     *
     * @param listener
     */
    void setLoadingCancelListener(LoadingCancelListener listener);
}
