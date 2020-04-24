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

package com.xuexiang.xuidemo.fragment.components.layout;

import androidx.recyclerview.widget.RecyclerView;

import com.xuexiang.xaop.annotation.MemoryCache;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.utils.WidgetUtils;
import com.xuexiang.xuidemo.DemoDataProvider;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.adapter.StickyListAdapter;
import com.xuexiang.xuidemo.adapter.entity.NewInfo;
import com.xuexiang.xuidemo.adapter.entity.StickyItem;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xuidemo.fragment.components.tabbar.tabsegment.MultiPage;
import com.xuexiang.xuidemo.utils.Utils;

import java.util.ArrayList;
import java.util.List;

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
        recyclerView.setNestedScrollingEnabled(false);
        WidgetUtils.initRecyclerView(recyclerView, 0);
        recyclerView.setAdapter(mAdapter = new StickyListAdapter());

        mAdapter.refresh(getStickyDemoData());
    }

    @Override
    protected void initListeners() {
        mAdapter.setOnItemClickListener((itemView, item, position) -> {
            if (item != null && item.getNewInfo() != null) {
                Utils.goWeb(getContext(), item.getNewInfo().getDetailUrl());
            }
        });
    }

    @MemoryCache
    private List<StickyItem> getStickyDemoData() {
        List<StickyItem> list = new ArrayList<>();
        List<StickyItem> temp = new ArrayList<>();
        List<NewInfo> news = DemoDataProvider.getDemoNewInfos();
        for (NewInfo newInfo : news) {
            temp.add(new StickyItem(newInfo));
        }
        for (String page : MultiPage.getPageNames()) {
            list.add(new StickyItem(page));
            list.addAll(temp);
        }
        return list;
    }


}
