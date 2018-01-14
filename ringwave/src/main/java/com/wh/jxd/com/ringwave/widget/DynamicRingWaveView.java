package com.wh.jxd.com.ringwave.widget;


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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin321vip on 2018/1/14.
 * 环形的波纹效果的View
 * 动感的圆环波纹
 */

public class DynamicRingWaveView extends View {

    private Paint mPaint;
    private int mRadius = 5;
    //圆环的宽度
//    private int mRingWidt = 50;
    //记录按下的点的X,Y的值
    private float mDownX = 300;
    private float mDownY = 300;
    //是否是运行起来
    private boolean isRunning;
    //定义两次触摸的点的有效距离,大于这个值才有效
    private int DIS_SOLP = 13;

    private List<Wave> mWaves = new ArrayList<>();

    int[] mColors = new int[]{
            Color.parseColor("#FF7F00")
            , Color.parseColor("#00FFFF"), Color.parseColor("#8B00FF")
    };
    private Shader mShader;

    public DynamicRingWaveView(Context context) {
        this(context, null);
    }

    public DynamicRingWaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 定义一个Handler
     */

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //刷新数据
            RefreshData();
            //刷新绘制
            invalidate();
            //循环动画
            if (isRunning) {
                mHandler.sendEmptyMessageDelayed(0, 50);
            }

        }
    };


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                int x = (int) event.getX();
                int y = (int) event.getY();
                addPoint(x, y);
                mRadius = 5;
                break;
        }
        return true;
    }

    /**
     * 把点记录下来
     *
     * @param downX
     * @param downY
     */
    private void addPoint(float downX, float downY) {
        if (mWaves.size() == 0) {
            //第一个点,直接添加到集合
            addPoint2List(downX, downY);
            //启动动画
            isRunning = true;
            mHandler.sendEmptyMessage(0);
        } else {
            //取出最后一个Wave
            Wave wave = mWaves.get(mWaves.size() - 1);
            //判断触摸或滑动的点超过了设置的最小距离
            if (Math.abs(wave.pointX - downX) > DIS_SOLP || Math.abs(wave.pointY - downY) > DIS_SOLP) {
                addPoint2List(downX, downY);
            }
        }
    }

    /**
     * 刷新数据
     */
    public void RefreshData() {
        int size = mWaves.size();
        /**
         * 讲wave集合中的每一个子元素循环遍历,如果透明度为0了就从几集合中移除
         * ,不为0则依次改变他们的参数
         */
        for (int i = 0; i < size; i++) {
            if (mWaves.size() < i + 1) {
                return;
            }
            Wave wave = mWaves.get(i);
            Paint paint = wave.paint;
            int alpha = paint.getAlpha();
            if (alpha == 0) {
                mWaves.remove(i);
                continue;
            }
            alpha -= 5;
            if (alpha < 0) {
                alpha = 0;
            }
            //降低透明度
            paint.setAlpha(alpha);
            //扩大半径
            wave.redius = wave.redius + 3;
            //设置环的宽度
            wave.paint.setStrokeWidth(wave.redius / 3);
        }
        //如果集合中的所有元素移除完了,则停止了运行
        if (mWaves.size() == 0) {
            isRunning = false;
        }
    }

    /**
     * 讲
     *
     * @param downX
     * @param downY
     */
    private void addPoint2List(float downX, float downY) {
        Wave wave = new Wave();
        wave.pointX = downX;
        wave.pointY=downY;
        Paint pa = new Paint();
        pa.setAntiAlias(true);
        //设置颜色线性渐变的效果
        mShader = new LinearGradient(100, 100, 100, 300, mColors, null, Shader.TileMode.CLAMP);
        pa.setShader(mShader);
//        mPaint.setColor(Color.RED);
        //样式为空心圆,圆环
        pa.setStyle(Paint.Style.STROKE);
        pa.setStrokeWidth(mRadius / 3);
        wave.paint = pa;
        wave.redius = mRadius;
        mWaves.add(wave);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int size = mWaves.size();
        for (int i = 0; i < size; i++) {
            Wave wave = mWaves.get(i);
            canvas.drawCircle(wave.pointX, wave.pointY, wave.redius, wave.paint);
        }
    }

    /**
     * 定义一个wave对象
     */
    public class Wave {
        //圆心
        float pointX;
        float pointY;
        //画笔
        Paint paint;
        //半径
        int redius;
    }
}
