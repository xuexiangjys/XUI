package com.xuexiang.xuidemo.adapter.swipe;

import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xuidemo.R;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.Collections;
import java.util.List;

/**
 * 拖拽适配器
 *
 * @author xuexiang
 * @since 2019/4/6 下午12:18
 */
public class SwipeDragTouchListAdapter extends BaseRecyclerAdapter<String> {

    private SwipeRecyclerView mMenuRecyclerView;

    public SwipeDragTouchListAdapter(List<String> list, SwipeRecyclerView recyclerView) {
        super(list);
        mMenuRecyclerView = recyclerView;
    }

    /**
     * 适配的布局
     *
     * @param viewType
     * @return
     */
    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.adapter_drag_touch_list_item;
    }

    /**
     * 绑定数据
     *
     * @param holder
     * @param position
     * @param item
     */
    @Override
    public void bindData(final RecyclerViewHolder holder, int position, String item) {
        holder.text(R.id.tv_title, item);

        holder.findViewById(R.id.iv_touch).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mMenuRecyclerView.startDrag(holder);
                }
                return false;
            }
        });
    }

    /**
     * 拖拽移动
     *
     * @param srcHolder
     * @param targetHolder
     */
    public boolean onMoveItem(RecyclerView.ViewHolder srcHolder, RecyclerView.ViewHolder targetHolder) {
        // 不同的ViewType不能拖拽换位置。
        if (srcHolder.getItemViewType() != targetHolder.getItemViewType()) {
            return false;
        }
        int fromPosition = srcHolder.getAdapterPosition();
        int toPosition = targetHolder.getAdapterPosition();

        Collections.swap(mData, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        // 返回true表示处理了并可以换位置，返回false表示你没有处理并不能换位置。
        return true;
    }

    /**
     * 侧滑删除
     *
     * @param srcHolder
     */
    public int onRemoveItem(RecyclerView.ViewHolder srcHolder) {
        int position = srcHolder.getAdapterPosition();
        delete(position);
        return position;
    }


}
