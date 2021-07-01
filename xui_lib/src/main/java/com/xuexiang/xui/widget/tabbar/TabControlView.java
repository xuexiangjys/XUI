/*
 * Copyright (C) 2019 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xuexiang.xui.widget.tabbar;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.xuexiang.xui.R;
import com.xuexiang.xui.XUI;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.ThemeUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.github.inflationx.calligraphy3.HasTypeface;

import static android.os.Build.VERSION_CODES.JELLY_BEAN;

/**
 * 选项卡控制器（单选）（RadioButton实现）
 *
 * @author xuexiang
 * @since 2019/1/14 下午10:03
 */
public class TabControlView extends RadioGroup implements HasTypeface {

    private Context mContext;

    /**
     * Tab选中的监听
     */
    private OnTabSelectionChangedListener mListener;

    /**
     * 字体大小
     */
    private int mTextSize;
    /**
     * 边框宽度
     */
    private int mStrokeWidth;
    /**
     * 选项间距
     */
    private int mItemPadding;
    /**
     * 选项水平间距
     */
    private int mItemPaddingHorizontal;
    /**
     * 选项垂直间距
     */
    private int mItemPaddingVertical;
    /**
     * 选中背景的颜色
     */
    private int mSelectedColor;
    /**
     * 未选中背景的颜色
     */
    private int mUnselectedColor;
    /**
     * 选中文字的颜色
     */
    private int mSelectedTextColor;
    /**
     * 未选中文字的颜色
     */
    private int mUnselectedTextColor;
    /**
     * 默认选中项的索引
     */
    private int mDefaultSelection = -1;
    /**
     * 是否展开（填充满）
     */
    private boolean mStretch = false;
    /**
     * 是否等宽显示
     */
    private boolean mEqualWidth = false;
    private ColorStateList mTextColorStateList;

    //Item organization

    private LinkedHashMap<String, String> mItemMap = new LinkedHashMap<>();
    private List<RadioButton> mOptions;
    /**
     * Used to pass along the selection change event
     * Calls onSelectionChangedListener with mIdentifier and value of selected segment
     */
    private OnCheckedChangeListener mSelectionChangedListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (mListener != null) {
                String identifier = ((RadioButton) group.findViewById(checkedId)).getText().toString();
                String value = mItemMap.get(identifier);
                mListener.newSelection(identifier, value);
            }
        }
    };

    public TabControlView(Context context) {
        super(context, null);
        //Initialize
        init(context);
        // Setup the view
        update();
    }

    public TabControlView(Context context, AttributeSet attrs) throws Exception {
        super(context, attrs);
        //Initialize
        init(context);
        initAttrs(context, attrs);
        //Setup the view
        update();
    }

    private void initAttrs(Context context, AttributeSet attrs) throws Exception {
        TypedArray attributes = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.TabControlView,
                0, 0);
        try {
            mTextSize = attributes.getDimensionPixelSize(R.styleable.TabControlView_tcv_textSize, ResUtils.getDimensionPixelSize(R.dimen.default_tcv_text_size));
            mSelectedColor = attributes.getColor(R.styleable.TabControlView_tcv_selectedColor, ThemeUtils.getMainThemeColor(context));
            mUnselectedColor = attributes.getColor(R.styleable.TabControlView_tcv_unselectedColor, Color.TRANSPARENT);
            mSelectedTextColor = attributes.getColor(R.styleable.TabControlView_tcv_selectedTextColor, Color.WHITE);
            mUnselectedTextColor = attributes.getColor(R.styleable.TabControlView_tcv_unselectedTextColor, ThemeUtils.getMainThemeColor(context));
            mStrokeWidth = attributes.getDimensionPixelSize(R.styleable.TabControlView_tcv_strokeWidth, ResUtils.getDimensionPixelSize(R.dimen.default_tcv_stroke_width));
            mItemPadding = attributes.getDimensionPixelSize(R.styleable.TabControlView_tcv_item_padding, -1);
            mItemPaddingHorizontal = attributes.getDimensionPixelSize(R.styleable.TabControlView_tcv_item_padding_horizontal, -1);
            mItemPaddingVertical = attributes.getDimensionPixelSize(R.styleable.TabControlView_tcv_item_padding_vertical, -1);

            //Set text mSelectedColor state list
            mTextColorStateList = new ColorStateList(new int[][]{
                    {-android.R.attr.state_checked}, {android.R.attr.state_checked}},
                    new int[]{mUnselectedTextColor, mSelectedTextColor}
            );

            mDefaultSelection = attributes.getInt(R.styleable.TabControlView_tcv_defaultSelection, mDefaultSelection);
            mEqualWidth = attributes.getBoolean(R.styleable.TabControlView_tcv_equalWidth, mEqualWidth);
            mStretch = attributes.getBoolean(R.styleable.TabControlView_tcv_stretch, mStretch);

            CharSequence[] itemArray = attributes.getTextArray(R.styleable.TabControlView_tcv_items);
            CharSequence[] valueArray = attributes.getTextArray(R.styleable.TabControlView_tcv_values);

            //Item and value arrays need to be of the same length
            setItems(itemArray, valueArray);
        } finally {
            attributes.recycle();
        }
    }

    private void init(Context context) {
        mContext = context;
        //Provide a tad bit of padding for the view
        setPadding(10, 10, 10, 10);
    }

    /**
     * 更新显示
     */
    private void update() {

        //Remove all views...
        removeAllViews();

        //Ensure orientation is horizontal
        setOrientation(RadioGroup.HORIZONTAL);

        float textWidth = 0;
        mOptions = new ArrayList<>();

        Iterator<Map.Entry<String, String>> itemIterator = mItemMap.entrySet().iterator();
        int i = 0;
        while (itemIterator.hasNext()) {
            Map.Entry<String, String> item = itemIterator.next();

            RadioButton rb = new RadioButton(mContext);
            rb.setTextColor(mTextColorStateList);
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            if (mStretch) {
                params.weight = 1.0F;
            }
            if (i > 0) {
                params.setMarginStart(-mStrokeWidth);
            }

            rb.setLayoutParams(params);

            //Clear out button drawable (text only)
            rb.setButtonDrawable(new StateListDrawable());

            //Create state list for background
            if (i == 0) {
                //Left
                if (isRtl()) {
                    updateRadioButton(rb, R.drawable.tcv_right_option, R.drawable.tcv_right_option_selected);
                } else {
                    updateRadioButton(rb, R.drawable.tcv_left_option, R.drawable.tcv_left_option_selected);
                }
            } else if (i == (mItemMap.size() - 1)) {
                //Right
                if (isRtl()) {
                    updateRadioButton(rb, R.drawable.tcv_left_option, R.drawable.tcv_left_option_selected);
                } else {
                    updateRadioButton(rb, R.drawable.tcv_right_option, R.drawable.tcv_right_option_selected);
                }
            } else {
                //Middle
                updateRadioButton(rb, R.drawable.tcv_middle_option, R.drawable.tcv_middle_option_selected);
            }

            rb.setLayoutParams(params);
            if (mItemPadding != -1) {
                rb.setPadding(mItemPadding, mItemPadding, mItemPadding, mItemPadding);
            }
            if (mItemPaddingHorizontal != -1 && mItemPaddingVertical != -1) {
                rb.setPadding(mItemPaddingHorizontal, mItemPaddingVertical, mItemPaddingHorizontal, mItemPaddingVertical);
            }
            rb.setMinWidth(mStrokeWidth * 10);
            rb.setGravity(Gravity.CENTER);
            rb.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
            rb.setTypeface(XUI.getDefaultTypeface());
            rb.setText(item.getKey());
            textWidth = Math.max(rb.getPaint().measureText(item.getKey()), textWidth);
            mOptions.add(rb);

            i++;
        }

        //We do this to make all the segments the same width
        for (RadioButton option : mOptions) {
            if (mEqualWidth) {
                option.setWidth((int) (textWidth + (mStrokeWidth * 20)));
            }
            addView(option);
        }

        this.setOnCheckedChangeListener(mSelectionChangedListener);

        if (mDefaultSelection > -1) {
            RadioButton radioButton = (RadioButton) getChildAt(mDefaultSelection);
            if (radioButton != null) {
                this.check(radioButton.getId());
            }
        }
    }

    private boolean isRtl() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 &&
                getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
    }

    /**
     * 更新RadioButton的显示
     *
     * @param rb
     * @param tcv_option_normal
     * @param tcv_option_selected
     */
    private void updateRadioButton(RadioButton rb, int tcv_option_normal, int tcv_option_selected) {
        GradientDrawable unselected = (GradientDrawable) mContext.getResources().getDrawable(tcv_option_normal).mutate();
        unselected.setStroke(mStrokeWidth, mSelectedColor);
        unselected.setDither(true);
        unselected.setColor(mUnselectedColor);
        GradientDrawable selected = (GradientDrawable) mContext.getResources().getDrawable(tcv_option_selected).mutate();
        selected.setColor(mSelectedColor);
        selected.setStroke(mStrokeWidth, mSelectedColor);

        setRadioButtonBackground(rb, unselected, selected);
    }

    /**
     * 设置RadioButton的背景
     *
     * @param rb
     * @param unselected
     * @param selected
     */
    private void setRadioButtonBackground(RadioButton rb, GradientDrawable unselected, GradientDrawable selected) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{-android.R.attr.state_checked}, unselected);
        stateListDrawable.addState(new int[]{android.R.attr.state_checked}, selected);
        if (Build.VERSION.SDK_INT < JELLY_BEAN) {
            rb.setBackgroundDrawable(stateListDrawable);
        } else {
            rb.setBackground(stateListDrawable);
        }
    }

    /**
     * Get currently selected segment and the view mIdentifier
     *
     * @return string array of mIdentifier [0] value of currently selected segment [1]
     */
    public String[] getCheckedWithIdentifier() {
        String identifier = ((RadioButton) findViewById(getCheckedRadioButtonId())).getText().toString();
        String value = mItemMap.get(identifier);
        return new String[]{identifier, value};
    }

    /**
     * Get currently selected segment
     *
     * @return value of currently selected segment
     */
    public String getChecked() {
        return mItemMap.get(((RadioButton) findViewById(getCheckedRadioButtonId())).getText().toString());
    }

    /**
     * 为每一个选项设置 items and values
     *
     * @param itemArray
     * @param valueArray
     */
    public TabControlView setItems(String[] itemArray, String[] valueArray) throws Exception {
        mItemMap.clear();
        if (itemArray != null && valueArray != null) {
            if (itemArray.length != valueArray.length) {
                throw new Exception("Item labels and value arrays must be the same size");
            }
        }
        if (itemArray != null) {
            if (valueArray != null) {
                for (int i = 0; i < itemArray.length; i++) {
                    mItemMap.put(itemArray[i], valueArray[i]);
                }
            } else {
                for (CharSequence item : itemArray) {
                    mItemMap.put(item.toString(), item.toString());
                }
            }
        }
        update();
        return this;
    }

    /**
     * 为每一个选项设置 items and values
     *
     * @param itemArray
     * @param valueArray
     */
    private void setItems(CharSequence[] itemArray, CharSequence[] valueArray) throws Exception {
        if (itemArray != null && valueArray != null) {
            if (itemArray.length != valueArray.length) {
                throw new Exception("Item labels and value arrays must be the same size");
            }
        }
        if (itemArray != null) {
            if (valueArray != null) {
                for (int i = 0; i < itemArray.length; i++) {
                    mItemMap.put(itemArray[i].toString(), valueArray[i].toString());
                }
            } else {
                for (CharSequence item : itemArray) {
                    mItemMap.put(item.toString(), item.toString());
                }
            }
        }
    }

    /**
     * 为每一个选项设置 items and values .并且设置一个默认选中的序号
     *
     * @param items
     * @param values
     * @param defaultSelection
     */
    public TabControlView setItems(String[] items, String[] values, int defaultSelection) throws Exception {
        if (defaultSelection > (items.length - 1)) {
            throw new Exception("Default selection cannot be greater than the number of items");
        } else {
            mDefaultSelection = defaultSelection;
            setItems(items, values);
        }
        return this;
    }

    /**
     * 设置默认选中的Tab
     *
     * @param defaultSelection
     * @return
     */
    public TabControlView setDefaultSelection(int defaultSelection) throws Exception {
        if (defaultSelection > (mItemMap.size() - 1)) {
            throw new Exception("Default selection cannot be greater than the number of items");
        } else {
            mDefaultSelection = defaultSelection;
            update();
        }
        return this;
    }

    /**
     * 通过值 设置选中的Tab
     *
     * @param value
     */
    public TabControlView setSelection(String value) {
        setSelection(value, true);
        return this;
    }

    /**
     * 通过值 设置选中的Tab
     *
     * @param value
     */
    public TabControlView setSelection(String value, boolean isSilent) {
        String title = getTitleByValue(value);
        setSelectionTitle(title, isSilent);
        return this;
    }

    /**
     * 通过标题设置选中的Tab
     *
     * @param title
     * @return
     */
    public TabControlView setSelectionTitle(String title) {
        setSelectionTitle(title, true);
        return this;
    }

    /**
     * 通过标题设置选中的Tab
     *
     * @param title
     * @param isSilent 是否静默设置
     * @return
     */
    public TabControlView setSelectionTitle(String title, boolean isSilent) {
        for (RadioButton option : mOptions) {
            if (option.getText().toString().equalsIgnoreCase(title)) {
                if (isSilent) {
                    setOnCheckedChangeListener(null);
                    this.check(option.getId());
                    setOnCheckedChangeListener(mSelectionChangedListener);
                } else {
                    this.check(option.getId());
                }
            }
        }
        return this;
    }

    private String getTitleByValue(String value) {
        if (mItemMap.containsValue(value)) {
            String title;
            for (String key : mItemMap.keySet()) {
                title = mItemMap.get(key);
                if (title != null && title.equalsIgnoreCase(value)) {
                    return key;
                }
            }
        }
        return "";
    }

    /**
     * Sets the colors used when drawing the view. The primary color will be used for selected color
     * and unselected text color, while the secondary color will be used for unselected color
     * and selected text color.
     *
     * @param primaryColor
     * @param secondaryColor
     */
    public TabControlView setColors(int primaryColor, int secondaryColor) {
        mSelectedColor = primaryColor;
        mSelectedTextColor = secondaryColor;
        mUnselectedColor = secondaryColor;
        mUnselectedTextColor = primaryColor;

        //Set text mSelectedColor state list
        mTextColorStateList = new ColorStateList(new int[][]{
                {-android.R.attr.state_checked}, {android.R.attr.state_checked}},
                new int[]{mUnselectedTextColor, mSelectedTextColor}
        );

        update();
        return this;
    }

    /**
     * 设置颜色
     *
     * @param selectedColor
     * @param selectedTextColor
     * @param unselectedColor
     * @param unselectedTextColor
     */
    public TabControlView setColors(int selectedColor, int selectedTextColor, int unselectedColor, int unselectedTextColor) {
        mSelectedColor = selectedColor;
        mSelectedTextColor = selectedTextColor;
        mUnselectedColor = unselectedColor;
        mUnselectedTextColor = unselectedTextColor;

        //Set text mSelectedColor state list
        mTextColorStateList = new ColorStateList(new int[][]{
                {-android.R.attr.state_checked}, {android.R.attr.state_checked}},
                new int[]{unselectedTextColor, selectedTextColor}
        );

        update();
        return this;
    }

    /**
     * 设置Tab选中的监听
     *
     * @param listener
     */
    public TabControlView setOnTabSelectionChangedListener(OnTabSelectionChangedListener listener) {
        mListener = listener;
        return this;
    }

    /**
     * 设置是否等宽
     *
     * @param equalWidth
     */
    public TabControlView setEqualWidth(boolean equalWidth) {
        mEqualWidth = equalWidth;
        update();
        return this;
    }

    /**
     * 设置是否伸展开（match_parent)
     *
     * @param stretch
     */
    public TabControlView setStretch(boolean stretch) {
        mStretch = stretch;
        update();
        return this;
    }

    @Override
    public void setTypeface(Typeface typeface) {
        if (mOptions != null) {
            for (int i = 0; i < mOptions.size(); i++) {
                mOptions.get(i).setTypeface(typeface);
            }
        }
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        update();
    }

    /**
     * Tab选中的监听
     */
    public interface OnTabSelectionChangedListener {
        /**
         * 选中
         *
         * @param title 选中展示的内容
         * @param value 选中的值
         */
        void newSelection(String title, String value);
    }

}
