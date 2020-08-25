/*
 * Copyright (C) 2020 xuexiangjys(xuexiangjys@163.com)
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

package com.xuexiang.xuidemo.fragment.components.refresh.diffutil;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.xuexiang.xuidemo.adapter.entity.NewInfo;
import com.xuexiang.xutil.common.StringUtils;

import java.util.List;

/**
 * 使用DiffUtil进行局部刷新
 *
 * @author xuexiang
 * @since 2020/6/22 12:07 AM
 */
public class DiffUtilCallback extends DiffUtil.Callback {

    public static final String PAYLOAD_USER_NAME = "payload_user_name";

    public static final String PAYLOAD_PRAISE = "payload_praise";

    public static final String PAYLOAD_COMMENT = "payload_comment";

    public static final String PAYLOAD_READ_NUMBER = "payload_read_number";

    private List<NewInfo> mOldData;
    private List<NewInfo> mNewData;

    public DiffUtilCallback(List<NewInfo> oldData, List<NewInfo> newData) {
        mOldData = oldData;
        mNewData = newData;
    }


    @Override
    public int getOldListSize() {
        return mOldData != null ? mOldData.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return mNewData != null ? mNewData.size() : 0;
    }

    /**
     * 被DiffUtil调用，用来判断 两个对象是否是相同的Item。
     *
     * @param oldItemPosition
     * @param newItemPosition
     * @return
     */
    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldData.get(oldItemPosition).getID() == mNewData.get(newItemPosition).getID();
    }

    /**
     * 1.这个方法仅仅在areItemsTheSame()返回true时，才调用。
     * <p>
     * 2.DiffUtil用这个方法来检查两个item是否含有相同的数据
     * <p>
     * 3.DiffUtil 用这个方法替代equals方法去检查是否相等。所以你可以根据你的UI去改变它的返回值
     *
     * @param oldItemPosition
     * @param newItemPosition
     * @return
     */
    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        NewInfo oldInfo = mOldData.get(oldItemPosition);
        NewInfo newInfo = mNewData.get(newItemPosition);
        //如果有内容不同，就返回false
        if (!StringUtils.equals(oldInfo.getUserName(), newInfo.getUserName())) {
            return false;
        }
        if (oldInfo.getPraise() != newInfo.getPraise()) {
            return false;
        }
        if (oldInfo.getComment() != newInfo.getComment()) {
            return false;
        }
        if (oldInfo.getRead() != newInfo.getRead()) {
            return false;
        }
        return true;
    }


    /**
     * 当{@link #areItemsTheSame(int, int)} 返回true，且{@link #areContentsTheSame(int, int)} 返回false时，DiffUtils会回调此方法，去得到这个Item（有哪些）改变的payload。
     *
     * @param oldItemPosition
     * @param newItemPosition
     * @return
     */
    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        NewInfo oldInfo = mOldData.get(oldItemPosition);
        NewInfo newInfo = mNewData.get(newItemPosition);

        //这里就不用比较核心字段了,一定相等
        Bundle payload = new Bundle();
        if (!StringUtils.equals(oldInfo.getUserName(), newInfo.getUserName())) {
            payload.putString(PAYLOAD_USER_NAME, newInfo.getUserName());
        }
        if (oldInfo.getPraise() != newInfo.getPraise()) {
            payload.putInt(PAYLOAD_PRAISE, newInfo.getPraise());
        }
        if (oldInfo.getComment() != newInfo.getComment()) {
            payload.putInt(PAYLOAD_COMMENT, newInfo.getComment());
        }
        if (oldInfo.getRead() != newInfo.getRead()) {
            payload.putInt(PAYLOAD_READ_NUMBER, newInfo.getRead());
        }
        if (payload.size() == 0) {
            return null;
        }
        return payload;
    }
}
