package com.xuexiang.xui.widget.textview;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;

/**
 * 自定义跑马灯Textview类
 *
 * @author xuexiang
 * @since 2018/11/22 上午12:36
 */
public class AutoMoveTextView extends AppCompatTextView {
	public AutoMoveTextView(Context context) {
		super(context);
		setEllipsize(TextUtils.TruncateAt.MARQUEE);
	}

	public AutoMoveTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setEllipsize(TextUtils.TruncateAt.MARQUEE);
	}

	public AutoMoveTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setEllipsize(TextUtils.TruncateAt.MARQUEE);
	}

	@Override
	public boolean isFocused() {
		return true;
	}
}
