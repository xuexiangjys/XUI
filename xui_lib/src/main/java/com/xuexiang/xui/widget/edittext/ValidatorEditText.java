package com.xuexiang.xui.widget.edittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.xuexiang.xui.R;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.widget.edittext.materialedittext.validation.METValidator;
import com.xuexiang.xui.widget.edittext.materialedittext.validation.RegexpValidator;
import com.xuexiang.xui.widget.popupwindow.ViewTooltip;

import java.util.ArrayList;
import java.util.List;

/**
 * 可自动验证的EditText
 *
 * @author xuexiang
 * @date 2017/12/10 下午10:59
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
    private int mIconSize;

    /**
     * 出错提示
     */
    private CharSequence mErrorMsg;

    private int mPosition;

    /**
     * 是否显示出错提示
     */
    private boolean mIsShowErrorIcon = true;

    public ValidatorEditText(Context context) {
        super(context);
        initAttrs(context, null);
        initView();
    }

    public ValidatorEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        initView();
    }

    public ValidatorEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initView();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray tArray = context.obtainStyledAttributes(attrs, R.styleable.ValidatorEditText);
            try {
                String regexp = tArray.getString(R.styleable.ValidatorEditText_vet_regexp);
                if (!TextUtils.isEmpty(regexp)) {
                    mValidators = new ArrayList<>();
                    String errorMessage = tArray.getString(R.styleable.ValidatorEditText_vet_errorMessage);
                    if (!TextUtils.isEmpty(errorMessage)) {
                        mValidators.add(new RegexpValidator(errorMessage, regexp));
                    } else {
                        mValidators.add(new RegexpValidator(ResUtils.getString(R.string.xui_tip_input_error), regexp));
                    }
                }
                mIsAutoValidate = tArray.getBoolean(R.styleable.ValidatorEditText_vet_autoValidate, true);
                mIsShowErrorIcon = tArray.getBoolean(R.styleable.ValidatorEditText_vet_show_errorIcon, true);
                mErrorDrawable = tArray.getDrawable(R.styleable.ValidatorEditText_vet_errorIcon);
                if (mErrorDrawable == null) {
                    //获取EditText的DrawableRight,假如没有设置我们就使用默认的图片
                    mErrorDrawable = getCompoundDrawables()[2];
                    if (mErrorDrawable == null) {
                        mErrorDrawable = ResUtils.getDrawable(R.drawable.xui_ic_default_tip_btn);
                    }
                }
                mIconSize = tArray.getDimensionPixelSize(R.styleable.ValidatorEditText_vet_errorIconSize, 0);
                if (mIconSize != 0) {
                    mErrorDrawable.setBounds(0, 0, mIconSize, mIconSize);
                } else {
                    mErrorDrawable.setBounds(0, 0, mErrorDrawable.getIntrinsicWidth(), mErrorDrawable.getIntrinsicHeight());
                }
                mPosition = tArray.getInt(R.styleable.ValidatorEditText_vet_tipPosition, 2);

            } finally {
                tArray.recycle();
            }
        }
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
        if (getCompoundDrawables()[2] != null) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                boolean touchable = event.getX() > (getWidth()
                        - getPaddingRight() - mErrorDrawable.getIntrinsicWidth())
                        && (event.getX() < ((getWidth() - getPaddingRight())));
                if (touchable) {
                    showErrorMsg();
                }
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 显示出错提示
     */
    private void showErrorMsg() {
        if (!mIsValid) {
            ViewTooltip
                    .on(this)
                    .color(ResUtils.getColor(R.color.xui_config_color_edittext_error_text))
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
        boolean isEmpty = text.length() == 0;

        boolean isValid = true;
        for (METValidator validator : mValidators) {
            isValid = isValid && validator.isValid(text, isEmpty);
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
//        super.setError(error);
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
            mOnValidateListener.onValidateError(getText().toString(), errorMessage);
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
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }

    public static ViewTooltip.Position parsePosition(int value) {
        switch (value) {
            case 0: return ViewTooltip.Position.LEFT;
            case 1: return ViewTooltip.Position.RIGHT;
            case 2: return ViewTooltip.Position.TOP;
            case 3: return ViewTooltip.Position.BOTTOM;
            default: return ViewTooltip.Position.TOP;
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setFocusable(enabled);
        super.setFocusableInTouchMode(enabled);
        super.setEnabled(enabled);
    }

    /**
     * 校验监听
     */
    public interface OnValidateListener {

        /**
         * 校验出错
         *
         * @param inputString
         * @param errorMessage
         */
        void onValidateError(String inputString, String errorMessage);

    }
}
