package com.xuexiang.xui.widget.flowlayout;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.xuexiang.xui.R;


/**
 * 默认流标签布局适配器
 * @author xuexiang
 * @date 2017/11/21 上午10:44
 */
public class DefaultFlowTagAdapter extends BaseTagAdapter<String, TextView> {

    public DefaultFlowTagAdapter(Context context) {
        super(context);
    }

    @Override
    protected TextView newViewHolder(View convertView) {
        return (TextView) convertView.findViewById(R.id.tv_tag_item);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.xui_adapter_default_flow_tag_item;
    }

    @Override
    protected void convert(TextView textView, String item, int position) {
        textView.setText(item);
    }
}
