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

import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.utils.DensityUtils;
import com.xuexiang.xui.utils.ThemeUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.dialog.bottomsheet.BottomSheet;
import com.xuexiang.xui.widget.tabbar.TabSegment;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xuidemo.fragment.components.tabbar.ContentPage;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * @author xuexiang
 * @since 2018/12/26 下午5:40
 */
@Page(name = "固定宽度，内容均分")
public class TabSegmentFixModeFragment extends BaseFragment {

    @BindView(R.id.tabSegment)
    TabSegment mTabSegment;
    @BindView(R.id.contentViewPager)
    ViewPager mContentViewPager;

    String[] pages = ContentPage.getPageNames();

    private Map<ContentPage, View> mPageMap = new HashMap<>();
    private ContentPage mDestPage = ContentPage.教育;
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
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.addAction(new TitleBar.ImageAction(R.drawable.icon_topbar_overflow) {
            @Override
            @SingleClick
            public void performAction(View view) {
                showBottomSheetList();
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
        mContentViewPager.setAdapter(mPagerAdapter);
        mContentViewPager.setCurrentItem(mDestPage.getPosition(), false);

        for (String page : pages) {
            mTabSegment.addTab(new TabSegment.Tab(page));
        }
        mTabSegment.setupWithViewPager(mContentViewPager, false);
        mTabSegment.setMode(TabSegment.MODE_FIXED);
        mTabSegment.addOnTabSelectedListener(new TabSegment.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int index) {
                mTabSegment.hideSignCountView(index);
            }

            @Override
            public void onTabUnselected(int index) {

            }

            @Override
            public void onTabReselected(int index) {
                mTabSegment.hideSignCountView(index);
            }

            @Override
            public void onDoubleTap(int index) {

            }
        });
    }

    private void showBottomSheetList() {
        new BottomSheet.BottomListSheetBuilder(getActivity())
                .addItem(getResources().getString(R.string.tabSegment_mode_general))
                .addItem(getResources().getString(R.string.tabSegment_mode_bottom_indicator))
                .addItem(getResources().getString(R.string.tabSegment_mode_top_indicator))
                .addItem(getResources().getString(R.string.tabSegment_mode_indicator_with_content))
                .addItem(getResources().getString(R.string.tabSegment_mode_left_icon_and_auto_tint))
                .addItem(getResources().getString(R.string.tabSegment_mode_sign_count))
                .addItem(getResources().getString(R.string.tabSegment_mode_icon_change))
                .addItem(getResources().getString(R.string.tabSegment_mode_muti_color))
                .addItem(getResources().getString(R.string.tabSegment_mode_change_content_by_index))
                .addItem(getResources().getString(R.string.tabSegment_mode_replace_tab_by_index))
                .setOnSheetItemClickListener(new BottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(BottomSheet dialog, View itemView, int position, String tag) {
                        dialog.dismiss();
                        switch (position) {
                            case 0:
                                mTabSegment.reset();
                                mTabSegment.setHasIndicator(false);
                                for (String page : pages) {
                                    mTabSegment.addTab(new TabSegment.Tab(page));
                                }
                                break;
                            case 1:
                                mTabSegment.reset();
                                mTabSegment.setHasIndicator(true);
                                mTabSegment.setIndicatorPosition(false);
                                mTabSegment.setIndicatorWidthAdjustContent(true);
                                for (String page : pages) {
                                    mTabSegment.addTab(new TabSegment.Tab(page));
                                }
                                break;
                            case 2:
                                mTabSegment.reset();
                                mTabSegment.setHasIndicator(true);
                                mTabSegment.setIndicatorPosition(true);
                                mTabSegment.setIndicatorWidthAdjustContent(true);
                                for (String page : pages) {
                                    mTabSegment.addTab(new TabSegment.Tab(page));
                                }
                                break;
                            case 3:
                                mTabSegment.reset();
                                mTabSegment.setHasIndicator(true);
                                mTabSegment.setIndicatorPosition(false);
                                mTabSegment.setIndicatorWidthAdjustContent(false);
                                for (String page : pages) {
                                    mTabSegment.addTab(new TabSegment.Tab(page));
                                }
                                break;
                            case 4:
                                mTabSegment.reset();
                                mTabSegment.setHasIndicator(false);
                                TabSegment.Tab component = new TabSegment.Tab(
                                        ContextCompat.getDrawable(getContext(), R.drawable.icon_tabbar_component),
                                        null,
                                        "组件", true
                                );
                                TabSegment.Tab util = new TabSegment.Tab(
                                        ContextCompat.getDrawable(getContext(), R.drawable.icon_tabbar_util),
                                        null,
                                        "工具", true
                                );
                                mTabSegment.addTab(component);
                                mTabSegment.addTab(util);
                                break;
                            case 5:
                                TabSegment.Tab tab = mTabSegment.getTab(0);
                                tab.setSignCountMargin(0, -DensityUtils.dp2px(getContext(), 4));
                                tab.showSignCountView(getContext(), 1);
                                break;
                            case 6:
                                mTabSegment.reset();
                                mTabSegment.setHasIndicator(false);
                                TabSegment.Tab component2 = new TabSegment.Tab(
                                        ContextCompat.getDrawable(getContext(), R.drawable.icon_tabbar_component),
                                        ContextCompat.getDrawable(getContext(), R.drawable.icon_tabbar_component_selected),
                                        "组件", false
                                );
                                TabSegment.Tab util2 = new TabSegment.Tab(
                                        ContextCompat.getDrawable(getContext(), R.drawable.icon_tabbar_util),
                                        ContextCompat.getDrawable(getContext(), R.drawable.icon_tabbar_util_selected),
                                        "工具", false
                                );
                                mTabSegment.addTab(component2);
                                mTabSegment.addTab(util2);
                                break;
                            case 7:
                                mTabSegment.reset();
                                mTabSegment.setHasIndicator(true);
                                mTabSegment.setIndicatorWidthAdjustContent(true);
                                mTabSegment.setIndicatorPosition(false);
                                TabSegment.Tab component3 = new TabSegment.Tab(
                                        ContextCompat.getDrawable(getContext(), R.drawable.icon_tabbar_component),
                                        null,
                                        "组件", true
                                );
                                component3.setTextColor(ThemeUtils.resolveColor(getContext(), R.attr.colorAccent),
                                        ContextCompat.getColor(getContext(), R.color.xui_config_color_red));
                                TabSegment.Tab util3 = new TabSegment.Tab(
                                        ContextCompat.getDrawable(getContext(), R.drawable.icon_tabbar_util),
                                        null,
                                        "工具", true
                                );
                                util3.setTextColor(ContextCompat.getColor(getContext(), R.color.xui_config_color_gray_1),
                                        ContextCompat.getColor(getContext(), R.color.xui_config_color_red));
                                mTabSegment.addTab(component3);
                                mTabSegment.addTab(util3);
                                break;
                            case 8:
                                mTabSegment.updateTabText(0, "动态更新文案");
                                break;
                            case 9:
                                TabSegment.Tab component4 = new TabSegment.Tab(
                                        ContextCompat.getDrawable(getContext(), R.drawable.icon_tabbar_component),
                                        null,
                                        "动态更新", true
                                );
                                mTabSegment.replaceTab(0, component4);
                                break;

                            default:
                                break;
                        }
                        mTabSegment.notifyDataChanged();
                    }
                })
                .build()
                .show();
    }
}
