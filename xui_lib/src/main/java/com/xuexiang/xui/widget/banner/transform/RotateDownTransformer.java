package com.xuexiang.xui.widget.banner.transform;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;

/**
 * 向下旋转切换
 *
 * @author xuexiang
 * @since 2019/1/14 下午10:09
 */
public class RotateDownTransformer implements ViewPager.PageTransformer {

	private static final float ROT_MOD = -15f;

	@Override
	public void transformPage(View page, float position) {
		final float width = page.getWidth();
		final float height = page.getHeight();
		final float rotation = ROT_MOD * position * -1.25f;

		ViewHelper.setPivotX(page,width * 0.5f);
        ViewHelper.setPivotY(page,height);
        ViewHelper.setRotation(page,rotation);
	}
}
