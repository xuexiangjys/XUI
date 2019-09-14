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

package com.xuexiang.xui.widget.behavior;

import android.animation.ValueAnimator;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;

/**
 * 平移动画
 *
 * @author xuexiang
 * @since 2019-05-10 01:06
 */
public class TranslateAnimateHelper implements AnimateHelper {
    public View mTarget;
    public float mStartY;
    public int mCurrentState = STATE_SHOW;
    public int mMode = MODE_TITLE;
    public static int MODE_TITLE = 233;
    public static int MODE_BOTTOM = 2333;
    private float mFirstY = 0;
    private float mMargin;

    private TranslateAnimateHelper(View view) {
        mTarget = view;
        mFirstY = mTarget.getY();
        mMargin = ((CoordinatorLayout.LayoutParams) mTarget.getLayoutParams()).topMargin
                + ((CoordinatorLayout.LayoutParams) mTarget.getLayoutParams()).bottomMargin;
    }

    public static TranslateAnimateHelper get(View target) {
        return new TranslateAnimateHelper(target);
    }

    @Override
    public void show() {
        if (mMode == MODE_TITLE) {
            showTitle();
        } else if (mMode == MODE_BOTTOM) {
            showBottom();
        }
    }

    private void hideTitle() {
        ValueAnimator va = ValueAnimator.ofFloat(mTarget.getY(), -mTarget.getHeight());
        va.setDuration(300);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mTarget.setY((Float) valueAnimator.getAnimatedValue());
            }
        });
        va.start();
        mCurrentState = STATE_HIDE;
    }

    private void showTitle() {
        ValueAnimator va = ValueAnimator.ofFloat(mTarget.getY(), 0);
        va.setDuration(300);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mTarget.setY((Float) valueAnimator.getAnimatedValue());
            }
        });
        va.start();
        mCurrentState = STATE_SHOW;
    }

    @Override
    public void hide() {
        if (mMode == MODE_TITLE) {
            hideTitle();
        } else if (mMode == MODE_BOTTOM) {
            hideBottom();
        }
    }

    private void showBottom() {
        ValueAnimator va = ValueAnimator.ofFloat(mTarget.getY(), mFirstY);
        va.setDuration(300);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mTarget.setY((Float) valueAnimator.getAnimatedValue());
            }
        });

        va.start();
        mCurrentState = STATE_SHOW;
    }

    private void hideBottom() {
        ValueAnimator va = ValueAnimator.ofFloat(mTarget.getY(), mFirstY + mTarget.getHeight() + mMargin);
        va.setDuration(300);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mTarget.setY((Float) valueAnimator.getAnimatedValue());
            }
        });

        va.start();
        mCurrentState = STATE_HIDE;
    }

    @Override
    public void setStartY(float y) {
        mStartY = y;
    }

    @Override
    public int getState() {
        return mCurrentState;
    }

    @Override
    public void setMode(int mode) {
        mMode = mode;
    }

    private void setState(int state) {
        mCurrentState = state;
    }
}
