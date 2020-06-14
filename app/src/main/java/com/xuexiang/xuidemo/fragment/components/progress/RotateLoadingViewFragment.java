package com.xuexiang.xuidemo.fragment.components.progress;

import android.widget.Button;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.progress.loading.RotateLoadingView;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;

import butterknife.BindView;

/**
 *
 *
 * @author xuexiang
 * @since 2018/11/26 下午1:37
 */
@Page(name = "RotateLoadingView\n旋转加载控件")
public class RotateLoadingViewFragment extends BaseFragment {
    @BindView(R.id.auto_arc_loading)
    RotateLoadingView mAutoLoadingView;
    @BindView(R.id.arc_loading)
    RotateLoadingView mLoadingView;

    @BindView(R.id.btn_switch)
    Button mBtSwitch;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_rotate_loadingview;
    }

    @Override
    protected void initViews() {
    }

    @Override
    protected void initListeners() {
        mBtSwitch.setOnClickListener(v -> {
            if (mLoadingView.isStart()) {
                mBtSwitch.setText(R.string.tip_start);
                mLoadingView.stop();
                mAutoLoadingView.stop();
            } else {
                mBtSwitch.setText(R.string.tip_stop);
                mLoadingView.start();
                mAutoLoadingView.start();
            }
        });
    }


    @Override
    public void onDestroyView() {
        mAutoLoadingView.recycle();
        mLoadingView.recycle();
        super.onDestroyView();
    }
}
