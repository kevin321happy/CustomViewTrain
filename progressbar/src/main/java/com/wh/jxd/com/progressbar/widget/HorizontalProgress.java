package com.wh.jxd.com.progressbar.widget;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ProgressBar;

import com.wh.jxd.com.progressbar.R;

/**
 * Created by kevin321vip on 2018/1/25.
 * 水平的ProgressBar
 */

public class HorizontalProgress extends ProgressBar {

    private Paint mPaint;
    /**
     * 到达的进度的颜色
     */
    private int mReachColor;
    /**
     * 未达到的进度的颜色
     */
    private int mUnRearchColor;

    /**
     * 达到的进度的高度
     *
     * @param context
     */
    private int mRearchBarHeight;
    /**
     * 未到达的进度的高度
     *
     * @param context
     */
    private int mUnRearchBarHeight;

    /**
     * 进度文字的颜色
     *
     * @param context
     */
    private int mTextColor;

    /**
     * 进度文字的大小
     *
     * @param context
     */
    private int mTextSize;
    /**
     * 实际可用的宽度
     */
    private int mActualWidth;

    /**
     * 文字的左边的边距
     *
     * @param context
     */
    private int mTextMargin;

    /**
     * 实际测量的高度
     */
    private int mMeasureHeight;
    /**
     * 是否需要绘制mUnRearch部分,当文字绘制达到了终点就不需要
     */
    private boolean mShowDrawunRearchBar;




    public HorizontalProgress(Context context) {
        this(context, null);
    }

    public HorizontalProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mPaint = new Paint();
        getAttrs(context, attrs);
        //注意：这里要先获取到自定义属性
        mPaint.setTextSize(mTextSize);
    }

    /**
     * 获取自定义属性
     *
     * @param context
     * @param attrs
     */
    private void getAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.HorizontalProgress);
        mReachColor = ta.getColor(R.styleable.HorizontalProgress_HorProgressReacherColor, Color.RED);
        mUnRearchColor = ta.getColor(R.styleable.HorizontalProgress_HorProgressUnReacherColor, Color.GRAY);
        mRearchBarHeight = ta.getDimensionPixelOffset(R.styleable.HorizontalProgress_HorProgressReacherHeight, 10);
        mUnRearchBarHeight = ta.getDimensionPixelSize(R.styleable.HorizontalProgress_HorProgressUnReacherHeight, 10);
        mTextColor = ta.getColor(R.styleable.HorizontalProgress_HorProgressTextColor, Color.BLUE);
        mTextSize = ta.getDimensionPixelSize(R.styleable.HorizontalProgress_HorProgressTextSize, 40);
        mTextMargin = ta.getDimensionPixelOffset(R.styleable.HorizontalProgress_HorProgressTextMargin, 10);
        //回收
        ta.recycle();
    }
    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        //控件的实际的宽度为测量的宽度减去左右内边距
        mActualWidth = widthSize - getPaddingLeft() - getPaddingRight();

        mMeasureHeight = getMeasureHeight(heightMeasureSpec);
        //手动测量控件需要的宽高
        setMeasuredDimension(widthSize, mMeasureHeight);
    }

    /**
     * 测量高度返回最终的值
     *
     * @param heightMeasureSpec
     */
    private int getMeasureHeight(int heightMeasureSpec) {
        int Mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        if (Mode == MeasureSpec.EXACTLY) {
            //如果是精确模式,直接返回测量的高度
            return size;
        } else {
            int result = 0;
            //画笔的descent减去asent可以得到画笔画出从文字的高度
            int textheight = (int) (mPaint.descent() - mPaint.ascent());
            //取已达到的进度的高度,未达到的进度高度,文字的高度三者的最大值作为结果高度
            result = Math.max(Math.max(mRearchBarHeight, mUnRearchBarHeight), textheight);
            if (Mode == MeasureSpec.AT_MOST) {
                //如果测量模式的至多模式,直接反回测量的高度和result的较小值
                return Math.min(result, size);
            } else {
                return result;
            }
        }
    }

    /**
     * 绘制
     *
     * @param canvas
     */
    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        //平移画布,保证实际的起点是从实际有效区域开始的
        canvas.translate(getPaddingLeft(), mMeasureHeight / 2);
        String text = getProgress() + "%";
        float progress = getProgress() * 1.0f / 100;
        //进度达到的宽度
        int progressWidth = (int) (mActualWidth * progress);
        //进度文本的宽度
        int TextWidth = (int) mPaint.measureText(text);
        //进度到达部分的终点X位置=进度宽度-文字的左边距-文字宽度的一半(开始位置为左边距但是这里做了画布的平移,所以起点是0)
        int RearchEndX = progressWidth - mTextMargin - mTextWidth / 2;
//        Log.i("Tag","进度结束点:"+RearchEndX,"当前进度:"+getProgress()+"进度宽度")
        //进度文字的开始位置
        int TextStartX = RearchEndX + mTextMargin;
        //未开始部分的进度的起始点
        int UnRearchStartX = TextStartX + TextWidth + mTextMargin;
        //当文字绘制的右边达到了终点位置,就不需要绘制UnRearch部分了
        if (UnRearchStartX >= mActualWidth) {
            mShowDrawunRearchBar = false;
        } else {
            mShowDrawunRearchBar = true;
        }
        drawRerchBar(canvas, RearchEndX);
        drawUnRearchBar(canvas, UnRearchStartX);
        drawProgressText(canvas, text, TextStartX);
        //画布还原
        canvas.restore();
    }

    /**
     * 绘制进度的文字
     *
     * @param canvas
     * @param text
     * @param textStartX
     */
    private void drawProgressText(Canvas canvas, String text, int textStartX) {
        mPaint.setColor(mTextColor);
        int textHeight = (int) (mPaint.descent() - mPaint.ascent());
        canvas.drawText(text, textStartX, 0 + textHeight / 2 - mRearchBarHeight / 2, mPaint);
        Log.i("tt", "绘制了文字");
    }

    /**
     * 绘制未到达的进度
     *
     * @param canvas
     * @param unRearchStartX
     */
    private void drawUnRearchBar(Canvas canvas, int unRearchStartX) {
        mPaint.setColor(mUnRearchColor);
        mPaint.setStrokeWidth(mUnRearchBarHeight);
        if (mShowDrawunRearchBar) {
            canvas.drawLine(unRearchStartX, 0, mActualWidth, 0, mPaint);
        }
    }

    /**
     * 绘制已到达的进度
     *
     * @param canvas
     * @param rearchEndX
     */
    private void drawRerchBar(Canvas canvas, int rearchEndX) {
        mPaint.setColor(mReachColor);
        mPaint.setStrokeWidth(mRearchBarHeight);
        canvas.drawLine(0, 0, rearchEndX, 0, mPaint);
    }

    @Override
    public synchronized void setProgress(int progress) {
        super.setProgress(progress);
    }
}
