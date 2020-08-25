package com.xuexiang.xui.widget.textview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.xuexiang.xui.R;
import com.xuexiang.xui.widget.textview.marqueen.DisplayEntity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 滚动字幕
 */
public class MarqueeTextView extends AppCompatTextView {
    private final static int REDRAW_TEXT = 1;

    /**
     * 滚动条中心线的高度
     */
    private int mBaseline;
    /**
     * 是否停止滚动
     */
    private boolean mStopMarquee;
    /**
     * 显示文本集合
     */
    private List<DisplayEntity> mDisplayList = new ArrayList<>();
    /**
     * 当前显示集合的索引
     */
    private int mCurrentIndex = 0;

    /**
     * 正在显示的消息内容
     */
    private DisplayEntity mShowDisplayEntity;
    /**
     * 文本宽度
     */
    private float mDisplayTextWidth;

    /**
     * 当前滚动位置
     */
    private float mCurrentPosition;
    /**
     * 滚动区域宽度
     */
    private int mScrollWidth;
    /**
     * 滚动速度
     */
    private int mSpeed = 3;

    /**
     * 正在滚动
     */
    private boolean mIsRolling = false;
    /**
     * 布局尺寸是否自适应
     */
    private boolean mIsAutoFit;
    /**
     * 是否自动隐藏显示
     */
    private boolean mIsAutoDisplay;

    private OnMarqueeListener mOnMarqueeListener;

    private final Object mLock = new Object();

    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MarqueeTextView);
        mIsAutoFit = typedArray.getBoolean(R.styleable.MarqueeTextView_mtv_isAutoFit, false);
        mIsAutoDisplay = typedArray.getBoolean(R.styleable.MarqueeTextView_mtv_isAutoDisplay, false);

        if (mIsAutoDisplay) {
            setVisibility(GONE);
        }
        typedArray.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mIsAutoFit) {
            measureView();
        }
    }

    /**
     * 测量控件尺寸
     */
    public MarqueeTextView measureView() {
        mCurrentPosition = getWidth();
        mScrollWidth = getWidth();
        mBaseline = calculateBaseLine();
        return this;
    }

    /**
     * 开始滚动
     *
     * @param list 滚动内容的集合
     */
    public MarqueeTextView startRoll(List<DisplayEntity> list) {
        return setDisplayList(list).startRoll();
    }

    /**
     * 开始滚动
     *
     * @param list 滚动内容的集合
     */
    public MarqueeTextView startSimpleRoll(List<String> list) {
        return setDisplaySimpleList(list).startRoll();
    }

    /**
     * 设置需要显示的内容的集合
     *
     * @param list
     */
    public MarqueeTextView setDisplayList(List<DisplayEntity> list) {
        if (list != null && list.size() > 0) {
            mDisplayList.clear();
            mDisplayList.addAll(list);
        }
        return this;
    }

    /**
     * 设置简单的string显示集合
     *
     * @param list
     */
    public MarqueeTextView setDisplaySimpleList(List<String> list) {
        if (list != null && list.size() > 0) {
            mDisplayList.clear();
            for (String message : list) {
                if (!TextUtils.isEmpty(message)) {
                    mDisplayList.add(new DisplayEntity(message));
                }
            }
        }
        return this;
    }

    /**
     * 添加新的展示消息
     *
     * @param displayString
     * @return
     */
    public boolean addDisplayString(String displayString) {
        return addDisplayEntity(new DisplayEntity(displayString));
    }

    /**
     * 添加新的展示消息
     *
     * @param displayEntity
     * @return
     */
    public boolean addDisplayEntity(DisplayEntity displayEntity) {
        boolean result = false;
        if (displayEntity != null && displayEntity.isValid()) {
            if (mDisplayList == null) {
                mDisplayList = new ArrayList<>();
            }
            result = addEntity(displayEntity);

            if (!mIsRolling) {
                startRoll();
            }
        }
        return result;
    }

    /**
     * 添加消息，如果有ID的话，替换掉消息实体，保证ID的唯一性
     *
     * @param displayEntity
     */
    private boolean addEntity(DisplayEntity displayEntity) {
        if (TextUtils.isEmpty(displayEntity.getID())) {
            return mDisplayList.add(displayEntity);
        } else {
            boolean result = false;
            boolean isFindSameID = false;
            for (int i = 0; i < mDisplayList.size(); i++) {  //去重
                if (displayEntity.getID().equals(mDisplayList.get(i).getID())) {
                    mDisplayList.set(i, displayEntity);
                    isFindSameID = true;
                    result = true;
                    break;
                }
            }
            if (!isFindSameID) {
                result = mDisplayList.add(displayEntity);
            }
            return result;
        }
    }

    /**
     * 开始滚动
     */
    public MarqueeTextView startRoll() {
        initRoll();
        return this;
    }

    /**
     * 去除展示的消息
     * @param displayString
     * @return
     */
    public boolean removeDisplayString(String displayString) {
        return removeDisplayEntity(new DisplayEntity(displayString));
    }

    /**
     * 去除展示的消息
     * @param displayEntity
     * @return
     */
    public boolean removeDisplayEntity(DisplayEntity displayEntity) {
        if (displayEntity != null && displayEntity.isValid()) {
            if (isRollingDisplayEntity(displayEntity)) {
                //防止remove出错
                if (mCurrentIndex <= mDisplayList.size() - 1) {
                    mDisplayList.remove(mCurrentIndex);
                    rollDisplayByIndex(mCurrentIndex);
                    return true;
                } else {
                    rollDisplayByIndex(mCurrentIndex);
                    return false;
                }
            } else {
                return removeByDisplayEntity(displayEntity);
            }
        } else {
            return false;
        }
    }

    /**
     * 判断是否是当前正在滚动展示的消息
     * @param displayEntity
     * @return
     */
    private boolean isRollingDisplayEntity(DisplayEntity displayEntity){
        if (!mIsRolling || mShowDisplayEntity == null) {
            return false;
        }
        if (TextUtils.isEmpty(displayEntity.getID())) {
            return displayEntity.getMessage().equals(mShowDisplayEntity.getMessage());
        } else {
            return displayEntity.getID().equals(mShowDisplayEntity.getID());
        }
    }

    /**
     * 去除消息 【只去除一个】
     * @param displayEntity
     */
    private boolean removeByDisplayEntity(DisplayEntity displayEntity) {
        if (getDisplaySize() > 0) {
            Iterator<DisplayEntity> it = mDisplayList.iterator();
            synchronized (mLock) {
                while (it.hasNext()) {
                    DisplayEntity matchEntity = it.next();
                    if (TextUtils.isEmpty(displayEntity.getID())) {
                        if (displayEntity.getMessage().equals(matchEntity.getMessage())) {
                            it.remove();
                            return true;
                        }
                    } else {
                        if (displayEntity.getID().equals(matchEntity.getID())) {
                            it.remove();
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * 更新显示的内容
     *
     * @param displayEntity
     */
    private void updateDisplayText(DisplayEntity displayEntity) {
        if (displayEntity != null) {
            if (mOnMarqueeListener != null) {
                DisplayEntity temp = mOnMarqueeListener.onStartMarquee(displayEntity, mCurrentIndex);
                //返回的消息有效
                if (temp != null && temp.isValid()) {
                    displayEntity = temp;
                    mDisplayList.set(mCurrentIndex, displayEntity);
                } else {   //返回的消息无效， 去除该条消息，继续滚动下一条
                    //防止remove出错
                    if (mCurrentIndex <= mDisplayList.size() - 1) {
                        mDisplayList.remove(mCurrentIndex);
                    }
                    rollDisplayByIndex(mCurrentIndex);
                    return;
                }
            }
            showDisplayEntity(displayEntity);
        } else {
            rollNextDisplay();
        }
    }

    /**
     * 滚动显示消息
     * @param displayEntity
     */
    private void showDisplayEntity(DisplayEntity displayEntity) {
        mShowDisplayEntity = displayEntity;
        mDisplayTextWidth = getPaint().measureText(mShowDisplayEntity.toString());
        mCurrentPosition = mScrollWidth;
        if (mHandler.hasMessages(REDRAW_TEXT)) {
            mHandler.removeMessages(REDRAW_TEXT);
        }
        if (!mStopMarquee) {
            mHandler.sendEmptyMessageDelayed(REDRAW_TEXT, 500);
        } else {
            mIsRolling = false;
        }
    }

    @Override
    protected void onAttachedToWindow() {
        mStopMarquee = false;
        if (isShowDisplayEntityValid()) {
//            mHandler.sendEmptyMessageDelayed(REDRAW_TEXT, 2000); //防止第一次进入的时候有加速的效果
        } else {
            mIsRolling = false;
        }
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        mStopMarquee = true;
        mIsRolling = false;
        if (mHandler.hasMessages(REDRAW_TEXT)) {
            mHandler.removeMessages(REDRAW_TEXT);
        }
        super.onDetachedFromWindow();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isShowDisplayEntityValid()) {
            mBaseline = calculateBaseLine();
            canvas.drawText(mShowDisplayEntity.toString(), mCurrentPosition, mBaseline, getPaint());
            mIsRolling = true;
        }
    }

    /**
     * 当前滚动显示的消息是否有效
     *
     * @return
     */
    private boolean isShowDisplayEntityValid() {
        return mShowDisplayEntity != null && mShowDisplayEntity.isValid();
    }

    private int calculateBaseLine() {
        Paint.FontMetricsInt fontMetrics = getPaint().getFontMetricsInt();
        return (getHeight() - fontMetrics.bottom - fontMetrics.top) / 2;
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case REDRAW_TEXT:
                    if (mCurrentPosition < (-mDisplayTextWidth)) {// 一段文字滚动完了
                        rollNextDisplay();
                    } else {
                        mCurrentPosition -= mSpeed;  //向前进
                        reDraw(30);
                    }
                    break;
                default:
                    break;
            }
            return true;
        }
    });

    /**
     * 滚动下一条
     */
    private void rollNextDisplay() {
        mCurrentIndex++;
        rollDisplayByIndex(mCurrentIndex);
    }

    /**
     * 滚动显示指定索引的内容
     * @param index
     */
    private void rollDisplayByIndex(int index) {
        if (index <= mDisplayList.size() - 1) {
            updateDisplayText(getDisplayItem(index));
        } else {  //所有文字滚完了一遍
            handleRollFinished();
        }
    }

    /**
     * 滚动一轮完毕
     */
    private void handleRollFinished() {
        if (mOnMarqueeListener == null || onMarqueeFinished()) {
            initRoll();
        } else {
            mIsRolling = false;
        }
    }

    /**
     * 滚动完毕之后的回调处理
     * @return
     */
    private boolean onMarqueeFinished() {
        List<DisplayEntity> tmp = mOnMarqueeListener.onMarqueeFinished(mDisplayList);
        if (tmp != null) {
            mDisplayList = tmp;
            return true;
        } else {
            return false;
        }
    }

    /**
     * 初始化滚动
     */
    private void initRoll() {
        if (mDisplayList != null && mDisplayList.size() > 0) {
            if (mIsAutoDisplay) {
                setVisibility(VISIBLE);
            }
            mCurrentIndex = 0;
            updateDisplayText(getDisplayItem(mCurrentIndex));
        } else {
            if (mIsAutoDisplay) {
                setVisibility(GONE);
            }
            mIsRolling = false;
        }
    }

    /**
     * 重绘
     *
     * @param delayMillis
     */
    private void reDraw(int delayMillis) {
        invalidate();
        if (!mStopMarquee && mHandler != null) {
            mHandler.sendEmptyMessageDelayed(REDRAW_TEXT, delayMillis);
        } else {
            mIsRolling = false;
        }
    }

    /**
     * 设置控件的宽度
     *
     * @param viewWidth
     * @return
     */
    public MarqueeTextView setViewWidth(int viewWidth) {
        mCurrentPosition = viewWidth;
        mScrollWidth = viewWidth;
        return this;
    }

    public float getCurrentPosition() {
        return mCurrentPosition;
    }

    public MarqueeTextView setCurrentPosition(float coordinateX) {
        mCurrentPosition = coordinateX;
        return this;
    }

    public int getScrollWidth() {
        return mScrollWidth;
    }

    public MarqueeTextView setScrollWidth(int scrollWidth) {
        mScrollWidth = scrollWidth;
        return this;
    }

    public int getSpeed() {
        return mSpeed;
    }

    /**
     * 设置滚动的速度
     *
     * @param speed（单位px）
     * @return
     */
    public MarqueeTextView setSpeed(int speed) {
        mSpeed = speed;
        return this;
    }

    /**
     * 是否在滚动
     *
     * @return
     */
    public boolean isRolling() {
        return mIsRolling;
    }

    /**
     * 获取当前滚动集合的索引
     *
     * @return
     */
    public int getCurrentIndex() {
        return mCurrentIndex;
    }

    /**
     * 获取正在滚动展示的消息实体
     *
     * @return
     */
    public DisplayEntity getShowDisplayEntity() {
        return mShowDisplayEntity;
    }

    /**
     * 获取正在滚动展示的消息体集合
     *
     * @return
     */
    public List<DisplayEntity> getDisplayList() {
        return mDisplayList;
    }

    /**
     * 获取显示消息的数量
     *
     * @return
     */
    public int getDisplaySize() {
        return mDisplayList != null ? mDisplayList.size() : 0;
    }

    /**
     * 当前滚动字幕是否有需要滚动的消息
     * @return
     */
    public boolean hasDisplayMessage() {
        return getDisplaySize() > 0;
    }

    /**
     * 根据index获取滚动的消息实体
     * @param index
     * @return
     */
    public DisplayEntity getDisplayItem(int index) {
        if (mDisplayList != null && index >= 0 && index <= mDisplayList.size() - 1) {
            return mDisplayList.get(index);
        } else {
            return  null;
        }
    }
    /**
     * 清除内容
     */
    public void clear() {
        mIsRolling = false;
        if (mDisplayList != null && mDisplayList.size() > 0) {
            mDisplayList.clear();
        }
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        if (mIsAutoDisplay) {
            setVisibility(GONE);
        }
    }

    /**
     * 设置滚动监听
     *
     * @param listener
     * @return
     */
    public MarqueeTextView setOnMarqueeListener(OnMarqueeListener listener) {
        mOnMarqueeListener = listener;
        return this;
    }

    /**
     * 滚动结束的监听
     */
    public interface OnMarqueeListener {
        /**
         * 开始滚动
         *
         * @param displayEntity 开始滚动的内容实体
         * @param index         开始滚动内容的索引
         * @return
         */
        DisplayEntity onStartMarquee(DisplayEntity displayEntity, int index);

        /**
         * 滚动完毕
         *
         * @return 新一轮滚动的消息集合，null默认不继续滚动
         */
        List<DisplayEntity> onMarqueeFinished(List<DisplayEntity> displayDatas);
    }

}
