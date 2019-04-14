package com.xuexiang.xuidemo.fragment.expands;

import android.view.View;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.ComponentContainerFragment;
import com.xuexiang.xuidemo.fragment.expands.chart.BarChartFragment;
import com.xuexiang.xuidemo.fragment.expands.chart.LineChartFragment;
import com.xuexiang.xuidemo.fragment.expands.chart.PieChartFragment;
import com.xuexiang.xuidemo.fragment.expands.chart.RadarChartFragment;
import com.xuexiang.xuidemo.utils.Utils;

/**
 * @author xuexiang
 * @since 2019/4/9 下午11:50
 */
@Page(name = "图表", extra = R.drawable.ic_expand_chart)
public class MPAndroidChartFragment extends ComponentContainerFragment {
    /**
     * 获取页面的类集合[使用@Page注解进行注册的页面]
     *
     * @return
     */
    @Override
    protected Class[] getPagesClasses() {
        return new Class[]{
                LineChartFragment.class,
                BarChartFragment.class,
                PieChartFragment.class,
                RadarChartFragment.class
        };
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.addAction(new TitleBar.TextAction("Github") {
            @Override
            public void performAction(View view) {
                Utils.goWeb(getContext(), "https://github.com/PhilJay/MPAndroidChart");
            }
        });
        return titleBar;
    }
}
