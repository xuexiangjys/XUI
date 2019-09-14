/*
 * Copyright (C) 2019 xuexiangjys(xuexiangjys@163.com)
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

package com.xuexiang.xui.widget.searchview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewAnimationUtils;

/**
 * 动画工具类
 *
 * @author xuexiang
 * @since 2019/1/2 下午3:59
 */
class AnimationUtils {

    private AnimationUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static int ANIMATION_DURATION_SHORT = 150;
    public static int ANIMATION_DURATION_MEDIUM = 400;
    public static int ANIMATION_DURATION_LONG = 800;

    public interface AnimationListener {
        /**
         * @return true to override parent. Else execute Parent method
         */
        boolean onAnimationStart(View view);

        boolean onAnimationEnd(View view);

        boolean onAnimationCancel(View view);
    }

    static void crossFadeViews(View showView, View hideView) {
        crossFadeViews(showView, hideView, ANIMATION_DURATION_SHORT);
    }

    static void crossFadeViews(View showView, final View hideView, int duration) {
        fadeInView(showView, duration);
        fadeOutView(hideView, duration);
    }

    static void fadeInView(View view) {
        fadeInView(view, ANIMATION_DURATION_SHORT);
    }

    static void fadeInView(View view, int duration) {
        fadeInView(view, duration, null);
    }

    static void fadeInView(View view, int duration, final AnimationListener listener) {
        view.setVisibility(View.VISIBLE);
        view.setAlpha(0f);
        ViewPropertyAnimatorListener vpListener = null;

        if (listener != null) {
            vpListener = new ViewPropertyAnimatorListener() {
                @Override
                public void onAnimationStart(View view) {
                    if (!listener.onAnimationStart(view)) {
                        view.setDrawingCacheEnabled(true);
                    }
                }

                @Override
                public void onAnimationEnd(View view) {
                    if (!listener.onAnimationEnd(view)) {
                        view.setDrawingCacheEnabled(false);
                    }
                }

                @Override
                public void onAnimationCancel(View view) {
                }
            };
        }
        ViewCompat.animate(view).alpha(1f).setDuration(duration).setListener(vpListener);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    static void reveal(final View view, final AnimationListener listener) {
        int cx = view.getWidth() - (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 24, view.getResources().getDisplayMetrics());
        int cy = view.getHeight() / 2;
        int finalRadius = Math.max(view.getWidth(), view.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);
        view.setVisibility(View.VISIBLE);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                listener.onAnimationStart(view);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                listener.onAnimationEnd(view);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                listener.onAnimationCancel(view);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim.start();
    }

    static void fadeOutView(View view) {
        fadeOutView(view, ANIMATION_DURATION_SHORT);
    }

    static void fadeOutView(View view, int duration) {
        fadeOutView(view, duration, null);
    }

    static void fadeOutView(View view, int duration, final AnimationListener listener) {
        ViewCompat.animate(view).alpha(0f).setDuration(duration).setListener(new ViewPropertyAnimatorListener() {
            @Override
            public void onAnimationStart(View view) {
                if (listener == null || !listener.onAnimationStart(view)) {
                    view.setDrawingCacheEnabled(true);
                }
            }

            @Override
            public void onAnimationEnd(View view) {
                if (listener == null || !listener.onAnimationEnd(view)) {
                    view.setVisibility(View.GONE);
                    view.setDrawingCacheEnabled(false);
                }
            }

            @Override
            public void onAnimationCancel(View view) {
            }
        });
    }
}