/*
 * Copyright (C) 2018 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.xuexiang.xui.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.xuexiang.xui.R;
import com.xuexiang.xui.XUI;
import com.xuexiang.xui.adapter.recyclerview.DividerItemDecoration;
import com.xuexiang.xui.adapter.recyclerview.GridDividerItemDecoration;
import com.xuexiang.xui.adapter.recyclerview.XGridLayoutManager;
import com.xuexiang.xui.adapter.recyclerview.XLinearLayoutManager;
import com.xuexiang.xui.widget.dialog.LoadingDialog;
import com.xuexiang.xui.widget.dialog.MiniLoadingDialog;
import com.xuexiang.xui.widget.progress.loading.IMessageLoader;
import com.xuexiang.xui.widget.progress.loading.LoadingViewLayout;

import java.util.List;

import static androidx.recyclerview.widget.OrientationHelper.VERTICAL;

/**
 * 组件工具类
 *
 * @author xuexiang
 * @since 2018/11/26 下午2:54
 */
public final class WidgetUtils {

    private WidgetUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 让Activity全屏显示
     *
     * @param activity
     */
    public static void requestFullScreen(@NonNull Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    //===============Spinner=============//

    /**
     * Spinner统一风格
     *
     * @param items
     * @param spinner
     */
    public static void initSpinnerStyle(Spinner spinner, String[] items) {
        setSpinnerDropDownVerticalOffset(spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(spinner.getContext(), R.layout.xui_layout_spinner_selected_item, R.id.spinner_item, items);
        adapter.setDropDownViewResource(R.layout.xui_layout_spinner_drop_down_item);
        spinner.setAdapter(adapter);
    }

    /**
     * 设置系统Spinner的下拉偏移
     *
     * @param spinner
     */
    public static void setSpinnerDropDownVerticalOffset(Spinner spinner) {
        int itemHeight = ThemeUtils.resolveDimension(spinner.getContext(), R.attr.ms_item_height_size);
        int dropdownOffset = ThemeUtils.resolveDimension(spinner.getContext(), R.attr.ms_dropdown_offset);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            spinner.setDropDownVerticalOffset(0);
        } else {
            spinner.setDropDownVerticalOffset(itemHeight + dropdownOffset);
        }
    }

    //===============TabLayout=============//

    /**
     * 设置TabLayout选项卡的字体
     *
     * @param tabLayout 选项卡
     */
    public static void setTabLayoutTextFont(TabLayout tabLayout) {
        setTabLayoutTextFont(tabLayout, XUI.getDefaultTypeface());
    }

    /**
     * 设置TabLayout选项卡的字体
     *
     * @param tabLayout 选项卡
     * @param typeface  字体
     */
    public static void setTabLayoutTextFont(TabLayout tabLayout, Typeface typeface) {
        if (tabLayout == null || typeface == null) {
            return;
        }
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int i = 0; i < tabsCount; i++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(i);
            int tabCount = vgTab.getChildCount();
            for (int j = 0; j < tabCount; j++) {
                View tabViewChild = vgTab.getChildAt(j);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(typeface);
                }
            }
        }
    }

    //===============recyclerView=============//

    /**
     * 初始化Grid网格RecyclerView
     *
     * @param recyclerView
     * @param spanCount    一行的数量
     */
    public static void initGridRecyclerView(@NonNull RecyclerView recyclerView, int spanCount) {
        recyclerView.setLayoutManager(new XGridLayoutManager(recyclerView.getContext(), spanCount));
        recyclerView.addItemDecoration(new GridDividerItemDecoration(recyclerView.getContext(), spanCount));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    /**
     * 初始化Grid网格RecyclerView
     *
     * @param recyclerView
     * @param spanCount    一行的数量
     * @param dividerWidth 分割线的宽度
     */
    public static void initGridRecyclerView(@NonNull RecyclerView recyclerView, int spanCount, int dividerWidth) {
        recyclerView.setLayoutManager(new XGridLayoutManager(recyclerView.getContext(), spanCount));
        recyclerView.addItemDecoration(new GridDividerItemDecoration(recyclerView.getContext(), spanCount, dividerWidth));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    /**
     * 初始化Grid网格RecyclerView
     *
     * @param recyclerView
     * @param spanCount    一行的数量
     * @param dividerWidth 分割线宽度
     * @param dividerColor 分割线的颜色
     */
    public static void initGridRecyclerView(@NonNull RecyclerView recyclerView, int spanCount, int dividerWidth, int dividerColor) {
        recyclerView.setLayoutManager(new XGridLayoutManager(recyclerView.getContext(), spanCount));
        recyclerView.addItemDecoration(new GridDividerItemDecoration(recyclerView.getContext(), spanCount, dividerWidth, dividerColor));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    /**
     * 初始化Grid网格RecyclerView
     *
     * @param recyclerView
     * @param canScroll    是否支持滑动
     * @param spanCount    一行的数量
     * @param dividerWidth 分割线宽度
     * @param dividerColor 分割线的颜色
     */
    public static void initGridRecyclerView(@NonNull RecyclerView recyclerView, boolean canScroll, int spanCount, int dividerWidth, int dividerColor) {
        recyclerView.setLayoutManager(new XGridLayoutManager(recyclerView.getContext(), spanCount).setScrollEnabled(canScroll));
        recyclerView.addItemDecoration(new GridDividerItemDecoration(recyclerView.getContext(), spanCount, dividerWidth, dividerColor));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    /**
     * 初始化RecyclerView
     *
     * @param recyclerView
     */
    public static void initRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new XLinearLayoutManager(recyclerView.getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    /**
     * 初始化RecyclerView
     *
     * @param recyclerView
     * @param dividerHeight 分割线的高度
     */
    public static void initRecyclerView(@NonNull RecyclerView recyclerView, int dividerHeight) {
        recyclerView.setLayoutManager(new XLinearLayoutManager(recyclerView.getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), VERTICAL, dividerHeight));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }


    /**
     * 初始化RecyclerView
     *
     * @param recyclerView
     * @param dividerHeight 分割线的高度
     * @param dividerColor  分割线的颜色
     */
    public static void initRecyclerView(@NonNull RecyclerView recyclerView, int dividerHeight, int dividerColor) {
        recyclerView.setLayoutManager(new XLinearLayoutManager(recyclerView.getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), VERTICAL, dividerHeight, dividerColor));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    /**
     * 初始化RecyclerView
     *
     * @param recyclerView
     * @param canScroll     是否支持滑动
     * @param dividerHeight 分割线的高度
     * @param dividerColor  分割线的颜色
     */
    public static void initRecyclerView(@NonNull RecyclerView recyclerView, boolean canScroll, int dividerHeight, int dividerColor) {
        recyclerView.setLayoutManager(new XLinearLayoutManager(recyclerView.getContext()).setScrollEnabled(canScroll));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), VERTICAL, dividerHeight, dividerColor));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }


    /**
     * 获取局部变化的值集合
     *
     * @param payloads 变化载体
     * @return 局部变化的值集合
     */
    public static Bundle getChangePayload(List<Object> payloads) {
        if (payloads == null || payloads.isEmpty()) {
            return null;
        }

        for (Object payload : payloads) {
            if (payload instanceof Bundle) {
                return (Bundle) payload;
            }
        }
        return null;
    }

    //===============Loading=============//

    /**
     * 获取loading加载框
     *
     * @param context
     * @return
     */
    public static LoadingDialog getLoadingDialog(Context context) {
        return new LoadingDialog(context);
    }

    /**
     * 获取loading加载框
     *
     * @param context
     * @param message 提示文字
     * @return
     */
    public static LoadingDialog getLoadingDialog(Context context, String message) {
        return new LoadingDialog(context, message);
    }

    /**
     * 更新loading加载框的提示信息
     *
     * @param loadingDialog loading加载框
     * @param message       提示文字
     * @return
     */
    public static LoadingDialog updateLoadingMessage(LoadingDialog loadingDialog, Context context, String message) {
        if (loadingDialog == null) {
            loadingDialog = getLoadingDialog(context);
        }
        loadingDialog.updateMessage(message);
        loadingDialog.show();
        return loadingDialog;
    }


    /**
     * 获取IMessageLoader
     *
     * @param isDialog
     * @param context
     * @return
     */
    public static IMessageLoader getMessageLoader(boolean isDialog, Context context) {
        return isDialog ? new LoadingDialog(context) : new LoadingViewLayout(context);
    }


    /**
     * 获取MiniLoadingDialog加载框
     *
     * @param context
     * @return
     */
    public static MiniLoadingDialog getMiniLoadingDialog(Context context) {
        return new MiniLoadingDialog(context);
    }

    /**
     * 获取MiniLoadingDialog加载框
     *
     * @param context
     * @param message 提示文字
     * @return
     */
    public static MiniLoadingDialog getMiniLoadingDialog(Context context, String message) {
        return new MiniLoadingDialog(context, message);
    }

}
