package com.xuexiang.xuidemo.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.xuexiang.xui.widget.flowlayout.BaseTagAdapter;
import com.xuexiang.xuidemo.R;

/**
 * @author xuexiang
 * @date 2017/11/21 上午10:44
 */
public class FlowTagAdapter extends BaseTagAdapter<String, TextView> {

    public FlowTagAdapter(Context context) {
        super(context);
    }

    @Override
    protected TextView newViewHolder(View convertView) {
        return (TextView) convertView.findViewById(R.id.tv_tag);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.adapter_tag_item;
    }

    @Override
    protected void convert(TextView textView, String item, int position) {
        textView.setText(item);
    }
}
