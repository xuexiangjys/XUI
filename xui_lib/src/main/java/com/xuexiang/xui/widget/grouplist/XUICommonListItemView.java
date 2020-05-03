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
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.IntDef;

import com.xuexiang.xui.R;
import com.xuexiang.xui.utils.DensityUtils;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.ThemeUtils;
import com.xuexiang.xui.utils.Utils;
import com.xuexiang.xui.utils.ViewUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 作为通用列表 {@link XUIGroupListView} 里的 item 使用，也可以单独使用。
 * 支持以下样式:
 * <ul>
 * <li>通过 {@link #setText(CharSequence)} 设置一行文字</li>
 * <li>通过 {@link #setDetailText(CharSequence)} 设置一行说明文字, 并通过 {@link #setOrientation(int)} 设置说明文字的位置,
 * 也可以在 xml 中使用 {@link R.styleable#XUICommonListItemView_xui_orientation} 设置。</li>
 * <li>通过 {@link #setAccessoryType(int)} 设置右侧 View 的类型, 可选的类型见 {@link XUICommonListItemAccessoryType},
 * 也可以在 xml 中使用 {@link R.styleable#XUICommonListItemView_xui_accessory_type} 设置。</li>
 * </ul>
 *
 * @author xuexiang
 * @since 2019/1/3 上午10:52
 */
public class XUICommonListItemView extends RelativeLayout {

    /**
     * 右侧不显示任何东西
     */
    public final static int ACCESSORY_TYPE_NONE = 0;
    /**
     * 右侧显示一个箭头
     */
    public final static int ACCESSORY_TYPE_CHEVRON = 1;
    /**
     * 右侧显示一个开关
     */
    public final static int ACCESSORY_TYPE_SWITCH = 2;
    /**
     * 自定义右侧显示的 View
     */
    public final static int ACCESSORY_TYPE_CUSTOM = 3;

    /**
     * detailText 在 title 文字的下方
     */
    public final static int VERTICAL = 0;
    /**
     * detailText 在 item 的右方
     */
    public final static int HORIZONTAL = 1;

    /**
     * 红点在左边
     */
    public final static int RED_DOT_POSITION_LEFT = 0;
    /**
     * 红点在右边
     */
    public final static int RED_DOT_POSITION_RIGHT = 1;

    @IntDef({ACCESSORY_TYPE_NONE, ACCESSORY_TYPE_CHEVRON, ACCESSORY_TYPE_SWITCH, ACCESSORY_TYPE_CUSTOM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface XUICommonListItemAccessoryType {
    }

    @IntDef({VERTICAL, HORIZONTAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface XUICommonListItemOrientation {
    }

    @IntDef({RED_DOT_POSITION_LEFT, RED_DOT_POSITION_RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface XUICommonListItemRedDotPosition {
    }

    /**
     * Item 右侧的 View 的类型
     */
    @XUICommonListItemAccessoryType
    private int mAccessoryType;

    /**
     * 控制 detailText 是在 title 文字的下方还是 item 的右方
     */
    private int mOrientation = HORIZONTAL;

    /**
     * 控制红点的位置
     */
    @XUICommonListItemRedDotPosition
    private int mRedDotPosition = RED_DOT_POSITION_LEFT;


    protected ImageView mImageView;
    private ViewGroup mAccessoryView;
    protected LinearLayout mTextContainer;
    protected TextView mTextView;
    protected TextView mDetailTextView;
    protected Space mTextDetailSpace;
    protected CheckBox mSwitch;
    private ImageView mRedDot;
    private ViewStub mNewTipViewStub;
    private View mNewTip;

    public XUICommonListItemView(Context context) {
        this(context, null);
    }

    public XUICommonListItemView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.XUICommonListItemViewStyle);
    }

    public XUICommonListItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    protected void init(Context context, AttributeSet attrs, int defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.xui_layout_common_list_item, this, true);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.XUICommonListItemView, defStyleAttr, 0);
        @XUICommonListItemOrientation int orientation = array.getInt(R.styleable.XUICommonListItemView_xui_orientation, HORIZONTAL);
        @XUICommonListItemAccessoryType int accessoryType = array.getInt(R.styleable.XUICommonListItemView_xui_accessory_type, ACCESSORY_TYPE_NONE);
        final int initTitleColor = array.getColor(R.styleable.XUICommonListItemView_xui_commonList_titleColor, ResUtils.getColor(R.color.xui_config_color_gray_1));
        final int initDetailColor = array.getColor(R.styleable.XUICommonListItemView_xui_commonList_detailColor, ResUtils.getColor(R.color.xui_config_color_gray_5));
        array.recycle();

        mImageView = findViewById(R.id.group_list_item_imageView);
        mTextContainer = findViewById(R.id.group_list_item_textContainer);
        mTextView = findViewById(R.id.group_list_item_textView);
        mTextView.setTextColor(initTitleColor);
        mRedDot = findViewById(R.id.group_list_item_tips_dot);
        mNewTipViewStub = findViewById(R.id.group_list_item_tips_new);
        mDetailTextView = findViewById(R.id.group_list_item_detailTextView);
        mTextDetailSpace = findViewById(R.id.group_list_item_space);
        mDetailTextView.setTextColor(initDetailColor);
        LinearLayout.LayoutParams detailTextViewLP = (LinearLayout.LayoutParams) mDetailTextView.getLayoutParams();
        if (ViewUtils.getIsLastLineSpacingExtraError()) {
            detailTextViewLP.bottomMargin = -ThemeUtils.resolveDimension(context, R.attr.xui_common_list_item_detail_line_space);
        }
        if (orientation == VERTICAL) {
            detailTextViewLP.topMargin = DensityUtils.dp2px(getContext(), 6);
        } else {
            detailTextViewLP.topMargin = 0;
        }
        mAccessoryView = findViewById(R.id.group_list_item_accessoryView);
        setOrientation(orientation);
        setAccessoryType(accessoryType);
    }


    public void updateImageViewLp(LayoutParamConfig lpConfig) {
        if (lpConfig != null) {
            LayoutParams lp = (LayoutParams) mImageView.getLayoutParams();
            mImageView.setLayoutParams(lpConfig.onConfig(lp));
        }
    }

    public void setImageDrawable(Drawable drawable) {
        if (drawable == null) {
            mImageView.setVisibility(View.GONE);
        } else {
            mImageView.setImageDrawable(drawable);
            mImageView.setVisibility(View.VISIBLE);
        }
    }

    public void setRedDotPosition(@XUICommonListItemRedDotPosition int redDotPosition) {
        mRedDotPosition = redDotPosition;
        requestLayout();
    }

    public CharSequence getText() {
        return mTextView.getText();
    }

    public void setText(CharSequence text) {
        mTextView.setText(text);
        if (Utils.isNullOrEmpty(text)) {
            mTextView.setVisibility(View.GONE);
        } else {
            mTextView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 切换是否显示小红点
     *
     * @param isShow 是否显示小红点
     */
    public void showRedDot(boolean isShow) {
        showRedDot(isShow, true);
    }

    public void showRedDot(boolean isShow, boolean configToShow) {
        mRedDot.setVisibility((isShow && configToShow) ? VISIBLE : GONE);
    }

    /**
     * 切换是否显示更新提示
     *
     * @param isShow 是否显示更新提示
     */
    public void showNewTip(boolean isShow) {
        if (isShow) {
            if (mNewTip == null) {
                mNewTip = mNewTipViewStub.inflate();
            }
            mNewTip.setVisibility(View.VISIBLE);
            mRedDot.setVisibility(GONE);
        } else {
            if (mNewTip != null && mNewTip.getVisibility() == View.VISIBLE) {
                mNewTip.setVisibility(View.GONE);
            }
        }
    }

    public CharSequence getDetailText() {
        return mDetailTextView.getText();
    }


    public void setDetailText(CharSequence text) {
        mDetailTextView.setText(text);
        if (Utils.isNullOrEmpty(text)) {
            mDetailTextView.setVisibility(View.GONE);
        } else {
            mDetailTextView.setVisibility(View.VISIBLE);
        }
    }

    public int getOrientation() {
        return mOrientation;
    }

    public void setOrientation(@XUICommonListItemOrientation int orientation) {
        mOrientation = orientation;

        LinearLayout.LayoutParams spaceLp = (LinearLayout.LayoutParams) mTextDetailSpace.getLayoutParams();
        // 默认文字是水平布局的
        if (mOrientation == VERTICAL) {
            mTextContainer.setOrientation(LinearLayout.VERTICAL);
            mTextContainer.setGravity(Gravity.LEFT);
            spaceLp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            spaceLp.height = DensityUtils.dp2px(getContext(), 4);
            spaceLp.weight = 0;
            mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, ThemeUtils.resolveDimension(getContext(), R.attr.xui_common_list_item_title_v_text_size));
            mDetailTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, ThemeUtils.resolveDimension(getContext(), R.attr.xui_common_list_item_detail_v_text_size));
        } else {
            mTextContainer.setOrientation(LinearLayout.HORIZONTAL);
            mTextContainer.setGravity(Gravity.CENTER_VERTICAL);
            spaceLp.width = 0;
            spaceLp.height = 0;
            spaceLp.weight = 1;
            mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, ThemeUtils.resolveDimension(getContext(), R.attr.xui_common_list_item_title_h_text_size));
            mDetailTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, ThemeUtils.resolveDimension(getContext(), R.attr.xui_common_list_item_detail_h_text_size));
        }
    }

    public int getAccessoryType() {
        return mAccessoryType;
    }

    /**
     * 设置右侧 View 的类型。
     * <p>
     * 注意如果 type 为 {@link #ACCESSORY_TYPE_SWITCH}, 那么 switch 的切换事件应该 {@link #getSwitch()} 后用 {@link CheckBox#setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener)} 来监听
     * </p>
     *
     * @param type 见 {@link XUICommonListItemAccessoryType}
     */
    public void setAccessoryType(@XUICommonListItemAccessoryType int type) {
        mAccessoryView.removeAllViews();
        mAccessoryType = type;

        switch (type) {
            // 向右的箭头
            case ACCESSORY_TYPE_CHEVRON: {
                ImageView tempImageView = getAccessoryImageView();
                tempImageView.setImageDrawable(ThemeUtils.resolveDrawable(getContext(), R.attr.xui_common_list_item_chevron));
                mAccessoryView.addView(tempImageView);
                mAccessoryView.setVisibility(VISIBLE);
            }
            break;
            // switch开关
            case ACCESSORY_TYPE_SWITCH: {
                if (mSwitch == null) {
                    mSwitch = new CheckBox(getContext());
                    mSwitch.setButtonDrawable(ThemeUtils.resolveDrawable(getContext(), R.attr.xui_common_list_item_switch));
                    mSwitch.setLayoutParams(getAccessoryLayoutParams());
                    // disable掉且不可点击，然后通过整个item的点击事件来toggle开关的状态
                    mSwitch.setClickable(false);
                    mSwitch.setEnabled(false);
                }
                mAccessoryView.addView(mSwitch);
                mAccessoryView.setVisibility(VISIBLE);
            }
            break;
            // 自定义View
            case ACCESSORY_TYPE_CUSTOM:
                mAccessoryView.setVisibility(VISIBLE);
                break;
            // 清空所有accessoryView
            case ACCESSORY_TYPE_NONE:
                mAccessoryView.setVisibility(GONE);
                break;
            default:
                break;
        }
    }

    private ViewGroup.LayoutParams getAccessoryLayoutParams() {
        return new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private ImageView getAccessoryImageView() {
        ImageView resultImageView = new ImageView(getContext());
        resultImageView.setLayoutParams(getAccessoryLayoutParams());
        resultImageView.setScaleType(ImageView.ScaleType.CENTER);
        return resultImageView;
    }

    public TextView getTextView() {
        return mTextView;
    }

    public TextView getDetailTextView() {
        return mDetailTextView;
    }

    public CheckBox getSwitch() {
        return mSwitch;
    }

    public ViewGroup getAccessoryContainerView() {
        return mAccessoryView;
    }

    /**
     * 添加自定义的 Accessory View
     *
     * @param view 自定义的 Accessory View
     */
    public void addAccessoryCustomView(View view) {
        if (mAccessoryType == ACCESSORY_TYPE_CUSTOM) {
            mAccessoryView.addView(view);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        // 红点的位置
        if (mRedDot != null && mRedDot.getVisibility() == View.VISIBLE) {

            int top = getHeight() / 2 - mRedDot.getMeasuredHeight() / 2;
            int textLeft = mTextContainer.getLeft();
            int left;

            if (mRedDotPosition == RED_DOT_POSITION_LEFT) {
                //红点在左
                float textWidth = mTextView.getPaint().measureText(mTextView.getText().toString()); // 文字宽度
                left = (int) (textLeft + textWidth + DensityUtils.dp2px(getContext(), 4)); // 在原来红点位置的基础上右移

            } else if (mRedDotPosition == RED_DOT_POSITION_RIGHT) {
                //红点在右
                left = textLeft + mTextContainer.getWidth() - mRedDot.getMeasuredWidth();

            } else {
                return;
            }

            mRedDot.layout(left,
                    top,
                    left + mRedDot.getMeasuredWidth(),
                    top + mRedDot.getMeasuredHeight());

        }

        // New的位置
        if (mNewTip != null && mNewTip.getVisibility() == View.VISIBLE) {
            int textLeft = mTextContainer.getLeft();
            float textWidth = mTextView.getPaint().measureText(mTextView.getText().toString()); // 文字宽度
            int left = (int) (textLeft + textWidth + DensityUtils.dp2px(getContext(), 4)); // 在原来红点位置的基础上右移
            int top = getHeight() / 2 - mNewTip.getMeasuredHeight() / 2;
            mNewTip.layout(left,
                    top,
                    left + mNewTip.getMeasuredWidth(),
                    top + mNewTip.getMeasuredHeight());
        }
    }


    public interface LayoutParamConfig {
        RelativeLayout.LayoutParams onConfig(RelativeLayout.LayoutParams lp);
    }
}
