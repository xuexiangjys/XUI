package com.xuexiang.xui.widget.banner.anim;

import android.view.View;
import android.view.animation.Interpolator;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.view.ViewHelper;


public abstract class BaseAnimator {
    protected long mDuration = 500;
    protected AnimatorSet mAnimatorSet = new AnimatorSet();
    private Interpolator mInterpolator;
    private long mDelay;
    private AnimatorListener mListener;

    public abstract void setAnimation(View view);

    protected void start(final View view) {
        reset(view);
        setAnimation(view);

        mAnimatorSet.setDuration(mDuration);
        if (mInterpolator != null) {
            mAnimatorSet.setInterpolator(mInterpolator);
        }

        if (mDelay > 0) {
            mAnimatorSet.setStartDelay(mDelay);
        }

        if (mListener != null) {
            mAnimatorSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    mListener.onAnimationStart(animator);
                }

                @Override
                public void onAnimationRepeat(Animator animator) {
                    mListener.onAnimationRepeat(animator);
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    mListener.onAnimationEnd(animator);
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                    mListener.onAnimationCancel(animator);
                }
            });
        }

        mAnimatorSet.start();
    }

    public static void reset(View view) {
        ViewHelper.setAlpha(view, 1);
        ViewHelper.setScaleX(view, 1);
        ViewHelper.setScaleY(view, 1);
        ViewHelper.setTranslationX(view, 0);
        ViewHelper.setTranslationY(view, 0);
        ViewHelper.setRotation(view, 0);
        ViewHelper.setRotationY(view, 0);
        ViewHelper.setRotationX(view, 0);
    }

    public BaseAnimator duration(long duration) {
        this.mDuration = duration;
        return this;
    }

    public BaseAnimator delay(long delay) {
        this.mDelay = delay;
        return this;
    }

    public BaseAnimator interpolator(Interpolator interpolator) {
        this.mInterpolator = interpolator;
        return this;
    }

    public BaseAnimator listener(AnimatorListener listener) {
        this.mListener = listener;
        return this;
    }

    public void playOn(View view) {
        start(view);
    }

    public interface AnimatorListener {
        void onAnimationStart(Animator animator);

        void onAnimationRepeat(Animator animator);

        void onAnimationEnd(Animator animator);

        void onAnimationCancel(Animator animator);
    }
}
