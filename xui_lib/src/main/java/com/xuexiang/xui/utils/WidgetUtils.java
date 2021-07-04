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
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
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
     * @param activity activity
     */
    public static void requestFullScreen(@NonNull Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    //===============Spinner=============//

    /**
     * Spinner统一风格和选择项
     *
     * @param spinner 下拉选择框
     * @param items   选择项
     */
    public static void initSpinnerStyle(@NonNull Spinner spinner, @NonNull String[] items) {
        initSpinnerStyle(spinner);
        initSpinnerItem(spinner, items);
    }

    /**
     * Spinner统一风格
     *
     * @param spinner 下拉选择框
     */
    public static void initSpinnerStyle(@NonNull Spinner spinner) {
        // 带下拉箭头的背景
        spinner.setBackground(ResUtils.getDrawable(spinner.getContext(), R.drawable.xui_config_bg_spinner));
        ViewUtils.setPaddingEnd(spinner, ResUtils.getDimensionPixelSize(R.dimen.default_spinner_icon_padding_size));
        // 下拉选择菜单的背景
        spinner.setPopupBackgroundDrawable(ResUtils.getDrawable(spinner.getContext(), R.drawable.ms_drop_down_bg_radius));
        setSpinnerDropDownVerticalOffset(spinner);
    }

    /**
     * 初始化Spinner下拉框的选择项
     *
     * @param spinner 下拉选择框
     * @param items   选择项
     */
    public static void initSpinnerItem(@NonNull Spinner spinner, @NonNull String[] items) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(spinner.getContext(), R.layout.xui_layout_spinner_selected_item, R.id.spinner_item, items);
        adapter.setDropDownViewResource(R.layout.xui_layout_spinner_drop_down_item);
        spinner.setAdapter(adapter);
    }

    /**
     * 设置系统Spinner的下拉偏移
     *
     * @param spinner 下拉选择框
     */
    public static void setSpinnerDropDownVerticalOffset(@NonNull Spinner spinner) {
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
     * 为TabLayout增加不带水波纹的选项卡
     *
     * @param tabLayout 选项卡
     * @param text      选项文字内容
     * @param resId     选项图标
     */
    public static TabLayout.Tab addTabWithoutRipple(@NonNull TabLayout tabLayout, @Nullable CharSequence text, @DrawableRes int resId) {
        TabLayout.Tab tab = tabLayout.newTab();
        tab.setText(text);
        tab.setIcon(resId);
        tab.view.setBackgroundColor(Color.TRANSPARENT);
        tabLayout.addTab(tab);
        return tab;
    }

    /**
     * 为TabLayout增加不带水波纹的选项卡
     *
     * @param tabLayout 选项卡
     * @param text      选项文字内容
     * @param icon      选项图标
     */
    public static TabLayout.Tab addTabWithoutRipple(@NonNull TabLayout tabLayout, @Nullable CharSequence text, @Nullable Drawable icon) {
        TabLayout.Tab tab = tabLayout.newTab();
        tab.setText(text);
        tab.setIcon(icon);
        tab.view.setBackgroundColor(Color.TRANSPARENT);
        tabLayout.addTab(tab);
        return tab;
    }

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
     * @param context 上下文
     * @return loading加载框
     */
    public static LoadingDialog getLoadingDialog(@NonNull Context context) {
        return new LoadingDialog(context);
    }

    /**
     * 获取loading加载框
     *
     * @param context 上下文
     * @param message 提示文字
     * @return loading加载框
     */
    public static LoadingDialog getLoadingDialog(@NonNull Context context, @NonNull String message) {
        return new LoadingDialog(context, message);
    }

    /**
     * 更新loading加载框的提示信息
     *
     * @param loadingDialog loading加载框
     * @param message       提示文字
     * @return loading加载框
     */
    public static LoadingDialog updateLoadingMessage(LoadingDialog loadingDialog, @NonNull Context context, @NonNull String message) {
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
     * @param isDialog 是否是弹窗
     * @param context  上下文
     * @return 消息加载者
     */
    public static IMessageLoader getMessageLoader(boolean isDialog, @NonNull Context context) {
        return isDialog ? new LoadingDialog(context) : new LoadingViewLayout(context);
    }


    /**
     * 获取MiniLoadingDialog加载框
     *
     * @param context 上下文
     * @return MiniLoadingDialog加载框
     */
    public static MiniLoadingDialog getMiniLoadingDialog(@NonNull Context context) {
        return new MiniLoadingDialog(context);
    }

    /**
     * 获取MiniLoadingDialog加载框
     *
     * @param context 上下文
     * @param message 提示文字
     * @return MiniLoadingDialog加载框
     */
    public static MiniLoadingDialog getMiniLoadingDialog(@NonNull Context context, @NonNull String message) {
        return new MiniLoadingDialog(context, message);
    }


    /**
     * 隐藏底部弹窗的背景颜色(用于显示圆角）
     *
     * @param dialog 底部弹窗
     */
    public static void transparentBottomSheetDialogBackground(BottomSheetDialog dialog) {
        if (dialog != null && dialog.getWindow() != null) {
            FrameLayout frameLayout = dialog.getWindow().findViewById(R.id.design_bottom_sheet);
            if (frameLayout != null) {
                frameLayout.setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }

    /**
     * 去除窗口的背景色【解决过度绘制问题】
     *
     * @param activity 窗口
     */
    public static void transparentWindowBackground(Activity activity) {
        if (activity == null) {
            return;
        }
        transparentWindowBackground(activity.getWindow());
    }

    /**
     * 去除窗口的背景色【解决过度绘制问题】
     *
     * @param window 窗口
     */
    public static void transparentWindowBackground(Window window) {
        if (window == null) {
            return;
        }
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

}
