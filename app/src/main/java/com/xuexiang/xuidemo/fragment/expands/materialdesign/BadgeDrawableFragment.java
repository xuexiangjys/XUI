/*
 * Copyright (C) 2023 xuexiangjys(xuexiangjys@163.com)
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

package com.xuexiang.xuidemo.fragment.expands.materialdesign;

import android.annotation.SuppressLint;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;

import butterknife.BindView;

/**
 * BadgeDrawable小气泡，只能设置数字, 可设置最大长度，超出长度的显示999+
 * <p>
 * 使用有点局限性
 *
 * @author xuexiang
 * @since 2023/5/14 18:27
 */
@Page(name = "BadgeDrawable")
public class BadgeDrawableFragment extends BaseFragment {

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.fl_image)
    FrameLayout flImage;
    @BindView(R.id.iv_image)
    AppCompatImageView ivImage;
    @BindView(R.id.navigation_view)
    BottomNavigationView navigationView;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_badge_drawable;
    }

    @Override
    protected void initViews() {
        initTabLayout();
        initTextView();
        initImageView();
        initNavigationView();
    }


    private void initTabLayout() {
        // 带数字小红点
        TabLayout.Tab tab1 = tabLayout.getTabAt(0);
        if (tab1 != null) {
            BadgeDrawable badgeDrawable = tab1.getOrCreateBadge();
            badgeDrawable.setBackgroundColor(ResUtils.getColor(getContext(), R.color.xui_config_color_red));
            badgeDrawable.setBadgeTextColor(ResUtils.getColor(getContext(), R.color.xui_config_color_white));
            badgeDrawable.setNumber(34);
            tab1.view.setOnClickListener(v -> tab1.removeBadge());
        }
        // 不带数字小红点
        TabLayout.Tab tab2 = tabLayout.getTabAt(1);
        if (tab2 != null) {
            BadgeDrawable badgeDrawable = tab2.getOrCreateBadge();
            badgeDrawable.setBackgroundColor(ResUtils.getColor(getContext(), R.color.xui_config_color_waring));
            tab2.view.setOnClickListener(v -> badgeDrawable.setVisible(false));
        }
        TabLayout.Tab tab3 = tabLayout.getTabAt(2);
        if (tab3 != null) {
            BadgeDrawable badgeDrawable = tab3.getOrCreateBadge();
            badgeDrawable.setBackgroundColor(ResUtils.getColor(getContext(), R.color.xui_config_color_blue));
            badgeDrawable.setBadgeTextColor(ResUtils.getColor(getContext(), R.color.xui_config_color_white));
            // 默认最大长度是4，这里改成3看看，就是最大显示99+
            badgeDrawable.setMaxCharacterCount(3);
            badgeDrawable.setNumber(99999);
        }
    }

    @SuppressLint("UnsafeExperimentalUsageError")
    private void initTextView() {
        tvTitle.post(() -> {
            BadgeDrawable badgeDrawable = BadgeDrawable.create(getContext());
            badgeDrawable.setBackgroundColor(ResUtils.getColor(getContext(), R.color.xui_config_color_red));
            badgeDrawable.setBadgeTextColor(ResUtils.getColor(getContext(), R.color.xui_config_color_white));
            badgeDrawable.setNumber(123);
            BadgeUtils.attachBadgeDrawable(badgeDrawable, tvTitle);

            BadgeDrawable badgeDrawable2 = BadgeDrawable.create(getContext());
            badgeDrawable2.setBackgroundColor(ResUtils.getColor(getContext(), R.color.xui_config_color_waring));
            badgeDrawable2.setBadgeTextColor(ResUtils.getColor(getContext(), R.color.xui_config_color_white));
            badgeDrawable2.setBadgeGravity(BadgeDrawable.TOP_START);
            badgeDrawable2.setHorizontalOffset(-10);
            badgeDrawable2.setVerticalOffset(-30);
            badgeDrawable2.setNumber(999999);
            BadgeUtils.attachBadgeDrawable(badgeDrawable2, tvTitle);
        });
    }

    @SuppressLint("UnsafeExperimentalUsageError")
    private void initImageView() {
        ivImage.post(() -> {
            BadgeDrawable badgeDrawable = BadgeDrawable.create(getContext());
            badgeDrawable.setBackgroundColor(ResUtils.getColor(getContext(), R.color.xui_config_color_red));
            badgeDrawable.setBadgeTextColor(ResUtils.getColor(getContext(), R.color.xui_config_color_white));
            badgeDrawable.setNumber(23);
            BadgeUtils.attachBadgeDrawable(badgeDrawable, ivImage, flImage);
        });
    }

    private void initNavigationView() {
        BadgeDrawable badgeDrawable = navigationView.getOrCreateBadge(R.id.item_dashboard);
        badgeDrawable.setBackgroundColor(ResUtils.getColor(getContext(), R.color.xui_config_color_red));
        badgeDrawable.setBadgeTextColor(ResUtils.getColor(getContext(), R.color.xui_config_color_white));
        badgeDrawable.setNumber(999999);
    }

}
