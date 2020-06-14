package com.xuexiang.xuidemo.fragment.components.imageview.pictureselector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.xuexiang.xuidemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author XUE
 * @since 2019/3/25 11:29
 */
public class ImageSelectGridAdapter extends RecyclerView.Adapter<ImageSelectGridAdapter.ViewHolder> {
    public static final int TYPE_CAMERA = 1;
    public static final int TYPE_PICTURE = 2;
    private LayoutInflater mInflater;
    private List<LocalMedia> mList = new ArrayList<>();
    private int mSelectMax = 9;
    /**
     * 点击添加图片跳转
     */
    private OnAddPicClickListener mOnAddPicClickListener;

    public interface OnAddPicClickListener {
        void onAddPicClick();
    }

    public ImageSelectGridAdapter(Context context, OnAddPicClickListener onAddPicClickListener) {
        mInflater = LayoutInflater.from(context);
        mOnAddPicClickListener = onAddPicClickListener;
    }

    public void setSelectMax(int selectMax) {
        this.mSelectMax = selectMax;
    }

    public void setSelectList(@NonNull List<LocalMedia> list) {
        mList = list;
    }

    public void update(@NonNull List<LocalMedia> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivSelectPic;
        LinearLayout llDelete;

        public ViewHolder(View view) {
            super(view);
            ivSelectPic = view.findViewById(R.id.iv_select_pic);
            llDelete = view.findViewById(R.id.ll_delete);
        }
    }

    @Override
    public int getItemCount() {
        if (mList.size() < mSelectMax) {
            return mList.size() + 1;
        } else {
            return mList.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowAddItem(position)) {
            return TYPE_CAMERA;
        } else {
            return TYPE_PICTURE;
        }
    }

    /**
     * 创建ViewHolder
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.adapter_select_image_grid_item, viewGroup, false);
        return new ViewHolder(view);
    }

    private boolean isShowAddItem(int position) {
        int size = mList.size();
        return position == size;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        //少于8张，显示继续添加的图标
        if (getItemViewType(position) == TYPE_CAMERA) {
            viewHolder.ivSelectPic.setImageResource(R.drawable.ic_add_image);
            viewHolder.ivSelectPic.setOnClickListener(v -> mOnAddPicClickListener.onAddPicClick());
            viewHolder.llDelete.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.llDelete.setVisibility(View.VISIBLE);
            viewHolder.llDelete.setOnClickListener(view -> {
                int index = viewHolder.getAdapterPosition();
                // 这里有时会返回-1造成数据下标越界,具体可参考getAdapterPosition()源码，
                // 通过源码分析应该是bindViewHolder()暂未绘制完成导致，知道原因的也可联系我~感谢
                if (index != RecyclerView.NO_POSITION) {
                    mList.remove(index);
                    notifyItemRemoved(index);
                    notifyItemRangeChanged(index, mList.size());
                }
            });
            LocalMedia media = mList.get(position);
            int mimeType = media.getMimeType();
            String path;
            if (media.isCut() && !media.isCompressed()) {
                // 裁剪过
                path = media.getCutPath();
            } else if (media.isCompressed() || (media.isCut() && media.isCompressed())) {
                // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
                path = media.getCompressPath();
            } else {
                // 原图
                path = media.getPath();
            }
            if (mimeType == PictureMimeType.ofAudio()) {
                viewHolder.ivSelectPic.setImageResource(R.drawable.pic_audio_placeholder);
            } else {
                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.color.color_f4)
                        .diskCacheStrategy(DiskCacheStrategy.ALL);
                Glide.with(viewHolder.itemView.getContext())
                        .load(path)
                        .apply(options)
                        .into(viewHolder.ivSelectPic);
            }
            //itemView 的点击事件
            if (mItemClickListener != null) {
                viewHolder.itemView.setOnClickListener(v -> {
                    int adapterPosition = viewHolder.getAdapterPosition();
                    mItemClickListener.onItemClick(adapterPosition, v);
                });
            }
        }
    }

    protected OnItemClickListener mItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position, View v);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }
}
