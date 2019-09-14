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

package com.xuexiang.xui.widget.dialog.materialdialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.xuexiang.xui.utils.KeyboardUtils;
import com.xuexiang.xui.widget.dialog.materialdialog.internal.MDRootLayout;

/**
 * @author Aidan Follestad (afollestad)
 */
class DialogBase extends Dialog implements DialogInterface.OnShowListener {

    protected MDRootLayout view;
    private OnShowListener showListener;

    DialogBase(Context context, int theme) {
        super(context, theme);
    }

    @Override
    public  <T extends View> T findViewById(int id) {
        return view.findViewById(id);
    }

    @Override
    public final void setOnShowListener(OnShowListener listener) {
        showListener = listener;
    }

    final void setOnShowListenerInternal() {
        super.setOnShowListener(this);
    }

    final void setViewInternal(View view) {
        super.setContentView(view);
    }

    @Override
    public void onShow(DialogInterface dialog) {
        if (showListener != null) {
            showListener.onShow(dialog);
        }
    }

    @Override
    @Deprecated
    public void setContentView(int layoutResID) throws IllegalAccessError {
        throw new IllegalAccessError(
                "setContentView() is not supported in MaterialDialog. Specify a custom view in the Builder instead.");
    }

    @Override
    @Deprecated
    public void setContentView(@NonNull View view) throws IllegalAccessError {
        throw new IllegalAccessError(
                "setContentView() is not supported in MaterialDialog. Specify a custom view in the Builder instead.");
    }

    @Override
    @Deprecated
    public void setContentView(@NonNull View view, ViewGroup.LayoutParams params)
            throws IllegalAccessError {
        throw new IllegalAccessError(
                "setContentView() is not supported in MaterialDialog. Specify a custom view in the Builder instead.");
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        KeyboardUtils.dispatchTouchEvent(ev, this);
        return super.onTouchEvent(ev);
    }
}
