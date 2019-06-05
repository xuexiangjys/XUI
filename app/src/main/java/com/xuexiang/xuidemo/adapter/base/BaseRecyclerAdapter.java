package com.xuexiang.xuidemo.adapter.base;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 基础适配器
 *
 * @author XUE
 * @date 2017/9/10 18:30
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder> {

    protected final List<T> mData = new ArrayList<>();
    private OnItemClickListener mClickListener;
    private OnItemLongClickListener mLongClickListener;

    /**
     * 当前点击的条目
     */
    private int mLastPosition = -1;

    public BaseRecyclerAdapter() {
        this(null);
    }

    public BaseRecyclerAdapter(List<T> list) {
        if (list != null) {
            mData.addAll(list);
        }
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final RecyclerViewHolder holder = new RecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(getItemLayoutId(viewType), parent, false));
        if (mClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onItemClick(holder.itemView, holder.getLayoutPosition());
                }
            });
        }
        if (mLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mLongClickListener.onItemLongClick(holder.itemView, holder.getLayoutPosition());
                    return true;
                }
            });
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        bindData(holder, position, mData.get(position));
    }

    public T getItem(int pos) {
        return mData.get(pos);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public BaseRecyclerAdapter add(int pos, T item) {
        mData.add(pos, item);
        notifyItemInserted(pos);
        return this;
    }

    public BaseRecyclerAdapter delete(int pos) {
        mData.remove(pos);
        notifyItemRemoved(pos);
        return this;
    }

    public BaseRecyclerAdapter<T> refresh(Collection<T> collection) {
        if (collection != null) {
            mData.clear();
            mData.addAll(collection);
            notifyDataSetChanged();
            mLastPosition = -1;
        }
        return this;
    }

    public BaseRecyclerAdapter<T> loadMore(Collection<T> collection) {
        if (collection != null) {
            mData.addAll(collection);
            notifyDataSetChanged();
        }
        return this;
    }

    public BaseRecyclerAdapter<T> load(T item) {
        if (item != null) {
            mData.add(item);
            notifyDataSetChanged();
        }
        return this;
    }

    public BaseRecyclerAdapter setOnItemClickListener(OnItemClickListener listener) {
        mClickListener = listener;
        return this;
    }

    public BaseRecyclerAdapter setOnItemLongClickListener(OnItemLongClickListener listener) {
        mLongClickListener = listener;
        return this;
    }

    /**
     * 适配的布局
     *
     * @param viewType
     * @return
     */
    abstract public int getItemLayoutId(int viewType);

    public int getLastPosition() {
        return mLastPosition;
    }

    public BaseRecyclerAdapter<T> setLastPosition(int lastPosition) {
        mLastPosition = lastPosition;
        return this;
    }

    /**
     * 绑定数据
     *
     * @param holder
     * @param position
     * @param item
     */
    abstract public void bindData(RecyclerViewHolder holder, int position, T item);

    public interface OnItemClickListener {
        /**
         * 条目点击
         *
         * @param itemView
         * @param pos
         */
        void onItemClick(View itemView, int pos);
    }

    public interface OnItemLongClickListener {
        /**
         * 条目长按
         *
         * @param itemView
         * @param pos
         */
        void onItemLongClick(View itemView, int pos);
    }
}
