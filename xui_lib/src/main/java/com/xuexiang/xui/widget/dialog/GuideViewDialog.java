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

package com.xuexiang.xui.widget.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

/**
 * 显示引导图片
 *
 * @author xuexiang
 * @since 2018/11/14 下午5:55
 */
public class GuideViewDialog extends AlertDialog {
    private LinearLayout mGuideViewLayout;
    private ImageView mImageView;
    private int[] mResourceIdList;
    private int mIndex = 0;

    public GuideViewDialog(Context context) {
        super(context);
    }

    public GuideViewDialog(Context context, int theme) {
        super(context, theme);
    }

    public GuideViewDialog(Context context, int theme, int[] resourseIdList) {
        super(context, theme);
        mResourceIdList = resourseIdList;
    }

    public GuideViewDialog(Context context, int[] resourseIdList) {
        super(context);
        mResourceIdList = resourseIdList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        setContentView(mGuideViewLayout);

    }

    private void initView() {
        mImageView = new ImageView(getContext());
        mImageView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        if (mResourceIdList != null && mResourceIdList.length > 0) {
            mImageView.setImageResource(mResourceIdList[mIndex]);
            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mIndex == (mResourceIdList.length - 1)) {
                        dismiss();
                    } else {
                        mIndex++;
                        mImageView.setImageResource(mResourceIdList[mIndex]);
                    }
                }
            });
        }
        mGuideViewLayout = new LinearLayout(getContext());
        mGuideViewLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mGuideViewLayout.setOrientation(LinearLayout.VERTICAL);
        mGuideViewLayout.setGravity(Gravity.CENTER);
        mGuideViewLayout.addView(mImageView);

        setCanceledOnTouchOutside(true);
    }

    public void setResourseId(int[] resourseId) {
        mResourceIdList = resourseId;
    }

    @Override
    public void dismiss() {
        if (mIndex == (mResourceIdList.length - 1)) {
            super.dismiss();
        } else {
            mIndex++;
            mImageView.setImageResource(mResourceIdList[mIndex]);
        }
    }

    /**
     * 显示位置
     *
     * @param pointX x轴坐标
     * @param pointY y轴坐标
     */
    public void show(int pointX, int pointY) {
        Window win = getWindow();
        WindowManager.LayoutParams params = win.getAttributes();
        //设置x坐标
        params.x = pointX;
        //设置y坐标
        params.y = pointY;
        win.setAttributes(params);
        show();
    }

    /**
     * 设置弹窗尺寸
     *
     * @param width  宽
     * @param height 高
     */
    public GuideViewDialog setDialogSize(int width, int height) {
        Window win = getWindow();
        if (win != null) {
            WindowManager.LayoutParams params = win.getAttributes();
            //设置宽
            params.width = width;
            //设置高
            params.height = height;
            win.setAttributes(params);
        }
        return this;
    }

    /**
     * 设置重心
     *
     * @param gravity
     */
    public GuideViewDialog setGravity(int gravity) {
        Window win = getWindow();
        if (win != null) {
            WindowManager.LayoutParams params = win.getAttributes();
            params.gravity = gravity;
            win.setAttributes(params);
        }
        return this;
    }

}