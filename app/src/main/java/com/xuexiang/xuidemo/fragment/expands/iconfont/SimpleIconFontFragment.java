/*
 * Copyright (C) 2019 xuexiangjys(xuexiangjys@163.com)
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

package com.xuexiang.xuidemo.fragment.expands.iconfont;

import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;

import com.mikepenz.iconics.IconicsColor;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.IconicsSize;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.utils.ThemeUtils;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xuidemo.widget.iconfont.XUIIconFont;
import com.xuexiang.xuidemo.widget.iconfont.XUIIconImageView;

import butterknife.BindView;

/**
 * @author xuexiang
 * @since 2019-10-13 17:07
 */
@Page(name = "字体图标库的用法展示")
public class SimpleIconFontFragment extends BaseFragment {


    @BindView(R.id.iv_font)
    AppCompatImageView ivFont;
    @BindView(R.id.xiiv_font)
    XUIIconImageView xiivFont;
    @BindView(R.id.tv_font)
    TextView tvFont;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_simple_iconfont;
    }

    @Override
    protected void initViews() {
        IconicsDrawable drawable = new IconicsDrawable(getContext())
                .icon(XUIIconFont.Icon.xui_emoj)
                .color(IconicsColor.colorInt(ThemeUtils.resolveColor(getContext(), R.attr.colorAccent)))
                .size(IconicsSize.dp(24));
        ivFont.setImageDrawable(drawable);

        xiivFont.setIconText("emoj");


        //TextView一定要注入字体，否则无法生效，字体注入方法详见 com.xuexiang.xuidemo.widget.iconfont.IconFontActivity类
        tvFont.setText("{xui_emoj}");
    }
}
