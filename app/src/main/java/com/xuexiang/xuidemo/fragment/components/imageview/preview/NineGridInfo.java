/*
 * Copyright (C) 2018 xuexiangjys(xuexiangjys@163.com)
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

package com.xuexiang.xuidemo.fragment.components.imageview.preview;

import com.xuexiang.xui.widget.imageview.nine.NineGridImageView;

import java.util.List;


/**
 * 九宫格信息
 *
 * @author xuexiang
 * @since 2018/12/9 下午11:57
 */
public class NineGridInfo {

    private String mContent;

    private List<ImageViewInfo> mImgUrlList;

    private int mShowType;

    private int mSpanType;

    public NineGridInfo() {

    }

    public NineGridInfo(String content, List<ImageViewInfo> imgUrlList) {
       this(content, imgUrlList,  NineGridImageView.STYLE_GRID, NineGridImageView.NOSPAN);
    }

    public NineGridInfo(String content, List<ImageViewInfo> imgUrlList, int showType, int spanType) {
        mContent = content;
        mImgUrlList = imgUrlList;
        mShowType = showType;
        mSpanType = spanType;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public int getShowType() {
        return mShowType;
    }

    public NineGridInfo setShowType(int showType) {
        mShowType = showType;
        return this;
    }

    public List<ImageViewInfo> getImgUrlList() {
        return mImgUrlList;
    }

    public void setImgUrlList(List<ImageViewInfo> imgUrlList) {
        mImgUrlList = imgUrlList;
    }

    public NineGridInfo setSpanType(int spanType) {
        mSpanType = spanType;
        return this;
    }

    public int getSpanType() {
        return mSpanType;
    }
}
