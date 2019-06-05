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

package com.xuexiang.xuidemo.fragment.utils.view;

import androidx.core.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.utils.ViewUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.dialog.bottomsheet.BottomSheet;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;

import butterknife.BindView;

import static com.xuexiang.xui.utils.ViewUtils.Direction.BOTTOM_TO_TOP;
import static com.xuexiang.xui.utils.ViewUtils.Direction.LEFT_TO_RIGHT;
import static com.xuexiang.xui.utils.ViewUtils.Direction.TOP_TO_BOTTOM;

/**
 * @author xuexiang
 * @since 2019/1/3 下午2:07
 */
@Page(name = "控件动画")
public class ViewAnimationFragment extends BaseFragment {

    @BindView(R.id.popup)
    TextView mPopupView;

    @BindView(R.id.container)
    ViewGroup mContainer;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_view_animation;
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.addAction(new TitleBar.ImageAction(R.drawable.icon_topbar_overflow) {
            @Override
            public void performAction(View view) {
                showBottomSheetList();
            }
        });
        return titleBar;
    }

    @Override
    protected void initViews() {

    }


    private void showBottomSheetList() {
        new BottomSheet.BottomListSheetBuilder(getActivity())
                .addItem("Fade 动画")
                .addItem("Slide（上进上出） 动画")
                .addItem("Slide（左进右出） 动画")
                .addItem("背景闪烁（黄色）")
                .setOnSheetItemClickListener(new BottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(BottomSheet dialog, View itemView, int position, String tag) {
                        dialog.dismiss();
                        switch (position) {
                            case 0:
                                if (mPopupView.getVisibility() == View.GONE) {
                                    mPopupView.setText("以 Fade 动画显示本浮层");
                                    ViewUtils.fadeIn(mPopupView, 500, null);
                                } else {
                                    mPopupView.setText("以 Fade 动画隐藏本浮层");
                                    ViewUtils.fadeOut(mPopupView, 500, null);
                                }
                                break;
                            case 1:
                                if (mPopupView.getVisibility() == View.GONE) {
                                    mPopupView.setText("以 Slide（上进上出）动画显示本浮层");
                                    ViewUtils.slideIn(mPopupView, 500, null, TOP_TO_BOTTOM);
                                } else {
                                    mPopupView.setText("以 Slide（上进上出）动画隐藏本浮层");
                                    ViewUtils.slideOut(mPopupView, 500, null, BOTTOM_TO_TOP);
                                }
                                break;
                            case 2:
                                if (mPopupView.getVisibility() == View.GONE) {
                                    mPopupView.setText("以 Slide（左进右出）动画显示本浮层");
                                    ViewUtils.slideIn(mPopupView, 500, null, LEFT_TO_RIGHT);
                                } else {
                                    mPopupView.setText("以 Slide（左进右出）动画隐藏本浮层");
                                    ViewUtils.slideOut(mPopupView, 500, null, LEFT_TO_RIGHT);
                                }
                                break;
                            case 3:
                                ViewUtils.playBackgroundBlinkAnimation(mContainer, ContextCompat.getColor(getContext(), R.color.xui_config_color_pure_yellow));
                                break;
                            default:
                                break;
                        }
                    }
                })
                .build()
                .show();
    }
}
