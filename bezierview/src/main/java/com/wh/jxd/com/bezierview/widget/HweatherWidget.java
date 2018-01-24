package com.wh.jxd.com.bezierview.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
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

    private Paint mPathpaint;
    private int mWidth;
    private int mHeight;
    private int mCPointX;
    private int mCPointY;
    private int mRadius;

    private Path mPath;
    private RectF mRectF;
    private Path mSecondPath;
    private PathMeasure mPathMeasure;
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
    private Bitmap mSunbitmap;
    private int mSize;
    private Thread mThread;
    private Path mSunPath;

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
        //绘制路径的画笔
        mPathpaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPathpaint.setAntiAlias(true);
        mPathpaint.setDither(true);
        mPathpaint.setColor(Color.GRAY);
        mPathpaint.setStrokeWidth(10);
        mPathpaint.setStyle(Paint.Style.STROKE);

        //轨迹
        mPath = new Path();
        mPathMeasure = new PathMeasure();
        mSecondPath = new Path();
        mSunPath = new Path();

        //太阳的图标
        mSunbitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_sun);
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
                Log.i("tag", "当前的mAnimatedValuevalue" + mAnimatedValuevalue);
//                postInvalidate();
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
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getWidth();
        mHeight = getHeight();
        mCPointX = mWidth / 2;
        mCPointY = mHeight;
        mRadius = mWidth / 2 - 100;
        initPathTrack();
    }

    /**
     * 初始化路线的轨迹
     */
    private void initPathTrack() {
        mPoints.clear();
        mIconPoints.clear();
        mPath.reset();
        //移动到圆弧的起点
        mPath.moveTo(mCPointX - mRadius, mCPointY + mRadius);
        for (int i = 180; i < 360; i++) {
            int pointX, pointY;
            double radians = Math.toRadians(i);
            pointX = (int) (mCPointX + mRadius * Math.cos(radians));
            pointY = (int) (mCPointY + mRadius * Math.sin(radians));
            //移动到圆上的各个点
            mPath.lineTo(pointX, pointY);
            Point point = new Point(pointX, pointY);

            mPoints.add(point);
            Point iconPoint = new Point();
            iconPoint.y = pointY;
            if (i > 270) {
                iconPoint.x = pointX - 80;
            } else {
                iconPoint.x = pointX;
            }
            mIconPoints.add(iconPoint);
        }
    }

    private void initSecondPathTrack() {
        mSecondPath.reset();
        mSecondPath.moveTo(mCPointX - mRadius, mCPointY + mRadius);
        mSize = mPoints.size();
        int currentSize = (int) (mSize * mAnimatedValuevalue);
        for (int i = 0; i < currentSize; i++) {
            Point point = mPoints.get(i);
            mSecondPath.lineTo(point.x, point.y);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPathpaint.setStyle(Paint.Style.STROKE);
        mPathpaint.setStrokeWidth(10);
        drawFirstPath(canvas);
        drawSecondPath(canvas);
        drawWeatherIcon(canvas);
    }

    /**
     * 绘制第一条路径
     *
     * @param canvas
     */
    private void drawFirstPath(Canvas canvas) {
        initPathTrack();
        mPathpaint.setColor(Color.GRAY);
        canvas.drawPath(mPath, mPathpaint);
    }

    /**
     * 绘制第二条路径
     *
     * @param canvas
     */
    private void drawSecondPath(Canvas canvas) {
        mPathpaint.setColor(Color.RED);
        initSecondPathTrack();
        canvas.drawPath(mSecondPath, mPathpaint);

    }


    /**
     * 绘制天气的图标
     *
     * @param canvas
     */
    private void drawWeatherIcon(Canvas canvas) {
        mPathpaint.setColor(Color.YELLOW);
        mPathpaint.setStyle(Paint.Style.FILL);
//        mPathpaint.setStrokeWidth(30);
        //获取当前进度下面的点
        int position = (int) (mSize * mAnimatedValuevalue);
        double radians = Math.toRadians(180 * mAnimatedValuevalue);
        //获取当前的角度
        if (position < mSize) {
            Point point = mPoints.get(position);
            Log.i("IC", "图标的点坐标:" + point.x + "，" + point.y);
            int x = (int) (point.x+Math.cos(radians));
            int y = (int) (point.y+Math.sin(radians))                                                                                                                                                                                                                                                                                                                                                                                                                                                ;
            canvas.drawCircle(x,y,40,mPathpaint);
            mPathpaint.setColor(Color.WHITE);
            canvas.drawCircle(x,y,35,mPathpaint);
        }
    }

}
