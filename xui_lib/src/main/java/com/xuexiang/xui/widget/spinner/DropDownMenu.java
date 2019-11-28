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
import com.xuexiang.xui.utils.DeviceUtils;
import com.xuexiang.xui.utils.ResUtils;

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

    /**
     * 分割线颜色
     */
    private int mDividerColor = 0xffcccccc;
    /**
     * tab选中颜色
     */
    private int mTextSelectedColor = 0xff890c85;
    /**
     * tab未选中颜色
     */
    private int mTextUnselectedColor = 0xff111111;
    /**
     * 遮罩颜色
     */
    private int mMaskColor = 0x88888888;
    /**
     * tab字体大小
     */
    private int mMenuTextSize = 14;
    /**
     * tab选中图标
     */
    private Drawable mMenuSelectedIcon;
    /**
     * tab未选中图标
     */
    private Drawable mMenuUnselectedIcon;

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
        int menuBackgroundColor = 0xFFFFFFFF;
        int underlineColor = 0xffcccccc;

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.DropDownMenu);
        underlineColor = array.getColor(R.styleable.DropDownMenu_ddm_underlineColor, underlineColor);
        mDividerColor = array.getColor(R.styleable.DropDownMenu_ddm_dividerColor, mDividerColor);
        mTextSelectedColor = array.getColor(R.styleable.DropDownMenu_ddm_textSelectedColor, mTextSelectedColor);
        mTextUnselectedColor = array.getColor(R.styleable.DropDownMenu_ddm_textUnselectedColor, mTextUnselectedColor);
        menuBackgroundColor = array.getColor(R.styleable.DropDownMenu_ddm_menuBackgroundColor, menuBackgroundColor);
        mMaskColor = array.getColor(R.styleable.DropDownMenu_ddm_maskColor, mMaskColor);
        mMenuTextSize = array.getDimensionPixelSize(R.styleable.DropDownMenu_ddm_menuTextSize, mMenuTextSize);
        mMenuSelectedIcon = ResUtils.getDrawableAttrRes(getContext(), array, R.styleable.DropDownMenu_ddm_menuSelectedIcon);
        mMenuUnselectedIcon = ResUtils.getDrawableAttrRes(getContext(), array, R.styleable.DropDownMenu_ddm_menuUnselectedIcon);
        mMenuHeightPercent = array.getFloat(R.styleable.DropDownMenu_ddm_menuMenuHeightPercent, mMenuHeightPercent);
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
        underLine.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtils.dp2px(1.0F)));
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
     * @param contentView
     */
    public void setDropDownMenu(@NonNull List<String> tabTexts, @NonNull List<View> popupViews, @NonNull View contentView) {
        if (tabTexts.size() != popupViews.size()) {
            throw new IllegalArgumentException("params not match, tabTexts.size() should be equal popupViews.size()");
        }

        for (int i = 0; i < tabTexts.size(); i++) {
            addTab(tabTexts, i);
        }
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
        mPopupMenuViews.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (DeviceUtils.getScreenSize(getContext()).y * mMenuHeightPercent)));
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
        tab.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
        tab.setTextColor(mTextUnselectedColor);
        tab.setCompoundDrawablesWithIntrinsicBounds(null, null, mMenuUnselectedIcon, null);
        tab.setText(tabTexts.get(index));
        tab.setPadding(DensityUtils.dp2px(5), DensityUtils.dp2px(12), DensityUtils.dp2px(5), DensityUtils.dp2px(12));
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
            view.setLayoutParams(new LayoutParams(DensityUtils.dp2px(0.5f), ViewGroup.LayoutParams.MATCH_PARENT));
            view.setBackgroundColor(mDividerColor);
            mTabMenuView.addView(view);
        }
    }

    /**
     * 改变tab文字
     *
     * @param text
     */
    public void setTabText(String text) {
        if (mCurrentTabPosition != -1) {
            ((TextView) mTabMenuView.getChildAt(mCurrentTabPosition)).setText(text);
        }
    }

    public void setTabClickable(boolean clickable) {
        for (int i = 0; i < mTabMenuView.getChildCount(); i = i + 2) {
            mTabMenuView.getChildAt(i).setClickable(clickable);
        }
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        if (mCurrentTabPosition != -1) {
            ((TextView) mTabMenuView.getChildAt(mCurrentTabPosition)).setTextColor(mTextUnselectedColor);
            ((TextView) mTabMenuView.getChildAt(mCurrentTabPosition)).setCompoundDrawablesWithIntrinsicBounds(null, null, mMenuUnselectedIcon, null);
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
     * 切换菜单
     *
     * @param target
     */
    private void switchMenu(View target) {
        System.out.println(mCurrentTabPosition);
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
                    ((TextView) mTabMenuView.getChildAt(i)).setTextColor(mTextSelectedColor);
                    ((TextView) mTabMenuView.getChildAt(i)).setCompoundDrawablesWithIntrinsicBounds(null, null, mMenuSelectedIcon, null);
                }
            } else {
                ((TextView) mTabMenuView.getChildAt(i)).setTextColor(mTextUnselectedColor);
                ((TextView) mTabMenuView.getChildAt(i)).setCompoundDrawablesWithIntrinsicBounds(null, null, mMenuUnselectedIcon, null);
                mPopupMenuViews.getChildAt(i / 2).setVisibility(View.GONE);
            }
        }
    }
}
