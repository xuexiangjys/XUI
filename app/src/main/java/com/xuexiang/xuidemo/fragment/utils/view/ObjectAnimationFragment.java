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

package com.xuexiang.xuidemo.fragment.utils.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.graphics.PointF;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * ObjectAnimator 对象动画
 * ValueAnimator 值动画
 * PropertyValueHolder 用于同时执行多个动画
 * TypeEvaluator 估值器
 * AnimatorSet 动画集合
 * Interpolator 差值器(已经预先定义好的估值器)
 *
 * @author xuexiang
 * @since 2019-11-30 17:29
 */
@Page(name = "属性动画")
public class ObjectAnimationFragment extends BaseFragment {

    @BindView(R.id.tv_content)
    TextView tvContent;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_object_animation;
    }

    @Override
    protected void initViews() {

    }

    @SingleClick
    @OnClick({R.id.btn_alpha, R.id.btn_translation, R.id.btn_scale, R.id.btn_rotation, R.id.btn_value_animator, R.id.btn_type_evaluator, R.id.btn_compose1, R.id.btn_compose2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_alpha:
                doAlphaAnimation(tvContent);
                break;
            case R.id.btn_translation:
                doTranslationAnimation(tvContent);
                break;
            case R.id.btn_scale:
                doScaleAnimation(tvContent);
                break;
            case R.id.btn_rotation:
                doRotationAnimation(tvContent);
                break;
            case R.id.btn_value_animator:
                doValueAnimator(tvContent);
                break;
            case R.id.btn_type_evaluator:
                doTypeEvaluator(tvContent);
                break;
            case R.id.btn_compose1:
                doComposeAnimation1(tvContent);
                break;
            case R.id.btn_compose2:
                doComposeAnimation2(tvContent);
                break;
            default:
                break;
        }
    }

    /**
     * 透明度动画
     *
     * @param view
     */
    private void doAlphaAnimation(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", 1, 0, 1);
        animator.setDuration(2000);
        animator.start();
    }

    /**
     * 平移动画
     *
     * @param view
     */
    private void doTranslationAnimation(View view) {
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(view, "translationX", 0, 200, 0, -200, 0);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(view, "translationY", 0, 200, 0, -200, 0);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animatorX).before(animatorY);
        animatorSet.setDuration(2000);
        animatorSet.start();
    }

    /**
     * 缩放动画
     *
     * @param view
     */
    private void doScaleAnimation(View view) {
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(view, "scaleX", 1F, 0.5F, 1F, 1.5F, 1F);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(view, "scaleY", 1F, 0.5F, 1F, 1.5F, 1F);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animatorX).with(animatorY);
        animatorSet.setDuration(2000);
        animatorSet.start();
    }

    /**
     * 旋转动画
     *
     * @param view
     */
    private void doRotationAnimation(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation", 0, 360, 0);
        animator.setDuration(2000);
        //加速查值器，参数越大，速度越来越快
        animator.setInterpolator(new AccelerateInterpolator(2));
//        //减速差值起，和上面相反
//        animator.setInterpolator(new DecelerateInterpolator(10));
//        //先加速后减速插值器
//        animator.setInterpolator(new AccelerateDecelerateInterpolator());
//        //张力值，默认为2，T越大，初始的偏移越大，而且速度越快
//        animator.setInterpolator(new AnticipateInterpolator(3));
//        //张力值tension，默认为2，张力越大，起始时和结束时的偏移越大
//        animator.setInterpolator(new AnticipateOvershootInterpolator(6));
//        //弹跳插值器
//        animator.setInterpolator(new BounceInterpolator());
//        //周期插值器
//        animator.setInterpolator(new CycleInterpolator(2));
//        //线性差值器,匀速
//        animator.setInterpolator(new LinearInterpolator());
//        //张力插值器，扩散反弹一下
//        animator.setInterpolator(new OvershootInterpolator(2));
        animator.start();
    }


    /**
     * 值动画，适合构建复杂的动画
     *
     * @param view
     */
    private void doValueAnimator(View view) {
        //值的变化，与控件无关
        ValueAnimator animator = ValueAnimator.ofFloat(1, 0, 1);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                view.setScaleX(value);
                view.setScaleY(value);
                view.setAlpha(value);
                view.setRotation(360 * (1 - value));
            }
        });
        animator.setDuration(2000).start();
    }


    /**
     * 估值器（实现重力下落的效果）
     *
     * @param view
     */
    private void doTypeEvaluator(final View view) {
        ValueAnimator animator = new ValueAnimator();
        animator.setDuration(3000);
        animator.setObjectValues(new PointF(0, 0));
        final PointF pointF = new PointF();
        animator.setEvaluator(new TypeEvaluator() {
            @Override
            public Object evaluate(float fraction, Object startValue, Object endValue) {
                //fraction是运动中的匀速变化的值
                //根据重力计算实际的运动y=vt=0.5*g*t*t
                //g越大效果越明显
                pointF.x = 100 * (fraction * 5);
                pointF.y = 0.5f * 300f * (fraction * 5) * (fraction * 5);
                return pointF;
            }
        });
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF p = (PointF) animation.getAnimatedValue();
                view.setX(p.x);
                view.setY(p.y);
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //还原到原始的位置
                view.setTranslationX(0);
                view.setTranslationY(0);
            }
        });
        animator.start();
    }


    /**
     * 组合动画
     *
     * @param view
     */
    private void doComposeAnimation1(View view) {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 1, 0, 1);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1, 0, 1);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1, 0, 1);
        ObjectAnimator rotation = ObjectAnimator.ofFloat(view, "rotation", 0, 360, 0);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(alpha).with(scaleX).with(scaleY).with(rotation);
        animatorSet.setDuration(2000).start();
    }


    /**
     * 组合动画
     *
     * @param view
     */
    private void doComposeAnimation2(View view) {
        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", 1, 0, 1);
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 1, 0, 1);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 1, 0, 1);
        PropertyValuesHolder rotation = PropertyValuesHolder.ofFloat("rotation", 0, 360, 0);

        ValueAnimator animator = ObjectAnimator.ofPropertyValuesHolder(view, alpha, scaleX, scaleY, rotation);
        animator.setDuration(2000).start();
    }
}
