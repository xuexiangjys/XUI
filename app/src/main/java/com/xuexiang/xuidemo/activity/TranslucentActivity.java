package com.xuexiang.xuidemo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.xuexiang.xui.utils.StatusBarUtils;
import com.xuexiang.xui.widget.banner.widget.banner.SimpleImageBanner;
import com.xuexiang.xui.widget.banner.widget.banner.base.BaseBanner;
import com.xuexiang.xuidemo.DemoDataProvider;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xutil.tip.ToastUtils;

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
                .setOnItemClickL(new BaseBanner.OnItemClickL() {
                    @Override
                    public void onItemClick(int position) {
                        ToastUtils.toast("position--->" + position);
                    }
                }).startScroll();

    }


    @Override
    protected void onDestroy() {
        sibSimpleUsage.recycle();
        super.onDestroy();
    }
}
