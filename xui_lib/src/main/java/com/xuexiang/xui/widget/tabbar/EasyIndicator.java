/*
 * Copyright (C) 2018 xuexiangjys(xuexiangjys@163.com)
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

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.PagerAdapter;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xuexiang.xui.R;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.ThemeUtils;

/**
 * 简单的索引器
 *
 * @author xuexiang
 * @since 2018/12/20 上午12:32
 */
public class EasyIndicator extends LinearLayout implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private View mIndicator;
    private int mPosition;
    private ViewPager mViewPager;
    /**
     * 选项卡点击监听
     */
    public onTabClickListener mOnTabClickListener;

    private LinearLayout tab_content;
    /**
     * Tab集合
     */
    private TextView[] tvs;
    public int screenWidth;
    public int screenHeight;
    /**
     * 当前显示的页卡编号
     */
    private int mCurrIndex;
    /**
     * tab默认高度
     */
    private int indicatorHeight = 45;
    /**
     * tab宽度,默认填充全屏
     */
    private int indicator_width = -1;
    //==============//
    /**
     * 是否显示指示器
     */
    private boolean indicator_line_show = true;
    /**
     * 指示器默认高度
     */
    private int indicator_line_height = 3;
    /**
     * 指示器颜色
     */
    private int indicator_line_color;
    //==============//
    /**
     * 指示器底部线条高度
     */
    private int indicator_bottom_line_height = 0;
    /**
     * 指示器底部线条颜色
     */
    private int indicator_bottom_line_color;

    /**
     * 中间分割线宽度
     */
    private int indicator_vertical_line_w = 0;
    /**
     * 中间分割线高度
     */
    private int indicator_vertical_line_h = 0;
    /**
     * 中间分割线颜色
     */
    private int indicator_vertical_line_color;


    /**
     * 选中颜色和默认颜色
     */
    private int indicator_selected_color, indicator_normal_color;
    /**
     * 默认字体大小
     */
    private float indicator_textSize = 14;
    /**
     * 选中字体大小
     */
    private float indicator_select_textSize = indicator_textSize;

    public EasyIndicator(Context context) {
        this(context, null);
    }

    public EasyIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.EasyIndicatorStyle);
    }

    public EasyIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        initScreenWidth();
        init(context, attrs, defStyleAttr);
    }

    /**
     * 初始化View
     */
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EasyIndicator, defStyleAttr, 0);
        if (a != null) {
            indicatorHeight = (int) getDimensionPixelSize(a, R.styleable.EasyIndicator_indicator_height, indicatorHeight);
            indicator_line_height = (int) getDimensionPixelSize(a, R.styleable.EasyIndicator_indicator_line_height, indicator_line_height);
            indicator_bottom_line_height = (int) getDimensionPixelSize(a, R.styleable.EasyIndicator_indicator_bottom_line_height, indicator_bottom_line_height);
            indicator_bottom_line_color = a.getColor(R.styleable.EasyIndicator_indicator_bottom_line_color, ThemeUtils.resolveColor(getContext(), R.attr.xui_config_color_separator_dark));
            indicator_selected_color = a.getColor(R.styleable.EasyIndicator_indicator_selected_color, ThemeUtils.resolveColor(getContext(), R.attr.colorAccent));
            indicator_normal_color = a.getColor(R.styleable.EasyIndicator_indicator_normal_color, ResUtils.getColor(R.color.xui_config_color_black));
            indicator_line_color = a.getColor(R.styleable.EasyIndicator_indicator_line_color, ThemeUtils.resolveColor(getContext(), R.attr.colorAccent));
            indicator_textSize = getDimensionPixelSize(a, R.styleable.EasyIndicator_indicator_textSize, (int) indicator_textSize);
            indicator_line_show = a.getBoolean(R.styleable.EasyIndicator_indicator_line_show, indicator_line_show);
            indicator_vertical_line_w = (int) getDimensionPixelSize(a, R.styleable.EasyIndicator_indicator_vertical_line_w, indicator_vertical_line_w);
            indicator_vertical_line_color = a.getColor(R.styleable.EasyIndicator_indicator_vertical_line_color, ThemeUtils.resolveColor(getContext(), R.attr.xui_config_color_separator_dark));
            indicator_vertical_line_h = (int) getDimensionPixelSize(a, R.styleable.EasyIndicator_indicator_vertical_line_h, indicator_vertical_line_h);
            indicator_width = (int) getDimensionPixelSize(a, R.styleable.EasyIndicator_indicator_width, indicator_width);
            indicator_select_textSize = getDimensionPixelSize(a, R.styleable.EasyIndicator_indicator_select_textSize, 14);
            if (indicator_width == 0) {
                indicator_width = -1;
            }
            a.recycle();
        }

        tab_content = new LinearLayout(getContext());
        LayoutParams params = new LayoutParams(indicator_width, LayoutParams.WRAP_CONTENT);
        tab_content.setBackgroundColor(Color.WHITE);
        params.gravity = Gravity.CENTER;
        tab_content.setLayoutParams(params);
    }

    /**
     * 设置tab item
     *
     * @param tabTitles
     */
    public void setTabTitles(String[] tabTitles) {
        // Create tab
        tvs = new TextView[tabTitles.length];
        tab_content.removeAllViews();
        TextView view;
        for (int i = 0; i < tabTitles.length; i++) {
            view = new TextView(getContext());
            view.setTag(i);
            view.setText(tabTitles[i]);

            view.setGravity(Gravity.CENTER);
            LayoutParams lp = new LayoutParams(0, indicatorHeight, 1.0f);
            view.setLayoutParams(lp);
            switch (i) {
                case 0:
                    view.setTextColor(indicator_selected_color);
                    view.setTextSize(TypedValue.COMPLEX_UNIT_PX, indicator_select_textSize);
                    break;
                default:
                    view.setTextColor(indicator_normal_color);
                    view.setTextSize(TypedValue.COMPLEX_UNIT_PX, indicator_textSize);
                    break;
            }
            view.setOnClickListener(this);
            tvs[i] = view;
            tab_content.addView(view);
            if (i != tabTitles.length - 1) {
                View line = new View(getContext());
                line.setBackgroundColor(indicator_vertical_line_color);
                LinearLayoutCompat.LayoutParams compat = new LinearLayoutCompat.LayoutParams(indicator_vertical_line_w, indicator_vertical_line_h);
                line.setLayoutParams(compat);
                tab_content.addView(line);
            }
        }

        removeAllViews();
        addView(tab_content);

        if (indicator_line_show) {
            // Create tab mIndicator
            mIndicator = new View(getContext());
            int iw = 0;
            if (indicator_width == 0 || indicator_width == -1) {
                iw = screenWidth / tvs.length;
            }
            mIndicator.setLayoutParams(new LinearLayoutCompat.LayoutParams(iw, indicator_line_height));
            mIndicator.setBackgroundColor(indicator_line_color);
            addView(mIndicator);
        }

        // Create tab bottom line
        View line = new View(getContext());
        LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, indicator_bottom_line_height);
        line.setLayoutParams(params);
        line.setBackgroundColor(indicator_bottom_line_color);
        addView(line);
    }

    /**
     * 内部设置
     *
     * @param adapter
     */
    public void setViewPager(PagerAdapter adapter) {
        mViewPager = new ViewPager(getContext());
        mViewPager.setId(R.id.view_pager);
        mViewPager.setLayoutParams(new LinearLayoutCompat.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(0);
        mViewPager.addOnPageChangeListener(this);
        addView(mViewPager);
    }

    /**
     * 外部设置ViewPage自定义
     *
     * @param viewPage
     * @param adapter
     */
    public void setViewPager(ViewPager viewPage, PagerAdapter adapter) {
        mViewPager = viewPage;
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(0);
        mViewPager.addOnPageChangeListener(this);
    }

    private AnimatorSet buildIndicatorAnimatorTowards(TextView tv) {
        float x = tab_content.getX();
        ObjectAnimator xAnimator = ObjectAnimator.ofFloat(mIndicator, "X", mIndicator.getX(), tv.getX() + x);
        final ViewGroup.LayoutParams params = mIndicator.getLayoutParams();
        ValueAnimator widthAnimator = ValueAnimator.ofInt(params.width, tv.getMeasuredWidth());
        widthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                params.width = (Integer) animation.getAnimatedValue();
                mIndicator.setLayoutParams(params);
            }
        });
        AnimatorSet set = new AnimatorSet();
        set.setInterpolator(new FastOutSlowInInterpolator());
        set.playTogether(xAnimator, widthAnimator);
        return set;
    }


    @Override
    public void onClick(View v) {
        TextView tv = (TextView) v;
        mPosition = (int) v.getTag();
        if (mViewPager != null) {
            mViewPager.setCurrentItem(mPosition);
        } else {
            setSelectorColor(tv);
            if (indicator_line_show) {
                buildIndicatorAnimatorTowards(tv).start();
            }
        }
        if (mOnTabClickListener != null) {
            mOnTabClickListener.onTabClick(((TextView) v).getText().toString(), mPosition);
        }
    }

    private void setSelectorColor(TextView tv) {
        for (int i = 0; i < tvs.length; i++) {
            tvs[i].setTextColor(indicator_normal_color);
            tvs[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, indicator_textSize);
        }
        tv.setTextColor(indicator_selected_color);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, indicator_select_textSize);
    }

    /**
     * 设置选项卡点击监听
     *
     * @param onTabClickListener
     */
    public void setOnTabClickListener(EasyIndicator.onTabClickListener onTabClickListener) {
        mOnTabClickListener = onTabClickListener;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        LayoutParams ll = (LayoutParams) mIndicator
                .getLayoutParams();
        if (mCurrIndex == position) {
            ll.leftMargin = (int) (mCurrIndex * mIndicator.getMeasuredWidth() + positionOffset
                    * mIndicator.getMeasuredWidth());
        } else if (mCurrIndex > position) {
            ll.leftMargin = (int) (mCurrIndex * mIndicator.getMeasuredWidth() - (1 - positionOffset)
                    * mIndicator.getMeasuredWidth());
        }

        mIndicator.setLayoutParams(ll);
    }

    @Override
    public void onPageSelected(int position) {
        mCurrIndex = position;
        setSelectorColor(tvs[position]);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    /**
     * 选项卡点击监听
     */
    public interface onTabClickListener {
        /**
         * tab点击
         *
         * @param title
         * @param position
         */
        void onTabClick(String title, int position);
    }


    /**
     * get the dimension pixel size from typeArray which is defined in attr
     *
     * @param typeArray   TypedArray
     * @param styleable   styleable
     * @param defaultSize defaultSize
     * @return the dimension pixel size
     */
    private float getDimensionPixelSize(TypedArray typeArray, int styleable, int defaultSize) {
        int sizeStyleable = typeArray.getResourceId(styleable, 0);
        return sizeStyleable > 0 ? typeArray.getResources().getDimensionPixelSize(sizeStyleable) : typeArray.getDimensionPixelSize(styleable, (int) getPixelSizeBySp(defaultSize));
    }

    /**
     * get Pixel size by sp
     *
     * @param sp sp
     * @return the value of px
     */
    private float getPixelSizeBySp(int sp) {
        Resources res = this.getResources();
        final float scale = res.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }


    /**
     * 初始化屏幕宽高
     */
    private void initScreenWidth() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenHeight = dm.heightPixels;
        screenWidth = dm.widthPixels;

    }

}