/*
 * Copyright (C) 2020 xuexiangjys(xuexiangjys@163.com)
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

package com.xuexiang.xuidemo.fragment.components.pickerview;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.picker.XRangeSlider;
import com.xuexiang.xui.widget.picker.XSeekBar;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;

import butterknife.BindView;

/**
 * @author xuexiang
 * @since 2020-01-04 23:51
 */
@Page(name = "SeekBar\n滑块选择器，支持双向范围选择")
public class SeekBarFragment extends BaseFragment {

    @BindView(R.id.xrs_normal)
    XRangeSlider xrsNormal;
    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.xrs_bubble)
    XRangeSlider xrsBubble;
    @BindView(R.id.tv_bubble)
    TextView tvBubble;
    @BindView(R.id.xsb)
    XSeekBar xsb;
    @BindView(R.id.tv_xsb)
    TextView tvXsb;
    @BindView(R.id.ll_container)
    LinearLayout llContainer;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_seekbar;
    }

    @Override
    protected void initViews() {
        llContainer.addView(new XRangeSlider(getContext()));
    }

    @Override
    protected void initListeners() {
        xrsNormal.setOnRangeSliderListener(new XRangeSlider.OnRangeSliderListener() {
            @Override
            public void onMaxChanged(XRangeSlider slider, int maxValue) {
                tvNumber.setText(String.format("%d     %d", slider.getSelectedMin(), slider.getSelectedMax()));
            }

            @Override
            public void onMinChanged(XRangeSlider slider, int minValue) {
                tvNumber.setText(String.format("%d     %d", slider.getSelectedMin(), slider.getSelectedMax()));
            }
        });


        xrsBubble.setStartingMinMax(60, 100);
        xrsBubble.setOnRangeSliderListener(new XRangeSlider.OnRangeSliderListener() {
            @Override
            public void onMaxChanged(XRangeSlider slider, int maxValue) {
                tvBubble.setText(String.format("%d     %d", slider.getSelectedMin(), slider.getSelectedMax()));
            }

            @Override
            public void onMinChanged(XRangeSlider slider, int minValue) {
                tvBubble.setText(String.format("%d     %d", slider.getSelectedMin(), slider.getSelectedMax()));
            }
        });


        xsb.setDefaultValue(50);
        xsb.setOnSeekBarListener((seekBar, newValue) -> tvXsb.setText(String.valueOf(newValue)));
    }
}
