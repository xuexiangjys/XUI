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

package com.xuexiang.xuidemo.fragment.components.button;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.popupwindow.good.GoodView;
import com.xuexiang.xui.widget.popupwindow.good.IGoodView;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author xuexiang
 * @since 2020-01-05 20:14
 */
@Page(name = "GoodView\n按钮点击漂浮效果")
public class GoodViewFragment extends BaseFragment {

    private IGoodView mGoodView;

    @BindView(R.id.iv_1)
    ImageView iv1;
    @BindView(R.id.iv_2)
    ImageView iv2;
    @BindView(R.id.iv_3)
    ImageView iv3;
    @BindView(R.id.iv_4)
    ImageView iv4;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_good_view;
    }

    @Override
    protected void initViews() {
        mGoodView = new GoodView(getContext());
    }

    @OnClick({R.id.btn_reset, R.id.iv_1, R.id.iv_2, R.id.iv_3, R.id.iv_4})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_reset:
                reset();
                break;
            case R.id.iv_1:
                iv1.setImageResource(R.drawable.ic_good_checked);
                mGoodView.setText("+1")
                        .setTextColor(Color.parseColor("#f66467"))
                        .setTextSize(12)
                        .show(view);
                break;
            case R.id.iv_2:
                iv2.setImageResource(R.drawable.ic_good_checked);
                mGoodView.setImageResource(R.drawable.ic_good_checked)
                        .show(view);
                break;
            case R.id.iv_3:
                iv3.setImageResource(R.drawable.ic_collection_checked);
                mGoodView.setTextInfo("收藏成功", Color.parseColor("#f66467"), 12)
                        .show(view);
                break;
            case R.id.iv_4:
                iv4.setImageResource(R.drawable.ic_bookmark_checked);
                mGoodView.setTextInfo("收藏成功", Color.parseColor("#ff941A"), 12)
                        .show(view);
                break;
            default:
                break;
        }
    }

    public void reset() {
        iv1.setImageResource(R.drawable.ic_good);
        iv2.setImageResource(R.drawable.ic_good);
        iv3.setImageResource(R.drawable.ic_collection);
        iv4.setImageResource(R.drawable.ic_bookmark);
        mGoodView.reset();
    }
}
