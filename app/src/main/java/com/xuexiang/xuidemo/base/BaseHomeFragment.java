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

package com.xuexiang.xuidemo.base;

import android.content.res.Configuration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.model.PageInfo;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.activity.MainActivity;
import com.xuexiang.xuidemo.adapter.BaseRecyclerAdapter;
import com.xuexiang.xuidemo.adapter.WidgetItemAdapter;
import com.xuexiang.xuidemo.base.decorator.GridDividerItemDecoration;
import com.xuexiang.xuidemo.fragment.AboutFragment;
import com.xuexiang.xutil.common.ClickUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;

/**
 * 基础主页面
 *
 * @author xuexiang
 * @since 2018/12/29 上午11:18
 */
public abstract class BaseHomeFragment extends BaseFragment implements BaseRecyclerAdapter.OnItemClickListener {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private WidgetItemAdapter mWidgetItemAdapter;

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.setLeftImageResource(R.drawable.ic_action_menu);
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            @SingleClick
            public void onClick(View v) {
                getContainer().openMenu();
            }
        });
        titleBar.addAction(new TitleBar.ImageAction(R.drawable.icon_action_about) {
            @Override
            @SingleClick
            public void performAction(View view) {
                openPage(AboutFragment.class);
            }
        });
        return titleBar;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home_container;
    }

    @Override
    protected void initViews() {
        initRecyclerView();
    }

    private void initRecyclerView() {
        mWidgetItemAdapter = new WidgetItemAdapter(getContext(), sortPageInfo(getPageContents()));
        mWidgetItemAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mWidgetItemAdapter);
        int spanCount = 3;
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
        mRecyclerView.addItemDecoration(new GridDividerItemDecoration(getContext(), spanCount));
    }

    /**
     * @return
     */
    protected abstract List<PageInfo> getPageContents();

    /**
     * 进行排序
     * @param pageInfoList
     * @return
     */
    private List<PageInfo> sortPageInfo(List<PageInfo> pageInfoList) {
        Collections.sort(pageInfoList, new Comparator<PageInfo>() {
            @Override
            public int compare(PageInfo o1, PageInfo o2) {
                return o1.getClassPath().compareTo(o2.getClassPath());
            }
        });
        return pageInfoList;
    }

    @Override
    @SingleClick
    public void onItemClick(View itemView, int pos) {
        PageInfo widgetInfo = mWidgetItemAdapter.getItem(pos);
        if (widgetInfo != null) {
            openPage(widgetInfo.getName());
            getContainer().switchTab(false);
        }
    }

    public MainActivity getContainer() {
        return (MainActivity) getActivity();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getContainer().switchTab(true);
        }
    }

    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (getContainer().isMenuOpen()) {
                getContainer().closeMenu();
            } else {
                ClickUtils.exitBy2Click();
            }
        }
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig); //屏幕旋转时刷新一下title
        ((ViewGroup) getRootView()).removeViewAt(0);
        initTitle();
    }

}
