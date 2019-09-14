package com.xuexiang.xui.widget.banner.transform;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;
/**
 * 向上旋转切换
 *
 * @author xuexiang
 * @since 2019/1/14 下午10:15
 */
public class RotateUpTransformer implements ViewPager.PageTransformer {

	private static final float ROT_MOD = -15f;

	@Override
	public void transformPage(@NonNull View page, float position) {
		final float width = page.getWidth();
		final float rotation = ROT_MOD * position;

		page.setPivotX(width * 0.5f);
		page.setPivotY(0f);
		page.setTranslationX(0f);
		page.setRotation(rotation);
	}
}
