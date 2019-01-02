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

package com.xuexiang.xui.widget.picker.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.xuexiang.xui.R;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.ThemeUtils;
import com.xuexiang.xui.widget.picker.widget.configure.PickerOptions;
import com.xuexiang.xui.widget.picker.widget.listener.OnDismissListener;
import com.xuexiang.xui.widget.picker.widget.utils.PickerViewAnimateUtils;

/**
 * 精仿iOSPickerViewController控件
 *
 * @author xuexiang
 * @since 2019/1/1 下午6:58
 */
public class BasePickerView {

    private Context context;
    protected ViewGroup contentContainer;
    private ViewGroup rootView;//附加View 的 根View
    private ViewGroup dialogView;//附加Dialog 的 根View

    protected PickerOptions mPickerOptions;
    private OnDismissListener onDismissListener;
    private boolean dismissing;

    private Animation outAnim;
    private Animation inAnim;
    private boolean isShowing;

    protected int animGravity = Gravity.BOTTOM;

    protected Dialog mDialog;
    protected View clickView;//是通过哪个View弹出的
    private boolean isAnim = true;

    public BasePickerView(Context context) {
        this.context = context;
    }

    protected void initViews() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM);

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        if (isDialog()) {
            //如果是对话框模式
            dialogView = (ViewGroup) layoutInflater.inflate(R.layout.xui_layout_picker_view_base, null, false);
            //设置界面的背景为透明
            dialogView.setBackgroundColor(Color.TRANSPARENT);
            //这个是真正要加载选择器的父布局
            contentContainer = dialogView.findViewById(R.id.content_container);
            //设置对话框 默认左右间距屏幕30
            params.leftMargin = 30;
            params.rightMargin = 30;
            contentContainer.setLayoutParams(params);
            //创建对话框
            createDialog();
            //给背景设置点击事件,这样当点击内容以外的地方会关闭界面
            dialogView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
        } else {
            //如果只是要显示在屏幕的下方
            //decorView是activity的根View,包含 contentView 和 titleView
            if (mPickerOptions.decorView == null) {
                mPickerOptions.decorView = (ViewGroup) ((Activity) context).getWindow().getDecorView();
            }
            //将控件添加到decorView中
            rootView = (ViewGroup) layoutInflater.inflate(R.layout.xui_layout_picker_view_base, mPickerOptions.decorView, false);
            rootView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            if (mPickerOptions.backgroundId != -1) {
                rootView.setBackgroundColor(mPickerOptions.backgroundId);
            }
            //这个是真正要加载时间选取器的父布局
            contentContainer = rootView.findViewById(R.id.content_container);
            contentContainer.setLayoutParams(params);
        }
        setKeyBackCancelable(true);
    }

    protected void initAnim() {
        inAnim = getInAnimation();
        outAnim = getOutAnimation();
    }

    protected void initEvents() {
    }


    /**
     * @param v      (是通过哪个View弹出的)
     * @param isAnim 是否显示动画效果
     */
    public void show(View v, boolean isAnim) {
        this.clickView = v;
        this.isAnim = isAnim;
        show();
    }

    public void show(boolean isAnim) {
        this.isAnim = isAnim;
        show();
    }

    public void show(View v) {
        this.clickView = v;
        show();
    }


    /**
     * 添加View到根视图
     */
    public void show() {
        if (isDialog()) {
            showDialog();
        } else {
            if (isShowing()) {
                return;
            }
            isShowing = true;
            onAttached(rootView);
            rootView.requestFocus();
        }
    }


    /**
     * show的时候调用
     *
     * @param view 这个View
     */
    private void onAttached(View view) {
        mPickerOptions.decorView.addView(view);
        if (isAnim) {
            contentContainer.startAnimation(inAnim);
        }
    }


    /**
     * 检测该View是不是已经添加到根视图
     *
     * @return 如果视图已经存在该View返回true
     */
    public boolean isShowing() {
        if (isDialog()) {
            return false;
        } else {
            return rootView.getParent() != null || isShowing;
        }

    }

    public void dismiss() {
        if (isDialog()) {
            dismissDialog();
        } else {
            if (dismissing) {
                return;
            }

            if (isAnim) {
                //消失动画
                outAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        dismissImmediately();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                contentContainer.startAnimation(outAnim);
            } else {
                dismissImmediately();
            }
            dismissing = true;
        }


    }

    public void dismissImmediately() {

        mPickerOptions.decorView.post(new Runnable() {
            @Override
            public void run() {
                //从根视图移除
                mPickerOptions.decorView.removeView(rootView);
                isShowing = false;
                dismissing = false;
                if (onDismissListener != null) {
                    onDismissListener.onDismiss(BasePickerView.this);
                }
            }
        });


    }

    private Animation getInAnimation() {
        int res = PickerViewAnimateUtils.getAnimationResource(this.animGravity, true);
        return AnimationUtils.loadAnimation(context, res);
    }

    private Animation getOutAnimation() {
        int res = PickerViewAnimateUtils.getAnimationResource(this.animGravity, false);
        return AnimationUtils.loadAnimation(context, res);
    }

    public BasePickerView setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
        return this;
    }

    public void setKeyBackCancelable(boolean isCancelable) {

        ViewGroup View;
        if (isDialog()) {
            View = dialogView;
        } else {
            View = rootView;
        }

        View.setFocusable(isCancelable);
        View.setFocusableInTouchMode(isCancelable);
        if (isCancelable) {
            View.setOnKeyListener(onKeyBackListener);
        } else {
            View.setOnKeyListener(null);
        }
    }

    private View.OnKeyListener onKeyBackListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == MotionEvent.ACTION_DOWN && isShowing()) {
                dismiss();
                return true;
            }
            return false;
        }
    };

    protected BasePickerView setOutSideCancelable(boolean isCancelable) {

        if (rootView != null) {
            View view = rootView.findViewById(R.id.outmost_container);

            if (isCancelable) {
                view.setOnTouchListener(onCancelableTouchListener);
            } else {
                view.setOnTouchListener(null);
            }
        }

        return this;
    }

    /**
     * 设置对话框模式是否可以点击外部取消
     */
    public void setDialogOutSideCancelable() {
        if (mDialog != null) {
            mDialog.setCancelable(mPickerOptions.cancelable);
        }
    }


    /**
     * Called when the user touch on black overlay, in order to dismiss the dialog.
     */
    private final View.OnTouchListener onCancelableTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                dismiss();
            }
            return false;
        }
    };

    public View findViewById(int id) {
        return contentContainer.findViewById(id);
    }

    public void createDialog() {
        if (dialogView != null) {
            mDialog = new Dialog(context, R.style.XUIDialog_Custom);
            mDialog.setCancelable(mPickerOptions.cancelable);//不能点外面取消,也不能点back取消
            mDialog.setContentView(dialogView);

            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(R.style.picker_view_scale_anim);
                dialogWindow.setGravity(Gravity.CENTER);//可以改成Bottom

                setWindowMaxWidth(dialogWindow);
            }

            mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (onDismissListener != null) {
                        onDismissListener.onDismiss(BasePickerView.this);
                    }
                }
            });
        }
    }

    /**
     * 设置窗口的最大宽度
     *
     * @param dialogWindow
     */
    private void setWindowMaxWidth(Window dialogWindow) {
        final int windowWidth = getWindowWidth(dialogWindow);
        int windowHorizontalPadding = ThemeUtils.resolveDimension(context, R.attr.md_dialog_horizontal_margin, ResUtils.getDimensionPixelSize(R.dimen.default_md_dialog_horizontal_margin_phone));
        int maxWidth = ThemeUtils.resolveDimension(context, R.attr.md_dialog_max_width);
        final int calculatedWidth = windowWidth - (windowHorizontalPadding * 2);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogWindow.getAttributes());
        lp.width = Math.min(maxWidth, calculatedWidth);
        dialogWindow.setAttributes(lp);
    }

    private int getWindowWidth(Window dialogWindow) {
        WindowManager wm = dialogWindow.getWindowManager();
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    private void showDialog() {
        if (mDialog != null) {
            mDialog.show();
        }
    }

    private void dismissDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    public ViewGroup getDialogContainerLayout() {
        return contentContainer;
    }


    public Dialog getDialog() {
        return mDialog;
    }


    public boolean isDialog() {
        return false;
    }

}
