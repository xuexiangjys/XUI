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

package com.xuexiang.xuidemo.fragment.expands.materialdesign.behavior;

import android.animation.ArgbEvaluator;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import androidx.core.graphics.drawable.DrawableCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.adapter.FragmentAdapter;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.StatusBarUtils;
import com.xuexiang.xui.utils.ThemeUtils;
import com.xuexiang.xui.utils.ViewUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xuidemo.widget.StickyNavigationLayout;

import butterknife.BindView;

import static com.google.android.material.tabs.TabLayout.MODE_FIXED;

/**
 * @author xuexiang
 * @since 2020/4/6 10:29 AM
 */
@Page(name = "NestedScrolling机制实现嵌套滑动")
public class NestedScrollingFragment extends BaseFragment {

    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.sticky_navigation_layout)
    StickyNavigationLayout stickyNavigationLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_nested_scrolling;
    }

    @Override
    protected void initArgs() {
        StatusBarUtils.initStatusBarStyle(getActivity(), true);
    }

    @Override
    protected TitleBar initTitle() {
        titlebar.setLeftClickListener(v -> popToBack());
        titlebar.setImmersive(true);
        titlebar.setTitle("NestedScrolling机制实现嵌套滑动");
        initTitleBar(0);
        return null;
    }

    private String[] titles = new String[]{"资讯", "娱乐", "教育"};

    @Override
    protected void initViews() {
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
        stickyNavigationLayout.setOnScrollChangeListener(this::initTitleBar);
    }

    private void initTitleBar(float moveRatio) {
        ArgbEvaluator argbEvaluator = new ArgbEvaluator();
        int backColor = (int) argbEvaluator.evaluate(moveRatio, Color.WHITE, Color.BLACK);
        Drawable wrapDrawable = DrawableCompat.wrap(ResUtils.getDrawable(getContext(), R.drawable.xui_ic_navigation_back_white));
        DrawableCompat.setTint(wrapDrawable, backColor);
        int bgColor = (int) argbEvaluator.evaluate(moveRatio, Color.TRANSPARENT, ThemeUtils.resolveColor(getContext(), R.attr.xui_actionbar_color));
        titlebar.setLeftImageDrawable(wrapDrawable);
        titlebar.setBackgroundColor(bgColor);
        titlebar.getCenterText().setAlpha(moveRatio);
    }

}
