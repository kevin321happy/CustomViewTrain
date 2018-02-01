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
import android.graphics.Rect;
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
    /**
     * 文字的大小
     *
     * @param context
     */
    private int mTextSize = 30;
    /**
     * 绘制文字的画笔
     */
    private Paint mTextPaint;
    /**
     * 早上时间
     */
    private String MorningTime = "早上7:20";
    /**
     * 晚上时间
     */
    private String EveningTime = "晚上7:20";
    /**
     * 模拟当前时间,这里用的是距离早上6的的时间差值的分钟数
     */
    private int mCurrentTime = 540;

    /**
     * 当前的进度
     */
    private float mProgress = 0.75f;
    /**
     * 测量文字边界的Rect
     */
    private Rect mRect = new Rect();

    /**
     * 太阳上竖线的Path的集合
     *
     * @param context
     */
    private List<Path> mSunPaths = new ArrayList<>();

    private Path mSunPath = new Path();

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
        mFirstArcPaint.setColor(Color.WHITE);
        mFirstArcPaint.setStrokeWidth(mStrokeWidth);
        mFirstArcPaint.setStyle(Paint.Style.STROKE);
        //绘制第二个圆弧的画笔
        mSecondArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSecondArcPaint.setPathEffect(mEffects);
        mSecondArcPaint.setAntiAlias(true);
        mSecondArcPaint.setDither(true);
        mSecondArcPaint.setColor(Color.YELLOW);
        mSecondArcPaint.setStrokeWidth(mStrokeWidth);
        mSecondArcPaint.setStyle(Paint.Style.STROKE);

        //绘制太阳的画笔
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setDither(true);
        mCirclePaint.setStrokeWidth(mStrokeWidth);
        mCirclePaint.setColor(Color.YELLOW);
        //绘制文字的画笔
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setDither(true);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(mTextSize);
        initAnimation();
    }

    private void initAnimation() {
        mAnimator = ValueAnimator.ofFloat(0, 1);
        mAnimator.setDuration(5000);
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimatedValuevalue = (float) animation.getAnimatedValue();
                if (mAnimatedValuevalue > mProgress) {
                    mAnimatedValuevalue = mProgress;
                }
                upDataDraw();
            }
        });
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
        mCPointY = mWidth / 2;
        //手动测量View的宽高,保证多出的空间
        setMeasuredDimension(mWidth, mHeight);
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
        int len = mRadius - mStrokeWidth - 40;
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
        drawLine(canvas);
        drawText(canvas);
    }

    /**
     * 绘制文字
     */
    private void drawText(Canvas canvas) {
        mTextPaint.setTextSize(mTextSize);
        String text = MorningTime;
        mRect = new Rect();
        mTextPaint.getTextBounds(text, 0, text.length(), mRect);
        canvas.drawText(text, 10, mHeight - mRect.height(), mTextPaint);
        text = EveningTime;
        canvas.drawText(text, mWidth - mRect.width() - 10, mHeight - mRect.height(), mTextPaint);
        mTextPaint.setTextSize((float) (1.2 * mTextSize));
        Rect rect = new Rect();
        mTextPaint.getTextBounds("日出日落", 0, 4, rect);
        canvas.drawText("日出日落", mCPointX - rect.width() / 2, (float) mCPointY - rect.height(), mTextPaint);
    }

    /**
     * 绘制底部的线
     */
    private void drawLine(Canvas canvas) {
        canvas.drawLine(0, mWidth / 2, mWidth, mWidth / 2, mTextPaint);
    }

    /**
     * 绘制第一条路径
     *
     * @param canvas
     */
    private void drawFirstArc(Canvas canvas) {
        mRectF = new RectF(mPadding, mPadding, mWidth - mPadding, mWidth - mPadding);
        canvas.drawArc(mRectF, 180, 180, false, mFirstArcPaint);
    }

    /**
     * 绘制第二条路径
     *
     * @param canvas
     */
    private void drawSecondArc(Canvas canvas) {
        float sweepAngle = 180 * mAnimatedValuevalue;
        mRectF = new RectF(mPadding, mPadding, mWidth - mPadding, mWidth - mPadding);
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
        //获取当前进度下面的点
        int position = (int) (mSize * mAnimatedValuevalue);
        //获取当前的角度
        if (position < mSize) {
            Point point = mPoints.get(position);
            int x = point.x;
            int y = point.y;
            canvas.drawCircle(x, y, 40, mCirclePaint);

            mSunPath.reset();
            //绘制太阳上的Path
            for (int i = 0; i <= 360; i = i + 30) {
                //path在圆上每一个点的开始位置和结束位置
                int startx, starty, endx, endy;
                double radians = Math.toRadians(i);
                //这里40是太阳那个圆的半径
                startx = (int) (x + Math.cos(radians) * 40);
                starty = (int) (y + Math.sin(radians) * 40);
                endx = (int) (x + Math.cos(radians) * 60);
                endy = (int) (y + Math.sin(radians) * 60);
                mSunPath.moveTo(startx, starty);
                mSunPath.lineTo(endx, endy);
                if (i==360){
                    canvas.drawPath(mSunPath, mCirclePaint);
                }
//                Log.d("path", "path的开始点:" + startx + "," + starty + "结束点:" + endx + "，" + endy);
            }
            mCirclePaint.setStyle(Paint.Style.FILL);
            mCirclePaint.setColor(Color.WHITE);
            canvas.drawCircle(x, y, 35, mCirclePaint);
        }
    }

    /**
     * 设置当前时间（是分钟值,当前时间距离早上六点的时间）
     *
     * @param currentTime
     */
    public void setCurrentTime(int currentTime) {
        mCurrentTime = currentTime;
        mProgress = (float) mCurrentTime / 720;
        Log.i("curr", mProgress + "");
        if (mAnimator != null) {
            mAnimator.start();
        }
    }
}
