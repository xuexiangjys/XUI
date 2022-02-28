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

package com.xuexiang.xuidemo.fragment.components.pickerview;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.picker.widget.OptionsPickerView;
import com.xuexiang.xui.widget.picker.widget.TimePickerView;
import com.xuexiang.xui.widget.picker.widget.builder.OptionsPickerBuilder;
import com.xuexiang.xui.widget.picker.widget.builder.TimePickerBuilder;
import com.xuexiang.xui.widget.picker.widget.configure.TimePickerType;
import com.xuexiang.xuidemo.DemoDataProvider;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xuidemo.utils.XToastUtils;
import com.xuexiang.xutil.data.DateUtils;

import java.util.Calendar;

import butterknife.OnClick;

/**
 * @author xuexiang
 * @since 2019/1/1 下午8:38
 */
@Page(name = "TimePicker\n时间选择器")
public class TimePickerFragment extends BaseFragment {

    private TimePickerView mDatePicker;

    private TimePickerView mTimePicker;
    private TimePickerView mTimePickerDialog;

    private String[] mTimeOption;
    private String[][] mTimeOption1;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_time_picker;
    }

    @Override
    protected void initViews() {

    }

    @OnClick({R.id.btn_date_bottom, R.id.btn_time_picker, R.id.btn_time_picker_dialog, R.id.btn_date_system, R.id.btn_time_system, R.id.btn_time_period, R.id.btn_time_period_1, R.id.btn_time_period_2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_date_bottom:
                showDatePicker();
                break;
            case R.id.btn_time_picker:
                showTimePicker();
                break;
            case R.id.btn_time_picker_dialog:
                showTimePickerDialog();
                break;
            case R.id.btn_date_system:
                showDatePickerDialog(getContext(), DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT, (TextView) view, Calendar.getInstance());
                break;
            case R.id.btn_time_system:
                showTimePickerDialog(getContext(), DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT, (TextView) view, Calendar.getInstance());
                break;
            case R.id.btn_time_period:
                showTimePeriodPicker();
                break;
            case R.id.btn_time_period_1:
                showTimePeriodPicker1();
                break;
            case R.id.btn_time_period_2:
                showTimePeriodPicker2();
                break;
            default:
                break;
        }
    }

    private void showDatePicker() {
        if (mDatePicker == null) {
            mDatePicker = new TimePickerBuilder(getContext(), (date, v) -> XToastUtils.toast(DateUtils.date2String(date, DateUtils.yyyyMMdd.get())))
                    .setTimeSelectChangeListener(date -> Log.i("pvTime", "onTimeSelectChanged"))
                    .setTitleText("日期选择")
                    .build();
//            // 这样设置日月年显示
//            mDatePicker.getWheelTime().getView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        mDatePicker.show();
    }

    private void showTimePicker() {
        if (mTimePicker == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(DateUtils.getNowDate());
            mTimePicker = new TimePickerBuilder(getContext(), (date, v) -> XToastUtils.toast(DateUtils.date2String(date, DateUtils.HHmmss.get())))
                    .setTimeSelectChangeListener(date -> Log.i("pvTime", "onTimeSelectChanged"))
                    .setType(TimePickerType.TIME)
                    .setTitleText("时间选择")
                    .setDate(calendar)
                    .build();
        }
        mTimePicker.show();
    }

    private void showTimePickerDialog() {
        if (mTimePickerDialog == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(DateUtils.string2Date("2013-07-08 12:32:46", DateUtils.yyyyMMddHHmmss.get()));
            mTimePickerDialog = new TimePickerBuilder(getContext(), (date, v) -> XToastUtils.toast(DateUtils.date2String(date, DateUtils.yyyyMMddHHmmss.get())))
                    .setTimeSelectChangeListener(date -> Log.i("pvTime", "onTimeSelectChanged"))
                    .setType(TimePickerType.ALL)
                    .setTitleText("时间选择")
                    .isDialog(true)
                    .setOutSideCancelable(false)
                    .setDate(calendar)
                    .build();
        }
        mTimePickerDialog.show();
    }

    private void showTimePeriodPicker() {
        if (mTimeOption == null) {
            mTimeOption = DemoDataProvider.getTimePeriod(15);
        }

        //8点
        int defaultOption = 8 * 60 / 15;
        OptionsPickerView pvOptions = new OptionsPickerBuilder(getContext(), (v, options1, options2, options3) -> {
            XToastUtils.toast(mTimeOption[options1]);
            return false;
        })
                .setTitleText("时间点选择")
                .setSelectOptions(defaultOption)
                .build();
        pvOptions.setPicker(mTimeOption);
        pvOptions.show();
    }

    private void showTimePeriodPicker1() {
        if (mTimeOption1 == null) {
            mTimeOption1 = new String[5][];
            mTimeOption1[0] = DemoDataProvider.getTimePeriod(0, 6, 15);
            mTimeOption1[1] = DemoDataProvider.getTimePeriod(6, 6, 15);
            mTimeOption1[2] = DemoDataProvider.getTimePeriod(12, 1, 15);
            mTimeOption1[3] = DemoDataProvider.getTimePeriod(1, 5, 15);
            mTimeOption1[4] = DemoDataProvider.getTimePeriod(6, 6, 15);
        }
        String[] option = new String[]{"凌晨", "上午", "中午", "下午", "晚上"};
        //8点
        int defaultOption = 2 * 60 / 15;
        OptionsPickerView pvOptions = new OptionsPickerBuilder(getContext(), (v, options1, options2, options3) -> {
            XToastUtils.toast(option[options1] + mTimeOption1[options1][options2]);
            return false;
        })
                .setTitleText("时间点选择")
                .isRestoreItem(true)
                .setSelectOptions(1, defaultOption)
                .build();
        pvOptions.setPicker(option, mTimeOption1);
        pvOptions.show();
    }

    private void showTimePeriodPicker2() {
        if (mTimeOption == null) {
            mTimeOption = DemoDataProvider.getTimePeriod(15);
        }

        //8点
        int defaultOption = 8 * 60 / 15;

        OptionsPickerView pvOptions = new OptionsPickerBuilder(getContext(), (v, options1, options2, options3) -> {
            if (options1 > options2) {
                XToastUtils.error("结束时间不能早于开始时间");
                //返回为true意为拦截，选择框不消失。
                return true;
            } else {
                XToastUtils.toast(mTimeOption[options1] + "~" + mTimeOption[options2]);
                return false;
            }
        })
                .setTitleText("时间段选择")
                .setSelectOptions(defaultOption, defaultOption)
                .build();
        pvOptions.setNPicker(mTimeOption, mTimeOption);
        pvOptions.show();

    }

    //==============系统=================//

    public void showDatePickerDialog(Context context, int themeResId, final TextView tv, Calendar calendar) {
        new DatePickerDialog(context
                , themeResId
                , (view, year, monthOfYear, dayOfMonth) -> tv.setText(String.format("您选择了：%d年%d月%d日", year, (monthOfYear + 1), dayOfMonth))
                // 设置初始日期
                , calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    public void showTimePickerDialog(Context context, int themeResId, final TextView tv, Calendar calendar) {
        new TimePickerDialog(context
                , themeResId
                , (view, hourOfDay, minute) -> tv.setText(String.format("您选择了：%d时%d分", hourOfDay, minute))
                // 设置初始日期
                , calendar.get(Calendar.HOUR_OF_DAY)
                , calendar.get(Calendar.MINUTE)
                , false)
                .show();
    }
}
