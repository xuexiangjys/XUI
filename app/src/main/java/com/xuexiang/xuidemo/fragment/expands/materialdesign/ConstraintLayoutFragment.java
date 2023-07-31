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

import android.os.Build;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.core.PageOption;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseSimpleListFragment;
import com.xuexiang.xuidemo.fragment.expands.materialdesign.constraintlayout.ConstraintLayoutConstraintSetFragment;
import com.xuexiang.xuidemo.fragment.expands.materialdesign.constraintlayout.ConstraintLayoutContainerFragment;
import com.xuexiang.xuidemo.fragment.expands.materialdesign.constraintlayout.ConstraintLayoutGroupFragment;
import com.xuexiang.xuidemo.fragment.expands.materialdesign.constraintlayout.ConstraintLayoutPlaceholderFragment;
import com.xuexiang.xui.utils.XToastUtils;

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
        lists.add("栅栏Barrier使用");
        lists.add("分组Group使用");
        lists.add("占位符Placeholder使用");
        lists.add("ConstraintSet实现切换动画");
        lists.add("MotionLayout\n能实现丰富的交互动画");
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
            case 4:
                gotoExample(mSimpleData.get(position), R.layout.layout_constraint_barrier);
                break;
            case 5:
                openPage(ConstraintLayoutGroupFragment.class);
                break;
            case 6:
                openPage(ConstraintLayoutPlaceholderFragment.class);
                break;
            case 7:
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                    XToastUtils.warning("当前手机版本过低，暂不支持");
                } else {
                    openPage(ConstraintLayoutConstraintSetFragment.class);
                }
                break;
            case 8:
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                    XToastUtils.warning("当前手机版本过低，暂不支持");
                } else {
                    openPage(MotionLayoutFragment.class);
                }
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
