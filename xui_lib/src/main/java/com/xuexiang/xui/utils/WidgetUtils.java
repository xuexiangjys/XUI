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
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.xuexiang.xui.R;
import com.xuexiang.xui.adapter.recyclerview.DividerItemDecoration;
import com.xuexiang.xui.adapter.recyclerview.GridDividerItemDecoration;
import com.xuexiang.xui.widget.dialog.LoadingDialog;
import com.xuexiang.xui.widget.dialog.MiniLoadingDialog;
import com.xuexiang.xui.widget.progress.loading.IMessageLoader;
import com.xuexiang.xui.widget.progress.loading.LoadingViewLayout;

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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(spinner.getContext(), R.layout.xui_layout_spinner_selected_item, R.id.spinner_item, items);
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

    //===============recyclerView=============//

    /**
     * 初始化Grid网格RecyclerView
     *
     * @param recyclerView
     * @param spanCount     一行的数量
     * @param dividerWidth 分割线的宽度
     */
    public static void initGridRecyclerView(@NonNull RecyclerView recyclerView, int spanCount, int dividerWidth) {
        recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), spanCount));
        recyclerView.addItemDecoration(new GridDividerItemDecoration(recyclerView.getContext(), spanCount, dividerWidth));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    /**
     * @param recyclerView
     */
    public static void initGridRecyclerView(@NonNull RecyclerView recyclerView, int spanCount, int dividerWidth, int dividerColor) {
        recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), spanCount));
        recyclerView.addItemDecoration(new GridDividerItemDecoration(recyclerView.getContext(), spanCount, dividerWidth, dividerColor));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    /**
     * 初始化RecyclerView
     *
     * @param recyclerView
     */
    public static void initRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    /**
     * 初始化RecyclerView
     *
     * @param recyclerView
     */
    public static void initRecyclerView(@NonNull RecyclerView recyclerView, int dividerHeight) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), VERTICAL, dividerHeight));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    /**
     * 初始化RecyclerView
     *
     * @param recyclerView
     */
    public static void initRecyclerView(@NonNull RecyclerView recyclerView, int dividerHeight, int dividerColor) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), VERTICAL, dividerHeight, dividerColor));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
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
