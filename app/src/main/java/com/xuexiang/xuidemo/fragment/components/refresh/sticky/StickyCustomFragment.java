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

import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.adapter.recyclerview.DividerItemDecoration;
import com.xuexiang.xui.adapter.recyclerview.XLinearLayoutManager;
import com.xuexiang.xuidemo.DemoDataProvider;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.adapter.StickyListAdapter;
import com.xuexiang.xuidemo.adapter.entity.StickyItem;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xuidemo.utils.Utils;
import com.xuexiang.xuidemo.utils.XToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static androidx.recyclerview.widget.OrientationHelper.VERTICAL;

/**
 * @author xuexiang
 * @since 2020/5/2 6:34 PM
 */
@Page(name = "通过监听RecyclerView滚动来实现粘顶效果")
public class StickyCustomFragment extends BaseFragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.header_container)
    FrameLayout headerContainer;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    private StickyListAdapter mAdapter;

    private LinearLayoutManager mLayoutManager;
    private int mTitleHeight;
    private int mFirstVisiblePosition;
    private String mLastGroupName;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sticky_custom;
    }

    @Override
    protected void initViews() {
        mLayoutManager = new XLinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), VERTICAL, 0));
        recyclerView.setAdapter(mAdapter = new StickyListAdapter());
        mAdapter.refresh(DemoDataProvider.getStickyDemoData());

        if (mAdapter.getItem(mFirstVisiblePosition).isHeadSticky()) {
            tvTitle.setText(mAdapter.getItem(mFirstVisiblePosition).getHeadTitle());
        }
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mTitleHeight = headerContainer.getMeasuredHeight();
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int firstPosition = mLayoutManager.findFirstVisibleItemPosition();
                int firstCompletePosition = mLayoutManager.findFirstCompletelyVisibleItemPosition();
                List<StickyItem> items = mAdapter.getData();

                // Here is the logic of the sticky:

                if (firstCompletePosition > 0 && (firstCompletePosition) < items.size()
                        && items.get(firstCompletePosition).isHeadSticky()) {

                    View view = mLayoutManager.findViewByPosition(firstCompletePosition);
                    if (view != null && view.getTop() <= mTitleHeight) {
                        headerContainer.setY(view.getTop() - mTitleHeight);
                    }
                }

                if (mFirstVisiblePosition != firstPosition && firstPosition >= 0) {
                    mFirstVisiblePosition = firstPosition;
                    headerContainer.setY(0);

                    String currentGroupName = items.get(mFirstVisiblePosition).isHeadSticky()
                            ? items.get(mFirstVisiblePosition).getHeadTitle()
                            : findStickyHeadName(mFirstVisiblePosition);

                    if (TextUtils.isEmpty(mLastGroupName) || !mLastGroupName.equals(currentGroupName)) {
                        mLastGroupName = currentGroupName;
                        tvTitle.setText(mLastGroupName);
                    }
                }

            }
        });

    }

    /**
     * 从传入位置递减找出标签的位置
     *
     * @param startPosition 开始寻找的位置
     * @return 最近的一个标签的位置
     */
    private String findStickyHeadName(int startPosition) {
        for (int position = startPosition; position >= 0; position--) {
            // 位置递减，只要查到位置是标签，立即返回此位置
            if (mAdapter.getItem(position).isHeadSticky()) {
                return mAdapter.getItem(position).getHeadTitle();
            }
        }
        return "";
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
        recyclerView.clearOnScrollListeners();
        super.onDestroyView();
    }
}
