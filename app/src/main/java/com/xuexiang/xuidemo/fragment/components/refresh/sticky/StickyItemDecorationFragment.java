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

package com.xuexiang.xuidemo.fragment.components.refresh.sticky;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.adapter.recyclerview.sticky.StickyHeadContainer;
import com.xuexiang.xui.adapter.recyclerview.sticky.StickyItemDecoration;
import com.xuexiang.xui.utils.WidgetUtils;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xuidemo.DemoDataProvider;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.adapter.StickyListAdapter;
import com.xuexiang.xuidemo.adapter.entity.StickyItem;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xuidemo.utils.Utils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author xuexiang
 * @since 2020/5/2 10:59 AM
 */
@Page(name = "StickyItemDecoration\n通过装饰实现粘顶效果")
public class StickyItemDecorationFragment extends BaseFragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.sticky_container)
    StickyHeadContainer stickyContainer;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    private StickyListAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sticky_item_decoration;
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.addAction(new TitleBar.TextAction("清除") {
            @Override
            public void performAction(View view) {
                mAdapter.clear();
            }
        });
        return titleBar;
    }

    @Override
    protected void initViews() {
        stickyContainer.setOnStickyPositionChangedListener(position -> {
            StickyItem item = mAdapter.getItem(position);
            if (item != null) {
                tvTitle.setText(item.getHeadTitle());
            }
        });

        WidgetUtils.initRecyclerView(recyclerView, 0);
        StickyItemDecoration stickyItemDecoration = new StickyItemDecoration(stickyContainer, StickyListAdapter.TYPE_HEAD_STICKY);
        recyclerView.addItemDecoration(stickyItemDecoration);
        recyclerView.setAdapter(mAdapter = new StickyListAdapter());

        mAdapter.refresh(DemoDataProvider.getStickyDemoData());
    }

    @Override
    protected void initListeners() {
        mAdapter.setOnItemClickListener((itemView, item, position) -> {
            if (item != null && item.getNewInfo() != null) {
                Utils.goWeb(getContext(), item.getNewInfo().getDetailUrl());
            }
        });
    }

    @SingleClick
    @OnClick(R.id.tv_action)
    public void onViewClicked(View view) {
        XToastUtils.toast("点击更多");
    }


    @Override
    public void onDestroyView() {
        stickyContainer.recycle();
        super.onDestroyView();
    }
}
