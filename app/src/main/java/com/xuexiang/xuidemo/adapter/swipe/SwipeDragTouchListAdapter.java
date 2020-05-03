package com.xuexiang.xuidemo.adapter.swipe;

import android.view.MotionEvent;

import androidx.annotation.NonNull;
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

    /**
     * 列表集合
     */
    public static final int TYPE_LIST = 1;
    /**
     * 网格集合
     */
    public static final int TYPE_GRID = 2;


    private SwipeRecyclerView mMenuRecyclerView;

    private int mItemViewType;

    public SwipeDragTouchListAdapter(List<String> list, int vewType, SwipeRecyclerView recyclerView) {
        super(list);
        mItemViewType = vewType;
        mMenuRecyclerView = recyclerView;
    }

    public SwipeDragTouchListAdapter setItemViewType(int itemViewType) {
        mItemViewType = itemViewType;
        notifyDataSetChanged();
        return this;
    }

    @Override
    public int getItemViewType(int position) {
        return mItemViewType;
    }

    /**
     * 适配的布局
     *
     * @param viewType
     * @return
     */
    @Override
    public int getItemLayoutId(int viewType) {
        if (viewType == TYPE_LIST) {
            return R.layout.adapter_drag_touch_list_item;
        } else {
            return R.layout.adapter_drag_touch_grid_item;
        }
    }

    /**
     * 绑定数据
     *
     * @param holder
     * @param position
     * @param item
     */
    @Override
    public void bindData(@NonNull final RecyclerViewHolder holder, int position, String item) {
        holder.text(R.id.tv_title, item);

        holder.findViewById(R.id.iv_touch).setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mMenuRecyclerView.startDrag(holder);
            }
            return false;
        });
    }

    /**
     * List拖拽移动
     *
     * @param srcHolder
     * @param targetHolder
     * @return
     */
    public boolean onMoveItem(RecyclerView.ViewHolder srcHolder, RecyclerView.ViewHolder targetHolder) {
       return mItemViewType == TYPE_LIST ? onMoveItemList(srcHolder, targetHolder) : onMoveItemGrid(srcHolder, targetHolder);
    }

    /**
     * List拖拽移动
     *
     * @param srcHolder
     * @param targetHolder
     */
    public boolean onMoveItemList(RecyclerView.ViewHolder srcHolder, RecyclerView.ViewHolder targetHolder) {
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
     * Grid样式的拖拽移动
     *
     * @param srcHolder
     * @param targetHolder
     */
    public boolean onMoveItemGrid(RecyclerView.ViewHolder srcHolder, RecyclerView.ViewHolder targetHolder) {
        // 不同的ViewType不能拖拽换位置。
        if (srcHolder.getItemViewType() != targetHolder.getItemViewType()) {
            return false;
        }

        // 真实的Position：通过ViewHolder拿到的position都需要减掉HeadView的数量。
        int fromPosition = srcHolder.getAdapterPosition();
        int toPosition = targetHolder.getAdapterPosition();

        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mData, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mData, i, i - 1);
            }
        }
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
