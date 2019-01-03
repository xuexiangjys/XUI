/*
 * Copyright (C) 2019 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xuexiang.xui.widget.grouplist;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xuexiang.xui.R;
import com.xuexiang.xui.utils.Utils;

/**
 * 用作通用列表 {@link XUIGroupListView} 里每个 {@link XUIGroupListView.Section} 的头部或尾部，也可单独使用。
 *
 * @author xuexiang
 * @since 2019/1/3 上午10:48
 */
public class XUIGroupListSectionHeaderFooterView extends LinearLayout {

    private TextView mTextView;

    public XUIGroupListSectionHeaderFooterView(Context context) {
        this(context, null, R.attr.XUIGroupListSectionViewStyle);
    }

    public XUIGroupListSectionHeaderFooterView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.XUIGroupListSectionViewStyle);
    }

    public XUIGroupListSectionHeaderFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public XUIGroupListSectionHeaderFooterView(Context context, CharSequence titleText) {
        this(context);
        setText(titleText);
    }

    public XUIGroupListSectionHeaderFooterView(Context context, CharSequence titleText, boolean isFooter) {
        this(context);
        if (isFooter) {
            // Footer View 不需要 padding bottom
            setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), 0);
        }
        setText(titleText);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.xui_layout_group_list_section, this, true);
        setGravity(Gravity.BOTTOM);

        mTextView = findViewById(R.id.group_list_section_header_textView);
    }

    public void setText(CharSequence text) {
        if (Utils.isNullOrEmpty(text)) {
            mTextView.setVisibility(GONE);
        } else {
            mTextView.setVisibility(VISIBLE);
        }
        mTextView.setText(text);
    }

    public TextView getTextView() {
        return mTextView;
    }

    public void setTextGravity(int gravity) {
        mTextView.setGravity(gravity);
    }
}
