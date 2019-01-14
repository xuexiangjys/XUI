package com.xuexiang.xui.widget.button.switchbutton;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.CompoundButton;

import com.xuexiang.xui.R;

/**
 * switch切换按钮
 *
 * @author xuexiang
 * @since 2019/1/14 上午11:23
 */
public class SwitchButton extends CompoundButton {
    public static final float DEFAULT_THUMB_RANGE_RATIO = 1.8f;
    public static final int DEFAULT_THUMB_SIZE_DP = 20;
    public static final int DEFAULT_THUMB_MARGIN_DP = 2;
    public static final int DEFAULT_ANIMATION_DURATION = 250;
    public static final int DEFAULT_TINT_COLOR = 0x327FC2;

    private static int[] CHECKED_PRESSED_STATE = new int[]{android.R.attr.state_checked, android.R.attr.state_enabled, android.R.attr.state_pressed};
    private static int[] UNCHECKED_PRESSED_STATE = new int[]{-android.R.attr.state_checked, android.R.attr.state_enabled, android.R.attr.state_pressed};

    private Drawable mThumbDrawable, mBackDrawable;
    private ColorStateList mBackColor, mThumbColor;
    private float mThumbRadius, mBackRadius;
    private RectF mThumbMargin;
    private float mThumbRangeRatio;
    private long mAnimationDuration;
    // fade back drawable or color when dragging or animating
    private boolean mFadeBack;
    private int mTintColor;
    private int mThumbWidth;
    private int mThumbHeight;
    private int mBackWidth;
    private int mBackHeight;

    private int mCurrThumbColor, mCurrBackColor, mNextBackColor, mOnTextColor, mOffTextColor;
    private Drawable mCurrentBackDrawable, mNextBackDrawable;
    private RectF mThumbRectF, mBackRectF, mSafeRectF, mTextOnRectF, mTextOffRectF;
    private Paint mPaint;
    // whether using Drawable for thumb or back
    private boolean mIsThumbUseDrawable, mIsBackUseDrawable;
    private boolean mDrawDebugRect = false;
    private ObjectAnimator mProgressAnimator;
    // animation control
    private float mProgress;
    // temp position of thumb when dragging or animating
    private RectF mPresentThumbRectF;
    private float mStartX, mStartY, mLastX;
    private int mTouchSlop;
    private int mClickTimeout;
    private Paint mRectPaint;
    private CharSequence mTextOn;
    private CharSequence mTextOff;
    private TextPaint mTextPaint;
    private Layout mOnLayout;
    private Layout mOffLayout;
    private float mTextWidth;
    private float mTextHeight;
    private int mTextThumbInset;
    private int mTextExtra;
    private int mTextAdjust;
    // FIX #78,#85 : When restoring saved states, setChecked() called by super. So disable
    // animation and event listening when restoring.
    private boolean mRestoring = false;
    private boolean mReady = false;
    private boolean mCatch = false;

    private CompoundButton.OnCheckedChangeListener mChildOnCheckedChangeListener;

    public SwitchButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public SwitchButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SwitchButton(Context context) {
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs) {
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        mClickTimeout = ViewConfiguration.getPressedStateDuration() + ViewConfiguration.getTapTimeout();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRectPaint.setStyle(Paint.Style.STROKE);
        mRectPaint.setStrokeWidth(getResources().getDisplayMetrics().density);

        mTextPaint = getPaint();

        mThumbRectF = new RectF();
        mBackRectF = new RectF();
        mSafeRectF = new RectF();
        mThumbMargin = new RectF();
        mTextOnRectF = new RectF();
        mTextOffRectF = new RectF();

        mProgressAnimator = ObjectAnimator.ofFloat(this, "progress", 0, 0).setDuration(DEFAULT_ANIMATION_DURATION);
        mProgressAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        mPresentThumbRectF = new RectF();

        Resources res = getResources();
        float density = res.getDisplayMetrics().density;

        Drawable thumbDrawable = null;
        ColorStateList thumbColor = null;
        float margin = density * DEFAULT_THUMB_MARGIN_DP;
        float marginLeft = 0;
        float marginRight = 0;
        float marginTop = 0;
        float marginBottom = 0;
        float thumbWidth = 0;
        float thumbHeight = 0;
        float thumbRadius = -1;
        float backRadius = -1;
        Drawable backDrawable = null;
        ColorStateList backColor = null;
        float thumbRangeRatio = DEFAULT_THUMB_RANGE_RATIO;
        int animationDuration = DEFAULT_ANIMATION_DURATION;
        boolean fadeBack = true;
        int tintColor = 0;
        String textOn = null;
        String textOff = null;
        int textThumbInset = 0;
        int textExtra = 0;
        int textAdjust = 0;

        TypedArray ta = attrs == null ? null : getContext().obtainStyledAttributes(attrs, R.styleable.SwitchButton);
        if (ta != null) {
            thumbDrawable = ta.getDrawable(R.styleable.SwitchButton_swb_thumbDrawable);
            thumbColor = ta.getColorStateList(R.styleable.SwitchButton_swb_thumbColor);
            margin = ta.getDimension(R.styleable.SwitchButton_swb_thumbMargin, margin);
            marginLeft = ta.getDimension(R.styleable.SwitchButton_swb_thumbMarginLeft, margin);
            marginRight = ta.getDimension(R.styleable.SwitchButton_swb_thumbMarginRight, margin);
            marginTop = ta.getDimension(R.styleable.SwitchButton_swb_thumbMarginTop, margin);
            marginBottom = ta.getDimension(R.styleable.SwitchButton_swb_thumbMarginBottom, margin);
            thumbWidth = ta.getDimension(R.styleable.SwitchButton_swb_thumbWidth, thumbWidth);
            thumbHeight = ta.getDimension(R.styleable.SwitchButton_swb_thumbHeight, thumbHeight);
            thumbRadius = ta.getDimension(R.styleable.SwitchButton_swb_thumbRadius, thumbRadius);
            backRadius = ta.getDimension(R.styleable.SwitchButton_swb_backRadius, backRadius);
            backDrawable = ta.getDrawable(R.styleable.SwitchButton_swb_backDrawable);
            backColor = ta.getColorStateList(R.styleable.SwitchButton_swb_backColor);
            thumbRangeRatio = ta.getFloat(R.styleable.SwitchButton_swb_thumbRangeRatio, thumbRangeRatio);
            animationDuration = ta.getInteger(R.styleable.SwitchButton_swb_animationDuration, animationDuration);
            fadeBack = ta.getBoolean(R.styleable.SwitchButton_swb_fadeBack, true);
            tintColor = ta.getColor(R.styleable.SwitchButton_swb_tintColor, tintColor);
            textOn = ta.getString(R.styleable.SwitchButton_swb_textOn);
            textOff = ta.getString(R.styleable.SwitchButton_swb_textOff);
            textThumbInset = ta.getDimensionPixelSize(R.styleable.SwitchButton_swb_textThumbInset, 0);
            textExtra = ta.getDimensionPixelSize(R.styleable.SwitchButton_swb_textExtra, 0);
            textAdjust = ta.getDimensionPixelSize(R.styleable.SwitchButton_swb_textAdjust, 0);
            ta.recycle();
        }

        // click
        ta = attrs == null ? null : getContext().obtainStyledAttributes(attrs, new int[]{android.R.attr.focusable, android.R.attr.clickable});
        if (ta != null) {
            boolean focusable = ta.getBoolean(0, true);
            //noinspection ResourceType
            @SuppressLint("ResourceType")
            boolean clickable = ta.getBoolean(1, focusable);
            setFocusable(focusable);
            setClickable(clickable);
            ta.recycle();
        } else {
            setFocusable(true);
            setClickable(true);
        }

        // text
        mTextOn = textOn;
        mTextOff = textOff;
        mTextThumbInset = textThumbInset;
        mTextExtra = textExtra;
        mTextAdjust = textAdjust;

        // thumb drawable and color
        mThumbDrawable = thumbDrawable;
        mThumbColor = thumbColor;
        mIsThumbUseDrawable = mThumbDrawable != null;
        mTintColor = tintColor;
        if (mTintColor == 0) {
            TypedValue typedValue = new TypedValue();
            boolean found = getContext().getTheme().resolveAttribute(R.attr.colorAccent, typedValue, true);
            if (found) {
                mTintColor = typedValue.data;
            } else {
                mTintColor = DEFAULT_TINT_COLOR;
            }
        }
        if (!mIsThumbUseDrawable && mThumbColor == null) {
            mThumbColor = ColorUtils.generateThumbColorWithTintColor(mTintColor);
            mCurrThumbColor = mThumbColor.getDefaultColor();
        }

        // thumbSize
        mThumbWidth = ceil(thumbWidth);
        mThumbHeight = ceil(thumbHeight);

        // back drawable and color
        mBackDrawable = backDrawable;
        mBackColor = backColor;
        mIsBackUseDrawable = mBackDrawable != null;
        if (!mIsBackUseDrawable && mBackColor == null) {
            mBackColor = ColorUtils.generateBackColorWithTintColor(mTintColor);
            mCurrBackColor = mBackColor.getDefaultColor();
            mNextBackColor = mBackColor.getColorForState(CHECKED_PRESSED_STATE, mCurrBackColor);
        }

        // margin
        mThumbMargin.set(marginLeft, marginTop, marginRight, marginBottom);

        // size & measure params must larger than 1
        mThumbRangeRatio = mThumbMargin.width() >= 0 ? Math.max(thumbRangeRatio, 1) : thumbRangeRatio;

        mThumbRadius = thumbRadius;
        mBackRadius = backRadius;
        mAnimationDuration = animationDuration;
        mFadeBack = fadeBack;

        mProgressAnimator.setDuration(mAnimationDuration);

        // sync checked status
        if (isChecked()) {
            setProgress(1);
        }
    }


    private Layout makeLayout(CharSequence text) {
        return new StaticLayout(text, mTextPaint, (int) Math.ceil(Layout.getDesiredWidth(text, mTextPaint)), Layout.Alignment.ALIGN_CENTER, 1.f, 0, false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /*
         * ensure textLayout
         */
        if (mOnLayout == null && !TextUtils.isEmpty(mTextOn)) {
            mOnLayout = makeLayout(mTextOn);
        }
        if (mOffLayout == null && !TextUtils.isEmpty(mTextOff)) {
            mOffLayout = makeLayout(mTextOff);
        }

        float onWidth = mOnLayout != null ? mOnLayout.getWidth() : 0;
        float offWidth = mOffLayout != null ? mOffLayout.getWidth() : 0;
        if (onWidth != 0 || offWidth != 0) {
            mTextWidth = Math.max(onWidth, offWidth);
        } else {
            mTextWidth = 0;
        }

        float onHeight = mOnLayout != null ? mOnLayout.getHeight() : 0;
        float offHeight = mOffLayout != null ? mOffLayout.getHeight() : 0;
        if (onHeight != 0 || offHeight != 0) {
            mTextHeight = Math.max(onHeight, offHeight);
        } else {
            mTextHeight = 0;
        }

        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    /**
     * SwitchButton use this formula to determine the final size of thumb, background and itself.
     * <p>
     * textWidth = max(onWidth, offWidth)
     * thumbRange = thumbWidth * rangeRatio
     * textExtraSpace = textWidth + textExtra - (moveRange - thumbWidth + max(thumbMargin.left, thumbMargin.right) + textThumbInset)
     * backWidth = thumbRange + thumbMargin.left + thumbMargin.right + max(textExtraSpace, 0)
     * contentSize = thumbRange + max(thumbMargin.left, 0) + max(thumbMargin.right, 0) + max(textExtraSpace, 0)
     *
     * @param widthMeasureSpec widthMeasureSpec
     * @return measuredWidth
     */
    private int measureWidth(int widthMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int measuredWidth = widthSize;

        if (mThumbWidth == 0 && mIsThumbUseDrawable) {
            mThumbWidth = mThumbDrawable.getIntrinsicWidth();
        }

        int moveRange;
        int textWidth = ceil(mTextWidth);
        // how much the background should extend to fit text.
        int textExtraSpace;
        int contentSize;

        if (mThumbRangeRatio == 0) {
            mThumbRangeRatio = DEFAULT_THUMB_RANGE_RATIO;
        }

        if (widthMode == MeasureSpec.EXACTLY) {
            contentSize = widthSize - getPaddingLeft() - getPaddingRight();

            if (mThumbWidth != 0) {
                moveRange = ceil(mThumbWidth * mThumbRangeRatio);
                textExtraSpace = textWidth + mTextExtra - (moveRange - mThumbWidth + ceil(Math.max(mThumbMargin.left, mThumbMargin.right)));
                mBackWidth = ceil(moveRange + mThumbMargin.left + mThumbMargin.right + Math.max(textExtraSpace, 0));
                if (mBackWidth < 0) {
                    mThumbWidth = 0;
                }
                if (moveRange + Math.max(mThumbMargin.left, 0) + Math.max(mThumbMargin.right, 0) + Math.max(textExtraSpace, 0) > contentSize) {
                    mThumbWidth = 0;
                }
            }

            if (mThumbWidth == 0) {
                contentSize = widthSize - getPaddingLeft() - getPaddingRight();
                moveRange = ceil(contentSize - Math.max(mThumbMargin.left, 0) - Math.max(mThumbMargin.right, 0));
                if (moveRange < 0) {
                    mThumbWidth = 0;
                    mBackWidth = 0;
                    return measuredWidth;
                }
                mThumbWidth = ceil(moveRange / mThumbRangeRatio);
                mBackWidth = ceil(moveRange + mThumbMargin.left + mThumbMargin.right);
                if (mBackWidth < 0) {
                    mThumbWidth = 0;
                    mBackWidth = 0;
                    return measuredWidth;
                }
                textExtraSpace = textWidth + mTextExtra - (moveRange - mThumbWidth + ceil(Math.max(mThumbMargin.left, mThumbMargin.right)));
                if (textExtraSpace > 0) {
                    // since backWidth is determined by view width, so we can only reduce thumbSize.
                    mThumbWidth = mThumbWidth - textExtraSpace;
                }
                if (mThumbWidth < 0) {
                    mThumbWidth = 0;
                    mBackWidth = 0;
                    return measuredWidth;
                }
            }
        } else {
            /*
            If parent view want SwitchButton to determine it's size itself, we calculate the minimal
            size of it's content. Further more, we ignore the limitation of widthSize since we want
            to display SwitchButton in its actual size rather than compress the shape.
             */
            if (mThumbWidth == 0) {
                /*
                If thumbWidth is not set, use the default one.
                 */
                mThumbWidth = ceil(getResources().getDisplayMetrics().density * DEFAULT_THUMB_SIZE_DP);
            }
            if (mThumbRangeRatio == 0) {
                mThumbRangeRatio = DEFAULT_THUMB_RANGE_RATIO;
            }

            moveRange = ceil(mThumbWidth * mThumbRangeRatio);
            textExtraSpace = ceil(textWidth + mTextExtra - (moveRange - mThumbWidth + Math.max(mThumbMargin.left, mThumbMargin.right) + mTextThumbInset));
            mBackWidth = ceil(moveRange + mThumbMargin.left + mThumbMargin.right + Math.max(0, textExtraSpace));
            if (mBackWidth < 0) {
                mThumbWidth = 0;
                mBackWidth = 0;
                return measuredWidth;
            }
            contentSize = ceil(moveRange + Math.max(0, mThumbMargin.left) + Math.max(0, mThumbMargin.right) + Math.max(0, textExtraSpace));

            measuredWidth = Math.max(contentSize, contentSize + getPaddingLeft() + getPaddingRight());
        }
        return measuredWidth;
    }

    private int measureHeight(int heightMeasureSpec) {
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int measuredHeight = heightSize;

        if (mThumbHeight == 0 && mIsThumbUseDrawable) {
            mThumbHeight = mThumbDrawable.getIntrinsicHeight();
        }
        int contentSize;
        int textExtraSpace;
        if (heightMode == MeasureSpec.EXACTLY) {
            if (mThumbHeight != 0) {
                /*
                If thumbHeight has been set, we calculate backHeight and check if there is enough room.
                 */
                mBackHeight = ceil(mThumbHeight + mThumbMargin.top + mThumbMargin.bottom);
                mBackHeight = ceil(Math.max(mBackHeight, mTextHeight));
                if (mBackHeight + getPaddingTop() + getPaddingBottom() - Math.min(0, mThumbMargin.top) - Math.min(0, mThumbMargin.bottom) > heightSize) {
                    // No enough room, we set thumbHeight to zero to calculate these value again.
                    mThumbHeight = 0;
                }
            }

            if (mThumbHeight == 0) {
                mBackHeight = ceil(heightSize - getPaddingTop() - getPaddingBottom() + Math.min(0, mThumbMargin.top) + Math.min(0, mThumbMargin.bottom));
                if (mBackHeight < 0) {
                    mBackHeight = 0;
                    mThumbHeight = 0;
                    return measuredHeight;
                }
                mThumbHeight = ceil(mBackHeight - mThumbMargin.top - mThumbMargin.bottom);
            }
            if (mThumbHeight < 0) {
                mBackHeight = 0;
                mThumbHeight = 0;
                return measuredHeight;
            }
        } else {
            if (mThumbHeight == 0) {
                mThumbHeight = ceil(getResources().getDisplayMetrics().density * DEFAULT_THUMB_SIZE_DP);
            }
            mBackHeight = ceil(mThumbHeight + mThumbMargin.top + mThumbMargin.bottom);
            if (mBackHeight < 0) {
                mBackHeight = 0;
                mThumbHeight = 0;
                return measuredHeight;
            }
            textExtraSpace = ceil(mTextHeight - mBackHeight);
            if (textExtraSpace > 0) {
                mBackHeight += textExtraSpace;
                mThumbHeight += textExtraSpace;
            }
            contentSize = Math.max(mThumbHeight, mBackHeight);

            measuredHeight = Math.max(contentSize, contentSize + getPaddingTop() + getPaddingBottom());
            measuredHeight = Math.max(measuredHeight, getSuggestedMinimumHeight());
        }

        return measuredHeight;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw || h != oldh) {
            setup();
        }
    }

    private int ceil(double dimen) {
        return (int) Math.ceil(dimen);
    }

    /**
     * set up the rect of back and thumb
     */
    private void setup() {
        if (mThumbWidth == 0 || mThumbHeight == 0 || mBackWidth == 0 || mBackHeight == 0) {
            return;
        }

        if (mThumbRadius == -1) {
            mThumbRadius = Math.min(mThumbWidth, mThumbHeight) / 2;
        }
        if (mBackRadius == -1) {
            mBackRadius = Math.min(mBackWidth, mBackHeight) / 2;
        }

        int contentWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int contentHeight = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();

        // max range of drawing content, when thumbMargin is negative, drawing range is larger than backWidth
        int drawingWidth = ceil(mBackWidth - Math.min(0, mThumbMargin.left) - Math.min(0, mThumbMargin.right));
        int drawingHeight = ceil(mBackHeight - Math.min(0, mThumbMargin.top) - Math.min(0, mThumbMargin.bottom));

        float thumbTop;
        if (contentHeight <= drawingHeight) {
            thumbTop = getPaddingTop() + Math.max(0, mThumbMargin.top);
        } else {
            // center vertical in content area
            thumbTop = getPaddingTop() + Math.max(0, mThumbMargin.top) + (contentHeight - drawingHeight + 1) / 2;
        }

        float thumbLeft;
        if (contentWidth <= mBackWidth) {
            thumbLeft = getPaddingLeft() + Math.max(0, mThumbMargin.left);
        } else {
            thumbLeft = getPaddingLeft() + Math.max(0, mThumbMargin.left) + (contentWidth - drawingWidth + 1) / 2;
        }

        mThumbRectF.set(thumbLeft, thumbTop, thumbLeft + mThumbWidth, thumbTop + mThumbHeight);

        float backLeft = mThumbRectF.left - mThumbMargin.left;
        mBackRectF.set(backLeft,
                mThumbRectF.top - mThumbMargin.top,
                backLeft + mBackWidth,
                mThumbRectF.top - mThumbMargin.top + mBackHeight);

        mSafeRectF.set(mThumbRectF.left, 0, mBackRectF.right - mThumbMargin.right - mThumbRectF.width(), 0);

        float minBackRadius = Math.min(mBackRectF.width(), mBackRectF.height()) / 2.f;
        mBackRadius = Math.min(minBackRadius, mBackRadius);

        if (mBackDrawable != null) {
            mBackDrawable.setBounds((int) mBackRectF.left, (int) mBackRectF.top, ceil(mBackRectF.right), ceil(mBackRectF.bottom));
        }

        if (mOnLayout != null) {
            float onLeft = mBackRectF.left + (mBackRectF.width() + mTextThumbInset - mThumbWidth - mThumbMargin.right - mOnLayout.getWidth()) / 2f - mTextAdjust;
            float onTop = mBackRectF.top + (mBackRectF.height() - mOnLayout.getHeight()) / 2;
            mTextOnRectF.set(onLeft, onTop, onLeft + mOnLayout.getWidth(), onTop + mOnLayout.getHeight());
        }

        if (mOffLayout != null) {
            float offLeft = mBackRectF.right - (mBackRectF.width() + mTextThumbInset - mThumbWidth - mThumbMargin.left - mOffLayout.getWidth()) / 2f - mOffLayout.getWidth() + mTextAdjust;
            float offTop = mBackRectF.top + (mBackRectF.height() - mOffLayout.getHeight()) / 2;
            mTextOffRectF.set(offLeft, offTop, offLeft + mOffLayout.getWidth(), offTop + mOffLayout.getHeight());
        }

        mReady = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!mReady) {
            setup();
        }
        if (!mReady) {
            return;
        }

        // fade back
        if (mIsBackUseDrawable) {
            if (mFadeBack && mCurrentBackDrawable != null && mNextBackDrawable != null) {
                // fix #75, 70%A + 30%B != 30%B + 70%A, order matters when mix two layer of different alpha.
                // So make sure the order of on/off layers never change during slide from one endpoint to another.
                Drawable below = isChecked() ? mCurrentBackDrawable : mNextBackDrawable;
                Drawable above = isChecked() ? mNextBackDrawable : mCurrentBackDrawable;

                int alpha = (int) (255 * getProgress());
                below.setAlpha(alpha);
                below.draw(canvas);
                alpha = 255 - alpha;
                above.setAlpha(alpha);
                above.draw(canvas);
            } else {
                mBackDrawable.setAlpha(255);
                mBackDrawable.draw(canvas);
            }
        } else {
            if (mFadeBack) {
                int alpha;
                int colorAlpha;

                // fix #75
                int belowColor = isChecked() ? mCurrBackColor : mNextBackColor;
                int aboveColor = isChecked() ? mNextBackColor : mCurrBackColor;

                // curr back
                alpha = (int) (255 * getProgress());
                colorAlpha = Color.alpha(belowColor);
                colorAlpha = colorAlpha * alpha / 255;
                mPaint.setARGB(colorAlpha, Color.red(belowColor), Color.green(belowColor), Color.blue(belowColor));
                canvas.drawRoundRect(mBackRectF, mBackRadius, mBackRadius, mPaint);

                // next back
                alpha = 255 - alpha;
                colorAlpha = Color.alpha(aboveColor);
                colorAlpha = colorAlpha * alpha / 255;
                mPaint.setARGB(colorAlpha, Color.red(aboveColor), Color.green(aboveColor), Color.blue(aboveColor));
                canvas.drawRoundRect(mBackRectF, mBackRadius, mBackRadius, mPaint);

                mPaint.setAlpha(255);
            } else {
                mPaint.setColor(mCurrBackColor);
                canvas.drawRoundRect(mBackRectF, mBackRadius, mBackRadius, mPaint);
            }
        }

        // text
        Layout switchText = getProgress() > 0.5 ? mOnLayout : mOffLayout;
        RectF textRectF = getProgress() > 0.5 ? mTextOnRectF : mTextOffRectF;
        if (switchText != null && textRectF != null) {
            int alpha = (int) (255 * (getProgress() >= 0.75 ? getProgress() * 4 - 3 : (getProgress() < 0.25 ? 1 - getProgress() * 4 : 0)));
            int textColor = getProgress() > 0.5 ? mOnTextColor : mOffTextColor;
            int colorAlpha = Color.alpha(textColor);
            colorAlpha = colorAlpha * alpha / 255;
            switchText.getPaint().setARGB(colorAlpha, Color.red(textColor), Color.green(textColor), Color.blue(textColor));
            canvas.save();
            canvas.translate(textRectF.left, textRectF.top);
            switchText.draw(canvas);
            canvas.restore();
        }

        // thumb
        mPresentThumbRectF.set(mThumbRectF);
        mPresentThumbRectF.offset(mProgress * mSafeRectF.width(), 0);
        if (mIsThumbUseDrawable) {
            mThumbDrawable.setBounds((int) mPresentThumbRectF.left, (int) mPresentThumbRectF.top, ceil(mPresentThumbRectF.right), ceil(mPresentThumbRectF.bottom));
            mThumbDrawable.draw(canvas);
        } else {
            mPaint.setColor(mCurrThumbColor);
            canvas.drawRoundRect(mPresentThumbRectF, mThumbRadius, mThumbRadius, mPaint);
        }

        if (mDrawDebugRect) {
            mRectPaint.setColor(Color.parseColor("#AA0000"));
            canvas.drawRect(mBackRectF, mRectPaint);
            mRectPaint.setColor(Color.parseColor("#0000FF"));
            canvas.drawRect(mPresentThumbRectF, mRectPaint);
            mRectPaint.setColor(Color.parseColor("#000000"));
            canvas.drawLine(mSafeRectF.left, mThumbRectF.top, mSafeRectF.right, mThumbRectF.top, mRectPaint);
            mRectPaint.setColor(Color.parseColor("#00CC00"));
            canvas.drawRect(getProgress() > 0.5 ? mTextOnRectF : mTextOffRectF, mRectPaint);
        }
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();

        if (!mIsThumbUseDrawable && mThumbColor != null) {
            mCurrThumbColor = mThumbColor.getColorForState(getDrawableState(), mCurrThumbColor);
        } else {
            setDrawableState(mThumbDrawable);
        }

        int[] nextState = isChecked() ? UNCHECKED_PRESSED_STATE : CHECKED_PRESSED_STATE;
        ColorStateList textColors = getTextColors();
        if (textColors != null) {
            int defaultTextColor = textColors.getDefaultColor();
            mOnTextColor = textColors.getColorForState(CHECKED_PRESSED_STATE, defaultTextColor);
            mOffTextColor = textColors.getColorForState(UNCHECKED_PRESSED_STATE, defaultTextColor);
        }
        if (!mIsBackUseDrawable && mBackColor != null) {
            mCurrBackColor = mBackColor.getColorForState(getDrawableState(), mCurrBackColor);
            mNextBackColor = mBackColor.getColorForState(nextState, mCurrBackColor);
        } else {
            if (mBackDrawable instanceof StateListDrawable && mFadeBack) {
                mBackDrawable.setState(nextState);
                mNextBackDrawable = mBackDrawable.getCurrent().mutate();
            } else {
                mNextBackDrawable = null;
            }
            setDrawableState(mBackDrawable);
            if (mBackDrawable != null) {
                mCurrentBackDrawable = mBackDrawable.getCurrent().mutate();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (!isEnabled() || !isClickable() || !isFocusable() || !mReady) {
            return false;
        }

        int action = event.getAction();

        float deltaX = event.getX() - mStartX;
        float deltaY = event.getY() - mStartY;

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mStartX = event.getX();
                mStartY = event.getY();
                mLastX = mStartX;
                setPressed(true);
                break;

            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                setProgress(getProgress() + (x - mLastX) / mSafeRectF.width());
                if (!mCatch && (Math.abs(deltaX) > mTouchSlop / 2 || Math.abs(deltaY) > mTouchSlop / 2)) {
                    if (deltaY == 0 || Math.abs(deltaX) > Math.abs(deltaY)) {
                        catchView();
                    } else if (Math.abs(deltaY) > Math.abs(deltaX)) {
                        return false;
                    }
                }
                mLastX = x;
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mCatch = false;
                setPressed(false);
                float time = event.getEventTime() - event.getDownTime();
                if (Math.abs(deltaX) < mTouchSlop && Math.abs(deltaY) < mTouchSlop && time < mClickTimeout) {
                    performClick();
                } else {
                    boolean nextStatus = getStatusBasedOnPos();
                    if (nextStatus != isChecked()) {
                        playSoundEffect(SoundEffectConstants.CLICK);
                        setChecked(nextStatus);
                    } else {
                        animateToState(nextStatus);
                    }
                }
                break;

            default:
                break;
        }
        return true;
    }


    /**
     * return the status based on position of thumb
     *
     * @return whether checked or not
     */
    private boolean getStatusBasedOnPos() {
        return getProgress() > 0.5f;
    }

    private float getProgress() {
        return mProgress;
    }

    private void setProgress(final float progress) {
        float tp = progress;
        if (tp > 1) {
            tp = 1;
        } else if (tp < 0) {
            tp = 0;
        }
        this.mProgress = tp;
        invalidate();
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    /**
     * processing animation
     *
     * @param checked checked or unChecked
     */
    protected void animateToState(boolean checked) {
        if (mProgressAnimator == null) {
            return;
        }
        if (mProgressAnimator.isRunning()) {
            mProgressAnimator.cancel();
        }
        mProgressAnimator.setDuration(mAnimationDuration);
        if (checked) {
            mProgressAnimator.setFloatValues(mProgress, 1f);
        } else {
            mProgressAnimator.setFloatValues(mProgress, 0);
        }
        mProgressAnimator.start();
    }

    private void catchView() {
        ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(true);
        }
        mCatch = true;
    }

    @Override
    public void setChecked(final boolean checked) {
        // animate before super.setChecked() become user may call setChecked again in OnCheckedChangedListener
        if (isChecked() != checked) {
            animateToState(checked);
        }
        if (mRestoring) {
            setCheckedImmediatelyNoEvent(checked);
        } else {
            super.setChecked(checked);
        }
    }

    public void setCheckedNoEvent(final boolean checked) {
        if (mChildOnCheckedChangeListener == null) {
            setChecked(checked);
        } else {
            super.setOnCheckedChangeListener(null);
            setChecked(checked);
            super.setOnCheckedChangeListener(mChildOnCheckedChangeListener);
        }
    }

    public void setCheckedImmediatelyNoEvent(boolean checked) {
        if (mChildOnCheckedChangeListener == null) {
            setCheckedImmediately(checked);
        } else {
            super.setOnCheckedChangeListener(null);
            setCheckedImmediately(checked);
            super.setOnCheckedChangeListener(mChildOnCheckedChangeListener);
        }
    }

    public void toggleNoEvent() {
        if (mChildOnCheckedChangeListener == null) {
            toggle();
        } else {
            super.setOnCheckedChangeListener(null);
            toggle();
            super.setOnCheckedChangeListener(mChildOnCheckedChangeListener);
        }
    }

    public void toggleImmediatelyNoEvent() {
        if (mChildOnCheckedChangeListener == null) {
            toggleImmediately();
        } else {
            super.setOnCheckedChangeListener(null);
            toggleImmediately();
            super.setOnCheckedChangeListener(mChildOnCheckedChangeListener);
        }
    }

    @Override
    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        super.setOnCheckedChangeListener(onCheckedChangeListener);
        mChildOnCheckedChangeListener = onCheckedChangeListener;
    }

    public void setCheckedImmediately(boolean checked) {
        super.setChecked(checked);
        if (mProgressAnimator != null && mProgressAnimator.isRunning()) {
            mProgressAnimator.cancel();
        }
        setProgress(checked ? 1 : 0);
        invalidate();
    }

    public void toggleImmediately() {
        setCheckedImmediately(!isChecked());
    }

    private void setDrawableState(Drawable drawable) {
        if (drawable != null) {
            int[] myDrawableState = getDrawableState();
            drawable.setState(myDrawableState);
            invalidate();
        }
    }

    public boolean isDrawDebugRect() {
        return mDrawDebugRect;
    }

    public void setDrawDebugRect(boolean drawDebugRect) {
        mDrawDebugRect = drawDebugRect;
        invalidate();
    }

    public long getAnimationDuration() {
        return mAnimationDuration;
    }

    public void setAnimationDuration(long animationDuration) {
        mAnimationDuration = animationDuration;
    }

    public Drawable getThumbDrawable() {
        return mThumbDrawable;
    }

    public void setThumbDrawable(Drawable thumbDrawable) {
        mThumbDrawable = thumbDrawable;
        mIsThumbUseDrawable = mThumbDrawable != null;
        refreshDrawableState();
        mReady = false;
        requestLayout();
        invalidate();
    }

    public void setThumbDrawableRes(int thumbDrawableRes) {
        setThumbDrawable(ContextCompat.getDrawable(getContext(), thumbDrawableRes));
    }

    public Drawable getBackDrawable() {
        return mBackDrawable;
    }

    public void setBackDrawable(Drawable backDrawable) {
        mBackDrawable = backDrawable;
        mIsBackUseDrawable = mBackDrawable != null;
        refreshDrawableState();
        mReady = false;
        requestLayout();
        invalidate();
    }

    public void setBackDrawableRes(int backDrawableRes) {
        setBackDrawable(ContextCompat.getDrawable(getContext(), backDrawableRes));
    }

    public ColorStateList getBackColor() {
        return mBackColor;
    }

    public void setBackColor(ColorStateList backColor) {
        mBackColor = backColor;
        if (mBackColor != null) {
            setBackDrawable(null);
        }
        invalidate();
    }

    public void setBackColorRes(int backColorRes) {
        setBackColor(ContextCompat.getColorStateList(getContext(), backColorRes));
    }

    public ColorStateList getThumbColor() {
        return mThumbColor;
    }

    public void setThumbColor(ColorStateList thumbColor) {
        mThumbColor = thumbColor;
        if (mThumbColor != null) {
            setThumbDrawable(null);
        }
        invalidate();
    }

    public void setThumbColorRes(int thumbColorRes) {
        setThumbColor(ContextCompat.getColorStateList(getContext(), thumbColorRes));
    }

    public float getThumbRangeRatio() {
        return mThumbRangeRatio;
    }

    public void setThumbRangeRatio(float thumbRangeRatio) {
        mThumbRangeRatio = thumbRangeRatio;
        // We need to mark "ready" to false since requestLayout may not cause size changed.
        mReady = false;
        requestLayout();
    }

    public RectF getThumbMargin() {
        return mThumbMargin;
    }

    public void setThumbMargin(RectF thumbMargin) {
        if (thumbMargin == null) {
            setThumbMargin(0, 0, 0, 0);
        } else {
            setThumbMargin(thumbMargin.left, thumbMargin.top, thumbMargin.right, thumbMargin.bottom);
        }
    }

    public void setThumbMargin(float left, float top, float right, float bottom) {
        mThumbMargin.set(left, top, right, bottom);
        mReady = false;
        requestLayout();
    }

    public void setThumbSize(int width, int height) {
        mThumbWidth = width;
        mThumbHeight = height;
        mReady = false;
        requestLayout();
    }

    public float getThumbWidth() {
        return mThumbWidth;
    }

    public float getThumbHeight() {
        return mThumbHeight;
    }

    public float getThumbRadius() {
        return mThumbRadius;
    }

    public void setThumbRadius(float thumbRadius) {
        mThumbRadius = thumbRadius;
        if (!mIsThumbUseDrawable) {
            invalidate();
        }
    }

    public PointF getBackSizeF() {
        return new PointF(mBackRectF.width(), mBackRectF.height());
    }

    public float getBackRadius() {
        return mBackRadius;
    }

    public void setBackRadius(float backRadius) {
        mBackRadius = backRadius;
        if (!mIsBackUseDrawable) {
            invalidate();
        }
    }

    public boolean isFadeBack() {
        return mFadeBack;
    }

    public void setFadeBack(boolean fadeBack) {
        mFadeBack = fadeBack;
    }

    public int getTintColor() {
        return mTintColor;
    }

    public void setTintColor(@SuppressWarnings("SameParameterValue") int tintColor) {
        mTintColor = tintColor;
        mThumbColor = ColorUtils.generateThumbColorWithTintColor(mTintColor);
        mBackColor = ColorUtils.generateBackColorWithTintColor(mTintColor);
        mIsBackUseDrawable = false;
        mIsThumbUseDrawable = false;
        // call this method to refresh color states
        refreshDrawableState();
        invalidate();
    }

    public void setText(CharSequence onText, CharSequence offText) {
        mTextOn = onText;
        mTextOff = offText;

        mOnLayout = null;
        mOffLayout = null;

        mReady = false;
        requestLayout();
        invalidate();
    }

    public CharSequence getTextOn() {
        return mTextOn;
    }

    public CharSequence getTextOff() {
        return mTextOff;
    }

    public void setTextThumbInset(int textThumbInset) {
        mTextThumbInset = textThumbInset;
        mReady = false;
        requestLayout();
        invalidate();
    }

    public void setTextExtra(int textExtra) {
        mTextExtra = textExtra;
        mReady = false;
        requestLayout();
        invalidate();
    }

    public void setTextAdjust(int textAdjust) {
        mTextAdjust = textAdjust;
        mReady = false;
        requestLayout();
        invalidate();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.onText = mTextOn;
        ss.offText = mTextOff;
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        setText(ss.onText, ss.offText);
        mRestoring = true;
        super.onRestoreInstanceState(ss.getSuperState());
        mRestoring = false;
    }

    static class SavedState extends BaseSavedState {
        CharSequence onText;
        CharSequence offText;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            onText = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
            offText = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            TextUtils.writeToParcel(onText, out, flags);
            TextUtils.writeToParcel(offText, out, flags);
        }

        public static final Parcelable.Creator<SavedState> CREATOR
                = new Parcelable.Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}