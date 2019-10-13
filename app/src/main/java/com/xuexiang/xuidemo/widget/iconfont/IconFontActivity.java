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

package com.xuexiang.xuidemo.widget.iconfont;

import android.os.Bundle;

import androidx.core.view.LayoutInflaterCompat;

import com.mikepenz.iconics.context.IconicsLayoutInflater;
import com.xuexiang.xpage.base.XPageActivity;
import com.xuexiang.xpage.base.XPageFragment;
import com.xuexiang.xpage.core.CoreSwitchBean;
import com.xuexiang.xui.widget.slideback.SlideBack;
import com.xuexiang.xuidemo.utils.Utils;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 字体图标容器Activity
 *
 * @author XUE
 * @since 2019/3/22 11:21
 */
public class IconFontActivity extends XPageActivity {

    //============================================================================================================================================================//
    /**
     * 是否支持侧滑返回
     */
    public static final String KEY_SUPPORT_SLIDE_BACK = "key_support_slide_back";

    Unbinder mUnbinder;

//    @Override
//    protected void attachBaseContext(Context newBase) {
//        //注入字体方法1
//        super.attachBaseContext(IconicsContextWrapper.wrap(newBase));
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.initTheme(this);
        //注入字体方法2,兼容性更好
        LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));
        super.onCreate(savedInstanceState);
        mUnbinder = ButterKnife.bind(this);

        // 侧滑回调
        if (isSupportSlideBack()) {
            SlideBack.with(this)
                    .haveScroll(true)
                    .callBack(this::popPage)
                    .register();
        }
    }

    /**
     * @return 是否支持侧滑返回
     */
    protected boolean isSupportSlideBack() {
        CoreSwitchBean page = getIntent().getParcelableExtra(CoreSwitchBean.KEY_SWITCH_BEAN);
        return page == null || page.getBundle() == null || page.getBundle().getBoolean(KEY_SUPPORT_SLIDE_BACK, true);
    }


    /**
     * 打开fragment
     *
     * @param clazz          页面类
     * @param addToBackStack 是否添加到栈中
     * @return 打开的fragment对象
     */
    public <T extends XPageFragment> T openPage(Class<T> clazz, boolean addToBackStack) {
        CoreSwitchBean page = new CoreSwitchBean(clazz)
                .setAddToBackStack(addToBackStack);
        return (T) openPage(page);
    }

    /**
     * 打开fragment
     *
     * @return 打开的fragment对象
     */
    public <T extends XPageFragment> T openNewPage(Class<T> clazz) {
        CoreSwitchBean page = new CoreSwitchBean(clazz)
                .setNewActivity(true);
        return (T) openPage(page);
    }


    /**
     * 切换fragment
     *
     * @param clazz 页面类
     * @return 打开的fragment对象
     */
    public <T extends XPageFragment> T switchPage(Class<T> clazz) {
        return changePage(clazz);
    }

    @Override
    protected void onRelease() {
        mUnbinder.unbind();
        if (isSupportSlideBack()) {
            SlideBack.unregister(this);
        }
        super.onRelease();
    }

}
