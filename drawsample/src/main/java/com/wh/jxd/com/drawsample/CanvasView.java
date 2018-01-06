package com.wh.jxd.com.drawsample;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by kevin321vip on 2018/1/6.
 */

public class CanvasView extends View {

    private Paint mPaint;

    public CanvasView(Context context) {
        this(context,null);
    }

    public CanvasView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 初始化 画笔等操作
     */
    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(60);
        mPaint.setColor(Color.GREEN);
    }

    /**
     * 绘制的方法
     * @param canvas
     */
    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //-------->绘制白色矩形
        mPaint.setColor(Color.WHITE);
        canvas.drawRect(0, 0, 800, 800, mPaint);
        mPaint.reset();

        //-------->绘制直线
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(10);
        canvas.drawLine(450, 30, 570, 170, mPaint);
        mPaint.reset();

        //-------->绘制带边框的矩形
        mPaint.setStrokeWidth(10);
        mPaint.setARGB(150, 90, 255, 0);
        mPaint.setStyle(Paint.Style.STROKE);
        RectF rectF1=new RectF(30, 60, 350, 350);
        canvas.drawRect(rectF1, mPaint);
        mPaint.reset();

        //-------->绘制实心圆
        mPaint.setStrokeWidth(14);
        mPaint.setColor(Color.GREEN);
        mPaint.setAntiAlias(true);
        canvas.drawCircle(670, 300, 70, mPaint);
        mPaint.reset();

        //-------->绘制椭圆
        mPaint.setColor(Color.YELLOW);
        RectF rectF2=new RectF(200, 430, 600, 600);
        canvas.drawOval(rectF2, mPaint);
        mPaint.reset();

        //-------->绘制文字
        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(60);
        mPaint.setUnderlineText(true);
        canvas.drawText("Hello Android", 150, 720, mPaint);
        mPaint.reset();

    }
}
