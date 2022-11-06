package com.xuexiang.xui.widget.spinner.editspinner;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.xuexiang.xui.R;
import com.xuexiang.xui.utils.DensityUtils;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.ThemeUtils;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * 可编辑的Spinner
 *
 * @author xuexiang
 * @since 2018/6/5 下午5:50
 */
public class EditSpinner extends FrameLayout implements View.OnClickListener, AdapterView.OnItemClickListener, TextWatcher {
    /**
     * 最大行数
     */
    private static final int DEFAULT_MAX_LINE = 1;
    private static final int TOGGLE_POPUP_WINDOW_INTERVAL = 200;
    private EditText mEditText;
    private ImageView mIvArrow;
    private WeakReference<ListPopupWindow> mPopupWindow;
    private BaseEditSpinnerAdapter mAdapter;
    private long mPopupWindowHideTime;
    private Animation mAnimation;
    private Animation mResetAnimation;
    private AdapterView.OnItemClickListener mOnItemClickListener;
    private int mMaxLine = DEFAULT_MAX_LINE;
    private Drawable mDropDownBg;
    private int mPopAnimStyle;

    private boolean mIsShowFilterData = true;

    private boolean mIsFilterKey = false;

    public EditSpinner(Context context) {
        this(context, null);
    }

    public EditSpinner(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.EditSpinnerStyle);
    }

    public EditSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initAttrs(context, attrs, defStyleAttr);
        initAnimation();
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.xui_layout_edit_spinner, this);
        mEditText = findViewById(R.id.edit_spinner_edit);
        mIvArrow = findViewById(R.id.edit_spinner_arrow);
        mIvArrow.setOnClickListener(this);
        mEditText.addTextChangedListener(this);
    }

    private void initAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.EditSpinner, defStyleAttr, 0);
        mIsShowFilterData = typedArray.getBoolean(R.styleable.EditSpinner_es_isShowFilterData, true);
        mIsFilterKey = typedArray.getBoolean(R.styleable.EditSpinner_es_isFilterKey, false);

        int imageId = typedArray.getResourceId(R.styleable.EditSpinner_es_arrowImage, 0);
        if (imageId != 0) {
            mIvArrow.setImageResource(imageId);
        }
        int arrowMargin = typedArray.getDimensionPixelSize(R.styleable.EditSpinner_es_arrowMargin, -1);
        if (arrowMargin != -1) {
            LayoutParams params = (LayoutParams) mIvArrow.getLayoutParams();
            params.setMargins(arrowMargin, 0, arrowMargin, 0);
            mIvArrow.setLayoutParams(params);
        }
        mEditText.setHint(typedArray.getString(R.styleable.EditSpinner_es_hint));
        int bg = typedArray.getResourceId(R.styleable.EditSpinner_es_background, 0);
        if (bg != 0) {
            mEditText.setBackgroundResource(bg);
        }
        mMaxLine = typedArray.getInt(R.styleable.EditSpinner_es_maxLine, DEFAULT_MAX_LINE);
        mEditText.setMaxLines(mMaxLine);
        int height = typedArray.getDimensionPixelSize(R.styleable.EditSpinner_es_height, ThemeUtils.resolveDimension(getContext(), R.attr.ms_item_height_size));
        mEditText.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
        setTextColors(ResUtils.getColorStateListAttrRes(context, typedArray, R.styleable.EditSpinner_es_textColor));
        setTextSize(typedArray.getDimensionPixelSize(R.styleable.EditSpinner_es_textSize, ThemeUtils.resolveDimension(getContext(), R.attr.xui_config_size_spinner_text)));
        int entriesId = typedArray.getResourceId(R.styleable.EditSpinner_es_entries, 0);
        if (entriesId != 0) {
            setItems(ResUtils.getStringArray(context, entriesId));
        }
        mDropDownBg = ResUtils.getDrawableAttrRes(context, typedArray, R.styleable.EditSpinner_es_dropdown_bg);
        boolean enable = typedArray.getBoolean(R.styleable.EditSpinner_es_enable, true);
        setEnabled(enable);

        int maxLength = typedArray.getInteger(R.styleable.EditSpinner_es_maxLength, -1);
        setMaxLength(maxLength);

        int maxEms = typedArray.getInteger(R.styleable.EditSpinner_es_maxEms, -1);
        setMaxEms(maxEms);

        mPopAnimStyle = typedArray.getResourceId(R.styleable.EditSpinner_es_popAnimStyle, -1);

        typedArray.recycle();
    }

    private void initAnimation() {
        mAnimation = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mAnimation.setDuration(300);
        mAnimation.setFillAfter(true);
        mResetAnimation = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mResetAnimation.setDuration(300);
        mResetAnimation.setFillAfter(true);
    }

    private void setBaseAdapter(BaseAdapter adapter) {
        if (mPopupWindow == null) {
            mPopupWindow = new WeakReference<>(buildPopupWindow());
        }
        ListPopupWindow popupWindow = getPopupWindow();
        if (popupWindow != null) {
            popupWindow.setAdapter(adapter);
        }
    }

    private ListPopupWindow buildPopupWindow() {
        ListPopupWindow popupWindow = new ListPopupWindow(getContext()) {
            @Override
            public void show() {
                if (!isShowing()) {
                    mIvArrow.startAnimation(mAnimation);
                }
                super.show();
            }
        };
        if (mPopAnimStyle != -1) {
            popupWindow.setAnimationStyle(mPopAnimStyle);
        }
        popupWindow.setOnItemClickListener(this);
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        popupWindow.setPromptPosition(ListPopupWindow.POSITION_PROMPT_BELOW);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setAnchorView(mEditText);
        popupWindow.setVerticalOffset(ThemeUtils.resolveDimension(getContext(), R.attr.ms_dropdown_offset));
        popupWindow.setListSelector(ResUtils.getDrawable(getContext(), R.drawable.xui_config_list_item_selector));
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mPopupWindowHideTime = System.currentTimeMillis();
                mIvArrow.startAnimation(mResetAnimation);
            }
        });
        if (mDropDownBg != null) {
            popupWindow.setBackgroundDrawable(mDropDownBg);
        } else {
            popupWindow.setBackgroundDrawable(ResUtils.getDrawable(getContext(), R.drawable.ms_drop_down_bg_radius));
        }
        return popupWindow;
    }


    @Override
    public final void onClick(View v) {
        togglePopupWindow();
    }

    private void togglePopupWindow() {
        if (System.currentTimeMillis() - mPopupWindowHideTime > TOGGLE_POPUP_WINDOW_INTERVAL) {
            if (mAdapter == null || mPopupWindow == null) {
                return;
            }
            showFilterData("");
        }
    }

    @Override
    public final void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String selectContent = ((BaseEditSpinnerAdapter) parent.getAdapter()).getItemString(position);
        setText(selectContent);
        dismissDropDown();
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(parent, view, position, id);
        }
    }

    @Override
    public final void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public final void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public final void afterTextChanged(Editable s) {
        String key = s.toString();
        mEditText.setSelection(key.length());
        if (!TextUtils.isEmpty(key)) {
            if (mIsShowFilterData) {
                showFilterData(key);
            }
        } else {
            dismissDropDown();
        }
    }

    private void showFilterData(String key) {
        if (mPopupWindow == null || mAdapter == null || mAdapter.getEditSpinnerFilter() == null) {
            dismissDropDown();
            return;
        }
        if (mAdapter.getEditSpinnerFilter().onFilter(key)) {
            showDropDown();
        } else {
            dismissDropDown();
        }
    }


    @Override
    protected void onDetachedFromWindow() {
        recycle();
        super.onDetachedFromWindow();
    }

    private void showDropDown() {
        ListPopupWindow popupWindow = getPopupWindow();
        if (popupWindow != null) {
            popupWindow.show();
        }
    }

    private void dismissDropDown() {
        ListPopupWindow popupWindow = getPopupWindow();
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
    }

    /**
     * 资源释放
     */
    private void recycle() {
        if (mIvArrow != null) {
            mIvArrow.clearAnimation();
        }
        if (mAnimation != null) {
            mAnimation.cancel();
        }
        if (mResetAnimation != null) {
            mResetAnimation.cancel();
        }
        setAdapter(null);
        dismissDropDown();
    }

    private ListPopupWindow getPopupWindow() {
        return mPopupWindow != null ? mPopupWindow.get() : null;
    }

    //==============对外接口=====================//

    /**
     * 获取编辑的内容
     *
     * @return 编辑的内容
     */
    public String getText() {
        return mEditText != null ? mEditText.getText().toString() : "";
    }

    /**
     * 设置默认可选项集合
     *
     * @param data 默认可选项集合
     * @return this
     */
    public EditSpinner setItems(String[] data) {
        mAdapter = new EditSpinnerAdapter<>(data)
                .setTextColor(mEditText.getTextColors().getDefaultColor())
                .setTextSize(mEditText.getTextSize())
                .setIsFilterKey(mIsFilterKey);
        setAdapter(mAdapter);
        return this;
    }

    /**
     * 设置默认可选项集合
     *
     * @param data 默认可选项集合
     * @return this
     */
    public EditSpinner setItems(List<String> data) {
        mAdapter = new EditSpinnerAdapter<>(data)
                .setTextColor(mEditText.getTextColors().getDefaultColor())
                .setTextSize(mEditText.getTextSize())
                .setIsFilterKey(mIsFilterKey);
        setAdapter(mAdapter);
        return this;
    }

    /**
     * 设置下拉框条目点击监听
     *
     * @param listener 下拉框条目点击监听
     * @return this
     */
    public EditSpinner setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        mOnItemClickListener = listener;
        return this;
    }

    /**
     * 设置默认内容
     *
     * @param text 输入的内容
     * @return this
     */
    public EditSpinner setText(@NonNull String text) {
        if (mEditText != null) { //可以传空字符串
            mEditText.removeTextChangedListener(this);
            mEditText.setText(text);
            mEditText.setSelection(text.length());
            mEditText.addTextChangedListener(this);
        }
        return this;
    }

    /**
     * 设置输入框字体的颜色
     *
     * @param colors 输入框字体的颜色
     * @return this
     */
    public EditSpinner setTextColors(ColorStateList colors) {
        if (mEditText != null && colors != null) {
            mEditText.setTextColor(colors);
            if (mAdapter != null && mAdapter instanceof EditSpinnerAdapter) {
                ((EditSpinnerAdapter) mAdapter).setTextColor(colors.getDefaultColor());
            }
        }
        return this;
    }

    /**
     * 设置输入框字体的颜色
     *
     * @param color 输入框字体的颜色
     * @return this
     */
    public EditSpinner setTextColor(@ColorInt int color) {
        if (mEditText != null) {
            mEditText.setTextColor(color);
            if (mAdapter != null && mAdapter instanceof EditSpinnerAdapter) {
                ((EditSpinnerAdapter) mAdapter).setTextColor(color);
            }
        }
        return this;
    }

    /**
     * 设置输入框的背景颜色
     *
     * @param backgroundSelector 输入框的背景颜色
     * @return this
     */
    public EditSpinner setBackgroundSelector(@DrawableRes int backgroundSelector) {
        if (mEditText != null) {
            mEditText.setBackgroundResource(backgroundSelector);
        }
        return this;
    }

    /**
     * 设置是否显示key为醒目的颜色
     *
     * @param isFilterKey 是否显示key为醒目的颜色
     * @return this
     */
    public EditSpinner setIsFilterKey(boolean isFilterKey) {
        if (mEditText != null) {
            if (mAdapter != null && mAdapter instanceof EditSpinnerAdapter) {
                ((EditSpinnerAdapter) mAdapter).setIsFilterKey(isFilterKey);
            }
        }
        return this;
    }

    /**
     * 设置显示key的醒目颜色
     *
     * @param filterColor 显示key的醒目颜色
     * @return this
     */
    public EditSpinner setFilterColor(String filterColor) {
        if (mEditText != null) {
            if (mAdapter != null && mAdapter instanceof EditSpinnerAdapter) {
                ((EditSpinnerAdapter) mAdapter).setFilterColor(filterColor);
            }
        }
        return this;
    }

    /**
     * 设置输入框的字体大小
     *
     * @param textSize 输入框的字体大小
     * @return this
     */
    public EditSpinner setTextSize(float textSize) {
        if (mEditText != null) {
            mEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            if (mAdapter != null && mAdapter instanceof EditSpinnerAdapter) {
                ((EditSpinnerAdapter) mAdapter).setTextSize(textSize);
            }
        }
        return this;
    }

    /**
     * 设置输入框的提示信息
     *
     * @param hint
     * @return
     */
    public EditSpinner setHint(String hint) {
        if (mEditText != null) {
            mEditText.setHint(hint);
        }
        return this;
    }

    public EditSpinner setArrowImageDrawable(Drawable drawable) {
        if (mIvArrow != null) {
            mIvArrow.setImageDrawable(drawable);
        }
        return this;
    }

    /**
     * 设置箭头图片
     *
     * @param res
     * @return
     */
    public EditSpinner setArrowImageResource(@DrawableRes int res) {
        if (mIvArrow != null) {
            mIvArrow.setImageResource(res);
        }
        return this;
    }

    /**
     * 设置下拉框适配器
     *
     * @param adapter 下拉框适配器
     * @return this
     */
    public EditSpinner setAdapter(BaseEditSpinnerAdapter adapter) {
        mAdapter = adapter;
        setBaseAdapter(mAdapter);
        return this;
    }

    /**
     * 设置输入框最大的行数
     *
     * @param maxLine
     * @return
     */
    public EditSpinner setMaxLine(int maxLine) {
        if (mEditText != null) {
            mMaxLine = maxLine;
            mEditText.setMaxLines(this.mMaxLine);
        }
        return this;
    }

    /**
     * 设置输入框的高度
     *
     * @param dp
     * @return
     */
    public EditSpinner setEditTextHeight(int dp) {
        if (mEditText != null) {
            mEditText.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtils.dp2px(getContext(), dp)));
        }
        return this;
    }

    /**
     * 设置输入框的宽度
     *
     * @param dp
     * @return
     */
    public EditSpinner setEditTextWidth(int dp) {
        ViewGroup.LayoutParams params = getLayoutParams();
        params.width = DensityUtils.dp2px(getContext(), dp);
        setLayoutParams(params);
        return this;
    }

    /**
     * 设置enable
     *
     * @param enabled
     * @return
     */
    @Override
    public void setEnabled(boolean enabled) {
        if (mEditText != null) {
            mEditText.setFocusable(enabled);
            mEditText.setFocusableInTouchMode(enabled);
            mEditText.setEnabled(enabled);
            mIvArrow.setEnabled(enabled);
        }
    }

    /**
     * 设置输入框的最大字符长度
     *
     * @param maxLength
     * @return
     */
    public EditSpinner setMaxLength(int maxLength) {
        if (mEditText != null && maxLength > 0) {
            InputFilter[] filters = {new InputFilter.LengthFilter(maxLength)};
            mEditText.setFilters(filters);
        }
        return this;
    }

    /**
     * 设置输入框的最大字符宽度
     *
     * @param maxEms
     * @return
     */
    public EditSpinner setMaxEms(int maxEms) {
        if (mEditText != null && maxEms > 0) {
            mEditText.setMaxEms(maxEms);
        }
        return this;
    }

    /**
     * 增加文字监听
     *
     * @param watcher
     * @return
     */
    public EditSpinner addTextChangedListener(TextWatcher watcher) {
        if (mEditText != null) {
            mEditText.addTextChangedListener(watcher);
        }
        return this;
    }

    /**
     * 设置输入的类型
     *
     * @param type
     * @return
     */
    public EditSpinner setInputType(int type) {
        if (mEditText != null) {
            mEditText.setInputType(type);
        }
        return this;
    }

    /**
     * 获取输入框控件
     *
     * @return
     */
    public EditText getEditText() {
        return mEditText;
    }


}
