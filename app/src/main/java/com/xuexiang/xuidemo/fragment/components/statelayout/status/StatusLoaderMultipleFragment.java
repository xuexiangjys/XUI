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

import android.view.View;
import android.widget.LinearLayout;

import com.github.clans.fab.FloatingActionMenu;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.statelayout.StatusLoader;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.fragment.components.statelayout.status.adapter.DefaultStatusAdapter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author xuexiang
 * @since 2020/4/29 12:34 AM
 */
@Page(name = "StatusLoader多状态组件使用")
public class StatusLoaderMultipleFragment extends BaseStatusLoaderFragment {

    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.fab_menu)
    FloatingActionMenu mFloatingActionMenu;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_status_loader;
    }

    @Override
    protected void initViews() {
        showLoading();
    }

    @SingleClick
    @OnClick({R.id.fab_loading, R.id.fab_empty, R.id.fab_error, R.id.fab_no_network, R.id.fab_content})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_loading:
                showLoading();
                break;
            case R.id.fab_empty:
                showEmpty();
                break;
            case R.id.fab_error:
                showError();
                break;
            case R.id.fab_no_network:
                showCustom();
                break;
            case R.id.fab_content:
                showContent();
                break;
            default:
                break;
        }
        mFloatingActionMenu.toggle(false);
    }


    @Override
    protected View getWrapView() {
        return llContent;
    }

    @Override
    protected StatusLoader.Adapter getStatusLoaderAdapter() {
        return new DefaultStatusAdapter();
    }
}
