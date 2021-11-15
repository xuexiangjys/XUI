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

package com.xuexiang.xuidemo.fragment.expands.calendar;

import android.view.LayoutInflater;
import android.widget.ListView;
import android.widget.TextView;

import com.codbking.calendar.CalendarDate;
import com.codbking.calendar.CalendarDateView;
import com.codbking.calendar.ChinaDateUtils;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xuidemo.DemoDataProvider;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.adapter.SimpleListViewAdapter;
import com.xuexiang.xuidemo.base.BaseFragment;

import java.util.Date;

import butterknife.BindView;

/**
 * @author xuexiang
 * @since 2019-05-29 23:12
 */
@Page(name = "农历日历")
public class ChineseCalendarFragment extends BaseFragment {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.calendarDateView)
    CalendarDateView calendarDateView;
    @BindView(R.id.list)
    ListView listView;

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chinese_calendar;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        calendarDateView.setAdapter((convertView, parentView, calendarDate) -> {

            if (convertView == null) {
                convertView = LayoutInflater.from(parentView.getContext()).inflate(R.layout.adapter_calendar_chinese, null);
            }

            TextView chinaText = convertView.findViewById(R.id.chinaText);
            TextView text = convertView.findViewById(R.id.text);
            text.setText(String.valueOf(calendarDate.day));

            if (calendarDate.monthFlag != 0) {
                text.setTextColor(0xFF9299A1);
            } else {
                text.setTextColor(0xFF444444);
            }
            chinaText.setText(calendarDate.chinaDay);

            return convertView;
        });

        calendarDateView.setOnCalendarSelectedListener((view, position, date) -> tvTitle.setText(ChinaDateUtils.oneDay(date.year, date.month, date.day)));

        calendarDateView.setOnMonthChangedListener((view, position, date) -> tvTitle.setText(ChinaDateUtils.oneDay(date.year, date.month, date.day)));

        CalendarDate data = CalendarDate.get(new Date());
        tvTitle.setText(ChinaDateUtils.oneDay(data.year, data.month, data.day));


        listView.setAdapter(new SimpleListViewAdapter(getContext(), DemoDataProvider.getDemoData()));
    }


}
