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

package com.xuexiang.xuidemo.fragment.components.banner;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.banner.recycler.BannerLayout;
import com.xuexiang.xuidemo.DemoDataProvider;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.adapter.RecyclerViewBannerAdapter;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xutil.tip.ToastUtils;

import butterknife.BindView;

/**
 * @author xuexiang
 * @since 2019-05-30 00:40
 */
@Page(name = "使用RecyclerView实现的Banner")
public class RecyclerViewBannerFragment extends BaseFragment implements BannerLayout.OnBannerItemClickListener {

    @BindView(R.id.bl_horizontal)
    BannerLayout blHorizontal;
    @BindView(R.id.bl_vertical)
    BannerLayout blVertical;

    private RecyclerViewBannerAdapter mAdapterHorizontal;
    private RecyclerViewBannerAdapter mAdapterVertical;


    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recyclerview_banner;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        blHorizontal.setAdapter(mAdapterHorizontal = new RecyclerViewBannerAdapter(DemoDataProvider.urls));
        blVertical.setAdapter(mAdapterVertical = new RecyclerViewBannerAdapter(DemoDataProvider.urls));
    }

    @Override
    protected void initListeners() {
        mAdapterHorizontal.setOnBannerItemClickListener(this);
        mAdapterVertical.setOnBannerItemClickListener(this);

        blHorizontal.setOnIndicatorIndexChangedListener(new BannerLayout.OnIndicatorIndexChangedListener() {
            @Override
            public void onIndexChanged(int position) {
                ToastUtils.toast("轮播到第" + (position + 1) + "个");
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        ToastUtils.toast("点击了第" + (position + 1) + "个");
    }

}
