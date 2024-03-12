/*
 * Copyright (C) 2021 xuexiangjys(xuexiangjys@163.com)
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

package com.xuexiang.xuidemo.fragment.components.refresh.sample.edit;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.utils.ViewUtils;
import com.xuexiang.xui.utils.WidgetUtils;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.button.SmoothCheckBox;
import com.xuexiang.xuidemo.DemoDataProvider;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xuidemo.utils.Utils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author xuexiang
 * @since 2021/7/7 2:12 AM
 */
@Page(name = "列表长按编辑案例")
public class NewsListEditFragment extends BaseFragment {


    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private NewsListEditAdapter mAdapter;

    @BindView(R.id.fl_edit)
    FrameLayout flEdit;
    @BindView(R.id.scb_select_all)
    SmoothCheckBox scbSelectAll;

    private TextView mTvSwitch;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_news_list_edit;
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        mTvSwitch = (TextView) titleBar.addAction(new TitleBar.TextAction(getString(R.string.title_enter_manage_mode)) {
            @SingleClick
            @Override
            public void performAction(View view) {
                if (mAdapter == null) {
                    return;
                }
                mAdapter.switchManageMode();
                refreshManageMode();
            }
        });
        return titleBar;
    }

    private void refreshManageMode() {
        if (mTvSwitch != null) {
            mTvSwitch.setText(mAdapter.isManageMode() ? R.string.title_exit_manage_mode : R.string.title_enter_manage_mode);
        }
        ViewUtils.setVisibility(flEdit, mAdapter.isManageMode());
    }

    @Override
    protected void initViews() {
        WidgetUtils.initRecyclerView(recyclerView, 0);
        recyclerView.setAdapter(mAdapter = new NewsListEditAdapter(isSelectAll -> {
            if (scbSelectAll != null) {
                scbSelectAll.setCheckedSilent(isSelectAll);
            }
        }));
        scbSelectAll.setOnCheckedChangeListener((checkBox, isChecked) -> mAdapter.setSelectAll(isChecked));
    }

    @Override
    protected void initListeners() {
        //下拉刷新
        refreshLayout.setOnRefreshListener(refreshLayout -> refreshLayout.getLayout().postDelayed(() -> {
            mAdapter.refresh(DemoDataProvider.getDemoNewInfos());
            refreshLayout.finishRefresh();
        }, 1000));
        //上拉加载
        refreshLayout.setOnLoadMoreListener(refreshLayout -> refreshLayout.getLayout().postDelayed(() -> {
            mAdapter.loadMore(DemoDataProvider.getDemoNewInfos());
            refreshLayout.finishLoadMore();
        }, 1000));
        refreshLayout.autoRefresh();//第一次进入触发自动刷新，演示效果

        mAdapter.setOnItemClickListener((itemView, item, position) -> {
            if (mAdapter.isManageMode()) {
                mAdapter.updateSelectStatus(position);
            } else {
                Utils.goWeb(getContext(), item.getDetailUrl());
            }
        });
        mAdapter.setOnItemLongClickListener((itemView, item, position) -> {
            if (!mAdapter.isManageMode()) {
                mAdapter.enterManageMode(position);
                refreshManageMode();
            }
        });
    }

    @SingleClick
    @OnClick(R.id.btn_submit)
    public void onViewClicked(View view) {
        XToastUtils.toast("选中了" + mAdapter.getSelectedIndexList().size() + "个选项！");

    }
}
