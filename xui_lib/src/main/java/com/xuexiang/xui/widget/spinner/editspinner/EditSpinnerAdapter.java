package com.xuexiang.xui.widget.spinner.editspinner;

import android.content.res.Configuration;
import android.os.Build;
import android.text.Html;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.xuexiang.xui.R;

import java.util.List;

/**
 * 可编辑Spinner的适配器(默认可选项）
 *
 * @author xuexiang
 * @since 2019/1/14 下午10:10
 */
public class EditSpinnerAdapter<T> extends BaseEditSpinnerAdapter<T> implements EditSpinnerFilter {

    /**
     * 选项的文字颜色
     */
    private int mTextColor;
    /**
     * 选项的文字大小
     */
    private float mTextSize;
    /**
     * 背景颜色
     */
    private int mBackgroundSelector;
    /**
     * 过滤关键词的选中颜色
     */
    private String mFilterColor = "#F15C58";

    private boolean mIsFilterKey = false;

    /**
     * 构造方法
     *
     * @param data 选项数据
     */
    public EditSpinnerAdapter(List<T> data) {
        super(data);
    }

    /**
     * 构造方法
     *
     * @param data 选项数据
     */
    public EditSpinnerAdapter(T[] data) {
        super(data);
    }

    @Override
    public EditSpinnerFilter getEditSpinnerFilter() {
        return this;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ms_layout_list_item, parent, false);
            holder = new ViewHolder(convertView, mTextColor, mTextSize, mBackgroundSelector);
            convertView.setTag(holder);
        } else {
            holder = ((ViewHolder) convertView.getTag());
        }
        holder.mTextView.setText(Html.fromHtml(getItem(position)));
        return convertView;
    }

    @Override
    public boolean onFilter(String keyword) {
        mDisplayData.clear();
        if (TextUtils.isEmpty(keyword)) {
            initDisplayData(mDataSource);
            for (int i = 0; i < mIndexs.length; i++) {
                mIndexs[i] = i;
            }
        } else {
            try {
                StringBuilder builder = new StringBuilder();
                builder.append("[^\\s]*").append(keyword).append("[^\\s]*");
                for (int i = 0; i < mDataSource.size(); i++) {
                    if (getDataSourceString(i)
                            .replaceAll("\\s+", "|")
                            .matches(builder.toString())) {
                        mIndexs[mDisplayData.size()] = i;
                        if (mIsFilterKey) {
                            mDisplayData.add(getDataSourceString(i).replaceFirst(keyword, "<font color=\"" + mFilterColor + "\">" + keyword + "</font>"));
                        } else {
                            mDisplayData.add(getDataSourceString(i));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        notifyDataSetChanged();
        return mDisplayData.size() > 0;
    }

    public EditSpinnerAdapter setTextColor(@ColorInt int textColor) {
        mTextColor = textColor;
        return this;
    }

    public EditSpinnerAdapter setTextSize(float textSize) {
        mTextSize = textSize;
        return this;
    }

    public EditSpinnerAdapter setBackgroundSelector(@DrawableRes int backgroundSelector) {
        mBackgroundSelector = backgroundSelector;
        return this;
    }

    public EditSpinnerAdapter setFilterColor(String filterColor) {
        mFilterColor = filterColor;
        return this;
    }

    public EditSpinnerAdapter setIsFilterKey(boolean isFilterKey) {
        mIsFilterKey = isFilterKey;
        return this;
    }

    private static class ViewHolder {

        private TextView mTextView;

        private ViewHolder(@NonNull View convertView, @ColorInt int textColor, float textSize, @DrawableRes int backgroundSelector) {
            mTextView = convertView.findViewById(R.id.tv_tinted_spinner);
            mTextView.setTextColor(textColor);
            mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            if (backgroundSelector != 0) {
                mTextView.setBackgroundResource(backgroundSelector);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                Configuration config = convertView.getResources().getConfiguration();
                if (config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
                    mTextView.setTextDirection(View.TEXT_DIRECTION_RTL);
                }
            }
        }
    }
}
