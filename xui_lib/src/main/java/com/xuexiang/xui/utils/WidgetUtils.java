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

import android.content.Context;
import android.content.DialogInterface;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

/**
 * 组件工具类
 *
 * @author xuexiang
 * @date 2017/12/5 下午2:09
 */
public class WidgetUtils {

//    /**
//     * Spinner统一风格
//     * @param items
//     * @param spinner
//     */
//    public static void initSpinnerStyle(Spinner spinner, String[] items) {
//        setSpinnerDropDownVerticalOffset(spinner);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(spinner.getContext(), R.layout.nlui_layout_spinner_selected_item, R.id.spinner_item, items);
//        adapter.setDropDownViewResource(R.layout.nlui_layout_spinner_drop_down_item);
//        spinner.setAdapter(adapter);
//    }
//
//    /**
//     * 设置系统Spinner的下拉偏移
//     * @param spinner
//     */
//    public static void setSpinnerDropDownVerticalOffset(Spinner spinner) {
//        int itemHeight = ThemeUtils.resolveDimension(spinner.getContext(), R.attr.ms_item_height_size);
//        int dropdownOffset = ThemeUtils.resolveDimension(spinner.getContext(), R.attr.ms_dropdown_offset);
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//            spinner.setDropDownVerticalOffset(0);
//        } else {
//            spinner.setDropDownVerticalOffset(itemHeight + dropdownOffset);
//        }
//    }

}
