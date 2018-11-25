package com.xuexiang.xui.widget.banner.widget.banner;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xuexiang.xui.R;
import com.xuexiang.xui.widget.banner.widget.banner.base.BaseIndicatorBanner;

/**
 * 简单的引导页
 *
 * @author xuexiang
 * @date 2017/10/16 上午9:47
 */
public class SimpleGuideBanner extends BaseIndicatorBanner<Integer, SimpleGuideBanner> {

    public SimpleGuideBanner(Context context) {
        super(context);
        setBarShowWhenLast(false);
    }

    public SimpleGuideBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBarShowWhenLast(false);
    }

    public SimpleGuideBanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setBarShowWhenLast(false);
    }

    @Override
    public View onCreateItemView(int position) {
        View inflate = View.inflate(mContext, R.layout.xui_adapter_simple_guide, null);
        ImageView iv = inflate.findViewById(R.id.iv);
        TextView tv_jump = inflate.findViewById(R.id.tv_jump);

        final Integer resId = mDatas.get(position);
        tv_jump.setVisibility(position == mDatas.size() - 1 ? VISIBLE : GONE);

        Glide.with(mContext)
                .load(resId)
                .into(iv);

        tv_jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onJumpClickL != null)
                    onJumpClickL.onJumpClick();
            }
        });

        return inflate;
    }

    private OnJumpClickL onJumpClickL;

    public interface OnJumpClickL {
        void onJumpClick();
    }

    public void setOnJumpClickL(OnJumpClickL onJumpClickL) {
        this.onJumpClickL = onJumpClickL;
    }
}
