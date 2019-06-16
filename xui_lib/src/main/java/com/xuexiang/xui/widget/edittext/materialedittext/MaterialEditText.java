package com.xuexiang.xui.widget.edittext.materialedittext;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import android.text.Editable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;

import com.nineoldandroids.animation.ArgbEvaluator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.xuexiang.xui.R;
import com.xuexiang.xui.XUI;
import com.xuexiang.xui.utils.DensityUtils;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.ThemeUtils;
import com.xuexiang.xui.utils.Utils;
import com.xuexiang.xui.widget.edittext.materialedittext.validation.METLengthChecker;
import com.xuexiang.xui.widget.edittext.materialedittext.validation.METValidator;
import com.xuexiang.xui.widget.edittext.materialedittext.validation.NotAllowEmptyValidator;
import com.xuexiang.xui.widget.edittext.materialedittext.validation.RegexpValidator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.co.chrisjenx.calligraphy.HasTypeface;

/**
 * Material Design 输入框
 *
 * @author XUE
 * @since 2019/3/20 16:47
 */
public class MaterialEditText extends AppCompatEditText implements HasTypeface {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({FLOATING_LABEL_NONE, FLOATING_LABEL_NORMAL, FLOATING_LABEL_HIGHLIGHT})
    public @interface FloatingLabelType {
    }

    public static final int FLOATING_LABEL_NONE = 0;
    public static final int FLOATING_LABEL_NORMAL = 1;
    public static final int FLOATING_LABEL_HIGHLIGHT = 2;

    /**
     * the spacing between the main text and the inner top padding.
     */
    private int extraPaddingTop;

    /**
     * the spacing between the main text and the inner bottom padding.
     */
    private int extraPaddingBottom;

    /**
     * the extra spacing between the main text and the left, actually for the left icon.
     */
    private int extraPaddingLeft;

    /**
     * the extra spacing between the main text and the right, actually for the right icon.
     */
    private int extraPaddingRight;

    /**
     * the floating label's text size.
     */
    private int floatingLabelTextSize;

    /**
     * the floating label's text color.
     */
    private int floatingLabelTextColor;

    /**
     * the bottom texts' size.
     */
    private int bottomTextSize;

    /**
     * the spacing between the main text and the floating label.
     */
    private int floatingLabelPadding;

    /**
     * the spacing between the main text and the bottom components (bottom ellipsis, helper/error text, characters counter).
     */
    private int bottomSpacing;

    /**
     * whether the floating label should be shown. default is false.
     */
    private boolean floatingLabelEnabled;

    /**
     * whether to highlight the floating label's text color when focused (with the main color). default is true.
     */
    private boolean highlightFloatingLabel;

    /**
     * the base color of the line and the texts. default is black.
     */
    private int baseColor;

    /**
     * inner top padding
     */
    private int innerPaddingTop;

    /**
     * inner bottom padding
     */
    private int innerPaddingBottom;

    /**
     * inner left padding
     */
    private int innerPaddingLeft;

    /**
     * inner right padding
     */
    private int innerPaddingRight;

    /**
     * the underline's highlight color, and the highlight color of the floating label if app:highlightFloatingLabel is set true in the xml. default is black(when app:darkTheme is false) or white(when app:darkTheme is true)
     */
    private int primaryColor;

    /**
     * the color for when something is wrong.(e.g. exceeding max characters)
     */
    private int errorColor;

    /**
     * min characters count limit. 0 means no limit. default is 0. NOTE: the character counter will increase the View's height.
     */
    private int minCharacters;

    /**
     * max characters count limit. 0 means no limit. default is 0. NOTE: the character counter will increase the View's height.
     */
    private int maxCharacters;

    /**
     * whether to show the bottom ellipsis in singleLine mode. default is false. NOTE: the bottom ellipsis will increase the View's height.
     */
    private boolean singleLineEllipsis;

    /**
     * Always show the floating label, instead of animating it in/out. False by default.
     */
    private boolean floatingLabelAlwaysShown;

    /**
     * Always show the helper text, no matter if the edit text is focused. False by default.
     */
    private boolean helperTextAlwaysShown;

    /**
     * bottom ellipsis's height
     */
    private int bottomEllipsisSize;

    /**
     * min bottom lines count.
     */
    private int minBottomLines;

    /**
     * reserved bottom text lines count, no matter if there is some helper/error text.
     */
    private int minBottomTextLines;

    /**
     * real-time bottom lines count. used for bottom extending/collapsing animation.
     */
    private float currentBottomLines;

    /**
     * bottom lines count.
     */
    private float bottomLines;

    /**
     * Helper text at the bottom
     */
    private String helperText;

    /**
     * Helper text color
     */
    private int helperTextColor = -1;

    /**
     * error text for manually invoked
     */
    private String tempErrorText;

    /**
     * animation fraction of the floating label (0 as totally hidden).
     */
    private float floatingLabelFraction;

    /**
     * whether the floating label is being shown.
     */
    private boolean floatingLabelShown;

    /**
     * the floating label's focusFraction
     */
    private float focusFraction;

    /**
     * The font used for the accent texts (floating label, error/helper text, character counter, etc.)
     */
    private Typeface accentTypeface;

    /**
     * The font used on the view (EditText content)
     */
    private Typeface typeface;

    /**
     * Text for the floatLabel if different from the hint
     */
    private CharSequence floatingLabelText;

    /**
     * Whether or not to show the underline. Shown by default
     */
    private boolean hideUnderline;

    /**
     * Underline's color
     */
    private int underlineColor;

    /**
     * Whether to validate as soon as the text has changed. False by default
     */
    private boolean autoValidate;

    /**
     * Whether the characters count is valid
     */
    private boolean charactersCountValid;

    /**
     * Whether use animation to show/hide the floating label.
     */
    private boolean floatingLabelAnimating;

    /**
     * Whether check the characters count at the beginning it's shown.
     */
    private boolean checkCharactersCountAtBeginning;

    /**
     * Left Icon
     */
    private Bitmap[] iconLeftBitmaps;

    /**
     * Right Icon
     */
    private Bitmap[] iconRightBitmaps;

    /**
     * Clear Button
     */
    private Bitmap[] clearButtonBitmaps;
    /**
     * showPwIcon Button
     */
    private Bitmap[] showPwIconBitmaps;
    /**
     * hidePwIcon Button
     */
    private Bitmap[] hidePwIconBitmaps;

    private boolean passwordVisible;

    /**
     * Auto validate when focus lost.
     */
    private boolean validateOnFocusLost;

    private boolean showClearButton;
    private boolean showPasswordButton;
    private boolean firstShown;
    private int iconSize;
    private int iconOuterWidth;
    private int iconOuterHeight;
    private int iconPadding;
    private boolean actionButtonTouched;
    private boolean actionButtonClicking;
    private ColorStateList textColorStateList;
    private ColorStateList textColorHintStateList;
    private ArgbEvaluator focusEvaluator = new ArgbEvaluator();
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    StaticLayout textLayout;
    ObjectAnimator labelAnimator;
    ObjectAnimator labelFocusAnimator;
    ObjectAnimator bottomLinesAnimator;
    OnFocusChangeListener innerFocusChangeListener;
    OnFocusChangeListener outerFocusChangeListener;
    private List<METValidator> validators = new ArrayList<>();
    private METLengthChecker lengthChecker;

    public MaterialEditText(Context context) {
        this(context, null);
    }

    public MaterialEditText(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.MaterialEditTextStyle);
    }

    public MaterialEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        if (isInEditMode()) {
            return;
        }

        iconSize = getPixel(32);
        iconOuterWidth = getPixel(16);
        iconOuterHeight = getPixel(32);

        bottomSpacing = getResources().getDimensionPixelSize(R.dimen.default_edittext_components_spacing);
        bottomEllipsisSize = getResources().getDimensionPixelSize(R.dimen.default_bottom_ellipsis_height);

        // default baseColor is black
        int defaultBaseColor = Color.BLACK;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MaterialEditText, defStyleAttr, 0);
        textColorStateList = typedArray.getColorStateList(R.styleable.MaterialEditText_met_textColor);
        textColorHintStateList = typedArray.getColorStateList(R.styleable.MaterialEditText_met_textColorHint);
        baseColor = typedArray.getColor(R.styleable.MaterialEditText_met_baseColor, defaultBaseColor);

        primaryColor = typedArray.getColor(R.styleable.MaterialEditText_met_primaryColor, ThemeUtils.resolveColor(getContext(), R.attr.colorPrimary, baseColor));
        setFloatingLabelInternal(typedArray.getInt(R.styleable.MaterialEditText_met_floatingLabel, 0));
        errorColor = typedArray.getColor(R.styleable.MaterialEditText_met_errorColor, ResUtils.getColor(R.color.xui_config_color_edittext_error_text));

        boolean allowEmpty = typedArray.getBoolean(R.styleable.MaterialEditText_met_allowEmpty, true);
        if (!allowEmpty) {
            String errorEmpty = typedArray.getString(R.styleable.MaterialEditText_met_errorEmpty);
            if (!TextUtils.isEmpty(errorEmpty)) {
                validators.add(new NotAllowEmptyValidator(errorEmpty));
            } else {
                validators.add(new NotAllowEmptyValidator(ResUtils.getString(R.string.xui_met_not_allow_empty)));
            }
        }
        minCharacters = typedArray.getInt(R.styleable.MaterialEditText_met_minCharacters, 0);
        maxCharacters = typedArray.getInt(R.styleable.MaterialEditText_met_maxCharacters, 0);
        singleLineEllipsis = typedArray.getBoolean(R.styleable.MaterialEditText_met_singleLineEllipsis, false);
        helperText = typedArray.getString(R.styleable.MaterialEditText_met_helperText);
        helperTextColor = typedArray.getColor(R.styleable.MaterialEditText_met_helperTextColor, -1);
        minBottomTextLines = typedArray.getInt(R.styleable.MaterialEditText_met_minBottomTextLines, 0);
        String fontPathForAccent = typedArray.getString(R.styleable.MaterialEditText_met_accentTypeface);
        if (fontPathForAccent != null && !isInEditMode()) {
            accentTypeface = XUI.getDefaultTypeface(fontPathForAccent);
            textPaint.setTypeface(accentTypeface);
        }
        String fontPathForView = typedArray.getString(R.styleable.MaterialEditText_met_typeface);
        if (fontPathForView != null && !isInEditMode()) {
            typeface = XUI.getDefaultTypeface(fontPathForView);
            setTypeface(typeface);
        }
        floatingLabelText = typedArray.getString(R.styleable.MaterialEditText_met_floatingLabelText);
        if (floatingLabelText == null) {
            floatingLabelText = getHint();
        }
        floatingLabelPadding = typedArray.getDimensionPixelSize(R.styleable.MaterialEditText_met_floatingLabelPadding, bottomSpacing);
        floatingLabelTextSize = typedArray.getDimensionPixelSize(R.styleable.MaterialEditText_met_floatingLabelTextSize, getResources().getDimensionPixelSize(R.dimen.default_floating_label_text_size));
        floatingLabelTextColor = typedArray.getColor(R.styleable.MaterialEditText_met_floatingLabelTextColor, -1);
        floatingLabelAnimating = typedArray.getBoolean(R.styleable.MaterialEditText_met_floatingLabelAnimating, true);
        bottomTextSize = typedArray.getDimensionPixelSize(R.styleable.MaterialEditText_met_bottomTextSize, getResources().getDimensionPixelSize(R.dimen.default_bottom_text_size));
        hideUnderline = typedArray.getBoolean(R.styleable.MaterialEditText_met_hideUnderline, false);
        underlineColor = typedArray.getColor(R.styleable.MaterialEditText_met_underlineColor, -1);
        autoValidate = typedArray.getBoolean(R.styleable.MaterialEditText_met_autoValidate, false);
        iconLeftBitmaps = generateIconBitmaps(typedArray.getResourceId(R.styleable.MaterialEditText_met_iconLeft, -1));
        iconRightBitmaps = generateIconBitmaps(typedArray.getResourceId(R.styleable.MaterialEditText_met_iconRight, -1));

        showClearButton = typedArray.getBoolean(R.styleable.MaterialEditText_met_clearButton, false);
        clearButtonBitmaps = generateIconBitmaps(R.drawable.xui_ic_met_clear);
        showPasswordButton = typedArray.getBoolean(R.styleable.MaterialEditText_met_passWordButton, false);
        showPwIconBitmaps = generateIconBitmaps(R.drawable.pet_icon_visibility);
        hidePwIconBitmaps = generateIconBitmaps(R.drawable.pet_icon_visibility_off);

        String regexp = typedArray.getString(R.styleable.MaterialEditText_met_regexp);
        if (!TextUtils.isEmpty(regexp)) {
            String errorMessage = typedArray.getString(R.styleable.MaterialEditText_met_errorMessage);
            if (!TextUtils.isEmpty(errorMessage)) {
                validators.add(new RegexpValidator(errorMessage, regexp));
            } else {
                validators.add(new RegexpValidator(ResUtils.getString(R.string.xui_met_input_error), regexp));
            }
        }

        iconPadding = typedArray.getDimensionPixelSize(R.styleable.MaterialEditText_met_iconPadding, getPixel(16));
        floatingLabelAlwaysShown = typedArray.getBoolean(R.styleable.MaterialEditText_met_floatingLabelAlwaysShown, false);
        helperTextAlwaysShown = typedArray.getBoolean(R.styleable.MaterialEditText_met_helperTextAlwaysShown, false);
        validateOnFocusLost = typedArray.getBoolean(R.styleable.MaterialEditText_met_validateOnFocusLost, false);
        checkCharactersCountAtBeginning = typedArray.getBoolean(R.styleable.MaterialEditText_met_checkCharactersCountAtBeginning, true);
        typedArray.recycle();

        int[] paddings = new int[]{
                android.R.attr.padding, // 0
                android.R.attr.paddingLeft, // 1
                android.R.attr.paddingTop, // 2
                android.R.attr.paddingRight, // 3
                android.R.attr.paddingBottom // 4
        };
        TypedArray paddingsTypedArray = context.obtainStyledAttributes(attrs, paddings);
        int padding = paddingsTypedArray.getDimensionPixelSize(0, 0);
        innerPaddingLeft = paddingsTypedArray.getDimensionPixelSize(1, padding);
        innerPaddingTop = paddingsTypedArray.getDimensionPixelSize(2, padding);
        innerPaddingRight = paddingsTypedArray.getDimensionPixelSize(3, padding);
        innerPaddingBottom = paddingsTypedArray.getDimensionPixelSize(4, padding);
        paddingsTypedArray.recycle();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(null);
        } else {
            setBackgroundDrawable(null);
        }
        if (singleLineEllipsis) {
            TransformationMethod transformationMethod = getTransformationMethod();
            setSingleLine();
            setTransformationMethod(transformationMethod);
        }
        initMinBottomLines();
        initPadding();
        initText();
        initFloatingLabel();
        initTextWatcher();
        checkCharactersCount();
    }

    private void initText() {
        if (!TextUtils.isEmpty(getText())) {
            CharSequence text = getText();
            setText(null);
            resetHintTextColor();
            setText(text);
            setSelection(text.length());
            floatingLabelFraction = 1;
            floatingLabelShown = true;
        } else {
            resetHintTextColor();
        }
        resetTextColor();
    }

    private void initTextWatcher() {
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkCharactersCount();
                if (autoValidate) {
                    validate();
                } else {
                    setError(null);
                }
                postInvalidate();
            }
        });
    }

    public MaterialEditText setIconLeft(@DrawableRes int res) {
        iconLeftBitmaps = generateIconBitmaps(res);
        initPadding();
        return this;
    }

    public MaterialEditText setIconLeft(Drawable drawable) {
        iconLeftBitmaps = generateIconBitmaps(drawable);
        initPadding();
        return this;
    }

    public MaterialEditText setIconLeft(Bitmap bitmap) {
        iconLeftBitmaps = generateIconBitmaps(bitmap);
        initPadding();
        return this;
    }

    public MaterialEditText setIconRight(@DrawableRes int res) {
        iconRightBitmaps = generateIconBitmaps(res);
        initPadding();
        return this;
    }

    public MaterialEditText setIconRight(Drawable drawable) {
        iconRightBitmaps = generateIconBitmaps(drawable);
        initPadding();
        return this;
    }

    public MaterialEditText setIconRight(Bitmap bitmap) {
        iconRightBitmaps = generateIconBitmaps(bitmap);
        initPadding();
        return this;
    }

    public boolean isShowClearButton() {
        return showClearButton;
    }

    public boolean isShowPasswordButton() {
        return showPasswordButton;
    }

    public MaterialEditText setShowClearButton(boolean show) {
        showClearButton = show;
        correctPaddings();
        return this;
    }

    private Bitmap[] generateIconBitmaps(@DrawableRes int origin) {
        if (origin == -1) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), origin, options);
        int size = Math.max(options.outWidth, options.outHeight);
        options.inSampleSize = size > iconSize ? size / iconSize : 1;
        options.inJustDecodeBounds = false;
        return generateIconBitmaps(BitmapFactory.decodeResource(getResources(), origin, options));
    }

    private Bitmap[] generateIconBitmaps(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return generateIconBitmaps(Bitmap.createScaledBitmap(bitmap, iconSize, iconSize, false));
    }

    private Bitmap[] generateIconBitmaps(Bitmap origin) {
        if (origin == null) {
            return null;
        }
        Bitmap[] iconBitmaps = new Bitmap[4];
        origin = scaleIcon(origin);
        iconBitmaps[0] = origin.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(iconBitmaps[0]);
        canvas.drawColor(baseColor & 0x00ffffff | (Utils.isLight(baseColor) ? 0xff000000 : 0x8a000000), PorterDuff.Mode.SRC_IN);
        iconBitmaps[1] = origin.copy(Bitmap.Config.ARGB_8888, true);
        canvas = new Canvas(iconBitmaps[1]);
        canvas.drawColor(primaryColor, PorterDuff.Mode.SRC_IN);
        iconBitmaps[2] = origin.copy(Bitmap.Config.ARGB_8888, true);
        canvas = new Canvas(iconBitmaps[2]);
        canvas.drawColor(baseColor & 0x00ffffff | (Utils.isLight(baseColor) ? 0x4c000000 : 0x42000000), PorterDuff.Mode.SRC_IN);
        iconBitmaps[3] = origin.copy(Bitmap.Config.ARGB_8888, true);
        canvas = new Canvas(iconBitmaps[3]);
        canvas.drawColor(errorColor, PorterDuff.Mode.SRC_IN);
        return iconBitmaps;
    }

    private Bitmap scaleIcon(Bitmap origin) {
        int width = origin.getWidth();
        int height = origin.getHeight();
        int size = Math.max(width, height);
        if (size == iconSize) {
            return origin;
        } else if (size > iconSize) {
            int scaledWidth;
            int scaledHeight;
            if (width > iconSize) {
                scaledWidth = iconSize;
                scaledHeight = (int) (iconSize * ((float) height / width));
            } else {
                scaledHeight = iconSize;
                scaledWidth = (int) (iconSize * ((float) width / height));
            }
            return Bitmap.createScaledBitmap(origin, scaledWidth, scaledHeight, false);
        } else {
            return origin;
        }
    }

    public float getFloatingLabelFraction() {
        return floatingLabelFraction;
    }

    public MaterialEditText setFloatingLabelFraction(float floatingLabelFraction) {
        this.floatingLabelFraction = floatingLabelFraction;
        invalidate();
        return this;
    }

    public float getFocusFraction() {
        return focusFraction;
    }

    public MaterialEditText setFocusFraction(float focusFraction) {
        this.focusFraction = focusFraction;
        invalidate();
        return this;
    }

    public float getCurrentBottomLines() {
        return currentBottomLines;
    }

    public MaterialEditText setCurrentBottomLines(float currentBottomLines) {
        this.currentBottomLines = currentBottomLines;
        initPadding();
        return this;
    }

    public boolean isFloatingLabelAlwaysShown() {
        return floatingLabelAlwaysShown;
    }

    public MaterialEditText setFloatingLabelAlwaysShown(boolean floatingLabelAlwaysShown) {
        this.floatingLabelAlwaysShown = floatingLabelAlwaysShown;
        invalidate();
        return this;
    }

    public boolean isHelperTextAlwaysShown() {
        return helperTextAlwaysShown;
    }

    public MaterialEditText setHelperTextAlwaysShown(boolean helperTextAlwaysShown) {
        this.helperTextAlwaysShown = helperTextAlwaysShown;
        invalidate();
        return this;
    }

    @Nullable
    public Typeface getAccentTypeface() {
        return accentTypeface;
    }

    /**
     * Set typeface used for the accent texts (floating label, error/helper text, character counter, etc.)
     */
    public MaterialEditText setAccentTypeface(Typeface accentTypeface) {
        this.accentTypeface = accentTypeface;
        this.textPaint.setTypeface(accentTypeface);
        postInvalidate();
        return this;
    }

    public boolean isHideUnderline() {
        return hideUnderline;
    }

    /**
     * Set whether or not to hide the underline (shown by default).
     * <p/>
     * The positions of text below will be adjusted accordingly (error/helper text, character counter, ellipses, etc.)
     * <p/>
     * NOTE: You probably don't want to hide this if you have any subtext features of this enabled, as it can look weird to not have a dividing line between them.
     */
    public MaterialEditText setHideUnderline(boolean hideUnderline) {
        this.hideUnderline = hideUnderline;
        initPadding();
        postInvalidate();
        return this;
    }

    /**
     * get the color of the underline for normal state
     */
    public int getUnderlineColor() {
        return underlineColor;
    }

    /**
     * Set the color of the underline for normal state
     *
     * @param color
     */
    public MaterialEditText setUnderlineColor(int color) {
        this.underlineColor = color;
        postInvalidate();
        return this;
    }

    public CharSequence getFloatingLabelText() {
        return floatingLabelText;
    }

    /**
     * Set the floating label text.
     * <p/>
     * Pass null to force fallback to use hint's value.
     *
     * @param floatingLabelText
     */
    public MaterialEditText setFloatingLabelText(@Nullable CharSequence floatingLabelText) {
        this.floatingLabelText = floatingLabelText == null ? getHint() : floatingLabelText;
        postInvalidate();
        return this;
    }

    public int getFloatingLabelTextSize() {
        return floatingLabelTextSize;
    }

    public MaterialEditText setFloatingLabelTextSize(int size) {
        floatingLabelTextSize = size;
        initPadding();
        return this;
    }

    public int getFloatingLabelTextColor() {
        return floatingLabelTextColor;
    }

    public MaterialEditText setFloatingLabelTextColor(int color) {
        this.floatingLabelTextColor = color;
        postInvalidate();
        return this;
    }

    public int getBottomTextSize() {
        return bottomTextSize;
    }

    public void setBottomTextSize(int size) {
        bottomTextSize = size;
        initPadding();
    }

    private int getPixel(int dp) {
        return DensityUtils.dp2px(getContext(), dp);
    }

    private void initPadding() {
        extraPaddingTop = floatingLabelEnabled ? floatingLabelTextSize + floatingLabelPadding : floatingLabelPadding;
        textPaint.setTextSize(bottomTextSize);
        Paint.FontMetrics textMetrics = textPaint.getFontMetrics();
        extraPaddingBottom = (int) ((textMetrics.descent - textMetrics.ascent) * currentBottomLines) + (hideUnderline ? bottomSpacing : bottomSpacing * 2);
        extraPaddingLeft = iconLeftBitmaps == null ? 0 : (iconOuterWidth + iconPadding);
        extraPaddingRight = iconRightBitmaps == null ? 0 : (iconOuterWidth + iconPadding);
        correctPaddings();
    }

    /**
     * calculate {@link #minBottomLines}
     */
    private void initMinBottomLines() {
        boolean extendBottom = minCharacters > 0 || maxCharacters > 0 || singleLineEllipsis || tempErrorText != null || helperText != null;
        currentBottomLines = minBottomLines = minBottomTextLines > 0 ? minBottomTextLines : extendBottom ? 1 : 0;
    }

    /**
     * use {@link #setPaddings(int, int, int, int)} instead, or the paddingTop and the paddingBottom may be set incorrectly.
     */
    @Deprecated
    @Override
    public final void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
    }

    /**
     * Use this method instead of {@link #setPadding(int, int, int, int)} to automatically set the paddingTop and the paddingBottom correctly.
     */
    public MaterialEditText setPaddings(int left, int top, int right, int bottom) {
        innerPaddingTop = top;
        innerPaddingBottom = bottom;
        innerPaddingLeft = left;
        innerPaddingRight = right;
        correctPaddings();
        return this;
    }

    /**
     * Set paddings to the correct values
     */
    private void correctPaddings() {
        int buttonsWidthLeft = 0, buttonsWidthRight = 0;
        int buttonsWidth = iconOuterWidth * getButtonsCount();
        if (isRTL()) {
            buttonsWidthLeft = buttonsWidth;
        } else {
            buttonsWidthRight = buttonsWidth;
        }
        super.setPadding(innerPaddingLeft + extraPaddingLeft + buttonsWidthLeft, innerPaddingTop + extraPaddingTop, innerPaddingRight + extraPaddingRight + buttonsWidthRight, innerPaddingBottom + extraPaddingBottom);
    }

    private int getButtonsCount() {
        return isShowClearButton() || isShowPasswordButton() ? 1 : 0;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!firstShown) {
            firstShown = true;
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            adjustBottomLines();
        }
    }

    /**
     * @return True, if adjustments were made that require the view to be invalidated.
     */
    private boolean adjustBottomLines() {
        // Bail out if we have a zero width; lines will be adjusted during next layout.
        if (getWidth() == 0) {
            return false;
        }
        int destBottomLines;
        textPaint.setTextSize(bottomTextSize);
        if (tempErrorText != null || helperText != null) {
            Layout.Alignment alignment = (getGravity() & Gravity.RIGHT) == Gravity.RIGHT || isRTL() ?
                    Layout.Alignment.ALIGN_OPPOSITE : (getGravity() & Gravity.LEFT) == Gravity.LEFT ?
                    Layout.Alignment.ALIGN_NORMAL : Layout.Alignment.ALIGN_CENTER;
            textLayout = new StaticLayout(tempErrorText != null ? tempErrorText : helperText, textPaint, getWidth() - getBottomTextLeftOffset() - getBottomTextRightOffset() - getPaddingLeft() - getPaddingRight(), alignment, 1.0f, 0.0f, true);
            destBottomLines = Math.max(textLayout.getLineCount(), minBottomTextLines);
        } else {
            destBottomLines = minBottomLines;
        }
        if (bottomLines != destBottomLines) {
            getBottomLinesAnimator(destBottomLines).start();
        }
        bottomLines = destBottomLines;
        return true;
    }

    /**
     * get inner top padding, not the real paddingTop
     */
    public int getInnerPaddingTop() {
        return innerPaddingTop;
    }

    /**
     * get inner bottom padding, not the real paddingBottom
     */
    public int getInnerPaddingBottom() {
        return innerPaddingBottom;
    }

    /**
     * get inner left padding, not the real paddingLeft
     */
    public int getInnerPaddingLeft() {
        return innerPaddingLeft;
    }

    /**
     * get inner right padding, not the real paddingRight
     */
    public int getInnerPaddingRight() {
        return innerPaddingRight;
    }

    private void initFloatingLabel() {
        // observe the text changing
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (floatingLabelEnabled) {
                    if (s.length() == 0) {
                        if (floatingLabelShown) {
                            floatingLabelShown = false;
                            getLabelAnimator().reverse();
                        }
                    } else if (!floatingLabelShown) {
                        floatingLabelShown = true;
                        getLabelAnimator().start();
                    }
                }
            }
        });
        // observe the focus state to animate the floating label's text color appropriately
        innerFocusChangeListener = new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (floatingLabelEnabled && highlightFloatingLabel) {
                    if (hasFocus) {
                        getLabelFocusAnimator().start();
                    } else {
                        getLabelFocusAnimator().reverse();
                    }
                }
                if (validateOnFocusLost && !hasFocus) {
                    validate();
                }
                if (outerFocusChangeListener != null) {
                    outerFocusChangeListener.onFocusChange(v, hasFocus);
                }
            }
        };
        super.setOnFocusChangeListener(innerFocusChangeListener);
    }

    public boolean isValidateOnFocusLost() {
        return validateOnFocusLost;
    }

    public MaterialEditText setValidateOnFocusLost(boolean validate) {
        this.validateOnFocusLost = validate;
        return this;
    }

    public MaterialEditText setBaseColor(int color) {
        if (baseColor != color) {
            baseColor = color;
        }
        initText();
        postInvalidate();
        return this;
    }

    public MaterialEditText setPrimaryColor(int color) {
        primaryColor = color;
        postInvalidate();
        return this;
    }

    /**
     * Same function as {@link #setTextColor(int)}. (Directly overriding the built-in one could cause some error, so use this method instead.)
     */
    public MaterialEditText setMetTextColor(int color) {
        textColorStateList = ColorStateList.valueOf(color);
        resetTextColor();
        return this;
    }

    /**
     * Same function as {@link #setTextColor(ColorStateList)}. (Directly overriding the built-in one could cause some error, so use this method instead.)
     */
    public MaterialEditText setMetTextColor(ColorStateList colors) {
        textColorStateList = colors;
        resetTextColor();
        return this;
    }

    private void resetTextColor() {
        if (textColorStateList == null) {
            textColorStateList = new ColorStateList(new int[][]{new int[]{android.R.attr.state_enabled}, EMPTY_STATE_SET}, new int[]{baseColor & 0x00ffffff | 0xdf000000, baseColor & 0x00ffffff | 0x44000000});
            setTextColor(textColorStateList);
        } else {
            setTextColor(textColorStateList);
        }
    }

    /**
     * Same function as {@link #setHintTextColor(int)}. (The built-in one is a final method that can't be overridden, so use this method instead.)
     */
    public MaterialEditText setMetHintTextColor(int color) {
        textColorHintStateList = ColorStateList.valueOf(color);
        resetHintTextColor();
        return this;
    }

    /**
     * Same function as {@link #setHintTextColor(ColorStateList)}. (The built-in one is a final method that can't be overridden, so use this method instead.)
     */
    public MaterialEditText setMetHintTextColor(ColorStateList colors) {
        textColorHintStateList = colors;
        resetHintTextColor();
        return this;
    }

    private void resetHintTextColor() {
        if (textColorHintStateList == null) {
            setHintTextColor(baseColor & 0x00ffffff | 0x44000000);
        } else {
            setHintTextColor(textColorHintStateList);
        }
    }

    private void setFloatingLabelInternal(int mode) {
        switch (mode) {
            case FLOATING_LABEL_NORMAL:
                floatingLabelEnabled = true;
                highlightFloatingLabel = false;
                break;
            case FLOATING_LABEL_HIGHLIGHT:
                floatingLabelEnabled = true;
                highlightFloatingLabel = true;
                break;
            default:
                floatingLabelEnabled = false;
                highlightFloatingLabel = false;
                break;
        }
    }

    public MaterialEditText setFloatingLabel(@FloatingLabelType int mode) {
        setFloatingLabelInternal(mode);
        initPadding();
        return this;
    }

    public int getFloatingLabelPadding() {
        return floatingLabelPadding;
    }

    public MaterialEditText setFloatingLabelPadding(int padding) {
        floatingLabelPadding = padding;
        postInvalidate();
        return this;
    }

    public boolean isFloatingLabelAnimating() {
        return floatingLabelAnimating;
    }

    public MaterialEditText setFloatingLabelAnimating(boolean animating) {
        floatingLabelAnimating = animating;
        return this;
    }

    public MaterialEditText setSingleLineEllipsis() {
        return setSingleLineEllipsis(true);
    }

    public MaterialEditText setSingleLineEllipsis(boolean enabled) {
        singleLineEllipsis = enabled;
        initMinBottomLines();
        initPadding();
        postInvalidate();
        return this;
    }

    public int getMaxCharacters() {
        return maxCharacters;
    }

    public MaterialEditText setMaxCharacters(int max) {
        maxCharacters = max;
        initMinBottomLines();
        initPadding();
        postInvalidate();
        return this;
    }

    public int getMinCharacters() {
        return minCharacters;
    }

    public MaterialEditText setMinCharacters(int min) {
        minCharacters = min;
        initMinBottomLines();
        initPadding();
        postInvalidate();
        return this;
    }

    public int getMinBottomTextLines() {
        return minBottomTextLines;
    }

    public MaterialEditText setMinBottomTextLines(int lines) {
        minBottomTextLines = lines;
        initMinBottomLines();
        initPadding();
        postInvalidate();
        return this;
    }

    public boolean isAutoValidate() {
        return autoValidate;
    }

    public MaterialEditText setAutoValidate(boolean autoValidate) {
        this.autoValidate = autoValidate;
        if (autoValidate) {
            validate();
        }
        return this;
    }

    public int getErrorColor() {
        return errorColor;
    }

    public MaterialEditText setErrorColor(int color) {
        errorColor = color;
        postInvalidate();
        return this;
    }

    public MaterialEditText setHelperText(CharSequence helperText) {
        this.helperText = helperText == null ? null : helperText.toString();
        if (adjustBottomLines()) {
            postInvalidate();
        }
        return this;
    }

    public String getHelperText() {
        return helperText;
    }

    public int getHelperTextColor() {
        return helperTextColor;
    }

    public MaterialEditText setHelperTextColor(int color) {
        helperTextColor = color;
        postInvalidate();
        return this;
    }

    /**
     * 设置输入框是否允许为空
     *
     * @param allowEmpty
     * @param errorEmpty
     * @return
     */
    public MaterialEditText setAllowEmpty(boolean allowEmpty, String errorEmpty) {
        boolean updateError = false;
        Iterator<METValidator> it = validators.iterator();
        while (it.hasNext()) {
            METValidator item = it.next();
            if (item instanceof NotAllowEmptyValidator) {
                if (allowEmpty) {
                    it.remove();
                } else {
                    if (!TextUtils.isEmpty(errorEmpty)) {
                        item.setErrorMessage(errorEmpty);
                    } else {
                        item.setErrorMessage(ResUtils.getString(R.string.xui_met_not_allow_empty));
                    }
                    updateError = true;
                }
                break;
            }
        }
        if (!allowEmpty && !updateError) {
            if (!TextUtils.isEmpty(errorEmpty)) {
                validators.add(new NotAllowEmptyValidator(errorEmpty));
            } else {
                validators.add(new NotAllowEmptyValidator(ResUtils.getString(R.string.xui_met_not_allow_empty)));
            }
        }
        return this;
    }

    @Override
    public void setError(CharSequence errorText) {
        tempErrorText = errorText == null ? null : errorText.toString();
        if (adjustBottomLines()) {
            postInvalidate();
        }
    }

    @Override
    public CharSequence getError() {
        return tempErrorText;
    }

    /**
     * only used to draw the bottom line
     */
    private boolean isInternalValid() {
        return tempErrorText == null && isCharactersCountValid();
    }

    /**
     * if the main text matches the regex
     *
     * @deprecated use the new validator interface to add your own custom validator
     */
    @Deprecated
    public boolean isValid(String regex) {
        if (regex == null) {
            return false;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(getText());
        return matcher.matches();
    }

    /**
     * check if the main text matches the regex, and set the error text if not.
     *
     * @return true if it matches the regex, false if not.
     * @deprecated use the new validator interface to add your own custom validator
     */
    @Deprecated
    public boolean validate(String regex, CharSequence errorText) {
        boolean isValid = isValid(regex);
        if (!isValid) {
            setError(errorText);
        }
        postInvalidate();
        return isValid;
    }

    /**
     * Run validation on a single validator instance
     *
     * @param validator Validator to check
     * @return True if valid, false if not
     */
    public boolean validateWith(@NonNull METValidator validator) {
        CharSequence text = getText();
        boolean isValid = validator.isValid(text, text.length() == 0);
        if (!isValid) {
            setError(validator.getErrorMessage());
        }
        postInvalidate();
        return isValid;
    }

    /**
     * Check all validators, sets the error text if not
     * <p/>
     * NOTE: this stops at the first validator to report invalid.
     *
     * @return True if all validators pass, false if not
     */
    public boolean validate() {
        if (validators == null || validators.isEmpty()) {
            return true;
        }

        CharSequence text = getText();
        boolean isEmpty = text.length() == 0;

        boolean isValid = true;
        for (METValidator validator : validators) {
            //noinspection ConstantConditions
            isValid = isValid && validator.isValid(text, isEmpty);
            if (!isValid) {
                setError(validator.getErrorMessage());
                break;
            }
        }
        if (isValid) {
            setError(null);
        }

        postInvalidate();
        return isValid;
    }

    public boolean hasValidators() {
        return this.validators != null && !this.validators.isEmpty();
    }

    /**
     * Adds a new validator to the View's list of validators
     * <p/>
     * This will be checked with the others in {@link #validate()}
     *
     * @param validator Validator to add
     * @return This instance, for easy chaining
     */
    public MaterialEditText addValidator(METValidator validator) {
        if (validators == null) {
            this.validators = new ArrayList<>();
        }
        this.validators.add(validator);
        return this;
    }

    public MaterialEditText clearValidators() {
        if (this.validators != null) {
            this.validators.clear();
        }
        return this;
    }

    @Nullable
    public List<METValidator> getValidators() {
        return this.validators;
    }

    public MaterialEditText setLengthChecker(METLengthChecker lengthChecker) {
        this.lengthChecker = lengthChecker;
        return this;
    }

    /**
     * 清除内容
     */
    public void clear() {
        if (!TextUtils.isEmpty(getText())) {
            setText(null);
        }
    }

    /**
     * 获取输入的内容
     *
     * @return
     */
    public String getEditValue() {
        return getEditableText().toString().trim();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setFocusable(enabled);
        super.setFocusableInTouchMode(enabled);
        super.setEnabled(enabled);
    }

    /**
     * 输入的内容是否为空
     *
     * @return
     */
    public boolean isEmpty() {
        return TextUtils.isEmpty(getEditValue());
    }

    /**
     * 输入的内容是否不为空
     *
     * @return
     */
    public boolean isNotEmpty() {
        return !TextUtils.isEmpty(getEditValue());
    }

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener listener) {
        if (innerFocusChangeListener == null) {
            super.setOnFocusChangeListener(listener);
        } else {
            outerFocusChangeListener = listener;
        }
    }

    private ObjectAnimator getLabelAnimator() {
        if (labelAnimator == null) {
            labelAnimator = ObjectAnimator.ofFloat(this, "floatingLabelFraction", 0f, 1f);
        }
        labelAnimator.setDuration(floatingLabelAnimating ? 300 : 0);
        return labelAnimator;
    }

    private ObjectAnimator getLabelFocusAnimator() {
        if (labelFocusAnimator == null) {
            labelFocusAnimator = ObjectAnimator.ofFloat(this, "focusFraction", 0f, 1f);
        }
        return labelFocusAnimator;
    }

    private ObjectAnimator getBottomLinesAnimator(float destBottomLines) {
        if (bottomLinesAnimator == null) {
            bottomLinesAnimator = ObjectAnimator.ofFloat(this, "currentBottomLines", destBottomLines);
        } else {
            bottomLinesAnimator.cancel();
            bottomLinesAnimator.setFloatValues(destBottomLines);
        }
        return bottomLinesAnimator;
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {

        int startX = getScrollX() + (iconLeftBitmaps == null ? 0 : (iconOuterWidth + iconPadding));
        int endX = getScrollX() + (iconRightBitmaps == null ? getWidth() : getWidth() - iconOuterWidth - iconPadding) - getPaddingRight();
        int lineStartY = getScrollY() + getHeight() - getPaddingBottom();

        // draw the icon(s)
        drawIcons(canvas, startX, endX, lineStartY);

        // draw the action button
        drawActionButton(canvas, startX, endX, lineStartY);

        // draw the underline
        lineStartY = drawUnderline(canvas, startX, endX, lineStartY);

        textPaint.setTextSize(bottomTextSize);
        Paint.FontMetrics textMetrics = textPaint.getFontMetrics();
        float relativeHeight = -textMetrics.ascent - textMetrics.descent;
        float bottomTextPadding = bottomTextSize + textMetrics.ascent + textMetrics.descent;

        // draw the characters counter
        if ((hasFocus() && hasCharactersCounter()) || !isCharactersCountValid()) {
            textPaint.setColor(isCharactersCountValid() ? (baseColor & 0x00ffffff | 0x44000000) : errorColor);
            String charactersCounterText = getCharactersCounterText();
            canvas.drawText(charactersCounterText, isRTL() ? startX : endX - textPaint.measureText(charactersCounterText), lineStartY + bottomSpacing + relativeHeight, textPaint);
        }

        // draw the bottom text
        drawBottomText(canvas, startX, endX, lineStartY, bottomTextPadding);

        // draw the floating label
        drawFloatingLabel(canvas, startX, endX);

        // draw the bottom ellipsis
        drawBottonEllipsis(canvas, startX, endX, lineStartY);

        // draw the original things
        super.onDraw(canvas);
    }

    /**
     * 画图标
     *
     * @param canvas
     * @param startX
     * @param endX
     * @param lineStartY
     */
    private void drawIcons(@NonNull Canvas canvas, int startX, int endX, int lineStartY) {
        paint.setAlpha(255);
        if (iconLeftBitmaps != null) {
            Bitmap icon = iconLeftBitmaps[!isInternalValid() ? 3 : !isEnabled() ? 2 : hasFocus() ? 1 : 0];
            int iconLeft = startX - iconPadding - iconOuterWidth + (iconOuterWidth - icon.getWidth()) / 2;
            int iconTop = lineStartY + bottomSpacing - iconOuterHeight + (iconOuterHeight - icon.getHeight()) / 2;
            canvas.drawBitmap(icon, iconLeft, iconTop, paint);
        }
        if (iconRightBitmaps != null) {
            Bitmap icon = iconRightBitmaps[!isInternalValid() ? 3 : !isEnabled() ? 2 : hasFocus() ? 1 : 0];
            int iconRight = endX + iconPadding + (iconOuterWidth - icon.getWidth()) / 2;
            int iconTop = lineStartY + bottomSpacing - iconOuterHeight + (iconOuterHeight - icon.getHeight()) / 2;
            canvas.drawBitmap(icon, iconRight, iconTop, paint);
        }
    }

    /**
     * 画清除按钮
     *
     * @param canvas
     * @param startX
     * @param endX
     * @param lineStartY
     */
    private void drawActionButton(@NonNull Canvas canvas, int startX, int endX, int lineStartY) {
        if (hasFocus() && isEnabled() && !TextUtils.isEmpty(getText()) && (showClearButton || showPasswordButton)) {
            paint.setAlpha(255);
            int buttonLeft = isRTL() ? startX : (endX - iconOuterWidth);
            Bitmap actionButtonBitmap;
            if (showClearButton) {
                actionButtonBitmap = clearButtonBitmaps[0];
            } else {
                actionButtonBitmap = passwordVisible ? showPwIconBitmaps[0] : hidePwIconBitmaps[0];
            }
            buttonLeft += (iconOuterWidth - actionButtonBitmap.getWidth()) / 2;
            int iconTop = lineStartY + bottomSpacing - iconOuterHeight + (iconOuterHeight - actionButtonBitmap.getHeight()) / 2;
            canvas.drawBitmap(actionButtonBitmap, buttonLeft, iconTop, paint);
        }
    }

    /**
     * 画下划线
     *
     * @param canvas
     * @param startX
     * @param endX
     * @param lineStartY
     * @return
     */
    private int drawUnderline(@NonNull Canvas canvas, int startX, int endX, int lineStartY) {
        if (!hideUnderline) {
            lineStartY += bottomSpacing;
            if (!isInternalValid()) { // not valid
                paint.setColor(errorColor);
                canvas.drawRect(startX, lineStartY, endX, lineStartY + getPixel(2), paint);
            } else if (!isEnabled()) { // disabled
                paint.setColor(underlineColor != -1 ? underlineColor : baseColor & 0x00ffffff | 0x44000000);
                float interval = getPixel(1);
                for (float xOffset = 0; xOffset < getWidth(); xOffset += interval * 3) {
                    canvas.drawRect(startX + xOffset, lineStartY, startX + xOffset + interval, lineStartY + getPixel(1), paint);
                }
            } else if (hasFocus()) { // focused
                paint.setColor(primaryColor);
                canvas.drawRect(startX, lineStartY, endX, lineStartY + getPixel(2), paint);
            } else { // normal
                paint.setColor(underlineColor != -1 ? underlineColor : baseColor & 0x00ffffff | 0x1E000000);
                canvas.drawRect(startX, lineStartY, endX, lineStartY + getPixel(1), paint);
            }
        }
        return lineStartY;
    }

    /**
     * 画底部文字
     *
     * @param canvas
     * @param startX
     * @param endX
     * @param lineStartY
     * @param bottomTextPadding
     */
    private void drawBottomText(@NonNull Canvas canvas, int startX, int endX, int lineStartY, float bottomTextPadding) {
        if (textLayout != null) {
            if (tempErrorText != null || ((helperTextAlwaysShown || hasFocus()) && !TextUtils.isEmpty(helperText))) { // error text or helper text
                textPaint.setColor(tempErrorText != null ? errorColor : helperTextColor != -1 ? helperTextColor : (baseColor & 0x00ffffff | 0x44000000));
                canvas.save();
                if (isRTL()) {
                    canvas.translate(endX - textLayout.getWidth(), lineStartY + bottomSpacing - bottomTextPadding);
                } else {
                    canvas.translate(startX + getBottomTextLeftOffset(), lineStartY + bottomSpacing - bottomTextPadding);
                }
                textLayout.draw(canvas);
                canvas.restore();
            }
        }
    }

    private void drawFloatingLabel(@NonNull Canvas canvas, int startX, int endX) {
        if (floatingLabelEnabled && !TextUtils.isEmpty(floatingLabelText)) {
            textPaint.setTextSize(floatingLabelTextSize);
            // calculate the text color
            textPaint.setColor((Integer) focusEvaluator.evaluate(focusFraction * (isEnabled() ? 1 : 0), floatingLabelTextColor != -1 ? floatingLabelTextColor : (baseColor & 0x00ffffff | 0x44000000), primaryColor));

            // calculate the horizontal position
            float floatingLabelWidth = textPaint.measureText(floatingLabelText.toString());
            int floatingLabelStartX;
            if ((getGravity() & Gravity.RIGHT) == Gravity.RIGHT || isRTL()) {
                floatingLabelStartX = (int) (endX - floatingLabelWidth);
            } else if ((getGravity() & Gravity.LEFT) == Gravity.LEFT) {
                floatingLabelStartX = startX;
            } else {
                floatingLabelStartX = startX + (int) (getInnerPaddingLeft() + (getWidth() - getInnerPaddingLeft() - getInnerPaddingRight() - floatingLabelWidth) / 2);
            }

            // calculate the vertical position
            int distance = floatingLabelPadding;
            int floatingLabelStartY = (int) (innerPaddingTop + floatingLabelTextSize + floatingLabelPadding - distance * (floatingLabelAlwaysShown ? 1 : floatingLabelFraction) + getScrollY());

            // calculate the alpha
            int alpha = ((int) ((floatingLabelAlwaysShown ? 1 : floatingLabelFraction) * 0xff * (0.74f * focusFraction * (isEnabled() ? 1 : 0) + 0.26f) * (floatingLabelTextColor != -1 ? 1 : Color.alpha(floatingLabelTextColor) / 255F)));
            textPaint.setAlpha(alpha);

            // draw the floating label
            canvas.drawText(floatingLabelText.toString(), floatingLabelStartX, floatingLabelStartY, textPaint);
        }
    }

    private void drawBottonEllipsis(@NonNull Canvas canvas, int startX, int endX, int lineStartY) {
        if (hasFocus() && singleLineEllipsis && getScrollX() != 0) {
            paint.setColor(isInternalValid() ? primaryColor : errorColor);
            float startY = lineStartY + bottomSpacing;
            int ellipsisStartX;
            if (isRTL()) {
                ellipsisStartX = endX;
            } else {
                ellipsisStartX = startX;
            }
            int signum = isRTL() ? -1 : 1;
            canvas.drawCircle(ellipsisStartX + signum * bottomEllipsisSize / 2, startY + bottomEllipsisSize / 2, bottomEllipsisSize / 2, paint);
            canvas.drawCircle(ellipsisStartX + signum * bottomEllipsisSize * 5 / 2, startY + bottomEllipsisSize / 2, bottomEllipsisSize / 2, paint);
            canvas.drawCircle(ellipsisStartX + signum * bottomEllipsisSize * 9 / 2, startY + bottomEllipsisSize / 2, bottomEllipsisSize / 2, paint);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private boolean isRTL() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return false;
        }
        Configuration config = getResources().getConfiguration();
        return config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
    }

    private int getBottomTextLeftOffset() {
        return isRTL() ? getCharactersCounterWidth() : getBottomEllipsisWidth();
    }

    private int getBottomTextRightOffset() {
        return isRTL() ? getBottomEllipsisWidth() : getCharactersCounterWidth();
    }

    private int getCharactersCounterWidth() {
        return hasCharactersCounter() ? (int) textPaint.measureText(getCharactersCounterText()) : 0;
    }

    private int getBottomEllipsisWidth() {
        return singleLineEllipsis ? (bottomEllipsisSize * 5 + getPixel(4)) : 0;
    }

    private void checkCharactersCount() {
        if ((!firstShown && !checkCharactersCountAtBeginning) || !hasCharactersCounter()) {
            charactersCountValid = true;
        } else {
            CharSequence text = getText();
            int count = text == null ? 0 : checkLength(text);
            charactersCountValid = (count >= minCharacters && (maxCharacters <= 0 || count <= maxCharacters));
        }
    }

    public boolean isCharactersCountValid() {
        return charactersCountValid;
    }

    private boolean hasCharactersCounter() {
        return minCharacters > 0 || maxCharacters > 0;
    }

    private String getCharactersCounterText() {
        String text;
        if (minCharacters <= 0) {
            text = isRTL() ? maxCharacters + " / " + checkLength(getText()) : checkLength(getText()) + " / " + maxCharacters;
        } else if (maxCharacters <= 0) {
            text = isRTL() ? "+" + minCharacters + " / " + checkLength(getText()) : checkLength(getText()) + " / " + minCharacters + "+";
        } else {
            text = isRTL() ? maxCharacters + "-" + minCharacters + " / " + checkLength(getText()) : checkLength(getText()) + " / " + minCharacters + "-" + maxCharacters;
        }
        return text;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (singleLineEllipsis && getScrollX() > 0 && event.getAction() == MotionEvent.ACTION_DOWN && event.getX() < getPixel(4 * 5) && event.getY() > getHeight() - extraPaddingBottom - innerPaddingBottom && event.getY() < getHeight() - innerPaddingBottom) {
            setSelection(0);
            return false;
        }
        if (hasFocus() && (showClearButton || showPasswordButton) && isEnabled()) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (insideActionButton(event)) {
                        actionButtonTouched = true;
                        actionButtonClicking = true;
                        return true;
                    }
                case MotionEvent.ACTION_MOVE:
                    if (actionButtonClicking && !insideActionButton(event)) {
                        actionButtonClicking = false;
                    }
                    if (actionButtonTouched) {
                        return true;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (actionButtonClicking) {
                        if (showClearButton) {
                            if (!TextUtils.isEmpty(getText())) {
                                setText(null);
                            }
                        } else {
                            togglePasswordIconVisibility();
                        }
                        actionButtonClicking = false;
                    }
                    if (actionButtonTouched) {
                        actionButtonTouched = false;
                        return true;
                    }
                    actionButtonTouched = false;
                    break;
                case MotionEvent.ACTION_CANCEL:
                    actionButtonTouched = false;
                    actionButtonClicking = false;
                    break;
            }
        }
        return super.onTouchEvent(event);
    }

    private boolean insideActionButton(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        int startX = getScrollX() + (iconLeftBitmaps == null ? 0 : (iconOuterWidth + iconPadding));
        int endX = getScrollX() + (iconRightBitmaps == null ? getWidth() : getWidth() - iconOuterWidth - iconPadding);
        int buttonLeft;
        if (isRTL()) {
            buttonLeft = startX;
        } else {
            buttonLeft = endX - iconOuterWidth;
        }
        int buttonTop = getScrollY() + getHeight() - getPaddingBottom() + bottomSpacing - iconOuterHeight;
        return (x >= buttonLeft - iconOuterWidth && x < buttonLeft + iconOuterWidth && y >= buttonTop && y < buttonTop + iconOuterHeight);
    }

    /**
     * 密码显示切换
     */
    private void handleSwitchPasswordInputVisibility() {
        int selectionStart = getSelectionStart();
        int selectionEnd = getSelectionEnd();
        if (passwordVisible) {
            setTransformationMethod(null);
        } else {
            setTransformationMethod(PasswordTransformationMethod.getInstance());

        }
        setSelection(selectionStart, selectionEnd);
    }

    /**
     * 密码显示切换
     */
    private void togglePasswordIconVisibility() {
        passwordVisible = !passwordVisible;
        handleSwitchPasswordInputVisibility();
    }

    private int checkLength(CharSequence text) {
        if (lengthChecker == null) {
            return text.length();
        }
        return lengthChecker.getLength(text);
    }

    @Override
    public void setTypeface(Typeface typeface) {
        super.setTypeface(typeface);
        if (textPaint != null) {
            textPaint.setTypeface(typeface);
        }
    }
}