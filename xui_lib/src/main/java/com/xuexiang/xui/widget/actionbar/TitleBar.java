package com.xuexiang.xui.widget.actionbar;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xuexiang.xui.R;
import com.xuexiang.xui.XUI;
import com.xuexiang.xui.utils.DensityUtils;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.ThemeUtils;

import java.util.LinkedList;

import uk.co.chrisjenx.calligraphy.HasTypeface;

/**
 * 标题栏
 *
 * @author XUE
 * @date 2017/9/7 20:24
 */
public class TitleBar extends ViewGroup implements View.OnClickListener, HasTypeface {
    private static final String STATUS_BAR_HEIGHT_RES_NAME = "status_bar_height";
    /**
     * 文字默认白色
     */
    private static int DEFAULT_TEXT_COLOR = Color.WHITE;

    private TextView mLeftText;
    private LinearLayout mRightLayout;
    private LinearLayout mCenterLayout;
    private TextView mCenterText;
    private TextView mSubTitleText;
    private View mCustomCenterView;
    private View mDividerView;

    /**
     * 是否是沉浸模式
     */
    private boolean mImmersive;
    /**
     * 屏幕宽
     */
    private int mScreenWidth;
    /**
     * 标题栏的高度
     */
    private int mBarHeight;
    /**
     * 状态栏的高度
     */
    private int mStatusBarHeight;
    /**
     * 点击动作控件的padding
     */
    private int mActionPadding;
    /**
     * 左右侧文字的padding
     */
    private int mSideTextPadding;

    private int mSideTextSize;
    private int mTitleTextSize;
    private int mSubTitleTextSize;
    private int mActionTextSize;

    private int mSideTextColor;
    private int mTitleTextColor;
    private int mSubTitleTextColor;
    private int mActionTextColor;

    private Drawable mLeftImageResource;
    private String mLeftTextString;
    private String mTitleTextString;
    private String mSubTextString;
    private int mDividerColor;

    public TitleBar(Context context) {
        super(context);
        initAttrs(context, null);
        init(context);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        init(context);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        init(context);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        if (isInEditMode()) {
            return;
        }
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);
        if (typedArray != null) {
            mBarHeight = typedArray.getDimensionPixelOffset(R.styleable.TitleBar_tb_barHeight, ThemeUtils.resolveDimension(context, R.attr.xui_actionbar_height));
            mImmersive = typedArray.getBoolean(R.styleable.TitleBar_tb_immersive, false);

            mActionPadding = typedArray.getDimensionPixelOffset(R.styleable.TitleBar_tb_actionPadding, ThemeUtils.resolveDimension(context, R.attr.xui_actionbar_action_padding));
            mSideTextPadding = typedArray.getDimensionPixelOffset(R.styleable.TitleBar_tb_sideTextPadding, ThemeUtils.resolveDimension(context, R.attr.xui_actionbar_sidetext_padding));

            mSideTextSize = typedArray.getDimensionPixelSize(R.styleable.TitleBar_tb_sideTextSize, ThemeUtils.resolveDimension(context, R.attr.xui_actionbar_action_text_size));
            mTitleTextSize = typedArray.getDimensionPixelSize(R.styleable.TitleBar_tb_titleTextSize, ThemeUtils.resolveDimension(context, R.attr.xui_actionbar_title_text_size));
            mSubTitleTextSize = typedArray.getDimensionPixelSize(R.styleable.TitleBar_tb_subTitleTextSize, ThemeUtils.resolveDimension(context, R.attr.xui_actionbar_sub_text_size));
            mActionTextSize = typedArray.getDimensionPixelSize(R.styleable.TitleBar_tb_actionTextSize, ThemeUtils.resolveDimension(context, R.attr.xui_actionbar_action_text_size));

            mSideTextColor = typedArray.getColor(R.styleable.TitleBar_tb_sideTextColor, DEFAULT_TEXT_COLOR);
            mTitleTextColor = typedArray.getColor(R.styleable.TitleBar_tb_titleTextColor, DEFAULT_TEXT_COLOR);
            mSubTitleTextColor = typedArray.getColor(R.styleable.TitleBar_tb_subTitleTextColor, DEFAULT_TEXT_COLOR);
            mActionTextColor = typedArray.getColor(R.styleable.TitleBar_tb_actionTextColor, DEFAULT_TEXT_COLOR);

            mLeftImageResource = typedArray.getDrawable(R.styleable.TitleBar_tb_leftImageResource);
            mLeftTextString = typedArray.getString(R.styleable.TitleBar_tb_leftText);
            mTitleTextString = typedArray.getString(R.styleable.TitleBar_tb_titleText);
            mSubTextString = typedArray.getString(R.styleable.TitleBar_tb_subTitleText);
            mDividerColor = typedArray.getColor(R.styleable.TitleBar_tb_dividerColor, Color.TRANSPARENT);

            typedArray.recycle();
        }
    }

    private void init(Context context) {
        mScreenWidth = getResources().getDisplayMetrics().widthPixels;
        if (mImmersive) {
            mStatusBarHeight = getStatusBarHeight();
        }
        initView(context);
    }

    private void initView(Context context) {
        mLeftText = new TextView(context);
        mCenterLayout = new LinearLayout(context);
        mRightLayout = new LinearLayout(context);
        mDividerView = new View(context);

        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);

        mLeftText.setTextSize(TypedValue.COMPLEX_UNIT_PX, mSideTextSize);
        mLeftText.setTextColor(mSideTextColor);
        mLeftText.setText(mLeftTextString);
        if (mLeftImageResource != null) {
            mLeftText.setCompoundDrawablesWithIntrinsicBounds(mLeftImageResource, null, null, null);
        }
        mLeftText.setSingleLine();

        mLeftText.setGravity(Gravity.CENTER_VERTICAL);
        mLeftText.setPadding(mSideTextPadding, 0, mSideTextPadding, 0);
        mLeftText.setTypeface(XUI.getDefaultTypeface());

        mCenterText = new TextView(context);
        mSubTitleText = new TextView(context);

        if (!TextUtils.isEmpty(mSubTextString)) {
            mCenterLayout.setOrientation(LinearLayout.VERTICAL);
        }
        mCenterLayout.addView(mCenterText);
        mCenterLayout.addView(mSubTitleText);
        mCenterLayout.setGravity(Gravity.CENTER);

        mCenterText.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleTextSize);
        mCenterText.setTextColor(mTitleTextColor);
        mCenterText.setText(mTitleTextString);
        mCenterText.setSingleLine();
        mCenterText.setGravity(Gravity.CENTER);
        mCenterText.setEllipsize(TextUtils.TruncateAt.END);
        mCenterText.setTypeface(XUI.getDefaultTypeface());

        mSubTitleText.setTextSize(TypedValue.COMPLEX_UNIT_PX, mSubTitleTextSize);
        mSubTitleText.setTextColor(mSubTitleTextColor);
        mSubTitleText.setText(mSubTextString);
        mSubTitleText.setSingleLine();
        mSubTitleText.setGravity(Gravity.CENTER);
        mSubTitleText.setPadding(0, DensityUtils.dp2px(getContext(), 2), 0, 0);
        mSubTitleText.setEllipsize(TextUtils.TruncateAt.END);
        mSubTitleText.setTypeface(XUI.getDefaultTypeface());

        mRightLayout.setPadding(mSideTextPadding, 0, mSideTextPadding, 0);

        mDividerView.setBackgroundColor(mDividerColor);

        addView(mLeftText, layoutParams);
        addView(mCenterLayout);
        addView(mRightLayout, layoutParams);
        addView(mDividerView, new LayoutParams(LayoutParams.MATCH_PARENT, 1));
    }

    public TitleBar setImmersive(boolean immersive) {
        mImmersive = immersive;
        if (mImmersive) {
            mStatusBarHeight = getStatusBarHeight();
        } else {
            mStatusBarHeight = 0;
        }
        return this;
    }

    /**
     * 设置状态栏高度
     *
     * @param height
     * @return
     */
    public TitleBar setHeight(int height) {
        mBarHeight = height;
        setMeasuredDimension(getMeasuredWidth(), mBarHeight);
        return this;
    }

    public TitleBar setLeftImageResource(int resId) {
        mLeftText.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0);
        return this;
    }

    /**
     * 设置左侧图标
     *
     * @param resId
     * @return
     */
    public TitleBar setBackImageResource(int resId) {
        if (resId != 0) {
            mLeftImageResource = ResUtils.getDrawable(getContext(), resId);
            mLeftImageResource.setBounds(0, 0, DensityUtils.dp2px(getContext(), 12), DensityUtils.dp2px(getContext(), 22));
            mLeftText.setCompoundDrawables(mLeftImageResource, null, null, null);
        } else {
            mLeftImageResource = null;
            mLeftText.setCompoundDrawables(null, null, null, null);
        }
        return this;
    }

    /**
     * 设置左侧点击事件
     *
     * @param l
     * @return
     */
    public TitleBar setLeftClickListener(OnClickListener l) {
        mLeftText.setOnClickListener(l);
        return this;
    }

    /**
     * 设置左侧文字
     *
     * @param title
     * @return
     */
    public TitleBar setLeftText(CharSequence title) {
        mLeftText.setText(title);
        return this;
    }

    /**
     * 设置左侧文字
     *
     * @param resId
     * @return
     */
    public TitleBar setLeftText(int resId) {
        mLeftText.setText(resId);
        return this;
    }

    /**
     * 设置左侧文字大小
     *
     * @param size
     * @return
     */
    public TitleBar setLeftTextSize(float size) {
        mLeftText.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        return this;
    }

    /**
     * 设置左侧文字的最大长度
     *
     * @param maxEms
     * @return
     */
    public TitleBar setLeftTextMaxEms(int maxEms) {
        mLeftText.setMaxEms(maxEms);
        return this;
    }

    /**
     * 设置左侧文字的最大宽度
     *
     * @param maxPixels
     * @return
     */
    public TitleBar setLeftTextMaxWidth(int maxPixels) {
        mLeftText.setMaxWidth(maxPixels);
        return this;
    }

    /**
     * 设置左侧文字长度超出的处理
     *
     * @param where
     * @return
     */
    public TitleBar setLeftTextEllipsize(TextUtils.TruncateAt where) {
        mLeftText.setEllipsize(where);
        return this;
    }

    /**
     * 左侧文字的颜色
     *
     * @param color
     * @return
     */
    public TitleBar setLeftTextColor(int color) {
        mLeftText.setTextColor(color);
        return this;
    }

    /**
     * 设置左侧文字是否可显示
     *
     * @param visible
     * @return
     */
    public TitleBar setLeftVisible(boolean visible) {
        mLeftText.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    /**
     * 设置标题文字
     *
     * @param title
     * @return
     */
    public TitleBar setTitle(CharSequence title) {
        int index = title.toString().indexOf("\n");
        if (index > 0) {
            setTitle(title.subSequence(0, index), title.subSequence(index + 1, title.length()), LinearLayout.VERTICAL);
        } else {
            index = title.toString().indexOf("\t");
            if (index > 0) {
                setTitle(title.subSequence(0, index), "  " + title.subSequence(index + 1, title.length()), LinearLayout.HORIZONTAL);
            } else {
                mCenterText.setText(title);
                mSubTitleText.setVisibility(View.GONE);
            }
        }
        return this;
    }

    /**
     * 设置标题和副标题的文字
     *
     * @param title       标题
     * @param subTitle    副标题
     * @param orientation 对齐方式
     * @return
     */
    public TitleBar setTitle(CharSequence title, CharSequence subTitle, int orientation) {
        mCenterLayout.setOrientation(orientation);
        mCenterText.setText(title);

        mSubTitleText.setText(subTitle);
        mSubTitleText.setVisibility(View.VISIBLE);
        return this;
    }

    /**
     * 设置中心点击
     *
     * @param l
     * @return
     */
    public TitleBar setCenterClickListener(OnClickListener l) {
        mCenterLayout.setOnClickListener(l);
        return this;
    }

    /**
     * 设置标题文字
     *
     * @param resId
     * @return
     */
    public TitleBar setTitle(int resId) {
        setTitle(getResources().getString(resId));
        return this;
    }

    /**
     * 设置标题文字颜色
     *
     * @param resId
     * @return
     */
    public TitleBar setTitleColor(int resId) {
        mCenterText.setTextColor(resId);
        return this;
    }

    /**
     * 设置标题文字大小
     *
     * @param size
     * @return
     */
    public TitleBar setTitleSize(float size) {
        mCenterText.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        return this;
    }

    /**
     * 设置标题背景
     *
     * @param resId
     * @return
     */
    public TitleBar setTitleBackground(int resId) {
        mCenterText.setBackgroundResource(resId);
        return this;
    }

    public TitleBar setSubTitleColor(int resId) {
        mSubTitleText.setTextColor(resId);
        return this;
    }

    public TitleBar setSubTitleSize(float size) {
        mSubTitleText.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        return this;
    }

    public TitleBar setCustomTitle(View titleView) {
        if (titleView == null) {
            mCenterText.setVisibility(View.VISIBLE);
            if (mCustomCenterView != null) {
                mCenterLayout.removeView(mCustomCenterView);
            }

        } else {
            if (mCustomCenterView != null) {
                mCenterLayout.removeView(mCustomCenterView);
            }
            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            mCustomCenterView = titleView;
            mCenterLayout.addView(titleView, layoutParams);
            mCenterText.setVisibility(View.GONE);
        }
        return this;
    }

    public TitleBar setDivider(Drawable drawable) {
        mDividerView.setBackgroundDrawable(drawable);
        return this;
    }

    public TitleBar setDividerColor(int color) {
        mDividerView.setBackgroundColor(color);
        return this;
    }

    public TitleBar setDividerHeight(int dividerHeight) {
        mDividerView.getLayoutParams().height = dividerHeight;
        return this;
    }

    public TitleBar setActionTextColor(int colorResId) {
        mActionTextColor = colorResId;
        return this;
    }

    /**
     * Function to set a click listener for Title TextView
     *
     * @param listener the onClickListener
     */
    public TitleBar setOnTitleClickListener(OnClickListener listener) {
        mCenterText.setOnClickListener(listener);
        return this;
    }

    @Override
    public void onClick(View view) {
        final Object tag = view.getTag();
        if (tag instanceof Action) {
            final Action action = (Action) tag;
            action.performAction(view);
        }
    }

    /**
     * Adds a list of {@link Action}s.
     *
     * @param actionList the actions to add
     */
    public TitleBar addActions(ActionList actionList) {
        int actions = actionList.size();
        for (int i = 0; i < actions; i++) {
            addAction(actionList.get(i));
        }
        return this;
    }

    /**
     * Adds a new {@link Action}.
     *
     * @param action the action to add
     */
    public View addAction(Action action) {
        final int index = mRightLayout.getChildCount();
        return addAction(action, index);
    }

    /**
     * Adds a new {@link Action} at the specified index.
     *
     * @param action the action to add
     * @param index  the position at which to add the action
     */
    public View addAction(Action action, int index) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT);
        View view = inflateAction(action);
        mRightLayout.addView(view, index, params);
        return view;
    }

    /**
     * Removes all action views from this action bar
     */
    public void removeAllActions() {
        mRightLayout.removeAllViews();
    }

    /**
     * Remove a action from the action bar.
     *
     * @param index position of action to remove
     */
    public void removeActionAt(int index) {
        mRightLayout.removeViewAt(index);
    }

    /**
     * Remove a action from the action bar.
     *
     * @param action The action to remove
     */
    public void removeAction(Action action) {
        int childCount = mRightLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = mRightLayout.getChildAt(i);
            if (view != null) {
                final Object tag = view.getTag();
                if (tag instanceof Action && tag.equals(action)) {
                    mRightLayout.removeView(view);
                }
            }
        }
    }

    /**
     * Returns the number of actions currently registered with the action bar.
     *
     * @return action count
     */
    public int getActionCount() {
        return mRightLayout.getChildCount();
    }

    /**
     * Inflates a {@link View} with the given {@link Action}.
     *
     * @param action the action to inflate
     * @return a view
     */
    protected View inflateAction(Action action) {
        View view = null;
        if (TextUtils.isEmpty(action.getText())) {
            ImageView img = new ImageView(getContext());
            img.setImageResource(action.getDrawable());
            view = img;
        } else {
            TextView text = new TextView(getContext());
            text.setGravity(Gravity.CENTER);
            text.setText(action.getText());
            text.setTextSize(TypedValue.COMPLEX_UNIT_PX, mActionTextSize);
            text.setTypeface(XUI.getDefaultTypeface());
            if (mActionTextColor != 0) {
                text.setTextColor(mActionTextColor);
            }
            view = text;
        }

        view.setPadding(action.leftPadding() != -1 ? action.leftPadding() : mActionPadding, 0, action.rightPadding() != -1 ? action.rightPadding() : mActionPadding, 0);
        view.setTag(action);
        view.setOnClickListener(this);
        return view;
    }

    public View getViewByAction(Action action) {
        return findViewWithTag(action);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height;
        if (heightMode != MeasureSpec.EXACTLY) {
            height = mBarHeight + mStatusBarHeight;
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(mBarHeight, MeasureSpec.EXACTLY);
        } else {
            height = MeasureSpec.getSize(heightMeasureSpec) + mStatusBarHeight;
        }

        measureChild(mLeftText, widthMeasureSpec, heightMeasureSpec);
        measureChild(mRightLayout, widthMeasureSpec, heightMeasureSpec);
        if (mLeftText.getMeasuredWidth() > mRightLayout.getMeasuredWidth()) {
            mCenterLayout.measure(
                    MeasureSpec.makeMeasureSpec(mScreenWidth - 2 * mLeftText.getMeasuredWidth(), MeasureSpec.EXACTLY)
                    , heightMeasureSpec);
        } else {
            mCenterLayout.measure(
                    MeasureSpec.makeMeasureSpec(mScreenWidth - 2 * mRightLayout.getMeasuredWidth(), MeasureSpec.EXACTLY)
                    , heightMeasureSpec);
        }
        measureChild(mDividerView, widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mLeftText.layout(0, mStatusBarHeight, mLeftText.getMeasuredWidth(), mLeftText.getMeasuredHeight() + mStatusBarHeight);
        mRightLayout.layout(mScreenWidth - mRightLayout.getMeasuredWidth(), mStatusBarHeight,
                mScreenWidth, mRightLayout.getMeasuredHeight() + mStatusBarHeight);
        if (mLeftText.getMeasuredWidth() > mRightLayout.getMeasuredWidth()) {
            mCenterLayout.layout(mLeftText.getMeasuredWidth(), mStatusBarHeight,
                    mScreenWidth - mLeftText.getMeasuredWidth(), getMeasuredHeight());
        } else {
            mCenterLayout.layout(mRightLayout.getMeasuredWidth(), mStatusBarHeight,
                    mScreenWidth - mRightLayout.getMeasuredWidth(), getMeasuredHeight());
        }
        mDividerView.layout(0, getMeasuredHeight() - mDividerView.getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight());
    }

    /**
     * 计算状态栏高度高度
     * getStatusBarHeight
     *
     * @return
     */
    public static int getStatusBarHeight() {
        return getInternalDimensionSize(Resources.getSystem(), STATUS_BAR_HEIGHT_RES_NAME);
    }

    private static int getInternalDimensionSize(Resources res, String key) {
        int result = 0;
        int resourceId = res.getIdentifier(key, "dimen", "android");
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId);
        }
        return result;
    }


    /**
     * A {@link LinkedList} that holds a list of {@link Action}s.
     */
    @SuppressWarnings("serial")
    public static class ActionList extends LinkedList<Action> {
    }

    /**
     * Definition of an action that could be performed, along with a icon to
     * show.
     */
    public interface Action {
        /**
         * @return 显示文字
         */
        String getText();

        /**
         * @return 显示图标
         */
        int getDrawable();

        /**
         * 点击动作
         *
         * @param view
         */
        void performAction(View view);

        /**
         * @return 左边间距
         */
        int leftPadding();

        /**
         * @return 右边间距
         */
        int rightPadding();
    }

    /**
     * 图片动作
     */
    public static abstract class ImageAction implements Action {

        private int mDrawable;

        public ImageAction(int drawable) {
            mDrawable = drawable;
        }

        @Override
        public int getDrawable() {
            return mDrawable;
        }

        @Override
        public String getText() {
            return null;
        }

        @Override
        public int leftPadding() {
            return -1;
        }

        @Override
        public int rightPadding() {
            return -1;
        }
    }

    /**
     * 文字动作
     */
    public static abstract class TextAction implements Action {

        final private String mText;

        public TextAction(String text) {
            mText = text;
        }

        @Override
        public int getDrawable() {
            return 0;
        }

        @Override
        public String getText() {
            return mText;
        }

        @Override
        public int leftPadding() {
            return -1;
        }

        @Override
        public int rightPadding() {
            return -1;
        }
    }

    /**
     * Used by Calligraphy to change view's typeface
     */
    @Override
    public void setTypeface(Typeface tf) {
        mLeftText.setTypeface(tf);
        mCenterText.setTypeface(tf);
        mSubTitleText.setTypeface(tf);
    }

    public TextView getLeftText() {
        return mLeftText;
    }

    public TextView getSubTitleText() {
        return mSubTitleText;
    }

    public TextView getCenterText() {
        return mCenterText;
    }

    public View getDividerView() {
        return mDividerView;
    }
}
