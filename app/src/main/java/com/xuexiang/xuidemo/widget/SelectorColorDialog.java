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

package com.xuexiang.xuidemo.widget;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.xuexiang.xui.widget.dialog.materialdialog.CustomMaterialDialog;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xutil.display.ScreenUtils;

/**
 * 颜色选择器
 *
 * @author xuexiang
 * @since 2018/12/28 上午10:15
 */
public class SelectorColorDialog extends CustomMaterialDialog {

    private OnColorSelectedListener mOnColorSelectedListener;

    /**
     * 构造窗体
     *
     * @param context
     */
    public SelectorColorDialog(Context context, OnColorSelectedListener listener) {
        super(context);
        mOnColorSelectedListener = listener;
    }

    @Override
    protected MaterialDialog.Builder getDialogBuilder(Context context) {
        return new MaterialDialog.Builder(context)
                .customView(getContentView(context), false);
    }

    private View getContentView(final Context context) {
        GridView gv = new GridView(context);
        gv.setNumColumns(4);
        gv.setAdapter(new BaseAdapter() {
            int[] colors = new int[]{Color.TRANSPARENT, 0xffffffff, 0xff000000, 0xffe51c23, 0xffE84E40, 0xff9c27b0, 0xff673ab7,
                    0xff3f51b5, 0xff5677fc, 0xff03a9f4, 0xff00bcd4, 0xff009688, 0xff259b24, 0xff8bc34a, 0xffcddc39,
                    0xffffeb3b, 0xffffc107, 0xffff9800, 0xffff5722, 0xff795548};

            @Override
            public int getCount() {
                return colors.length;
            }

            @Override
            public Object getItem(int position) {
                return colors[position];
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                View v = new View(context);
                v.setBackgroundColor(colors[position]);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnColorSelectedListener != null) {
                            mOnColorSelectedListener.onColorSelected(colors[position]);
                        }
                        dismiss();
                    }
                });
                GridView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                        (int) (ScreenUtils.getScreenWidth() / 5f));
                v.setLayoutParams(lp);
                return v;
            }
        });
        return gv;
    }

    @Override
    protected void initViews(Context context) {

    }


    /**
     * 颜色选择
     */
    public interface OnColorSelectedListener {
        /**
         * 颜色选择
         *
         * @param color
         */
        void onColorSelected(int color);
    }
}
