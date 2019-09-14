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

package com.xuexiang.xui.widget.popupwindow.bar;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.xuexiang.xui.R;
import com.xuexiang.xui.utils.ThemeUtils;

/**
 * 顶部和底部信息消息显示条
 *
 * @author xuexiang
 * @since 2018/12/19 上午9:20
 */
final class Cookie extends LinearLayout {

    /**
     * 默认持续时间（ms）
     */
    public static final long DEFAULT_COOKIE_DURATION = 2000;

    private Animation mSlideInAnimation;
    private Animation mSlideOutAnimation;

    private LinearLayout mLayoutCookie;
    private TextView mTvTitle;
    private TextView mTvMessage;
    private ImageView mIvIcon;
    private TextView mBtnAction;
    private ImageView mBtnActionWithIcon;
    private long mDuration = DEFAULT_COOKIE_DURATION;
    private int mGravity = Gravity.BOTTOM;

    public Cookie(@NonNull final Context context) {
        this(context, null);
    }

    public Cookie(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Cookie(@NonNull final Context context, @Nullable final AttributeSet attrs,
                  final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    public int getLayoutGravity() {
        return mGravity;
    }

    private void initViews(Context context) {
        inflate(getContext(), R.layout.xui_layout_cookie, this);

        mLayoutCookie = findViewById(R.id.cookie);
        mTvTitle = findViewById(R.id.tv_title);
        mTvMessage = findViewById(R.id.tv_message);
        mIvIcon = findViewById(R.id.iv_icon);
        mBtnAction = findViewById(R.id.btn_action);
        mBtnActionWithIcon = findViewById(R.id.btn_action_with_icon);
        initDefaultStyle(context);
    }

    /**
     * Init the default text color or background color. You can change the default style by set the
     * Theme's attributes.
     *
     * <pre>
     *  <style name="AppTheme" parent="Theme.AppCompat.Light.DarkActionBar">
     *          <item name="cookieTitleColor">@color/default_title_color</item>
     *          <item name="cookieMessageColor">@color/default_message_color</item>
     *          <item name="cookieActionColor">@color/default_action_color</item>
     *          <item name="cookieBackgroundColor">@color/default_bg_color</item>
     *  </style>
     * </pre>
     */
    private void initDefaultStyle(Context context) {
        //Custom the default style of a cookie
        int titleColor = ThemeUtils.resolveColor(context, R.attr.cookieTitleColor, Color.WHITE);
        int messageColor = ThemeUtils.resolveColor(context, R.attr.cookieMessageColor, Color.WHITE);
        int actionColor = ThemeUtils.resolveColor(context, R.attr.cookieActionColor, Color.WHITE);
        int backgroundColor = ThemeUtils.resolveColor(context, R.attr.cookieBackgroundColor,
                ContextCompat.getColor(context, R.color.cookie_bar_default_bg_color));

        mTvTitle.setTextColor(titleColor);
        mTvMessage.setTextColor(messageColor);
        mBtnAction.setTextColor(actionColor);
        mLayoutCookie.setBackgroundColor(backgroundColor);
    }

    public void setParams(final CookieBar.Params params) {
        if (params != null) {
            mDuration = params.duration;
            mGravity = params.layoutGravity;

            //Icon
            if (params.iconResId != 0) {
                mIvIcon.setVisibility(VISIBLE);
                mIvIcon.setBackgroundResource(params.iconResId);
            }

            //Title
            if (!TextUtils.isEmpty(params.title)) {
                mTvTitle.setVisibility(VISIBLE);
                mTvTitle.setText(params.title);
                if (params.titleColor != 0) {
                    mTvTitle.setTextColor(ContextCompat.getColor(getContext(), params.titleColor));
                }
            }

            //Message
            if (!TextUtils.isEmpty(params.message)) {
                mTvMessage.setVisibility(VISIBLE);
                mTvMessage.setText(params.message);
                if (params.messageColor != 0) {
                    mTvMessage.setTextColor(ContextCompat.getColor(getContext(), params.messageColor));
                }

                if (TextUtils.isEmpty(params.title)) {
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mTvMessage
                            .getLayoutParams();
                    layoutParams.topMargin = 0;
                }
            }

            //Action
            if ((!TextUtils.isEmpty(params.action) || params.actionIcon != 0)
                    && params.onActionClickListener != null) {
                mBtnAction.setVisibility(VISIBLE);
                mBtnAction.setText(params.action);
                mBtnAction.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        params.onActionClickListener.onClick(view);
                        dismiss();
                    }
                });

                //Action Color
                if (params.actionColor != 0) {
                    mBtnAction.setTextColor(ContextCompat.getColor(getContext(), params.actionColor));
                }
            }

            if (params.actionIcon != 0 && params.onActionClickListener != null) {
                mBtnAction.setVisibility(GONE);
                mBtnActionWithIcon.setVisibility(VISIBLE);
                mBtnActionWithIcon.setBackgroundResource(params.actionIcon);
                mBtnActionWithIcon.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        params.onActionClickListener.onClick(view);
                        dismiss();
                    }
                });
            }

            //Background
            if (params.backgroundColor != 0) {
                mLayoutCookie
                        .setBackgroundColor(ContextCompat.getColor(getContext(), params.backgroundColor));
            }

            int padding = ThemeUtils.resolveDimension(getContext(), R.attr.xui_config_content_spacing_horizontal);
            if (mGravity == Gravity.BOTTOM) {
                mLayoutCookie.setPadding(padding, padding, padding, padding);
            }

            createInAnim();
            createOutAnim();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (mGravity == Gravity.TOP) {
            super.onLayout(changed, l, 0, r, mLayoutCookie.getMeasuredHeight());
        } else {
            super.onLayout(changed, l, t, r, b);
        }
    }

    private void createInAnim() {
        mSlideInAnimation = AnimationUtils.loadAnimation(getContext(),
                mGravity == Gravity.BOTTOM ? R.anim.cookiebar_slide_in_from_bottom : R.anim.cookiebar_slide_in_from_top);
        mSlideInAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (mDuration < 0) {
                    return;
                }
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dismiss();
                    }
                }, mDuration);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        setAnimation(mSlideInAnimation);
    }

    private void createOutAnim() {
        mSlideOutAnimation = AnimationUtils.loadAnimation(getContext(),
                mGravity == Gravity.BOTTOM ? R.anim.cookiebar_slide_out_to_bottom : R.anim.cookiebar_slide_out_to_top);
        mSlideOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * 消失
     */
    public void dismiss() {
        mSlideOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(final Animation animation) {
            }

            @Override
            public void onAnimationEnd(final Animation animation) {
                destroy();
            }

            @Override
            public void onAnimationRepeat(final Animation animation) {
            }
        });
        startAnimation(mSlideOutAnimation);
    }

    /**
     * 销毁
     */
    private void destroy() {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                ViewParent parent = getParent();
                if (parent != null) {
                    Cookie.this.clearAnimation();
                    ((ViewGroup) parent).removeView(Cookie.this);
                }
            }
        }, 200);
    }

}
