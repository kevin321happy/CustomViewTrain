package com.wh.jxd.com.bezierview.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.wh.jxd.com.bezierview.LineInfo;
import com.wh.jxd.com.bezierview.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by kevin321vip on 2018/1/23.
 * 仿华为天气的自定义控件
 */

public class HweatherWidget extends View {
    private int mWidth;
    private int mHeight;
    private int mCPointX;
    private int mCPointY;
    private int mRadius;

    private Path mPath;
    private Path mSecondPath;
    private ValueAnimator mAnimator;
    private float mAnimatedValuevalue;
    Matrix matrix = new Matrix();
    /**
     * 记录圆上的点的集合
     */
    private List<Point> mPoints = new ArrayList<>();
    /**
     * 记录图片的左上角的点
     */
    private List<Point> mIconPoints = new ArrayList<>();
    private int mSize;
    private Thread mThread;
    private Path mSunPath;
    /**
     * 画圈的画笔
     */
    private Paint mCirclePaint;
    /**
     * 画笔宽度
     */
    private int mStrokeWidth = 4;
    private RectF mRectF;
    /**
     * 控件的内边距,保证空间可以完整的画出来
     */
    private int mPadding = 50;
    /**
     * 绘制第一个圆弧的画笔
     */
    private Paint mFirstArcPaint;
    /**
     * 绘制第二个圆弧的画笔
     */
    private Paint mSecondArcPaint;
    /**
     * 虚线
     */
    private PathEffect mEffects;

    public HweatherWidget(Context context) {
        super(context);
        init();
    }

    public HweatherWidget(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HweatherWidget(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mEffects = new DashPathEffect(new float[]{30, 30}, 0);
        //绘制第一个圆弧的画笔
        mFirstArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFirstArcPaint.setPathEffect(mEffects);
        mFirstArcPaint.setAntiAlias(true);
        mFirstArcPaint.setDither(true);
        mFirstArcPaint.setColor(Color.GRAY);
        mFirstArcPaint.setStrokeWidth(mStrokeWidth);
        mFirstArcPaint.setStyle(Paint.Style.STROKE);

        //绘制第二个圆弧的画笔
        mSecondArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSecondArcPaint.setPathEffect(mEffects);
        mSecondArcPaint.setAntiAlias(true);
        mSecondArcPaint.setDither(true);
        mSecondArcPaint.setColor(Color.RED);
        mSecondArcPaint.setStrokeWidth(mStrokeWidth);
        mSecondArcPaint.setStyle(Paint.Style.STROKE);

        //绘制太阳的画笔
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setDither(true);
        mCirclePaint.setStrokeWidth(mStrokeWidth);
        mCirclePaint.setColor(Color.YELLOW);
        //轨迹
        mPath = new Path();
        mSecondPath = new Path();
        mSunPath = new Path();
        initAnimation();
    }

    private void initAnimation() {
        mAnimator = ValueAnimator.ofFloat(0, 1);
        mAnimator.setDuration(10000);
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimatedValuevalue = (float) animation.getAnimatedValue();
                upDataDraw();

            }
        });
        mAnimator.start();
    }

    /**
     * 更新绘制
     */
    private void upDataDraw() {
        if (mThread == null) {
            mThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    postInvalidate();

                }
            });
        }
        mThread.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        mCPointX = mWidth / 2;
        mRadius = mWidth / 2;
        mCPointY =mWidth/2;
        //手动测量View的宽高,保证多出的空间
        setMeasuredDimension(mWidth , mHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initPointPath();
    }

    /**
     * 初始化点的路径，将点存到集合中
     */
    private void initPointPath() {
        int len=mRadius-mStrokeWidth-40;
        for (int i = 180; i < 360; i++) {
            double radians = Math.toRadians(i);
            //通过半径和弧度可以求出圆上的点的坐标
            int x = (int) (mCPointX + Math.cos(radians) * len);
            int y = (int) (mCPointY + Math.sin(radians) * len);
            mPoints.add(new Point(x, y));
        }
        mSize = mPoints.size();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawFirstArc(canvas);
        drawSecondArc(canvas);
        drawWeatherIcon(canvas);
    }

    /**
     * 绘制第一条路径
     *
     * @param canvas
     */
    private void drawFirstArc(Canvas canvas) {
        mRectF = new RectF(mPadding, mPadding, 2 * mRadius - mPadding, 2 * mRadius - mPadding);
        canvas.drawArc(mRectF, 180, 180, false, mFirstArcPaint);
    }

    /**
     * 绘制第二条路径
     *
     * @param canvas
     */
    private void drawSecondArc(Canvas canvas) {
        float sweepAngle = 180 * mAnimatedValuevalue;
        mRectF = new RectF(mPadding, mPadding, 2 * mRadius - mPadding, 2 * mRadius - mPadding);
        canvas.drawArc(mRectF, 180, sweepAngle, false, mSecondArcPaint);
    }


    /**
     * 绘制天气的图标
     *
     * @param canvas
     */
    private void drawWeatherIcon(Canvas canvas) {
        mCirclePaint.setColor(Color.YELLOW);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(mStrokeWidth);
        //获取当前进度下面的点
        int position = (int) (mSize * mAnimatedValuevalue);
//        double radians = Math.toRadians(180 * mAnimatedValuevalue);
        //获取当前的角度
        if (position < mSize) {
            Point point = mPoints.get(position);
//            int x = (int) (point.x + Math.cos(radians));
//            int y = (int) (point.y + Math.sin(radians));
            int x = point.x;
            int y = point.y;
            canvas.drawCircle(x, y, 40, mCirclePaint);
            mCirclePaint.setColor(Color.WHITE);
            canvas.drawCircle(x, y, 35, mCirclePaint);
            mCirclePaint.setColor(Color.BLACK);
            mCirclePaint.setStyle(Paint.Style.FILL);
            mCirclePaint.setStrokeWidth(3);
            canvas.drawPoint(x,y,mCirclePaint);
        }
    }
}
