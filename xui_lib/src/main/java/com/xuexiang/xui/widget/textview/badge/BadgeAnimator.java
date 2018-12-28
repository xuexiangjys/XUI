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

package com.xuexiang.xui.widget.textview.badge;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import java.lang.ref.WeakReference;
import java.util.Random;

/**
 * Animation borrowed from https://github.com/tyrantgit/ExplosionField
 *
 * @author xuexiang
 * @since 2018/12/28 上午9:20
 */
public class BadgeAnimator extends ValueAnimator {
    private BitmapFragment[][] mFragments;
    private WeakReference<BadgeView> mWeakBadge;

    public BadgeAnimator(Bitmap badgeBitmap, PointF center, BadgeView badge) {
        mWeakBadge = new WeakReference<>(badge);
        setFloatValues(0f, 1f);
        setDuration(500);
        mFragments = getFragments(badgeBitmap, center);
        addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                BadgeView badgeView = mWeakBadge.get();
                if (badgeView == null || !badgeView.isShown()) {
                    cancel();
                } else {
                    badgeView.invalidate();
                }
            }
        });
        addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                BadgeView badgeView = mWeakBadge.get();
                if (badgeView != null) {
                    badgeView.reset();
                }
            }
        });
    }

    public void draw(Canvas canvas) {
        for (int i = 0; i < mFragments.length; i++) {
            for (int j = 0; j < mFragments[i].length; j++) {
                BitmapFragment bf = mFragments[i][j];
                float value = Float.parseFloat(getAnimatedValue().toString());
                bf.update(value, canvas);
            }
        }
    }


    private BitmapFragment[][] getFragments(Bitmap badgeBitmap, PointF center) {
        int width = badgeBitmap.getWidth();
        int height = badgeBitmap.getHeight();
        float fragmentSize = Math.min(width, height) / 6f;
        float startX = center.x - badgeBitmap.getWidth() / 2f;
        float startY = center.y - badgeBitmap.getHeight() / 2f;
        BitmapFragment[][] fragments = new BitmapFragment[(int) (height / fragmentSize)][(int) (width / fragmentSize)];
        for (int i = 0; i < fragments.length; i++) {
            for (int j = 0; j < fragments[i].length; j++) {
                BitmapFragment bf = new BitmapFragment();
                bf.color = badgeBitmap.getPixel((int) (j * fragmentSize), (int) (i * fragmentSize));
                bf.x = startX + j * fragmentSize;
                bf.y = startY + i * fragmentSize;
                bf.size = fragmentSize;
                bf.maxSize = Math.max(width, height);
                fragments[i][j] = bf;
            }
        }
        badgeBitmap.recycle();
        return fragments;
    }

    private class BitmapFragment {
        Random random;
        float x;
        float y;
        float size;
        int color;
        int maxSize;
        Paint paint;

        public BitmapFragment() {
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL);
            random = new Random();
        }

        public void update(float value, Canvas canvas) {
            paint.setColor(color);
            x = x + 0.1f * random.nextInt(maxSize) * (random.nextFloat() - 0.5f);
            y = y + 0.1f * random.nextInt(maxSize) * (random.nextFloat() - 0.5f);
            canvas.drawCircle(x, y, size - value * size, paint);
        }
    }
}
