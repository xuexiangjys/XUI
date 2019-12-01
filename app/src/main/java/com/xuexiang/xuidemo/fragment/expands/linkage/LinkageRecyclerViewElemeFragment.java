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

package com.xuexiang.xuidemo.fragment.expands.linkage;

import android.view.View;
import android.view.ViewGroup;

import com.kunminx.linkage.LinkageRecyclerView;
import com.kunminx.linkage.adapter.viewholder.LinkagePrimaryViewHolder;
import com.kunminx.linkage.adapter.viewholder.LinkageSecondaryViewHolder;
import com.kunminx.linkage.bean.BaseGroupedItem;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.utils.SnackbarUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xuidemo.DemoDataProvider;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xuidemo.fragment.expands.linkage.custom.CustomLinkagePrimaryAdapterConfig;
import com.xuexiang.xuidemo.fragment.expands.linkage.eleme.ElemeGroupedItem;
import com.xuexiang.xuidemo.fragment.expands.linkage.eleme.ElemeSecondaryAdapterConfig;

import butterknife.BindView;

/**
 * @author xuexiang
 * @since 2019-11-25 23:20
 */
@Page(name = "仿饿了么双列表联动菜单")
public class LinkageRecyclerViewElemeFragment extends BaseFragment implements CustomLinkagePrimaryAdapterConfig.OnPrimaryItemClickListener, ElemeSecondaryAdapterConfig.OnSecondaryItemClickListener {

    @BindView(R.id.linkage)
    LinkageRecyclerView linkage;

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_linkage_recyclerview;
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.addAction(new TitleBar.TextAction("切换") {
            @SingleClick
            @Override
            public void performAction(View view) {
                if (linkage != null) {
                    linkage.setGridMode(!linkage.isGridMode());
                }
            }
        });
        return titleBar;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        linkage.init(DemoDataProvider.getElemeGroupItems(), new CustomLinkagePrimaryAdapterConfig(this), new ElemeSecondaryAdapterConfig(this));
    }

    @Override
    public void onPrimaryItemClick(LinkagePrimaryViewHolder holder, View view, String title) {
        SnackbarUtils.Short(view, title).show();
    }

    @Override
    public void onSecondaryItemClick(LinkageSecondaryViewHolder holder, ViewGroup view, BaseGroupedItem<ElemeGroupedItem.ItemInfo> item) {
        SnackbarUtils.Short(view, item.info.getTitle()).show();
    }

    @Override
    public void onGoodAdd(View view, BaseGroupedItem<ElemeGroupedItem.ItemInfo> item) {
        SnackbarUtils.Short(view, "添加：" + item.info.getTitle()).show();
    }
}
