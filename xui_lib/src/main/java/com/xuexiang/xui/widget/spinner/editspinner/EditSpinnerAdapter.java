package com.xuexiang.xui.widget.spinner.editspinner;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.text.Html;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.xuexiang.xui.R;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 可编辑Spinner的适配器(默认可选项）
 *
 * @author xuexiang
 * @since 2019/1/14 下午10:10
 */
public class EditSpinnerAdapter extends BaseEditSpinnerAdapter implements EditSpinnerFilter {
    private Context mContext;
    /**
     * 默认可选项集合
     */
    private final List<String> mSpinnerData;
    /**
     * 输入后匹配相关联的选项（展示）
     */
    private final List<String> mDisplayData;
    private final int[] mIndexs;

    private int textColor;
    private float textSize;
    private int backgroundSelector;

    private boolean mIsFilterKey = false;

    public EditSpinnerAdapter(Context context, List<String> data) {
        mContext = context;
        mSpinnerData = data;
        mDisplayData = new ArrayList<>(data);
        mIndexs = new int[mSpinnerData.size()];
    }

    public EditSpinnerAdapter(Context context, String[] data) {
        mContext = context;
        mSpinnerData = new ArrayList<>();
        mSpinnerData.addAll(Arrays.asList(data));
        mDisplayData = new ArrayList<>(mSpinnerData);
        mIndexs = new int[mSpinnerData.size()];
    }

    @Override
    public EditSpinnerFilter getEditSpinnerFilter() {
        return this;
    }

    @Override
    public String getItemString(int position) {
        return mSpinnerData.get(mIndexs[position]);
    }

    @Override
    public int getCount() {
        return mDisplayData == null ? 0 : mDisplayData.size();
    }

    @Override
    public String getItem(int position) {
        return mDisplayData == null ? "" : mDisplayData.get(position) == null ? "" : mDisplayData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final TextView textView;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.ms_list_item, parent, false);
            textView = convertView.findViewById(R.id.tv_tinted_spinner);
            textView.setTextColor(textColor);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            if (backgroundSelector != 0) {
                textView.setBackgroundResource(backgroundSelector);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                Configuration config = mContext.getResources().getConfiguration();
                if (config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
                    textView.setTextDirection(View.TEXT_DIRECTION_RTL);
                }
            }
            convertView.setTag(new ViewHolder(textView));
        } else {
            textView = ((ViewHolder)convertView.getTag()).textView;
        }
        textView.setText(Html.fromHtml(getItem(position)));
        return textView;
    }

    @Override
    public boolean onFilter(String keyword) {
        mDisplayData.clear();
        if (TextUtils.isEmpty(keyword)) {
            mDisplayData.addAll(mSpinnerData);
            for (int i = 0; i < mIndexs.length; i++) {
                mIndexs[i] = i;
            }
        } else {
            try {
                StringBuilder builder = new StringBuilder();
                builder.append("[^\\s]*").append(keyword).append("[^\\s]*");
                for (int i = 0; i < mSpinnerData.size(); i++) {
                    if (mSpinnerData.get(i)
                            .replaceAll("\\s+", "|")
                            .matches(builder.toString())) {
                        mIndexs[mDisplayData.size()] = i;
                        if (mIsFilterKey) {
                            mDisplayData.add(mSpinnerData.get(i).replaceFirst(keyword, "<font color=\"#F15C58\">" + keyword + "</font>"));
                        } else {
                            mDisplayData.add(mSpinnerData.get(i));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        notifyDataSetChanged();
        return mDisplayData.size() <= 0;
    }

    public EditSpinnerAdapter setTextColor(@ColorInt int textColor) {
        this.textColor = textColor;
        return this;
    }

    public EditSpinnerAdapter setTextSize(float textSize) {
        this.textSize = textSize;
        return this;
    }

    public EditSpinnerAdapter setBackgroundSelector(@DrawableRes int backgroundSelector) {
        this.backgroundSelector = backgroundSelector;
        return this;
    }

    public EditSpinnerAdapter setIsFilterKey(boolean isFilterKey) {
        mIsFilterKey = isFilterKey;
        return this;
    }

    private static class ViewHolder {

        private TextView textView;

        private ViewHolder(TextView textView) {
            this.textView = textView;
        }
    }
}
