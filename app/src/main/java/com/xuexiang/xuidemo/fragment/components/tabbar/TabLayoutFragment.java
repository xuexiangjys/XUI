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

package com.xuexiang.xuidemo.fragment.components.tabbar;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import android.support.design.widget.TabLayout;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xuidemo.fragment.components.tabbar.tabsegment.MultiPage;
import com.xuexiang.xutil.tip.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

import static android.support.design.widget.TabLayout.MODE_SCROLLABLE;

/**
 * @author xuexiang
 * @since 2018/12/27 上午11:45
 */
@Page(name = "TabLayout\nMaterial Design 组件")
public class TabLayoutFragment extends BaseFragment implements TabLayout.OnTabSelectedListener {

    @BindView(R.id.tab1)
    TabLayout mTabLayout1;

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    private Map<ContentPage, View> mPageMap = new HashMap<>();

    private PagerAdapter mPagerAdapter = new PagerAdapter() {
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getCount() {
            return ContentPage.size();
        }

        @Override
        public Object instantiateItem(final ViewGroup container, int position) {
            ContentPage page = ContentPage.getPage(position);
            View view = getPageView(page);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            container.addView(view, params);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return ContentPage.getPageNames()[position];
        }
    };

    private View getPageView(ContentPage page) {
        View view = mPageMap.get(page);
        if (view == null) {
            TextView textView = new TextView(getContext());
            textView.setTextAppearance(getContext(), R.style.TextStyle_Content_Match);
            textView.setGravity(Gravity.CENTER);
            textView.setText(String.format("这个是%s页面的内容", page.name()));
            view = textView;
            mPageMap.put(page, view);
        }
        return view;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tablayout;
    }

    @Override
    protected void initViews() {
        for (String page : MultiPage.getPageNames()) {
            mTabLayout1.addTab(mTabLayout1.newTab().setText(page));
        }
        mTabLayout1.setTabMode(MODE_SCROLLABLE);
        mTabLayout1.addOnTabSelectedListener(this);

        mTabLayout.addOnTabSelectedListener(this);
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        ToastUtils.toast("选中了:" + tab.getText());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
