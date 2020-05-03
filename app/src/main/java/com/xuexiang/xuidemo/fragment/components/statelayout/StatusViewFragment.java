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

package com.xuexiang.xuidemo.fragment.components.statelayout;

import android.view.View;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.popupwindow.status.Status;
import com.xuexiang.xui.widget.popupwindow.status.StatusView;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xuidemo.utils.XToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author xuexiang
 * @since 2018/12/27 下午6:14
 */
@Page(name = "StatusView\n状态提示")
public class StatusViewFragment extends BaseFragment {

    @BindView(R.id.status)
    StatusView mStatusView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_status_view;
    }

    @Override
    protected void initViews() {
        mStatusView.setOnErrorClickListener(v -> {
            XToastUtils.toast("点击错误状态视图");
            mStatusView.dismiss();
        });

        mStatusView.setOnLoadingClickListener(v -> {
            XToastUtils.toast("点击正在加载状态视图");
            mStatusView.dismiss();
        });

        mStatusView.setOnCustomClickListener(v -> {
            XToastUtils.toast("点击自定义状态视图");
            mStatusView.dismiss();
        });
    }


    @OnClick({R.id.complete, R.id.error, R.id.loading, R.id.none, R.id.custom})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.complete:
                mStatusView.setStatus(Status.COMPLETE);
                break;
            case R.id.error:
                mStatusView.setStatus(Status.ERROR);
                break;
            case R.id.loading:
                mStatusView.setStatus(Status.LOADING);
                break;
            case R.id.none:
                mStatusView.setStatus(Status.NONE);
                break;
            case R.id.custom:
                mStatusView.setStatus(Status.CUSTOM);
                break;
            default:
                break;
        }
    }

}
