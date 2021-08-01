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

package com.xuexiang.xuidemo.fragment.expands.alibaba;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;

import com.tmall.ultraviewpager.UltraViewPager;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.utils.DensityUtils;
import com.xuexiang.xui.utils.ThemeUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xuidemo.DemoDataProvider;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.adapter.UltraPagerAdapter;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xuidemo.utils.Utils;
import com.xuexiang.xuidemo.utils.XToastUtils;
import com.xuexiang.xutil.display.ScreenUtils;

import butterknife.BindView;

/**
 * @author xuexiang
 * @since 2020/4/7 12:13 AM
 */
@Page(name = "UltraViewPager\n阿里巴巴轮播控件")
public class UltraViewPagerFragment extends BaseFragment {

    @BindView(R.id.ultra_view_pager)
    UltraViewPager ultraViewPager;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_ultraviewpager;
    }

    @Override
    protected void initViews() {
        ultraViewPager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);
        float scale = 0.5625F;
        UltraPagerAdapter adapter = new UltraPagerAdapter(DemoDataProvider.getBannerList(), scale);
        ultraViewPager.setAdapter(adapter);
        ultraViewPager.setMaxHeight((int) (ScreenUtils.getScreenWidth() * scale));
        //指示器
        ultraViewPager.initIndicator();
        ultraViewPager.getIndicator()
                .setOrientation(UltraViewPager.Orientation.HORIZONTAL)
                .setFocusColor(ThemeUtils.getMainThemeColor(getContext()))
                .setNormalColor(Color.WHITE)
                .setMargin(DensityUtils.dp2px(10), DensityUtils.dp2px(10), DensityUtils.dp2px(10), DensityUtils.dp2px(10))
                .setRadius(DensityUtils.dp2px(5));
        ultraViewPager.getIndicator().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        ultraViewPager.getIndicator().build();

        ultraViewPager.setInfiniteLoop(true);
        ultraViewPager.setAutoScroll(2000);

        adapter.setOnItemClickListener((itemView, item, position) -> XToastUtils.toast("position--->" + position));
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.addAction(new TitleBar.TextAction("Github") {
            @Override
            public void performAction(View view) {
                Utils.goWeb(getContext(), "https://github.com/alibaba/UltraViewPager");
            }
        });
        return titleBar;
    }
}
