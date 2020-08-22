package com.xuexiang.xui.widget.edittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;

import com.xuexiang.xui.R;
import com.xuexiang.xui.utils.DensityUtils;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.ThemeUtils;
import com.xuexiang.xui.widget.edittext.materialedittext.validation.METValidator;
import com.xuexiang.xui.widget.edittext.materialedittext.validation.RegexpValidator;
import com.xuexiang.xui.widget.popupwindow.ViewTooltip;

import java.util.ArrayList;
import java.util.List;

/**
 * 可自动验证的EditText
 *
 * @author xuexiang
 * @since 2019/1/14 下午10:13
 */
public class ValidatorEditText extends AppCompatEditText implements View.OnFocusChangeListener {

    private List<METValidator> mValidators;

    /**
     * 是否自动验证
     */
    private boolean mIsAutoValidate;

    /**
     * 校验监听
     */
    private OnValidateListener mOnValidateListener;

    /**
     * 验证是否有效
     */
    private boolean mIsValid = true;

    private Drawable mErrorDrawable;

    /**
     * 出错提示
     */
    private CharSequence mErrorMsg;

    private int mPosition;

    /**
     * 是否显示出错提示
     */
    private boolean mIsShowErrorIcon = true;

    private boolean mIsRTL;

    /**
     * 增大点击区域
     */
    private int mExtraClickArea;

    public ValidatorEditText(Context context) {
        this(context, null);
    }

    public ValidatorEditText(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.ValidatorEditTextStyle);
    }

    public ValidatorEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs, defStyleAttr);
        initView();
    }

    private void initAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        mExtraClickArea = DensityUtils.dp2px(20);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ValidatorEditText, defStyleAttr, 0);
        String regexp = typedArray.getString(R.styleable.ValidatorEditText_vet_regexp);
        if (!TextUtils.isEmpty(regexp)) {
            mValidators = new ArrayList<>();
            String errorMessage = typedArray.getString(R.styleable.ValidatorEditText_vet_errorMessage);
            if (!TextUtils.isEmpty(errorMessage)) {
                mValidators.add(new RegexpValidator(errorMessage, regexp));
            } else {
                mValidators.add(new RegexpValidator(ResUtils.getString(R.string.xui_met_input_error), regexp));
            }
        }
        mIsAutoValidate = typedArray.getBoolean(R.styleable.ValidatorEditText_vet_autoValidate, true);
        mIsShowErrorIcon = typedArray.getBoolean(R.styleable.ValidatorEditText_vet_showErrorIcon, true);
        mErrorDrawable = ResUtils.getDrawableAttrRes(getContext(), typedArray, R.styleable.ValidatorEditText_vet_errorIcon);
        if (mErrorDrawable == null) {
            //获取EditText的DrawableRight,假如没有设置我们就使用默认的图片
            mErrorDrawable = getCompoundDrawables()[2];
            if (mErrorDrawable == null) {
                mErrorDrawable = ResUtils.getDrawable(getContext(), R.drawable.xui_ic_default_tip_btn);
            }
        }
        int iconSize = typedArray.getDimensionPixelSize(R.styleable.ValidatorEditText_vet_errorIconSize, 0);
        if (iconSize != 0) {
            mErrorDrawable.setBounds(0, 0, iconSize, iconSize);
        } else {
            mErrorDrawable.setBounds(0, 0, mErrorDrawable.getIntrinsicWidth(), mErrorDrawable.getIntrinsicHeight());
        }
        mPosition = typedArray.getInt(R.styleable.ValidatorEditText_vet_tipPosition, 2);
        typedArray.recycle();

        mIsRTL = isRtl();
    }

    private void initView() {
        setErrorIconVisible(false);
        super.setOnFocusChangeListener(this);
        initTextWatcher();
        if (mIsAutoValidate) {
            updateValid();
        }
    }

    private void initTextWatcher() {
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mIsAutoValidate) {
                    updateValid();
                } else {
                    setError(null);
                }
                postInvalidate();
            }
        });
    }

    /**
     * 因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件
     * 当我们按下的位置 在  EditText的宽度 - 图标到控件右边的间距 - 图标的宽度  和
     * EditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向没有考虑
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getCompoundDrawables()[mIsRTL ? 0 : 2] != null) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (isTouchable(event)) {
                    showErrorMsg();
                }
            }
        }
        return super.onTouchEvent(event);
    }

    public ValidatorEditText setExtraClickAreaSize(int extraClickArea) {
        mExtraClickArea = extraClickArea;
        return this;
    }

    private boolean isTouchable(MotionEvent event) {
        if (mIsRTL) {
            return event.getX() > getPaddingStart() - mExtraClickArea && event.getX() < getPaddingStart() + mErrorDrawable.getIntrinsicWidth() + mExtraClickArea;
        } else {
            return event.getX() > getWidth() - getPaddingEnd() - mErrorDrawable.getIntrinsicWidth() - mExtraClickArea && event.getX() < getWidth() - getPaddingEnd() + mExtraClickArea;
        }
    }

    /**
     * 显示出错提示
     */
    private void showErrorMsg() {
        if (!mIsValid) {
            ViewTooltip
                    .on(this)
                    .color(ThemeUtils.resolveColor(getContext(), R.attr.xui_config_color_error_text))
                    .position(parsePosition(mPosition))
                    .text(mErrorMsg.toString())
                    .show();
        }
    }

    /**
     * 增加校验规则
     *
     * @param validator
     * @return
     */
    public ValidatorEditText addValidator(METValidator validator) {
        if (validator != null) {
            if (mValidators == null) {
                mValidators = new ArrayList<>();
            }
            mValidators.add(validator);
        }
        return this;
    }

    /**
     * 清除校验规则
     */
    public void clearValidators() {
        if (mValidators != null) {
            mValidators.clear();
        }
    }

    /**
     * 增加校验监听
     *
     * @param onValidateListener
     * @return
     */
    public ValidatorEditText setOnValidateListener(OnValidateListener onValidateListener) {
        mOnValidateListener = onValidateListener;
        return this;
    }

    /**
     * 校验输入的合法性
     *
     * @return
     */
    public boolean validate() {
        if (mValidators == null || mValidators.isEmpty()) {
            return true;
        }

        CharSequence text = getText();
        boolean isEmpty = TextUtils.isEmpty(text);

        boolean isValid = true;
        for (METValidator validator : mValidators) {
            isValid = validator.isValid(text, isEmpty);
            if (!isValid) {
                setError(validator.getErrorMessage());
                break;
            }
        }

        if (isValid) {
            setError(null);
        }

        postInvalidate();
        return isValid;
    }

    /**
     * 更新有效性
     */
    public void updateValid() {
        mIsValid = validate();
    }

    /**
     * 输入的内容是否有效
     *
     * @return
     */
    public boolean isInputValid() {
        if (mIsAutoValidate) {
            return mIsValid;
        } else {
            return validate();
        }
    }

    @Override
    public void setError(CharSequence error) {
        mErrorMsg = error;
        if (TextUtils.isEmpty(error)) {
            setErrorIconVisible(false);
            setBackground(ResUtils.getDrawable(getContext(), R.drawable.xui_config_bg_edittext));
        } else {
            onValidateError(error.toString());
            setBackground(ResUtils.getDrawable(getContext(), R.drawable.xui_config_color_edittext_error));
        }
    }

    public CharSequence getErrorMsg() {
        return mErrorMsg;
    }

    /**
     * 获取输入的内容
     *
     * @return
     */
    public String getInputValue() {
        return getEditableText().toString().trim();
    }

    /**
     * 出现校验出错的情况
     *
     * @param errorMessage
     */
    private void onValidateError(String errorMessage) {
        setErrorIconVisible(true);
        if (mOnValidateListener != null) {
            mOnValidateListener.onValidateError(getText() != null ? getText().toString() : "", errorMessage);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (mIsAutoValidate && !hasFocus) {
            updateValid();
        }
    }

    /**
     * 设置出错提示图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
     *
     * @param visible
     */
    private void setErrorIconVisible(boolean visible) {
        Drawable right = visible && mIsShowErrorIcon ? mErrorDrawable : null;
        setCompoundDrawables(mIsRTL ? right : getCompoundDrawables()[0],
                getCompoundDrawables()[1], mIsRTL ? getCompoundDrawables()[2] : right, getCompoundDrawables()[3]);
    }

    public static ViewTooltip.Position parsePosition(int value) {
        switch (value) {
            case 0:
                return ViewTooltip.Position.LEFT;
            case 1:
                return ViewTooltip.Position.RIGHT;
            case 2:
                return ViewTooltip.Position.TOP;
            case 3:
                return ViewTooltip.Position.BOTTOM;
            default:
                return ViewTooltip.Position.TOP;
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setFocusable(enabled);
        super.setFocusableInTouchMode(enabled);
        super.setEnabled(enabled);
    }

    /**
     * 输入的内容是否为空
     *
     * @return
     */
    public boolean isEmpty() {
        return TextUtils.isEmpty(getInputValue());
    }

    /**
     * 输入的内容是否不为空
     *
     * @return
     */
    public boolean isNotEmpty() {
        return !TextUtils.isEmpty(getInputValue());
    }

    /**
     * 校验监听
     */
    public interface OnValidateListener {

        /**
         * 校验出错
         *
         * @param inputString  输入内容
         * @param errorMessage 错误信息
         */
        void onValidateError(String inputString, String errorMessage);

    }

    private boolean isRtl() {
        return getLayoutDirection() == LAYOUT_DIRECTION_RTL;
    }
}
