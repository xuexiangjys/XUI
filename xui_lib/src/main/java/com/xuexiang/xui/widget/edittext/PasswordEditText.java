package com.xuexiang.xui.widget.edittext;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;

import com.xuexiang.xui.R;
import com.xuexiang.xui.utils.DensityUtils;
import com.xuexiang.xui.utils.ResUtils;

/**
 * 支持显示密码的输入框
 *
 * @author xuexiang
 * @since 2019/1/14 下午10:08
 */
public class PasswordEditText extends AppCompatEditText {
    /**
     * 增大点击区域
     */
    private int mExtraClickArea;

    private final static int ALPHA_ICON_ENABLED = (int) (255 * 0.54f);
    private final static int ALPHA_ICON_DISABLED = (int) (255 * 0.38f);

    private Drawable mShowPwDrawable;
    private Drawable mHidePwDrawable;
    private boolean mPasswordVisible;
    private boolean mIsRTL;
    private boolean mShowingIcon;
    private boolean mSetErrorCalled;
    private boolean mHoverShowsPw;
    private boolean mHandlingHoverEvent;
    private PasswordTransformationMethod mTransformationMethod;

    public PasswordEditText(Context context) {
        this(context, null);
    }

    public PasswordEditText(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.PasswordEditTextStyle);
    }

    public PasswordEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs, defStyleAttr);
    }

    public void initAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        mExtraClickArea = DensityUtils.dp2px(20);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PasswordEditText, defStyleAttr, 0);
        boolean useNonMonospaceFont;
        boolean enableIconAlpha;
        try {
            mShowPwDrawable = ResUtils.getDrawableAttrRes(getContext(), typedArray, R.styleable.PasswordEditText_pet_iconShow);
            if (mShowPwDrawable == null) {
                mShowPwDrawable = ResUtils.getVectorDrawable(getContext(), R.drawable.pet_icon_visibility_24dp);
            }
            mHidePwDrawable = ResUtils.getDrawableAttrRes(getContext(), typedArray, R.styleable.PasswordEditText_pet_iconHide);
            if (mHidePwDrawable == null) {
                mHidePwDrawable = ResUtils.getVectorDrawable(getContext(), R.drawable.pet_icon_visibility_off_24dp);
            }
            mHoverShowsPw = typedArray.getBoolean(R.styleable.PasswordEditText_pet_hoverShowsPw, false);
            useNonMonospaceFont = typedArray.getBoolean(R.styleable.PasswordEditText_pet_nonMonospaceFont, false);
            enableIconAlpha = typedArray.getBoolean(R.styleable.PasswordEditText_pet_enableIconAlpha, true);
            boolean isAsteriskStyle = typedArray.getBoolean(R.styleable.PasswordEditText_pet_isAsteriskStyle, false);
            if (isAsteriskStyle) {
                mTransformationMethod = AsteriskPasswordTransformationMethod.getInstance();
            } else {
                mTransformationMethod = PasswordTransformationMethod.getInstance();
            }
        } finally {
            typedArray.recycle();
        }

        if (enableIconAlpha) {
            mHidePwDrawable.setAlpha(ALPHA_ICON_ENABLED);
            mShowPwDrawable.setAlpha(ALPHA_ICON_DISABLED);
        }

        if (useNonMonospaceFont) {
            setTypeface(Typeface.DEFAULT);
        }

        mIsRTL = isRtl();

        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence seq, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    if (mSetErrorCalled) {
                        // resets drawables after setError was called as this leads to icons
                        // not changing anymore afterwards
                        setCompoundDrawables(null, null, null, null);
                        mSetErrorCalled = false;
                        showPasswordVisibilityIndicator(true);
                    }
                    if (!mShowingIcon) {
                        showPasswordVisibilityIndicator(true);
                    }
                } else {
                    // hides the indicator if no text inside text field
                    mPasswordVisible = false;
                    handlePasswordInputVisibility();
                    showPasswordVisibilityIndicator(false);
                }

            }
        });

        handlePasswordInputVisibility();
    }

    public PasswordEditText setExtraClickAreaSize(int extraClickArea) {
        mExtraClickArea = extraClickArea;
        return this;
    }

    /**
     * 设置密码输入框的样式
     *
     * @param transformationMethod
     * @return
     */
    public PasswordEditText setPasswordTransformationMethod(PasswordTransformationMethod transformationMethod) {
        mTransformationMethod = transformationMethod;
        return this;
    }

    /**
     * 设置密码输入框的样式
     *
     * @param isAsteriskStyle
     * @return
     */
    public PasswordEditText setIsAsteriskStyle(boolean isAsteriskStyle) {
        if (isAsteriskStyle) {
            mTransformationMethod = AsteriskPasswordTransformationMethod.getInstance();
        } else {
            mTransformationMethod = PasswordTransformationMethod.getInstance();
        }
        return this;
    }

    private boolean isRtl() {
        return getLayoutDirection() == LAYOUT_DIRECTION_RTL;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        return new SavedState(superState, mShowingIcon, mPasswordVisible);
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        mShowingIcon = savedState.isShowingIcon();
        mPasswordVisible = savedState.isPasswordVisible();
        handlePasswordInputVisibility();
        showPasswordVisibilityIndicator(mShowingIcon);
    }

    @Override
    public void setError(CharSequence error) {
        super.setError(error);
        mSetErrorCalled = true;

    }

    @Override
    public void setError(CharSequence error, Drawable icon) {
        super.setError(error, icon);
        mSetErrorCalled = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mShowingIcon) {
            return super.onTouchEvent(event);
        } else {
            boolean touchable = isTouchable(event);
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (mHoverShowsPw) {
                        if (touchable) {
                            togglePasswordIconVisibility();
                            // prevent keyboard from coming up
                            event.setAction(MotionEvent.ACTION_CANCEL);
                            mHandlingHoverEvent = true;
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (mHandlingHoverEvent || touchable) {
                        togglePasswordIconVisibility();
                        // prevent keyboard from coming up
                        event.setAction(MotionEvent.ACTION_CANCEL);
                        mHandlingHoverEvent = false;
                    }
                    break;
                default:
                    break;
            }
            return super.onTouchEvent(event);
        }
    }

    private boolean isTouchable(MotionEvent event) {
        boolean touchable;
        if (mIsRTL) {
            touchable = event.getX() > getPaddingLeft() - mExtraClickArea && event.getX() < getPaddingLeft() + mShowPwDrawable.getIntrinsicWidth() + mExtraClickArea;
        } else {
            touchable = event.getX() > getWidth() - getPaddingRight() - mShowPwDrawable.getIntrinsicWidth() - mExtraClickArea && event.getX() < getWidth() - getPaddingRight() + mExtraClickArea;
        }
        return touchable;
    }


    private void showPasswordVisibilityIndicator(boolean shouldShowIcon) {
        if (shouldShowIcon) {
            Drawable drawable = mPasswordVisible ? mShowPwDrawable : mHidePwDrawable;
            mShowingIcon = true;
            setCompoundDrawablesWithIntrinsicBounds(mIsRTL ? drawable : null, null, mIsRTL ? null : drawable, null);
        } else {
            // reset drawable
            setCompoundDrawables(null, null, null, null);
            mShowingIcon = false;
        }
    }

    /**
     * This method toggles the visibility of the icon and takes care of switching the input type
     * of the view to be able to see the password afterwards.
     * <p>
     * This method may only be called if there is an icon visible
     */
    private void togglePasswordIconVisibility() {
        mPasswordVisible = !mPasswordVisible;
        handlePasswordInputVisibility();
        showPasswordVisibilityIndicator(true);
    }

    /**
     * This method is called when restoring the state (e.g. on orientation change)
     */
    private void handlePasswordInputVisibility() {
        int selectionStart = getSelectionStart();
        int selectionEnd = getSelectionEnd();
        if (mPasswordVisible) {
            setTransformationMethod(null);
        } else {
            setTransformationMethod(mTransformationMethod);
        }
        setSelection(selectionStart, selectionEnd);

    }

    /**
     * Convenience class to save / restore the state of icon.
     */
    protected static class SavedState extends BaseSavedState {

        private final boolean mShowingIcon;
        private final boolean mPasswordVisible;

        private SavedState(Parcelable superState, boolean sI, boolean pV) {
            super(superState);
            mShowingIcon = sI;
            mPasswordVisible = pV;
        }

        private SavedState(Parcel in) {
            super(in);
            mShowingIcon = in.readByte() != 0;
            mPasswordVisible = in.readByte() != 0;
        }

        public boolean isShowingIcon() {
            return mShowingIcon;
        }

        public boolean isPasswordVisible() {
            return mPasswordVisible;
        }

        @Override
        public void writeToParcel(Parcel destination, int flags) {
            super.writeToParcel(destination, flags);
            destination.writeByte((byte) (mShowingIcon ? 1 : 0));
            destination.writeByte((byte) (mPasswordVisible ? 1 : 0));
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {

            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }

        };
    }
}
