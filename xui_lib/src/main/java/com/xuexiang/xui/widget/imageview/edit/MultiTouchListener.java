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

package com.xuexiang.xui.widget.imageview.edit;

import android.graphics.Rect;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

/**
 * 多点触摸监听
 *
 * @author xuexiang
 * @since 2019-10-28 10:03
 */
class MultiTouchListener implements OnTouchListener {

    private static final int INVALID_POINTER_ID = -1;
    private final GestureDetector mGestureListener;
    private boolean isRotateEnabled = true;
    private boolean isTranslateEnabled = true;
    private boolean isScaleEnabled = true;
    private float minimumScale = 0.5f;
    private float maximumScale = 10.0f;
    private int mActivePointerId = INVALID_POINTER_ID;
    private float mPrevX, mPrevY, mPrevRawX, mPrevRawY;
    private ScaleGestureDetector mScaleGestureDetector;

    private int[] location = new int[2];
    private Rect outRect;
    private View deleteView;
    private ImageView photoEditImageView;
    private RelativeLayout parentView;

    private OnMultiTouchListener onMultiTouchListener;
    private OnGestureControl mOnGestureControl;
    private boolean mIsTextPinchZoomable;
    private OnPhotoEditorListener mOnPhotoEditorListener;

    MultiTouchListener(@Nullable View deleteView, RelativeLayout parentView,
                       ImageView photoEditImageView, boolean isTextPinchZoomable,
                       OnPhotoEditorListener onPhotoEditorListener) {
        mIsTextPinchZoomable = isTextPinchZoomable;
        mScaleGestureDetector = new ScaleGestureDetector(new ScaleGestureListener());
        mGestureListener = new GestureDetector(new GestureListener());
        this.deleteView = deleteView;
        this.parentView = parentView;
        this.photoEditImageView = photoEditImageView;
        this.mOnPhotoEditorListener = onPhotoEditorListener;
        if (deleteView != null) {
            outRect = new Rect(deleteView.getLeft(), deleteView.getTop(),
                    deleteView.getRight(), deleteView.getBottom());
        } else {
            outRect = new Rect(0, 0, 0, 0);
        }
    }

    private static float adjustAngle(float degrees) {
        if (degrees > 180.0f) {
            degrees -= 360.0f;
        } else if (degrees < -180.0f) {
            degrees += 360.0f;
        }

        return degrees;
    }

    private static void move(View view, TransformInfo info) {
        computeRenderOffset(view, info.pivotX, info.pivotY);
        adjustTranslation(view, info.deltaX, info.deltaY);

        float scale = view.getScaleX() * info.deltaScale;
        scale = Math.max(info.minimumScale, Math.min(info.maximumScale, scale));
        view.setScaleX(scale);
        view.setScaleY(scale);

        float rotation = adjustAngle(view.getRotation() + info.deltaAngle);
        view.setRotation(rotation);
    }

    private static void adjustTranslation(View view, float deltaX, float deltaY) {
        float[] deltaVector = {deltaX, deltaY};
        view.getMatrix().mapVectors(deltaVector);
        view.setTranslationX(view.getTranslationX() + deltaVector[0]);
        view.setTranslationY(view.getTranslationY() + deltaVector[1]);
    }

    private static void computeRenderOffset(View view, float pivotX, float pivotY) {
        if (view.getPivotX() == pivotX && view.getPivotY() == pivotY) {
            return;
        }

        float[] prevPoint = {0.0f, 0.0f};
        view.getMatrix().mapPoints(prevPoint);

        view.setPivotX(pivotX);
        view.setPivotY(pivotY);

        float[] currPoint = {0.0f, 0.0f};
        view.getMatrix().mapPoints(currPoint);

        float offsetX = currPoint[0] - prevPoint[0];
        float offsetY = currPoint[1] - prevPoint[1];

        view.setTranslationX(view.getTranslationX() - offsetX);
        view.setTranslationY(view.getTranslationY() - offsetY);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        mScaleGestureDetector.onTouchEvent(view, event);
        mGestureListener.onTouchEvent(event);

        if (!isTranslateEnabled) {
            return true;
        }

        int action = event.getAction();

        int x = (int) event.getRawX();
        int y = (int) event.getRawY();

        switch (action & event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mPrevX = event.getX();
                mPrevY = event.getY();
                mPrevRawX = event.getRawX();
                mPrevRawY = event.getRawY();
                mActivePointerId = event.getPointerId(0);
                if (deleteView != null) {
                    deleteView.setVisibility(View.VISIBLE);
                }
                view.bringToFront();
                firePhotoEditorSDKListener(view, true);
                break;
            case MotionEvent.ACTION_MOVE:
                int pointerIndexMove = event.findPointerIndex(mActivePointerId);
                if (pointerIndexMove != -1) {
                    float currX = event.getX(pointerIndexMove);
                    float currY = event.getY(pointerIndexMove);
                    if (!mScaleGestureDetector.isInProgress()) {
                        adjustTranslation(view, currX - mPrevX, currY - mPrevY);
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                mActivePointerId = INVALID_POINTER_ID;
                break;
            case MotionEvent.ACTION_UP:
                mActivePointerId = INVALID_POINTER_ID;
                if (deleteView != null && isViewInBounds(deleteView, x, y)) {
                    if (onMultiTouchListener != null) {
                        onMultiTouchListener.onRemoveViewListener(view);
                    }
                } else if (!isViewInBounds(photoEditImageView, x, y)) {
                    view.animate().translationY(0).translationY(0);
                }
                if (deleteView != null) {
                    deleteView.setVisibility(View.GONE);
                }
                firePhotoEditorSDKListener(view, false);
                break;
            case MotionEvent.ACTION_POINTER_UP:
                int pointerIndexPointerUp = (action & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                int pointerId = event.getPointerId(pointerIndexPointerUp);
                if (pointerId == mActivePointerId) {
                    int newPointerIndex = pointerIndexPointerUp == 0 ? 1 : 0;
                    mPrevX = event.getX(newPointerIndex);
                    mPrevY = event.getY(newPointerIndex);
                    mActivePointerId = event.getPointerId(newPointerIndex);
                }
                break;
            default:
                break;
        }
        return true;
    }

    private void firePhotoEditorSDKListener(View view, boolean isStart) {
        Object viewTag = view.getTag();
        if (mOnPhotoEditorListener != null && viewTag != null && viewTag instanceof ViewType) {
            if (isStart) {
                mOnPhotoEditorListener.onStartViewChangeListener(((ViewType) view.getTag()));
            } else {
                mOnPhotoEditorListener.onStopViewChangeListener(((ViewType) view.getTag()));
            }
        }
    }

    private boolean isViewInBounds(View view, int x, int y) {
        view.getDrawingRect(outRect);
        view.getLocationOnScreen(location);
        outRect.offset(location[0], location[1]);
        return outRect.contains(x, y);
    }

    void setOnMultiTouchListener(OnMultiTouchListener onMultiTouchListener) {
        this.onMultiTouchListener = onMultiTouchListener;
    }

    private class ScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        private float mPivotX;
        private float mPivotY;
        private Vector2D mPrevSpanVector = new Vector2D();

        @Override
        public boolean onScaleBegin(View view, ScaleGestureDetector detector) {
            mPivotX = detector.getFocusX();
            mPivotY = detector.getFocusY();
            mPrevSpanVector.set(detector.getCurrentSpanVector());
            return mIsTextPinchZoomable;
        }

        @Override
        public boolean onScale(View view, ScaleGestureDetector detector) {
            TransformInfo info = new TransformInfo();
            info.deltaScale = isScaleEnabled ? detector.getScaleFactor() : 1.0f;
            info.deltaAngle = isRotateEnabled ? Vector2D.getAngle(mPrevSpanVector, detector.getCurrentSpanVector()) : 0.0f;
            info.deltaX = isTranslateEnabled ? detector.getFocusX() - mPivotX : 0.0f;
            info.deltaY = isTranslateEnabled ? detector.getFocusY() - mPivotY : 0.0f;
            info.pivotX = mPivotX;
            info.pivotY = mPivotY;
            info.minimumScale = minimumScale;
            info.maximumScale = maximumScale;
            move(view, info);
            return !mIsTextPinchZoomable;
        }
    }

    private class TransformInfo {
        float deltaX;
        float deltaY;
        float deltaScale;
        float deltaAngle;
        float pivotX;
        float pivotY;
        float minimumScale;
        float maximumScale;
    }

    interface OnMultiTouchListener {
        void onEditTextClickListener(String text, int colorCode);

        void onRemoveViewListener(View removedView);
    }

    interface OnGestureControl {
        void onClick();

        void onLongClick();
    }

    void setOnGestureControl(OnGestureControl onGestureControl) {
        mOnGestureControl = onGestureControl;
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (mOnGestureControl != null) {
                mOnGestureControl.onClick();
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
            if (mOnGestureControl != null) {
                mOnGestureControl.onLongClick();
            }
        }
    }
}