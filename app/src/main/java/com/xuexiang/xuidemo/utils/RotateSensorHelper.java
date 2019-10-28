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

package com.xuexiang.xuidemo.utils;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * 陀螺仪实现图标旋转的工具类
 *
 * @author xuexiang
 * @since 2019-10-17 00:33
 */
public final class RotateSensorHelper implements SensorEventListener {

    private WeakReference<Activity> mActivity;
    private SensorManager mSensorManager;
    /**
     * 默认旋转角度code，分别为 0,1/2,3,4/-1
     */
    private int mCurRotateCode = 0;
    /**
     * 记录当前的角度，每次旋转从此处开始
     */
    private int mCurAngle = 0;
    /**
     * 需要操作旋转的集合
     */
    private List<View> mViews;

    public RotateSensorHelper(Activity activity, List<View> views) {
        mActivity = new WeakReference<>(activity);
        mViews = views;
        mSensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        //通过传感器管理器获取重力加速度传感器
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER) {
            return;
        }

        float[] values = event.values;
        float angleX = values[0];
        float angleY = values[1];

        double acceleration = Math.sqrt(angleX * angleX + angleY * angleY);
        double cos = angleY / acceleration;
        if (cos > 1) {
            cos = 1;
        } else if (cos < -1) {
            cos = -1;
        }
        double rad = Math.acos(cos);
        if (angleX < 0) {
            rad = 2 * Math.PI - rad;
        }

        if (mActivity.get() != null) {
            int uiRot = mActivity.get().getWindowManager().getDefaultDisplay().getRotation();
            double uiRad = Math.PI / 2 * uiRot;
            rad -= uiRad;

            checkRotate((int) rad);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * 旋转检测
     *
     * @param rotateCode
     */
    private void checkRotate(int rotateCode) {
        if (rotateCode == 2) {
            rotateCode = 1;
        }
        if (rotateCode == -1) {
            rotateCode = 4;
        }

        int angle = 0;

        switch (rotateCode) {
            case 0:
                angle = 0;
                break;
            case 1:
                angle = 90;
                break;
            case 3:
                angle = 180;
                break;
            case 4:
                angle = 270;
                break;
            default:
                break;
        }

        if (rotateCode != mCurRotateCode) {
            if (valueRotateAnim(angle)) {
                mCurAngle = angle;
                mCurRotateCode = rotateCode;
            }
        }
    }

    /**
     * @param angle 旋转角度
     */
    private boolean valueRotateAnim(int angle) {
        //特别处理从270-0度的反向旋转
        if (mCurAngle == 270) {
            angle = 360;
        }

        if (angle != 0 && angle != 90) {
            return false;
        }

        if (mCurAngle == angle) {
            return false;
        }

        for (int i = 0; i < mViews.size(); i++) {
            startRotateAnim(mViews.get(i), 300, mCurAngle, angle);
        }
        return true;
    }

    /**
     * 开始旋转动画
     *
     * @param view
     * @param duration  动画持续时间
     * @param fromAngle
     * @param toAngle
     */
    private void startRotateAnim(View view, long duration, int fromAngle, float toAngle) {
        ObjectAnimator animRotate = ObjectAnimator.ofFloat(view, "rotation", fromAngle, toAngle);
        animRotate.setDuration(duration);
        animRotate.start();
    }

    public void recycle() {
        mSensorManager.unregisterListener(this);
        mActivity = null;
        mViews.clear();
        mViews = null;
    }
}
