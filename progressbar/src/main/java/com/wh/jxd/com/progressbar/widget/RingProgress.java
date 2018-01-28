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
import android.widget.FrameLayout;
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
    //圆心的位置的偏移,
    private int mOffsetAngle = 20;
    private RectF mRectF;

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
        mReachColor = ta.getColor(R.styleable.RingProgress_RingProgressRearchColor, Color.BLUE);
        mUnRearchColor = ta.getColor(R.styleable.RingProgress_RingProgressUnRearchColor, Color.GRAY);
        mRingwidth = ta.getDimensionPixelSize(R.styleable.RingProgress_RingWidth, 40);
        mTextSize = ta.getDimensionPixelSize(R.styleable.RingProgress_RingProgressTextSize, 60);
        mTextColor = ta.getColor(R.styleable.RingProgress_RingProgressTextColor, Color.BLACK);
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
        int progress = getProgress();
        float precent = ((float) progress) / 100;
        //圆环的结束位置对应的Point在mPoints中的脚标索引
        int endPointIndex = (int) (360 * precent);
        //进度弧形扫描过的角度
        float sweepAngle = 360 * precent;
        String text = progress + "%";
        drawBackGroundArc(canvas);
        drawRearchArc(canvas, endPointIndex, sweepAngle);
        drawProgressText(canvas, text);
        //画布还原
        canvas.restore();
    }

    /**
     * 绘制背景的圆弧
     *
     * @param canvas
     */
    private void drawBackGroundArc(Canvas canvas) {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mRingwidth);
        mPaint.setColor(mUnRearchColor);
        canvas.drawCircle(mCpointX, mCPointY, mWidthSize / 2 - mRingwidth, mPaint);
    }

    /**
     * 绘制进度的圆弧
     *
     * @param canvas
     * @param endPointIndex
     * @param sweepAngle
     */
    private void drawRearchArc(Canvas canvas, int endPointIndex, float sweepAngle) {
        mPaint.setColor(mReachColor);
        if (mRectF == null) {
            mRectF = new RectF();
        }
        mRectF.left = mRingwidth;
        mRectF.top = mRingwidth;
        mRectF.right = mWidthSize - mRingwidth;
        mRectF.bottom = mHeightSize - mRingwidth;
        canvas.drawArc(mRectF, 0, sweepAngle, false, mPaint);
        //绘制两端的圆形,让圆环看起来是圆滑的
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.BLUE);
        Point startPoint = mPoints.get(0);
        Point endPoint = mPoints.get(endPointIndex);
        canvas.drawCircle(startPoint.x, startPoint.y, mRingwidth / 2, mPaint);
        canvas.drawCircle(endPoint.x, endPoint.y, mRingwidth / 2, mPaint);
    }

    /**
     * 绘制进度的文字
     *
     * @param canvas
     * @param text
     */
    private void drawProgressText(Canvas canvas, String text) {
        mPaint.setColor(mTextColor);
        //测量出Text的宽度
        int textWidth = (int) mPaint.measureText(text);
        int textHeight = (int) (mPaint.descent() - mPaint.ascent());
        canvas.drawText(text, mCpointX - textWidth / 2, mCPointY + textHeight / 2, mPaint);
        Log.i("tt", "绘制了文字");
    }

    @Override
    public synchronized void setProgress(int progress) {
        super.setProgress(progress);
    }
}
