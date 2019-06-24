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

import uk.co.chrisjenx.calligraphy.HasTypeface;

/**
 * 通用的textView可以实现大部分常用布局样式
 *
 * @author xuexiang
 * @since 2019/1/14 上午11:24
 */
public class CommonTextView extends RelativeLayout implements HasTypeface {

    private Context mContext;

    private int defaultColor = 0xFF373737;//文字默认颜色
    private int defaultDividerLineColor = 0xFFE8E8E8;//分割线默认颜色
    private int defaultSize;//默认字体大小
    private int defaultPadding;//默认边距

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
    private boolean useRipple;

    private boolean mSetSingleLine = true;
    private int mSetMaxEms = 10;
    private int mSetLines = 1;

    /**
     * TextView的Gravity
     */
    private static final int Gravity_Left_Center = 0;
    private static final int Gravity_Center = 1;
    private static final int Gravity_Right_Center = 2;

    private static final int DEFAULT_Gravity = 1;

    private int mLeftTextViewGravity;
    private int mCenterTextViewGravity;
    private int mRightTextViewGravity;

    private TextView leftTextView, centerTextView, rightTextView;
    private TextView leftTopTextView, centerTopTextView, rightTopTextView;
    private TextView leftBottomTextView, centerBottomTextView, rightBottomTextView;

    private ImageView leftImageView;
    //    private ImageView rightImageView;
    private View topLineView, bottomLineView;
    private View centerBaseLineView;

    private boolean mLeftViewIsClickable = false;
    private boolean mCenterViewIsClickable = false;
    private boolean mRightViewIsClickable = false;


    private LayoutParams leftTVParams, centerTVParams, rightTVParams, topLineParams, bottomLineParams;
    private LayoutParams leftTopTVParams, centerTopTVParams, rightTopTVParams;
    private LayoutParams leftBottomTVParams, centerBottomTVParams, rightBottomTVParams;
    private LayoutParams centerBaseLineParams;
    private LayoutParams leftIVParams;
//    private RelativeLayout.LayoutParams rightIVParams;

    private OnCommonTextViewClickListener onCommonTextViewClickListener;

    private Drawable mBackground_drawable;
    private boolean mIsCenterAlignLeft = false;
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
        defaultSize = dip2px(context, 13);
        defaultPadding = dip2px(context, 10);
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
        mLeft_drawableLeft = typedArray.getDrawable(R.styleable.CommonTextView_cLeftIconResForDrawableLeft);
        mLeft_drawableTop = typedArray.getDrawable(R.styleable.CommonTextView_cLeftIconResForDrawableTop);
        mLeft_drawableRight = typedArray.getDrawable(R.styleable.CommonTextView_cLeftIconResForDrawableRight);
        mLeft_drawableBottom = typedArray.getDrawable(R.styleable.CommonTextView_cLeftIconResForDrawableBottom);

        mCenter_drawableLeft = typedArray.getDrawable(R.styleable.CommonTextView_cCenterIconResForDrawableLeft);
        mCenter_drawableTop = typedArray.getDrawable(R.styleable.CommonTextView_cCenterIconResForDrawableTop);
        mCenter_drawableRight = typedArray.getDrawable(R.styleable.CommonTextView_cCenterIconResForDrawableRight);
        mCenter_drawableBottom = typedArray.getDrawable(R.styleable.CommonTextView_cCenterIconResForDrawableBottom);

        mRight_drawableLeft = typedArray.getDrawable(R.styleable.CommonTextView_cRightIconResForDrawableLeft);
        mRight_drawableTop = typedArray.getDrawable(R.styleable.CommonTextView_cRightIconResForDrawableTop);
        mRight_drawableRight = typedArray.getDrawable(R.styleable.CommonTextView_cRightIconResForDrawableRight);
        mRight_drawableBottom = typedArray.getDrawable(R.styleable.CommonTextView_cRightIconResForDrawableBottom);

        mLeft_IV_drawable = typedArray.getDrawable(R.styleable.CommonTextView_cLeftImageViewDrawableRes);
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

        mLeftTextColor = typedArray.getColor(R.styleable.CommonTextView_cLeftTextColor, defaultColor);
        mLeftTopTextColor = typedArray.getColor(R.styleable.CommonTextView_cLeftTopTextColor, defaultColor);
        mLeftBottomTextColor = typedArray.getColor(R.styleable.CommonTextView_cLeftBottomTextColor, defaultColor);

        mCenterTextColor = typedArray.getColor(R.styleable.CommonTextView_cCenterTextColor, defaultColor);
        mCenterTopTextColor = typedArray.getColor(R.styleable.CommonTextView_cCenterTopTextColor, defaultColor);
        mCenterBottomTextColor = typedArray.getColor(R.styleable.CommonTextView_cCenterBottomTextColor, defaultColor);

        mRightTextColor = typedArray.getColor(R.styleable.CommonTextView_cRightTextColor, defaultColor);
        mRightTopTextColor = typedArray.getColor(R.styleable.CommonTextView_cRightTopTextColor, defaultColor);
        mRightBottomTextColor = typedArray.getColor(R.styleable.CommonTextView_cRightBottomTextColor, defaultColor);

        mLeftTextSize = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cLeftTextSize, defaultSize);
        mLeftTopTextSize = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cLeftTopTextSize, defaultSize);
        mLeftBottomTextSize = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cLeftBottomTextSize, defaultSize);

        mCenterTextSize = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cCenterTextSize, defaultSize);
        mCenterTopTextSize = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cCenterTopTextSize, defaultSize);
        mCenterBottomTextSize = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cCenterBottomTextSize, defaultSize);

        mRightTextSize = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cRightTextSize, defaultSize);
        mRightTopTextSize = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cRightTopTextSize, defaultSize);
        mRightBottomTextSize = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cRightBottomTextSize, defaultSize);

        mLeftIconDrawablePadding = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cLeftIconDrawablePadding, defaultPadding);
        mCenterIconDrawablePadding = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cCenterIconDrawablePadding, defaultPadding);
        mRightIconDrawablePadding = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cRightIconDrawablePadding, defaultPadding);

        mLeftViewPaddingLeft = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cLeftViewPaddingLeft, defaultPadding);
        mLeftViewPaddingRight = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cLeftViewPaddingRight, defaultPadding);
        mCenterViewPaddingLeft = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cCenterViewPaddingLeft, defaultPadding);
        mCenterViewPaddingRight = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cCenterViewPaddingRight, defaultPadding);
        mRightViewPaddingLeft = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cRightViewPaddingLeft, defaultPadding);
        mRightViewPaddingRight = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cRightViewPaddingRight, defaultPadding);

        mBothDividerLineMarginLeft = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cBothDividerLineMarginLeft, 0);
        mBothDividerLineMarginRight = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cBothDividerLineMarginRight, 0);

        mTopDividerLineMarginLR = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cTopDividerLineMarginLR, 0);
        mTopDividerLineMarginLeft = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cTopDividerLineMarginLeft, 0);
        mTopDividerLineMarginRight = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cTopDividerLineMarginRight, 0);

        mBottomDividerLineMarginLR = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cBottomDividerLineMarginLR, 0);
        mBottomDividerLineMarginLeft = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cBottomDividerLineMarginLeft, 0);
        mBottomDividerLineMarginRight = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cBottomDividerLineMarginRight, 0);

        mLeftImageViewMarginLeft = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cLeftImageViewMarginLeft, defaultPadding);
//        mRightImageViewMarginRight = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cRightImageViewMarginRight, defaultPadding);

        mCenterSpaceHeight = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cCenterSpaceHeight, mCenterSpaceHeight);


        mLeftTextViewLineSpacingExtra = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cLeftTextViewLineSpacingExtra, 0);
        mCenterTextViewLineSpacingExtra = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cCenterTextViewLineSpacingExtra, 0);
        mRightTextViewLineSpacingExtra = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cRightTextViewLineSpacingExtra, 0);

        mDividerLineType = typedArray.getInt(R.styleable.CommonTextView_cShowDividerLineType, DEFAULT);
        mDividerLineColor = typedArray.getColor(R.styleable.CommonTextView_cDividerLineColor, defaultDividerLineColor);

        mDividerLineHeight = typedArray.getDimensionPixelSize(R.styleable.CommonTextView_cDividerLineHeight, dip2px(mContext, 0.5f));

        useRipple = typedArray.getBoolean(R.styleable.CommonTextView_cUseRipple, false);

        mBackgroundColor = typedArray.getColor(R.styleable.CommonTextView_cBackgroundColor, mBackgroundColor);

        mSetSingleLine = typedArray.getBoolean(R.styleable.CommonTextView_cSetSingleLine, true);
        mSetMaxEms = typedArray.getInt(R.styleable.CommonTextView_cSetMaxEms, mSetMaxEms);
        mSetLines = typedArray.getInt(R.styleable.CommonTextView_cSetLines, 1);

        mLeftTextViewGravity = typedArray.getInt(R.styleable.CommonTextView_cLeftTextViewGravity, DEFAULT_Gravity);
        mCenterTextViewGravity = typedArray.getInt(R.styleable.CommonTextView_cCenterTextViewGravity, DEFAULT_Gravity);
        mRightTextViewGravity = typedArray.getInt(R.styleable.CommonTextView_cRightTextViewGravity, DEFAULT_Gravity);

        mLeftViewIsClickable = typedArray.getBoolean(R.styleable.CommonTextView_cLeftViewIsClickable, false);
        mCenterViewIsClickable = typedArray.getBoolean(R.styleable.CommonTextView_cCenterViewIsClickable, false);
        mRightViewIsClickable = typedArray.getBoolean(R.styleable.CommonTextView_cRightViewIsClickable, false);

        mBackground_drawable = typedArray.getDrawable(R.styleable.CommonTextView_cBackgroundDrawableRes);

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
        if (useRipple) {
            this.setBackgroundResource(R.drawable.stv_btn_selector_white);
        }
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCommonTextViewClickListener != null) {
                    onCommonTextViewClickListener.onCommonTextViewClick();
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
        if (centerBaseLineView == null) {
            if (centerBaseLineParams == null) {
                centerBaseLineParams = new LayoutParams(LayoutParams.MATCH_PARENT, mCenterSpaceHeight);
                centerBaseLineParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
            }
            centerBaseLineView = new View(mContext);

            centerBaseLineView.setId(R.id.cCenterBaseLineId);
            centerBaseLineView.setLayoutParams(centerBaseLineParams);
        }
        addView(centerBaseLineView);
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
        if (topLineView == null) {
            if (topLineParams == null) {
                topLineParams = new LayoutParams(LayoutParams.MATCH_PARENT, mDividerLineHeight);
            }
            topLineParams.addRule(ALIGN_PARENT_TOP, TRUE);
            topLineParams.setMargins(marginLeft, 0, marginRight, 0);
            topLineView = new View(mContext);
            topLineView.setLayoutParams(topLineParams);
            topLineView.setBackgroundColor(mDividerLineColor);
        }
        addView(topLineView);
    }

    /**
     * 设置底部分割线view
     *
     * @param marginLeft  左间距
     * @param marginRight 右间距
     */
    private void initBottomLineView(int marginLeft, int marginRight) {
        if (bottomLineView == null) {
            if (bottomLineParams == null) {
                bottomLineParams = new LayoutParams(LayoutParams.MATCH_PARENT, mDividerLineHeight);
            }
            bottomLineParams.addRule(ALIGN_PARENT_BOTTOM, TRUE);
            bottomLineParams.setMargins(marginLeft, 0, marginRight, 0);

            bottomLineView = new View(mContext);
            bottomLineView.setLayoutParams(bottomLineParams);
            bottomLineView.setBackgroundColor(mDividerLineColor);
        }
        addView(bottomLineView);
    }

    /**
     * 初始化左边ImageView
     * 主要是为了便于使用第三方图片框架获取网络图片使用
     */
    private void initLeftImageView() {
        leftImageView = new ImageView(mContext);
        leftIVParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        leftIVParams.addRule(ALIGN_PARENT_LEFT, TRUE);
        leftIVParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
        leftIVParams.setMargins(mLeftImageViewMarginLeft, 0, 0, 0);
        leftImageView.setScaleType(ImageView.ScaleType.CENTER);
        leftImageView.setId(R.id.cLeftImageViewId);
        leftImageView.setLayoutParams(leftIVParams);
        if (mLeft_IV_drawable != null) {
            leftImageView.setImageDrawable(mLeft_IV_drawable);
        }
        addView(leftImageView);
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
        if (leftTextView == null) {
            if (leftTVParams == null) {
                leftTVParams = getParams(leftTVParams);
            }
            leftTVParams.addRule(CENTER_VERTICAL, TRUE);
            leftTVParams.addRule(RIGHT_OF, R.id.cLeftImageViewId);
            leftTVParams.setMargins(mLeftViewPaddingLeft, 0, mLeftViewPaddingRight, 0);
            leftTextView = initText(leftTextView, leftTVParams, R.id.cLeftTextId, mLeftTextColor, mLeftTextSize);
            leftTextView.setText(mLeftTextString);
            leftTextView.setLineSpacing(mLeftTextViewLineSpacingExtra, 1.0f);
            setTextViewGravity(leftTextView, mLeftTextViewGravity);
            if (mLeftViewIsClickable) {
                leftTextView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onCommonTextViewClickListener != null) {
                            onCommonTextViewClickListener.onLeftViewClick();
                        }
                    }
                });
            }
        }

        setDrawable(leftTextView, mLeft_drawableLeft, mLeft_drawableTop, mLeft_drawableRight, mLeft_drawableBottom, mLeftIconDrawablePadding);

    }

    /**
     * 初始化左上textView
     */
    private void initLeftTopText() {
        if (leftTopTextView == null) {
            if (leftTopTVParams == null) {
                leftTopTVParams = getParams(leftTopTVParams);
            }
            leftTopTVParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
            leftTopTVParams.addRule(ABOVE, R.id.cCenterBaseLineId);
            leftTopTVParams.addRule(RIGHT_OF, R.id.cLeftImageViewId);
            leftTopTVParams.setMargins(mLeftViewPaddingLeft, 0, mLeftViewPaddingRight, 0);
            leftTopTextView = initText(leftTopTextView, leftTopTVParams, R.id.cLeftTopTextId, mLeftTopTextColor, mLeftTopTextSize);
            leftTopTextView.setText(mLeftTopTextString);
            setTextViewGravity(leftTopTextView, mLeftTextViewGravity);

        }

    }

    /**
     * 初始化左下textView
     */
    private void initLeftBottomText() {
        if (leftBottomTextView == null) {
            if (leftBottomTVParams == null) {
                leftBottomTVParams = getParams(leftBottomTVParams);
            }
            leftBottomTVParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
            leftBottomTVParams.addRule(BELOW, R.id.cCenterBaseLineId);
            leftBottomTVParams.addRule(RIGHT_OF, R.id.cLeftImageViewId);
            leftBottomTVParams.setMargins(mLeftViewPaddingLeft, 0, mLeftViewPaddingRight, 0);
            leftBottomTextView = initText(leftBottomTextView, leftBottomTVParams, R.id.cLeftBottomTextId, mLeftBottomTextColor, mLeftBottomTextSize);
            leftBottomTextView.setText(mLeftBottomTextString);
            setTextViewGravity(leftBottomTextView, mLeftTextViewGravity);

        }

    }

    /**
     * 初始化中间textView
     */
    private void initCenterText() {
        if (centerTextView == null) {
            if (centerTVParams == null) {
                if (mIsCenterAlignLeft) {
                    centerTVParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                } else {
                    centerTVParams = getParams(centerTVParams);
                }
            }

            centerTVParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
            centerTVParams.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);

            if (mIsCenterAlignLeft) {
                centerTextView = initText(centerTextView, centerTVParams, R.id.cCenterTextId, mCenterTextColor, mCenterTextSize);
                centerTVParams.setMargins(mCenterViewMarginLeft, 0, mCenterViewPaddingRight, 0);
                setTextViewGravity(centerTextView, Gravity_Left_Center);
            } else {
                centerTextView = initText(centerTextView, centerTVParams, R.id.cCenterTextId, mCenterTextColor, mCenterTextSize);
                centerTVParams.setMargins(mCenterViewPaddingLeft, 0, mCenterViewPaddingRight, 0);
                setTextViewGravity(centerTextView, mCenterTextViewGravity);
            }

            centerTextView.setText(mCenterTextString);
            centerTextView.setLineSpacing(mCenterTextViewLineSpacingExtra, 1.0f);

            if (mCenterViewIsClickable) {
                centerTextView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onCommonTextViewClickListener != null) {
                            onCommonTextViewClickListener.onCenterViewClick();
                        }
                    }
                });
            }

        }
        setDrawable(centerTextView, mCenter_drawableLeft, mCenter_drawableTop, mCenter_drawableRight, mCenter_drawableBottom, mCenterIconDrawablePadding);

    }

    /**
     * 初始化中上textView
     */
    private void initCenterTopText() {
        if (centerTopTextView == null) {
            if (centerTopTVParams == null) {
                centerTopTVParams = getParams(centerTopTVParams);
            }
            centerTopTVParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
            centerTopTVParams.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
            centerTopTVParams.addRule(ABOVE, R.id.cCenterBaseLineId);
            centerTopTVParams.setMargins(mCenterViewPaddingLeft, 0, mCenterViewPaddingRight, 0);
            centerTopTextView = initText(centerTopTextView, centerTopTVParams, R.id.cCenterTopTextId, mCenterTopTextColor, mCenterTopTextSize);
            centerTopTextView.setText(mCenterTopTextString);
            centerTopTextView.setLineSpacing(mCenterTextViewLineSpacingExtra, 1.0f);
            setTextViewGravity(centerTopTextView, mCenterTextViewGravity);
        }

    }

    /**
     * 初始化中下textView
     */
    private void initCenterBottomText() {
        if (centerBottomTextView == null) {
            if (centerBottomTVParams == null) {
                centerBottomTVParams = getParams(centerBottomTVParams);
            }
            centerBottomTVParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
            centerBottomTVParams.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
            centerBottomTVParams.addRule(BELOW, R.id.cCenterBaseLineId);
            centerBottomTVParams.setMargins(mCenterViewPaddingLeft, 0, mCenterViewPaddingRight, 0);
            centerBottomTextView = initText(centerBottomTextView, centerBottomTVParams, R.id.cCenterBottomTextId, mCenterBottomTextColor, mCenterBottomTextSize);
            centerBottomTextView.setText(mCenterBottomTextString);
            centerBottomTextView.setLineSpacing(mCenterTextViewLineSpacingExtra, 1.0f);
            setTextViewGravity(centerBottomTextView, mCenterTextViewGravity);
        }

    }


    /**
     * 初始化右边textView
     */
    private void initRightText() {
        if (rightTextView == null) {
            if (rightTVParams == null) {
                rightTVParams = getParams(rightTVParams);
            }

            rightTVParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
            rightTVParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, TRUE);
            rightTVParams.addRule(RelativeLayout.LEFT_OF, R.id.cRightImageViewId);
            rightTVParams.setMargins(mRightViewPaddingLeft, 0, mRightViewPaddingRight, 0);
            rightTextView = initText(rightTextView, rightTVParams, R.id.cRightTextId, mRightTextColor, mRightTextSize);
            rightTextView.setText(mRightTextString);
//            rightTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
            rightTextView.setLineSpacing(mRightTextViewLineSpacingExtra, 1.0f);
            setTextViewGravity(rightTextView, mRightTextViewGravity);
            if (mRightViewIsClickable) {
                rightTextView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onCommonTextViewClickListener != null) {
                            onCommonTextViewClickListener.onRightViewClick();
                        }
                    }
                });
            }

        }
        setDrawable(rightTextView, mRight_drawableLeft, mRight_drawableTop, mRight_drawableRight, mRight_drawableBottom, mRightIconDrawablePadding);

    }

    /**
     * 初始化右上textView
     */
    private void initRightTopText() {
        if (rightTopTextView == null) {
            if (rightTopTVParams == null) {
                rightTopTVParams = getParams(rightTopTVParams);
            }
            rightTopTVParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
            rightTopTVParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, TRUE);
            rightTopTVParams.addRule(ABOVE, R.id.cCenterBaseLineId);
            rightTopTVParams.addRule(RelativeLayout.LEFT_OF, R.id.cRightImageViewId);
            rightTopTVParams.setMargins(mRightViewPaddingLeft, 0, mRightViewPaddingRight, 0);
            rightTopTextView = initText(rightTopTextView, rightTopTVParams, R.id.cRightTopTextId, mRightTopTextColor, mRightTopTextSize);
            rightTopTextView.setText(mRightTopTextString);
            rightTopTextView.setLineSpacing(mRightTextViewLineSpacingExtra, 1.0f);
            setTextViewGravity(rightTopTextView, mRightTextViewGravity);

        }

    }

    /**
     * 初始化右下textView
     */
    private void initRightBottomText() {
        if (rightBottomTextView == null) {
            if (rightBottomTVParams == null) {
                rightBottomTVParams = getParams(rightBottomTVParams);
            }
            rightBottomTVParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
            rightBottomTVParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, TRUE);
            rightBottomTVParams.addRule(BELOW, R.id.cCenterBaseLineId);
            rightBottomTVParams.addRule(RelativeLayout.LEFT_OF, R.id.cRightImageViewId);
            rightBottomTVParams.setMargins(mRightViewPaddingLeft, 0, mRightViewPaddingRight, 0);
            rightBottomTextView = initText(rightBottomTextView, rightBottomTVParams, R.id.cRightBottomTextId, mRightBottomTextColor, mRightBottomTextSize);
            rightBottomTextView.setText(mRightBottomTextString);
            rightBottomTextView.setLineSpacing(mRightTextViewLineSpacingExtra, 1.0f);
            setTextViewGravity(rightBottomTextView, mRightTextViewGravity);
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
            textView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mSetMaxEms) });
            addView(textView);
        }
        return textView;
    }

    private void setTextViewGravity(TextView textView, int gravity_type) {

        switch (gravity_type) {
            case Gravity_Left_Center:
                textView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                break;
            case Gravity_Center:
                textView.setGravity(Gravity.CENTER);
                break;
            case Gravity_Right_Center:
                textView.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
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
        if (leftTextView == null) {
            initLeftText();
        }
        leftTextView.setText(string);
        return this;
    }

    /**
     * 设置左上文字字符串
     *
     * @param string 字符串
     * @return 返回
     */
    public CommonTextView setLeftTopTextString(CharSequence string) {
        if (leftTopTextView == null) {
            initLeftTopText();
        }
        leftTopTextView.setText(string);
        return this;
    }

    /**
     * 设置左下文字字符串
     *
     * @param string 字符串
     * @return 返回
     */
    public CommonTextView setLeftBottomTextString(CharSequence string) {
        if (leftBottomTextView == null) {
            initLeftBottomText();
        }
        leftBottomTextView.setText(string);
        return this;
    }

    /**
     * 设置左边文字字体大小
     *
     * @param size 字体大小
     * @return 返回
     */
    public CommonTextView setLeftTextSize(float size) {
        if (leftTextView == null) {
            initLeftText();
        }
        leftTextView.setTextSize(size);
        return this;
    }

    /**
     * set Left TextColor
     *
     * @param color textColor
     * @return return
     */
    public CommonTextView setLeftTextColor(int color) {
        if (leftTextView == null) {
            initLeftText();
        }
        leftTextView.setTextColor(color);
        return this;
    }

    /**
     * 设置中间文字字符串
     *
     * @param string 字符串
     * @return 返回
     */
    public CommonTextView setCenterTextString(CharSequence string) {
        if (centerTextView == null) {
            initCenterText();
        }
        centerTextView.setText(string);
        return this;
    }

    /**
     * 设置中上文字字符串
     *
     * @param string 字符串
     * @return 返回
     */
    public CommonTextView setCenterTopTextString(CharSequence string) {
        if (centerTopTextView == null) {
            initCenterTopText();
        }
        centerTopTextView.setText(string);
        return this;
    }

    /**
     * 设置中下文字字符串
     *
     * @param string 字符串
     * @return 返回
     */
    public CommonTextView setCenterBottomTextString(CharSequence string) {
        if (centerBottomTextView == null) {
            initCenterBottomText();
        }
        centerBottomTextView.setText(string);
        return this;
    }

    /**
     * 设置中间文字字体大小
     *
     * @param size 字体大小
     * @return 返回
     */
    public CommonTextView setCenterTextSize(float size) {
        if (centerTextView == null) {
            initCenterText();
        }
        centerTextView.setTextSize(size);
        return this;
    }

    /**
     * 设置中间文字颜色值
     *
     * @param color 颜色值
     * @return 返回
     */
    public CommonTextView setCenterTextColor(int color) {
        if (centerTextView == null) {
            initCenterText();
        }
        centerTextView.setTextColor(color);
        return this;
    }

    /**
     * 设置右边文字字符串
     *
     * @param string 字符串
     * @return 返回
     */
    public CommonTextView setRightTextString(CharSequence string) {
        if (rightTextView == null) {
            initRightText();
        }
        rightTextView.setText(string);
        return this;
    }

    /**
     * 设置右上文字字符串
     *
     * @param string 字符串
     * @return 返回
     */
    public CommonTextView setRightTopTextString(CharSequence string) {
        if (rightTopTextView == null) {
            initRightTopText();
        }
        rightTopTextView.setText(string);
        return this;
    }

    /**
     * 设置右下文字字符串
     *
     * @param string 字符串
     * @return 返回
     */
    public CommonTextView setRightBottomTextString(CharSequence string) {
        if (rightBottomTextView == null) {
            initRightBottomText();
        }
        rightBottomTextView.setText(string);
        return this;
    }

    /**
     * 设置右边文字的字体大小
     *
     * @param size 字体大小
     * @return 返回
     */
    public CommonTextView setRightTextSize(float size) {
        if (rightTextView == null) {
            initRightText();
        }
        rightTextView.setTextSize(size);
        return this;
    }

    /**
     * 设置右边文字的颜色
     *
     * @param color 颜色值
     * @return 返回
     */
    public CommonTextView setRightTextColor(int color) {
        if (rightTextView == null) {
            initRightText();
        }
        rightTextView.setTextColor(color);
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
        if (leftTextView == null) {
            initLeftText();
        }
        leftTextView.setCompoundDrawables(drawableLeft, null, null, null);
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
        if (leftTextView == null) {
            initLeftText();
        }
        leftTextView.setCompoundDrawables(null, drawableTop, null, null);
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
        if (leftTextView == null) {
            initLeftText();
        }
        leftTextView.setCompoundDrawables(null, null, drawableRight, null);
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
        if (leftTextView == null) {
            initLeftText();
        }
        leftTextView.setCompoundDrawables(null, null, null, drawableBottom);
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
        if (centerTextView == null) {
            initCenterText();
        }
        centerTextView.setCompoundDrawables(drawableLeft, null, null, null);
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
        if (centerTextView == null) {
            initCenterText();
        }
        centerTextView.setCompoundDrawables(null, drawableTop, null, null);
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
        if (centerTextView == null) {
            initCenterText();
        }
        centerTextView.setCompoundDrawables(null, null, drawableRight, null);
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
        if (centerTextView == null) {
            initCenterText();
        }
        centerTextView.setCompoundDrawables(null, null, null, drawableBottom);
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
        if (rightTextView == null) {
            initRightText();
        }
        rightTextView.setCompoundDrawables(drawableLeft, null, null, null);
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
        if (rightTextView == null) {
            initRightText();
        }
        rightTextView.setCompoundDrawables(null, drawableTop, null, null);
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
        if (rightTextView == null) {
            initRightText();
        }
        rightTextView.setCompoundDrawables(null, null, drawableRight, null);
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
        if (rightTextView == null) {
            initRightText();
        }
        rightTextView.setCompoundDrawables(null, null, null, drawableBottom);
        return this;
    }

    /**
     * 获取左边imageView
     *
     * @return 返回imageView对象
     */
    public ImageView getLeftImageView() {
        if (leftImageView == null) {
            initLeftImageView();
        }
        return leftImageView;
    }

    /**
     * 获取左边textView的值
     *
     * @return 返回
     */
    public CharSequence getLeftTextString() {
        return leftTextView != null ? leftTextView.getText() : "";
    }

    /**
     * 获取左上textView的值
     *
     * @return 返回
     */
    public CharSequence getLeftTopTextString() {
        return leftTopTextView != null ? leftTopTextView.getText() : "";
    }

    /**
     * 获取左下textView的值
     *
     * @return 返回
     */
    public CharSequence getLeftBottomTextString() {
        return leftBottomTextView != null ? leftBottomTextView.getText() : "";
    }

    /**
     * 获取中间textView的值
     *
     * @return 返回
     */
    public CharSequence getCenterTextString() {
        return centerTextView != null ? centerTextView.getText() : "";
    }

    /**
     * 获取中上textView的值
     *
     * @return 返回
     */
    public CharSequence getCenterTopTextString() {
        return centerTopTextView != null ? centerTopTextView.getText() : "";
    }

    /**
     * 获取中下textView的值
     *
     * @return 返回
     */
    public CharSequence getCenterBottomTextString() {
        return centerBottomTextView != null ? centerBottomTextView.getText() : "";
    }

    /**
     * 获取右边textView的值
     *
     * @return 返回
     */
    public CharSequence getRightTextString() {
        return rightTextView != null ? rightTextView.getText() : "";
    }

    /**
     * 获取右上textView的值
     *
     * @return 返回
     */
    public CharSequence getRightTopTextString() {
        return rightTopTextView != null ? rightTopTextView.getText() : "";
    }

    /**
     * 获取右下textView的值
     *
     * @return 返回
     */
    public CharSequence getRightBottomTextString() {
        return rightBottomTextView != null ? rightBottomTextView.getText() : "";
    }

    @Override
    public void setTypeface(Typeface typeface) {
        if (leftTextView != null) {
            leftTextView.setTypeface(typeface);
        }
        if (centerTextView != null) {
            centerTextView.setTypeface(typeface);
        }
        if (rightTextView != null) {
            rightTextView.setTypeface(typeface);
        }
        if (leftTopTextView != null) {
            leftTopTextView.setTypeface(typeface);
        }
        if (centerTopTextView != null) {
            centerTopTextView.setTypeface(typeface);
        }
        if (rightTopTextView != null) {
            rightTopTextView.setTypeface(typeface);
        }
        if (leftBottomTextView != null) {
            leftBottomTextView.setTypeface(typeface);
        }
        if (centerBottomTextView != null) {
            centerBottomTextView.setTypeface(typeface);
        }
        if (rightBottomTextView != null) {
            rightBottomTextView.setTypeface(typeface);
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
        this.onCommonTextViewClickListener = listener;
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
            if (leftTextView != null) {
                leftTextView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onCommonTextViewClickListener != null) {
                            onCommonTextViewClickListener.onLeftViewClick();
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
            if (centerTextView != null) {
                centerTextView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onCommonTextViewClickListener != null) {
                            onCommonTextViewClickListener.onCenterViewClick();
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
            if (rightTextView != null) {
                rightTextView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onCommonTextViewClickListener != null) {
                            onCommonTextViewClickListener.onRightViewClick();
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
