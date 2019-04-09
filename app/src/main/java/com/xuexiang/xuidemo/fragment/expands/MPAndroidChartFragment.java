package com.xuexiang.xuidemo.fragment.expands;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.ComponentContainerFragment;
import com.xuexiang.xuidemo.fragment.expands.chart.BarChartFragment;
import com.xuexiang.xuidemo.fragment.expands.chart.LineChartFragment;
import com.xuexiang.xuidemo.fragment.expands.chart.PieChartFragment;
import com.xuexiang.xuidemo.fragment.expands.chart.RadarChartFragment;

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
}
