package com.xuexiang.xui.widget.flowlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;

import com.xuexiang.xui.R;
import com.xuexiang.xui.utils.ResUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 流式标签布局
 *
 * @author xuexiang
 * @since 2018/5/28 下午7:40
 */
public class FlowTagLayout extends ViewGroup {
    /**
     * FlowLayout not support checked
     */
    public static final int FLOW_TAG_CHECKED_NONE = 0;
    /**
     * FlowLayout support single-select
     */
    public static final int FLOW_TAG_CHECKED_SINGLE = 1;
    /**
     * FlowLayout support multi-select
     */
    public static final int FLOW_TAG_CHECKED_MULTI = 2;
    /**
     * FlowLayout support display
     */
    public static final int FLOW_TAG_DISPLAY = 3;

    /**
     * Should be used by subclasses to listen to changes in the dataset
     */
    AdapterDataSetObserver mDataSetObserver;

    /**
     * The adapter containing the data to be displayed by this view
     */
    BaseTagAdapter mAdapter;

    /**
     * the tag click event callback
     */
    OnTagClickListener mOnTagClickListener;

    /**
     * the tag select event callback
     */
    OnTagSelectListener mOnTagSelectListener;

    /**
     * 标签流式布局选中模式，默认是不支持选中的
     */
    private int mTagCheckMode = FLOW_TAG_CHECKED_NONE;
    /**
     * 单选模式下点击是否可取消选中,默认为false不可取消
     */
    private boolean mSingleCancelable;

    /**
     * 存储选中的tag
     */
    private SparseBooleanArray mCheckedTagArray = new SparseBooleanArray();
    /**
     * 子View的宽度，如果为0 则为warp_content
     */
    private int mWidth;

    public FlowTagLayout(Context context) {
        super(context);
    }

    public FlowTagLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    public FlowTagLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        if (isInEditMode()) {
            return;
        }

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FlowTagLayout);
        mTagCheckMode = typedArray.getInt(R.styleable.FlowTagLayout_ftl_check_mode, FLOW_TAG_CHECKED_NONE);
        mSingleCancelable = typedArray.getBoolean(R.styleable.FlowTagLayout_ftl_single_cancelable, false);
        int entriesID = typedArray.getResourceId(R.styleable.FlowTagLayout_ftl_entries, 0);
        if (entriesID != 0) {
            BaseTagAdapter tagAdapter = setItems(ResUtils.getStringArray(entriesID));
            int selectedIDs = typedArray.getResourceId(R.styleable.FlowTagLayout_ftl_selecteds, 0);
            if (selectedIDs != 0) {
                tagAdapter.setSelectedPositions(ResUtils.getIntArray(selectedIDs));
            }
        }
        typedArray.recycle();
    }

    /**
     * 设置单选模式下点击是否可取消选中
     *
     * @param singleCancelable
     * @return
     */
    public FlowTagLayout setSingleCancelable(boolean singleCancelable) {
        mSingleCancelable = singleCancelable;
        return this;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //获取Padding
        // 获得它的父容器为它设置的测量模式和大小
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        //FlowLayout最终的宽度和高度值
        int resultWidth = 0;
        int resultHeight = 0;

        //测量时每一行的宽度
        int lineWidth = 0;
        //测量时每一行的高度，加起来就是FlowLayout的高度
        int lineHeight = 0;

        //遍历每个子元素
        for (int i = 0, childCount = getChildCount(); i < childCount; i++) {
            View childView = getChildAt(i);
            //测量每一个子view的宽和高
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);

            //获取到测量的宽和高
            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();

            //因为子View可能设置margin，这里要加上margin的距离
            MarginLayoutParams mlp = (MarginLayoutParams) childView.getLayoutParams();
            int realChildWidth = childWidth + mlp.leftMargin + mlp.rightMargin;
            int realChildHeight = childHeight + mlp.topMargin + mlp.bottomMargin;

            //如果当前一行的宽度加上要加入的子view的宽度大于父容器给的宽度，就换行
            if ((lineWidth + realChildWidth) > sizeWidth) {
                //换行
                resultWidth = Math.max(lineWidth, realChildWidth);
                resultHeight += realChildHeight;
                //换行了，lineWidth和lineHeight重新算
                lineWidth = realChildWidth;
                lineHeight = realChildHeight;
            } else {
                //不换行，直接相加
                lineWidth += realChildWidth;
                //每一行的高度取二者最大值
                lineHeight = Math.max(lineHeight, realChildHeight);
            }

            //遍历到最后一个的时候，肯定走的是不换行
            if (i == childCount - 1) {
                resultWidth = Math.max(lineWidth, resultWidth);
                resultHeight += lineHeight;
            }

            setMeasuredDimension(modeWidth == MeasureSpec.EXACTLY ? sizeWidth : resultWidth,
                    modeHeight == MeasureSpec.EXACTLY ? sizeHeight : resultHeight);

        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int flowWidth = getWidth();

        int childLeft = 0;
        int childTop = 0;

        //遍历子控件，记录每个子view的位置
        for (int i = 0, childCount = getChildCount(); i < childCount; i++) {
            View childView = getChildAt(i);

            //跳过View.GONE的子View
            if (childView.getVisibility() == View.GONE) {
                continue;
            }

            //获取到测量的宽和高
            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();

            //因为子View可能设置margin，这里要加上margin的距离
            MarginLayoutParams mlp = (MarginLayoutParams) childView.getLayoutParams();

            if (childLeft + mlp.leftMargin + childWidth + mlp.rightMargin > flowWidth) {
                //换行处理
                childTop += (mlp.topMargin + childHeight + mlp.bottomMargin);
                childLeft = 0;
            }
            //布局
            int left = childLeft + mlp.leftMargin;
            int top = childTop + mlp.topMargin;
            int right = childLeft + mlp.leftMargin + childWidth;
            int bottom = childTop + mlp.topMargin + childHeight;
            childView.layout(left, top, right, bottom);

            childLeft += (mlp.leftMargin + childWidth + mlp.rightMargin);
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    public BaseTagAdapter getAdapter() {
        return mAdapter;
    }

    /**
     * 像ListView、GridView一样使用FlowLayout
     *
     * @param adapter
     */
    public FlowTagLayout setAdapter(BaseTagAdapter adapter) {
        if (mAdapter != null && mDataSetObserver != null) {
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
        }

        //清除现有的数据
        removeAllViews();
        mAdapter = adapter;

        if (mAdapter != null) {
            mDataSetObserver = new AdapterDataSetObserver();
            mAdapter.registerDataSetObserver(mDataSetObserver);
        }
        return this;
    }

    /**
     * 增加标签数据
     *
     * @param data
     */
    public <T> FlowTagLayout addTag(T data) {
        if (mAdapter != null) {
            mAdapter.addTag(data);
        }
        return this;
    }

    /**
     * 增加标签数据
     *
     * @param datas
     */
    public <T> FlowTagLayout addTags(List<T> datas) {
        if (mAdapter != null) {
            mAdapter.addTags(datas);
        }
        return this;
    }

    /**
     * 增加标签数据
     *
     * @param datas
     */
    public <T> FlowTagLayout addTags(T[] datas) {
        if (mAdapter != null) {
            mAdapter.addTags(datas);
        }
        return this;
    }

    /**
     * 清除并增加标签数据
     *
     * @param datas
     */
    public <T> FlowTagLayout clearAndAddTags(List<T> datas) {
        if (mAdapter != null) {
            mAdapter.clearAndAddTags(datas);
        }
        return this;
    }

    /**
     * 清除标签数据
     */
    public <T> FlowTagLayout clearTags() {
        if (mAdapter != null) {
            mAdapter.clearData();
        }
        return this;
    }

    /**
     * 子View个数
     *
     * @param width
     */
    public FlowTagLayout setChildWidth(int width) {
        mWidth = width;
        return this;
    }

    /**
     * 重新加载刷新数据
     */
    private void reloadData() {
        removeAllViews();

        MarginLayoutParams mMarginLayoutParams = new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        if (mWidth != 0) {
            mMarginLayoutParams.width = mWidth;
        }
        boolean isSetted = false;
        for (int i = 0; i < mAdapter.getCount(); i++) {
            final int index = i;
            mCheckedTagArray.put(i, false);
            final View childView = mAdapter.getView(i, null, this);
            addView(childView, mMarginLayoutParams);

            if (mAdapter instanceof OnInitSelectedPosition) {
                boolean isSelected = mAdapter.isSelectedPosition(i);
                //判断一下模式
                if (mTagCheckMode == FLOW_TAG_CHECKED_SINGLE) {
                    //单选只有第一个起作用
                    if (isSelected && !isSetted) {
                        mCheckedTagArray.put(i, true);
                        childView.setSelected(true);
                        isSetted = true;
                    }
                } else if (mTagCheckMode == FLOW_TAG_CHECKED_MULTI) {
                    if (isSelected) {
                        mCheckedTagArray.put(i, true);
                        childView.setSelected(true);
                    }
                } else if (mTagCheckMode == FLOW_TAG_DISPLAY) {
                    //不可点击
                    mCheckedTagArray.put(i, true);
                    childView.setSelected(true);
                    childView.setEnabled(false);
                }
            }
            //重新加载数据，点击索引清空
            setSelectedIndexs(null);
            setChildViewClickListener(index, childView);
        }
    }

    /**
     * 设置子控件的点击监听
     *
     * @param index
     * @param childView
     */
    private void setChildViewClickListener(final int index, final View childView) {
        childView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTagCheckMode == FLOW_TAG_CHECKED_NONE) {
                    if (mOnTagClickListener != null) {
                        mOnTagClickListener.onItemClick(FlowTagLayout.this, childView, index);
                    }
                } else if (mTagCheckMode == FLOW_TAG_CHECKED_SINGLE) {
                    //判断状态
                    if (mCheckedTagArray.get(index)) {
                        if (mSingleCancelable) {
                            //更新点击状态
                            mCheckedTagArray.put(index, false);
                            childView.setSelected(false);
                            setSelectedIndexs(null);
                            if (mOnTagSelectListener != null) {
                                mOnTagSelectListener.onItemSelect(FlowTagLayout.this, index, new ArrayList<Integer>());
                            }
                        }
                    } else {
                        //更新全部状态为fasle
                        for (int k = 0; k < mAdapter.getCount(); k++) {
                            mCheckedTagArray.put(k, false);
                            getChildAt(k).setSelected(false);
                        }

                        //更新点击状态
                        mCheckedTagArray.put(index, true);
                        childView.setSelected(true);
                        setSelectedIndexs(Arrays.asList(index));
                        if (mOnTagSelectListener != null) {
                            mOnTagSelectListener.onItemSelect(FlowTagLayout.this, index, Arrays.asList(index));
                        }
                    }
                } else if (mTagCheckMode == FLOW_TAG_CHECKED_MULTI) {
                    if (mCheckedTagArray.get(index)) {
                        mCheckedTagArray.put(index, false);
                        childView.setSelected(false);
                    } else {
                        mCheckedTagArray.put(index, true);
                        childView.setSelected(true);
                    }
                    //回调
                    List<Integer> list = new ArrayList<>();
                    for (int k = 0; k < mAdapter.getCount(); k++) {
                        if (mCheckedTagArray.get(k)) {
                            list.add(k);
                        }
                    }
                    setSelectedIndexs(list);
                    if (mOnTagSelectListener != null) {
                        mOnTagSelectListener.onItemSelect(FlowTagLayout.this, index, list);
                    }
                }
            }
        });
    }

    public FlowTagLayout setOnTagClickListener(OnTagClickListener onTagClickListener) {
        mOnTagClickListener = onTagClickListener;
        return this;
    }

    public FlowTagLayout setOnTagSelectListener(OnTagSelectListener onTagSelectListener) {
        mOnTagSelectListener = onTagSelectListener;
        return this;
    }

    /**
     * 获取标签模式
     *
     * @return
     */
    public int getTagCheckMode() {
        return mTagCheckMode;
    }

    /**
     * 设置标签选中模式
     *
     * @param tagMode
     */
    public FlowTagLayout setTagCheckedMode(int tagMode) {
        mTagCheckMode = tagMode;
        return this;
    }

    private FlowTagLayout setSelectedIndexs(List<Integer> selectedIndexs) {
        if (mAdapter != null) {
            mAdapter.setSelectedIndexs(selectedIndexs);
        }
        return this;
    }

    /**
     * 获取选中索引的集合
     *
     * @return
     */
    @Nullable
    public List<Integer> getSelectedIndexs() {
        if (mAdapter != null) {
            mAdapter.getSelectedIndexs();
        }
        return null;
    }

    /**
     * 获取选中索引
     *
     * @return
     */
    public int getSelectedIndex() {
        if (mAdapter != null) {
            return mAdapter.getSelectedIndex();
        }
        return -1;
    }

    /**
     * 获取选中索引
     *
     * @return
     */
    @Nullable
    public <T> T getSelectedItem() {
        if (mAdapter != null) {
            return (T) mAdapter.getSelectedItem();
        }
        return null;
    }

    /**
     * 设置默认的流布局内容
     *
     * @param items A list of items
     */
    public <T> BaseTagAdapter setItems(@NonNull T... items) {
        return setItems(Arrays.asList(items));
    }

    /**
     * 设置默认的流布局内容
     *
     * @param items A list of items
     */
    public <T> BaseTagAdapter setItems(@NonNull List<T> items) {
        if (mAdapter != null) {
            mAdapter.clearAndAddTags(items);
        } else {
            BaseTagAdapter tagAdapter = new DefaultFlowTagAdapter(getContext());
            setAdapter(tagAdapter);
            tagAdapter.addTags(items);
        }
        return mAdapter;
    }

    /**
     * 设置初始化选中的标签索引
     *
     * @param ps
     * @return
     */
    public FlowTagLayout setSelectedPositions(Integer... ps) {
        if (mAdapter != null) {
            mAdapter.setSelectedPositions(ps);
        }
        return this;
    }

    /**
     * 设置初始化选中的标签索引
     *
     * @param ps
     * @return
     */
    public FlowTagLayout setSelectedPositions(List<Integer> ps) {
        if (mAdapter != null) {
            mAdapter.setSelectedPositions(ps);
        }
        return this;
    }

    /**
     * 设置初始化选中的标签索引
     *
     * @param ps
     * @return
     */
    public FlowTagLayout setSelectedPositions(int[] ps) {
        if (mAdapter != null) {
            mAdapter.setSelectedPositions(ps);
        }
        return this;
    }


    /**
     * 设置默认选中的内容
     *
     * @param selectedItems 选中的内容集合
     * @return
     */
    public <T> FlowTagLayout setSelectedItems(T... selectedItems) {
        setSelectedItems(Arrays.asList(selectedItems));
        return this;
    }

    /**
     * 设置默认选中的内容
     *
     * @param selectedItems 选中的内容集合
     * @return
     */
    public <T> FlowTagLayout setSelectedItems(List<T> selectedItems) {
        if (mTagCheckMode != FLOW_TAG_CHECKED_NONE) {
            if (mAdapter != null) {
                mAdapter.setSelectedPositions(getSelectedPositions(selectedItems, mAdapter.getItems()));
            }
        }
        return this;
    }

    /**
     * 获取选中内容在流布局中的索引位置集合
     *
     * @param selectedItems 选中的内容集合
     * @param items         流布局中选项的集合
     * @return
     */
    private <T> List<Integer> getSelectedPositions(List<T> selectedItems, List<T> items) {
        List<Integer> positions = new ArrayList<>();
        if (!isListEmpty(selectedItems) && !isListEmpty(items)) {
            for (int i = 0; i < selectedItems.size(); i++) {
                for (int j = 0; j < items.size(); j++) {
                    if (items.get(j).equals(selectedItems.get(i))) {
                        positions.add(j);
                        break;
                    }
                }
            }
        }
        return positions;
    }

    /**
     * 集合是否为空
     *
     * @param list
     * @param <T>
     * @return
     */
    private <T> boolean isListEmpty(List<T> list) {
        return list == null || list.isEmpty();
    }

    /**
     * 初始化选择
     */
    public interface OnInitSelectedPosition {
        /**
         * @param position 位置
         * @return
         */
        boolean isSelectedPosition(int position);
    }

    /**
     * 点击的监听
     */
    public interface OnTagClickListener {
        /**
         * 当标签被点击
         *
         * @param parent   流布局
         * @param view     被点击的标签
         * @param position 被点击控件的位置
         */
        void onItemClick(FlowTagLayout parent, View view, int position);
    }

    /**
     * 选择的监听
     */
    public interface OnTagSelectListener {
        /**
         * 当标签被选中
         *
         * @param parent       流布局
         * @param position     位置
         * @param selectedList 选中内容的集合
         */
        void onItemSelect(FlowTagLayout parent, int position, List<Integer> selectedList);
    }

    class AdapterDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            super.onChanged();
            reloadData();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
        }
    }
}
