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

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.adapter.FragmentAdapter;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xutil.common.CollectionUtils;
import com.xuexiang.xutil.tip.ToastUtils;

import butterknife.BindView;

/**
 * @author xuexiang
 * @since 2019-05-10 00:17
 */
@Page(name = "BottomNavigationView Behavior")
public class BottomNavigationViewBehaviorFragment extends BaseFragment implements ViewPager.OnPageChangeListener, BottomNavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigation;

    @Override
    protected TitleBar initTitle() {
        toolbar.setNavigationIcon(R.drawable.ic_navigation_back_white);
        toolbar.setTitle("BottomNavigationView");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popToBack();
            }
        });
        return null;
    }

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_bottom_navigationview_behavior;
    }

    int[] menuItemIds = new int[]{R.id.item_dashboard, R.id.item_photo, R.id.item_music, R.id.item_movie};
    String[] titles = new String[]{"资讯", "照片", "音乐", "电影"};

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        FragmentAdapter<BaseFragment> adapter = new FragmentAdapter<>(getChildFragmentManager());
        for (String title : titles) {
            adapter.addFragment(new SimpleListFragment(), title);
        }
        viewPager.setOffscreenPageLimit(titles.length - 1);
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void initListeners() {
        viewPager.addOnPageChangeListener(this);
        bottomNavigation.setOnNavigationItemSelectedListener(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.toast("新建");
            }
        });
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int position) {
        bottomNavigation.getMenu().getItem(position).setChecked(true);
//        bottomNavigation.setSelectedItemId(menuItemIds[position]);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int index = CollectionUtils.arrayIndexOf(titles, menuItem.getTitle());
        if (index != -1) {
            viewPager.setCurrentItem(index, true);
            return true;
        }
        return false;
    }
}
