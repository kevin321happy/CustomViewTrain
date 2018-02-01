package com.wh.jxd.com.bezierview.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.util.ArrayList;
import java.util.List;

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
    /**
     * 旋转的角度
     */
    private int mAngle;
    /**
     * 矩阵
     */
    private Matrix mMatrix = new Matrix();
    private Path mPath;
    /**
     * 动画的值
     */
    private int mValue;
    /**
     * 绘制扫描的画笔
     */
    private Paint mGradientPaint;
    private SweepGradient mShader;
    private int mShaderColor = Color.BLUE;
    private ValueAnimator mAnimator;
    /**
     * 扫描画笔的透明度不断变化
     */
    private int mScanAlpha;
    /**
     * 存放斑点圆的圆心的集合
     */
    private List<Point> mPoints = new ArrayList<>();
    /**
     * 绘制斑点的画笔
     */
    private Paint mSpotPaint;
    private int mAlpha;
    private Thread mThread;

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
        mAnimator.start();
    }

    /**
     * 初始化
     *
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {
        //绘制的画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(mPaintColor);
        mPaint.setStyle(Paint.Style.STROKE);

        mSpotPaint = new Paint();
        mSpotPaint.setAntiAlias(true);
        mSpotPaint.setDither(true);
        mSpotPaint.setColor(Color.RED);
        mSpotPaint.setStyle(Paint.Style.FILL);
        //十字路径
        mPath = new Path();
        //绘制扫描的画笔
        mGradientPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mGradientPaint.setAntiAlias(true);
        mGradientPaint.setDither(true);
        mGradientPaint.setStyle(Paint.Style.FILL);
        initAnimation();
    }

    /**
     * 初始化动画
     */
    private void initAnimation() {
        mAnimator = ValueAnimator.ofInt(0, 360);
        //动画插值器
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mValue = (int) animation.getAnimatedValue();
                Log.i("value", "获取到了动画的值:" + mValue);
                redraw();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                postInvalidate();
            }
        });
        mAnimator.setDuration(3000);
        mAnimator.setRepeatCount(100);

    }

    private void redraw() {
        if (mThread == null) {
         mThread=  new Thread(new Runnable() {
               @Override
               public void run() {
                   try {
                       Thread.sleep(200);
                       postInvalidate();
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }
           });
        }
        mThread.start();
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
        drawScan(canvas);
        if (mValue % 30 == 0) {
            drawSpot(canvas);
        }
    }

    /**
     * 绘制圆形的斑点
     *
     * @param canvas
     */
    private void drawSpot(Canvas canvas) {
        canvas.save();
        mAlpha -= 5;
        if (mAlpha < 0) {
            mAlpha = 30;
        }
        mSpotPaint.setAlpha(mAlpha);

        int randowValuex = -50 + (int) (Math.random() * mRadius);      //返回大于等于m小于m+n（不包括m+n）之间的随机数
        int randowValuey = -50 + (int) (Math.random() * mRadius);
        int randowValuer = 5 + (int) (Math.random() * mRadius / 3);
        //返回大于等于m小于m+n（不包括m+n）之间的随机数


        canvas.drawCircle(mCPointx + randowValuex, mCPointy + randowValuey, randowValuer, mSpotPaint);

        canvas.restore();
    }

    /**
     * 绘制扫描的效果
     *
     * @param canvas
     */
    private void drawScan(Canvas canvas) {
        //设置透明度
//        mGradientPaint.setAlpha(mScanAlpha);
        mScanAlpha -= 5;
        if (mScanAlpha < 0) {
            mScanAlpha = 20;
        }
        Log.i("value", "旋转的rotate:" + mValue);
        //绘制雷达扫描的效果
        //绘制雷达扫描的效果
        mMatrix.setRotate(mValue, mCPointx, mCPointy);
        if (mShader == null) {
            //渐变效果
            mShader = new SweepGradient(mCPointx, mCPointy, Color.TRANSPARENT, mShaderColor);
        }
        mShader.setLocalMatrix(mMatrix);
        mGradientPaint.setShader(mShader);
        //绘制扫描效果
        canvas.drawCircle(mCPointx, mCPointy, mRadius, mGradientPaint);
    }

    /**
     * 绘制十字的路径
     *
     * @param canvas
     */
    private void drawCrossPath(Canvas canvas) {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStrokeWidth / 2);
        canvas.drawPath(mPath, mPaint);
    }

    /**
     * 绘制圆圈
     *
     * @param canvas
     */
    private void drawCircle(Canvas canvas) {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStrokeWidth);
        for (int i = 0; i < 3; i++) {
            canvas.drawCircle(mCPointx, mCPointy, mRadius - (mRadius * i / 4), mPaint);
        }
    }
}
