/*
 * Copyright (C) 2018 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.xuexiang.xui.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Build;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.xuexiang.xui.XUI;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * 软键盘工具
 *
 * @author xuexiang
 * @since 2019/1/14 下午10:04
 */
public class KeyboardUtils implements ViewTreeObserver.OnGlobalLayoutListener {

    private static final HashMap<SoftKeyboardToggleListener, KeyboardUtils> sListenerMap = new HashMap<>();
    private final ViewGroup mRootView;
    private SoftKeyboardToggleListener mCallback;
    private Boolean prevValue = null;

    private KeyboardUtils(Activity activity, SoftKeyboardToggleListener listener) {
        mCallback = listener;
        mRootView = (ViewGroup) activity.getWindow().getDecorView();
        mRootView.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    private KeyboardUtils(ViewGroup viewGroup, SoftKeyboardToggleListener listener) {
        mCallback = listener;
        mRootView = viewGroup;
        mRootView.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    /**
     * Add a new keyboard listener
     *
     * @param act      calling activity
     * @param listener callback
     */
    public static void addKeyboardToggleListener(Activity act, SoftKeyboardToggleListener listener) {
        removeKeyboardToggleListener(listener);
        sListenerMap.put(listener, new KeyboardUtils(act, listener));
    }

    /**
     * Add a new keyboard listener
     *
     * @param act      calling activity
     * @param listener callback
     */
    public static void addKeyboardToggleListener(ViewGroup act, SoftKeyboardToggleListener listener) {
        removeKeyboardToggleListener(listener);
        sListenerMap.put(listener, new KeyboardUtils(act, listener));
    }

    /**
     * Remove a registered listener
     *
     * @param listener {@link SoftKeyboardToggleListener}
     */
    public static void removeKeyboardToggleListener(SoftKeyboardToggleListener listener) {
        if (sListenerMap.containsKey(listener)) {
            KeyboardUtils k = sListenerMap.get(listener);
            if (k != null) {
                k.removeListener();
            }
            sListenerMap.remove(listener);
        }
    }

    /**
     * Remove all registered keyboard listeners
     */
    public static void removeAllKeyboardToggleListeners() {
        for (SoftKeyboardToggleListener l : sListenerMap.keySet()) {
            KeyboardUtils k = sListenerMap.get(l);
            if (k != null) {
                k.removeListener();
            }
        }
        sListenerMap.clear();
    }

    /**
     * Manually toggle soft keyboard visibility
     *
     * @param context calling context
     */
    public static void toggleKeyboardVisibility(Context context) {
        InputMethodManager imm = ContextCompat.getSystemService(context, InputMethodManager.class);
        if (imm == null) {
            return;
        }
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    /**
     * Force closes the soft keyboard
     *
     * @param activeView the view with the keyboard focus
     */
    public static void forceCloseKeyboard(View activeView) {
        InputMethodManager imm = ContextCompat.getSystemService(activeView.getContext(), InputMethodManager.class);
        if (imm == null) {
            return;
        }
        imm.hideSoftInputFromWindow(activeView.getWindowToken(), 0);
    }

    /**
     * 软键盘以覆盖当前界面的形式出现
     *
     * @param activity
     */
    public static void setSoftInputAdjustNothing(@NonNull Activity activity) {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING
                | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    /**
     * 软键盘以顶起当前界面的形式出现, 注意这种方式会使得当前布局的高度发生变化，触发当前布局onSizeChanged方法回调，这里前后高度差就是软键盘的高度了
     *
     * @param activity
     */
    public static void setSoftInputAdjustResize(@NonNull Activity activity) {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    /**
     * 软键盘以上推当前界面的形式出现, 注意这种方式不会改变布局的高度
     *
     * @param activity
     */
    public static void setSoftInputAdjustPan(@NonNull Activity activity) {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
                | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    /**
     * 输入键盘是否在显示
     *
     * @param activity 应用窗口
     */
    public static boolean isSoftInputShow(Activity activity) {
        return activity != null && isSoftInputShow(activity.getWindow());
    }

    /**
     * 输入键盘是否在显示
     *
     * @param window 应用窗口
     */
    public static boolean isSoftInputShow(Window window) {
        if (window != null && window.getDecorView() instanceof ViewGroup) {
            return isSoftInputShow((ViewGroup) window.getDecorView());
        }
        return false;
    }

    /**
     * 输入键盘是否在显示
     *
     * @param rootView 根布局
     */
    public static boolean isSoftInputShow(ViewGroup rootView) {
        if (rootView == null) {
            return false;
        }
        int viewHeight = rootView.getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        rootView.getWindowVisibleDisplayFrame(rect);
        int space = viewHeight - rect.bottom - DensityUtils.getNavigationBarHeight(rootView.getContext());
        return space > 0;
    }

    /**
     * 禁用物理返回键
     * <p>
     * 使用方法：
     * <p>需重写 onKeyDown</p>
     *
     * @param keyCode
     * @return 是否拦截事件
     * <p>
     * @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
     * return KeyboardUtils.onDisableBackKeyDown(keyCode) && super.onKeyDown(keyCode, event);
     * }
     * </p>
     */
    public static boolean onDisableBackKeyDown(int keyCode) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
            case KeyEvent.KEYCODE_HOME:
                return false;
            default:
                break;
        }
        return true;
    }

    /**
     * 点击屏幕空白区域隐藏软键盘
     * <p>根据 EditText 所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘</p>
     * <p>需重写 dispatchTouchEvent</p>
     *
     * @param ev       点击事件
     * @param activity 窗口
     */
    public static void dispatchTouchEvent(MotionEvent ev, @NonNull Activity activity) {
        dispatchTouchEvent(ev, activity.getWindow());
    }

    /**
     * 点击屏幕空白区域隐藏软键盘
     * <p>根据 EditText 所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘</p>
     * <p>需重写 dispatchTouchEvent</p>
     *
     * @param ev     点击事件
     * @param dialog 窗口
     */
    public static void dispatchTouchEvent(MotionEvent ev, @NonNull Dialog dialog) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View currentFocus = dialog.getCurrentFocus();
            if (isShouldHideKeyboardInDialog(ev, currentFocus)) {
                hideSoftInput(currentFocus);
            }
        }
    }

    /**
     * 根据 EditText 所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘(Dialog中使用)
     *
     * @param event 点击事件
     * @param view  目标view
     * @return 是否需要隐藏键盘
     */
    public static boolean isShouldHideKeyboardInDialog(MotionEvent event, View view) {
        if ((view instanceof EditText)) {
            int[] l = {0, 0};
            view.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + view.getHeight(),
                    right = left + view.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }

    /**
     * 点击屏幕空白区域隐藏软键盘
     * <p>根据 EditText 所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘</p>
     * <p>需重写 dispatchTouchEvent</p>
     *
     * @param ev     点击事件
     * @param window 窗口
     */
    public static void dispatchTouchEvent(MotionEvent ev, Window window) {
        if (ev == null || window == null) {
            return;
        }
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (isShouldHideKeyboard(window, ev)) {
                hideSoftInputClearFocus(window.getCurrentFocus());
            }
        }
    }

    /**
     * 根据 EditText 所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘
     *
     * @param view  窗口
     * @param event 用户点击事件
     * @return 是否隐藏键盘
     */
    public static boolean isShouldHideKeyboard(View view, MotionEvent event) {
        if ((view instanceof EditText) && event != null) {
            return !isTouchView(view, event);
        }
        return false;
    }

    /**
     * 根据用户点击的坐标获取用户在窗口上触摸到的View，判断这个View是否是EditText来判断是否隐藏键盘
     *
     * @param window 窗口
     * @param event  用户点击事件
     * @return 是否隐藏键盘
     */
    public static boolean isShouldHideKeyboard(Window window, MotionEvent event) {
        if (window == null || event == null) {
            return false;
        }
        if (!isSoftInputShow(window)) {
            return false;
        }
        if (!(window.getCurrentFocus() instanceof EditText)) {
            return false;
        }
        View decorView = window.getDecorView();
        if (decorView instanceof ViewGroup) {
            return findTouchEditText((ViewGroup) decorView, event) == null;
        }
        return false;
    }

    private static View findTouchEditText(ViewGroup viewGroup, MotionEvent event) {
        if (viewGroup == null) {
            return null;
        }
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            if (child == null || !child.isShown()) {
                continue;
            }
            if (!isTouchView(child, event)) {
                continue;
            }
            if (child instanceof EditText) {
                return child;
            } else if (child instanceof ViewGroup) {
                return findTouchEditText((ViewGroup) child, event);
            }
        }
        return null;
    }

    /**
     * 判断view是否在触摸区域内
     *
     * @param view  view
     * @param event 点击事件
     * @return view是否在触摸区域内
     */
    private static boolean isTouchView(View view, MotionEvent event) {
        if (view == null || event == null) {
            return false;
        }
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + view.getMeasuredWidth();
        int bottom = top + view.getMeasuredHeight();
        return event.getY() >= top && event.getY() <= bottom && event.getX() >= left
                && event.getX() <= right;
    }

    /**
     * 动态隐藏软键盘
     *
     * @param view 视图
     */
    public static void hideSoftInput(final View view) {
        if (view == null) {
            return;
        }
        InputMethodManager imm = ContextCompat.getSystemService(view.getContext(), InputMethodManager.class);
        if (imm == null) {
            return;
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 动态隐藏弹窗弹出的软键盘【注意：一定要在dialog.dismiss之前调用】
     *
     * @param dialog 对话框
     */
    public static void hideSoftInput(@NonNull DialogInterface dialog) {
        if (dialog instanceof Dialog) {
            hideSoftInput((Dialog) dialog);
        }
    }

    /**
     * 动态隐藏弹窗弹出的软键盘【注意：一定要在dialog.dismiss之前调用】
     *
     * @param dialog 对话框
     */
    public static void hideSoftInput(@NonNull Dialog dialog) {
        View view = dialog.getCurrentFocus();
        if (view == null && dialog.getWindow() != null) {
            view = dialog.getWindow().getDecorView();
        }
        hideSoftInput(view);
    }

    /**
     * 动态隐藏弹窗弹出的软键盘【注意：一定要在fragment的onPause之前调用】
     *
     * @Override
     * public void onPause() {
     *    super.onPause();
     *    KeyboardUtils.hideSoftInput(this);
     * }
     *
     * @param fragment fragment
     */
    public static void hideSoftInput(@NonNull Fragment fragment) {
        Activity currentActivity = fragment.getActivity();
        if (currentActivity == null) {
            return;
        }
        hideSoftInput(currentActivity);
    }

    /**
     * 动态隐藏弹窗弹出的软键盘【注意：一定要在activity的onPause之前调用】
     *
     * @Override
     * protected void onPause() {
     *    super.onPause();
     *    KeyboardUtils.hideSoftInput(this);
     * }
     *
     * @param activity activity
     */
    public static void hideSoftInput(@NonNull Activity activity) {
        View view = activity.getCurrentFocus();
        if (view == null && activity.getWindow() != null) {
            view = activity.getWindow().getDecorView();
        }
        hideSoftInput(view);
    }

    /**
     * 动态隐藏软键盘并且清除当前view的焦点【记住，要在xml的父布局加上android:focusable="true" 和 android:focusableInTouchMode="true"】
     *
     * @param view 视图
     */
    public static void hideSoftInputClearFocus(final View view) {
        if (view == null) {
            return;
        }
        InputMethodManager imm = ContextCompat.getSystemService(view.getContext(), InputMethodManager.class);
        if (imm == null) {
            return;
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        if (view instanceof EditText) {
            view.clearFocus();
        }
    }

    /**
     * 切换软键盘显示与否状态
     */
    public static void toggleSoftInput() {
        InputMethodManager imm = ContextCompat.getSystemService(XUI.getContext(), InputMethodManager.class);
        if (imm == null) {
            return;
        }
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    /**
     * 强制显示软键盘
     *
     * @param activity 活动窗口
     */
    public static void showSoftInputForce(Activity activity) {
        if (!isSoftInputShow(activity)) {
            toggleSoftInput();
        }
    }

    /**
     * 显示软键盘
     *
     * @param view 可输入控件，并且在焦点上方可显示
     */
    public static void showSoftInput(final EditText view) {
        if (view == null) {
            return;
        }
        InputMethodManager imm = ContextCompat.getSystemService(view.getContext(), InputMethodManager.class);
        if (imm == null) {
            return;
        }
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 修复软键盘内存泄漏
     *
     * @param context context
     */
    public static void fixSoftInputLeaks(final Context context) {
        if (context == null) {
            return;
        }
        InputMethodManager imm = ContextCompat.getSystemService(context, InputMethodManager.class);
        if (imm == null) {
            return;
        }
        String[] strArr = new String[]{"mCurRootView", "mServedView", "mNextServedView"};
        for (String s : strArr) {
            try {
                Field declaredField = imm.getClass().getDeclaredField(s);
                if (!declaredField.isAccessible()) {
                    declaredField.setAccessible(true);
                }
                Object obj = declaredField.get(imm);
                if (!(obj instanceof View)) {
                    continue;
                }
                View view = (View) obj;
                if (view.getContext() == context) {
                    declaredField.set(imm, null);
                } else {
                    return;
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    @Override
    public void onGlobalLayout() {
        boolean isVisible = isSoftInputShow(mRootView);
        if (mCallback != null && (prevValue == null || isVisible != prevValue)) {
            prevValue = isVisible;
            mCallback.onToggleSoftKeyboard(isVisible);
        }
    }

    private void removeListener() {
        mCallback = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mRootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        } else {
            mRootView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
        }
    }


    public interface SoftKeyboardToggleListener {
        /**
         * 键盘显示状态监听回调
         *
         * @param isVisible 键盘是否显示
         */
        void onToggleSoftKeyboard(boolean isVisible);
    }

}
