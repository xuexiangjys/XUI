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

package com.xuexiang.xui.widget.behavior;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * Behavior for ToolBar
 *
 * @author xuexiang
 * @since 2019-05-10 01:05
 */
public class ToolBarBehavior extends BaseBehavior {

    public ToolBarBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        if (canInit) {
            mAnimateHelper = TranslateAnimateHelper.get(child);
            canInit = false;
        }
        return super.layoutDependsOn(parent, child, dependency);
    }


    @Override
    protected void onNestPreScrollInit(View child) {
        if (isFirstMove) {
            isFirstMove = false;
            mAnimateHelper.setStartY(child.getY());
            mAnimateHelper.setMode(TranslateAnimateHelper.MODE_TITLE);
        }
    }
}

