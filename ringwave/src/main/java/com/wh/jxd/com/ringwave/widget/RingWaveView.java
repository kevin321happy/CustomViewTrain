package com.wh.jxd.com.ringwave.widget;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by kevin321vip on 2018/1/14.
 * 环形的波纹效果的View
 */

public class RingWaveView extends View {

    private Paint mPaint;
    private int mRadius = 5;
    //圆环的宽度
//    private int mRingWidt = 50;
    //记录按下的点的X,Y的值
    private float mDownX = 300;
    private float mDownY = 300;

    int[] mColors = new int[]{
            Color.parseColor("#FF7F00")
            , Color.parseColor("#00FFFF"), Color.parseColor("#8B00FF")
    };
    private Shader mShader;

    public RingWaveView(Context context) {
        this(context, null);
    }

    public RingWaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    /**
     * 定义一个Handler
     */

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mRadius += 5;
            int alpha = mPaint.getAlpha();
            alpha -= 5;
            if (alpha < 0) {
                alpha = 0;
            }
            mPaint.setAlpha(alpha);
            mPaint.setStrokeWidth(mRadius / 3);
            //重绘
            invalidate();
        }
    };

    /**
     * 初始化控件
     */
    private void initView() {
        mRadius = 5;
        mPaint = new Paint();
        //抗锯齿
        mPaint.setAntiAlias(true);

        //设置颜色线性渐变的效果
        mShader = new LinearGradient(100, 100, 100, 300, mColors, null, Shader.TileMode.CLAMP);
        mPaint.setShader(mShader);
//        mPaint.setColor(Color.RED);
        //样式为空心圆,圆环
        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(mRadius / 3);
        invalidate();
    }
//    dynamic

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                mDownX = event.getX();
                mDownY = event.getY();
                initView();
                break;


        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mPaint.getAlpha() > 0) {
            mHandler.sendEmptyMessageDelayed(0, 30);
            canvas.drawCircle(mDownX, mDownY, mRadius, mPaint);
        }
    }
}
