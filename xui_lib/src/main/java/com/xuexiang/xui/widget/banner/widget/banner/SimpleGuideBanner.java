package com.xuexiang.xui.widget.banner.widget.banner;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xuexiang.xui.R;
import com.xuexiang.xui.widget.banner.widget.banner.base.BaseIndicatorBanner;
import com.xuexiang.xui.widget.imageview.ImageLoader;

/**
 * 简单的引导页
 *
 * @author xuexiang
 * @since 2018/12/6 下午4:32
 */
public class SimpleGuideBanner extends BaseIndicatorBanner<Object, SimpleGuideBanner> {

    public SimpleGuideBanner(Context context) {
        super(context);
        onCreateBanner();
    }

    public SimpleGuideBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
        onCreateBanner();
    }

    public SimpleGuideBanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        onCreateBanner();
    }

    protected void onCreateBanner() {
        setBarShowWhenLast(true);
        //不进行自动滚动
        setAutoScrollEnable(false);
    }

    @Override
    public View onCreateItemView(int position) {
        View inflate = View.inflate(mContext, R.layout.xui_adapter_simple_guide, null);
        ImageView iv = inflate.findViewById(R.id.iv);
        TextView tvJump = inflate.findViewById(R.id.tv_jump);
        TextView tvStart = inflate.findViewById(R.id.tv_start);

        final Object resId = mDatas.get(position);
        tvJump.setVisibility(position == 0 ? VISIBLE : GONE);
        tvStart.setVisibility(position == mDatas.size() - 1 ? VISIBLE : GONE);
        ImageLoader.get().loadImage(iv, resId);

        tvJump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onJumpClickListener != null) {
                    onJumpClickListener.onJumpClick();
                }
            }
        });
        tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onJumpClickListener != null) {
                    onJumpClickListener.onJumpClick();
                }
            }
        });
        return inflate;
    }

    private OnJumpClickListener onJumpClickListener;

    /**
     * 点击跳过或者立即体验的监听
     */
    public interface OnJumpClickListener {
        /**
         * 跳过监听
         */
        void onJumpClick();
    }

    public void setOnJumpClickListener(OnJumpClickListener onJumpClickListener) {
        this.onJumpClickListener = onJumpClickListener;
    }

}
