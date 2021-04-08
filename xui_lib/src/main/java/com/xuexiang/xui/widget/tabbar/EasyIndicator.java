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
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.xuexiang.xui.R;
import com.xuexiang.xui.XUI;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.ThemeUtils;

import io.github.inflationx.calligraphy3.HasTypeface;

/**
 * 简单的索引器
 *
 * @author xuexiang
 * @since 2018/12/20 上午12:32
 */
public class EasyIndicator extends LinearLayout implements View.OnClickListener, ViewPager.OnPageChangeListener, HasTypeface {
    private LinearLayout mIndicatorContainer;
    private ViewPager mViewPager;
    /**
     * 选项卡点击监听
     */
    public OnTabClickListener mOnTabClickListener;

    private LinearLayout tabContent;
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
    private int indicatorWidth = LayoutParams.MATCH_PARENT;
    //==============//
    /**
     * 是否显示指示器
     */
    private boolean indicatorLineShow = true;
    /**
     * 指示器默认高度
     */
    private int indicatorLineHeight = 3;
    /**
     * 指示器默认宽度
     */
    private int indicatorLineWidth = LayoutParams.MATCH_PARENT;
    /**
     * 指示器颜色
     */
    private int indicatorLineColor;
    /**
     * 指示器资源
     */
    private Drawable indicatorLineDrawable;
    //==============//
    /**
     * 指示器底部线条高度
     */
    private int indicatorBottomLineHeight = 0;
    /**
     * 指示器底部线条颜色
     */
    private int indicatorBottomLineColor;

    /**
     * 中间分割线宽度
     */
    private int indicatorVerticalLineWidth = 0;
    /**
     * 中间分割线高度
     */
    private int indicatorVerticalLineHeight = 0;
    /**
     * 中间分割线颜色
     */
    private int indicatorVerticalLineColor;

    /**
     * 选中颜色和默认颜色
     */
    private int indicatorSelectedColor, indicatorNormalColor;
    /**
     * 默认字体大小
     */
    private float indicatorTextSize = 14;
    /**
     * 选中字体大小
     */
    private float indicatorSelectTextSize = indicatorTextSize;

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
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.EasyIndicator, defStyleAttr, 0);
        if (typedArray != null) {
            indicatorWidth = (int) getDimensionPixelSize(typedArray, R.styleable.EasyIndicator_indicator_width, indicatorWidth);
            indicatorHeight = (int) getDimensionPixelSize(typedArray, R.styleable.EasyIndicator_indicator_height, indicatorHeight);
            indicatorLineShow = typedArray.getBoolean(R.styleable.EasyIndicator_indicator_line_show, indicatorLineShow);
            indicatorLineWidth = (int) getDimensionPixelSize(typedArray, R.styleable.EasyIndicator_indicator_line_width, indicatorLineWidth);
            indicatorLineHeight = (int) getDimensionPixelSize(typedArray, R.styleable.EasyIndicator_indicator_line_height, indicatorLineHeight);
            indicatorLineColor = typedArray.getColor(R.styleable.EasyIndicator_indicator_line_color, ThemeUtils.resolveColor(getContext(), R.attr.colorAccent));
            indicatorLineDrawable = ResUtils.getDrawableAttrRes(context, typedArray, R.styleable.EasyIndicator_indicator_line_res);
            indicatorBottomLineHeight = (int) getDimensionPixelSize(typedArray, R.styleable.EasyIndicator_indicator_bottom_line_height, indicatorBottomLineHeight);
            indicatorBottomLineColor = typedArray.getColor(R.styleable.EasyIndicator_indicator_bottom_line_color, ThemeUtils.resolveColor(getContext(), R.attr.xui_config_color_separator_dark));
            indicatorSelectedColor = typedArray.getColor(R.styleable.EasyIndicator_indicator_selected_color, ThemeUtils.resolveColor(getContext(), R.attr.colorAccent));
            indicatorNormalColor = typedArray.getColor(R.styleable.EasyIndicator_indicator_normal_color, ResUtils.getColor(R.color.xui_config_color_black));
            indicatorTextSize = getDimensionPixelSize(typedArray, R.styleable.EasyIndicator_indicator_textSize, (int) indicatorTextSize);
            indicatorVerticalLineWidth = (int) getDimensionPixelSize(typedArray, R.styleable.EasyIndicator_indicator_vertical_line_w, indicatorVerticalLineWidth);
            indicatorVerticalLineColor = typedArray.getColor(R.styleable.EasyIndicator_indicator_vertical_line_color, ThemeUtils.resolveColor(getContext(), R.attr.xui_config_color_separator_dark));
            indicatorVerticalLineHeight = (int) getDimensionPixelSize(typedArray, R.styleable.EasyIndicator_indicator_vertical_line_h, indicatorVerticalLineHeight);
            indicatorSelectTextSize = getDimensionPixelSize(typedArray, R.styleable.EasyIndicator_indicator_select_textSize, 14);
            // 指引器不能超过屏幕的宽度
            indicatorWidth = Math.min(indicatorWidth, screenWidth);
            if (indicatorWidth == 0) {
                indicatorWidth = LayoutParams.MATCH_PARENT;
            }
            typedArray.recycle();
        }
        tabContent = new LinearLayout(getContext());
        LayoutParams params = new LayoutParams(indicatorWidth, LayoutParams.WRAP_CONTENT);
        tabContent.setBackgroundColor(Color.WHITE);
        params.gravity = Gravity.CENTER;
        tabContent.setLayoutParams(params);
        tabContent.setGravity(Gravity.CENTER);
    }

    /**
     * 设置tab的标题
     *
     * @param tabTitles tab的标题
     */
    public void setTabTitles(String[] tabTitles) {
        // Create tab
        tvs = new TextView[tabTitles.length];
        tabContent.removeAllViews();
        TextView view;
        for (int i = 0; i < tabTitles.length; i++) {
            view = new TextView(getContext());
            view.setTag(i);
            view.setText(tabTitles[i]);
            view.setTypeface(XUI.getDefaultTypeface());

            view.setGravity(Gravity.CENTER);
            LayoutParams lp = new LayoutParams(0, indicatorHeight, 1.0f);
            view.setLayoutParams(lp);
            if (i == 0) {
                view.setTextColor(indicatorSelectedColor);
                view.setTextSize(TypedValue.COMPLEX_UNIT_PX, indicatorSelectTextSize);
            } else {
                view.setTextColor(indicatorNormalColor);
                view.setTextSize(TypedValue.COMPLEX_UNIT_PX, indicatorTextSize);
            }
            view.setOnClickListener(this);
            tvs[i] = view;
            tabContent.addView(view);
            if (i != tabTitles.length - 1) {
                View line = new View(getContext());
                line.setBackgroundColor(indicatorVerticalLineColor);
                LinearLayoutCompat.LayoutParams compat = new LinearLayoutCompat.LayoutParams(indicatorVerticalLineWidth, indicatorVerticalLineHeight);
                line.setLayoutParams(compat);
                tabContent.addView(line);
            }
        }

        removeAllViews();
        addView(tabContent);

        if (indicatorLineShow) {
            mIndicatorContainer = new LinearLayout(getContext());
            mIndicatorContainer.setGravity(Gravity.CENTER);
            int iw = indicatorWidth > 0 ? indicatorWidth / tvs.length : screenWidth / tvs.length;
            mIndicatorContainer.setLayoutParams(new LinearLayoutCompat.LayoutParams(iw, indicatorLineHeight));
            View indicator = new View(getContext());
            indicator.setLayoutParams(new LinearLayoutCompat.LayoutParams(indicatorLineWidth > 0 ? indicatorLineWidth : LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            if (indicatorLineDrawable != null) {
                indicator.setBackground(indicatorLineDrawable);
            } else {
                indicator.setBackgroundColor(indicatorLineColor);
            }
            mIndicatorContainer.addView(indicator);
            addView(mIndicatorContainer);
            if (indicatorWidth > 0) {
                LayoutParams ll = (LayoutParams) mIndicatorContainer
                        .getLayoutParams();
                ll.setMarginStart((screenWidth - indicatorWidth) / 2);
                mIndicatorContainer.setLayoutParams(ll);
            }
        }

        // Create tab bottom line
        View line = new View(getContext());
        LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, indicatorBottomLineHeight);
        line.setLayoutParams(params);
        line.setBackgroundColor(indicatorBottomLineColor);
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
        float x = tabContent.getX();
        ObjectAnimator xAnimator = ObjectAnimator.ofFloat(mIndicatorContainer, "X", mIndicatorContainer.getX(), tv.getX() + x);
        final ViewGroup.LayoutParams params = mIndicatorContainer.getLayoutParams();
        ValueAnimator widthAnimator = ValueAnimator.ofInt(params.width, tv.getMeasuredWidth());
        widthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                params.width = (Integer) animation.getAnimatedValue();
                mIndicatorContainer.setLayoutParams(params);
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
        int position = (int) v.getTag();
        if (mViewPager != null) {
            mViewPager.setCurrentItem(position);
        } else {
            setSelectorColor(tv);
            if (indicatorLineShow) {
                buildIndicatorAnimatorTowards(tv).start();
            }
        }
        if (mOnTabClickListener != null) {
            mOnTabClickListener.onTabClick(((TextView) v).getText().toString(), position);
        }
    }

    private void setSelectorColor(TextView tv) {
        for (TextView textView : tvs) {
            textView.setTextColor(indicatorNormalColor);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, indicatorTextSize);
        }
        tv.setTextColor(indicatorSelectedColor);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, indicatorSelectTextSize);
    }

    /**
     * 设置选项卡点击监听
     *
     * @param onTabClickListener 选项卡点击监听
     */
    public void setOnTabClickListener(OnTabClickListener onTabClickListener) {
        mOnTabClickListener = onTabClickListener;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        LayoutParams ll = (LayoutParams) mIndicatorContainer
                .getLayoutParams();
        if (mCurrIndex == position) {
            ll.setMarginStart((int) (mCurrIndex * mIndicatorContainer.getMeasuredWidth() + positionOffset
                    * mIndicatorContainer.getMeasuredWidth()));
        } else if (mCurrIndex > position) {
            ll.setMarginStart((int) (mCurrIndex * mIndicatorContainer.getMeasuredWidth() - (1 - positionOffset)
                    * mIndicatorContainer.getMeasuredWidth()));
        }
        mIndicatorContainer.setLayoutParams(ll);
    }

    @Override
    public void onPageSelected(int position) {
        mCurrIndex = position;
        setSelectorColor(tvs[position]);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void setTypeface(Typeface typeface) {
        if (tvs != null) {
            for (TextView tv : tvs) {
                tv.setTypeface(typeface);
            }
        }
    }

    /**
     * 选项卡点击监听
     *
     * @author xuexiang
     * @since 2021/3/14 3:38 PM
     */
    public interface OnTabClickListener {
        /**
         * tab点击
         *
         * @param title    标题
         * @param position 位置
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