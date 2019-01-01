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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.picker.widget.TimePickerView;
import com.xuexiang.xui.widget.picker.widget.builder.TimePickerBuilder;
import com.xuexiang.xui.widget.picker.widget.configure.TimePickerType;
import com.xuexiang.xui.widget.picker.widget.listener.OnTimeSelectChangeListener;
import com.xuexiang.xui.widget.picker.widget.listener.OnTimeSelectListener;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xutil.data.DateUtils;
import com.xuexiang.xutil.tip.ToastUtils;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author xuexiang
 * @since 2019/1/1 下午8:38
 */
@Page(name = "TimePicker\n时间选择器")
public class TimePickerFragment extends BaseFragment {

    @BindView(R.id.btn_date_bottom)
    Button btnDateBottom;
    @BindView(R.id.btn_time_dialog)
    Button btnTimeDialog;
    @BindView(R.id.btn_date_system)
    Button btnDateSystem;
    @BindView(R.id.btn_time_system)
    Button btnTimeSystem;

    private TimePickerView mDatePicker;

    private TimePickerView mTimePicker;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_time_picker;
    }

    @Override
    protected void initViews() {
        initDatePicker();
        initTimePickerDialog();
    }

    @OnClick({R.id.btn_date_bottom, R.id.btn_time_dialog, R.id.btn_date_system, R.id.btn_time_system})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_date_bottom:
                mDatePicker.show();
                break;
            case R.id.btn_time_dialog:
                mTimePicker.show();
                break;
            case R.id.btn_date_system:
                showDatePickerDialog(getContext(), DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT, (TextView) view, Calendar.getInstance());
                break;
            case R.id.btn_time_system:
                showTimePickerDialog(getContext(), DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT, (TextView) view, Calendar.getInstance());
                break;

        }
    }

    private void initDatePicker() {
        mDatePicker = new TimePickerBuilder(getContext(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelected(Date date, View v) {
                ToastUtils.toast(DateUtils.date2String(date, DateUtils.yyyyMMdd.get()));
            }
        })
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {
                        Log.i("pvTime", "onTimeSelectChanged");
                    }
                })
                .setTitleText("日期选择")
                .build();
    }

    private void initTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtils.string2Date("2013-07-08 12:32:46", DateUtils.yyyyMMddHHmmss.get()));
        mTimePicker = new TimePickerBuilder(getContext(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelected(Date date, View v) {
                ToastUtils.toast(DateUtils.date2String(date, DateUtils.yyyyMMddHHmmss.get()));
            }
        })
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {
                        Log.i("pvTime", "onTimeSelectChanged");
                    }
                })
                .setType(TimePickerType.ALL)
                .setTitleText("时间选择")
                .isDialog(true)
                .setOutSideCancelable(false)
                .setDate(calendar)
                .build();
    }


    public void showDatePickerDialog(Context context, int themeResId, final TextView tv, Calendar calendar) {
        new DatePickerDialog(context
                , themeResId
                , new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                tv.setText(String.format("您选择了：%d年%d月%d日", year, (monthOfYear + 1), dayOfMonth));
            }
        }
                // 设置初始日期
                , calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    public void showTimePickerDialog(Context context, int themeResId, final TextView tv, Calendar calendar) {
        new TimePickerDialog(context
                , themeResId
                , new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                tv.setText(String.format("您选择了：%d时%d分", hourOfDay, minute));
            }
        }
                // 设置初始日期
                , calendar.get(Calendar.HOUR_OF_DAY)
                , calendar.get(Calendar.MINUTE)
                , true)
                .show();
    }
}
