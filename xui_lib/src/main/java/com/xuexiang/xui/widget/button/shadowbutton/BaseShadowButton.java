package com.xuexiang.xui.widget.button.shadowbutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

import com.xuexiang.xui.R;

/**
 * 基础可设置阴影效果的Button
 *
 * @author xuexiang
 * @since 2019/1/14 下午10:00
 */
public class BaseShadowButton extends AppCompatButton {

	public static final int SHAPE_TYPE_ROUND = 0;
	public static final int SHAPE_TYPE_RECTANGLE = 1;

	protected int mWidth;
	protected int mHeight;
	protected Paint mBackgroundPaint;
	protected int mShapeType;
	protected int mRadius;

	protected RectF mRectF;

	public BaseShadowButton(Context context) {
		super(context);
		init(context, null);
	}

	public BaseShadowButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public BaseShadowButton(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}

	protected void init(final Context context, final AttributeSet attrs) {
		final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShadowButton);
		mShapeType = typedArray.getInt(R.styleable.ShadowButton_sb_shape_type, SHAPE_TYPE_RECTANGLE);
		mRadius = typedArray.getDimensionPixelSize(R.styleable.ShadowButton_sb_radius, getResources().getDimensionPixelSize(R.dimen.default_shadow_button_radius));
		int unpressedColor = typedArray.getColor(R.styleable.ShadowButton_sb_color_unpressed, Color.TRANSPARENT);
		typedArray.recycle();

		mBackgroundPaint = new Paint();
		mBackgroundPaint.setStyle(Paint.Style.FILL);
		mBackgroundPaint.setAlpha(Color.alpha(unpressedColor));
		mBackgroundPaint.setColor(unpressedColor);
		mBackgroundPaint.setAntiAlias(true);

		this.setWillNotDraw(false);
		this.setDrawingCacheEnabled(true);
		this.setClickable(true);
		this.eraseOriginalBackgroundColor(unpressedColor);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mWidth = w;
		mHeight = h;
		mRectF = new RectF(0, 0, mWidth, mHeight);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (mBackgroundPaint == null) {
			super.onDraw(canvas);
			return;
		}
		if (mShapeType == SHAPE_TYPE_ROUND) {
			canvas.drawCircle(mWidth / 2F, mHeight / 2F, mWidth / 2F, mBackgroundPaint);
		} else {
			canvas.drawRoundRect(mRectF, mRadius, mRadius, mBackgroundPaint);
		}
		super.onDraw(canvas);
	}

	protected void eraseOriginalBackgroundColor(int color) {
		if (color != Color.TRANSPARENT) {
			this.setBackgroundColor(Color.TRANSPARENT);
		}
	}

	/**
	 * Set the unpressed color.
	 * 
	 * @param color
	 *            the color of the background
	 */
	public void setUnpressedColor(int color) {
		mBackgroundPaint.setAlpha(Color.alpha(color));
		mBackgroundPaint.setColor(color);
		eraseOriginalBackgroundColor(color);
		invalidate();
	}

	public int getShapeType() {
		return mShapeType;
	}

	/**
	 * Set the shape type.
	 * 
	 * @param shapeType
	 *            SHAPE_TYPE_ROUND or SHAPE_TYPE_RECTANGLE
	 */
	public void setShapeType(int shapeType) {
		mShapeType = shapeType;
		invalidate();
	}

	public int getRadius() {
		return mRadius;
	}

	/**
	 * Set the radius if the shape type is SHAPE_TYPE_ROUND.
	 * 
	 * @param radius
	 *            by px.
	 */
	public void setRadius(int radius) {
		mRadius = radius;
		invalidate();
	}
}
