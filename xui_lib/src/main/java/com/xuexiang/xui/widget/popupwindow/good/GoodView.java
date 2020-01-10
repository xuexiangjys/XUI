/*
 * Copyright (C) 2020 xuexiangjys(xuexiangjys@163.com)
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
package com.xuexiang.xui.widget.popupwindow.good;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xuexiang.xui.utils.ResUtils;

/**
 * 点赞效果
 *
 * @author xuexiang
 * @since 2020-01-05 19:49
 */
public class GoodView extends PopupWindow implements IGoodView {

    //=======文字属性=====//

    private TextView mContent = null;
    private String mText = DEFAULT_TEXT;
    private int mTextColor = DEFAULT_TEXT_COLOR;
    private int mTextSize = DEFAULT_TEXT_SIZE;

    //=======动画属性=====//

    private AnimationSet mAnimationSet;
    private int mFromY = DEFAULT_FROM_Y_DELTA;
    private int mToY = DEFAULT_TO_Y_DELTA;
    private float mFromAlpha = DEFAULT_FROM_ALPHA;
    private float mToAlpha = DEFAULT_TO_ALPHA;
    private int mDuration = DEFAULT_DURATION;
    private int mDistance = DEFAULT_DISTANCE;

    private boolean mChanged = false;

    public GoodView(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        RelativeLayout layout = new RelativeLayout(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        mContent = new TextView(context);
        mContent.setIncludeFontPadding(false);
        mContent.setTextSize(TypedValue.COMPLEX_UNIT_DIP, mTextSize);
        mContent.setTextColor(mTextColor);
        mContent.setText(mText);
        mContent.setLayoutParams(params);
        layout.addView(mContent);
        setContentView(layout);

        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        mContent.measure(w, h);
        setWidth(mContent.getMeasuredWidth());
        setHeight(mDistance + mContent.getMeasuredHeight());
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setFocusable(false);
        setTouchable(false);
        setOutsideTouchable(false);

        mAnimationSet = createAnimation();
    }

    private static int getTextViewHeight(TextView textView, int width) {
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.AT_MOST);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        textView.measure(widthMeasureSpec, heightMeasureSpec);
        return textView.getMeasuredHeight();
    }

    /**
     * 设置文本
     *
     * @param text
     */
    @Override
    public IGoodView setText(String text) {
        if (TextUtils.isEmpty(text)) {
            throw new IllegalArgumentException("text cannot be null.");
        }
        mText = text;
        mContent.setText(text);
        mContent.setBackground(new ColorDrawable(Color.TRANSPARENT));
        int w = (int) mContent.getPaint().measureText(text);
        setWidth(w);
        setHeight(mDistance + getTextViewHeight(mContent, w));
        return this;
    }

    /**
     * 设置文本颜色
     *
     * @param color
     */
    @Override
    public IGoodView setTextColor(int color) {
        mTextColor = color;
        mContent.setTextColor(color);
        return this;
    }

    /**
     * 设置文本大小
     *
     * @param textSize
     */
    @Override
    public IGoodView setTextSize(int textSize) {
        mTextSize = textSize;
        mContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        return this;
    }

    /**
     * 设置文本信息
     *
     * @param text
     * @param textColor
     * @param textSize
     */
    @Override
    public IGoodView setTextInfo(String text, int textColor, int textSize) {
        setTextColor(textColor);
        setTextSize(textSize);
        setText(text);
        return this;
    }

    /**
     * 设置图片
     *
     * @param resId
     */
    @Override
    public IGoodView setImageResource(int resId) {
        return setImageDrawable(ResUtils.getDrawable(mContent.getContext(), resId));
    }

    /**
     * 设置图片
     *
     * @param drawable
     */
    @Override
    public IGoodView setImageDrawable(Drawable drawable) {
        if (drawable == null) {
            throw new IllegalArgumentException("drawable cannot be null.");
        }
        mContent.setBackground(drawable);
        mContent.setText("");
        setWidth(drawable.getIntrinsicWidth());
        setHeight(mDistance + drawable.getIntrinsicHeight());
        return this;
    }

    /**
     * 设置移动距离
     *
     * @param dis
     */
    @Override
    public IGoodView setDistance(int dis) {
        mDistance = dis;
        mToY = dis;
        mChanged = true;
        setHeight(mDistance + mContent.getMeasuredHeight());
        return this;
    }

    /**
     * 设置Y轴移动属性
     *
     * @param fromY
     * @param toY
     */
    @Override
    public IGoodView setTranslateY(int fromY, int toY) {
        mFromY = fromY;
        mToY = toY;
        mChanged = true;
        return this;
    }

    /**
     * 设置透明度属性
     *
     * @param fromAlpha
     * @param toAlpha
     */
    @Override
    public IGoodView setAlpha(float fromAlpha, float toAlpha) {
        mFromAlpha = fromAlpha;
        mToAlpha = toAlpha;
        mChanged = true;
        return this;
    }

    /**
     * 设置动画时长
     *
     * @param duration
     */
    @Override
    public IGoodView setDuration(int duration) {
        mDuration = duration;
        mChanged = true;
        return this;
    }

    /**
     * 重置属性
     */
    @Override
    public void reset() {
        mText = DEFAULT_TEXT;
        mTextColor = DEFAULT_TEXT_COLOR;
        mTextSize = DEFAULT_TEXT_SIZE;
        mFromY = DEFAULT_FROM_Y_DELTA;
        mToY = DEFAULT_TO_Y_DELTA;
        mFromAlpha = DEFAULT_FROM_ALPHA;
        mToAlpha = DEFAULT_TO_ALPHA;
        mDuration = DEFAULT_DURATION;
        mDistance = DEFAULT_DISTANCE;
        mChanged = false;
        mAnimationSet = createAnimation();
    }

    /**
     * 展示
     *
     * @param v
     */
    @Override
    public void show(View v) {
        if (!isShowing()) {
            int offsetY = -v.getHeight() - getHeight();
            showAsDropDown(v, v.getWidth() / 2 - getWidth() / 2, offsetY);
            if (mAnimationSet == null || mChanged) {
                mAnimationSet = createAnimation();
                mChanged = false;
            }
            mContent.startAnimation(mAnimationSet);
        }
    }

    /**
     * 构建动画
     *
     * @return
     */
    private AnimationSet createAnimation() {
        AnimationSet anim = new AnimationSet(true);
        TranslateAnimation translateAnim = new TranslateAnimation(0, 0, mFromY, -mToY);
        AlphaAnimation alphaAnim = new AlphaAnimation(mFromAlpha, mToAlpha);
        anim.addAnimation(translateAnim);
        anim.addAnimation(alphaAnim);
        anim.setDuration(mDuration);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (isShowing()) {
                    dismiss();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        return anim;
    }
}
