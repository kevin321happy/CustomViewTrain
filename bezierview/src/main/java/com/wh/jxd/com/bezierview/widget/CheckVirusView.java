package com.wh.jxd.com.bezierview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by kevin321vip on 2018/2/1.
 * 仿华为病毒检查控件
 */

public class CheckVirusView extends View {

    private Paint mPaint;
    /**
     * 画笔的宽度
     */
    private int mStrokeWidth = 3;
    /**
     * 圆心的X,Y
     */
    private int mCPointx, mCPointy;
    /**
     * 最外圈圆的半径
     */
    private int mRadius;
    /**
     * 画笔的颜色
     */
    private int mPaintColor = Color.BLACK;
    private Path mPath;

    public CheckVirusView(Context context) {
        super(context);
        init(context, null);
    }

    public CheckVirusView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CheckVirusView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        mCPointx = widthSize / 2;
        mCPointy = heightSize / 2;
        mRadius = widthSize / 2 - mStrokeWidth;
        initPathTrack();
    }
    /**
     * 初始化
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(mPaintColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPath = new Path();



    }

    /**
     * Path的轨迹
     */
    private void initPathTrack() {
        mPath.moveTo(mCPointx, mCPointy);
        mPath.lineTo(mCPointx + mRadius, mCPointy);
        mPath.moveTo(mCPointx, mCPointy);
        mPath.lineTo(mCPointx, mCPointy + mRadius);
        mPath.moveTo(mCPointx, mCPointy);
        mPath.lineTo(mCPointx - mRadius, mCPointy);
        mPath.moveTo(mCPointx, mCPointy);
        mPath.lineTo(mCPointx, mCPointy - mRadius);
    }

    /**
     * 绘制内容
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCircle(canvas);
        drawCrossPath(canvas);
//        canvas.drawCircle();

    }

    /**
     * 绘制十字的路径
     *
     * @param canvas
     */
    private void drawCrossPath(Canvas canvas) {
        mPaint.setStrokeWidth(mStrokeWidth/2);
        canvas.drawPath(mPath, mPaint);
    }

    /**
     * 绘制圆圈
     *
     * @param canvas
     */
    private void drawCircle(Canvas canvas) {
        mPaint.setStrokeWidth(mStrokeWidth);
        for (int i = 0; i < 3; i++) {
            canvas.drawCircle(mCPointx, mCPointy, mRadius - (mRadius * i / 4), mPaint);
        }
    }
}
