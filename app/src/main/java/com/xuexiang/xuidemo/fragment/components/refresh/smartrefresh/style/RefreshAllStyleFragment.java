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

package com.xuexiang.xuidemo.fragment.components.refresh.smartrefresh.style;

import android.content.Context;
import android.view.View;

import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.adapter.SmartRecyclerAdapter;
import com.scwang.smartrefresh.layout.adapter.SmartViewHolder;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.utils.WidgetUtils;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;

import java.lang.reflect.Constructor;
import java.util.Arrays;

import butterknife.BindView;

import static android.R.layout.simple_list_item_2;

/**
 * @author xuexiang
 * @since 2018/12/7 下午2:32
 */
@Page(name = "样式大全")
public class RefreshAllStyleFragment extends BaseFragment implements SmartViewHolder.OnItemClickListener {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;

    /**
     * 点击
     *
     * @param itemView
     * @param position
     */
    @Override
    public void onItemClick(View itemView, int position) {
        if (!RefreshState.None.equals(mRefreshLayout.getState())) {
            return;
        }

        Item item = Item.values()[position];
        RefreshHeader header = getRefreshHeader(item);
        if (header != null) {
            mRefreshLayout.setRefreshHeader(header);
            mRefreshLayout.autoRefresh();
        }
    }

    private RefreshHeader getRefreshHeader(Item item) {
        try {
            Class<?> headerClass = Class.forName("com.scwang.smartrefresh.header." + item.name());
            Constructor<?> constructor = headerClass.getConstructor(Context.class);
            return (RefreshHeader) constructor.newInstance(getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private enum Item {
        BezierCircleHeader(R.string.item_head_style_bezier_circle),
        DeliveryHeader(R.string.item_head_style_delivery),
        DropBoxHeader(R.string.item_head_style_drop_box),
        FunGameBattleCityHeader(R.string.item_head_style_fun_game_battle_city),
        FunGameHitBlockHeader(R.string.item_head_style_fun_game_hit_block),
        PhoenixHeader(R.string.item_head_style_phoenix),
        StoreHouseHeader(R.string.item_head_style_store_house),
        TaurusHeader(R.string.item_head_style_taurus),
        WaterDropHeader(R.string.item_head_style_water_drop),
        WaveSwipeHeader(R.string.item_head_style_wave_swipe);

        public int nameId;

        Item(@StringRes int nameId) {
            this.nameId = nameId;
        }
    }

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_refresh_all_style;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        WidgetUtils.initRecyclerView(mRecyclerView);

        mRecyclerView.setAdapter(new SmartRecyclerAdapter<Item>(Arrays.asList(Item.values()), simple_list_item_2, this) {
            @Override
            protected void onBindViewHolder(SmartViewHolder holder, Item model, int position) {
                holder.text(android.R.id.text1, model.name());
                holder.text(android.R.id.text2, model.nameId);
                holder.textColorId(android.R.id.text2, R.color.xui_config_color_light_blue_gray);
            }
        });
        mRefreshLayout.autoRefresh();//第一次进入触发自动刷新，演示效果

    }

}
