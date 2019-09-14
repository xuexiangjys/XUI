package com.xuexiang.xui.widget.progress.ratingbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.xuexiang.xui.R;

/**
 *
 *
 * @author xuexiang
 * @since 2019-06-06 00:48
 */
public class RotationRatingBar extends AnimationRatingBar {

    // Control animation speed
    private static final long ANIMATION_DELAY = 15;

    public RotationRatingBar(Context context) {
        super(context);
    }

    public RotationRatingBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RotationRatingBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void emptyRatingBar() {
        // Need to remove all previous runnable to prevent emptyRatingBar and fillRatingBar out of sync
        if (mRunnable != null) {
            mHandler.removeCallbacksAndMessages(mRunnableToken);
        }

        long delay = 0;
        for (final PartialView partialView : mPartialViews) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    partialView.setEmpty();
                }
            }, delay += 5);
        }
    }

    @Override
    protected void fillRatingBar(final float rating) {
        // Need to remove all previous runnable to prevent emptyRatingBar and fillRatingBar out of sync
        if (mRunnable != null) {
            mHandler.removeCallbacksAndMessages(mRunnableToken);
        }

        for (final PartialView partialView : mPartialViews) {
            final int ratingViewId = (int) partialView.getTag();
            final double maxIntOfRating = Math.ceil(rating);

            if (ratingViewId > maxIntOfRating) {
                partialView.setEmpty();
                continue;
            }

            mRunnable = getAnimationRunnable(rating, partialView, ratingViewId, maxIntOfRating);
            postRunnable(mRunnable, ANIMATION_DELAY);
        }
    }

    @NonNull
    private Runnable getAnimationRunnable(final float rating, final PartialView partialView, final int ratingViewId, final double maxIntOfRating) {
        return new Runnable() {
            @Override
            public void run() {
                if (ratingViewId == maxIntOfRating) {
                    partialView.setPartialFilled(rating);
                } else {
                    partialView.setFilled();
                }

                if (ratingViewId == rating) {
                    Animation rotation = AnimationUtils.loadAnimation(getContext(), R.anim.srb_rotation);
                    partialView.startAnimation(rotation);
                }
            }
        };
    }
}