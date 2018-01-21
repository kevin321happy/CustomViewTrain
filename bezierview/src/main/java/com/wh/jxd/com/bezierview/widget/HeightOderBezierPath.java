package com.wh.jxd.com.bezierview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by kevin321vip on 2018/1/21.
 * 高阶的贝塞尔曲线4~6
 */

public class HeightOderBezierPath extends View {

    private Paint mPaint;
    private Path mPath;
    //背景的路径
    private Path mBgpath;

    public HeightOderBezierPath(Context context) {
        super(context);
        init();
    }


    public HeightOderBezierPath(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HeightOderBezierPath(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化方法
     */
    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(10);
        mPaint.setStyle(Paint.Style.STROKE);
        mPath = new Path();
        mBgpath = new Path();
        new Thread(new Runnable() {
            @Override
            public void run() {
                initBezier();
            }
        }).start();

    }

    /**
     * 初始化贝塞尔曲线
     */
    private void initBezier() {
        //四阶贝塞尔
        //x点坐标的数组
//        float[] xPonits = new float[]{0, 200, 600, 700, 1000};
//        //y点坐标的数组
//        float[] yPonits = new float[]{400, 0, 800, 400, 400};
//        mPath.moveTo(0,600);
        mBgpath.moveTo(0,600);
        float[] xPonits = new float[]{0,200, 800, 1000};
        //y点坐标的数组
        float[] yPonits = new float[]{600,0, 1200, 0};
        mBgpath.cubicTo(200,0,800,1200,1000,0);
        //进度t值为0.2的时候
//        float progress = 0.2f;
        //循环刷新的次数,当值足够大,连起来的点就会是一个平滑的曲线了
        int fps = 10000;
        for (int i = 0; i < fps; i++) {
            float t = i / (float) fps;
            float x = calculatePoint(t, xPonits);
            float y = calculatePoint(t, yPonits);
            //path连接的方式
            mPath.lineTo(x, y);
            //刷新
            postInvalidate();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.i("path", "path的当前点:" + x + "," + y);
        }
//        calculatePoint(progress,xPonits);


    }

    /**
     * 计算某时刻贝塞尔点的值 x,或y的值
     * t 0-1
     * values贝塞尔点的x，y的集合
     * 算法规则：
     * 相邻两个点之间根据贝塞尔公式两两运算,运算的结果存在前面一个值中
     * 没运算一轮就减少一个点(那一轮的在最后的一个点)
     * 通过双重循环到最后计算得到的值存贮在了数组中的0元素
     */
    private float calculatePoint(float t, float... values) {
        int length = values.length;
        for (int i = length - 1; i >= 0; i--) {
            for (int j = 0; j < i; j++) {
                //根据上一个点的位置得到下一个点
                values[j] = values[j] + (values[j + 1] - values[j]) * t;
            }
        }
        //运算时的的结果永远保存在第一位
        return values[0];
    }

    /**
     * 绘制
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.GRAY);
        canvas.drawPath(mBgpath, mPaint);
        //绘制贝塞尔曲线
        mPaint.setColor(Color.RED);
        canvas.drawPath(mPath, mPaint);
    }
}
