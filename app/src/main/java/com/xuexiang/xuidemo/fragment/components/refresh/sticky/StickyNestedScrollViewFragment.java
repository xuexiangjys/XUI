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

import androidx.recyclerview.widget.RecyclerView;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.utils.WidgetUtils;
import com.xuexiang.xuidemo.DemoDataProvider;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.adapter.StickyListAdapter;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xuidemo.utils.Utils;

import butterknife.BindView;

/**
 * @author xuexiang
 * @since 2020/4/25 1:09 AM
 */
@Page(name = "StickyNestedScrollView\n粘顶嵌套滚动布局")
public class StickyNestedScrollViewFragment extends BaseFragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private StickyListAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sticky_nested_scrollview;
    }

    @Override
    protected void initViews() {
        // 注意，这里使用StickyNestedScrollView嵌套RecyclerView会导致RecyclerView复用机制失效，
        // 在数据量大的情况下，加载容易卡顿
        recyclerView.setNestedScrollingEnabled(false);
        WidgetUtils.initRecyclerView(recyclerView, 0);
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

}
