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

package com.xuexiang.xui.widget.spinner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.xuexiang.xui.R;
import com.xuexiang.xui.utils.DensityUtils;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.ThemeUtils;

import java.util.Arrays;
import java.util.List;


/**
 * 下拉选择菜单
 *
 * @author xuexiang
 * @since 2019-11-28 12:26
 */
public class DropDownMenu extends LinearLayout {
    /**
     * 顶部菜单布局
     */
    private LinearLayout mTabMenuView;
    /**
     * 底部容器，包含popupMenuViews，maskView
     */
    private FrameLayout mContainerView;
    /**
     * 弹出菜单父布局
     */
    private FrameLayout mPopupMenuViews;
    /**
     * 遮罩半透明View，点击可关闭DropDownMenu
     */
    private View mMaskView;
    /**
     * tabMenuView里面选中的tab位置，-1表示未选中
     */
    private int mCurrentTabPosition = -1;

    private int mContentLayoutId;
    private View mContentView;
    /**
     * 分割线颜色
     */
    private int mDividerColor;
    /**
     * 分割线宽度
     */
    private int mDividerWidth;
    /**
     * 分割线的Margin
     */
    private int mDividerMargin;
    /**
     * tab选中颜色
     */
    private int mMenuTextSelectedColor;
    /**
     * tab未选中颜色
     */
    private int mMenuTextUnselectedColor;
    /**
     * tab字体水平padding
     */
    private int mMenuTextPaddingHorizontal;
    /**
     * tab字体水平padding
     */
    private int mMenuTextPaddingVertical;
    /**
     * 遮罩颜色
     */
    private int mMaskColor;
    /**
     * tab字体大小
     */
    private int mMenuTextSize;
    /**
     * tab选中图标
     */
    private Drawable mMenuSelectedIcon;
    /**
     * tab未选中图标
     */
    private Drawable mMenuUnselectedIcon;
    /**
     * 选择菜单的高度/屏幕高度 占比
     */
    private float mMenuHeightPercent = 0.5F;

    public DropDownMenu(Context context) {
        super(context, null);
    }

    public DropDownMenu(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.DropDownMenuStyle);
    }

    public DropDownMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        //为DropDownMenu添加自定义属性
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.DropDownMenu);
        mContentLayoutId = array.getResourceId(R.styleable.DropDownMenu_ddm_contentLayoutId, -1);
        mDividerColor = array.getColor(R.styleable.DropDownMenu_ddm_dividerColor, ThemeUtils.resolveColor(getContext(), R.attr.xui_config_color_separator_dark));
        mDividerWidth = array.getDimensionPixelSize(R.styleable.DropDownMenu_ddm_dividerWidth, ResUtils.getDimensionPixelSize(R.dimen.default_ddm_divider_width));
        mDividerMargin = array.getDimensionPixelSize(R.styleable.DropDownMenu_ddm_dividerMargin, ResUtils.getDimensionPixelSize(R.dimen.default_ddm_divider_margin));
        int underlineColor = array.getColor(R.styleable.DropDownMenu_ddm_underlineColor, ThemeUtils.resolveColor(getContext(), R.attr.xui_config_color_separator_light));
        int underlineHeight = array.getDimensionPixelSize(R.styleable.DropDownMenu_ddm_underlineHeight, ResUtils.getDimensionPixelSize(R.dimen.default_ddm_underline_height));
        int menuBackgroundColor = array.getColor(R.styleable.DropDownMenu_ddm_menuBackgroundColor, Color.WHITE);
        mMaskColor = array.getColor(R.styleable.DropDownMenu_ddm_maskColor, ResUtils.getColor(R.color.default_ddm_mask_color));
        mMenuTextSelectedColor = array.getColor(R.styleable.DropDownMenu_ddm_menuTextSelectedColor, ThemeUtils.getMainThemeColor(getContext()));
        mMenuTextUnselectedColor = array.getColor(R.styleable.DropDownMenu_ddm_menuTextUnselectedColor, ThemeUtils.resolveColor(getContext(), R.attr.xui_config_color_content_text));
        mMenuTextPaddingHorizontal = array.getDimensionPixelSize(R.styleable.DropDownMenu_ddm_menuTextPaddingHorizontal, ResUtils.getDimensionPixelSize(R.dimen.default_ddm_menu_text_padding_horizontal));
        mMenuTextPaddingVertical = array.getDimensionPixelSize(R.styleable.DropDownMenu_ddm_menuTextPaddingVertical, ResUtils.getDimensionPixelSize(R.dimen.default_ddm_menu_text_padding_vertical));
        mMenuTextSize = array.getDimensionPixelSize(R.styleable.DropDownMenu_ddm_menuTextSize, ResUtils.getDimensionPixelSize(R.dimen.default_ddm_menu_text_size));
        mMenuUnselectedIcon = ResUtils.getDrawableAttrRes(getContext(), array, R.styleable.DropDownMenu_ddm_menuUnselectedIcon);
        if (mMenuUnselectedIcon == null) {
            mMenuUnselectedIcon = ResUtils.getVectorDrawable(getContext(), R.drawable.ddm_ic_arrow_down);
        }
        mMenuSelectedIcon = ResUtils.getDrawableAttrRes(getContext(), array, R.styleable.DropDownMenu_ddm_menuSelectedIcon);
        if (mMenuSelectedIcon == null) {
            mMenuSelectedIcon = ResUtils.getVectorDrawable(getContext(), R.drawable.ddm_ic_arrow_up);
        }
        mMenuHeightPercent = array.getFloat(R.styleable.DropDownMenu_ddm_menuHeightPercent, mMenuHeightPercent);
        array.recycle();

        //初始化tabMenuView并添加到tabMenuView
        mTabMenuView = new LinearLayout(context);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mTabMenuView.setOrientation(HORIZONTAL);
        mTabMenuView.setBackgroundColor(menuBackgroundColor);
        mTabMenuView.setLayoutParams(params);
        addView(mTabMenuView, 0);

        //为tabMenuView添加下划线
        View underLine = new View(getContext());
        underLine.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, underlineHeight));
        underLine.setBackgroundColor(underlineColor);
        addView(underLine, 1);

        //初始化containerView并将其添加到DropDownMenu
        mContainerView = new FrameLayout(context);
        mContainerView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        addView(mContainerView, 2);

    }

    /**
     * 初始化DropDownMenu
     *
     * @param tabTexts
     * @param popupViews
     */
    public void setDropDownMenu(@NonNull String[] tabTexts, @NonNull List<View> popupViews) {
        setDropDownMenu(Arrays.asList(tabTexts), popupViews);
    }

    /**
     * 初始化DropDownMenu
     *
     * @param tabTexts
     * @param popupViews
     */
    public void setDropDownMenu(@NonNull List<String> tabTexts, @NonNull List<View> popupViews) {
        if (mContentLayoutId == -1) {
            throw new IllegalArgumentException("mContentLayoutId == -1, You need to set properties app:ddm_contentLayoutId");
        }
        setDropDownMenu(tabTexts, popupViews, View.inflate(getContext(), mContentLayoutId, null));
    }

    /**
     * 初始化DropDownMenu
     *
     * @param tabTexts
     * @param popupViews
     * @param contentView
     */
    public void setDropDownMenu(@NonNull String[] tabTexts, @NonNull List<View> popupViews, @NonNull View contentView) {
        setDropDownMenu(Arrays.asList(tabTexts), popupViews, contentView);
    }

    /**
     * 初始化DropDownMenu
     *
     * @param tabTexts
     * @param popupViews
     * @param contentView
     */
    public void setDropDownMenu(@NonNull List<String> tabTexts, @NonNull List<View> popupViews, @NonNull View contentView) {
        if (tabTexts.size() != popupViews.size()) {
            throw new IllegalArgumentException("params not match, tabTexts.size() should be equal popupViews.size()");
        }

        for (int i = 0; i < tabTexts.size(); i++) {
            addTab(tabTexts, i);
        }

        mContentView = contentView;
        mContainerView.addView(contentView, 0);

        mMaskView = new View(getContext());
        mMaskView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        mMaskView.setBackgroundColor(mMaskColor);
        mMaskView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeMenu();
            }
        });
        mContainerView.addView(mMaskView, 1);
        mMaskView.setVisibility(GONE);
        if (mContainerView.getChildAt(2) != null) {
            mContainerView.removeViewAt(2);
        }

        mPopupMenuViews = new FrameLayout(getContext());
        mPopupMenuViews.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (DensityUtils.getDisplaySize(getContext(), true).y * mMenuHeightPercent)));
        mPopupMenuViews.setVisibility(GONE);
        mContainerView.addView(mPopupMenuViews, 2);

        for (int i = 0; i < popupViews.size(); i++) {
            popupViews.get(i).setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mPopupMenuViews.addView(popupViews.get(i), i);
        }
    }

    private void addTab(@NonNull List<String> tabTexts, int index) {
        final TextView tab = new TextView(getContext());
        tab.setSingleLine();
        tab.setEllipsize(TextUtils.TruncateAt.END);
        tab.setGravity(Gravity.CENTER);
        tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, mMenuTextSize);
        tab.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0F));
        tab.setTextColor(mMenuTextUnselectedColor);
        setArrowIconEnd(tab, mMenuUnselectedIcon);
        tab.setText(tabTexts.get(index));
        tab.setPadding(mMenuTextPaddingHorizontal, mMenuTextPaddingVertical, mMenuTextPaddingHorizontal, mMenuTextPaddingVertical);
        //添加点击事件
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switchMenu(tab);
            }
        });
        mTabMenuView.addView(tab);
        //添加分割线
        if (index < tabTexts.size() - 1) {
            View view = new View(getContext());
            LayoutParams params = new LayoutParams(mDividerWidth, ViewGroup.LayoutParams.MATCH_PARENT);
            params.topMargin = mDividerMargin;
            params.bottomMargin = mDividerMargin;
            view.setLayoutParams(params);
            view.setBackgroundColor(mDividerColor);
            mTabMenuView.addView(view);
        }
    }

    /**
     * 设置tab菜单文字
     *
     * @param text
     */
    public void setTabMenuText(String text) {
        if (mCurrentTabPosition != -1) {
            ((TextView) mTabMenuView.getChildAt(mCurrentTabPosition)).setText(text);
        }
    }

    /**
     * 设置tab菜单是否可点击
     *
     * @param clickable
     */
    public void setTabMenuClickable(boolean clickable) {
        for (int i = 0; i < mTabMenuView.getChildCount(); i = i + 2) {
            mTabMenuView.getChildAt(i).setClickable(clickable);
        }
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        if (mCurrentTabPosition != -1) {
            ((TextView) mTabMenuView.getChildAt(mCurrentTabPosition)).setTextColor(mMenuTextUnselectedColor);
            setArrowIconEnd((TextView) mTabMenuView.getChildAt(mCurrentTabPosition), mMenuUnselectedIcon);
            mPopupMenuViews.setVisibility(View.GONE);
            mPopupMenuViews.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.ddm_menu_out));
            mMaskView.setVisibility(GONE);
            mMaskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.ddm_mask_out));
            mCurrentTabPosition = -1;
        }
    }

    /**
     * DropDownMenu是否处于可见状态
     *
     * @return
     */
    public boolean isShowing() {
        return mCurrentTabPosition != -1;
    }

    /**
     * @return 内容页
     */
    public View getContentView() {
        return mContentView;
    }

    /**
     * 切换菜单
     *
     * @param target
     */
    private void switchMenu(View target) {
        for (int i = 0; i < mTabMenuView.getChildCount(); i = i + 2) {
            if (target == mTabMenuView.getChildAt(i)) {
                if (mCurrentTabPosition == i) {
                    closeMenu();
                } else {
                    if (mCurrentTabPosition == -1) {
                        mPopupMenuViews.setVisibility(View.VISIBLE);
                        mPopupMenuViews.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.ddm_menu_in));
                        mMaskView.setVisibility(VISIBLE);
                        mMaskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.ddm_mask_in));
                        mPopupMenuViews.getChildAt(i / 2).setVisibility(View.VISIBLE);
                    } else {
                        mPopupMenuViews.getChildAt(i / 2).setVisibility(View.VISIBLE);
                    }
                    mCurrentTabPosition = i;
                    ((TextView) mTabMenuView.getChildAt(i)).setTextColor(mMenuTextSelectedColor);
                    setArrowIconEnd((TextView) mTabMenuView.getChildAt(i), mMenuSelectedIcon);
                }
            } else {
                ((TextView) mTabMenuView.getChildAt(i)).setTextColor(mMenuTextUnselectedColor);
                setArrowIconEnd((TextView) mTabMenuView.getChildAt(i), mMenuUnselectedIcon);
                mPopupMenuViews.getChildAt(i / 2).setVisibility(View.GONE);
            }
        }
    }

    private void setArrowIconEnd(TextView view, Drawable arrowIcon) {
        if (view == null) {
            return;
        }
        view.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, arrowIcon, null);
    }

}
