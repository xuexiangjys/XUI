package com.xuexiang.xui.widget.edittext.verify;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xuexiang.xui.R;
import com.xuexiang.xui.utils.ResUtils;

/**
 * 验证码输入框
 *
 * @author XUE
 * @since 2019/5/7 11:22
 */
public class VerifyCodeEditText extends RelativeLayout {

    private LinearLayout mLlContainer;
    private PwdEditText mEditText;

    // 输入框数量
    private int mEtNumber;
    // 输入框的宽度
    private int mEtWidth;
    //输入框分割线
    private Drawable mEtDivider;
    //输入框文字颜色
    private int mEtTextColor;
    //输入框文字大小
    private float mEtTextSize;
    //输入框获取焦点时背景
    private Drawable mBackgroundFocus;
    //输入框没有焦点时背景
    private Drawable mBackgroundNormal;
    //是否是密码模式
    private boolean mIsPwd;
    //密码模式时圆的半径
    private float mPwdRadius;
    //存储TextView的数据 数量由自定义控件的属性传入
    private PwdTextView[] mPwdTextViews;

    private InputNumberTextWatcher mTextWatcher = new InputNumberTextWatcher();

    public VerifyCodeEditText(Context context) {
        this(context, null);
    }

    public VerifyCodeEditText(Context context, AttributeSet attrs) {
        this(context, attrs,  R.attr.VerifyCodeEditTextStyle);
    }

    public VerifyCodeEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    //初始化 布局和属性
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.xui_layout_verify_code, this);
        mLlContainer = findViewById(R.id.ll_container);
        mEditText = findViewById(R.id.et_input);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VerifyCodeEditText, defStyleAttr, 0);
        mEtNumber = typedArray.getInteger(R.styleable.VerifyCodeEditText_vcet_number, 4);
        mEtWidth = typedArray.getDimensionPixelSize(R.styleable.VerifyCodeEditText_vcet_width,  ResUtils.getDimensionPixelSize(R.dimen.default_vcet_width));
        mEtDivider = typedArray.getDrawable(R.styleable.VerifyCodeEditText_vcet_divider);
        mEtTextSize = typedArray.getDimensionPixelSize(R.styleable.VerifyCodeEditText_vcet_text_size, ResUtils.getDimensionPixelSize(R.dimen.default_vcet_text_size));
        mEtTextColor = typedArray.getColor(R.styleable.VerifyCodeEditText_vcet_text_color, Color.BLACK);
        mBackgroundFocus = typedArray.getDrawable(R.styleable.VerifyCodeEditText_vcet_bg_focus);
        mBackgroundNormal = typedArray.getDrawable(R.styleable.VerifyCodeEditText_vcet_bg_normal);
        mIsPwd = typedArray.getBoolean(R.styleable.VerifyCodeEditText_vcet_is_pwd, false);
        mPwdRadius = typedArray.getDimensionPixelSize(R.styleable.VerifyCodeEditText_vcet_pwd_radius, ResUtils.getDimensionPixelSize(R.dimen.default_vcet_pwd_radius));
        //释放资源
        typedArray.recycle();

        // 当xml中未配置时 这里进行初始配置默认图片
        if (mEtDivider == null) {
            mEtDivider = context.getResources().getDrawable(R.drawable.vcet_shape_divider);
        }

        if (mBackgroundFocus == null) {
            mBackgroundFocus = context.getResources().getDrawable(R.drawable.vcet_shape_bg_focus);
        }

        if (mBackgroundNormal == null) {
            mBackgroundNormal = context.getResources().getDrawable(R.drawable.vcet_shape_bg_normal);
        }

        initUI();
    }

    // 初始UI
    private void initUI() {
        initTextViews(getContext(), mEtNumber, mEtWidth, mEtDivider, mEtTextSize, mEtTextColor);
        initEtContainer(mPwdTextViews);
        setListener();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 设置当 高为 warpContent 模式时的默认值 为 50dp
        int heightMeasureSpecValue = heightMeasureSpec;

        int heightMode = MeasureSpec.getMode(heightMeasureSpecValue);
        if (heightMode == MeasureSpec.AT_MOST) {
            heightMeasureSpecValue = MeasureSpec.makeMeasureSpec((int) dp2px(50, getContext()), MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpecValue);
    }


    //初始化TextView
    private void initTextViews(Context context, int etNumber, int etWidth, Drawable etDividerDrawable, float etTextSize, int etTextColor) {
        // 设置 editText 的输入长度
        mEditText.setCursorVisible(false);//将光标隐藏
        mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(etNumber)}); //最大输入长度
        // 设置分割线的宽度
        if (etDividerDrawable != null) {
            etDividerDrawable.setBounds(0, 0, etDividerDrawable.getMinimumWidth(), etDividerDrawable.getMinimumHeight());
            mLlContainer.setDividerDrawable(etDividerDrawable);
        }
        mPwdTextViews = new PwdTextView[etNumber];

        for (int i = 0; i < mPwdTextViews.length; i++) {
            PwdTextView textView = new PwdTextView(context);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, etTextSize);
            textView.setTextColor(etTextColor);
            textView.setWidth(etWidth);
            textView.setHeight(etWidth);
            if (i == 0) {
                textView.setBackgroundDrawable(mBackgroundFocus);
            } else {
                textView.setBackgroundDrawable(mBackgroundNormal);
            }
            textView.setGravity(Gravity.CENTER);
            textView.setFocusable(false);
            mPwdTextViews[i] = textView;
        }
    }

    //初始化存储TextView 的容器
    private void initEtContainer(TextView[] mTextViews) {
        for (TextView mTextView : mTextViews) {
            mLlContainer.addView(mTextView);
        }
    }

    private void setListener() {
        // 监听输入内容
        mEditText.addTextChangedListener(mTextWatcher);
        // 监听删除按键
        mEditText.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    onKeyDelete();
                    return true;
                }
                return false;
            }
        });
    }

    // 给TextView 设置文字
    private void setText(String inputContent) {
        if (TextUtils.isEmpty(inputContent)) {
            return;
        }
        for (int i = 0; i < mPwdTextViews.length; i++) {
            PwdTextView tv = mPwdTextViews[i];
            if (tv.getText().toString().trim().equals("")) {
                if (mIsPwd) {
                    tv.drawPassword(mPwdRadius);
                }
                tv.setText(inputContent);
                tv.setBackgroundDrawable(mBackgroundNormal);
                if (i < mEtNumber - 1) {
                    mPwdTextViews[i + 1].setBackgroundDrawable(mBackgroundFocus);
                    if (mOnInputListener != null) {
                        mOnInputListener.onChange(getInputValue());
                    }
                } else if (i == mEtNumber - 1) {
                    if (mOnInputListener != null) {
                        mOnInputListener.onComplete(getInputValue());
                    }
                }
                break;
            }
        }
    }

    // 监听删除
    private void onKeyDelete() {
        for (int i = mPwdTextViews.length - 1; i >= 0; i--) {
            PwdTextView tv = mPwdTextViews[i];
            if (!tv.getText().toString().trim().equals("")) {
                if (mIsPwd) {
                    tv.clearPassword();
                }
                tv.setText("");
                tv.setBackgroundDrawable(mBackgroundFocus);
                if (i < mEtNumber - 1) {
                    mPwdTextViews[i + 1].setBackgroundDrawable(mBackgroundNormal);
                    if (i == 0) {
                        if (mOnInputListener != null) {
                            mOnInputListener.onClear();
                        }
                    } else {
                        if (mOnInputListener != null) {
                            mOnInputListener.onChange(getInputValue());
                        }
                    }
                } else if (i == mEtNumber - 1) {
                    if (mOnInputListener != null) {
                        mOnInputListener.onChange(getInputValue());
                    }
                }
                break;
            }
        }
    }


    /**
     * 获取输入文本
     *
     * @return string
     */
    public String getInputValue() {
        StringBuilder sb = new StringBuilder();
        for (TextView tv : mPwdTextViews) {
            sb.append(tv.getText().toString().trim());
        }
        return sb.toString();
    }

    /**
     * 删除输入内容
     */
    public void clearInputValue() {
        for (int i = 0; i < mPwdTextViews.length; i++) {
            if (i == 0) {
                mPwdTextViews[i].setBackgroundDrawable(mBackgroundFocus);
            } else {
                mPwdTextViews[i].setBackgroundDrawable(mBackgroundNormal);
            }
            if (mIsPwd) {
                mPwdTextViews[i].clearPassword();
            }
            mPwdTextViews[i].setText("");
        }
        if (mOnInputListener != null) {
            mOnInputListener.onClear();
        }
    }

    /**
     * 设置输入框个数
     *
     * @param etNumber
     */
    public void setEtNumber(int etNumber) {
        mEtNumber = etNumber;
        mEditText.removeTextChangedListener(mTextWatcher);
        mLlContainer.removeAllViews();
        initUI();
    }

    /**
     * 获取输入的位数
     *
     * @return int
     */
    public int getEtNumber() {
        return mEtNumber;
    }


    /**
     * 设置是否是密码模式 默认不是
     *
     * @param isPwdMode
     */
    public void setPwdMode(boolean isPwdMode) {
        this.mIsPwd = isPwdMode;
    }


    /**
     * 获取输入的EditText 用于外界设置键盘弹出
     *
     * @return EditText
     */
    public EditText getEditText() {
        return mEditText;
    }

    // 输入完成 和 删除成功 的监听
    private OnInputListener mOnInputListener;

    public void setOnInputListener(OnInputListener onInputListener) {
        mOnInputListener = onInputListener;
    }


    /**
     * 输入框监听
     */
    public interface OnInputListener {
        /**
         * 输入完成
         */
        void onComplete(String input);
        /**
         * 输入变化
         * @param input
         */
        void onChange(String input);
        /**
         * 输入清空
         */
        void onClear();
    }


    public float dp2px(float dpValue, Context context) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpValue, context.getResources().getDisplayMetrics());
    }

    public float sp2px(float spValue, Context context) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spValue, context.getResources().getDisplayMetrics());
    }


    /**
     * 监听输入框输入的数量
     */
    private class InputNumberTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String inputStr = editable.toString();
            if (!TextUtils.isEmpty(inputStr)) {
                String[] strArray = inputStr.split("");
                for (int i = 0; i < strArray.length; i++) {
                    // 不能大于输入框个数
                    if (i > mEtNumber) {
                        break;
                    }
                    setText(strArray[i]);
                    mEditText.setText("");
                }
            }
        }
    }


}
