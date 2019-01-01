/*
 * Copyright (C) 2019 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xuexiang.xui.widget.picker.widget.adapter;


import com.xuexiang.xui.widget.picker.wheelview.adapter.WheelAdapter;

/**
 * 数字滚轮适配器
 *
 * @author xuexiang
 * @since 2019/1/1 下午6:37
 */
public class NumericWheelAdapter implements WheelAdapter {
	
	private int mMinValue;
	private int mMaxValue;

	/**
	 * Constructor
	 * @param minValue the wheel min value
	 * @param maxValue the wheel max value
	 */
	public NumericWheelAdapter(int minValue, int maxValue) {
		mMinValue = minValue;
		mMaxValue = maxValue;
	}

	@Override
	public Object getItem(int index) {
		if (index >= 0 && index < getItemsCount()) {
			return mMinValue + index;
		}
		return 0;
	}

	@Override
	public int getItemsCount() {
		return mMaxValue - mMinValue + 1;
	}
	
	@Override
	public int indexOf(Object o){
		try {
			return (int)o - mMinValue;
		} catch (Exception e) {
			return -1;
		}

	}
}
