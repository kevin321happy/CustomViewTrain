package com.wh.jxd.com.bezierview.widget.hc;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by kevin321vip on 2018/1/23.
 * 通过动画的方式实现了线跑动的效果
 */

public class ChongPath extends View {

    private Paint mPaint;
    /**
     * 动画的值
     */
    private float mFloat;
    private ValueAnimator mAnimator;
    private Path mPath;
    private Path mDispath;
    private PathMeasure mPathMeasure;

    public ChongPath(Context context) {
        super(context);
        init();
    }

    public ChongPath(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChongPath(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //初始化画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeWidth(10);
        mPaint.setStyle(Paint.Style.STROKE);

        mPath = new Path();
        mDispath = new Path();
        /**
         * mPathMeasure是一个用来测量Path的类
         */
        mPathMeasure = new PathMeasure();
        /**
         * 关联一个Path,Path不闭合
         */
//        mPathMeasure.setPath(mPath, false);

        mAnimator = ValueAnimator.ofFloat(0, 1);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mFloat = (float) animation.getAnimatedValue();
                Log.i("TAG","当前进度:"+mFloat);
                invalidate();
            }
        });
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.setDuration(4000);
        mAnimator.setRepeatMode(ValueAnimator.REVERSE);
        mAnimator.setRepeatCount(10);
        mAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制第一条线
        drawFirstLine(canvas);
        //绘制第二条线
        drawSecond(canvas);
    }

    /**
     * 绘制第一条线
     *
     * @param canvas
     */
    private void drawFirstLine(Canvas canvas) {
        mPath.reset();
        mPath.lineTo(200, 200);
        mPath.lineTo(200, 800);
        mPath.lineTo(800, 800);
        mPath.lineTo(300, 1000);
        mPaint.setColor(Color.GRAY);
        canvas.drawPath(mPath, mPaint);

    }

    /**
     * 绘制第二条线
     */
    private void drawSecond(Canvas canvas) {
        mDispath.reset();
        /**
         * 截取Path片段
         * 返回值 boolean  判断是否截取成功
         * startD 开始截取的位置距离Path起点的长度
         * stopD 结束截取的位置距离Path起点的长度
         * dsi  截取之后的path
         * startWithTo 起始点是否用MoveTo,用于保证截取的Path第一个点的位置不变
         */
        mPathMeasure.setPath(mPath, false);
        mPathMeasure.getSegment(0.0f, mPathMeasure.getLength() * mFloat, mDispath, true);
        mPaint.setColor(Color.RED);
        canvas.drawPath(mDispath, mPaint);


    }
}
