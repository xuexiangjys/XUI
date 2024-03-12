package com.xuexiang.xuidemo.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.xuexiang.xui.utils.StatusBarUtils;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.banner.widget.banner.SimpleImageBanner;
import com.xuexiang.xuidemo.DemoDataProvider;
import com.xuexiang.xuidemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 沉浸式状态栏
 *
 * @author xuexiang
 * @since 2019/4/9 上午12:00
 */
public class TranslucentActivity extends AppCompatActivity {

    @BindView(R.id.sib_simple_usage)
    SimpleImageBanner sibSimpleUsage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置沉浸式状态栏
        StatusBarUtils.translucent(this);
        setContentView(R.layout.activity_translucent);
        ButterKnife.bind(this);

        sibSimpleUsage.setSource(DemoDataProvider.getBannerList())
                .setOnItemClickListener((view, item, position) -> XToastUtils.toast("headBanner position--->" + position)).startScroll();

    }


    @Override
    protected void onDestroy() {
        sibSimpleUsage.recycle();
        super.onDestroy();
    }
}
