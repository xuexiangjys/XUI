package com.xuexiang.xui.widget.statelayout;

import android.view.View;

import androidx.annotation.DrawableRes;

import java.io.Serializable;

/**
 * Model builder class to show custom state
 * @see com.xuexiang.xui.widget.statelayout.StatefulLayout#showCustom(CustomStateOptions)
 */
public class CustomStateOptions implements Serializable {

    @DrawableRes
    private int imageRes;
    private boolean isLoading;
    private String message;
    private String buttonText;
    private View.OnClickListener buttonClickListener;

    public CustomStateOptions image(@DrawableRes int val) {
        imageRes = val;
        return this;
    }

    public CustomStateOptions loading() {
        isLoading = true;
        return this;
    }

    public CustomStateOptions message(String val) {
        message = val;
        return this;
    }

    public CustomStateOptions buttonText(String val) {
        buttonText = val;
        return this;
    }

    public CustomStateOptions buttonClickListener(View.OnClickListener val) {
        buttonClickListener = val;
        return this;
    }

    public int getImageRes() {
        return imageRes;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public String getMessage() {
        return message;
    }

    public String getButtonText() {
        return buttonText;
    }

    public View.OnClickListener getClickListener() {
        return buttonClickListener;
    }

}
