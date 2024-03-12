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

package com.xuexiang.xuidemo.fragment.components.imageview;

import static com.xuexiang.xuidemo.fragment.components.imageview.DrawablePreviewFragment.DRAWABLE_ID;

import android.os.Bundle;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.imageview.preview.view.SmoothImageView;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;

import butterknife.BindView;

/**
 * @author xuexiang
 * @since 2019/1/7 下午2:09
 */
@Page(name = "资源图片预览", params = {DRAWABLE_ID}, anim = CoreAnim.none)
public class DrawablePreviewFragment extends BaseFragment {

    public final static String DRAWABLE_ID = "drawable_id";
    @BindView(R.id.photoView)
    SmoothImageView mImageView;

    private int mDrawableId;

    @Override
    protected TitleBar initTitle() {
        return null;
    }

    @Override
    protected void initArgs() {
        Bundle args = getArguments();
        if (args != null) {
            mDrawableId = args.getInt(DRAWABLE_ID, -1);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_drawable_preview;
    }

    @Override
    protected void initViews() {
        mImageView.setImageDrawable(ResUtils.getDrawable(getContext(), mDrawableId));
        mImageView.setMinimumScale(0.5F);
    }

    @Override
    public void onDestroyView() {
        if (mImageView != null) {
            mImageView.setImageBitmap(null);
        }
        super.onDestroyView();
    }
}
