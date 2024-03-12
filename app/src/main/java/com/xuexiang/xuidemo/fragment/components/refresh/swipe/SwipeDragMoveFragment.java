package com.xuexiang.xuidemo.fragment.components.refresh.swipe;

import static com.xuexiang.xuidemo.adapter.swipe.SwipeDragTouchListAdapter.TYPE_GRID;
import static com.xuexiang.xuidemo.adapter.swipe.SwipeDragTouchListAdapter.TYPE_LIST;

import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.xuexiang.xaop.annotation.MemoryCache;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.utils.WidgetUtils;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.adapter.swipe.SwipeDragTouchListAdapter;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.yanzhenjie.recyclerview.touch.OnItemMoveListener;
import com.yanzhenjie.recyclerview.touch.OnItemStateChangedListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author xuexiang
 * @since 2019/4/6 上午11:59
 */
@Page(name ="SwipeItemMove\n侧滑删除和拖拽")
public class SwipeDragMoveFragment extends BaseFragment {

    @BindView(R.id.recycler_view)
    SwipeRecyclerView recyclerView;

    private TitleBar mTitleBar;
    private ImageView mAction;
    private SwipeDragTouchListAdapter mAdapter;
    private int mType = TYPE_LIST;


    @Override
    protected TitleBar initTitle() {
        mTitleBar = super.initTitle();
        mAction = (ImageView) mTitleBar.addAction(new TitleBar.ImageAction(R.drawable.ic_type_list) {
            @Override
            public void performAction(View view) {
                switchType();
                if (mType == TYPE_LIST) {
                    mAction.setImageResource(R.drawable.ic_type_grid);
                    WidgetUtils.initRecyclerView(recyclerView);
                } else {
                    mAction.setImageResource(R.drawable.ic_type_list);
                    WidgetUtils.initGridRecyclerView(recyclerView, 2);
                }
                mAdapter.setItemViewType(mType);
            }
        });
        return mTitleBar;
    }

    private void switchType() {
        if (mType == TYPE_LIST) {
            mType = TYPE_GRID;
        } else {
            mType = TYPE_LIST;
        }
    }

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_swipe_recycler_view;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        WidgetUtils.initRecyclerView(recyclerView);

        // 监听拖拽和侧滑删除，更新UI和数据源。
        recyclerView.setOnItemMoveListener(onItemMoveListener);
        // 监听Item的手指状态:拖拽、侧滑、松开。
        recyclerView.setOnItemStateChangedListener(mOnItemStateChangedListener);

        recyclerView.setAdapter(mAdapter = new SwipeDragTouchListAdapter(getDemoData(), mType, recyclerView));

        // 长按拖拽，默认关闭。
        recyclerView.setLongPressDragEnabled(true);
        // 滑动删除，默认关闭。
        recyclerView.setItemViewSwipeEnabled(true);
    }


    /**
     * Item的拖拽/侧滑删除时，手指状态发生变化监听。
     */
    private OnItemStateChangedListener mOnItemStateChangedListener = new OnItemStateChangedListener() {
        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            if (actionState == OnItemStateChangedListener.ACTION_STATE_DRAG) {
                mTitleBar.setSubTitle("状态：拖拽");
            } else if (actionState == OnItemStateChangedListener.ACTION_STATE_SWIPE) {
                mTitleBar.setSubTitle("状态：滑动删除");
            } else if (actionState == OnItemStateChangedListener.ACTION_STATE_IDLE) {
                mTitleBar.setSubTitle("状态：手指松开");
            }
        }
    };

    /**
     * 监听拖拽和侧滑删除，更新UI和数据源。
     */
    private OnItemMoveListener onItemMoveListener = new OnItemMoveListener() {
        @Override
        public boolean onItemMove(RecyclerView.ViewHolder srcHolder, RecyclerView.ViewHolder targetHolder) {
            return mAdapter.onMoveItem(srcHolder, targetHolder);
        }

        @Override
        public void onItemDismiss(RecyclerView.ViewHolder srcHolder) {
            int position = mAdapter.onRemoveItem(srcHolder);
            XToastUtils.toast("现在的第" + (position + 1) + "被删除。");
        }

    };


    @MemoryCache
    private List<String> getDemoData() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add("第" + i + "条数据");
        }
        return list;
    }
}
