/*
 * Copyright (C) 2018 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use file except in compliance with the License.
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

package com.xuexiang.xuidemo.fragment.components.imageview.preview;

import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.xuexiang.xui.widget.imageview.preview.enitity.IPreviewInfo;

/**
 * 图片预览实体类
 *
 * @author xuexiang
 * @since 2018/12/7 下午5:34
 */
public class ImageViewInfo implements IPreviewInfo {

    private String mUrl;  //图片地址
    private Rect mBounds; // 记录坐标
    private String mVideoUrl;

    private String mDescription = "描述信息";

    public ImageViewInfo(String url) {
        mUrl = url;
    }

    public ImageViewInfo(String videoUrl, String url) {
        mUrl = url;
        mVideoUrl = videoUrl;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    @Override
    public String getUrl() {//将你的图片地址字段返回
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    @Override
    public Rect getBounds() {//将你的图片显示坐标字段返回
        return mBounds;
    }

    @Nullable
    @Override
    public String getVideoUrl() {
        return mVideoUrl;
    }

    public void setBounds(Rect bounds) {
        mBounds = bounds;
    }

    public void setVideoUrl(String videoUrl) {
        mVideoUrl = videoUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mUrl);
        dest.writeParcelable(mBounds, flags);
        dest.writeString(mDescription);
        dest.writeString(mVideoUrl);
    }

    protected ImageViewInfo(Parcel in) {
        mUrl = in.readString();
        mBounds = in.readParcelable(Rect.class.getClassLoader());
        mDescription = in.readString();
        mVideoUrl = in.readString();
    }

    public static final Parcelable.Creator<ImageViewInfo> CREATOR = new Parcelable.Creator<ImageViewInfo>() {
        @Override
        public ImageViewInfo createFromParcel(Parcel source) {
            return new ImageViewInfo(source);
        }

        @Override
        public ImageViewInfo[] newArray(int size) {
            return new ImageViewInfo[size];
        }
    };
}
