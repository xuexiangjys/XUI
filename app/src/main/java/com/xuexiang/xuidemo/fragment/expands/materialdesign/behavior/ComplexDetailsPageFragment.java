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

package com.xuexiang.xuidemo.fragment.expands.materialdesign.behavior;

import static com.google.android.material.tabs.TabLayout.MODE_FIXED;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.adapter.FragmentAdapter;
import com.xuexiang.xui.utils.StatusBarUtils;
import com.xuexiang.xui.utils.ViewUtils;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xutil.display.Colors;

import butterknife.BindView;

/**
 * @author xuexiang
 * @since 2019/5/17 8:57
 */
@Page(name = "复杂详情页联动")
public class ComplexDetailsPageFragment extends BaseFragment {

    @BindView(R.id.appbar_layout_toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapse_layout)
    CollapsingToolbarLayout collapseLayout;
    @BindView(R.id.appbar_layout)
    AppBarLayout appbarLayout;
    @BindView(R.id.fab_scrolling)
    FloatingActionButton fabScrolling;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_complex_details_page;
    }

    @Override
    protected void initArgs() {
        super.initArgs();
        StatusBarUtils.translucent(getActivity(), Colors.TRANSPARENT);
        StatusBarUtils.setStatusBarLightMode(getActivity());
    }

    @Override
    protected TitleBar initTitle() {
        return null;
    }

    String[] titles = new String[]{"资讯", "娱乐", "教育"};

    @Override
    protected void initViews() {
        toolbar.setNavigationOnClickListener(v -> popToBack());

        FragmentAdapter<BaseFragment> adapter = new FragmentAdapter<>(getChildFragmentManager());
        tabLayout.setTabMode(MODE_FIXED);
        for (String title : titles) {
            tabLayout.addTab(tabLayout.newTab().setText(title));
            adapter.addFragment(new SimpleListFragment(), title);
        }
        viewPager.setOffscreenPageLimit(titles.length - 1);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        ViewUtils.setViewsFont(tabLayout);
        ViewUtils.setToolbarLayoutTextFont(collapseLayout);
    }

    @Override
    protected void initListeners() {
        fabScrolling.setOnClickListener(v -> XToastUtils.toast("新建"));
        appbarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                StatusBarUtils.setStatusBarDarkMode(getActivity());
                fabScrolling.hide();
            } else {
                StatusBarUtils.setStatusBarLightMode(getActivity());
                fabScrolling.show();
            }
        });
    }
}
