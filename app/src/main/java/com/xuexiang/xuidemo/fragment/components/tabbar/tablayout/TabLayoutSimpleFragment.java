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

package com.xuexiang.xuidemo.fragment.components.tabbar.tablayout;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.adapter.FragmentAdapter;
import com.xuexiang.xui.utils.WidgetUtils;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xuidemo.fragment.components.tabbar.tabsegment.MultiPage;
import com.xuexiang.xui.utils.XToastUtils;

import butterknife.BindView;

import static com.google.android.material.tabs.TabLayout.MODE_SCROLLABLE;

/**
 * @author xuexiang
 * @since 2020/4/21 12:19 AM
 */
@Page(name = "TabLayout简单使用")
public class TabLayoutSimpleFragment extends BaseFragment implements TabLayout.OnTabSelectedListener {
    @BindView(R.id.tab1)
    TabLayout mTabLayout1;

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tablayout_simple;
    }

    @Override
    protected void initViews() {
        // 多数量的Tab,不关联ViewPager
        for (String page : MultiPage.getPageNames()) {
            mTabLayout1.addTab(mTabLayout1.newTab().setText(page));
        }
        mTabLayout1.setTabMode(MODE_SCROLLABLE);
        mTabLayout1.addOnTabSelectedListener(this);

        // 固定数量的Tab,关联ViewPager
        FragmentAdapter<SimpleTabFragment> adapter = new FragmentAdapter<>(getChildFragmentManager());
        for (String page : ContentPage.getPageNames()) {
            adapter.addFragment(SimpleTabFragment.newInstance(page), page);
        }
        mTabLayout.addOnTabSelectedListener(this);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);

        WidgetUtils.setTabLayoutTextFont(mTabLayout);
        WidgetUtils.setTabLayoutTextFont(mTabLayout1);

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        XToastUtils.toast("选中了:" + tab.getText());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
