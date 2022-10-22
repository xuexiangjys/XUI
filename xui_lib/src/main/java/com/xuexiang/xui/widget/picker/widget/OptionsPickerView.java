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

package com.xuexiang.xui.widget.picker.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.xuexiang.xui.R;
import com.xuexiang.xui.XUI;
import com.xuexiang.xui.widget.picker.widget.configure.PickerOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.github.inflationx.calligraphy3.HasTypeface;

/**
 * 条件选择器
 *
 * @author xuexiang
 * @since 2019/1/1 下午7:09
 */
public class OptionsPickerView<T> extends BasePickerView implements View.OnClickListener, HasTypeface {

    private WheelOptions<T> wheelOptions;

    private static final String TAG_SUBMIT = "submit";
    private static final String TAG_CANCEL = "cancel";


    public OptionsPickerView(PickerOptions pickerOptions) {
        super(pickerOptions.context);
        mPickerOptions = pickerOptions;
        initView(pickerOptions.context);
    }

    private void initView(Context context) {
        setDialogOutSideCancelable();
        initViews();
        initAnim();
        initEvents();
        if (mPickerOptions.customListener == null) {
            if (isDialog()) {
                LayoutInflater.from(context).inflate(R.layout.xui_layout_picker_view_options_dialog, contentContainer);
            } else {
                LayoutInflater.from(context).inflate(R.layout.xui_layout_picker_view_options, contentContainer);
            }

            //顶部标题
            TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
            LinearLayout llContent = (LinearLayout) findViewById(R.id.ll_content);

            //确定和取消按钮
            Button btnSubmit = (Button) findViewById(R.id.btnSubmit);
            Button btnCancel = (Button) findViewById(R.id.btnCancel);

            btnSubmit.setTag(TAG_SUBMIT);
            btnCancel.setTag(TAG_CANCEL);
            btnSubmit.setOnClickListener(this);
            btnCancel.setOnClickListener(this);

            //设置文字
            btnSubmit.setText(TextUtils.isEmpty(mPickerOptions.textContentConfirm) ? context.getResources().getString(R.string.xui_picker_view_submit) : mPickerOptions.textContentConfirm);
            btnCancel.setText(TextUtils.isEmpty(mPickerOptions.textContentCancel) ? context.getResources().getString(R.string.xui_picker_view_cancel) : mPickerOptions.textContentCancel);
            tvTitle.setText(TextUtils.isEmpty(mPickerOptions.textContentTitle) ? "" : mPickerOptions.textContentTitle);//默认为空

            //设置color
            btnSubmit.setTextColor(mPickerOptions.textColorConfirm);
            btnCancel.setTextColor(mPickerOptions.textColorCancel);
            tvTitle.setTextColor(mPickerOptions.textColorTitle);
            if (isDialog()) {
                if (TextUtils.isEmpty(tvTitle.getText().toString())) {
                    tvTitle.setVisibility(View.GONE);
                }
            }
            llContent.setBackgroundColor(mPickerOptions.bgColorTitle);

            //设置文字大小
            btnSubmit.setTextSize(mPickerOptions.textSizeSubmitCancel);
            btnCancel.setTextSize(mPickerOptions.textSizeSubmitCancel);
            tvTitle.setTextSize(mPickerOptions.textSizeTitle);
        } else {
            mPickerOptions.customListener.customLayout(LayoutInflater.from(context).inflate(mPickerOptions.layoutRes, contentContainer));
        }

        // ----滚轮布局
        final LinearLayout optionsPicker = (LinearLayout) findViewById(R.id.options_picker);
        optionsPicker.setBackgroundColor(mPickerOptions.bgColorWheel);

        wheelOptions = new WheelOptions<>(optionsPicker, mPickerOptions.isRestoreItem);
        if (mPickerOptions.optionsSelectChangeListener != null) {
            wheelOptions.setOptionsSelectChangeListener(mPickerOptions.optionsSelectChangeListener);
        }

        wheelOptions.setTextContentSize(mPickerOptions.textSizeContent);
        wheelOptions.setLabels(mPickerOptions.label1, mPickerOptions.label2, mPickerOptions.label3);
        wheelOptions.setTextXOffset(mPickerOptions.xOffsetOne, mPickerOptions.xOffsetTwo, mPickerOptions.xOffsetThree);
        wheelOptions.setCyclic(mPickerOptions.cyclic1, mPickerOptions.cyclic2, mPickerOptions.cyclic3);
        if (XUI.getDefaultTypeface() == null) {
            wheelOptions.setTypeface(mPickerOptions.font);
        }
        setOutSideCancelable(mPickerOptions.cancelable);

        wheelOptions.setDividerColor(mPickerOptions.dividerColor);
        wheelOptions.setDividerType(mPickerOptions.dividerType);
        wheelOptions.setLineSpacingMultiplier(mPickerOptions.lineSpacingMultiplier);
        wheelOptions.setTextColorOut(mPickerOptions.textColorOut);
        wheelOptions.setTextColorCenter(mPickerOptions.textColorCenter);
        wheelOptions.isCenterLabel(mPickerOptions.isCenterLabel);
    }

    /**
     * 动态设置标题
     *
     * @param text 标题文本内容
     */
    public void setTitleText(String text) {
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        if (tvTitle != null) {
            tvTitle.setText(text);
        }
    }

    /**
     * 设置默认选中项
     *
     * @param option1
     */
    public void setSelectOptions(int option1) {
        mPickerOptions.option1 = option1;
        reSetCurrentItems();
    }


    public void setSelectOptions(int option1, int option2) {
        mPickerOptions.option1 = option1;
        mPickerOptions.option2 = option2;
        reSetCurrentItems();
    }

    public void setSelectOptions(int option1, int option2, int option3) {
        mPickerOptions.option1 = option1;
        mPickerOptions.option2 = option2;
        mPickerOptions.option3 = option3;
        reSetCurrentItems();
    }

    private void reSetCurrentItems() {
        if (wheelOptions != null) {
            wheelOptions.setCurrentItems(mPickerOptions.option1, mPickerOptions.option2, mPickerOptions.option3);
        }
    }

    //=======================================联动===========================================//

    public void setPicker(List<T> optionsItems) {
        this.setPicker(optionsItems, null, null);
    }

    public void setPicker(@NonNull T[] optionsArray) {
        this.setPicker(Arrays.asList(optionsArray), null, null);
    }

    public void setPicker(List<T> options1Items, List<List<T>> options2Items) {
        this.setPicker(options1Items, options2Items, null);
    }

    public void setPicker(T[] options1Array,
                          T[][] options2Array) {
        List<List<T>> options2Items = new ArrayList<>();
        for (T[] ts : options2Array) {
            options2Items.add(Arrays.asList(ts));
        }
        wheelOptions.setPicker(Arrays.asList(options1Array), options2Items, null);
        reSetCurrentItems();
    }

    public void setPicker(List<T> options1Items,
                          List<List<T>> options2Items,
                          List<List<List<T>>> options3Items) {
        wheelOptions.setPicker(options1Items, options2Items, options3Items);
        reSetCurrentItems();
    }

    public void setPicker(T[] options1Array,
                          T[][] options2Array,
                          T[][][] options3Array) {
        List<List<T>> options2Items = new ArrayList<>();
        for (T[] ts : options2Array) {
            options2Items.add(Arrays.asList(ts));
        }
        List<List<List<T>>> options3Items = new ArrayList<>();
        for (T[][] ts2 : options3Array) {
            List<List<T>> temp = new ArrayList<>();
            for (T[] ts1 : ts2) {
                temp.add(Arrays.asList(ts1));
            }
            options3Items.add(temp);
        }
        wheelOptions.setPicker(Arrays.asList(options1Array), options2Items, options3Items);
        reSetCurrentItems();
    }

    //=======================================不联动===========================================//

    //不联动情况下调用
    public void setNPicker(@NonNull T[] options1Array,
                           @NonNull T[] options2Array) {
        setNPicker(Arrays.asList(options1Array), Arrays.asList(options2Array));
    }

    //不联动情况下调用
    public void setNPicker(List<T> options1Items,
                           List<T> options2Items) {
        wheelOptions.setLinkage(false);
        wheelOptions.setNPicker(options1Items, options2Items, null);
        reSetCurrentItems();
    }

    //不联动情况下调用
    public void setNPicker(@NonNull T[] options1Array,
                           @NonNull T[] options2Array,
                           @NonNull T[] options3Array) {
        setNPicker(Arrays.asList(options1Array), Arrays.asList(options2Array), Arrays.asList(options3Array));
    }

    //不联动情况下调用
    public void setNPicker(List<T> options1Items,
                           List<T> options2Items,
                           List<T> options3Items) {
        wheelOptions.setLinkage(false);
        wheelOptions.setNPicker(options1Items, options2Items, options3Items);
        reSetCurrentItems();
    }

    @Override
    public void onClick(View v) {
        String tag = (String) v.getTag();
        if (tag.equals(TAG_SUBMIT)) {
            if (!returnData()) {
                dismiss();
            }
        } else {
            dismiss();
        }
    }

    /**
     * 抽离接口回调的方法
     *
     * @return true：拦截，不消失；false：不拦截，消失
     */
    public boolean returnData() {
        if (mPickerOptions.optionsSelectListener != null) {
            int[] optionsCurrentItems = wheelOptions.getCurrentItems();
            return mPickerOptions.optionsSelectListener.onOptionsSelect(clickView, optionsCurrentItems[0], optionsCurrentItems[1], optionsCurrentItems[2]);
        }
        return false;
    }

    @Override
    public boolean isDialog() {
        return mPickerOptions.isDialog;
    }

    @Override
    public void setTypeface(Typeface typeface) {
        if (wheelOptions != null) {
            wheelOptions.setTypeface(typeface);
        }
    }
}
