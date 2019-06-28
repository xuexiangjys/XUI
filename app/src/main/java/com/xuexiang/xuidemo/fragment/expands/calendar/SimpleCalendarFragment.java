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

import android.graphics.Color;
import android.view.View;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xuidemo.DemoDataProvider;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.adapter.NewsCardViewListAdapter;
import com.xuexiang.xuidemo.adapter.base.BaseRecyclerAdapter;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xuidemo.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * @author xuexiang
 * @since 2019-06-28 11:23
 */
@Page(name = "简单的日历控件\n支持自定义样式")
public class SimpleCalendarFragment extends BaseFragment implements CalendarView.OnCalendarSelectListener {

    @BindView(R.id.calendarView)
    CalendarView calendarView;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.calendarLayout)
    CalendarLayout calendarLayout;

    private NewsCardViewListAdapter mAdapter;

    private TitleBar mTitleBar;

    @Override
    protected TitleBar initTitle() {
        mTitleBar = super.initTitle();
        mTitleBar.addAction(new TitleBar.TextAction("复杂") {
            @SingleClick
            @Override
            public void performAction(View view) {
                openPage(ComplexCalendarFragment.class);
            }
        });
        return mTitleBar;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_simple_calendar;
    }

    @Override
    protected void initViews() {
        mTitleBar.setTitle(calendarView.getCurMonth() + "月" + calendarView.getCurDay() + "日");

        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(mAdapter = new NewsCardViewListAdapter());
        mAdapter.refresh(DemoDataProvider.getDemoNewInfos());

        initCalendarView();
    }

    private void initCalendarView() {
        final int year = calendarView.getCurYear();
        final int month = calendarView.getCurMonth();
        Map<String, Calendar> map = new HashMap<>();
        for (int j = 5; j < 10; j++) {
            map.put(getSchemeCalendar(year, month, j, Color.RED).toString(), getSchemeCalendar(year, month, j, Color.RED));
        }
        for (int i = 10; i < 28; i++) {
            map.put(getSchemeCalendar(year, month, i, Color.TRANSPARENT).toString(), getSchemeCalendar(year, month, i, Color.TRANSPARENT));
        }
        calendarView.setSchemeDate(map);
    }

    public Calendar getSchemeCalendar(int year, int month, int day, int color) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        return calendar;
    }

    @Override
    protected void initListeners() {
        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Utils.goWeb(getContext(), mAdapter.getItem(position).getDetailUrl());
            }
        });

        calendarView.setOnCalendarSelectListener(this);
    }

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }

    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        mTitleBar.setTitle(calendar.getMonth() + "月" + calendar.getDay() + "日");
        if (isClick) {
            if (calendarLayout.isExpand()) {
                calendarLayout.shrink();
            }
        }
    }

}
