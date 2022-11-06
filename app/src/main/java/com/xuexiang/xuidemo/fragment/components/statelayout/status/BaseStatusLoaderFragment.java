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

package com.xuexiang.xuidemo.fragment.components.statelayout.status;

import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.xuexiang.xui.widget.statelayout.StatusLoader;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xui.utils.XToastUtils;

import static com.xuexiang.xui.widget.statelayout.StatusLoader.STATUS_LOADING;

/**
 * 基础StatusLoader页
 *
 * @author xuexiang
 * @since 2020/4/29 11:57 PM
 */
public abstract class BaseStatusLoaderFragment extends BaseFragment {

    private Handler mLoadingHandler = new Handler(Looper.getMainLooper());

    @Override
    public void onDestroyView() {
        mLoadingHandler.removeCallbacksAndMessages(null);
        super.onDestroyView();
    }

    //=============StatusLoader================//

    protected StatusLoader.Holder mHolder;

    protected void initLoadingStatusViewIfNeed() {
        if (mHolder == null) {
            //此处设置需要包裹的布局
            mHolder = StatusLoader.from(getStatusLoaderAdapter())
                    .wrap(getWrapView())
                    .withRetry(this::onLoadRetry);
        }
    }


    /**
     * 获取被包裹的内容组件
     *
     * @return 被包裹的内容组件
     */
    protected abstract View getWrapView();

    /**
     * 获取状态加载适配器
     *
     * @return 状态加载适配器
     */
    protected abstract StatusLoader.Adapter getStatusLoaderAdapter();

    /**
     * 重试
     */
    protected void onLoadRetry(View view) {
        XToastUtils.toast("点击重试");
        showLoading();
    }

    /**
     * 显示加载页面
     */
    protected void showLoading() {
        initLoadingStatusViewIfNeed();
        // 模拟加载
        if (mHolder.getCurState() != STATUS_LOADING) {
            mLoadingHandler.postDelayed(() -> {
                if (mHolder.getCurState() == STATUS_LOADING) {
                    showContent();
                }
            }, 3000);
        }
        mHolder.showLoading();
    }

    /**
     * 显示内容
     */
    protected void showContent() {
        initLoadingStatusViewIfNeed();
        mHolder.showLoadSuccess();
    }

    /**
     * 显示出错页面
     */
    protected void showError() {
        initLoadingStatusViewIfNeed();
        mHolder.showLoadFailed();
    }

    /**
     * 显示空页面
     */
    protected void showEmpty() {
        initLoadingStatusViewIfNeed();
        mHolder.showEmpty();
    }

    /**
     * 显示自定义布局页面
     */
    protected void showCustom() {
        initLoadingStatusViewIfNeed();
        mHolder.showCustom();
    }

}
