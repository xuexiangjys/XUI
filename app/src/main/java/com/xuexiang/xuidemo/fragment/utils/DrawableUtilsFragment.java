/*
 * Copyright (C) 2019 xuexiangjys(xuexiangjys@163.com)
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

package com.xuexiang.xuidemo.fragment.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.utils.DensityUtils;
import com.xuexiang.xui.utils.DrawableUtils;
import com.xuexiang.xui.utils.ViewUtils;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;

import butterknife.BindView;

/**
 * {@link DrawableUtils} 的使用示例。
 *
 * @author xuexiang
 * @since 2019/1/3 下午3:50
 */
@Page(name = "DrawableUtils", extra = R.drawable.ic_util_drawable)
public class DrawableUtilsFragment extends BaseFragment {

    @BindView(R.id.createFromView)
    Button mCreateFromViewButton;
    @BindView(R.id.solidImage)
    ImageView mSolidImageView;
    @BindView(R.id.circleGradient)
    ImageView mCircleGradientView;
    @BindView(R.id.tintColor)
    ImageView mTintColorImageView;
    @BindView(R.id.tintColorOrigin)
    ImageView mTintColorOriginImageView;
    @BindView(R.id.separator)
    View mSeparatorView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_utils_drawable;
    }

    @Override
    protected void initViews() {
        initContent();
    }

    private void initContent() {
        int commonShapeSize = getResources().getDimensionPixelSize(R.dimen.drawable_utils_common_shape_size);
        int commonShapeRadius = DensityUtils.dp2px(getContext(), 10);

        // 创建一张指定大小的纯色图片，支持圆角
        BitmapDrawable solidImageBitmapDrawable = DrawableUtils.createDrawableWithSize(getResources(), commonShapeSize, commonShapeSize, commonShapeRadius, ContextCompat.getColor(getContext(), R.color.app_color_theme_3));
        mSolidImageView.setImageDrawable(solidImageBitmapDrawable);

        // 创建一张圆形渐变图片，支持圆角
        GradientDrawable gradientCircleGradientDrawable = DrawableUtils.createCircleGradientDrawable(ContextCompat.getColor(getContext(), R.color.app_color_theme_4),
                ContextCompat.getColor(getContext(), R.color.xui_config_color_transparent), commonShapeRadius, 0.5f, 0.5f);
        mCircleGradientView.setImageDrawable(gradientCircleGradientDrawable);

        // 设置 Drawable 的颜色
        // 创建两张表现相同的图片
        BitmapDrawable tintColorBitmapDrawable = DrawableUtils.createDrawableWithSize(getResources(), commonShapeSize, commonShapeSize, commonShapeRadius, ContextCompat.getColor(getContext(), R.color.app_color_theme_1));
        BitmapDrawable tintColorOriginBitmapDrawable = DrawableUtils.createDrawableWithSize(getResources(), commonShapeSize, commonShapeSize, commonShapeRadius, ContextCompat.getColor(getContext(), R.color.app_color_theme_1));
        // 其中一张重新设置颜色
        DrawableUtils.setDrawableTintColor(tintColorBitmapDrawable, ContextCompat.getColor(getContext(), R.color.app_color_theme_7));
        mTintColorImageView.setImageDrawable(tintColorBitmapDrawable);
        mTintColorOriginImageView.setImageDrawable(tintColorOriginBitmapDrawable);

        // 创建带上分隔线或下分隔线的 Drawable
        LayerDrawable separatorLayerDrawable = DrawableUtils.createItemSeparatorBg(ContextCompat.getColor(getContext(), R.color.app_color_theme_7),
                ContextCompat.getColor(getContext(), R.color.app_color_theme_6), DensityUtils.dp2px(getContext(), 2), true);
        ViewUtils.setBackgroundKeepingPadding(mSeparatorView, separatorLayerDrawable);

        // 从一个 View 创建 Bitmap
        mCreateFromViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MaterialDialog dialog = new MaterialDialog.Builder(getContext())
                        .customView(R.layout.dialog_drawable_utils_createfromview, false)
                        .title("示例效果")
                        .build();
                ImageView displayImageView = dialog.findViewById(R.id.createFromViewDisplay);
                Bitmap createFromViewBitmap = DrawableUtils.createBitmapFromView(getRootView());
                displayImageView.setImageBitmap(createFromViewBitmap);

                displayImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
    }


}
