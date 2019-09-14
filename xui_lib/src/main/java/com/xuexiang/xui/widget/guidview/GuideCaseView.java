package com.xuexiang.xui.widget.guidview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.Spanned;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import android.support.annotation.AttrRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;

import com.xuexiang.xui.R;
import com.xuexiang.xui.XUI;

/**
 * @author xuexiang
 * @since 2018/11/29 上午12:47
 */
public class GuideCaseView extends FrameLayout implements ViewTreeObserver.OnGlobalLayoutListener {

    GuideCaseView(@NonNull Context context) {
        super(context);
    }

    GuideCaseView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    GuideCaseView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    GuideCaseView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    // Tag for container view
    private static final String CONTAINER_TAG = "ShowCaseViewTag";
    // SharedPreferences name
    private static final String PREF_NAME = "PrefShowCaseView";

    /**
     * 设置已经显示
     *
     * @param context
     * @param id
     */
    public static void setShowOnce(Context context, String id) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        sharedPrefs.edit().putBoolean(id, true).apply();
    }

    /**
     * 是否已显示
     *
     * @param context
     * @param id
     * @return
     */
    public static boolean isShowOnce(Context context, String id) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPrefs.getBoolean(id, false);
    }

    /**
     * Resets the show once flag
     *
     * @param context context that should be used to create the shared preference instance
     * @param id      id of the show once flag that should be reset
     */
    public static void resetShowOnce(Context context, String id) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        sharedPrefs.edit().remove(id).apply();
    }

    /**
     * Resets all show once flags
     *
     * @param context context that should be used to create the shared preference instance
     */
    public static void resetAllShowOnce(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        sharedPrefs.edit().clear().apply();
    }

    /**
     * Builder parameters
     */
    private Activity mActivity;
    private String mTitle;
    private Spanned mSpannedTitle;
    private String mId;
    private double mFocusCircleRadiusFactor;
    private View mView;
    private int mBackgroundColor;
    private int mFocusBorderColor;
    private int mTitleGravity;
    private int mTitleStyle;
    private int mTitleSize;
    private int mTitleSizeUnit;
    private int mPictureResId;
    private int mPictureWidth;
    private int mPictureHeight;
    private int mCustomViewRes;
    private int mFocusBorderSize;
    private int mRoundRectRadius;
    private OnViewInflateListener mViewInflateListener;
    private Animation mEnterAnimation, mExitAnimation;
    private boolean mCloseOnTouch;
    private boolean mFitSystemWindows;
    private int mAdjustHeight;
    private FocusShape mFocusShape;
    private DismissListener mDismissListener;


    private int mAnimationDuration = 400;
    private int mFocusAnimationMaxValue;
    private int mFocusAnimationStep;
    private int mCenterX, mCenterY;
    private ViewGroup mRoot;
    private SharedPreferences mSharedPreferences;
    private Calculator mCalculator;

    private int mFocusPositionX, mFocusPositionY, mFocusCircleRadius, mFocusRectangleWidth, mFocusRectangleHeight;

    private boolean mFocusAnimationEnabled;

    public GuideCaseView(Builder builder) {
        this(builder.mActivity, builder.mView, builder.mId, builder.mTitle, builder.mSpannedTitle, builder.mTitleGravity, builder.mTitleStyle, builder.mTitleSize, builder.mTitleSizeUnit,
                builder.mFocusCircleRadiusFactor, builder.mBackgroundColor, builder.mFocusBorderColor, builder.mFocusBorderSize, builder.mCustomViewRes, builder.mViewInflateListener,
                builder.mEnterAnimation, builder.mExitAnimation, builder.mCloseOnTouch, builder.mFitSystemWindows, builder.mAdjustHeight, builder.mFocusShape, builder.mDismissListener, builder.mRoundRectRadius,
                builder.mPictureResId, builder.mPictureWidth, builder.mPictureHeight, builder.mFocusPositionX, builder.mFocusPositionY, builder.mFocusCircleRadius, builder.mFocusRectangleWidth,
                builder.mFocusRectangleHeight, builder.mFocusAnimationEnabled, builder.mFocusAnimationMaxValue, builder.mFocusAnimationStep);
    }

    /**
     * Constructor for GuideCaseView
     *
     * @param activity                Activity to show GuideCaseView in
     * @param view                    view to focus
     * @param id                      unique identifier for GuideCaseView
     * @param title                   title text
     * @param spannedTitle            title text if spanned text should be used
     * @param titleGravity            title gravity
     * @param titleStyle              title text style
     * @param titleSize               title text size
     * @param titleSizeUnit           title text size unit
     * @param focusCircleRadiusFactor focus circle radius factor (default value = 1)
     * @param backgroundColor         background color of GuideCaseView
     * @param focusBorderColor        focus border color of GuideCaseView
     * @param focusBorderSize         focus border size of GuideCaseView
     * @param customViewRes           custom view layout resource
     * @param viewInflateListener     inflate listener for custom view
     * @param enterAnimation          enter animation for GuideCaseView
     * @param exitAnimation           exit animation for GuideCaseView
     * @param closeOnTouch            closes on touch if enabled
     * @param fitSystemWindows        should be the same value of root view's fitSystemWindows value
     * @param focusShape              shape of focus, can be circle or rounded rectangle
     * @param dismissListener         listener that gets notified when showcase is dismissed
     * @param roundRectRadius         round rectangle radius
     * @param focusPositionX          focus at specific position X coordinate
     * @param focusPositionY          focus at specific position Y coordinate
     * @param focusCircleRadius       focus at specific position circle radius
     * @param focusRectangleWidth     focus at specific position rectangle width
     * @param focusRectangleHeight    focus at specific position rectangle height
     * @param animationEnabled        flag to enable/disable animation
     */
    private GuideCaseView(Activity activity, View view, String id, String title, Spanned spannedTitle,
                          int titleGravity, int titleStyle, int titleSize, int titleSizeUnit, double focusCircleRadiusFactor,
                          int backgroundColor, int focusBorderColor, int focusBorderSize, int customViewRes,
                          OnViewInflateListener viewInflateListener, Animation enterAnimation,
                          Animation exitAnimation, boolean closeOnTouch, boolean fitSystemWindows, int adjustHeight,
                          FocusShape focusShape, DismissListener dismissListener, int roundRectRadius, int pictureResId, int pictureWidth, int pictureHeight,
                          int focusPositionX, int focusPositionY, int focusCircleRadius, int focusRectangleWidth, int focusRectangleHeight,
                          final boolean animationEnabled, int focusAnimationMaxValue, int focusAnimationStep) {
        super(activity);
        mId = id;
        mActivity = activity;
        mView = view;
        mTitle = title;
        mSpannedTitle = spannedTitle;
        mFocusCircleRadiusFactor = focusCircleRadiusFactor;
        mBackgroundColor = backgroundColor;
        mFocusBorderColor = focusBorderColor;
        mFocusBorderSize = focusBorderSize;
        mTitleGravity = titleGravity;
        mTitleStyle = titleStyle;
        mTitleSize = titleSize;
        mTitleSizeUnit = titleSizeUnit;
        mRoundRectRadius = roundRectRadius;
        mPictureResId = pictureResId;
        mPictureWidth = pictureWidth;
        mPictureHeight = pictureHeight;
        mCustomViewRes = customViewRes;
        mViewInflateListener = viewInflateListener;
        mEnterAnimation = enterAnimation;
        mExitAnimation = exitAnimation;
        mCloseOnTouch = closeOnTouch;
        mFitSystemWindows = fitSystemWindows;
        mAdjustHeight = adjustHeight;
        mFocusShape = focusShape;
        mDismissListener = dismissListener;
        mFocusPositionX = focusPositionX;
        mFocusPositionY = focusPositionY;
        mFocusCircleRadius = focusCircleRadius;
        mFocusRectangleWidth = focusRectangleWidth;
        mFocusRectangleHeight = focusRectangleHeight;
        mFocusAnimationEnabled = animationEnabled;
        mFocusAnimationMaxValue = focusAnimationMaxValue;
        mFocusAnimationStep = focusAnimationStep;

        initializeParameters();
    }

    /**
     * Calculates and set initial parameters
     */
    private void initializeParameters() {
        mBackgroundColor = mBackgroundColor != 0 ? mBackgroundColor :
                mActivity.getResources().getColor(R.color.default_guide_case_view_background_color);
        mTitleGravity = mTitleGravity >= 0 ? mTitleGravity : Gravity.CENTER;
        mTitleStyle = mTitleStyle != 0 ? mTitleStyle : R.style.DefaultGuideCaseTitleStyle;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int deviceWidth = displayMetrics.widthPixels;
        int deviceHeight = displayMetrics.heightPixels;
        mCenterX = deviceWidth / 2;
        mCenterY = deviceHeight / 2;
        mSharedPreferences = mActivity.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Shows GuideCaseView
     */
    public void show() {
        if (mActivity == null || (mId != null && isShownBefore())) {
            if (mDismissListener != null) {
                mDismissListener.onSkipped(mId);
            }
            return;
        }

        if (mView != null) {
            // if view is not laid out get width/height values in onGlobalLayout
            if (mView.getWidth() == 0 && mView.getHeight() == 0) {
                mView.getViewTreeObserver().addOnGlobalLayoutListener(this);
            } else {
                focus();
            }
        } else {
            focus();
        }
    }

    private void focus() {
        mCalculator = new Calculator(mActivity, mFocusShape, mView, mFocusCircleRadiusFactor,
                mFitSystemWindows, mAdjustHeight);

        ViewGroup androidContent = (ViewGroup) mActivity.findViewById(android.R.id.content);
        mRoot = (ViewGroup) androidContent.getParent().getParent();
        GuideCaseView visibleView = (GuideCaseView) mRoot.findViewWithTag(CONTAINER_TAG);
        setClickable(true);
        if (visibleView == null) {
            setTag(CONTAINER_TAG);
            if (mCloseOnTouch) {
                setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hide();
                    }
                });
            }
            setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            mRoot.addView(this);


            GuideImageView imageView = new GuideImageView(mActivity);
            imageView.setFocusAnimationParameters(mFocusAnimationMaxValue, mFocusAnimationStep);
            if (mCalculator.hasFocus()) {
                mCenterX = mCalculator.getCircleCenterX();
                mCenterY = mCalculator.getCircleCenterY();
            }


            imageView.setParameters(mBackgroundColor, mCalculator);
            if (mFocusRectangleWidth > 0 && mFocusRectangleHeight > 0) {
                mCalculator.setRectPosition(mFocusPositionX, mFocusPositionY, mFocusRectangleWidth, mFocusRectangleHeight);
            }
            if (mFocusCircleRadius > 0) {
                mCalculator.setCirclePosition(mFocusPositionX, mFocusPositionY, mFocusCircleRadius);
            }
            imageView.setAnimationEnabled(mFocusAnimationEnabled);
            imageView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            if (mFocusBorderColor != 0 && mFocusBorderSize > 0) {
                imageView.setBorderParameters(mFocusBorderColor, mFocusBorderSize);
            }
            if (mRoundRectRadius > 0) {
                imageView.setRoundRectRadius(mRoundRectRadius);
            }
            addView(imageView);

            if (mCustomViewRes == 0) {
                if (mPictureResId == 0) {
                    inflateTitleView();
                } else {
                    inflatePicture();
                }
            } else {
                inflateCustomView(mCustomViewRes, mViewInflateListener);
            }

            startEnterAnimation();
            writeShown();
        }
    }

    /**
     * Check is GuideCaseView visible
     *
     * @param activity should be used to find GuideCaseView inside it
     */
    public static Boolean isVisible(Activity activity) {
        ViewGroup androidContent = (ViewGroup) activity.findViewById(android.R.id.content);
        ViewGroup mRoot = (ViewGroup) androidContent.getParent().getParent();
        GuideCaseView mContainer = (GuideCaseView) mRoot.findViewWithTag(CONTAINER_TAG);
        return mContainer != null;
    }

    /**
     * Hide  GuideCaseView
     *
     * @param activity should be used to hide GuideCaseView inside it
     */
    public static void hideCurrent(Activity activity) {
        ViewGroup androidContent = (ViewGroup) activity.findViewById(android.R.id.content);
        ViewGroup mRoot = (ViewGroup) androidContent.getParent().getParent();
        GuideCaseView mContainer = (GuideCaseView) mRoot.findViewWithTag(CONTAINER_TAG);
        mContainer.hide();
    }

    /**
     * Starts enter animation of GuideCaseView
     */
    private void startEnterAnimation() {
        if (mEnterAnimation != null) {
            startAnimation(mEnterAnimation);
        } else if (Utils.shouldShowCircularAnimation()) {
            doCircularEnterAnimation();
        } else {
            Animation fadeInAnimation = AnimationUtils.loadAnimation(mActivity, R.anim.gcv_fade_in);
            fadeInAnimation.setFillAfter(true);
            startAnimation(fadeInAnimation);
        }
    }

    /**
     * Hides GuideCaseView with animation
     */
    public void hide() {
        if (mExitAnimation != null) {
            startAnimation(mExitAnimation);
        } else if (Utils.shouldShowCircularAnimation()) {
            doCircularExitAnimation();
        } else {
            Animation fadeOut = AnimationUtils.loadAnimation(mActivity, R.anim.gcv_fade_out);
            fadeOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    removeView();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            fadeOut.setFillAfter(true);
            startAnimation(fadeOut);
        }
    }

    /**
     * Inflates custom view
     *
     * @param layout              layout for custom view
     * @param viewInflateListener inflate listener for custom view
     */
    private void inflateCustomView(@LayoutRes int layout, OnViewInflateListener viewInflateListener) {
        View view = mActivity.getLayoutInflater().inflate(layout, this, false);
        this.addView(view);
        if (viewInflateListener != null) {
            viewInflateListener.onViewInflated(view);
        }
    }

    /**
     * Inflates title view layout
     */
    private void inflateTitleView() {
        inflateCustomView(R.layout.gcv_layout_title, new OnViewInflateListener() {
            @Override
            public void onViewInflated(View view) {
                TextView textView = (TextView) view.findViewById(R.id.gcv_title);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    textView.setTextAppearance(mTitleStyle);
                } else {
                    textView.setTextAppearance(mActivity, mTitleStyle);
                }
                if (mTitleSize != -1) {
                    textView.setTextSize(mTitleSizeUnit, mTitleSize);
                }
                textView.setGravity(mTitleGravity);
                textView.setTypeface(XUI.getDefaultTypeface());
                if (mSpannedTitle != null) {
                    textView.setText(mSpannedTitle);
                } else {
                    textView.setText(mTitle);
                }
            }
        });
    }

    /**
     * Inflates picture view layout
     */
    private void inflatePicture() {
        inflateCustomView(R.layout.gcv_layout_image, new OnViewInflateListener() {
            @Override
            public void onViewInflated(View view) {
                ImageView imageView = (ImageView) view.findViewById(R.id.gcv_img);
                imageView.setImageResource(mPictureResId);
                if (mPictureHeight != 0 || mPictureWidth != 0) {
                    ViewGroup.LayoutParams params = imageView.getLayoutParams();
                    if (mPictureHeight != 0) {
                        params.height = mPictureHeight;
                    }
                    if (mPictureWidth != 0) {
                        params.width = mPictureWidth;
                    }
                    imageView.setLayoutParams(params);
                }
            }
        });

    }

    /**
     * Circular reveal enter animation
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void doCircularEnterAnimation() {
        getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        getViewTreeObserver().removeOnPreDrawListener(this);

                        final int revealRadius = (int) Math.hypot(
                                getWidth(), getHeight());
                        int startRadius = 0;
                        if (mView != null) {
                            startRadius = mView.getWidth() / 2;
                        } else if (mFocusCircleRadius > 0 || mFocusRectangleWidth > 0 || mFocusRectangleHeight > 0) {
                            mCenterX = mFocusPositionX;
                            mCenterY = mFocusPositionY;
                        }
                        Animator enterAnimator = ViewAnimationUtils.createCircularReveal(GuideCaseView.this,
                                mCenterX, mCenterY, startRadius, revealRadius);
                        enterAnimator.setDuration(mAnimationDuration);
                        enterAnimator.setInterpolator(AnimationUtils.loadInterpolator(mActivity,
                                android.R.interpolator.accelerate_cubic));
                        enterAnimator.start();
                        return false;
                    }
                });

    }

    /**
     * Circular reveal exit animation
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void doCircularExitAnimation() {
        final int revealRadius = (int) Math.hypot(getWidth(), getHeight());
        Animator exitAnimator = ViewAnimationUtils.createCircularReveal(this,
                mCenterX, mCenterY, revealRadius, 0f);
        exitAnimator.setDuration(mAnimationDuration);
        exitAnimator.setInterpolator(AnimationUtils.loadInterpolator(mActivity,
                android.R.interpolator.decelerate_cubic));
        exitAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                removeView();

            }
        });
        exitAnimator.start();


    }

    /**
     * Saves the GuideCaseView id to SharedPreferences that is shown once
     */
    private void writeShown() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(mId, true);
        editor.apply();
    }

    /**
     * Returns if GuideCaseView with given id is shown before
     *
     * @return true if show before
     */
    public boolean isShownBefore() {
        return mSharedPreferences.getBoolean(mId, false);
    }

    /**
     * Removes GuideCaseView view from activity root view
     */
    public void removeView() {
        mRoot.removeView(this);
        if (mDismissListener != null) {
            mDismissListener.onDismiss(mId);
        }
    }

    protected DismissListener getDismissListener() {
        return mDismissListener;
    }

    protected void setDismissListener(DismissListener dismissListener) {
        mDismissListener = dismissListener;
    }

    @Override
    public void onGlobalLayout() {
        if (Build.VERSION.SDK_INT < 16) {
            mView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
        } else {
            mView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
        focus();
    }

    public int getFocusCenterX() {
        return mCalculator.getCircleCenterX();
    }

    public int getFocusCenterY() {
        return mCalculator.getCircleCenterY();
    }

    public float getFocusRadius() {
        return FocusShape.CIRCLE.equals(mFocusShape) ? mCalculator.circleRadius(0, 1) : 0;
    }

    public int getFocusWidth() {
        return mCalculator.getFocusWidth();
    }

    public int getFocusHeight() {
        return mCalculator.getFocusHeight();
    }


    /**
     * Builder class for {@link GuideCaseView}
     */
    public static class Builder {

        private Activity mActivity;
        private View mView;
        private String mId;
        private String mTitle;
        private Spanned mSpannedTitle;
        private double mFocusCircleRadiusFactor = 1;
        private int mBackgroundColor;
        private int mFocusBorderColor;
        private int mTitleGravity = -1;
        private int mTitleSize = -1;
        private int mTitleSizeUnit = -1;
        private int mTitleStyle;
        private int mPictureResId;
        private int mPictureWidth;
        private int mPictureHeight;
        private int mCustomViewRes;
        private int mRoundRectRadius;
        private OnViewInflateListener mViewInflateListener;
        private Animation mEnterAnimation, mExitAnimation;
        private boolean mCloseOnTouch = true;
        private boolean mFitSystemWindows;
        private int mAdjustHeight = -1;
        private FocusShape mFocusShape = FocusShape.CIRCLE;
        private DismissListener mDismissListener = null;
        private int mFocusBorderSize;
        private int mFocusPositionX, mFocusPositionY, mFocusCircleRadius, mFocusRectangleWidth, mFocusRectangleHeight;
        private boolean mFocusAnimationEnabled = true;
        private int mFocusAnimationMaxValue = 20;
        private int mFocusAnimationStep = 1;

        /**
         * Constructor for Builder class
         *
         * @param activity Activity to show GuideCaseView in
         */
        public Builder(Activity activity) {
            mActivity = activity;
        }


        /**
         * 设置标题文字
         *
         * @param title title text
         * @return Builder
         */
        public Builder title(String title) {
            mTitle = title;
            mSpannedTitle = null;
            return this;
        }

        /**
         * @param title title text
         * @return Builder
         */
        public Builder title(Spanned title) {
            mSpannedTitle = title;
            mTitle = null;
            return this;
        }

        /**
         * 设置图片资源
         *
         * @param pictureResId 图片资源Id
         * @return Builder
         */
        public Builder picture(int pictureResId) {
            mPictureResId = pictureResId;
            return this;
        }

        /**
         * @param pictureResId 图片资源Id
         * @param width        图片资源Id
         * @param height       图片资源Id
         * @return Builder
         */
        public Builder picture(int pictureResId, int width, int height) {
            mPictureResId = pictureResId;
            mPictureWidth = width;
            mPictureHeight = height;
            return this;
        }

        /**
         * @param style        title text style
         * @param titleGravity title gravity
         * @return Builder
         */
        public Builder titleStyle(@StyleRes int style, int titleGravity) {
            mTitleGravity = titleGravity;
            mTitleStyle = style;
            return this;
        }

        /**
         * 设置聚焦边框的颜色
         *
         * @param focusBorderColor Border color for focus shape
         * @return Builder
         */
        public Builder focusBorderColor(int focusBorderColor) {
            mFocusBorderColor = focusBorderColor;
            return this;
        }

        /**
         * 设置聚焦边框的粗细
         *
         * @param focusBorderSize Border size for focus shape
         * @return Builder
         */
        public Builder focusBorderSize(int focusBorderSize) {
            mFocusBorderSize = focusBorderSize;
            return this;
        }

        /**
         * @param titleGravity title gravity
         * @return Builder
         */
        public Builder titleGravity(int titleGravity) {
            mTitleGravity = titleGravity;
            return this;
        }

        /**
         * the defined text size overrides any defined size in the default or provided style
         *
         * @param titleSize title size
         * @param unit      title text unit
         * @return Builder
         */
        public Builder titleSize(int titleSize, int unit) {
            mTitleSize = titleSize;
            mTitleSizeUnit = unit;
            return this;
        }

        /**
         * @param id unique identifier for GuideCaseView
         * @return Builder
         */
        public Builder showOnce(String id) {
            mId = id;
            return this;
        }

        /**
         * 设置聚焦的控件
         *
         * @param view view to focus
         * @return Builder
         */
        public Builder focusOn(View view) {
            mView = view;
            return this;
        }

        /**
         * 设置引导朦层的背景颜色
         *
         * @param backgroundColor background color of GuideCaseView
         * @return Builder
         */
        public Builder backgroundColor(int backgroundColor) {
            mBackgroundColor = backgroundColor;
            return this;
        }

        /**
         * @param focusCircleRadiusFactor focus circle radius factor (default value = 1)
         * @return Builder
         */
        public Builder focusCircleRadiusFactor(double focusCircleRadiusFactor) {
            mFocusCircleRadiusFactor = focusCircleRadiusFactor;
            return this;
        }

        /**
         * 设置自定义引导朦层布局
         *
         * @param layoutResource custom view layout resource
         * @param listener       inflate listener for custom view
         * @return Builder
         */
        public Builder customView(@LayoutRes int layoutResource, @Nullable OnViewInflateListener listener) {
            mCustomViewRes = layoutResource;
            mViewInflateListener = listener;
            return this;
        }

        /**
         * 设置进入动画
         *
         * @param enterAnimation enter animation for GuideCaseView
         * @return Builder
         */
        public Builder enterAnimation(Animation enterAnimation) {
            mEnterAnimation = enterAnimation;
            return this;
        }

        /**
         * 设置退出动画
         *
         * @param exitAnimation exit animation for GuideCaseView
         * @return Builder
         */
        public Builder exitAnimation(Animation exitAnimation) {
            mExitAnimation = exitAnimation;
            return this;
        }

        /**
         * @param closeOnTouch closes on touch if enabled
         * @return Builder
         */
        public Builder closeOnTouch(boolean closeOnTouch) {
            mCloseOnTouch = closeOnTouch;
            return this;
        }

        /**
         * This should be the same as root view's fitSystemWindows value
         *
         * @param fitSystemWindows fitSystemWindows value
         * @return Builder
         */
        public Builder fitSystemWindows(boolean fitSystemWindows) {
            mFitSystemWindows = fitSystemWindows;
            return this;
        }

        /**
         * 自动适应屏幕高度
         *
         * @return
         */
        public Builder fitWindowsAuto() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mFitSystemWindows = true;
            } else {
                adjustHeight(0);
            }
            return this;
        }

        /**
         * 调整的高度
         *
         * @param adjustHeight
         * @return
         */
        public Builder adjustHeight(int adjustHeight) {
            mAdjustHeight = adjustHeight;
            return this;
        }

        /**
         * 设置聚焦的形状，默认是圆形
         *
         * @param focusShape
         * @return
         */
        public Builder focusShape(FocusShape focusShape) {
            mFocusShape = focusShape;
            return this;
        }

        /**
         * Focus round rectangle at specific position
         * 自定义聚焦的矩形区域
         *
         * @param positionX      focus at specific position Y coordinate
         * @param positionY      focus at specific position circle radius
         * @param positionWidth  focus at specific position rectangle width
         * @param positionHeight focus at specific position rectangle height
         * @return Builder
         */

        public Builder focusRectAtPosition(int positionX, int positionY, int positionWidth, int positionHeight) {
            mFocusPositionX = positionX;
            mFocusPositionY = positionY;
            mFocusRectangleWidth = positionWidth;
            mFocusRectangleHeight = positionHeight;
            return this;
        }

        /**
         * Focus circle at specific position
         * 自定义聚焦的环形区域
         *
         * @param positionX focus at specific position Y coordinate
         * @param positionY focus at specific position circle radius
         * @param radius    focus at specific position circle radius
         * @return Builder
         */

        public Builder focusCircleAtPosition(int positionX, int positionY, int radius) {
            mFocusPositionX = positionX;
            mFocusPositionY = positionY;
            mFocusCircleRadius = radius;
            return this;
        }

        /**
         * 设置引导朦层消失的监听
         *
         * @param dismissListener the dismiss listener
         * @return Builder
         */
        public Builder dismissListener(DismissListener dismissListener) {
            mDismissListener = dismissListener;
            return this;
        }

        public Builder roundRectRadius(int roundRectRadius) {
            mRoundRectRadius = roundRectRadius;
            return this;
        }

        /**
         * disable Focus Animation
         *
         * @return Builder
         */
        public Builder disableFocusAnimation() {
            mFocusAnimationEnabled = false;
            return this;
        }

        public Builder focusAnimationMaxValue(int focusAnimationMaxValue) {
            mFocusAnimationMaxValue = focusAnimationMaxValue;
            return this;
        }

        public Builder focusAnimationStep(int focusAnimationStep) {
            mFocusAnimationStep = focusAnimationStep;
            return this;
        }

        /**
         * builds the builder
         *
         * @return {@link GuideCaseView} with given parameters
         */
        public GuideCaseView build() {
            return new GuideCaseView(this);
        }
    }
}
