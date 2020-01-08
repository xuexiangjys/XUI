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

package com.xuexiang.xuidemo.fragment.expands.materialdesign;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.core.PageOption;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseSimpleListFragment;
import com.xuexiang.xuidemo.fragment.expands.materialdesign.constraintlayout.ConstraintLayoutContainerFragment;

import java.util.List;

import static com.xuexiang.xuidemo.fragment.expands.materialdesign.constraintlayout.ConstraintLayoutContainerFragment.KEY_LAYOUT_ID;
import static com.xuexiang.xuidemo.fragment.expands.materialdesign.constraintlayout.ConstraintLayoutContainerFragment.KEY_TITLE;

/**
 * @author xuexiang
 * @since 2020-01-08 9:34
 */
@Page(name = "ConstraintLayout\n约束布局")
public class ConstraintLayoutFragment extends BaseSimpleListFragment {

    @Override
    protected List<String> initSimpleData(List<String> lists) {
        lists.add("对齐方式");
        lists.add("尺寸约束");
        lists.add("链约束");
        lists.add("导航线Guideline使用");
        return lists;
    }

    @Override
    protected void onItemClick(int position) {
        switch (position) {
            case 0:
                gotoExample(mSimpleData.get(position), R.layout.layout_constraint_align);
                break;
            case 1:
                gotoExample(mSimpleData.get(position), R.layout.layout_constraint_size);
                break;
            case 2:
                gotoExample(mSimpleData.get(position), R.layout.layout_constraint_chain);
                break;
            case 3:
                gotoExample(mSimpleData.get(position), R.layout.layout_constraint_guideline);
                break;
            default:
                break;
        }
    }


    private void gotoExample(String title, int layoutId) {
        PageOption.to(ConstraintLayoutContainerFragment.class)
                .putString(KEY_TITLE, title)
                .putInt(KEY_LAYOUT_ID, layoutId)
                .open(this);
    }
}
