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

package com.xuexiang.xuidemo.fragment.expands.chart.echarts;

import android.view.View;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xuidemo.base.BaseSimpleListFragment;
import com.xuexiang.xuidemo.base.webview.XPageWebViewFragment;
import com.xuexiang.xuidemo.utils.Utils;

import java.util.List;

/**
 * @author xuexiang
 * @since 2019/5/27 10:38
 */
@Page(name = "ECharts\n非常丰富的web图表组件")
public class EChartsFragment extends BaseSimpleListFragment {

    @Override
    protected List<String> initSimpleData(List<String> lists) {
        lists.add("简单的ECharts使用（直接使用Js）");
        return lists;
    }

    @Override
    protected void onItemClick(int position) {
        switch(position) {
            case 0:
                XPageWebViewFragment.openUrl(this, "file:///android_asset/chart/src/index.html");
                break;
            default:
                break;
        }
    }


    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.addAction(new TitleBar.TextAction("官网") {
            @Override
            public void performAction(View view) {
                Utils.goWeb(getContext(), "https://echarts.baidu.com/index.html");
            }
        });
        return titleBar;
    }
}
