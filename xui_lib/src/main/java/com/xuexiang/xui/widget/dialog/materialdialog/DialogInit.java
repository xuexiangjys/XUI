/*
 * Copyright (C) 2018 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.xuexiang.xui.widget.dialog.materialdialog;

import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.InputType;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.annotation.UiThread;

import com.xuexiang.xui.R;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.ThemeUtils;
import com.xuexiang.xui.widget.XUIKeyboardScrollView;
import com.xuexiang.xui.widget.dialog.materialdialog.internal.MDAdapter;
import com.xuexiang.xui.widget.dialog.materialdialog.internal.MDButton;
import com.xuexiang.xui.widget.dialog.materialdialog.internal.MDRootLayout;
import com.xuexiang.xui.widget.dialog.materialdialog.internal.MDTintHelper;
import com.xuexiang.xui.widget.progress.materialprogressbar.HorizontalProgressDrawable;
import com.xuexiang.xui.widget.progress.materialprogressbar.IndeterminateCircularProgressDrawable;
import com.xuexiang.xui.widget.progress.materialprogressbar.IndeterminateHorizontalProgressDrawable;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Used by MaterialDialog while initializing the dialog. Offloads some of the code to make the main
 * class cleaner and easier to read/maintain.
 *
 * @author Aidan Follestad (afollestad)
 */
class DialogInit {

    @StyleRes
    static int getTheme(@NonNull MaterialDialog.Builder builder) {
        if (builder.customTheme != -1) {
            return builder.customTheme;
        } else {
            boolean darkTheme = ThemeUtils.resolveBoolean(builder.context, R.attr.md_dark_theme, builder.theme == Theme.DARK);
            builder.theme = darkTheme ? Theme.DARK : Theme.LIGHT;
            return darkTheme ? R.style.MD_Dark : R.style.MD_Light;
        }
    }

    @LayoutRes
    static int getInflateLayout(MaterialDialog.Builder builder) {
        if (builder.customView != null) {
            return R.layout.xmd_layout_dialog_custom;
        } else if (builder.items != null || builder.adapter != null) {
            if (builder.checkBoxPrompt != null) {
                return R.layout.xmd_layout_dialog_list_check;
            }
            return R.layout.xmd_layout_dialog_list;
        } else if (builder.progress > -2) {
            return R.layout.xmd_layout_dialog_progress;
        } else if (builder.indeterminateProgress) {
            if (builder.indeterminateIsHorizontalProgress) {
                return R.layout.xmd_layout_dialog_progress_indeterminate_horizontal;
            }
            return R.layout.xmd_layout_dialog_progress_indeterminate;
        } else if (builder.inputCallback != null) {
            if (builder.checkBoxPrompt != null) {
                return R.layout.xmd_layout_dialog_input_check;
            }
            return R.layout.xmd_layout_dialog_input;
        } else if (builder.checkBoxPrompt != null) {
            return R.layout.xmd_layout_dialog_basic_check;
        } else {
            return R.layout.xmd_layout_dialog_basic;
        }
    }

    @SuppressWarnings("ConstantConditions")
    @UiThread
    public static void init(final MaterialDialog dialog) {
        final MaterialDialog.Builder builder = dialog.builder;

        // Set cancelable flag and dialog background color
        dialog.setCancelable(builder.cancelable);
        dialog.setCanceledOnTouchOutside(builder.canceledOnTouchOutside);
        if (builder.backgroundColor == 0) {
            builder.backgroundColor =
                    ThemeUtils.resolveColor(
                            builder.context,
                            R.attr.md_background_color,
                            ThemeUtils.resolveColor(dialog.getContext(), R.attr.colorBackgroundFloating));
        }
        if (builder.backgroundColor != 0) {
            GradientDrawable drawable = new GradientDrawable();
            drawable.setCornerRadius(
                    builder.context.getResources().getDimension(R.dimen.md_bg_corner_radius));
            drawable.setColor(builder.backgroundColor);
            dialog.getWindow().setBackgroundDrawable(drawable);
        }

        // Retrieve color theme attributes
        if (!builder.positiveColorSet) {
            builder.positiveColor =
                    ThemeUtils.resolveActionTextColorStateList(
                            builder.context, R.attr.md_positive_color, builder.positiveColor);
        }
        if (!builder.neutralColorSet) {
            builder.neutralColor =
                    ThemeUtils.resolveActionTextColorStateList(
                            builder.context, R.attr.md_neutral_color, builder.neutralColor);
        }
        if (!builder.negativeColorSet) {
            builder.negativeColor =
                    ThemeUtils.resolveActionTextColorStateList(
                            builder.context, R.attr.md_negative_color, builder.negativeColor);
        }
        if (!builder.widgetColorSet) {
            builder.widgetColor =
                    ThemeUtils.resolveColor(builder.context, R.attr.md_widget_color, builder.widgetColor);
        }

        // Retrieve default title/content colors
        if (!builder.titleColorSet) {
            final int titleColorFallback =
                    ThemeUtils.resolveColor(dialog.getContext(), android.R.attr.textColorPrimary);
            builder.titleColor =
                    ThemeUtils.resolveColor(builder.context, R.attr.md_title_color, titleColorFallback);
        }
        if (!builder.contentColorSet) {
            final int contentColorFallback =
                    ThemeUtils.resolveColor(dialog.getContext(), android.R.attr.textColorSecondary);
            builder.contentColor =
                    ThemeUtils.resolveColor(builder.context, R.attr.md_content_color, contentColorFallback);
        }
        if (!builder.itemColorSet) {
            builder.itemColor =
                    ThemeUtils.resolveColor(builder.context, R.attr.md_item_color, builder.contentColor);
        }

        // Retrieve references to views
        dialog.title = dialog.view.findViewById(R.id.md_title);
        dialog.icon = dialog.view.findViewById(R.id.md_icon);
        dialog.titleFrame = dialog.view.findViewById(R.id.md_titleFrame);
        dialog.content = dialog.view.findViewById(R.id.md_content);
        dialog.recyclerView = dialog.view.findViewById(R.id.md_contentRecyclerView);
        dialog.checkBoxPrompt = dialog.view.findViewById(R.id.md_promptCheckbox);

        // Button views initially used by checkIfStackingNeeded()
        dialog.positiveButton = dialog.view.findViewById(R.id.md_buttonDefaultPositive);
        dialog.neutralButton = dialog.view.findViewById(R.id.md_buttonDefaultNeutral);
        dialog.negativeButton = dialog.view.findViewById(R.id.md_buttonDefaultNegative);

        // Don't allow the submit button to not be shown for input dialogs
        if (builder.inputCallback != null && builder.positiveText == null) {
            builder.positiveText = builder.context.getText(android.R.string.ok);
        }

        // Set up the initial visibility of action buttons based on whether or not text was set
        dialog.positiveButton.setVisibility(builder.positiveText != null ? View.VISIBLE : View.GONE);
        dialog.neutralButton.setVisibility(builder.neutralText != null ? View.VISIBLE : View.GONE);
        dialog.negativeButton.setVisibility(builder.negativeText != null ? View.VISIBLE : View.GONE);

        // Set up the focus of action buttons
        dialog.positiveButton.setFocusable(true);
        dialog.neutralButton.setFocusable(true);
        dialog.negativeButton.setFocusable(true);
        if (builder.positiveFocus) {
            dialog.positiveButton.requestFocus();
        }
        if (builder.neutralFocus) {
            dialog.neutralButton.requestFocus();
        }
        if (builder.negativeFocus) {
            dialog.negativeButton.requestFocus();
        }

        // Setup icon
        if (builder.icon != null) {
            dialog.icon.setVisibility(View.VISIBLE);
            dialog.icon.setImageDrawable(builder.icon);
        } else {
            Drawable d = ThemeUtils.resolveDrawable(builder.context, R.attr.md_icon);
            if (d != null) {
                dialog.icon.setVisibility(View.VISIBLE);
                dialog.icon.setImageDrawable(d);
            } else {
                dialog.icon.setVisibility(View.GONE);
            }
        }

        // Setup icon size limiting
        int maxIconSize = builder.maxIconSize;
        if (maxIconSize == -1) {
            maxIconSize = ThemeUtils.resolveDimension(builder.context, R.attr.md_icon_max_size);
        }
        if (builder.limitIconToDefaultSize
                || ThemeUtils.resolveBoolean(builder.context, R.attr.md_icon_limit_icon_to_default_size)) {
            maxIconSize = builder.context.getResources().getDimensionPixelSize(R.dimen.default_md_icon_max_size);
        }
        if (maxIconSize > -1) {
            dialog.icon.setAdjustViewBounds(true);
            dialog.icon.setMaxHeight(maxIconSize);
            dialog.icon.setMaxWidth(maxIconSize);
            dialog.icon.requestLayout();
        }

        // Setup divider color in case content scrolls
        if (!builder.dividerColorSet) {
            final int dividerFallback = ThemeUtils.resolveColor(dialog.getContext(), R.attr.md_divider);
            builder.dividerColor =
                    ThemeUtils.resolveColor(builder.context, R.attr.md_divider_color, dividerFallback);
        }
        dialog.view.setDividerColor(builder.dividerColor);

        // Setup title and title frame
        if (dialog.title != null) {
            dialog.setTypeface(dialog.title, builder.mediumFont);
            dialog.title.setTextColor(builder.titleColor);
            dialog.title.setGravity(builder.titleGravity.getGravityInt());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                //noinspection ResourceType
                dialog.title.setTextAlignment(builder.titleGravity.getTextAlignment());
            }

            if (builder.title == null) {
                dialog.titleFrame.setVisibility(View.GONE);
            } else {
                dialog.title.setText(builder.title);
                dialog.titleFrame.setVisibility(View.VISIBLE);
            }
        }

        // Setup content
        if (dialog.content != null) {
            dialog.content.setMovementMethod(new LinkMovementMethod());
            dialog.setTypeface(dialog.content, builder.regularFont);
            dialog.content.setLineSpacing(0f, builder.contentLineSpacingMultiplier);
            if (builder.linkColor == null) {
                dialog.content.setLinkTextColor(
                        ThemeUtils.resolveColor(dialog.getContext(), android.R.attr.textColorPrimary));
            } else {
                dialog.content.setLinkTextColor(builder.linkColor);
            }
            dialog.content.setTextColor(builder.contentColor);
            dialog.content.setGravity(builder.contentGravity.getGravityInt());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                //noinspection ResourceType
                dialog.content.setTextAlignment(builder.contentGravity.getTextAlignment());
            }

            if (builder.content != null) {
                dialog.content.setText(builder.content);
                dialog.content.setVisibility(View.VISIBLE);
            } else {
                dialog.content.setVisibility(View.GONE);
            }
        }

        // Setup prompt checkbox
        if (dialog.checkBoxPrompt != null) {
            dialog.checkBoxPrompt.setText(builder.checkBoxPrompt);
            dialog.checkBoxPrompt.setChecked(builder.checkBoxPromptInitiallyChecked);
            dialog.checkBoxPrompt.setOnCheckedChangeListener(builder.checkBoxPromptListener);
            dialog.setTypeface(dialog.checkBoxPrompt, builder.regularFont);
            dialog.checkBoxPrompt.setTextColor(builder.contentColor);
            MDTintHelper.setTint(dialog.checkBoxPrompt, builder.widgetColor);
        }

        // Setup action buttons
        dialog.view.setButtonGravity(builder.buttonsGravity);
        dialog.view.setButtonStackedGravity(builder.btnStackedGravity);
        dialog.view.setStackingBehavior(builder.stackingBehavior);
        boolean textAllCaps;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            textAllCaps = ThemeUtils.resolveBoolean(builder.context, android.R.attr.textAllCaps, true);
            if (textAllCaps) {
                textAllCaps = ThemeUtils.resolveBoolean(builder.context, R.attr.textAllCaps, true);
            }
        } else {
            textAllCaps = ThemeUtils.resolveBoolean(builder.context, R.attr.textAllCaps, true);
        }

        MDButton positiveTextView = dialog.positiveButton;
        dialog.setTypeface(positiveTextView, builder.mediumFont);
        positiveTextView.setAllCapsCompat(textAllCaps);
        positiveTextView.setText(builder.positiveText);
        positiveTextView.setTextColor(builder.positiveColor);
        dialog.positiveButton.setStackedSelector(dialog.getButtonSelector(DialogAction.POSITIVE, true));
        dialog.positiveButton.setDefaultSelector(
                dialog.getButtonSelector(DialogAction.POSITIVE, false));
        dialog.positiveButton.setTag(DialogAction.POSITIVE);
        dialog.positiveButton.setOnClickListener(dialog);
        dialog.positiveButton.setVisibility(View.VISIBLE);

        MDButton negativeTextView = dialog.negativeButton;
        dialog.setTypeface(negativeTextView, builder.mediumFont);
        negativeTextView.setAllCapsCompat(textAllCaps);
        negativeTextView.setText(builder.negativeText);
        negativeTextView.setTextColor(builder.negativeColor);
        dialog.negativeButton.setStackedSelector(dialog.getButtonSelector(DialogAction.NEGATIVE, true));
        dialog.negativeButton.setDefaultSelector(
                dialog.getButtonSelector(DialogAction.NEGATIVE, false));
        dialog.negativeButton.setTag(DialogAction.NEGATIVE);
        dialog.negativeButton.setOnClickListener(dialog);
        dialog.negativeButton.setVisibility(View.VISIBLE);

        MDButton neutralTextView = dialog.neutralButton;
        dialog.setTypeface(neutralTextView, builder.mediumFont);
        neutralTextView.setAllCapsCompat(textAllCaps);
        neutralTextView.setText(builder.neutralText);
        neutralTextView.setTextColor(builder.neutralColor);
        dialog.neutralButton.setStackedSelector(dialog.getButtonSelector(DialogAction.NEUTRAL, true));
        dialog.neutralButton.setDefaultSelector(dialog.getButtonSelector(DialogAction.NEUTRAL, false));
        dialog.neutralButton.setTag(DialogAction.NEUTRAL);
        dialog.neutralButton.setOnClickListener(dialog);
        dialog.neutralButton.setVisibility(View.VISIBLE);

        // Setup list dialog stuff
        if (builder.listCallbackMultiChoice != null) {
            dialog.selectedIndicesList = new ArrayList<>();
        }
        if (dialog.recyclerView != null) {
            if (builder.adapter == null) {
                // Determine list type
                if (builder.listCallbackSingleChoice != null) {
                    dialog.listType = MaterialDialog.ListType.SINGLE;
                } else if (builder.listCallbackMultiChoice != null) {
                    dialog.listType = MaterialDialog.ListType.MULTI;
                    if (builder.selectedIndices != null) {
                        dialog.selectedIndicesList = new ArrayList<>(Arrays.asList(builder.selectedIndices));
                        builder.selectedIndices = null;
                    }
                } else {
                    dialog.listType = MaterialDialog.ListType.REGULAR;
                }
                builder.adapter =
                        new DefaultRvAdapter(dialog, MaterialDialog.ListType.getLayoutForType(dialog.listType));
            } else if (builder.adapter instanceof MDAdapter) {
                // Notify simple list adapter of the dialog it belongs to
                ((MDAdapter) builder.adapter).setDialog(dialog);
            }
        }

        // Setup progress dialog stuff if needed
        setupProgressDialog(dialog);

        // Setup input dialog stuff if needed
        setupInputDialog(dialog);

        // Setup custom views
        if (builder.customView != null) {
            ((MDRootLayout) dialog.view.findViewById(R.id.md_root)).noTitleNoPadding();
            FrameLayout frame = dialog.view.findViewById(R.id.md_customViewFrame);
            dialog.customViewFrame = frame;
            View innerView = builder.customView;
            if (innerView.getParent() != null) {
                ((ViewGroup) innerView.getParent()).removeView(innerView);
            }
            if (builder.wrapCustomViewInScroll) {
        /* Apply the frame padding to the content, this allows the ScrollView to draw it's
        over scroll glow without clipping */
                final Resources r = dialog.getContext().getResources();
                final int framePadding = ThemeUtils.resolveDimension(dialog.getContext(), R.attr.md_dialog_frame_margin, R.dimen.default_md_dialog_frame_margin);
                final ScrollView sv = new XUIKeyboardScrollView(dialog.getContext());
                int paddingTop = r.getDimensionPixelSize(R.dimen.md_content_padding_top);
                int paddingBottom = r.getDimensionPixelSize(R.dimen.md_content_padding_bottom);
                sv.setClipToPadding(false);

                if (innerView instanceof EditText) {
                    // Setting padding to an EditText causes visual errors, set it to the parent instead
                    sv.setPadding(framePadding, paddingTop, framePadding, paddingBottom);
                } else {
                    // Setting padding to scroll view pushes the scroll bars out, don't do it if not necessary (like above)
                    sv.setPadding(0, paddingTop, 0, paddingBottom);
                    innerView.setPadding(framePadding, 0, framePadding, 0);
                }
                sv.addView(
                        innerView,
                        new ScrollView.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                innerView = sv;
            }
            frame.addView(
                    innerView,
                    new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        // Setup user listeners
        if (builder.showListener != null) {
            dialog.setOnShowListener(builder.showListener);
        }
        if (builder.cancelListener != null) {
            dialog.setOnCancelListener(builder.cancelListener);
        }
        if (builder.dismissListener != null) {
            dialog.setOnDismissListener(builder.dismissListener);
        }
        if (builder.keyListener != null) {
            dialog.setOnKeyListener(builder.keyListener);
        }

        // Setup internal listener
        dialog.setOnShowListenerInternal();

        // Other internal initialization
        dialog.invalidateList();
        dialog.setViewInternal(dialog.view);
        dialog.checkIfListInitScroll();

        // Min height and max width calculations
        WindowManager wm = dialog.getWindow().getWindowManager();
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int windowWidth = size.x;
        final int windowHeight = size.y;

        int windowVerticalPadding = ThemeUtils.resolveDimension(builder.context, R.attr.md_dialog_vertical_margin, ResUtils.getDimensionPixelSize(R.dimen.default_md_dialog_vertical_margin_phone));
        int windowHorizontalPadding = ThemeUtils.resolveDimension(builder.context, R.attr.md_dialog_horizontal_margin, ResUtils.getDimensionPixelSize(R.dimen.default_md_dialog_horizontal_margin_phone));

        int maxWidth = ThemeUtils.resolveDimension(builder.context, R.attr.md_dialog_max_width);

        final int calculatedWidth = windowWidth - (windowHorizontalPadding * 2);

        dialog.view.setMaxHeight(windowHeight - windowVerticalPadding * 2);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = Math.min(maxWidth, calculatedWidth);
        dialog.getWindow().setAttributes(lp);
    }

    private static void fixCanvasScalingWhenHardwareAccelerated(ProgressBar pb) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            // Canvas scaling when hardware accelerated results in artifacts on older API levels, so
            // we need to use software rendering
            if (pb.isHardwareAccelerated() && pb.getLayerType() != View.LAYER_TYPE_SOFTWARE) {
                pb.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
        }
    }

    private static void setupProgressDialog(final MaterialDialog dialog) {
        final MaterialDialog.Builder builder = dialog.builder;
        if (builder.indeterminateProgress || builder.progress > -2) {
            dialog.progressBar = dialog.view.findViewById(android.R.id.progress);
            if (dialog.progressBar == null) {
                return;
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                if (builder.indeterminateProgress) {
                    if (builder.indeterminateIsHorizontalProgress) {
                        IndeterminateHorizontalProgressDrawable d =
                                new IndeterminateHorizontalProgressDrawable(builder.getContext());
                        d.setTint(builder.widgetColor);
                        dialog.progressBar.setProgressDrawable(d);
                        dialog.progressBar.setIndeterminateDrawable(d);
                    } else {
                        IndeterminateCircularProgressDrawable d =
                                new IndeterminateCircularProgressDrawable(builder.getContext());
                        d.setTint(builder.widgetColor);
                        dialog.progressBar.setProgressDrawable(d);
                        dialog.progressBar.setIndeterminateDrawable(d);
                    }
                } else {
                    HorizontalProgressDrawable d = new HorizontalProgressDrawable(builder.getContext());
                    d.setTint(builder.widgetColor);
                    dialog.progressBar.setProgressDrawable(d);
                    dialog.progressBar.setIndeterminateDrawable(d);
                }
            } else {
                MDTintHelper.setTint(dialog.progressBar, builder.widgetColor);
            }

            if (!builder.indeterminateProgress || builder.indeterminateIsHorizontalProgress) {
                dialog.progressBar.setIndeterminate(
                        builder.indeterminateProgress && builder.indeterminateIsHorizontalProgress);
                dialog.progressBar.setProgress(0);
                dialog.progressBar.setMax(builder.progressMax);
                dialog.progressLabel = dialog.view.findViewById(R.id.md_label);
                if (dialog.progressLabel != null) {
//          dialog.progressLabel.setTextColor(builder.contentColor);
                    dialog.setTypeface(dialog.progressLabel, builder.mediumFont);
                    dialog.progressLabel.setText(builder.progressPercentFormat.format(0));
                }
                dialog.progressMinMax = dialog.view.findViewById(R.id.md_minMax);
                if (dialog.progressMinMax != null) {
//          dialog.progressMinMax.setTextColor(builder.contentColor);
                    dialog.setTypeface(dialog.progressMinMax, builder.regularFont);

                    if (builder.showMinMax) {
                        dialog.progressMinMax.setVisibility(View.VISIBLE);
                        dialog.progressMinMax.setText(
                                String.format(builder.progressNumberFormat, 0, builder.progressMax));
                        ViewGroup.MarginLayoutParams lp =
                                (ViewGroup.MarginLayoutParams) dialog.progressBar.getLayoutParams();
                        lp.leftMargin = 0;
                        lp.rightMargin = 0;
                    } else {
                        dialog.progressMinMax.setVisibility(View.GONE);
                    }
                } else {
                    builder.showMinMax = false;
                }
            }
        }

        if (dialog.progressBar != null) {
            fixCanvasScalingWhenHardwareAccelerated(dialog.progressBar);
        }
    }

    private static void setupInputDialog(final MaterialDialog dialog) {
        final MaterialDialog.Builder builder = dialog.builder;
        dialog.input = dialog.view.findViewById(android.R.id.input);
        if (dialog.input == null) {
            return;
        }
        dialog.setTypeface(dialog.input, builder.regularFont);
        if (builder.inputPrefill != null) {
            dialog.input.setText(builder.inputPrefill);
        }
        dialog.setInternalInputCallback();
        dialog.input.setHint(builder.inputHint);
        dialog.input.setSingleLine();
        dialog.input.setTextColor(builder.contentColor);
        dialog.input.setHintTextColor(ThemeUtils.adjustAlpha(builder.contentColor, 0.3f));
        MDTintHelper.setTint(dialog.input, dialog.builder.widgetColor);

        if (builder.inputType != -1) {
            dialog.input.setInputType(builder.inputType);
            if (builder.inputType != InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    && (builder.inputType & InputType.TYPE_TEXT_VARIATION_PASSWORD)
                    == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                // If the flags contain TYPE_TEXT_VARIATION_PASSWORD, apply the password transformation method automatically
                dialog.input.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }

        dialog.inputMinMax = dialog.view.findViewById(R.id.md_minMax);
        if (builder.inputMinLength > 0 || builder.inputMaxLength > -1) {
            dialog.invalidateInputMinMaxIndicator(
                    dialog.input.getText().toString().length(), !builder.inputAllowEmpty);
        } else {
            dialog.inputMinMax.setVisibility(View.GONE);
            dialog.inputMinMax = null;
        }
    }
}
