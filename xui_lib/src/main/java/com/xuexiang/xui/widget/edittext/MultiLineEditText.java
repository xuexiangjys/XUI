package com.xuexiang.xui.widget.edittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xuexiang.xui.R;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.ThemeUtils;

import static com.xuexiang.xui.utils.DensityUtils.dp2px;
import static com.xuexiang.xui.utils.DensityUtils.sp2px;

/**
 * 多行计数输入框
 * ignoreCnOrEn 为false的时候
 * 1个中文算1个
 * 2个英文算1个
 * 另外：如：只有一个英文时也算1个
 *
 * @author XUE
 * @since 2019/3/22 13:46
 */
public class MultiLineEditText extends LinearLayout {
    private EditText mEtInput;
    private TextView mTvInputNumber;

    /**
     * 最大输入的字符数
     */
    private int mMaxCount;
    /**
     * 输入提示文字
     */
    private String mHintText;
    /**
     * 提示文字的颜色
     */
    private int mHintTextColor;
    /**
     * 是否忽略中英文差异
     */
    private boolean mIgnoreCnOrEn;
    /**
     * 输入内容
     */
    private String mContentText;
    /**
     * 输入框文字大小
     */
    private int mContentTextSize;
    /**
     * 输入框文字颜色
     */
    private int mContentTextColor;
    /**
     * 输入框高度
     */
    private float mContentViewHeight;
    /**
     * 输入框高度是否是固定高度，默认是true
     */
    private boolean mIsFixHeight;
    /**
     * 输入框padding
     */
    private int mContentPadding;
    /**
     * 输入框背景
     */
    private Drawable mContentBackground;
    /**
     * 是否显示剩余数目
     */
    private boolean mIsShowSurplusNumber;

    public MultiLineEditText(Context context) {
        this(context, null);
    }

    public MultiLineEditText(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.MultiLineEditTextStyle);
    }

    public MultiLineEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化属性
     *
     * @param context
     * @param attrs
     */
    private void initAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MultiLineEditText, defStyleAttr, 0);
        mMaxCount = typedArray.getInteger(R.styleable.MultiLineEditText_mlet_maxCount, 240);
        mIgnoreCnOrEn = typedArray.getBoolean(R.styleable.MultiLineEditText_mlet_ignoreCnOrEn, true);
        mHintText = typedArray.getString(R.styleable.MultiLineEditText_mlet_hintText);
        mHintTextColor = typedArray.getColor(R.styleable.MultiLineEditText_mlet_hintTextColor, ThemeUtils.resolveColor(getContext(), R.attr.xui_config_color_hint_text));
        mContentPadding = typedArray.getDimensionPixelSize(R.styleable.MultiLineEditText_mlet_contentPadding, dp2px(context, 10));
        mContentBackground = ResUtils.getDrawableAttrRes(getContext(), typedArray, R.styleable.MultiLineEditText_mlet_contentBackground);
        mContentText = typedArray.getString(R.styleable.MultiLineEditText_mlet_contentText);
        mContentTextColor = typedArray.getColor(R.styleable.MultiLineEditText_mlet_contentTextColor, ThemeUtils.resolveColor(getContext(), R.attr.xui_config_color_input_text));
        mContentTextSize = typedArray.getDimensionPixelSize(R.styleable.MultiLineEditText_mlet_contentTextSize, sp2px(context, 14));
        mContentViewHeight = typedArray.getDimensionPixelSize(R.styleable.MultiLineEditText_mlet_contentViewHeight, dp2px(context, 140));
        mIsFixHeight = typedArray.getBoolean(R.styleable.MultiLineEditText_mlet_isFixHeight, true);
        mIsShowSurplusNumber = typedArray.getBoolean(R.styleable.MultiLineEditText_mlet_showSurplusNumber, false);
        typedArray.recycle();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.xui_layout_multiline_edittext, this);
        mEtInput = view.findViewById(R.id.mlet_input);
        mTvInputNumber = view.findViewById(R.id.mlet_number);

        if (getBackground() == null) {
            setBackgroundResource(R.drawable.mlet_selector_bg);
        }

        mEtInput.addTextChangedListener(mTextWatcher);
        mEtInput.setHint(mHintText);
        mEtInput.setHintTextColor(mHintTextColor);
        mEtInput.setText(mContentText);
        mEtInput.setPadding(mContentPadding, mContentPadding, mContentPadding, 0);
        if (mContentBackground != null) {
            mEtInput.setBackground(mContentBackground);
        }
        mEtInput.setTextColor(mContentTextColor);
        mEtInput.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContentTextSize);
        if (mIsFixHeight) {
            mEtInput.setHeight((int) mContentViewHeight);
        } else {
            mEtInput.setMinHeight((int) mContentViewHeight);
        }
        /**
         * 配合 mTvInputNumber xml的 android:focusable="true"
         android:focusableInTouchMode="true"
         在mlet_input设置完文本后
         不给mlet_input 焦点
         */
        mTvInputNumber.requestFocus();
        //init
        configCount();
        mEtInput.setSelection(mEtInput.length()); // 将光标移动最后一个字符后面
        /**
         * focus后给背景设置Selected
         */
        mEtInput.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                MultiLineEditText.this.setSelected(b);
            }
        });
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        private int mEditStart;
        private int mEditEnd;

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

            mEditStart = mEtInput.getSelectionStart();
            mEditEnd = mEtInput.getSelectionEnd();

            // 先去掉监听器，否则会出现栈溢出
            mEtInput.removeTextChangedListener(mTextWatcher);

            if (mIgnoreCnOrEn) {
                //当输入字符个数超过限制的大小时，进行截断操作
                while (calculateLengthIgnoreCnOrEn(editable.toString()) > mMaxCount) {
                    editable.delete(mEditStart - 1, mEditEnd);
                    mEditStart--;
                    mEditEnd--;
                }
            } else {
                // 因为是中英文混合，单个字符而言，calculateLength函数都会返回1
                while (calculateLength(editable.toString()) > mMaxCount) { // 当输入字符个数超过限制的大小时，进行截断操作
                    editable.delete(mEditStart - 1, mEditEnd);
                    mEditStart--;
                    mEditEnd--;
                }
            }

            mEtInput.setSelection(mEditStart);

            // 恢复监听器
            mEtInput.addTextChangedListener(mTextWatcher);

            //update
            configCount();
        }
    };


    private long calculateLength(CharSequence c) {
        double len = 0;
        for (int i = 0; i < c.length(); i++) {
            int tmp = (int) c.charAt(i);
            if (tmp > 0 && tmp < 127) {
                len += 0.5;
            } else {
                len++;
            }
        }
        return Math.round(len);
    }

    private int calculateLengthIgnoreCnOrEn(CharSequence c) {
        return c != null ? c.length() : 0;
    }

    private void configCount() {
        if (mIgnoreCnOrEn) {
            int nowCount = calculateLengthIgnoreCnOrEn(mEtInput.getText().toString());
            updateCount(nowCount);
        } else {
            int nowCount = (int) calculateLength(mEtInput.getText().toString());
            updateCount(nowCount);
        }
    }

    private void updateCount(int nowCount) {
        if (mIsShowSurplusNumber) {
            mTvInputNumber.setText((mMaxCount - nowCount) + "/" + mMaxCount);
        } else {
            mTvInputNumber.setText(nowCount + "/" + mMaxCount);
        }
    }

    public EditText getEditText() {
        return mEtInput;
    }

    public TextView getCountTextView() {
        return mTvInputNumber;
    }

    /**
     * 设置填充内容
     *
     * @param content
     */
    public void setContentText(String content) {
        if (content != null && calculateContentLength(content) > mMaxCount) {
            content = content.substring(0, getSubStringIndex(content));
        }
        mContentText = content;
        if (mEtInput == null) {
            return;
        }
        mEtInput.setText(mContentText);
    }

    private long calculateContentLength(String content) {
        return mIgnoreCnOrEn ? calculateLengthIgnoreCnOrEn(content) : calculateLength(content);
    }

    private int getSubStringIndex(String content) {
        if (!mIgnoreCnOrEn) {
            double len = 0;
            for (int i = 0; i < content.length(); i++) {
                int tmp = (int) content.charAt(i);
                if (tmp > 0 && tmp < 127) {
                    len += 0.5;
                } else {
                    len++;
                }
                if (Math.round(len) == mMaxCount) {
                    return i + 1;
                }
            }
        }
        return mMaxCount;
    }

    /**
     * 获取输入的内容
     *
     * @return
     */
    public String getContentText() {
        if (mEtInput != null) {
            mContentText = mEtInput.getText() == null ? "" : mEtInput.getText().toString();
        }
        return mContentText;
    }

    public void setHintText(String hintText) {
        mHintText = hintText;
        if (mEtInput == null) {
            return;
        }
        mEtInput.setHint(hintText);
    }

    public void setContentTextSize(int size) {
        if (mEtInput == null) {
            return;
        }
        mEtInput.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    public void setContentTextColor(int color) {
        if (mEtInput == null) {
            return;
        }
        mEtInput.setTextColor(color);
    }

    public void setHintColor(int color) {
        if (mEtInput == null) {
            return;
        }
        mEtInput.setHintTextColor(color);
    }

    public String getHintText() {
        if (mEtInput != null) {
            mHintText = mEtInput.getHint() == null ? "" : mEtInput.getHint().toString();
        }
        return mHintText;
    }

    public MultiLineEditText setMaxCount(int max_count) {
        mMaxCount = max_count;
        configCount();
        return this;
    }

    public MultiLineEditText setIgnoreCnOrEn(boolean ignoreCnOrEn) {
        mIgnoreCnOrEn = ignoreCnOrEn;
        configCount();
        return this;
    }

    public MultiLineEditText setIsShowSurplusNumber(boolean isShowSurplusNumber) {
        mIsShowSurplusNumber = isShowSurplusNumber;
        configCount();
        return this;
    }

    /**
     * 输入的内容是否为空
     *
     * @return
     */
    public boolean isEmpty() {
        return TextUtils.isEmpty(getContentText());
    }

    /**
     * 输入的内容是否不为空
     *
     * @return
     */
    public boolean isNotEmpty() {
        return !TextUtils.isEmpty(getContentText());
    }
}
