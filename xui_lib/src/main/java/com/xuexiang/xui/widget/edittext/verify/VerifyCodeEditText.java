package com.xuexiang.xui.widget.edittext.verify;

import android.content.Context;
import android.content.res.Configuration;
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
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xuexiang.xui.R;
import com.xuexiang.xui.utils.DensityUtils;
import com.xuexiang.xui.utils.ResUtils;

/**
 * 验证码输入框
 *
 * @author XUE
 * @since 2019/5/7 11:22
 */
public class VerifyCodeEditText extends FrameLayout {

    private static final int DEFAULT_HEIGHT = 50;

    private static final int DEFAULT_EDIT_TEXT_SIZE = 4;

    private LinearLayout mLlContainer;
    private PwdEditText mEditText;
    /**
     * 输入框数量
     */
    private int mEtNumber;
    /**
     * 输入框的宽度
     */
    private int mEtWidth;
    /**
     * 是否等分输入框
     */
    private boolean mIsDivideEqually;
    /**
     * 输入框分割线
     */
    private Drawable mEtDivider;
    /**
     * 输入框文字颜色
     */
    private int mEtTextColor;
    /**
     * 输入框文字大小
     */
    private float mEtTextSize;
    /**
     * 输入框获取焦点时背景
     */
    private Drawable mBackgroundFocus;
    /**
     * 输入框没有焦点时背景
     */
    private Drawable mBackgroundNormal;
    /**
     * 是否是密码模式
     */
    private boolean mIsPwd;
    /**
     * 密码模式时圆的半径
     */
    private float mPwdRadius;
    /**
     * 存储TextView的数据 数量由自定义控件的属性传入
     */
    private PwdTextView[] mPwdTextViews;

    private InputNumberTextWatcher mTextWatcher = new InputNumberTextWatcher();

    public VerifyCodeEditText(Context context) {
        this(context, null);
    }

    public VerifyCodeEditText(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.VerifyCodeEditTextStyle);
    }

    public VerifyCodeEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    /**
     * 初始化 布局和属性
     *
     * @param context      上下文
     * @param attrs        属性
     * @param defStyleAttr 默认属性
     */
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.xui_layout_verify_code, this);
        mLlContainer = findViewById(R.id.ll_container);
        mEditText = findViewById(R.id.et_input);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VerifyCodeEditText, defStyleAttr, 0);
        mEtNumber = typedArray.getInteger(R.styleable.VerifyCodeEditText_vcet_number, DEFAULT_EDIT_TEXT_SIZE);
        mEtWidth = typedArray.getDimensionPixelSize(R.styleable.VerifyCodeEditText_vcet_width, ResUtils.getDimensionPixelSize(context, R.dimen.default_vcet_width));
        mIsDivideEqually = typedArray.getBoolean(R.styleable.VerifyCodeEditText_vcet_is_divide_equally, false);
        mEtDivider = ResUtils.getDrawableAttrRes(context, typedArray, R.styleable.VerifyCodeEditText_vcet_divider);
        mEtTextSize = typedArray.getDimensionPixelSize(R.styleable.VerifyCodeEditText_vcet_text_size, ResUtils.getDimensionPixelSize(context, R.dimen.default_vcet_text_size));
        mEtTextColor = typedArray.getColor(R.styleable.VerifyCodeEditText_vcet_text_color, Color.BLACK);
        mBackgroundFocus = ResUtils.getDrawableAttrRes(context, typedArray, R.styleable.VerifyCodeEditText_vcet_bg_focus);
        mBackgroundNormal = ResUtils.getDrawableAttrRes(context, typedArray, R.styleable.VerifyCodeEditText_vcet_bg_normal);
        mIsPwd = typedArray.getBoolean(R.styleable.VerifyCodeEditText_vcet_is_pwd, false);
        mPwdRadius = typedArray.getDimensionPixelSize(R.styleable.VerifyCodeEditText_vcet_pwd_radius, ResUtils.getDimensionPixelSize(context, R.dimen.default_vcet_pwd_radius));
        //释放资源
        typedArray.recycle();

        // 当xml中未配置时 这里进行初始配置默认图片
        if (mEtDivider == null) {
            mEtDivider = ResUtils.getDrawable(context, R.drawable.vcet_shape_divider);
        }
        if (mBackgroundFocus == null) {
            mBackgroundFocus = ResUtils.getDrawable(context, R.drawable.vcet_shape_bg_focus);
        }
        if (mBackgroundNormal == null) {
            mBackgroundNormal = ResUtils.getDrawable(context, R.drawable.vcet_shape_bg_normal);
        }
        // 平分每个输入框的宽度
        if (mIsDivideEqually) {
            post(new Runnable() {
                @Override
                public void run() {
                    refreshEditSizeWhenDivideEqually();
                    initView(getContext());
                }
            });
        } else {
            initView(context);
        }
    }

    private void refreshEditSizeWhenDivideEqually() {
        MarginLayoutParams params = (MarginLayoutParams) getLayoutParams();
        int dividerWidth = mEtDivider != null ? mEtDivider.getMinimumWidth() : 0;
        mEtWidth = (DensityUtils.getDisplayWidth(getContext(), true) - dividerWidth * (mEtNumber - 1) - getPaddingLeft() - getPaddingRight() - params.leftMargin - params.rightMargin) / mEtNumber;
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mIsDivideEqually) {
            String input = getInputValue();
            mEditText.removeTextChangedListener(mTextWatcher);
            mLlContainer.removeAllViews();
            refreshEditSizeWhenDivideEqually();
            restoreView(getContext(), input);
        }
    }

    private void restoreView(Context context, String input) {
        initTextViews(context, mEtNumber, mEtWidth, mEtDivider, mEtTextSize, mEtTextColor);
        initEtContainer(mPwdTextViews);
        if (!TextUtils.isEmpty(input)) {
            String[] strArray = input.split("");
            for (int i = 0; i < strArray.length; i++) {
                // 不能大于输入框个数
                if (i > mEtNumber) {
                    break;
                }
                setText(strArray[i], true);
            }
        }
        setListener();
    }

    /**
     * 初始化控件
     *
     * @param context 上下文
     */
    private void initView(Context context) {
        initTextViews(context, mEtNumber, mEtWidth, mEtDivider, mEtTextSize, mEtTextColor);
        initEtContainer(mPwdTextViews);
        setListener();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMeasureSpecValue = heightMeasureSpec;
        int heightMode = MeasureSpec.getMode(heightMeasureSpecValue);
        if (heightMode == MeasureSpec.AT_MOST) {
            // 设置当高为 warpContent 模式时的最小高度是50dp
            int minHeight = (int) dp2px(DEFAULT_HEIGHT, getContext());
            if (mEtWidth < minHeight) {
                heightMeasureSpecValue = MeasureSpec.makeMeasureSpec(minHeight, MeasureSpec.EXACTLY);
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpecValue);
    }


    /**
     * 初始化TextView
     *
     * @param context           上下文
     * @param etNumber          输入长度
     * @param etWidth           输入框宽度
     * @param etDividerDrawable 分割线
     * @param etTextSize        输入文字大小
     * @param etTextColor       输入文字颜色
     */
    private void initTextViews(Context context, int etNumber, int etWidth, Drawable etDividerDrawable, float etTextSize, int etTextColor) {
        // 设置 editText 的输入长度
        // 将光标隐藏
        mEditText.setCursorVisible(false);
        //最大输入长度
        mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(etNumber)});
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
            if (!mIsDivideEqually) {
                textView.setWidth(etWidth);
            }
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

    /**
     * 初始化存储TextView 的容器
     *
     * @param textViews 输入文字
     */
    private void initEtContainer(TextView[] textViews) {
        if (mIsDivideEqually) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mLlContainer.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                layoutParams.gravity = Gravity.CENTER;
            } else {
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            }
            mLlContainer.setLayoutParams(layoutParams);
            LinearLayout.LayoutParams childParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
            childParams.weight = 1;
            for (TextView textView : textViews) {
                mLlContainer.addView(textView, childParams);
            }
        } else {
            for (TextView textView : textViews) {
                mLlContainer.addView(textView);
            }
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
        mEditText.setBackSpaceListener(new TInputConnection.BackspaceListener() {
            @Override
            public boolean onBackspace() {
                onKeyDelete();
                return true;
            }
        });
    }

    /**
     * 设置输入
     *
     * @param inputContent 输入内容
     * @param isSilent     是否静默输入
     */
    private void setText(String inputContent, boolean isSilent) {
        if (TextUtils.isEmpty(inputContent)) {
            return;
        }
        for (int i = 0; i < mPwdTextViews.length; i++) {
            PwdTextView tv = mPwdTextViews[i];
            if ("".equals(tv.getText().toString().trim())) {
                if (mIsPwd) {
                    tv.drawPassword(mPwdRadius);
                }
                tv.setText(inputContent);
                tv.setBackgroundDrawable(mBackgroundNormal);
                if (i < mEtNumber - 1) {
                    mPwdTextViews[i + 1].setBackgroundDrawable(mBackgroundFocus);
                    if (mOnInputListener != null && !isSilent) {
                        mOnInputListener.onChange(getInputValue());
                    }
                } else if (i == mEtNumber - 1) {
                    if (mOnInputListener != null && !isSilent) {
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
            if (!"".equals(tv.getText().toString().trim())) {
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
     * @param etNumber 输入框个数
     */
    public void setEtNumber(int etNumber) {
        mEtNumber = etNumber;
        mEditText.removeTextChangedListener(mTextWatcher);
        mLlContainer.removeAllViews();
        if (mIsDivideEqually) {
            refreshEditSizeWhenDivideEqually();
        }
        initView(getContext());
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
     * 设置是否是密码模式 默认false
     *
     * @param isPwdMode 是否是密码模式
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

    /**
     * 输入完成 和 删除成功 的监听
     */
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
         *
         * @param input 输入内容
         */
        void onComplete(String input);

        /**
         * 输入变化
         *
         * @param input 变化内容
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
                    setText(strArray[i], false);
                    mEditText.setText("");
                }
            }
        }
    }


}
