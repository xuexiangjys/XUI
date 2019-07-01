package com.xuexiang.xui.widget.edittext;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.appcompat.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.xuexiang.xui.R;
import com.xuexiang.xui.utils.ResUtils;

/**
 * 支持显示密码的输入框
 *
 * @author xuexiang
 * @since 2019/1/14 下午10:08
 */
public class PasswordEditText extends AppCompatEditText {

    private final static int ALPHA_ICON_ENABLED = (int) (255 * 0.54f);

    private final static int ALPHA_ICON_DISABLED = (int) (255 * 0.38f);

    private Drawable showPwDrawable;

    private Drawable hidePwDrawable;

    private boolean passwordVisible;

    private boolean isRTL;

    private boolean showingIcon;

    private boolean setErrorCalled;

    private boolean hoverShowsPw;

    private boolean useNonMonospaceFont;

    private boolean enableIconAlpha;

    private boolean handlingHoverEvent;

    public PasswordEditText(Context context) {
        this(context, null);
    }

    public PasswordEditText(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.PasswordEditTextStyle);
    }

    public PasswordEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs, defStyleAttr);
    }

    public void initAttrs(AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.PasswordEditText, defStyleAttr, 0);
        try {
            showPwDrawable = typedArray.getDrawable(R.styleable.PasswordEditText_pet_iconShow);
            if (showPwDrawable == null) {
                showPwDrawable = ResUtils.getDrawable(getContext(), R.drawable.pet_icon_visibility_24dp);
            }
            hidePwDrawable = typedArray.getDrawable(R.styleable.PasswordEditText_pet_iconHide);
            if (hidePwDrawable == null) {
                hidePwDrawable = ResUtils.getDrawable(getContext(), R.drawable.pet_icon_visibility_off_24dp);
            }
            hoverShowsPw = typedArray.getBoolean(R.styleable.PasswordEditText_pet_hoverShowsPw, false);
            useNonMonospaceFont = typedArray.getBoolean(R.styleable.PasswordEditText_pet_nonMonospaceFont, false);
            enableIconAlpha = typedArray.getBoolean(R.styleable.PasswordEditText_pet_enableIconAlpha, true);
        } finally {
            typedArray.recycle();
        }

        if (enableIconAlpha) {
            hidePwDrawable.setAlpha(ALPHA_ICON_ENABLED);
            showPwDrawable.setAlpha(ALPHA_ICON_DISABLED);
        }

        if (useNonMonospaceFont) {
            setTypeface(Typeface.DEFAULT);
        }

        isRTL = isRTLLanguage();

        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //NOOP
            }

            @Override
            public void onTextChanged(CharSequence seq, int start, int before, int count) {
                //NOOP
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    if (setErrorCalled) {
                        // resets drawables after setError was called as this leads to icons
                        // not changing anymore afterwards
                        setCompoundDrawables(null, null, null, null);
                        setErrorCalled = false;
                        showPasswordVisibilityIndicator(true);
                    }
                    if (!showingIcon) {
                        showPasswordVisibilityIndicator(true);
                    }
                } else {
                    // hides the indicator if no text inside text field
                    passwordVisible = false;
                    handlePasswordInputVisibility();
                    showPasswordVisibilityIndicator(false);
                }

            }
        });
    }

    private boolean isRTLLanguage() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return false;
        }
        Configuration config = getResources().getConfiguration();
        return config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        return new SavedState(superState, showingIcon, passwordVisible);
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        showingIcon = savedState.isShowingIcon();
        passwordVisible = savedState.isPasswordVisible();
        handlePasswordInputVisibility();
        showPasswordVisibilityIndicator(showingIcon);
    }

    @Override
    public void setError(CharSequence error) {
        super.setError(error);
        setErrorCalled = true;

    }

    @Override
    public void setError(CharSequence error, Drawable icon) {
        super.setError(error, icon);
        setErrorCalled = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!showingIcon) {
            return super.onTouchEvent(event);
        } else {
            boolean touchable;
            if (isRTL) {
                touchable = event.getX() > getPaddingLeft() && event.getX() < (getPaddingLeft() + showPwDrawable.getIntrinsicWidth());
            } else {
                touchable = event.getX() > (getWidth() - getPaddingRight() - showPwDrawable.getIntrinsicWidth()) && event.getX() < ((getWidth() - getPaddingRight()));
            }
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (hoverShowsPw) {
                        if (touchable) {
                            togglePasswordIconVisibility();
                            // prevent keyboard from coming up
                            event.setAction(MotionEvent.ACTION_CANCEL);
                            handlingHoverEvent = true;
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (handlingHoverEvent || touchable) {
                        togglePasswordIconVisibility();
                        // prevent keyboard from coming up
                        event.setAction(MotionEvent.ACTION_CANCEL);
                        handlingHoverEvent = false;
                    }
                    break;
            }
            return super.onTouchEvent(event);
        }
    }


    private void showPasswordVisibilityIndicator(boolean shouldShowIcon) {
        if (shouldShowIcon) {
            Drawable drawable = passwordVisible ? showPwDrawable : hidePwDrawable;
            showingIcon = true;
            setCompoundDrawablesWithIntrinsicBounds(isRTL ? drawable : null, null, isRTL ? null : drawable, null);
        } else {
            // reset drawable
            setCompoundDrawables(null, null, null, null);
            showingIcon = false;
        }
    }

    /**
     * This method toggles the visibility of the icon and takes care of switching the input type
     * of the view to be able to see the password afterwards.
     * <p>
     * This method may only be called if there is an icon visible
     */
    private void togglePasswordIconVisibility() {
        passwordVisible = !passwordVisible;
        handlePasswordInputVisibility();
        showPasswordVisibilityIndicator(true);
    }

    /**
     * This method is called when restoring the state (e.g. on orientation change)
     */
    private void handlePasswordInputVisibility() {
        int selectionStart = getSelectionStart();
        int selectionEnd = getSelectionEnd();
        if (passwordVisible) {
            setTransformationMethod(null);
        } else {
            setTransformationMethod(PasswordTransformationMethod.getInstance());

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
