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

package com.xuexiang.xuidemo.fragment.expands;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.core.PageOption;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseSimpleListFragment;
import com.xuexiang.xuidemo.fragment.expands.iconfont.SimpleIconFontFragment;
import com.xuexiang.xuidemo.fragment.expands.iconfont.XUIIconFontDisplayFragment;
import com.xuexiang.xuidemo.widget.iconfont.IconFontActivity;

import java.util.List;

/**
 * @author xuexiang
 * @since 2019-10-13 16:59
 */
@Page(name = "字体图标库", extra = R.drawable.ic_expand_iconfont)
public class IconFontFragment extends BaseSimpleListFragment {

    @Override
    protected List<String> initSimpleData(List<String> lists) {
        lists.add("字体图标库的用法展示");
        lists.add("自定义字体图标库XUIIconFont展示");
        return lists;
    }

    @Override
    protected void onItemClick(int position) {
        switch(position) {
            case 0:
                PageOption.to(SimpleIconFontFragment.class)
                        .setNewActivity(true, IconFontActivity.class)
                        .open(this);
                break;
            case 1:
                openPage(XUIIconFontDisplayFragment.class);
                break;
            default:
                break;
        }
    }
}
