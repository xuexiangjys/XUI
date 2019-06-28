/*
 * Copyright (C) 2019 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.xuexiang.xuidemo.widget.calendar;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.WeekBar;
import com.xuexiang.xuidemo.R;

/**
 * 自定义英文栏
 * Created by huanghaibin on 2017/11/30.
 */
public class CustomWeekBar extends WeekBar {
    private int mPreSelectedIndex;

    public CustomWeekBar(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.layout_custom_week_bar, this, true);
        setBackgroundColor(Color.WHITE);
    }

    @Override
    protected void onDateSelected(Calendar calendar, int weekStart, boolean isClick) {
        getChildAt(mPreSelectedIndex).setSelected(false);
        int viewIndex = getViewIndexByCalendar(calendar, weekStart);
        getChildAt(viewIndex).setSelected(true);
        mPreSelectedIndex = viewIndex;
    }

    /**
     * 当周起始发生变化，使用自定义布局需要重写这个方法，避免出问题
     *
     * @param weekStart 周起始
     */
    @Override
    protected void onWeekStartChange(int weekStart) {
        for (int i = 0; i < getChildCount(); i++) {
            ((TextView) getChildAt(i)).setText(getWeekString(i, weekStart));
        }
    }

    /**
     * 或者周文本，这个方法仅供父类使用
     *
     * @param index     index
     * @param weekStart weekStart
     * @return 或者周文本
     */
    private String getWeekString(int index, int weekStart) {
        String[] weeks = getContext().getResources().getStringArray(R.array.chinese_week_string_array);

        if (weekStart == 1) {
            return weeks[index];
        }
        if (weekStart == 2) {
            return weeks[index == 6 ? 0 : index + 1];
        }
        return weeks[index == 0 ? 6 : index - 1];
    }
}
