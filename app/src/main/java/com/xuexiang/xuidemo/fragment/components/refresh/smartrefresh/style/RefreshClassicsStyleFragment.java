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

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.adapter.SmartViewHolder;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xui.utils.ViewUtils;
import com.xuexiang.xui.utils.WidgetUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xuidemo.utils.DynamicTimeFormat;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import butterknife.BindView;

/**
 * @author xuexiang
 * @since 2018/12/7 上午12:45
 */
@Page(name = "经典样式")
public class RefreshClassicsStyleFragment extends BaseFragment implements SmartViewHolder.OnItemClickListener {
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;

    private ClassicsHeader mClassicsHeader;
    private Drawable mDrawableProgress;

    private TitleBar mTitleBar;


    @Override
    protected TitleBar initTitle() {
        mTitleBar = super.initTitle();
        return mTitleBar;
    }

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

        switch (Item.values()[position]) {
            case 背后固定:
                mClassicsHeader.setSpinnerStyle(SpinnerStyle.FixedBehind);
                mRefreshLayout.setPrimaryColors(0xff444444, 0xffffffff);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mDrawableProgress.setTint(0xffffffff);
                }
                /*
                 * 由于是后面才设置，需要手动更改视图的位置
                 * 如果在 onCreate 或者 xml 中设置好[SpinnerStyle] 就不用手动调整位置了
                 */
                mRefreshLayout.getLayout().bringChildToFront(mRecyclerView);
                break;
            case 尺寸拉伸:
                mClassicsHeader.setSpinnerStyle(SpinnerStyle.Scale);
                break;
            case 位置平移:
                mClassicsHeader.setSpinnerStyle(SpinnerStyle.Translate);
                break;
            case 显示时间:
                mClassicsHeader.setEnableLastTime(true);
                break;
            case 隐藏时间:
                mClassicsHeader.setEnableLastTime(false);
                break;
            case 默认主题:
                setThemeColor(R.color.colorPrimary);
                mRefreshLayout.getLayout().setBackgroundResource(android.R.color.transparent);
                mRefreshLayout.setPrimaryColors(0, 0xff666666);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mDrawableProgress.setTint(0xff666666);
                }
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
//            case 加载更多:
//                mRefreshLayout.autoLoadMore();
//                break;
            default:
                break;
        }
        mRefreshLayout.autoRefresh();
    }

    private void setThemeColor(int colorPrimary) {
        mRefreshLayout.setPrimaryColorsId(colorPrimary, android.R.color.white);
        mTitleBar.setBackgroundColor(ContextCompat.getColor(getContext(), colorPrimary));
        if (Build.VERSION.SDK_INT >= 21) {
            if (getActivity() != null) {
                getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), colorPrimary));
            }
            mDrawableProgress.setTint(0xffffffff);
        }
    }

    private enum Item {
        尺寸拉伸(R.string.item_style_spinner_scale),
        位置平移(R.string.item_style_spinner_translation),
        背后固定(R.string.item_style_spinner_behind),
        显示时间(R.string.item_style_spinner_update_on),
        隐藏时间(R.string.item_style_spinner_update_off),
        默认主题(R.string.item_style_theme_default_abstract),
        橙色主题(R.string.item_style_theme_orange_abstract),
        红色主题(R.string.item_style_theme_red_abstract),
        绿色主题(R.string.item_style_theme_green_abstract),
        蓝色主题(R.string.item_style_theme_blue_abstract);
        //        加载更多(R.string.item_style_load_more);
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
        return R.layout.fragment_refresh_classics_style;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        mClassicsHeader = (ClassicsHeader) mRefreshLayout.getRefreshHeader();
        int delta = new Random().nextInt(7 * 24 * 60 * 60 * 1000);
        mClassicsHeader.setLastUpdateTime(new Date(System.currentTimeMillis() - delta));
        mClassicsHeader.setTimeFormat(new SimpleDateFormat("更新于 MM-dd HH:mm", Locale.CHINA));
        mClassicsHeader.setTimeFormat(new DynamicTimeFormat("更新于 %s"));

        mDrawableProgress = ((ImageView) mClassicsHeader.findViewById(ClassicsHeader.ID_IMAGE_PROGRESS)).getDrawable();
        if (mDrawableProgress instanceof LayerDrawable) {
            mDrawableProgress = ((LayerDrawable) mDrawableProgress).getDrawable(0);
        }

        View view = findViewById(R.id.recyclerView);
        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;
            WidgetUtils.initRecyclerView(recyclerView);

            recyclerView.setAdapter(new BaseRecyclerAdapter<Item>(Arrays.asList(Item.values())) {
                @Override
                protected void bindData(@NonNull RecyclerViewHolder holder, int position, Item item) {
                    holder.text(android.R.id.text1, item.name());
                    holder.text(android.R.id.text2, item.nameId);
                    holder.textColorId(android.R.id.text2, R.color.xui_config_color_light_blue_gray);
                }

                @Override
                protected int getItemLayoutId(int viewType) {
                    return android.R.layout.simple_list_item_2;
                }
            });
            mRecyclerView = recyclerView;
            //触发自动刷新
            mRefreshLayout.autoRefresh();
        }
        ViewUtils.setViewsFont(mRefreshLayout);
    }

}
