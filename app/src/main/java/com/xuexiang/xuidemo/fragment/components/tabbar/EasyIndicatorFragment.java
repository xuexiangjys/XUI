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

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.tabbar.EasyIndicator;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.activity.EasyIndicatorActivity;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xutil.tip.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * @author xuexiang
 * @since 2018/12/26 下午1:57
 */
@Page(name = "EasyIndicator\n简单的指示器")
public class EasyIndicatorFragment extends BaseFragment {

    @BindView(R.id.easy_indicator1)
    EasyIndicator mEasyIndicator1;

    @BindView(R.id.easy_indicator)
    EasyIndicator mEasyIndicator;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.addAction(new TitleBar.TextAction("fragment") {
            @Override
            public void performAction(View view) {
                startActivity(new Intent(getContext(), EasyIndicatorActivity.class));
            }
        });
        return titleBar;
    }

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
        return R.layout.fragment_easy_indicator;
    }

    @Override
    protected void initViews() {
        mEasyIndicator.setTabTitles(ContentPage.getPageNames());
        mEasyIndicator.setViewPager(mViewPager, mPagerAdapter);
        mViewPager.setOffscreenPageLimit(ContentPage.size() - 1);
        mViewPager.setCurrentItem(2);


        initIndicatorNoViewPager();
    }

    private void initIndicatorNoViewPager() {
        mEasyIndicator1.setTabTitles(ContentPage.getPageNames());
        mEasyIndicator1.setOnTabClickListener(new EasyIndicator.onTabClickListener() {
            @Override
            public void onTabClick(String title, int position) {
                ToastUtils.toast("点击了" + title);
            }
        });

    }

}
