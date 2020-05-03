package com.xuexiang.xuidemo.utils;

import android.animation.ValueAnimator;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;

public class Animators {

    private Animators() {}

    public static ValueAnimator makeDeterminateCircularPrimaryProgressAnimator(
            final ProgressBar[] progressBars) {
        ValueAnimator animator = ValueAnimator.ofInt(0, 150);
        animator.setDuration(6000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.addUpdateListener(
                animator1 -> {
                    int value = (int) animator1.getAnimatedValue();
                    for (ProgressBar progressBar : progressBars) {
                        progressBar.setProgress(value);
                    }
                });
        return animator;
    }

    public static ValueAnimator makeDeterminateCircularPrimaryAndSecondaryProgressAnimator(
            final ProgressBar[] progressBars) {
        ValueAnimator animator = makeDeterminateCircularPrimaryProgressAnimator(progressBars);
        animator.addUpdateListener(
                animator1 -> {
                    int value = Math.round(1.25f * (int) animator1.getAnimatedValue());
                    for (ProgressBar progressBar : progressBars) {
                        progressBar.setSecondaryProgress(value);
                    }
                });
        return animator;
    }
}
