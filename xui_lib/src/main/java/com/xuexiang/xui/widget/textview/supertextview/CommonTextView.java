package com.xuexiang.xui.widget.textview.supertextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xuexiang.xui.R;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.ThemeUtils;

import uk.co.chrisjenx.calligraphy.HasTypeface;

/**
 * 通用的textView可以实现大部分常用布局样式
 *
 * @author xuexiang
 * @since 2019/1/14 上午11:24
 */
public class CommonTextView extends RelativeLayout implements HasTypeface {

    private Context mContext;

    private int mDefaultTextColor;//文字默认颜色
    private int mDefaultTextSize;//默认字体大小
    private int mDefaultPadding;//默认边距

    private int mBackgroundColor = 0xffffffff;//背景颜色

    private Drawable mLeft_drawableLeft;
    private Drawable mLeft_drawableTop;
    private Drawable mLeft_drawableRight;
    private Drawable mLeft_drawableBottom;

    private Drawable mCenter_drawableLeft;
    private Drawable mCenter_drawableTop;
    private Drawable mCenter_drawableRight;
    private Drawable mCenter_drawableBottom;

    private Drawable mRight_drawableLeft;
    private Drawable mRight_drawableTop;
    private Drawable mRight_drawableRight;
    private Drawable mRight_drawableBottom;


    private Drawable mLeft_IV_drawable;
//    private Drawable mRight_IV_drawable;


    private CharSequence mLeftTextString;
    private CharSequence mLeftTopTextString;
    private CharSequence mLeftBottomTextString;

    private CharSequence mRightTextString;
    private CharSequence mRightTopTextString;
    private CharSequence mRightBottomTextString;

    private CharSequence mCenterTextString;
    private CharSequence mCenterTopTextString;
    private CharSequence mCenterBottomTextString;

    private int mLeftTextSize;
    private int mLeftTopTextSize;
    private int mLeftBottomTextSize;

    private int mRightTextSize;
    private int mRightTopTextSize;
    private int mRightBottomTextSize;

    private int mCenterTextSize;
    private int mCenterTopTextSize;
    private int mCenterBottomTextSize;

    private int mLeftTextColor;
    private int mLeftTopTextColor;
    private int mLeftBottomTextColor;

    private int mCenterTextColor;
    private int mCenterTopTextColor;
    private int mCenterBottomTextColor;

    private int mRightTextColor;
    private int mRightTopTextColor;
    private int mRightBottomTextColor;

    private int mLeftIconDrawablePadding;
    private int mCenterIconDrawablePadding;
    private int mRightIconDrawablePadding;

    private int mLeftViewPaddingLeft;
    private int mLeftViewPaddingRight;

    private int mCenterViewPaddingLeft;
    private int mCenterViewPaddingRight;

    private int mRightViewPaddingLeft;
    private int mRightViewPaddingRight;

    private int mTopDividerLineMarginLR;
    private int mTopDividerLineMarginLeft;
    private int mTopDividerLineMarginRight;

    private int mBottomDividerLineMarginLR;
    private int mBottomDividerLineMarginLeft;
    private int mBottomDividerLineMarginRight;

    private int mBothDividerLineMarginLeft;
    private int mBothDividerLineMarginRight;

    private int mCenterSpaceHeight;

    private int mLeftImageViewMarginLeft;
//    private int mRightImageViewMarginRight;

    private int mDividerLineType;
    private int mDividerLineColor;
    private int mDividerLineHeight;


    private int mLeftTextViewLineSpacingExtra;
    private int mCenterTextViewLineSpacingExtra;
    private int mRightTextViewLineSpacingExtra;

    /**
     * 分割线的类型
     */
    private static final int NONE = 0;
    private static final int TOP = 1;
    private static final int BOTTOM = 2;
    private static final int BOTH = 3;
    private static final int DEFAULT = BOTTOM;

    /**
     * 是否使用点击出现波纹效果
     */
    private boolean mIsUseRipple;

    private boolean mSetSingleLine = true;
    private int mSetMaxEms = 10;
    private int mSetLines = 1;

    /**
     * TextView的Gravity
     */
    private static final int GRAVITY_LEFT_CENTER = 0;
    private static final int GRAVITY_CENTER = 1;
    private static final int GRAVITY_RIGHT_CENTER = 2;

    private static final int DEFAULT_GRAVITY = GRAVITY_CENTER;

    private int mLeftTextViewGravity;
    private int mCenterTextViewGravity;
    private int mRightTextViewGravity;

    private TextView mLeftTextView, mCenterTextView, mRightTextView;
    private TextView mLeftTopTextView, mCenterTopTextView, mRightTopTextView;
    private TextView mLeftBottomTextView, mCenterBottomTextView, mRightBottomTextView;

    private ImageView mLeftImageView;
    //    private ImageView rightImageView;
    private View mTopLineView, mBottomLineView;
    private View mCenterBaseLineView;

    private boolean mLeftViewIsClickable;
    private boolean mCenterViewIsClickable;
    private boolean mRightViewIsClickable;


    private LayoutParams mLeftTVParams, mCenterTVParams, mRightTVParams, mTopLineParams, mBottomLineParams;
    private LayoutParams mLeftTopTVParams, mCenterTopTVParams, mRightTopTVParams;
    private LayoutParams mLeftBottomTVParams, mCenterBottomTVParams, mRightBottomTVParams;
    private LayoutParams mCenterBaseLineParams;
    //    private RelativeLayout.LayoutParams rightIVParams;

    private OnCommonTextViewClickListener mOnCommonTextViewClickListener;

    private Drawable mBackground_drawable;
    private boolean mIsCenterAlignLeft;
    private int mCenterViewMarginLeft;

    public CommonTextView(Context context) {
        super(context);
        initAttrs(context, null);
    }

    public CommonTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    public CommonTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        mContext = context;

        mDefaultTextColor = ThemeUtils.resolveColor(context, R.attr.stv_color_common_text, ResUtils.getColor(R.color.stv_color_common_text));
        mDefaultTextSize = ThemeUtils.resolveDimension(context, R.attr.stv_text_size, ResUtils.getDimensionPixelSize(R.dimen.default_stv_text_size));
        mDefaultPadding = ThemeUtils.resolveDimension(context, R.attr.stv_margin, ResUtils.getDimensionPixelSize(R.dimen.default_stv_margin));
        mCenterSpaceHeight = dip2px(context, 5);
        getAttr(attrs);
        init();
    }

    /**
     * 获取自定义控件资源
     *
     * @param attrs attrs
     */
    private void getAttr(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.CommonTextView);

        ////////设置文字或者图片资源////////
        mLeft_drawableLeft = ResUtils.getDrawableAttrRes(getContext(), typedArray, R.styleable.CommonTextView_cLeftIconResForDrawableLeft);
        mLeft_drawableTop = ResUtils.getDrawableAttrRes(getContext(), typedArray, R.styleable.CommonTextView_cLeftIconResForDrawableTop);
        mLeft_drawableRight = ResUtils.getDrawableAttrRes(getContext(), typedArray, R.styleable.CommonTextView_cLeftIconResForDrawableRight);
        mLeft_drawableBottom = ResUtils.getDrawableAttrRes(getContext(), typedArray, R.styleable.CommonTextView_cLeftIconResForDrawableBottom);

        mCenter_drawableLeft = ResUtils.getDrawableAttrRes(getContext(), typedArray, R.styleable.CommonTextView_cCenterIconResForDrawableLeft);
        mCenter_drawableTop = ResUtils.getDrawableAttrRes(getContext(), typedArray, R.styleable.CommonTextView_cCenterIconResForDrawableTop);
        mCenter_drawableRight = ResUtils.getDrawableAttrRes(getContext(), typedArray, R.styleable.CommonTextView_cCenterIconResForDrawableRight);
        mCenter_drawableBottom = ResUtils.getDrawableAttrRes(getContext(), typedArray, R.styleable.CommonTextView_cCenterIconResForDrawableBottom);

        mRight_drawableLeft = ResUtils.getDrawableAttrRes(getContext(), typedArray, R.styleable.CommonTextView_cRightIconResForDrawableLeft);
        mRight_drawableTop = ResUtils.getDrawableAttrRes(getContext(), typedArray, R.styleable.CommonTextView_cRightIconResForDrawableTop);
        mRight_drawableRight = ResUtils.getDrawableAttrRes(getContext(), typedArray, R.styleable.CommonTextView_cRightIconResForDrawableRight);
        mRight_drawableBottom = ResUtils.getDrawableAttrRes(getContext(), typedArray, R.styleable.CommonTextView_cRightIconResForDrawableBottom);

        mLeft_IV_drawable = ResUtils.getDrawableAttrRes(getContext(), typedArray, R.styleable.CommonTextView_cLeftImageViewDrawableRes);
//        mRight_IV_drawable = typedArray.getDrawable(R.styleable.CommonTextView_cRightImageViewDrawableRes);
        /////////////////////

        mLeftTextString = typedArray.getString(R.styleable.CommonTextView_cLeftTextString);
        mLeftTopTextString = typedArray.getString(R.styleable.CommonTextView_cLeftTopTextString);
        mLeftBottomTextString = typedArray.getString(R.styleable.CommonTextView_cLeftBottomTextString);

        mCenterTextString = typedArray.getString(R.styleable.CommonTextView_cCenterTextString);
        mCenterTopTextString = typedArray.getString(R.styleable.CommonTextView_cCenterTopTextString);
        mCenterBottomTextString = typedArray.getString(R.styleable.CommonTextView_cCenterBottomTextString);

        mRightTextString = typedArray.getString(R.styleable.CommonTextView_cRightTextString);
        mRightTopTextString = typedArray.getString(R.styleable.CommonTextView_cRightTopTextString);
        mRightBottomTextString = typedArray.getString(R.styleable.CommonTextView_cRightBottomTextString);

        mLeftTextColor = typedArray.getColor(R.styleable.CommonTextView_cLeftTextColor, mDefaultTextColor);
        mLeftTopTextColor = typedArray.getColor(R.styleable.CommonTextView_cLeftTopTextColor, mDefaultTextColor);
        mLeftBottomTextColor = typedArray.getColor(R.styleable.CommonTextView_cLeftBottomTextColor, mDefaultTextColor);

        mCenterTextColor = typedArray.getColor(R.styleable.CommonTextView_cCenterTextColor, mDefaultTextColor);
        mCenterTopTextColor = typedArray.getColor(R.styleable.CommonTextView_cCenterTopTextColor, mDefaultTextColor);
        mCenterBottomTextColor = typedArray.getColor(R.styleable.CommonTextView_cCenterBottomTextColor, mDefaultTextColor);

        mRightTextColor = typedArray.getColor(R.styleable.CommonTextView_cRightTextColor, mDefaultTextColor);
        mRightTopTextColor = typedArray.getColor(R.styleable.CommonTextView_cRightTopTextColor, mDefaultTextColor);
        mRightBottomTextColor = typedArray.getColor(R.styleable.CommonTextView_cRightBottomTextColor, mDefaultTextColor);

        mLeftTextSize = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cLeftTextSize, mDefaultTextSize);
        mLeftTopTextSize = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cLeftTopTextSize, mDefaultTextSize);
        mLeftBottomTextSize = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cLeftBottomTextSize, mDefaultTextSize);

        mCenterTextSize = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cCenterTextSize, mDefaultTextSize);
        mCenterTopTextSize = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cCenterTopTextSize, mDefaultTextSize);
        mCenterBottomTextSize = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cCenterBottomTextSize, mDefaultTextSize);

        mRightTextSize = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cRightTextSize, mDefaultTextSize);
        mRightTopTextSize = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cRightTopTextSize, mDefaultTextSize);
        mRightBottomTextSize = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cRightBottomTextSize, mDefaultTextSize);

        mLeftIconDrawablePadding = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cLeftIconDrawablePadding, mDefaultPadding);
        mCenterIconDrawablePadding = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cCenterIconDrawablePadding, mDefaultPadding);
        mRightIconDrawablePadding = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cRightIconDrawablePadding, mDefaultPadding);

        mLeftViewPaddingLeft = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cLeftViewPaddingLeft, mDefaultPadding);
        mLeftViewPaddingRight = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cLeftViewPaddingRight, mDefaultPadding);
        mCenterViewPaddingLeft = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cCenterViewPaddingLeft, mDefaultPadding);
        mCenterViewPaddingRight = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cCenterViewPaddingRight, mDefaultPadding);
        mRightViewPaddingLeft = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cRightViewPaddingLeft, mDefaultPadding);
        mRightViewPaddingRight = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cRightViewPaddingRight, mDefaultPadding);

        mBothDividerLineMarginLeft = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cBothDividerLineMarginLeft, 0);
        mBothDividerLineMarginRight = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cBothDividerLineMarginRight, 0);

        mTopDividerLineMarginLR = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cTopDividerLineMarginLR, 0);
        mTopDividerLineMarginLeft = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cTopDividerLineMarginLeft, 0);
        mTopDividerLineMarginRight = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cTopDividerLineMarginRight, 0);

        mBottomDividerLineMarginLR = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cBottomDividerLineMarginLR, 0);
        mBottomDividerLineMarginLeft = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cBottomDividerLineMarginLeft, 0);
        mBottomDividerLineMarginRight = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cBottomDividerLineMarginRight, 0);

        mLeftImageViewMarginLeft = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cLeftImageViewMarginLeft, mDefaultPadding);
//        mRightImageViewMarginRight = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cRightImageViewMarginRight, mDefaultPadding);

        mCenterSpaceHeight = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cCenterSpaceHeight, mCenterSpaceHeight);


        mLeftTextViewLineSpacingExtra = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cLeftTextViewLineSpacingExtra, 0);
        mCenterTextViewLineSpacingExtra = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cCenterTextViewLineSpacingExtra, 0);
        mRightTextViewLineSpacingExtra = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cRightTextViewLineSpacingExtra, 0);

        mDividerLineType = typedArray.getInt(R.styleable.CommonTextView_cShowDividerLineType, DEFAULT);
        mDividerLineColor = typedArray.getColor(R.styleable.CommonTextView_cDividerLineColor, ThemeUtils.resolveColor(getContext(), R.attr.xui_config_color_separator_light));

        mDividerLineHeight = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cDividerLineHeight, dip2px(mContext, 0.5f));

        mIsUseRipple = typedArray.getBoolean(R.styleable.CommonTextView_cUseRipple, false);

        mBackgroundColor = typedArray.getColor(R.styleable.CommonTextView_cBackgroundColor, mBackgroundColor);

        mSetSingleLine = typedArray.getBoolean(R.styleable.CommonTextView_cSetSingleLine, true);
        mSetMaxEms = typedArray.getInt(R.styleable.CommonTextView_cSetMaxEms, mSetMaxEms);
        mSetLines = typedArray.getInt(R.styleable.CommonTextView_cSetLines, 1);

        mLeftTextViewGravity = typedArray.getInt(R.styleable.CommonTextView_cLeftTextViewGravity, DEFAULT_GRAVITY);
        mCenterTextViewGravity = typedArray.getInt(R.styleable.CommonTextView_cCenterTextViewGravity, DEFAULT_GRAVITY);
        mRightTextViewGravity = typedArray.getInt(R.styleable.CommonTextView_cRightTextViewGravity, DEFAULT_GRAVITY);

        mLeftViewIsClickable = typedArray.getBoolean(R.styleable.CommonTextView_cLeftViewIsClickable, false);
        mCenterViewIsClickable = typedArray.getBoolean(R.styleable.CommonTextView_cCenterViewIsClickable, false);
        mRightViewIsClickable = typedArray.getBoolean(R.styleable.CommonTextView_cRightViewIsClickable, false);

        mBackground_drawable = ResUtils.getDrawableAttrRes(getContext(), typedArray, R.styleable.CommonTextView_cBackgroundDrawableRes);

        mIsCenterAlignLeft = typedArray.getBoolean(R.styleable.CommonTextView_cIsCenterAlignLeft, false);
        mCenterViewMarginLeft = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cCenterViewMarginLeft, dip2px(mContext, 200));

        typedArray.recycle();
    }

    /**
     * 初始化
     */
    private void init() {
        initCommonTextView();
        initLineView();
        initCenterBaseLine();
        if (mLeft_IV_drawable != null) {
            initLeftImageView();
        }
//        if (mRight_IV_drawable!=null){
//            initRightImageView();
//        }
        if (mLeftTextString != null || mLeft_drawableLeft != null || mLeft_drawableRight != null) {
            initLeftText();
        }
        if (mCenterTextString != null) {
            initCenterText();
        }
        if (mRightTextString != null || mRight_drawableLeft != null || mRight_drawableRight != null) {
            initRightText();
        }

        if (mLeftTopTextString != null) {
            initLeftTopText();
        }
        if (mLeftBottomTextString != null) {
            initLeftBottomText();
        }

        if (mCenterTopTextString != null) {
            initCenterTopText();
        }
        if (mCenterBottomTextString != null) {
            initCenterBottomText();
        }
        if (mRightTopTextString != null) {
            initRightTopText();
        }
        if (mRightBottomTextString != null) {
            initRightBottomText();
        }

    }

    /**
     * 初始化commonTextView
     */
    private void initCommonTextView() {
        this.setBackgroundColor(mBackgroundColor);
        if (mIsUseRipple) {
            this.setBackgroundResource(R.drawable.stv_btn_selector_white);
        }
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnCommonTextViewClickListener != null) {
                    mOnCommonTextViewClickListener.onCommonTextViewClick();
                }
            }
        });
        if (mBackground_drawable != null) {
            this.setBackgroundDrawable(mBackground_drawable);
        }
    }

    /**
     * 为了设置上下两排文字居中对齐显示而需要设置的基准线
     */
    private void initCenterBaseLine() {
        if (mCenterBaseLineView == null) {
            if (mCenterBaseLineParams == null) {
                mCenterBaseLineParams = new LayoutParams(LayoutParams.MATCH_PARENT, mCenterSpaceHeight);
                mCenterBaseLineParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
            }
            mCenterBaseLineView = new View(mContext);

            mCenterBaseLineView.setId(R.id.cCenterBaseLineId);
            mCenterBaseLineView.setLayoutParams(mCenterBaseLineParams);
        }
        addView(mCenterBaseLineView);
    }

    /**
     * 初始化分割线
     */
    private void initLineView() {
        switch (mDividerLineType) {
            case NONE:
                break;
            case TOP:
                setTopLineMargin();
                break;
            case BOTTOM:
                setBottomLineMargin();
                break;
            case BOTH:
                setTopLineMargin();
                setBottomLineMargin();
                break;
            default:
                break;
        }

    }

    /**
     * 设置顶部的分割线
     */
    private void setTopLineMargin() {
        if (mTopDividerLineMarginLR != 0) {
            initTopLineView(mTopDividerLineMarginLR, mTopDividerLineMarginLR);
        } else if (mBothDividerLineMarginLeft != 0 | mBothDividerLineMarginRight != 0) {
            initTopLineView(mBothDividerLineMarginLeft, mBothDividerLineMarginRight);
        } else {
            initTopLineView(mTopDividerLineMarginLeft, mTopDividerLineMarginRight);
        }
    }

    /**
     * 设置底部的分割线
     */
    private void setBottomLineMargin() {
        if (mBottomDividerLineMarginLR != 0) {
            initBottomLineView(mBottomDividerLineMarginLR, mBottomDividerLineMarginLR);
        } else if (mBothDividerLineMarginRight != 0 | mBothDividerLineMarginRight != 0) {
            initBottomLineView(mBothDividerLineMarginLeft, mBothDividerLineMarginRight);
        } else {
            initBottomLineView(mBottomDividerLineMarginLeft, mBottomDividerLineMarginRight);
        }
    }


    /**
     * 设置上边分割线view
     *
     * @param marginLeft  左间距
     * @param marginRight 右间距
     */
    private void initTopLineView(int marginLeft, int marginRight) {
        if (mTopLineView == null) {
            if (mTopLineParams == null) {
                mTopLineParams = new LayoutParams(LayoutParams.MATCH_PARENT, mDividerLineHeight);
            }
            mTopLineParams.addRule(ALIGN_PARENT_TOP, TRUE);
            mTopLineParams.setMargins(marginLeft, 0, marginRight, 0);
            mTopLineView = new View(mContext);
            mTopLineView.setLayoutParams(mTopLineParams);
            mTopLineView.setBackgroundColor(mDividerLineColor);
        }
        addView(mTopLineView);
    }

    /**
     * 设置底部分割线view
     *
     * @param marginLeft  左间距
     * @param marginRight 右间距
     */
    private void initBottomLineView(int marginLeft, int marginRight) {
        if (mBottomLineView == null) {
            if (mBottomLineParams == null) {
                mBottomLineParams = new LayoutParams(LayoutParams.MATCH_PARENT, mDividerLineHeight);
            }
            mBottomLineParams.addRule(ALIGN_PARENT_BOTTOM, TRUE);
            mBottomLineParams.setMargins(marginLeft, 0, marginRight, 0);

            mBottomLineView = new View(mContext);
            mBottomLineView.setLayoutParams(mBottomLineParams);
            mBottomLineView.setBackgroundColor(mDividerLineColor);
        }
        addView(mBottomLineView);
    }

    /**
     * 初始化左边ImageView
     * 主要是为了便于使用第三方图片框架获取网络图片使用
     */
    private void initLeftImageView() {
        mLeftImageView = new ImageView(mContext);
        LayoutParams mLeftIVParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mLeftIVParams.addRule(ALIGN_PARENT_LEFT, TRUE);
        mLeftIVParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
        mLeftIVParams.setMargins(mLeftImageViewMarginLeft, 0, 0, 0);
        mLeftImageView.setScaleType(ImageView.ScaleType.CENTER);
        mLeftImageView.setId(R.id.cLeftImageViewId);
        mLeftImageView.setLayoutParams(mLeftIVParams);
        if (mLeft_IV_drawable != null) {
            mLeftImageView.setImageDrawable(mLeft_IV_drawable);
        }
        addView(mLeftImageView);
    }

    /**
     * 初始化右边ImageView
     * 主要是为了便于使用第三方图片框架获取网络图片使用
     */
//    private void initRightImageView() {
//        rightImageView = new ImageView(mContext);
//        rightIVParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//        rightIVParams.addRule(ALIGN_PARENT_RIGHT, TRUE);
//        rightIVParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
//        rightIVParams.setMargins(0, 0, mRightImageViewMarginRight, 0);
//        rightImageView.setScaleType(ImageView.ScaleType.CENTER);
//        rightImageView.setId(R.id.cRightImageViewId);
//        rightImageView.setLayoutParams(rightIVParams);
//        if (mRight_IV_drawable != null) {
//            rightImageView.setImageDrawable(mRight_IV_drawable);
//        }
//        addView(rightImageView);
//    }

    /**
     * 初始化左边textView
     */
    private void initLeftText() {
        if (mLeftTextView == null) {
            if (mLeftTVParams == null) {
                mLeftTVParams = getParams(mLeftTVParams);
            }
            mLeftTVParams.addRule(CENTER_VERTICAL, TRUE);
            mLeftTVParams.addRule(RIGHT_OF, R.id.cLeftImageViewId);
            mLeftTVParams.setMargins(mLeftViewPaddingLeft, 0, mLeftViewPaddingRight, 0);
            mLeftTextView = initText(mLeftTextView, mLeftTVParams, R.id.cLeftTextId, mLeftTextColor, mLeftTextSize);
            mLeftTextView.setText(mLeftTextString);
            mLeftTextView.setLineSpacing(mLeftTextViewLineSpacingExtra, 1.0f);
            setTextViewGravity(mLeftTextView, mLeftTextViewGravity);
            if (mLeftViewIsClickable) {
                mLeftTextView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnCommonTextViewClickListener != null) {
                            mOnCommonTextViewClickListener.onLeftViewClick();
                        }
                    }
                });
            }
        }

        setDrawable(mLeftTextView, mLeft_drawableLeft, mLeft_drawableTop, mLeft_drawableRight, mLeft_drawableBottom, mLeftIconDrawablePadding);

    }

    /**
     * 初始化左上textView
     */
    private void initLeftTopText() {
        if (mLeftTopTextView == null) {
            if (mLeftTopTVParams == null) {
                mLeftTopTVParams = getParams(mLeftTopTVParams);
            }
            mLeftTopTVParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
            mLeftTopTVParams.addRule(ABOVE, R.id.cCenterBaseLineId);
            mLeftTopTVParams.addRule(RIGHT_OF, R.id.cLeftImageViewId);
            mLeftTopTVParams.setMargins(mLeftViewPaddingLeft, 0, mLeftViewPaddingRight, 0);
            mLeftTopTextView = initText(mLeftTopTextView, mLeftTopTVParams, R.id.cLeftTopTextId, mLeftTopTextColor, mLeftTopTextSize);
            mLeftTopTextView.setText(mLeftTopTextString);
            setTextViewGravity(mLeftTopTextView, mLeftTextViewGravity);

        }

    }

    /**
     * 初始化左下textView
     */
    private void initLeftBottomText() {
        if (mLeftBottomTextView == null) {
            if (mLeftBottomTVParams == null) {
                mLeftBottomTVParams = getParams(mLeftBottomTVParams);
            }
            mLeftBottomTVParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
            mLeftBottomTVParams.addRule(BELOW, R.id.cCenterBaseLineId);
            mLeftBottomTVParams.addRule(RIGHT_OF, R.id.cLeftImageViewId);
            mLeftBottomTVParams.setMargins(mLeftViewPaddingLeft, 0, mLeftViewPaddingRight, 0);
            mLeftBottomTextView = initText(mLeftBottomTextView, mLeftBottomTVParams, R.id.cLeftBottomTextId, mLeftBottomTextColor, mLeftBottomTextSize);
            mLeftBottomTextView.setText(mLeftBottomTextString);
            setTextViewGravity(mLeftBottomTextView, mLeftTextViewGravity);

        }

    }

    /**
     * 初始化中间textView
     */
    private void initCenterText() {
        if (mCenterTextView == null) {
            if (mCenterTVParams == null) {
                if (mIsCenterAlignLeft) {
                    mCenterTVParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                } else {
                    mCenterTVParams = getParams(mCenterTVParams);
                }
            }

            mCenterTVParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
            mCenterTVParams.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);

            if (mIsCenterAlignLeft) {
                mCenterTextView = initText(mCenterTextView, mCenterTVParams, R.id.cCenterTextId, mCenterTextColor, mCenterTextSize);
                mCenterTVParams.setMargins(mCenterViewMarginLeft, 0, mCenterViewPaddingRight, 0);
                setTextViewGravity(mCenterTextView, GRAVITY_LEFT_CENTER);
            } else {
                mCenterTextView = initText(mCenterTextView, mCenterTVParams, R.id.cCenterTextId, mCenterTextColor, mCenterTextSize);
                mCenterTVParams.setMargins(mCenterViewPaddingLeft, 0, mCenterViewPaddingRight, 0);
                setTextViewGravity(mCenterTextView, mCenterTextViewGravity);
            }

            mCenterTextView.setText(mCenterTextString);
            mCenterTextView.setLineSpacing(mCenterTextViewLineSpacingExtra, 1.0f);

            if (mCenterViewIsClickable) {
                mCenterTextView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnCommonTextViewClickListener != null) {
                            mOnCommonTextViewClickListener.onCenterViewClick();
                        }
                    }
                });
            }

        }
        setDrawable(mCenterTextView, mCenter_drawableLeft, mCenter_drawableTop, mCenter_drawableRight, mCenter_drawableBottom, mCenterIconDrawablePadding);

    }

    /**
     * 初始化中上textView
     */
    private void initCenterTopText() {
        if (mCenterTopTextView == null) {
            if (mCenterTopTVParams == null) {
                mCenterTopTVParams = getParams(mCenterTopTVParams);
            }
            mCenterTopTVParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
            mCenterTopTVParams.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
            mCenterTopTVParams.addRule(ABOVE, R.id.cCenterBaseLineId);
            mCenterTopTVParams.setMargins(mCenterViewPaddingLeft, 0, mCenterViewPaddingRight, 0);
            mCenterTopTextView = initText(mCenterTopTextView, mCenterTopTVParams, R.id.cCenterTopTextId, mCenterTopTextColor, mCenterTopTextSize);
            mCenterTopTextView.setText(mCenterTopTextString);
            mCenterTopTextView.setLineSpacing(mCenterTextViewLineSpacingExtra, 1.0f);
            setTextViewGravity(mCenterTopTextView, mCenterTextViewGravity);
        }

    }

    /**
     * 初始化中下textView
     */
    private void initCenterBottomText() {
        if (mCenterBottomTextView == null) {
            if (mCenterBottomTVParams == null) {
                mCenterBottomTVParams = getParams(mCenterBottomTVParams);
            }
            mCenterBottomTVParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
            mCenterBottomTVParams.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
            mCenterBottomTVParams.addRule(BELOW, R.id.cCenterBaseLineId);
            mCenterBottomTVParams.setMargins(mCenterViewPaddingLeft, 0, mCenterViewPaddingRight, 0);
            mCenterBottomTextView = initText(mCenterBottomTextView, mCenterBottomTVParams, R.id.cCenterBottomTextId, mCenterBottomTextColor, mCenterBottomTextSize);
            mCenterBottomTextView.setText(mCenterBottomTextString);
            mCenterBottomTextView.setLineSpacing(mCenterTextViewLineSpacingExtra, 1.0f);
            setTextViewGravity(mCenterBottomTextView, mCenterTextViewGravity);
        }

    }


    /**
     * 初始化右边textView
     */
    private void initRightText() {
        if (mRightTextView == null) {
            if (mRightTVParams == null) {
                mRightTVParams = getParams(mRightTVParams);
            }

            mRightTVParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
            mRightTVParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, TRUE);
            mRightTVParams.addRule(RelativeLayout.LEFT_OF, R.id.cRightImageViewId);
            mRightTVParams.setMargins(mRightViewPaddingLeft, 0, mRightViewPaddingRight, 0);
            mRightTextView = initText(mRightTextView, mRightTVParams, R.id.cRightTextId, mRightTextColor, mRightTextSize);
            mRightTextView.setText(mRightTextString);
//            mRightTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
            mRightTextView.setLineSpacing(mRightTextViewLineSpacingExtra, 1.0f);
            setTextViewGravity(mRightTextView, mRightTextViewGravity);
            if (mRightViewIsClickable) {
                mRightTextView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnCommonTextViewClickListener != null) {
                            mOnCommonTextViewClickListener.onRightViewClick();
                        }
                    }
                });
            }

        }
        setDrawable(mRightTextView, mRight_drawableLeft, mRight_drawableTop, mRight_drawableRight, mRight_drawableBottom, mRightIconDrawablePadding);

    }

    /**
     * 初始化右上textView
     */
    private void initRightTopText() {
        if (mRightTopTextView == null) {
            if (mRightTopTVParams == null) {
                mRightTopTVParams = getParams(mRightTopTVParams);
            }
            mRightTopTVParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
            mRightTopTVParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, TRUE);
            mRightTopTVParams.addRule(ABOVE, R.id.cCenterBaseLineId);
            mRightTopTVParams.addRule(RelativeLayout.LEFT_OF, R.id.cRightImageViewId);
            mRightTopTVParams.setMargins(mRightViewPaddingLeft, 0, mRightViewPaddingRight, 0);
            mRightTopTextView = initText(mRightTopTextView, mRightTopTVParams, R.id.cRightTopTextId, mRightTopTextColor, mRightTopTextSize);
            mRightTopTextView.setText(mRightTopTextString);
            mRightTopTextView.setLineSpacing(mRightTextViewLineSpacingExtra, 1.0f);
            setTextViewGravity(mRightTopTextView, mRightTextViewGravity);

        }

    }

    /**
     * 初始化右下textView
     */
    private void initRightBottomText() {
        if (mRightBottomTextView == null) {
            if (mRightBottomTVParams == null) {
                mRightBottomTVParams = getParams(mRightBottomTVParams);
            }
            mRightBottomTVParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
            mRightBottomTVParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, TRUE);
            mRightBottomTVParams.addRule(BELOW, R.id.cCenterBaseLineId);
            mRightBottomTVParams.addRule(RelativeLayout.LEFT_OF, R.id.cRightImageViewId);
            mRightBottomTVParams.setMargins(mRightViewPaddingLeft, 0, mRightViewPaddingRight, 0);
            mRightBottomTextView = initText(mRightBottomTextView, mRightBottomTVParams, R.id.cRightBottomTextId, mRightBottomTextColor, mRightBottomTextSize);
            mRightBottomTextView.setText(mRightBottomTextString);
            mRightBottomTextView.setLineSpacing(mRightTextViewLineSpacingExtra, 1.0f);
            setTextViewGravity(mRightBottomTextView, mRightTextViewGravity);
        }
    }


    /**
     * 初始化Params
     *
     * @param params 对象
     * @return 返回
     */
    public LayoutParams getParams(LayoutParams params) {
        if (params == null) {
            params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        }
        return params;
    }

    /**
     * 初始化textView
     *
     * @param textView     对象
     * @param layoutParams 对象
     * @param id           id
     * @param textColor    颜色值
     * @param textSize     字体大小
     * @return 返回
     */
    public TextView initText(TextView textView, LayoutParams layoutParams, int id, int textColor, int textSize) {
        if (textView == null) {
            textView = new TextView(mContext);
            textView.setId(id);
            textView.setLayoutParams(layoutParams);
            textView.setTextColor(textColor);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
//            textView.setGravity(Gravity.CENTER);
            textView.setLines(mSetLines);
            textView.setSingleLine(mSetSingleLine);
//            textView.setMaxEms(mSetMaxEms);
//            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mSetMaxEms)});
            addView(textView);
        }
        return textView;
    }

    private void setTextViewGravity(TextView textView, int gravity_type) {

        switch (gravity_type) {
            case GRAVITY_LEFT_CENTER:
                textView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                break;
            case GRAVITY_CENTER:
                textView.setGravity(Gravity.CENTER);
                break;
            case GRAVITY_RIGHT_CENTER:
                textView.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
                break;
            default:
                break;
        }

    }

    /**
     * 设置textView的drawable
     *
     * @param textView        对象
     * @param drawableLeft    左边图标
     * @param drawableTop     上边图标
     * @param drawableRight   右边图标
     * @param drawableBottom  下边图标
     * @param drawablePadding 图标距离文字的间距
     */
    public void setDrawable(TextView textView, Drawable drawableLeft, Drawable drawableTop, Drawable drawableRight, Drawable drawableBottom, int drawablePadding) {
        textView.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, drawableTop, drawableRight, drawableBottom);
        textView.setCompoundDrawablePadding(drawablePadding);
    }

    /**
     * 设置左边文字字符串
     *
     * @param string 字符串
     * @return 返回
     */
    public CommonTextView setLeftTextString(CharSequence string) {
        if (mLeftTextView == null) {
            initLeftText();
        }
        mLeftTextView.setText(string);
        return this;
    }

    /**
     * 设置左上文字字符串
     *
     * @param string 字符串
     * @return 返回
     */
    public CommonTextView setLeftTopTextString(CharSequence string) {
        if (mLeftTopTextView == null) {
            initLeftTopText();
        }
        mLeftTopTextView.setText(string);
        return this;
    }

    /**
     * 设置左下文字字符串
     *
     * @param string 字符串
     * @return 返回
     */
    public CommonTextView setLeftBottomTextString(CharSequence string) {
        if (mLeftBottomTextView == null) {
            initLeftBottomText();
        }
        mLeftBottomTextView.setText(string);
        return this;
    }

    /**
     * 设置左边文字字体大小
     *
     * @param size 字体大小
     * @return 返回
     */
    public CommonTextView setLeftTextSize(float size) {
        if (mLeftTextView == null) {
            initLeftText();
        }
        mLeftTextView.setTextSize(size);
        return this;
    }

    /**
     * set Left TextColor
     *
     * @param color textColor
     * @return return
     */
    public CommonTextView setLeftTextColor(int color) {
        if (mLeftTextView == null) {
            initLeftText();
        }
        mLeftTextView.setTextColor(color);
        return this;
    }

    /**
     * 设置中间文字字符串
     *
     * @param string 字符串
     * @return 返回
     */
    public CommonTextView setCenterTextString(CharSequence string) {
        if (mCenterTextView == null) {
            initCenterText();
        }
        mCenterTextView.setText(string);
        return this;
    }

    /**
     * 设置中上文字字符串
     *
     * @param string 字符串
     * @return 返回
     */
    public CommonTextView setCenterTopTextString(CharSequence string) {
        if (mCenterTopTextView == null) {
            initCenterTopText();
        }
        mCenterTopTextView.setText(string);
        return this;
    }

    /**
     * 设置中下文字字符串
     *
     * @param string 字符串
     * @return 返回
     */
    public CommonTextView setCenterBottomTextString(CharSequence string) {
        if (mCenterBottomTextView == null) {
            initCenterBottomText();
        }
        mCenterBottomTextView.setText(string);
        return this;
    }

    /**
     * 设置中间文字字体大小
     *
     * @param size 字体大小
     * @return 返回
     */
    public CommonTextView setCenterTextSize(float size) {
        if (mCenterTextView == null) {
            initCenterText();
        }
        mCenterTextView.setTextSize(size);
        return this;
    }

    /**
     * 设置中间文字颜色值
     *
     * @param color 颜色值
     * @return 返回
     */
    public CommonTextView setCenterTextColor(int color) {
        if (mCenterTextView == null) {
            initCenterText();
        }
        mCenterTextView.setTextColor(color);
        return this;
    }

    /**
     * 设置右边文字字符串
     *
     * @param string 字符串
     * @return 返回
     */
    public CommonTextView setRightTextString(CharSequence string) {
        if (mRightTextView == null) {
            initRightText();
        }
        mRightTextView.setText(string);
        return this;
    }

    /**
     * 设置右上文字字符串
     *
     * @param string 字符串
     * @return 返回
     */
    public CommonTextView setRightTopTextString(CharSequence string) {
        if (mRightTopTextView == null) {
            initRightTopText();
        }
        mRightTopTextView.setText(string);
        return this;
    }

    /**
     * 设置右下文字字符串
     *
     * @param string 字符串
     * @return 返回
     */
    public CommonTextView setRightBottomTextString(CharSequence string) {
        if (mRightBottomTextView == null) {
            initRightBottomText();
        }
        mRightBottomTextView.setText(string);
        return this;
    }

    /**
     * 设置右边文字的字体大小
     *
     * @param size 字体大小
     * @return 返回
     */
    public CommonTextView setRightTextSize(float size) {
        if (mRightTextView == null) {
            initRightText();
        }
        mRightTextView.setTextSize(size);
        return this;
    }

    /**
     * 设置右边文字的颜色
     *
     * @param color 颜色值
     * @return 返回
     */
    public CommonTextView setRightTextColor(int color) {
        if (mRightTextView == null) {
            initRightText();
        }
        mRightTextView.setTextColor(color);
        return this;
    }

    /**
     * 设置左边view的drawableLeft
     *
     * @param drawableLeft 资源
     * @return 返回
     */
    public CommonTextView setLeftDrawableLeft(Drawable drawableLeft) {
        if (drawableLeft != null) {
            drawableLeft.setBounds(0, 0, drawableLeft.getMinimumWidth(), drawableLeft.getMinimumHeight());
        }
        if (mLeftTextView == null) {
            initLeftText();
        }
        mLeftTextView.setCompoundDrawables(drawableLeft, null, null, null);
        return this;
    }

    /**
     * 设置左边view的drawableTop
     *
     * @param drawableTop 资源
     * @returnTop 返回
     */
    public CommonTextView setLeftDrawableTop(Drawable drawableTop) {
        if (drawableTop != null) {
            drawableTop.setBounds(0, 0, drawableTop.getMinimumWidth(), drawableTop.getMinimumHeight());
        }
        if (mLeftTextView == null) {
            initLeftText();
        }
        mLeftTextView.setCompoundDrawables(null, drawableTop, null, null);
        return this;
    }

    /**
     * 设置左边view的drawableRight
     *
     * @param drawableRight 资源
     * @return 返回
     */
    public CommonTextView setLeftDrawableRight(Drawable drawableRight) {
        if (drawableRight != null) {
            drawableRight.setBounds(0, 0, drawableRight.getMinimumWidth(), drawableRight.getMinimumHeight());
        }
        if (mLeftTextView == null) {
            initLeftText();
        }
        mLeftTextView.setCompoundDrawables(null, null, drawableRight, null);
        return this;
    }

    /**
     * 设置左边view的drawableBottom
     *
     * @param drawableBottom 资源
     * @return 返回
     */
    public CommonTextView setLeftDrawableBottom(Drawable drawableBottom) {
        if (drawableBottom != null) {
            drawableBottom.setBounds(0, 0, drawableBottom.getMinimumWidth(), drawableBottom.getMinimumHeight());
        }
        if (mLeftTextView == null) {
            initLeftText();
        }
        mLeftTextView.setCompoundDrawables(null, null, null, drawableBottom);
        return this;
    }

    /**
     * 设置中间view的drawableLeft
     *
     * @param drawableLeft 资源
     * @return 返回
     */
    public CommonTextView setCenterDrawableLeft(Drawable drawableLeft) {
        if (drawableLeft != null) {
            drawableLeft.setBounds(0, 0, drawableLeft.getMinimumWidth(), drawableLeft.getMinimumHeight());
        }
        if (mCenterTextView == null) {
            initCenterText();
        }
        mCenterTextView.setCompoundDrawables(drawableLeft, null, null, null);
        return this;
    }

    /**
     * 设置中间view的drawableTop
     *
     * @param drawableTop 资源
     * @returnTop 返回
     */
    public CommonTextView setCenterDrawableTop(Drawable drawableTop) {
        if (drawableTop != null) {
            drawableTop.setBounds(0, 0, drawableTop.getMinimumWidth(), drawableTop.getMinimumHeight());
        }
        if (mCenterTextView == null) {
            initCenterText();
        }
        mCenterTextView.setCompoundDrawables(null, drawableTop, null, null);
        return this;
    }

    /**
     * 设置中间view的drawableRight
     *
     * @param drawableRight 资源
     * @return 返回
     */
    public CommonTextView setCenterDrawableRight(Drawable drawableRight) {
        if (drawableRight != null) {
            drawableRight.setBounds(0, 0, drawableRight.getMinimumWidth(), drawableRight.getMinimumHeight());
        }
        if (mCenterTextView == null) {
            initCenterText();
        }
        mCenterTextView.setCompoundDrawables(null, null, drawableRight, null);
        return this;
    }

    /**
     * 设置中间view的drawableBottom
     *
     * @param drawableBottom 资源
     * @return 返回
     */
    public CommonTextView setCenterDrawableBottom(Drawable drawableBottom) {
        if (drawableBottom != null) {
            drawableBottom.setBounds(0, 0, drawableBottom.getMinimumWidth(), drawableBottom.getMinimumHeight());
        }
        if (mCenterTextView == null) {
            initCenterText();
        }
        mCenterTextView.setCompoundDrawables(null, null, null, drawableBottom);
        return this;
    }

    /**
     * 设置右边view的drawableLeft
     *
     * @param drawableLeft 资源
     * @return 返回
     */
    public CommonTextView setRightDrawableLeft(Drawable drawableLeft) {
        if (drawableLeft != null) {
            drawableLeft.setBounds(0, 0, drawableLeft.getMinimumWidth(), drawableLeft.getMinimumHeight());
        }
        if (mRightTextView == null) {
            initRightText();
        }
        mRightTextView.setCompoundDrawables(drawableLeft, null, null, null);
        return this;
    }

    /**
     * 设置右边view的drawableTop
     *
     * @param drawableTop 资源
     * @returnTop 返回
     */
    public CommonTextView setRightDrawableTop(Drawable drawableTop) {
        if (drawableTop != null) {
            drawableTop.setBounds(0, 0, drawableTop.getMinimumWidth(), drawableTop.getMinimumHeight());
        }
        if (mRightTextView == null) {
            initRightText();
        }
        mRightTextView.setCompoundDrawables(null, drawableTop, null, null);
        return this;
    }

    /**
     * 设置右边view的drawableRight
     *
     * @param drawableRight 资源
     * @return 返回
     */
    public CommonTextView setRightDrawableRight(Drawable drawableRight) {
        if (drawableRight != null) {
            drawableRight.setBounds(0, 0, drawableRight.getMinimumWidth(), drawableRight.getMinimumHeight());
        }
        if (mRightTextView == null) {
            initRightText();
        }
        mRightTextView.setCompoundDrawables(null, null, drawableRight, null);
        return this;
    }

    /**
     * 设置右边view的drawableBottom
     *
     * @param drawableBottom 资源
     * @return 返回
     */
    public CommonTextView setRightDrawableBottom(Drawable drawableBottom) {
        if (drawableBottom != null) {
            drawableBottom.setBounds(0, 0, drawableBottom.getMinimumWidth(), drawableBottom.getMinimumHeight());
        }
        if (mRightTextView == null) {
            initRightText();
        }
        mRightTextView.setCompoundDrawables(null, null, null, drawableBottom);
        return this;
    }

    /**
     * 获取左边imageView
     *
     * @return 返回imageView对象
     */
    public ImageView getLeftImageView() {
        if (mLeftImageView == null) {
            initLeftImageView();
        }
        return mLeftImageView;
    }

    /**
     * 获取左边textView的值
     *
     * @return 返回
     */
    public CharSequence getLeftTextString() {
        return mLeftTextView != null ? mLeftTextView.getText() : "";
    }

    /**
     * 获取左上textView的值
     *
     * @return 返回
     */
    public CharSequence getLeftTopTextString() {
        return mLeftTopTextView != null ? mLeftTopTextView.getText() : "";
    }

    /**
     * 获取左下textView的值
     *
     * @return 返回
     */
    public CharSequence getLeftBottomTextString() {
        return mLeftBottomTextView != null ? mLeftBottomTextView.getText() : "";
    }

    /**
     * 获取中间textView的值
     *
     * @return 返回
     */
    public CharSequence getCenterTextString() {
        return mCenterTextView != null ? mCenterTextView.getText() : "";
    }

    /**
     * 获取中上textView的值
     *
     * @return 返回
     */
    public CharSequence getCenterTopTextString() {
        return mCenterTopTextView != null ? mCenterTopTextView.getText() : "";
    }

    /**
     * 获取中下textView的值
     *
     * @return 返回
     */
    public CharSequence getCenterBottomTextString() {
        return mCenterBottomTextView != null ? mCenterBottomTextView.getText() : "";
    }

    /**
     * 获取右边textView的值
     *
     * @return 返回
     */
    public CharSequence getRightTextString() {
        return mRightTextView != null ? mRightTextView.getText() : "";
    }

    /**
     * 获取右上textView的值
     *
     * @return 返回
     */
    public CharSequence getRightTopTextString() {
        return mRightTopTextView != null ? mRightTopTextView.getText() : "";
    }

    /**
     * 获取右下textView的值
     *
     * @return 返回
     */
    public CharSequence getRightBottomTextString() {
        return mRightBottomTextView != null ? mRightBottomTextView.getText() : "";
    }

    @Override
    public void setTypeface(Typeface typeface) {
        if (mLeftTextView != null) {
            mLeftTextView.setTypeface(typeface);
        }
        if (mCenterTextView != null) {
            mCenterTextView.setTypeface(typeface);
        }
        if (mRightTextView != null) {
            mRightTextView.setTypeface(typeface);
        }
        if (mLeftTopTextView != null) {
            mLeftTopTextView.setTypeface(typeface);
        }
        if (mCenterTopTextView != null) {
            mCenterTopTextView.setTypeface(typeface);
        }
        if (mRightTopTextView != null) {
            mRightTopTextView.setTypeface(typeface);
        }
        if (mLeftBottomTextView != null) {
            mLeftBottomTextView.setTypeface(typeface);
        }
        if (mCenterBottomTextView != null) {
            mCenterBottomTextView.setTypeface(typeface);
        }
        if (mRightBottomTextView != null) {
            mRightBottomTextView.setTypeface(typeface);
        }
    }

    /**
     * 点击事件接口
     */
    public static class OnCommonTextViewClickListener {
        public void onCommonTextViewClick() {
        }

        public void onLeftViewClick() {
        }

        public void onCenterViewClick() {
        }

        public void onRightViewClick() {
        }

    }

    /**
     * 点击事件
     *
     * @param listener listener对象
     * @return 返回当前对象便于链式操作
     */
    public CommonTextView setOnCommonTextViewClickListener(OnCommonTextViewClickListener listener) {
        this.mOnCommonTextViewClickListener = listener;
        return this;
    }

    /**
     * 设置左边view可点击
     *
     * @param isClickable boolean类型
     * @return 返回
     */
    public CommonTextView setLeftViewIsClickable(boolean isClickable) {
        if (isClickable) {
            if (mLeftTextView != null) {
                mLeftTextView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnCommonTextViewClickListener != null) {
                            mOnCommonTextViewClickListener.onLeftViewClick();
                        }
                    }
                });
            }
        }
        return this;
    }

    /**
     * 设置中间view可点击
     *
     * @param isClickable boolean类型
     * @return 返回
     */
    public CommonTextView setCenterViewIsClickable(boolean isClickable) {
        if (isClickable) {
            if (mCenterTextView != null) {
                mCenterTextView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnCommonTextViewClickListener != null) {
                            mOnCommonTextViewClickListener.onCenterViewClick();
                        }
                    }
                });
            }
        }
        return this;
    }

    /**
     * 设置右边view可点击
     *
     * @param isClickable boolean类型
     * @return 返回
     */
    public CommonTextView setRightViewIsClickable(boolean isClickable) {
        if (isClickable) {
            if (mRightTextView != null) {
                mRightTextView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnCommonTextViewClickListener != null) {
                            mOnCommonTextViewClickListener.onRightViewClick();
                        }
                    }
                });
            }
        }
        return this;
    }

    /**
     * 单位转换工具类
     *
     * @param context  上下文对象
     * @param dipValue 值
     * @return 返回值
     */
    public int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 单位转换工具类
     *
     * @param context 上下文对象
     * @param pxValue 值
     * @return 返回值
     */
    public int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 单位转换工具类
     *
     * @param context 上下文对象
     * @param spValue 值
     * @return 返回值
     */
    public int sp2px(Context context, float spValue) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * scale + 0.5f);
    }

}
