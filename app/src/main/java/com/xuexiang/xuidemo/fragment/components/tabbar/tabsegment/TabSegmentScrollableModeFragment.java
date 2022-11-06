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

package com.xuexiang.xuidemo.fragment.components.tabbar.tabsegment;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.utils.DensityUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.tabbar.TabSegment;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xui.utils.XToastUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * @author xuexiang
 * @since 2018/12/26 下午11:46
 */
@Page(name = "内容自适应，超过父容器则滚动")
public class TabSegmentScrollableModeFragment extends BaseFragment {

    @BindView(R.id.tabSegment1)
    TabSegment mTabSegment1;
    @BindView(R.id.tabSegment)
    TabSegment mTabSegment;
    @BindView(R.id.contentViewPager)
    ViewPager mContentViewPager;

    String[] pages = MultiPage.getPageNames();
    private final int TAB_COUNT = 10;
    private int mCurrentItemCount = TAB_COUNT;
    private MultiPage mDestPage = MultiPage.教育;
    private Map<MultiPage, View> mPageMap = new HashMap<>();
    private PagerAdapter mPagerAdapter = new PagerAdapter() {
        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public int getCount() {
            return mCurrentItemCount;
        }

        @Override
        public Object instantiateItem(final ViewGroup container, int position) {
            MultiPage page = MultiPage.getPage(position);
            View view = getPageView(page);
            view.setTag(page);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            container.addView(view, params);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            View view = (View) object;
            Object page = view.getTag();
            if (page instanceof MultiPage) {
                int pos = ((MultiPage) page).getPosition();
                if (pos >= mCurrentItemCount) {
                    return POSITION_NONE;
                }
                return POSITION_UNCHANGED;
            }
            return POSITION_NONE;
        }
    };

    private View getPageView(MultiPage page) {
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
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.addAction(new TitleBar.TextAction("减少Tab") {
            @Override
            @SingleClick
            public void performAction(View view) {
                reduceTabCount();
            }
        });
        return titleBar;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tabsegment;
    }

    @Override
    protected void initViews() {
        mTabSegment1.setVisibility(View.GONE);

        mContentViewPager.setAdapter(mPagerAdapter);
        mContentViewPager.setCurrentItem(mDestPage.getPosition(), false);
        for (int i = 0; i < mCurrentItemCount; i++) {
            mTabSegment.addTab(new TabSegment.Tab(pages[i]));
        }
        int space = DensityUtils.dp2px(getContext(), 16);
        mTabSegment.setHasIndicator(true);
        mTabSegment.setMode(TabSegment.MODE_SCROLLABLE);
        mTabSegment.setItemSpaceInScrollMode(space);
        mTabSegment.setupWithViewPager(mContentViewPager, false);
        mTabSegment.setPadding(space, 0, space, 0);
        mTabSegment.addOnTabSelectedListener(new TabSegment.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int index) {
                XToastUtils.toast("select " + pages[index]);
            }

            @Override
            public void onTabUnselected(int index) {
                XToastUtils.toast("unSelect " + pages[index]);
            }

            @Override
            public void onTabReselected(int index) {
                XToastUtils.toast("reSelect " + pages[index]);
            }

            @Override
            public void onDoubleTap(int index) {
                XToastUtils.toast("double tap " + pages[index]);
            }
        });
    }

    private void reduceTabCount() {
        if (mCurrentItemCount <= 1) {
            Toast.makeText(getContext(), "Only the last one, don't reduce it anymore!!!", Toast.LENGTH_SHORT).show();
            return;
        }
        mCurrentItemCount--;
        mPagerAdapter.notifyDataSetChanged();
        mTabSegment.reset();
        for (int i = 0; i < mCurrentItemCount; i++) {
            mTabSegment.addTab(new TabSegment.Tab(pages[i]));
        }
        mTabSegment.notifyDataChanged();
    }
}
