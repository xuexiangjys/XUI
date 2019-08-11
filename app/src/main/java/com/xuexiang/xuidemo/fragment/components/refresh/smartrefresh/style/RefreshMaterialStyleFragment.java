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

import android.os.Build;
import android.view.View;

import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.adapter.SmartRecyclerAdapter;
import com.scwang.smartrefresh.layout.adapter.SmartViewHolder;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.utils.WidgetUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;

import java.util.Arrays;

import butterknife.BindView;

import static android.R.layout.simple_list_item_2;

/**
 * @author xuexiang
 * @since 2018/12/7 上午1:43
 */
@Page(name = "Material Design风格")
public class RefreshMaterialStyleFragment extends BaseFragment implements SmartViewHolder.OnItemClickListener{

    private TitleBar mTitleBar;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    private MaterialHeader mMaterialHeader;

    /**
     * 点击
     *
     * @param itemView
     * @param position
     */
    @Override
    public void onItemClick(View itemView, int position) {
        if (!RefreshState.None.equals(mRefreshLayout.getState())) return;

        switch (Item.values()[position]) {
            case 内容不偏移:
                mRefreshLayout.setEnableHeaderTranslationContent(false);
                break;
            case 内容跟随偏移:
                mRefreshLayout.setEnableHeaderTranslationContent(true);
                break;
            case 打开背景:
                mMaterialHeader.setShowBezierWave(true);
                break;
            case 关闭背景:
                mMaterialHeader.setShowBezierWave(false);
                break;
            case 蓝色主题:
                setThemeColor(R.color.colorPrimary);
                break;
            case 绿色主题:
                setThemeColor(android.R.color.holo_green_light);
                break;
            case 红色主题:
                setThemeColor(android.R.color.holo_red_light);
                break;
            case 橙色主题:
                setThemeColor(android.R.color.holo_orange_light);
                break;
        }
        mRefreshLayout.autoRefresh();
    }

    private void setThemeColor(int colorPrimary) {
        mRefreshLayout.setPrimaryColorsId(colorPrimary, android.R.color.white);
        mTitleBar.setBackgroundColor(ContextCompat.getColor(getContext(), colorPrimary));
        if (Build.VERSION.SDK_INT >= 21) {
            getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), colorPrimary));
        }
    }

    private enum Item {
        打开背景(R.string.item_style_bezier_on),
        关闭背景(R.string.item_style_bezier_off),
        内容不偏移(R.string.item_style_content_translation_off),
        内容跟随偏移(R.string.item_style_content_translation_on),
        橙色主题(R.string.item_style_theme_orange_abstract),
        红色主题(R.string.item_style_theme_red_abstract),
        绿色主题(R.string.item_style_theme_green_abstract),
        蓝色主题(R.string.item_style_theme_blue_abstract),
        ;
        public int nameId;
        Item(@StringRes int nameId) {
            this.nameId = nameId;
        }
    }

    @Override
    protected TitleBar initTitle() {
        mTitleBar = super.initTitle();
        return mTitleBar;
    }
    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_refresh_material_style;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        mMaterialHeader = (MaterialHeader)mRefreshLayout.getRefreshHeader();

        View view = findViewById(R.id.recyclerView);
        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;
            WidgetUtils.initRecyclerView(recyclerView);

            recyclerView.setAdapter(new SmartRecyclerAdapter<Item>(Arrays.asList(Item.values()), simple_list_item_2,this) {
                @Override
                protected void onBindViewHolder(SmartViewHolder holder, Item model, int position) {
                    holder.text(android.R.id.text1, model.name());
                    holder.text(android.R.id.text2, model.nameId);
                    holder.textColorId(android.R.id.text2, R.color.xui_config_color_light_blue_gray);
                }
            });
        }
        mRefreshLayout.autoRefresh();//第一次进入触发自动刷新，演示效果
    }
}
