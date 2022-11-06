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

package com.xuexiang.xui.widget.picker.wheelview.adapter;


/**
 * 滚轮适配器
 *
 * @author xuexiang
 * @since 2019/1/1 下午5:05
 */
public interface WheelAdapter<T> {
	/**
	 * Gets items count
	 * @return the count of wheel items
	 */
	int getItemsCount();

	/**
	 * Gets a wheel item by index.
	 * @param index the item index
	 * @return the wheel item text or null
	 */
	T getItem(int index);

	/**
	 * Gets maximum item length. It is used to determine the wheel width.
	 * If -1 is returned there will be used the default wheel width.
	 * @param o  the item object
	 * @return the maximum item length or -1
	 */
	int indexOf(T o);
}
