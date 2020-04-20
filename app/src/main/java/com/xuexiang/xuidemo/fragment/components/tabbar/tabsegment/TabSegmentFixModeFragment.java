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

import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.adapter.FragmentAdapter;
import com.xuexiang.xui.utils.DensityUtils;
import com.xuexiang.xui.utils.ThemeUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.dialog.bottomsheet.BottomSheet;
import com.xuexiang.xui.widget.tabbar.TabSegment;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xuidemo.fragment.components.tabbar.tablayout.ContentPage;
import com.xuexiang.xuidemo.fragment.expands.materialdesign.behavior.SimpleListFragment;
import com.xuexiang.xuidemo.utils.XToastUtils;

import butterknife.BindView;

/**
 * @author xuexiang
 * @since 2018/12/26 下午5:40
 */
@Page(name = "固定宽度，内容均分")
public class TabSegmentFixModeFragment extends BaseFragment {

    @BindView(R.id.tabSegment1)
    TabSegment mTabSegment1;
    @BindView(R.id.tabSegment)
    TabSegment mTabSegment;
    @BindView(R.id.contentViewPager)
    ViewPager mContentViewPager;

    String[] pages = ContentPage.getPageNames();

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
        FragmentAdapter<BaseFragment> adapter = new FragmentAdapter<>(getChildFragmentManager());
        for (String page : pages) {
            mTabSegment.addTab(new TabSegment.Tab(page));
            adapter.addFragment(new SimpleListFragment(), page);
        }
        mContentViewPager.setAdapter(adapter);
        mContentViewPager.setCurrentItem(0, false);
        mTabSegment.setupWithViewPager(mContentViewPager, false);
        mTabSegment.setMode(TabSegment.MODE_FIXED);

        initNoViewPagerTabSegment();
    }

    /**
     * 不使用ViewPager的情况
     */
    private void initNoViewPagerTabSegment() {
        for (String page : pages) {
            mTabSegment1.addTab(new TabSegment.Tab(page));
        }
        mTabSegment1.setMode(TabSegment.MODE_FIXED);
        mTabSegment1.setOnTabClickListener(new TabSegment.OnTabClickListener() {
            @Override
            public void onTabClick(int index) {
                XToastUtils.toast("点击了" + index);
            }
        });
        //不使用ViewPager的话，必须notifyDataChanged，否则不能正常显示
        mTabSegment1.notifyDataChanged();
        mTabSegment1.selectTab(0);
        mTabSegment1.addOnTabSelectedListener(new TabSegment.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int index) {
                if (mTabSegment1 != null) {
                    mTabSegment1.hideSignCountView(index);
                }
            }

            @Override
            public void onTabUnselected(int index) {

            }

            @Override
            public void onTabReselected(int index) {
                if (mTabSegment1 != null) {
                    mTabSegment1.hideSignCountView(index);
                }
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
                                mTabSegment1.reset();
                                mTabSegment1.setHasIndicator(false);
                                for (String page : pages) {
                                    mTabSegment1.addTab(new TabSegment.Tab(page));
                                }
                                break;
                            case 1:
                                mTabSegment1.reset();
                                mTabSegment1.setHasIndicator(true);
                                mTabSegment1.setIndicatorPosition(false);
                                mTabSegment1.setIndicatorWidthAdjustContent(true);
                                for (String page : pages) {
                                    mTabSegment1.addTab(new TabSegment.Tab(page));
                                }
                                break;
                            case 2:
                                mTabSegment1.reset();
                                mTabSegment1.setHasIndicator(true);
                                mTabSegment1.setIndicatorPosition(true);
                                mTabSegment1.setIndicatorWidthAdjustContent(true);
                                for (String page : pages) {
                                    mTabSegment1.addTab(new TabSegment.Tab(page));
                                }
                                break;
                            case 3:
                                mTabSegment1.reset();
                                mTabSegment1.setHasIndicator(true);
                                mTabSegment1.setIndicatorPosition(false);
                                mTabSegment1.setIndicatorWidthAdjustContent(false);
                                for (String page : pages) {
                                    mTabSegment1.addTab(new TabSegment.Tab(page));
                                }
                                break;
                            case 4:
                                mTabSegment1.reset();
                                mTabSegment1.setHasIndicator(false);
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
                                mTabSegment1.addTab(component);
                                mTabSegment1.addTab(util);
                                break;
                            case 5:
                                TabSegment.Tab tab = mTabSegment1.getTab(0);
                                tab.setSignCountMargin(0, -DensityUtils.dp2px(getContext(), 4));
                                tab.showSignCountView(getContext(), 1);
                                break;
                            case 6:
                                mTabSegment1.reset();
                                mTabSegment1.setHasIndicator(false);
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
                                mTabSegment1.addTab(component2);
                                mTabSegment1.addTab(util2);
                                break;
                            case 7:
                                mTabSegment1.reset();
                                mTabSegment1.setHasIndicator(true);
                                mTabSegment1.setIndicatorWidthAdjustContent(true);
                                mTabSegment1.setIndicatorPosition(false);
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
                                mTabSegment1.addTab(component3);
                                mTabSegment1.addTab(util3);
                                break;
                            case 8:
                                mTabSegment1.updateTabText(0, "动态更新文案");
                                break;
                            case 9:
                                TabSegment.Tab component4 = new TabSegment.Tab(
                                        ContextCompat.getDrawable(getContext(), R.drawable.icon_tabbar_component),
                                        null,
                                        "动态更新", true
                                );
                                mTabSegment1.replaceTab(0, component4);
                                break;

                            default:
                                break;
                        }
                        mTabSegment1.notifyDataChanged();
                    }
                })
                .build()
                .show();
    }
}
