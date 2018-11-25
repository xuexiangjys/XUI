package com.xuexiang.xui.widget.banner.widget.banner;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.xuexiang.xui.R;
import com.xuexiang.xui.widget.banner.widget.banner.base.BaseIndicatorBanner;

/**
 * 简单的文字轮播
 *
 * @author xuexiang
 * @date 2017/10/16 上午9:47
 */
public class SimpleTextBanner extends BaseIndicatorBanner<String, SimpleTextBanner> {
    public SimpleTextBanner(Context context) {
        super(context);
    }

    public SimpleTextBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SimpleTextBanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onTitleSelect(TextView tv, int position) {
    }

    @Override
    public View onCreateItemView(int position) {
        View inflate = View.inflate(mContext, R.layout.xui_adapter_simple_text, null);
        TextView tv = inflate.findViewById(R.id.tv);
        tv.setText(mDatas.get(position));
        return inflate;
    }
}
