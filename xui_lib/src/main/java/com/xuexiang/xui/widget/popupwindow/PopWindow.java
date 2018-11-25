package com.xuexiang.xui.widget.popupwindow;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.xuexiang.xui.R;
import com.xuexiang.xui.utils.Utils;

/**
 * 基类PopupWindow
 * @author xx
 * @Date 2017-1-18 上午11:10:05
 */
public class PopWindow extends PopupWindow {
	private int mPopupWidth;
	private int mPopupHeight;

	/**
	 * @param contentView 布局控件
	 */
	public PopWindow(View contentView) {
		this(contentView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	}
	
	/**
	 * @param context
	 * @param layoutId 布局资源id
	 */
	public PopWindow(Context context, int layoutId) {
		this(context, layoutId, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	}
	
	/**
	 * @param contentView
	 * @param width 宽
	 * @param height 高
	 */
	public PopWindow(View contentView, int width, int height) {
		super(contentView, width, height, false);
		init(contentView.getContext());
	}
	
	/**
	 * @param context
	 * @param layoutId 布局资源id
	 * @param width 宽
	 * @param height 高
	 */
	public PopWindow(Context context, int layoutId, int width, int height) {
		super();
		initContentView(context, layoutId, width, height);
		init(context);
	}
	
	private void initContentView(Context context, int layoutId, int width, int height) {
		View contentView = View.inflate(context, layoutId, null);
		setContentView(contentView);
		setWidth(width);
	    setHeight(height);
	}

	/**
	 * 默认可聚焦、可外部点击消失、无背景
	 */
	private void init(Context context) {
		setFocusable(true);
		setOutsideTouchable(true);
		// 必须设置，否则获得焦点后页面上其他地方点击无响应
		setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_center_popwindow));
		measurePopWindowSize();

	}

	/**
	 * 计算popwindow的尺寸
	 */
	public void measurePopWindowSize() {
		//获取自身的长宽高
		getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
		mPopupHeight = getContentView().getMeasuredHeight();
		mPopupWidth = getContentView().getMeasuredWidth();
	}

	/**
	 * 点击显示或者隐藏弹窗
	 * @param v 点击显示弹窗的控件
	 */
	public void onClick(View v) {
		if (isShowing()) {
			dismiss();
		} else {
			showAsDropDown(v);
		}
	}

	/**
	 * 点击显示或者隐藏弹窗[显示在v上方(以v的左边距为开始位置)]
	 * @param v 点击显示弹窗的控件
	 */
	public void onClickUp2(View v) {
		if (isShowing()) {
			dismiss();
		} else {
			showUp2(v);
		}
	}

	/**
	 * 点击显示或者隐藏弹窗（以v的中心位置为开始位置）
	 * @param v 点击显示弹窗的控件
	 */
	public void onClickUp(View v) {
		if (isShowing()) {
			dismiss();
		} else {
			showUp(v);
		}
	}
	
	/**
	 * 点击显示或者隐藏弹窗
	 * @param v 点击显示弹窗的控件
	 * @param xoff x轴偏移量
	 * @param yoff y轴偏移量
	 */
	public void onClick(View v, int xoff, int yoff) {
		if (isShowing()) {
			dismiss();
		} else {
			showAsDropDown(v, xoff, yoff);
		}
	}
	
	public View findViewById(int resId) {
		return getContentView().findViewById(resId);
	}

    protected <T extends View> T findView(int resId) {
        return (T) getContentView().findViewById(resId);
    }

	public Context getContext() {
		return getContentView().getContext();
	}
	/**
	 * 隐藏PopWindow
	 * @param popWindow
	 */
	public static void dismissPopWindow(PopWindow popWindow) {
		if (popWindow != null) {
			popWindow.dismiss();
		}
	}

	/**
	 * 计算popwindow的真实高度
	 * @param listView
	 */
	public void updatePopWindowHeight(ListView listView) {
		mPopupHeight = Utils.getListViewHeightBasedOnChildren(listView);
	}

	/**
	 * 设置显示在v上方（以v的中心位置为开始位置）
	 * @param v
	 */
	public void showUp(View v) {
		//获取需要在其上方显示的控件的位置信息
		int[] location = new int[2];
		v.getLocationOnScreen(location);
		//在控件上方显示
		showAtLocation(v, Gravity.NO_GRAVITY, (location[0] + v.getWidth() / 2) - mPopupWidth / 2, location[1] - v.getHeight() / 2 - mPopupHeight);
	}

	/**
	 * 设置显示在v上方(以v的左边距为开始位置)
	 * @param v
	 */
	public void showUp2(View v) {
		//获取需要在其上方显示的控件的位置信息
		int[] location = new int[2];
		v.getLocationOnScreen(location);
		//在控件上方显示
		showAtLocation(v, Gravity.NO_GRAVITY, (location[0]) - mPopupWidth / 2, location[1] - mPopupHeight);
	}

}
