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

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.adapter.FragmentStateAdapter;
import com.xuexiang.xui.utils.StatusBarUtils;
import com.xuexiang.xui.utils.ViewUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.layout.XUILinearLayout;
import com.xuexiang.xui.widget.statelayout.SimpleAnimationListener;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xuidemo.fragment.components.tabbar.tabsegment.MultiPage;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xuidemo.widget.ComplexNestedScrollingLayout;

import butterknife.BindView;
import butterknife.OnClick;

import static com.google.android.material.tabs.TabLayout.MODE_SCROLLABLE;

/**
 * @author xuexiang
 * @since 2020/4/12 7:37 PM
 */
@Page(name = "复杂嵌套滑动")
public class ComplexNestedScrollingFragment extends BaseFragment implements TabLayout.OnTabSelectedListener {

    @BindView(R.id.titlebar)
    TitleBar titlebar;

    @BindView(R.id.scrolling_layout)
    ComplexNestedScrollingLayout scrollingLayout;

    @BindView(R.id.ll_tip_container)
    LinearLayout llTipContainer;

    @BindView(R.id.ll_tip1)
    XUILinearLayout llTip1;
    @BindView(R.id.iv_action1)
    AppCompatImageView ivAction1;
    @BindView(R.id.ll_tip2)
    XUILinearLayout llTip2;
    @BindView(R.id.iv_action2)
    AppCompatImageView ivAction2;


    @BindView(R.id.ll_navigation_view)
    LinearLayout llNavigationView;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_switch)
    AppCompatImageView ivSwitch;

    @BindView(R.id.fl_container)
    FrameLayout flContainer;
    @BindView(R.id.fl_default_page)
    FrameLayout flDefaultPage;
    @BindView(R.id.view_pager)
    ViewPager viewPager;


    private boolean mIsShowNavigationView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_complex_nested_scrolling;
    }

    @Override
    protected void initArgs() {
        StatusBarUtils.initStatusBarStyle(getActivity(), true);
    }

    @Override
    protected TitleBar initTitle() {
        titlebar.setLeftClickListener(v -> popToBack());
        titlebar.setImmersive(true);
        titlebar.setTitle("复杂嵌套滑动");
        titlebar.addAction(new TitleBar.ImageAction(R.drawable.icon_action_query) {
            @SingleClick
            @Override
            public void performAction(View view) {
                XToastUtils.toast("搜索");
            }
        });
        return null;
    }

    @Override
    protected void initViews() {
        initScrollingLayout();

        initFragment();

        initTabLayout();

    }

    private void initScrollingLayout() {
        llTipContainer.post(() -> refreshTopViewHeight(llTipContainer.getMeasuredHeight()));
    }

    private void refreshTopViewHeight(int topViewHeight) {
        scrollingLayout.setTopViewHeight(topViewHeight, mIsShowNavigationView);
        if (updateContainerHeight()) {
            scrollingLayout.requestLayout();
        }
    }

    private boolean updateContainerHeight() {
        int containerHeight = scrollingLayout.getContainerHeight(mIsShowNavigationView);
        ViewGroup.LayoutParams lp = flContainer.getLayoutParams();
        if (lp.height != containerHeight) {
            lp.height = containerHeight;
            flContainer.setLayoutParams(lp);
            return true;
        }
        return false;
    }

    private void initFragment() {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.fl_default_page, SimpleListFragment.newInstance(true));
        transaction.commit();
    }

    private void initTabLayout() {
        FragmentStateAdapter<Fragment> adapter = new FragmentStateAdapter<>(getChildFragmentManager());
        for (String page : MultiPage.getPageNames()) {
            adapter.addFragment(SimpleListFragment.newInstance(false), page);
        }
        tabLayout.setTabMode(MODE_SCROLLABLE);
        tabLayout.addOnTabSelectedListener(this);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);
        tabLayout.setupWithViewPager(viewPager);
    }

    @SingleClick
    @OnClick({R.id.iv_switch, R.id.iv_action1, R.id.iv_action2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_switch:
                refreshStatus(mIsShowNavigationView = !mIsShowNavigationView);
                break;
            case R.id.iv_action1:
                dismissTipView(llTip1);
                break;
            case R.id.iv_action2:
                dismissTipView(llTip2);
                break;
            default:
                break;
        }
    }

    private void dismissTipView(View view) {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();
        int newTopHeight = llTipContainer.getMeasuredHeight() - view.getMeasuredHeight() - lp.topMargin - lp.bottomMargin;
        ViewUtils.fadeOut(view, 500, new SimpleAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
                refreshTopViewHeight(newTopHeight);
            }
        });
    }

    private void refreshStatus(final boolean isShow) {
        ObjectAnimator rotation;
        ObjectAnimator tabAlpha;
        ObjectAnimator textAlpha;
        if (isShow) {
            rotation = ObjectAnimator.ofFloat(ivSwitch, "rotation", 0, -45);
            tabAlpha = ObjectAnimator.ofFloat(tabLayout, "alpha", 0, 1);
            textAlpha = ObjectAnimator.ofFloat(tvTitle, "alpha", 1, 0);
        } else {
            rotation = ObjectAnimator.ofFloat(ivSwitch, "rotation", -45, 0);
            tabAlpha = ObjectAnimator.ofFloat(tabLayout, "alpha", 1, 0);
            textAlpha = ObjectAnimator.ofFloat(tvTitle, "alpha", 0, 1);
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(rotation).with(tabAlpha).with(textAlpha);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                switchContainer(isShow);
            }
        });
        animatorSet.setDuration(500).start();
    }

    private void switchContainer(boolean isShow) {
        refreshTopViewHeight(llTipContainer.getMeasuredHeight());

        tvTitle.setVisibility(isShow ? View.GONE : View.VISIBLE);

        flDefaultPage.setVisibility(isShow ? View.GONE : View.VISIBLE);
        viewPager.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void initListeners() {

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
