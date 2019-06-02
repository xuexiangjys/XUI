package com.xuexiang.xuidemo.widget;

import android.animation.Animator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Interpolator;

/**
 * 悬浮按钮的行为
 *
 * @author XUE
 * @since 2019/5/9 9:20
 */
public class FabBehavior extends FloatingActionButton.Behavior {

    private static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();

    private float viewY; // Distance from fab to bottom of parent
    private boolean isAnimate;

    public FabBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child,
                                       @NonNull View directTargetChild, @NonNull View target, int nestedScrollAxes) {
        if (child.getVisibility() == View.VISIBLE && viewY == 0) {
            viewY = coordinatorLayout.getHeight() - child.getY();
        }
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL
                || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child,
                                  @NonNull View target, int dx, int dy, @NonNull int[] consumed) {
        if (dy >= 0 && !isAnimate && child.getVisibility() == View.VISIBLE) {
            hide(child);
        } else if (dy < 0 && !isAnimate && child.getVisibility() == View.INVISIBLE) {
            show(child);
        }
    }

    private void hide(final View view) {
        ViewPropertyAnimator animator = view.animate().translationY(viewY).setInterpolator(INTERPOLATOR).setDuration(300);
        animator.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                isAnimate = true;
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                view.setVisibility(View.INVISIBLE);
                isAnimate = false;
            }
            @Override
            public void onAnimationCancel(Animator animator) {
                show(view);
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        animator.start();
    }

    private void show(final View view) {
        ViewPropertyAnimator animator = view.animate().translationY(0).setInterpolator(INTERPOLATOR).setDuration(300);
        animator.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                view.setVisibility(View.VISIBLE);
                isAnimate = true;
            }
            @Override
            public void onAnimationEnd(Animator animator) {
                isAnimate = false;
            }
            @Override
            public void onAnimationCancel(Animator animator) {
                hide(view);
            }
            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        animator.start();
    }

}
