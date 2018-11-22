package com.xuexiang.xui.widget.button.roundbutton;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

/**
 * 使按钮能方便地指定圆角、边框颜色、边框粗细、背景色
 * <p>
 * 注意: 因为该控件的圆角采用 View 的 background 实现, 所以与原生的 <code>android:background</code> 有冲突。
 * <ul>
 * <li>如果在 xml 中用 <code>android:background</code> 指定 background, 该 background 不会生效。</li>
 * <li>如果在该 View 构造完后用 {@link #setBackgroundResource(int)} 等方法设置背景, 该背景将覆盖圆角效果。</li>
 * </ul>
 * </p>
 * <p>
 * 如需在 xml 中指定圆角、边框颜色、边框粗细、背景色等值,采用 xml 属性
 * </p>
 * <p>
 * 如需在 Java 中指定以上属性, 需要通过 {@link #getBackground()} 获取 {@link RoundDrawable} 对象,
 * 然后使用 {@link RoundDrawable} 提供的方法进行设置。
 * </p>
 * <p>
 * @see RoundDrawable
 * </p>
 */
public class RoundButton extends AppCompatButton {

    public RoundButton(Context context) {
        super(context);
        init(context, null);
    }

    public RoundButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RoundButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        RoundDrawable bg = RoundDrawable.fromAttributeSet(context, attrs);
        setBackgroundDrawable(bg);
    }
}
