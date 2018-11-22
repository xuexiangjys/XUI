package com.xuexiang.xuidemo.base;

import android.view.View;
import android.widget.AdapterView;

import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.base.XPageContainerListFragment;
import com.xuexiang.xui.utils.KeyboardUtils;

/**
 * 解决输入法内存泄漏
 *
 * @author xuexiang
 * @since 2018/11/22 上午11:26
 */
public abstract class ComponentContainerFragment extends XPageContainerListFragment {


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        onItemClick(view, position);
    }

    @SingleClick
    private void onItemClick(View view, int position) {
        onItemClick(position);
    }

    @Override
    public void onDestroyView() {
        KeyboardUtils.fixSoftInputLeaks(getContext());
        super.onDestroyView();
    }
}
