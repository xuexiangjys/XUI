/*
 * Copyright (C) 2018 xuexiangjys(xuexiangjys@163.com)
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

package com.xuexiang.xuidemo.fragment.components.textview;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.textview.badge.Badge;
import com.xuexiang.xui.widget.textview.badge.BadgeView;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xuidemo.widget.SelectorColorDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author xuexiang
 * @since 2018/12/28 上午9:24
 */
@Page(name = "BadgeView\n小红点标记")
public class BadgeViewFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.button)
    Button button;
    List<RadioButton> radioButtons = new ArrayList<>();
    CompoundButton lastRadioButton;
    @BindView(R.id.tv_offset_x)
    TextView tvOffsetX;
    @BindView(R.id.seekBar_offset_x)
    SeekBar seekBarOffsetX;
    @BindView(R.id.tv_offset_y)
    TextView tvOffsetY;
    @BindView(R.id.seekBar_offset_y)
    SeekBar seekBarOffsetY;
    @BindView(R.id.tv_padding)
    TextView tvPadding;
    @BindView(R.id.seekBar_padding)
    SeekBar seekBarPadding;
    @BindView(R.id.iv_badge_color)
    ImageView ivBadgeColor;
    @BindView(R.id.switch_shadow)
    Switch switchShadow;
    @BindView(R.id.iv_number_color)
    ImageView ivNumberColor;
    @BindView(R.id.tv_number_size)
    TextView tvNumberSize;
    @BindView(R.id.seekBar_number_size)
    SeekBar seekBarNumberSize;
    @BindView(R.id.et_badge_number)
    EditText etBadgeNumber;
    @BindView(R.id.switch_exact)
    Switch switchExact;
    @BindView(R.id.et_badge_text)
    EditText etBadgeText;
    @BindView(R.id.switch_draggable)
    Switch switchDraggable;
    @BindView(R.id.tv_drag_state)
    TextView tvDragState;

    List<Badge> mBadges;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_badge_view;
    }

    @Override
    protected void initViews() {
        initBadge();

        radioButtons.add(findViewById(R.id.rb_st));
        radioButtons.add(findViewById(R.id.rb_sb));
        RadioButton rbEt = findViewById(R.id.rb_et);
        lastRadioButton = rbEt;
        radioButtons.add(rbEt);
        radioButtons.add(findViewById(R.id.rb_eb));
        radioButtons.add(findViewById(R.id.rb_ct));
        radioButtons.add(findViewById(R.id.rb_ce));
        radioButtons.add(findViewById(R.id.rb_cb));
        radioButtons.add(findViewById(R.id.rb_cs));
        radioButtons.add(findViewById(R.id.rb_c));


    }

    @Override
    protected void initListeners() {
        for (RadioButton rb : radioButtons) {
            rb.setOnCheckedChangeListener(this);
        }

        SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                for (Badge badge : mBadges) {
                    if (seekBar == seekBarOffsetX || seekBar == seekBarOffsetY) {
                        int x = seekBarOffsetX.getProgress();
                        int y = seekBarOffsetY.getProgress();
                        tvOffsetX.setText("GravityOffsetX : " + x);
                        tvOffsetY.setText("GravityOffsetY : " + y);
                        badge.setGravityOffset(x, y, true);
                    } else if (seekBar == seekBarPadding) {
                        tvPadding.setText("BadgePadding : " + progress);
                        badge.setBadgePadding(progress, true);
                    } else if (seekBar == seekBarNumberSize) {
                        tvNumberSize.setText("TextSize : " + progress);
                        badge.setBadgeTextSize(progress, true);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };
        seekBarOffsetX.setOnSeekBarChangeListener(onSeekBarChangeListener);
        seekBarOffsetY.setOnSeekBarChangeListener(onSeekBarChangeListener);
        seekBarPadding.setOnSeekBarChangeListener(onSeekBarChangeListener);
        seekBarNumberSize.setOnSeekBarChangeListener(onSeekBarChangeListener);

        etBadgeNumber.addTextChangedListener(new MyTextWatcher(etBadgeNumber));
        etBadgeText.addTextChangedListener(new MyTextWatcher(etBadgeText));

        switchExact.setOnCheckedChangeListener(onCheckedChangeListener);
        switchDraggable.setOnCheckedChangeListener(onCheckedChangeListener);
        switchShadow.setOnCheckedChangeListener(onCheckedChangeListener);
    }

    private void initBadge() {
        mBadges = new ArrayList<>();
        mBadges.add(new BadgeView(getContext()).bindTarget(textView).setBadgeNumber(5));
        mBadges.add(new BadgeView(getContext()).bindTarget(imageView)
                .setBadgeText("PNG")
                .setBadgeTextColor(0x00000000)
                .setBadgeGravity(Gravity.BOTTOM | Gravity.END)
                .setBadgeBackgroundColor(0xff9370DB)
                .setBadgeBackground(getResources().getDrawable(R.drawable.shape_round_rect_purple)));
        mBadges.add(new BadgeView(getContext()).bindTarget(button).setBadgeText("新").setBadgeTextSize(13, true)
                .setBadgeBackgroundColor(0xffffeb3b).setBadgeTextColor(0xff000000)
                .stroke(0xff000000, 1, true));
    }

    @OnClick({R.id.iv_badge_color, R.id.iv_number_color, R.id.btn_animation})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_badge_color:
                new SelectorColorDialog(getContext(), color -> {
                    ivBadgeColor.setBackgroundColor(color);
                    for (Badge badge : mBadges) {
                        badge.setBadgeBackgroundColor(color);
                    }
                }).show();
                break;
            case R.id.iv_number_color:
                new SelectorColorDialog(getContext(), color -> {
                    ivNumberColor.setBackgroundColor(color);
                    for (Badge badge : mBadges) {
                        badge.setBadgeTextColor(color);
                    }
                }).show();
                break;
            case R.id.btn_animation:
                for (Badge badge : mBadges) {
                    badge.hide(true);
                }
                break;
            default:
                break;
        }
    }

    class MyTextWatcher implements TextWatcher {
        private EditText editText;

        public MyTextWatcher(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try {
                for (Badge badge : mBadges) {
                    if (editText == etBadgeNumber) {
                        int num = TextUtils.isEmpty(s) ? 0 : Integer.parseInt(s.toString());
                        badge.setBadgeNumber(num);
                    } else if (editText == etBadgeText) {
                        badge.setBadgeText(s.toString());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!isChecked) {
            return;
        }
        if (lastRadioButton != null) {
            lastRadioButton.setChecked(false);
        }
        lastRadioButton = buttonView;
        for (Badge badge : mBadges) {
            switch (buttonView.getId()) {
                case R.id.rb_st:
                    badge.setBadgeGravity(Gravity.START | Gravity.TOP);
                    break;
                case R.id.rb_sb:
                    badge.setBadgeGravity(Gravity.START | Gravity.BOTTOM);
                    break;
                case R.id.rb_et:
                    badge.setBadgeGravity(Gravity.END | Gravity.TOP);
                    break;
                case R.id.rb_eb:
                    badge.setBadgeGravity(Gravity.END | Gravity.BOTTOM);
                    break;
                case R.id.rb_ct:
                    badge.setBadgeGravity(Gravity.CENTER | Gravity.TOP);
                    break;
                case R.id.rb_ce:
                    badge.setBadgeGravity(Gravity.CENTER | Gravity.END);
                    break;
                case R.id.rb_cb:
                    badge.setBadgeGravity(Gravity.CENTER | Gravity.BOTTOM);
                    break;
                case R.id.rb_cs:
                    badge.setBadgeGravity(Gravity.CENTER | Gravity.START);
                    break;
                case R.id.rb_c:
                    badge.setBadgeGravity(Gravity.CENTER);
                    break;
                default:
                    break;
            }
        }
    }


    CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            for (Badge badge : mBadges) {
                if (buttonView == switchExact) {
                    badge.setExactMode(isChecked);
                } else if (buttonView == switchDraggable) {
                    badge.setOnDragStateChangedListener(isChecked ?
                            (Badge.OnDragStateChangedListener) (dragState, badge1, targetView) -> {
                                switch (dragState) {
                                    case Badge.OnDragStateChangedListener.STATE_START:
                                        tvDragState.setText("STATE_START");
                                        break;
                                    case Badge.OnDragStateChangedListener.STATE_DRAGGING:
                                        tvDragState.setText("STATE_DRAGGING");
                                        break;
                                    case Badge.OnDragStateChangedListener.STATE_DRAGGING_OUT_OF_RANGE:
                                        tvDragState.setText("STATE_DRAGGING_OUT_OF_RANGE");
                                        break;
                                    case Badge.OnDragStateChangedListener.STATE_SUCCEED:
                                        tvDragState.setText("STATE_SUCCEED");
                                        break;
                                    case Badge.OnDragStateChangedListener.STATE_CANCELED:
                                        tvDragState.setText("STATE_CANCELED");
                                        break;
                                    default:
                                        break;
                                }
                            } : null);
                } else if (buttonView == switchShadow) {
                    badge.setShowShadow(isChecked);
                }
            }
        }
    };
}
