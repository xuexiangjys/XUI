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
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import android.support.annotation.NonNull;

import com.xuexiang.xui.R;
import com.xuexiang.xui.XUI;
import com.xuexiang.xui.utils.ResUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.HasTypeface;

import static android.os.Build.VERSION_CODES.JELLY_BEAN;

/**
 * 多选Tab（CheckBox实现）
 *
 * @author xuexiang
 * @since 2019/1/2 下午11:25
 */
public class MultiTabControlView extends LinearLayout implements HasTypeface {

    private Context mContext;

    /**
     * 点击监听
     */
    private OnMultiTabSelectionChangedListener mListener;

    /**
     * 字体大小
     */
    private int mTextSize;
    /**
     * 边框宽度
     */
    private int mStrokeWidth;
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
     * 默认选中项的索引集合
     */
    private int[] mDefaultSelectionList;
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
    private List<CheckBox> mOptions;

    /**
     * 增加多选框监听
     *
     * @param cb
     */
    private void addOnCheckedChangeListener(CheckBox cb) {
        if (cb != null) {
            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton checkBox, boolean b) {
                    if (mListener != null) {
                        String identifier = checkBox.getText().toString();
                        String value = mItemMap.get(identifier);
                        mListener.newSelection(identifier, value, checkBox.isChecked());
                    }
                }
            });
        }
    }

    public MultiTabControlView(Context context) {
        super(context, null);
        //Initialize
        init(context);
        // Setup the view
        update();
    }

    public MultiTabControlView(Context context, AttributeSet attrs) throws Exception {
        super(context, attrs);
        //Initialize
        init(context);
        initAttrs(context, attrs);
        //Setup the view
        update();
    }

    private void initAttrs(Context context, AttributeSet attrs) throws Exception {
        if (isInEditMode()) {
            return;
        }

        TypedArray attributes = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.TabControlView,
                0, 0);
        try {
            mTextSize = attributes.getDimensionPixelSize(R.styleable.TabControlView_tcv_textSize, ResUtils.getDimensionPixelSize(R.dimen.default_tcv_text_size));
            mSelectedColor = attributes.getColor(R.styleable.TabControlView_tcv_selectedColor, ResUtils.getColor(R.color.xui_config_color_main_theme));
            mUnselectedColor = attributes.getColor(R.styleable.TabControlView_tcv_unselectedColor, Color.TRANSPARENT);
            mSelectedTextColor = attributes.getColor(R.styleable.TabControlView_tcv_selectedTextColor, Color.WHITE);
            mUnselectedTextColor = attributes.getColor(R.styleable.TabControlView_tcv_unselectedTextColor, ResUtils.getColor(R.color.xui_config_color_main_theme));
            mStrokeWidth = attributes.getDimensionPixelSize(R.styleable.TabControlView_tcv_strokeWidth, ResUtils.getDimensionPixelSize(R.dimen.default_tcv_stroke_width));
            //Set text mSelectedColor state list
            mTextColorStateList = new ColorStateList(new int[][]{
                    {-android.R.attr.state_checked}, {android.R.attr.state_checked}},
                    new int[]{mUnselectedTextColor, mSelectedTextColor}
            );

            int defaultSelection = attributes.getInt(R.styleable.TabControlView_tcv_defaultSelection, -1);
            if (defaultSelection > -1) {
                mDefaultSelectionList = new int[]{defaultSelection};
            }
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
        //Needed for calling the updateSide "setbackground" method
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
        setOrientation(LinearLayout.HORIZONTAL);

        float textWidth = 0;
        mOptions = new ArrayList<>();

        Iterator<Map.Entry<String, String>> itemIterator = mItemMap.entrySet().iterator();
        int i = 0;
        while (itemIterator.hasNext()) {
            Map.Entry<String, String> item = itemIterator.next();

            CheckBox cb = new CheckBox(mContext);
            cb.setTextColor(mTextColorStateList);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            if (mStretch) {
                params.weight = 1.0F;
            }
            if (i > 0) {
                params.setMargins(-mStrokeWidth, 0, 0, 0);
            }
            cb.setLayoutParams(params);

            //Clear out button drawable (text only)
            cb.setButtonDrawable(new StateListDrawable());

            //Create state list for background
            if (i == 0) {
                //Left
                updateCheckBox(cb, R.drawable.tcv_left_option, R.drawable.tcv_left_option_selected);
            } else if (i == (mItemMap.size() - 1)) {
                //Right
                updateCheckBox(cb, R.drawable.tcv_right_option, R.drawable.tcv_right_option_selected);
            } else {
                //Middle
                updateCheckBox(cb, R.drawable.tcv_middle_option, R.drawable.tcv_middle_option_selected);
            }

            cb.setLayoutParams(params);
            cb.setMinWidth(mStrokeWidth * 10);
            cb.setGravity(Gravity.CENTER);
            cb.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
            cb.setTypeface(XUI.getDefaultTypeface());
            cb.setText(item.getKey());
            addOnCheckedChangeListener(cb);
            textWidth = Math.max(cb.getPaint().measureText(item.getKey()), textWidth);
            mOptions.add(cb);

            i++;
        }

        //We do this to make all the segments the same width
        for (CheckBox option : mOptions) {
            if (mEqualWidth) {
                option.setWidth((int) (textWidth + (mStrokeWidth * 20)));
            }
            addView(option);
        }

        if (mDefaultSelectionList != null && mDefaultSelectionList.length > 0) {
            for (int selection : mDefaultSelectionList) {
                setIsChecked(selection, true);
            }
        }
    }

    /**
     * 更新updateCheckBox的显示
     *
     * @param cb
     * @param tcv_option_normal
     * @param tcv_option_selected
     */
    private void updateCheckBox(CheckBox cb, int tcv_option_normal, int tcv_option_selected) {
        GradientDrawable unselected = (GradientDrawable) mContext.getResources().getDrawable(tcv_option_normal).mutate();
        unselected.setStroke(mStrokeWidth, mSelectedColor);
        unselected.setDither(true);
        unselected.setColor(mUnselectedColor);
        GradientDrawable selected = (GradientDrawable) mContext.getResources().getDrawable(tcv_option_selected).mutate();
        selected.setColor(mSelectedColor);
        selected.setStroke(mStrokeWidth, mSelectedColor);

        setCheckBoxBackground(cb, unselected, selected);
    }

    /**
     * 设置CheckBox的背景
     *
     * @param cb
     * @param unselected
     * @param selected
     */
    private void setCheckBoxBackground(CheckBox cb, GradientDrawable unselected, GradientDrawable selected) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{-android.R.attr.state_checked}, unselected);
        stateListDrawable.addState(new int[]{android.R.attr.state_checked}, selected);
        if (Build.VERSION.SDK_INT < JELLY_BEAN) {
            cb.setBackgroundDrawable(stateListDrawable);
        } else {
            cb.setBackground(stateListDrawable);
        }
    }

    public boolean isChecked(int position) {
        return ((CheckBox) getChildAt(position)).isChecked();
    }

    public void setIsChecked(int position, boolean isChecked) {
        CheckBox cb = (CheckBox) getChildAt(position);
        if (cb != null) {
            cb.setChecked(isChecked);
        }
    }

    /**
     * 为每一个选项设置 items and values
     *
     * @param itemArray
     * @param valueArray
     */
    public MultiTabControlView setItems(String[] itemArray, String[] valueArray) throws Exception {
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
     * 为每一个选项设置 items and values .并且设置默认选中的序号
     *
     * @param items
     * @param values
     * @param defaultSelection
     */
    public MultiTabControlView setItems(String[] items, String[] values, int... defaultSelection) throws Exception {
        if (defaultSelection == null) {
            throw new Exception("defaultSelectionList cannot be null");
        } else if (defaultSelection.length > items.length) {
            throw new Exception("the length of Default selectionList cannot be greater than the length of items");
        } else {
            for (int selection : defaultSelection) {
                if (selection > (mItemMap.size() - 1)) {
                    throw new Exception("Default selection cannot be greater than the number of items");
                }
            }
            mDefaultSelectionList = defaultSelection;
            setItems(items, values);
        }
        return this;
    }

    /**
     * 设置默认选中的Tab.
     *
     * @param defaultSelections
     */
    public MultiTabControlView setDefaultSelection(int... defaultSelections) throws Exception {
        if (defaultSelections == null) {
            throw new Exception("defaultSelectionList cannot be null");
        } else if (defaultSelections.length > mItemMap.size()) {
            throw new Exception("the length of Default selectionList cannot be greater than the length of items");
        } else {
            for (int selection : defaultSelections) {
                if (selection > (mItemMap.size() - 1)) {
                    throw new Exception("Default selection cannot be greater than the number of items");
                }
            }
            mDefaultSelectionList = defaultSelections;
            update();
        }
        return this;
    }

    /**
     * 设置默认选中的Tab.
     *
     * @param defaultSelections
     */
    public MultiTabControlView setDefaultSelection(@NonNull List<Integer> defaultSelections) throws Exception {
        int[] selections = new int[defaultSelections.size()];
        for (int i = 0; i < selections.length; i++) {
            selections[i] = defaultSelections.get(i);
        }
        setDefaultSelection(selections);
        return this;
    }

    /**
     * 通过值 设置选中的Tab
     *
     * @param values
     */
    public MultiTabControlView setSelection(String... values) {
        for (String value : values) {
            setSelection(value);
        }
        return this;
    }

    /**
     * 通过值 设置选中的Tab
     *
     * @param value
     */
    public MultiTabControlView setSelection(String value) {
        String buttonText = "";
        if (mItemMap.containsValue(value)) {
            for (String entry : mItemMap.keySet()) {
                if (mItemMap.get(entry).equalsIgnoreCase(value)) {
                    buttonText = entry;
                }
            }
        }
        for (CheckBox option : mOptions) {
            if (option.getText().toString().equalsIgnoreCase(buttonText)) {
                option.setChecked(true);
            }
        }
        return this;
    }

    /**
     * Sets the colors used when drawing the view. The primary color will be used for selected color
     * and unselected text color, while the secondary color will be used for unselected color
     * and selected text color.
     *
     * @param primaryColor
     * @param secondaryColor
     */
    public MultiTabControlView setColors(int primaryColor, int secondaryColor) {
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
    public MultiTabControlView setColors(int selectedColor, int selectedTextColor, int unselectedColor, int unselectedTextColor) {
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
    public MultiTabControlView setOnMultiTabSelectionChangedListener(OnMultiTabSelectionChangedListener listener) {
        mListener = listener;
        return this;
    }

    /**
     * 设置是否等宽
     *
     * @param equalWidth
     */
    public MultiTabControlView setEqualWidth(boolean equalWidth) {
        mEqualWidth = equalWidth;
        update();
        return this;
    }

    /**
     * 设置是否伸展开（match_parent)
     *
     * @param stretch
     */
    public MultiTabControlView setStretch(boolean stretch) {
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

    /**
     * Tab选中的监听
     */
    public interface OnMultiTabSelectionChangedListener {
        /**
         * 选中
         *
         * @param title     选中展示的内容
         * @param value     选中的值
         * @param isChecked 是否选中
         */
        void newSelection(String title, String value, boolean isChecked);
    }
}
