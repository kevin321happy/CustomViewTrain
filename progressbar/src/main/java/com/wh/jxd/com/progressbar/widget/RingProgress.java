package com.wh.jxd.com.progressbar.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.wh.jxd.com.progressbar.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin321vip on 2018/1/25.
 * 圆环的ProgressBar
 */
public class RingProgress extends ProgressBar {
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
    /**
     * 实际高度
     */
    private int mActualHeight;
    /**
     * 圆心坐标
     */
    private int mCpointX, mCPointY;
    private int mWidthSize;
    private int mHeightSize;

    /**
     * 存放圆上的点的集合
     *
     * @param context
     */
    private List<Point> mPoints = new ArrayList<>();
    /**
     * 圆环的宽度
     */
    private int mRingwidth;

    public RingProgress(Context context) {
        this(context, null);
    }

    public RingProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RingProgress(Context context, AttributeSet attrs, int defStyleAttr) {
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
     * 初始化圆环上面的点(以这些点为圆心绘制圆保证了圆的接口处是弧线的)
     */
    private void initPointOnRing() {
        //圆上的点和圆心的连线的长度
        int distance = mWidthSize / 2 - mRingwidth;
        for (int i = 0; i < 360; i++) {
            Point point = new Point();
            double radians = Math.toRadians(i);
            point.x = (int) (mCpointX + distance * Math.cos(radians));
            point.y = (int) (mCPointY + distance * Math.sin(radians));
            mPoints.add(point);
        }
    }

    /**
     * 获取自定义属性
     *
     * @param context
     * @param attrs
     */
    private void getAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RingProgress);
        mReachColor = ta.getColor(R.styleable.RingProgress_RingProgressRearchColor, Color.RED);
        mUnRearchColor = ta.getColor(R.styleable.RingProgress_RingProgressUnRearchColor, Color.GRAY);
        mRingwidth = ta.getDimensionPixelSize(R.styleable.RingProgress_RingWidth, 40);
        mTextSize = ta.getDimensionPixelSize(R.styleable.RingProgress_RingProgressTextSize, 40);
        mTextColor = ta.getColor(R.styleable.RingProgress_RingProgressTextColor, Color.RED);
        //回收
        ta.recycle();
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidthSize = MeasureSpec.getSize(widthMeasureSpec);
        mHeightSize = MeasureSpec.getSize(heightMeasureSpec);
        mCpointX = mWidthSize / 2;
        mCPointY = mHeightSize / 2;
        //初始化环上的圆的圆心点
        initPointOnRing();


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
        canvas.translate(getPaddingLeft(), getPaddingTop());
        String text = getProgress() + "%";
        drawRearchArc(canvas);
        //画布还原
        canvas.restore();
    }

    /**
     * 绘制进度的圆弧
     *
     * @param canvas
     */
    private void drawRearchArc(Canvas canvas) {
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mRingwidth);
        RectF rectF = new RectF();
        rectF.left = 0 +mRingwidth;
        rectF.top = 0 + mRingwidth;
        rectF.right = mWidthSize - mRingwidth;
        rectF.bottom = mHeightSize - mRingwidth;
        canvas.drawCircle(mCpointX,mCPointY,mWidthSize/2-mRingwidth,mPaint);
        mPaint.setColor(Color.BLUE);
        canvas.drawArc(rectF, 0, 270, false, mPaint);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.BLUE);
        Point startPoint = mPoints.get(0);
        Point endPoint = mPoints.get(270);
        Log.i("Point", "圆心上的点:" + startPoint.x + "，" + startPoint.y);
        canvas.drawCircle(startPoint.x, startPoint.y, 20, mPaint);
        canvas.drawCircle(endPoint.x, endPoint.y, 20, mPaint);
//        canvas.drawCircle();
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
