package com.xuexiang.xuidemo.fragment.expands.chart;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xuidemo.base.ComponentContainerFragment;
import com.xuexiang.xuidemo.fragment.expands.chart.pie.BasicPieChartFragment;
import com.xuexiang.xuidemo.fragment.expands.chart.pie.HalfPieChartFragment;

/**
 * @author xuexiang
 * @since 2019/4/10 上午12:03
 */
@Page(name = "PieChart\n饼图")
public class PieChartFragment extends ComponentContainerFragment {
    /**
     * 获取页面的类集合[使用@Page注解进行注册的页面]
     *
     * @return
     */
    @Override
    protected Class[] getPagesClasses() {
        return new Class[] {
                BasicPieChartFragment.class,
                HalfPieChartFragment.class
        };
    }
}
