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

package com.xuexiang.xuidemo.fragment.components.layout.expandable;

import android.util.Log;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.layout.ExpandableLayout;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xuidemo.utils.XToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

import static com.xuexiang.xui.widget.layout.ExpandableLayout.State.COLLAPSED;
import static com.xuexiang.xui.widget.layout.ExpandableLayout.State.EXPANDED;

/**
 * @author xuexiang
 * @since 2019-11-22 14:21
 */
@Page(name = "可伸缩布局简单使用")
public class ExpandableSimpleFragment extends BaseFragment {

    @BindView(R.id.expandable_layout_1)
    ExpandableLayout expandableLayout1;
    @BindView(R.id.expandable_layout_2)
    ExpandableLayout expandableLayout2;

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_expandable_simple;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        expandableLayout1.setOnExpansionChangedListener((expansion, state) -> Log.d("expandableLayout1", "State: " + state));

        expandableLayout2.setOnExpansionChangedListener((expansion, state) -> {
            if (state == COLLAPSED) {
                XToastUtils.toast("已收起");
            } else if (state == EXPANDED) {
                XToastUtils.toast("已展开");
            }
        });
    }

    @OnClick(R.id.expand_button)
    public void onViewClicked() {
        if (expandableLayout1.isExpanded()) {
            expandableLayout1.collapse();
        } else if (expandableLayout2.isExpanded()) {
            expandableLayout2.collapse();
        } else {
            expandableLayout1.expand();
            expandableLayout2.expand();
        }
    }
}
