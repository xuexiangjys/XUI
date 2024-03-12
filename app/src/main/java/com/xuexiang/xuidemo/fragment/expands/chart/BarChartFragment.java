package com.xuexiang.xuidemo.fragment.expands.chart;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xuidemo.base.ComponentContainerFragment;
import com.xuexiang.xuidemo.fragment.expands.chart.bar.BasicBarChartFragment;
import com.xuexiang.xuidemo.fragment.expands.chart.bar.HorizontalBarChartFragment;
import com.xuexiang.xuidemo.fragment.expands.chart.bar.SimpleBarChartFragment;

/**
 * @author xuexiang
 * @since 2019/4/10 上午12:00
 */
@Page(name = "BarChart\n柱状图")
public class BarChartFragment extends ComponentContainerFragment {
    /**
     * 获取页面的类集合[使用@Page注解进行注册的页面]
     *
     * @return
     */
    @Override
    protected Class[] getPagesClasses() {
        return new Class[] {
                BasicBarChartFragment.class,
                SimpleBarChartFragment.class,
                HorizontalBarChartFragment.class
        };
    }
}
