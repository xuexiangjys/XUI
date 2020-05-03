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

package com.xuexiang.xuidemo.fragment.components;

import android.view.View;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xuidemo.utils.XToastUtils;

import butterknife.BindView;

/**
 * 导航栏使用
 *
 * @author xuexiang
 * @since 2018/11/14 下午3:10
 */
@Page(name = "导航栏", extra = R.drawable.ic_widget_titlebar)
public class TitleBarFragment extends BaseFragment {
    @BindView(R.id.titlebar)
    TitleBar mTitleBar;

    @BindView(R.id.titlebar1)
    TitleBar mTitleBar1;
    @BindView(R.id.titlebar2)
    TitleBar mTitleBar2;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_titlebar;
    }

    @Override
    protected void initViews() {
        mTitleBar.setLeftClickListener(view -> XToastUtils.toast("点击返回")).setCenterClickListener(v -> XToastUtils.toast("点击标题")).addAction(new TitleBar.ImageAction(R.drawable.ic_add_white_24dp) {
            @Override
            public void performAction(View view) {
                XToastUtils.toast("点击更多！");
            }
        });

        mTitleBar1.setLeftClickListener(v -> XToastUtils.toast("点击返回"))
                .addAction(new TitleBar.TextAction("更多") {
                    @Override
                    public void performAction(View view) {
                        XToastUtils.toast("点击更多！");
                    }
                });

        //禁用左侧的图标及文字
        mTitleBar2.disableLeftView()
                .addAction(new TitleBar.ImageAction(R.drawable.ic_navigation_more) {
                    @Override
                    public void performAction(View view) {
                        XToastUtils.toast("点击菜单！");
                    }
                });
    }

}
