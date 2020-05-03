package com.xuexiang.xuidemo.widget;

import android.content.Context;

import com.xuexiang.xui.UIConsts;
import com.xuexiang.xui.XUI;
import com.xuexiang.xui.utils.KeyboardUtils;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.spinner.editspinner.EditSpinner;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xutil.common.StringUtils;

/**
 * 可编辑下拉框弹窗
 *
 * @author xuexiang
 * @since 2019/1/14 下午10:01
 */
public class EditSpinnerDialog {

    /**
     * 窗体
     */
    private MaterialDialog mDialog;
    /**
     * Spinner
     */
    private EditSpinner mEditSpinner;

    private OnEditListener mListener;

    /**
     * 构造签名弹窗
     *
     * @param builder
     */
    public EditSpinnerDialog(Builder builder) {
        mListener = builder.listener;
        mDialog = new MaterialDialog.Builder(builder.context)
                .title(builder.title)
                .customView(R.layout.layout_dialog_spinner, false)
                .negativeText(builder.negativeText)
                .positiveText(builder.positiveText)
                .onPositive((dialog, which) -> {
                    KeyboardUtils.hideSoftInput(mEditSpinner);
                    if (mListener != null) {
                        mListener.onEdit(mEditSpinner.getText());
                    }
                })
                .onNegative((dialog, which) -> KeyboardUtils.hideSoftInput(mEditSpinner))
                .cancelable(builder.enableCancel)
                .build();
        mEditSpinner = mDialog.findViewById(R.id.editSpinner);
        mEditSpinner.setEditTextWidth(builder.width);
        if (builder.defaultItems != null) {
            mEditSpinner.setItems(builder.defaultItems);
        }
        if (!StringUtils.isEmpty(builder.text)) {
            mEditSpinner.setText(builder.text);
        }
        if (!StringUtils.isEmpty(builder.hint)) {
            mEditSpinner.setHint(builder.hint);
        }
    }


    public EditSpinnerDialog show() {
        if (mDialog != null) {
            mDialog.show();
        }
        return this;
    }

    public EditSpinnerDialog dismiss() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
        return this;
    }

    public static Builder newBuilder(Context context) {
        return new Builder(context);
    }

    /**
     * 构造器
     */
    public static class Builder {
        Context context;
        String[] defaultItems;
        /**
         * 标题
         */
        String title = "标题";
        String negativeText = "取消";
        String positiveText = "确定";
        /**
         * 是否可以取消
         */
        boolean enableCancel = false;

        String hint;

        String text;

        int width;

        OnEditListener listener;

        public Builder(Context context) {
            this.context = context;
            if (XUI.getScreenType() == UIConsts.ScreenType.BIG_TABLET) {
                width = 300;
            } else if (XUI.getScreenType() == UIConsts.ScreenType.SMALL_TABLET) {
                width = 250;
            } else {
                width = 150;
            }
        }

        /**
         * 设置标题
         *
         * @param title
         * @return
         */
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }


        public Builder setNegativeText(String negativeText) {
            this.negativeText = negativeText;
            return this;
        }

        public Builder setPositiveText(String positiveText) {
            this.positiveText = positiveText;
            return this;
        }

        public Builder setWidth(int width) {
            this.width = width;
            return this;
        }

        /**
         * 设置能否取消
         *
         * @param enableCancel
         * @return
         */
        public Builder enableCancel(boolean enableCancel) {
            this.enableCancel = enableCancel;
            return this;
        }

        public Builder setDefaultItems(String[] defaultItems) {
            this.defaultItems = defaultItems;
            return this;
        }

        public Builder setEnableCancel(boolean enableCancel) {
            this.enableCancel = enableCancel;
            return this;
        }

        public Builder setHint(String hint) {
            this.hint = hint;
            return this;
        }

        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public Builder setOnEditListener(OnEditListener listener) {
            this.listener = listener;
            return this;
        }

        public EditSpinnerDialog build() {
            return new EditSpinnerDialog(this);
        }

        public EditSpinnerDialog show() {
            return build().show();
        }
    }

    /**
     * 编辑监听
     */
    public interface OnEditListener {
        /**
         * 编辑内容
         *
         * @param value
         */
        void onEdit(String value);
    }

}
