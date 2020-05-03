package com.xuexiang.xui.widget.popupwindow;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.xuexiang.xui.R;
import com.xuexiang.xui.XUI;
import com.xuexiang.xui.utils.ThemeUtils;

/**
 * 控件提示弹出窗，可自定义弹出的位置，持续时间以及样式
 *
 * @author xuexiang
 * @since 2019/1/14 上午11:23
 */
public class ViewTooltip {

    private final View mView;
    private final TooltipView mTooltipView;

    private ViewTooltip(View view) {
        mView = view;
        mTooltipView = new TooltipView(getActivityContext(view.getContext()));
    }

    /**
     * 创建并设置提示控件依附的View
     * @param view
     * @return
     */
    public static ViewTooltip on(final View view) {
        return new ViewTooltip(view);
    }

    public Activity getActivityContext(Context context) {
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    /**
     * 设置提示显示的相对位置
     * @param position
     * @return
     */
    public ViewTooltip position(Position position) {
        mTooltipView.setPosition(position);
        return this;
    }

    /**
     * 设置自定义提示布局
     * @param customView
     * @return
     */
    public ViewTooltip customView(View customView) {
        mTooltipView.setCustomView(customView);
        return this;
    }

    public ViewTooltip customView(int viewId) {
        mTooltipView.setCustomView(((Activity) mView.getContext()).findViewById(viewId));
        return this;
    }

    public ViewTooltip align(ALIGN align) {
        mTooltipView.setAlign(align);
        return this;
    }

    /**
     * 显示
     * @return
     */
    public TooltipView show() {
        final Context activityContext = mTooltipView.getContext();
        if (activityContext != null && activityContext instanceof Activity) {
            final ViewGroup decorView = (ViewGroup) ((Activity) activityContext).getWindow().getDecorView();
            mView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    final Rect rect = new Rect();
                    mView.getGlobalVisibleRect(rect);

                    int[] location = new int[2];
                    mView.getLocationOnScreen(location);
                    rect.left = location[0];
                    //rect.left = location[0];

                    decorView.addView(mTooltipView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    mTooltipView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {

                            mTooltipView.setup(rect, decorView.getWidth());

                            mTooltipView.getViewTreeObserver().removeOnPreDrawListener(this);

                            return false;
                        }
                    });
                }
            }, 100);
        }
        return mTooltipView;
    }

    public void close() {
        mTooltipView.close();
    }

    /**
     * 设置提示持续的时间
     * @param duration
     * @return
     */
    public ViewTooltip duration(long duration) {
        mTooltipView.setDuration(duration);
        return this;
    }

    /**
     * 设置提示框的背景颜色
     * @param color
     * @return
     */
    public ViewTooltip color(int color) {
        mTooltipView.setColor(color);
        return this;
    }

    /**
     * 设置显示的监听
     * @param listener
     * @return
     */
    public ViewTooltip onDisplay(ListenerDisplay listener) {
        mTooltipView.setListenerDisplay(listener);
        return this;
    }

    /**
     * 设置隐藏的监听
     * @param listener
     * @return
     */
    public ViewTooltip onHide(ListenerHide listener) {
        mTooltipView.setListenerHide(listener);
        return this;
    }

    /**
     * 设置间隔的距离
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @return
     */
    public ViewTooltip padding(int left, int top, int right, int bottom) {
        mTooltipView.mPaddingTop = top;
        mTooltipView.mPaddingBottom = bottom;
        mTooltipView.mPaddingLeft = left;
        mTooltipView.mPaddingRight = right;
        return this;
    }

    /**
     * 设置显示和隐藏的动画
     * @param tooltipAnimation
     * @return
     */
    public ViewTooltip animation(TooltipAnimation tooltipAnimation) {
        mTooltipView.setTooltipAnimation(tooltipAnimation);
        return this;
    }

    /**
     * 设置提示的文字
     * @param text
     * @return
     */
    public ViewTooltip text(String text) {
        mTooltipView.setText(text);
        return this;
    }

    /**
     * 设置圆角的角度
     * @param corner
     * @return
     */
    public ViewTooltip corner(int corner) {
        mTooltipView.setCorner(corner);
        return this;
    }

    /**
     * 设置提示文字的颜色
     * @param textColor
     * @return
     */
    public ViewTooltip textColor(int textColor) {
        mTooltipView.setTextColor(textColor);
        return this;
    }

    public ViewTooltip textTypeFace(Typeface typeface) {
        mTooltipView.setTextTypeFace(typeface);
        return this;
    }

    /**
     * 设置提示文字的字体大小
     * @param unit
     * @param textSize
     * @return
     */
    public ViewTooltip textSize(int unit, float textSize) {
        mTooltipView.setTextSize(unit, textSize);
        return this;
    }

    public ViewTooltip setTextGravity(int textGravity) {
        mTooltipView.setTextGravity(textGravity);
        return this;
    }

    /**
     * 设置是否点击隐藏
     * @param clickToHide
     * @return
     */
    public ViewTooltip clickToHide(boolean clickToHide) {
        mTooltipView.setClickToHide(clickToHide);
        return this;
    }

    public ViewTooltip autoHide(boolean autoHide, long duration) {
        mTooltipView.setAutoHide(autoHide);
        mTooltipView.setDuration(duration);
        return this;
    }

    /**
     * 显示的位置
     */
    public enum Position {
        LEFT,
        RIGHT,
        TOP,
        BOTTOM,
    }

    public enum ALIGN {
        START,
        CENTER,
    }

    public interface TooltipAnimation {
        void animateEnter(View view, Animator.AnimatorListener animatorListener);

        void animateExit(View view, Animator.AnimatorListener animatorListener);
    }

    /**
     * 显示监听
     */
    public interface ListenerDisplay {
        void onDisplay(View view);
    }

    /**
     * 隐藏监听
     */
    public interface ListenerHide {
        void onHide(View view);
    }

    public static class FadeTooltipAnimation implements TooltipAnimation {

        private long mFadeDuration = 400;

        public FadeTooltipAnimation() {
        }

        public FadeTooltipAnimation(long fadeDuration) {
            mFadeDuration = fadeDuration;
        }

        @Override
        public void animateEnter(View view, Animator.AnimatorListener animatorListener) {
            view.setAlpha(0);
            view.animate().alpha(1).setDuration(mFadeDuration).setListener(animatorListener);
        }

        @Override
        public void animateExit(View view, Animator.AnimatorListener animatorListener) {
            view.animate().alpha(0).setDuration(mFadeDuration).setListener(animatorListener);
        }
    }

    public static class TooltipView extends FrameLayout {

        private static final int MARGIN_SCREEN_BORDER_TOOLTIP = 30;
        private final int ARROW_HEIGHT = 15;
        protected View mChildView;
        private int mBubbleColor = Color.parseColor("#B2299EE3");
        private Path mBubblePath;
        private Paint mBubblePaint;
        private Position mPosition = Position.BOTTOM;
        private ALIGN mAlign = ALIGN.CENTER;
        private boolean mClickToHide;
        private boolean mAutoHide = true;
        private long mDuration = 4000;

        private ListenerDisplay mListenerDisplay;

        private ListenerHide mListenerHide;

        private TooltipAnimation mTooltipAnimation = new FadeTooltipAnimation();

        private int mCorner = 30;

        private int mPaddingTop;
        private int mPaddingBottom;
        private int mPaddingRight;
        private int mPaddingLeft;

        private Rect mViewRect;

        public TooltipView(Context context) {
            super(context);
            setWillNotDraw(false);

            mChildView = new TextView(context);
            ((TextView) mChildView).setTextColor(Color.WHITE);
            addView(mChildView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mChildView.setPadding(0, 0, 0, 0);

            mBubblePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mBubblePaint.setColor(mBubbleColor);
            mBubblePaint.setStyle(Paint.Style.FILL);

            mPaddingTop = mPaddingBottom = ThemeUtils.resolveDimension(getContext(), R.attr.xui_tip_popup_padding_top);
            mPaddingRight = mPaddingLeft = ThemeUtils.resolveDimension(getContext(), R.attr.xui_tip_popup_padding_left);

            setTextTypeFace(XUI.getDefaultTypeface());
        }

        public void setCustomView(View customView) {
            removeView(mChildView);
            mChildView = customView;
            addView(mChildView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        public void setColor(int color) {
            mBubbleColor = color;
            mBubblePaint.setColor(color);
            postInvalidate();
        }

        public void setPosition(Position position) {
            mPosition = position;
            switch (position) {
                case TOP:
                    setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom + ARROW_HEIGHT);
                    break;
                case BOTTOM:
                    setPadding(mPaddingLeft, mPaddingTop + ARROW_HEIGHT, mPaddingRight, mPaddingBottom);
                    break;
                case LEFT:
                    setPadding(mPaddingLeft, mPaddingTop, mPaddingRight + ARROW_HEIGHT, mPaddingBottom);
                    break;
                case RIGHT:
                    setPadding(mPaddingLeft + ARROW_HEIGHT, mPaddingTop, mPaddingRight, mPaddingBottom);
                    break;
                default:
                    break;
            }
            postInvalidate();
        }

        public void setAlign(ALIGN align) {
            mAlign = align;
            postInvalidate();
        }

        public void setText(String text) {
            if (mChildView instanceof TextView) {
                ((TextView) mChildView).setText(Html.fromHtml(text));
            }
            postInvalidate();
        }

        public void setTextColor(int textColor) {
            if (mChildView instanceof TextView) {
                ((TextView) mChildView).setTextColor(textColor);
            }
            postInvalidate();
        }

        public void setTextTypeFace(Typeface textTypeFace) {
            if (mChildView instanceof TextView) {
                ((TextView) mChildView).setTypeface(textTypeFace);
            }
            postInvalidate();
        }

        public void setTextSize(int unit, float size) {
            if (mChildView instanceof TextView) {
                ((TextView) mChildView).setTextSize(unit, size);
            }
            postInvalidate();
        }

        public void setTextGravity(int textGravity) {
            if (mChildView instanceof TextView) {
                ((TextView) mChildView).setGravity(textGravity);
            }
            postInvalidate();
        }

        public void setClickToHide(boolean clickToHide) {
            mClickToHide = clickToHide;
        }

        public void setCorner(int corner) {
            mCorner = corner;
        }

        @Override
        protected void onSizeChanged(int width, int height, int oldw, int oldh) {
            super.onSizeChanged(width, height, oldw, oldh);

            mBubblePath = drawBubble(new RectF(0, 0, width, height), mCorner, mCorner, mCorner, mCorner);

        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            if (mBubblePath != null) {
                canvas.drawPath(mBubblePath, mBubblePaint);
            }
        }

        public void setListenerDisplay(ListenerDisplay listener) {
            mListenerDisplay = listener;
        }

        public void setListenerHide(ListenerHide listener) {
            mListenerHide = listener;
        }

        public void setTooltipAnimation(TooltipAnimation tooltipAnimation) {
            mTooltipAnimation = tooltipAnimation;
        }

        protected void startEnterAnimation() {
            mTooltipAnimation.animateEnter(this, new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    if (mListenerDisplay != null) {
                        mListenerDisplay.onDisplay(TooltipView.this);
                    }
                }
            });
        }

        protected void startExitAnimation(final Animator.AnimatorListener animatorListener) {
            mTooltipAnimation.animateExit(this, new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    animatorListener.onAnimationEnd(animation);
                    if (mListenerHide != null) {
                        mListenerHide.onHide(TooltipView.this);
                    }
                }
            });
        }

        protected void handleAutoRemove() {
            if (mClickToHide) {
                setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mClickToHide) {
                            remove();
                        }
                    }
                });
            }

            if (mAutoHide) {
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        remove();
                    }
                }, mDuration);
            }
        }

        public void remove() {
            startExitAnimation(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    if (getParent() != null) {
                        final ViewGroup parent = ((ViewGroup) getParent());
                        parent.removeView(TooltipView.this);
                    }
                }
            });
        }

        public void setDuration(long duration) {
            mDuration = duration;
        }

        public void setAutoHide(boolean autoHide) {
            mAutoHide = autoHide;
        }

        public void setupPosition(Rect rect) {

            if (mPosition == Position.LEFT || mPosition == Position.RIGHT) {

                final int myHeight = getHeight();
                final int hisHeight = rect.height();

                final int maxHeight = Math.max(hisHeight, myHeight);
                final int minHeight = Math.min(hisHeight, myHeight);

                int spacingY = 0;
                switch (mAlign) {
                    case START:
                        spacingY = 0;
                        break;
                    //case END:
                    //    spacingY = maxHeight - minHeight;
                    //    break;
                    case CENTER:
                        spacingY = (int) (-1f * maxHeight / 2f + minHeight / 2f);
                        break;
                    default:
                        break;
                }

                if (mPosition == Position.LEFT) {
                    setTranslationY(rect.top + spacingY);
                    setTranslationX(rect.left - getWidth());
                } else {
                    setTranslationY(rect.top + spacingY);
                    setTranslationX(rect.right);
                }

            } else {
                int spacingX = 0;

                final int myWidth = getWidth();
                final int hisWidth = rect.width();

                if (mAlign == ALIGN.CENTER) {
                    spacingX = (int) (hisWidth / 2f - 1f * myWidth / 2f);
                }

                if (mPosition == Position.BOTTOM) {
                    setTranslationY(rect.bottom);
                    setTranslationX(rect.left + spacingX);
                } else {
                    setTranslationY(rect.top - getHeight());
                    setTranslationX(rect.left + spacingX);
                }
            }
        }

        /**
         * 画边框
         *
         * @param myRect
         * @param topLeftDiameter
         * @param topRightDiameter
         * @param bottomRightDiameter
         * @param bottomLeftDiameter
         * @return
         */
        private Path drawBubble(RectF myRect, float topLeftDiameter, float topRightDiameter, float bottomRightDiameter, float bottomLeftDiameter) {
            final Path path = new Path();

            if (mViewRect == null) {
                return path;
            }

            topLeftDiameter = topLeftDiameter < 0 ? 0 : topLeftDiameter;
            topRightDiameter = topRightDiameter < 0 ? 0 : topRightDiameter;
            bottomLeftDiameter = bottomLeftDiameter < 0 ? 0 : bottomLeftDiameter;
            bottomRightDiameter = bottomRightDiameter < 0 ? 0 : bottomRightDiameter;

            final float spacingLeft = mPosition == Position.RIGHT ? ARROW_HEIGHT : 0;
            final float spacingTop = mPosition == Position.BOTTOM ? ARROW_HEIGHT : 0;
            final float spacingRight = mPosition == Position.LEFT ? ARROW_HEIGHT : 0;
            final float spacingBottom = mPosition == Position.TOP ? ARROW_HEIGHT : 0;

            final float left = spacingLeft + myRect.left;
            final float top = spacingTop + myRect.top;
            final float right = myRect.right - spacingRight;
            final float bottom = myRect.bottom - spacingBottom;
            final float centerX = mViewRect.centerX() - getX();

            path.moveTo(left + topLeftDiameter / 2f, top);
            //LEFT, TOP

            int ARROW_WIDTH = 15;
            if (mPosition == Position.BOTTOM) {
                path.lineTo(centerX - ARROW_WIDTH, top);
                path.lineTo(centerX, myRect.top);
                path.lineTo(centerX + ARROW_WIDTH, top);
            }
            path.lineTo(right - topRightDiameter / 2f, top);

            path.quadTo(right, top, right, top + topRightDiameter / 2);
            //RIGHT, TOP

            if (mPosition == Position.LEFT) {
                path.lineTo(right, bottom / 2f - ARROW_WIDTH);
                path.lineTo(myRect.right, bottom / 2f);
                path.lineTo(right, bottom / 2f + ARROW_WIDTH);
            }
            path.lineTo(right, bottom - bottomRightDiameter / 2);

            path.quadTo(right, bottom, right - bottomRightDiameter / 2, bottom);
            //RIGHT, BOTTOM

            if (mPosition == Position.TOP) {
                path.lineTo(centerX + ARROW_WIDTH, bottom);
                path.lineTo(centerX, myRect.bottom);
                path.lineTo(centerX - ARROW_WIDTH, bottom);
            }
            path.lineTo(left + bottomLeftDiameter / 2, bottom);

            path.quadTo(left, bottom, left, bottom - bottomLeftDiameter / 2);
            //LEFT, BOTTOM

            if (mPosition == Position.RIGHT) {
                path.lineTo(left, bottom / 2f + ARROW_WIDTH);
                path.lineTo(myRect.left, bottom / 2f);
                path.lineTo(left, bottom / 2f - ARROW_WIDTH);
            }
            path.lineTo(left, top + topLeftDiameter / 2);

            path.quadTo(left, top, left + topLeftDiameter / 2, top);

            path.close();

            return path;
        }

        public boolean adjustSize(Rect rect, int screenWidth) {
            boolean changed = false;
            final ViewGroup.LayoutParams layoutParams = getLayoutParams();
            if (mPosition == Position.LEFT && getWidth() > rect.left) {
                layoutParams.width = rect.left - MARGIN_SCREEN_BORDER_TOOLTIP;
                changed = true;
            } else if (mPosition == Position.RIGHT && rect.right + getWidth() > screenWidth) {
                layoutParams.width = screenWidth - rect.right - MARGIN_SCREEN_BORDER_TOOLTIP;
                changed = true;
            } else if (mPosition == Position.TOP || mPosition == Position.BOTTOM) {
                float widthRight = (getWidth() - rect.width()) / 2f;
                if (rect.right + widthRight > screenWidth) {
                    final float movinX = (rect.right + widthRight) - screenWidth + 30;
                    rect.left -= movinX;
                    rect.right -= movinX;
                    changed = true;
                } else if (rect.left - widthRight < 0) {
                    final float movinX = 0 - (rect.left - widthRight) + 30;
                    rect.left += movinX;
                    rect.right += movinX;
                    changed = true;
                }
            }
            setLayoutParams(layoutParams);
            postInvalidate();
            return changed;
        }

        private void onSetup(Rect myRect) {
            setupPosition(myRect);

            mBubblePath = drawBubble(new RectF(0, 0, getWidth(), getHeight()), mCorner, mCorner, mCorner, mCorner);

            startEnterAnimation();

            handleAutoRemove();
        }

        public void setup(final Rect viewRect, int screenWidth) {
            mViewRect = new Rect(viewRect);
            final Rect myRect = new Rect(viewRect);

            final boolean changed = adjustSize(myRect, screenWidth);
            if (!changed) {
                onSetup(myRect);
            } else {
                getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        onSetup(myRect);
                        getViewTreeObserver().removeOnPreDrawListener(this);
                        return false;
                    }
                });
            }
        }

        public void close() {
            remove();
        }

    }
}
