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

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.xuexiang.xuidemo.R;

import static com.xuexiang.xui.widget.statelayout.StatusLoader.STATUS_CUSTOM;
import static com.xuexiang.xui.widget.statelayout.StatusLoader.STATUS_EMPTY_DATA;
import static com.xuexiang.xui.widget.statelayout.StatusLoader.STATUS_LOADING;
import static com.xuexiang.xui.widget.statelayout.StatusLoader.STATUS_LOAD_FAILED;
import static com.xuexiang.xui.widget.statelayout.StatusLoader.STATUS_LOAD_SUCCESS;

/**
 * 默认状态布局
 *
 * @author xuexiang
 * @since 2020/4/29 1:12 AM
 */
@SuppressLint("ViewConstructor")
public class DefaultStatusView extends LinearLayout implements View.OnClickListener {

    private final OnClickListener mRetryListener;

    public DefaultStatusView(Context context, int status, OnClickListener retryListener) {
        super(context);
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        int layoutId = getLayoutIdByStatus(status);
        if (layoutId != 0) {
            LayoutInflater.from(context).inflate(layoutId, this, true);
        }
        mRetryListener = retryListener;
        setStatus(status);
        setBackgroundColor(0xFFF0F0F0);
    }

    /**
     * 设置状态
     *
     * @param status 状态
     */
    private void setStatus(int status) {
        switch (status) {
            case STATUS_LOAD_SUCCESS:
                setVisibility(View.GONE);
                break;
            case STATUS_LOADING:
                setVisibility(View.VISIBLE);
                break;
            case STATUS_LOAD_FAILED:
            case STATUS_EMPTY_DATA:
            case STATUS_CUSTOM:
                setVisibility(View.VISIBLE);
                setOnClickListener(this);
                break;
            default:
                break;
        }
    }

    private int getLayoutIdByStatus(int status) {
        int layoutId = 0;
        switch (status) {
            case STATUS_LOADING:
                layoutId = R.layout.msv_layout_loading_view;
                break;
            case STATUS_LOAD_FAILED:
                layoutId = R.layout.msv_layout_empty_view;
                break;
            case STATUS_EMPTY_DATA:
                layoutId = R.layout.msv_layout_error_view;
                break;
            case STATUS_CUSTOM:
                layoutId = R.layout.msv_layout_no_network_view;
                break;
            default:
                break;
        }
        return layoutId;
    }


    @Override
    public void onClick(View v) {
        if (mRetryListener != null) {
            mRetryListener.onClick(v);
        }
    }
}
