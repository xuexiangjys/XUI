/*
 * Copyright (C) 2019 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.xuexiang.xui.adapter.recyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.xuexiang.xui.utils.DensityUtils;

/**
 * 可自定义分割线样式
 *
 * @author XUE
 * @date 2017/9/10 15:24
 */
public class GridDividerItemDecoration extends RecyclerView.ItemDecoration {
    private static final int[] ATTRS = new int[]{
            android.R.attr.listDivider
    };
    private Drawable mDivider;
    private int mSpanCount;
    /**
     * 分割线宽度，默认为1dp
     */
    private int mDividerWidth;
    /**
     * 画笔
     */
    private Paint mPaint;

    public GridDividerItemDecoration(Context context, int spanCount) {
        mSpanCount = spanCount;
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
        if (mDivider != null) {
            mDividerWidth = mDivider.getIntrinsicWidth();
        } else {
            mDividerWidth = DensityUtils.dp2px(1);
        }
    }

    /**
     * 自定义分割线
     *
     * @param context      上下文
     * @param spanCount    一行的数量
     * @param dividerWidth 分割线的宽度
     */
    public GridDividerItemDecoration(Context context, int spanCount, int dividerWidth) {
        this(context, spanCount);
        mDividerWidth = dividerWidth;
    }

    /**
     * 自定义分割线
     *
     * @param context      上下文
     * @param spanCount    一行的数量
     * @param dividerWidth 分割线的宽度
     * @param dividerColor 分割线的颜色
     */
    public GridDividerItemDecoration(Context context, int spanCount, int dividerWidth, int dividerColor) {
        this(context, spanCount);
        mDividerWidth = dividerWidth;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(dividerColor);
        mPaint.setStyle(Paint.Style.FILL);
    }

    /**
     * 设置分割线
     *
     * @param divider 分割线
     * @return this
     */
    public GridDividerItemDecoration setDivider(@NonNull Drawable divider) {
        mDivider = divider;
        mDividerWidth = mDivider.getIntrinsicWidth();
        return this;
    }

    @Override
    public void onDraw(@NonNull Canvas canvas, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(canvas, parent, state);
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            int position = parent.getChildLayoutPosition(child);
            int column = (position + 1) % mSpanCount;
            if (column == 0) {
                column = mSpanCount;
            }
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin + Math.round(ViewCompat.getTranslationY(child));
            final int bottom = top + mDividerWidth;
            final int left = child.getRight() + params.rightMargin + Math.round(ViewCompat.getTranslationX(child));
            final int right = left + mDividerWidth;

            if (mPaint != null) {
                canvas.drawRect(child.getLeft(), top, right, bottom, mPaint);
            } else {
                if (mDivider != null) {
                    mDivider.setBounds(child.getLeft(), top, right, bottom);
                    mDivider.draw(canvas);
                }
            }

            if (column < mSpanCount) {
                if (mPaint != null) {
                    canvas.drawRect(left, child.getTop(), right, bottom, mPaint);
                } else {
                    if (mDivider != null) {
                        mDivider.setBounds(left, child.getTop(), right, bottom);
                        mDivider.draw(canvas);
                    }
                }
            }

        }
    }


    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildLayoutPosition(view);
        RecyclerView.Adapter adapter = parent.getAdapter();
        if (adapter != null) {
            int itemCount = parent.getAdapter().getItemCount();
            boolean isLastRow = isLastRow(parent, position, mSpanCount, itemCount);

            int eachWidth = (mSpanCount - 1) * mDividerWidth / mSpanCount;
            int dl = mDividerWidth - eachWidth;

            int left = position % mSpanCount * dl;
            int right = eachWidth - left;

            if (isLastRow) {
                outRect.set(left, 0, right, 0);
            } else {
                outRect.set(left, 0, right, mDividerWidth);
            }
        } else {
            if ((position + 1) % mSpanCount > 0) {
                outRect.set(0, 0, mDividerWidth, mDividerWidth);
            } else {
                outRect.set(0, 0, 0, mDividerWidth);
            }
        }
    }


    private boolean isLastRow(RecyclerView parent, int pos, int spanCount, int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            // childCount = childCount - childCount % spanCount;
            int lines = childCount % spanCount == 0 ? childCount / spanCount : childCount / spanCount + 1;
            return lines == pos / spanCount + 1;
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            // StaggeredGridLayoutManager 且纵向滚动
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                childCount = childCount - childCount % spanCount;
                // 如果是最后一行，则不需要绘制底部
                return pos >= childCount;
            } else {
                // 如果是最后一行，则不需要绘制底部
                return (pos + 1) % spanCount == 0;
            }
        }
        return false;
    }
}
