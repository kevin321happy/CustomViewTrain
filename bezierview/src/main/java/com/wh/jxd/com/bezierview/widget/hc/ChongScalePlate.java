package com.wh.jxd.com.bezierview.widget.hc;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;

import com.wh.jxd.com.bezierview.LineInfo;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by kevin321vip on 2018/1/23.
 * 通过动画的方式来实现了刻度盘控件
 */

public class ChongScalePlate extends View {
    /**
     * 初始的路径
     */
    private Path mPath;
    /**
     * 高亮显示的路径
     */
    private Path mDispath;
    private Paint mPaint;
    private float mStrokeWidth = 10;
    private PathMeasure mPathMeasure;
    private ValueAnimator mAnimator;
    private float mValue;
    private int mWidth;
    private int mHeight;
    /**
     * 进度值
     */
    private int mProgress = 80;
    //圆心的X，y的坐标
    private int mCPointX, mCPointY;
    //圆的半径长度
    private int mRadius;
    /**
     * 刻度线的长度
     */
    private int mScaleLineLength = 60;

    private List<LineInfo> mLineInfos = new ArrayList<>();
    private Paint mTextPaint;

    public ChongScalePlate(Context context) {
        super(context);
        init();
    }

    public ChongScalePlate(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChongScalePlate(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
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
        //圆心的X，Y的坐标
        mCPointX = mWidth / 2;
        mCPointY = mHeight / 2;
        mRadius = mWidth / 2;
        upDataPathLayout();
    }

    /**
     * 初始化Path的路径
     */
    private void upDataPathLayout() {
        mDispath.reset();
        mPath.reset();
        //最大的角度值为360
        int MAX_ANGLE_VALUE = 360;
        mLineInfos.clear();
        for (int i = 0; i < MAX_ANGLE_VALUE; i += 5) {
            //所有角度是5的倍数的圆上的点绘制一条线出来
            //定义起始点和结束点
            int startX, startY, endX, endY;
            //将角度转成弧度
            double radians = Math.toRadians(i);
            //根据三角函数求出圆上的点的坐标
            startX = (int) (mCPointX + mRadius * Math.cos(radians));
            startY = (int) (mCPointY + mRadius * Math.sin(radians));
            int len = mRadius - mScaleLineLength;
            endX = (int) (mCPointX + len * Math.cos(radians));
            endY = (int) (mCPointY + len * Math.sin(radians));
            mPath.moveTo(startX, startY);
            mPath.lineTo(endX, endY);
            mLineInfos.add(new LineInfo(startX, startY, endX, endY));
            int size = mLineInfos.size();
            //当前显示的个数
            int courrentSize = (int) (size * mValue*mProgress/100);
            for (int j = 0; j < courrentSize; j++) {
                LineInfo lineInfo = mLineInfos.get(j);
                mDispath.moveTo(lineInfo.getLineSrartX(), lineInfo.getLineSrartY());
                mDispath.lineTo(lineInfo.getLineEndX(), lineInfo.getLineEndY());
            }
        }
        //重绘制
        postInvalidate();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStrokeWidth);


        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setDither(true);
        mTextPaint.setTextSize(200);
        mTextPaint.setColor(Color.BLACK);
        //初始的Path
        mPath = new Path();
        mDispath = new Path();
        //用来测量Path
        mPathMeasure = new PathMeasure();
        initAnimation();
    }

    /**
     * 初始化动画
     * 属性动画的强大之处在于可以对任意对象的任意属性增加动画效果，
     * 并且可以自定义值的类型和变化过程（TypeEvaluator）
     * 和过渡速度（Interpolator）。
     * ValueAnimator animator = ValueAnimator.ofFloat(0f, 1.0f);
     * animator.setDuration(3000);
     * animator.setInterpolator(new LinearInterpolator());
     * animator.start();
     * ofFloat方法传递0，1参数初始化了一个ValueAnimator对象，
     * 接着设置动画播放的时间，设置变化速率为系统提供的线性变化，最后启动动画。
     * 在3000毫秒内动画float值从0线性增加到1。
     */
    private void initAnimation() {
        mAnimator = ValueAnimator.ofFloat(0, 1);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //动画的值(0-1)
                mValue = (float) animation.getAnimatedValue();
                upDataPathLayout();
                //重绘制
//                invalidate();
            }
        });
        mAnimator.setInterpolator(new DecelerateInterpolator());
        mAnimator.setDuration(4000);
        mAnimator.setRepeatMode(ValueAnimator.REVERSE);
        mAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawFirstPath(canvas);


    }

    /**
     * 绘制第二条Path
     *
     * @param canvas
     */
    private void drawSecondPath(Canvas canvas) {
//        mDispath.reset();
//        mPathMeasure.setPath(mPath, false);
//        mPathMeasure.getSegment(0.0f, mPathMeasure.getLength() * mValue, mDispath, true);
        mPaint.setColor(Color.RED);
        canvas.drawPath(mDispath, mPaint);
        drawText(canvas);
    }

    /**
     * 绘制中间的文字
     * @param canvas
     */
    private void drawText(Canvas canvas) {
        String progress=(int)mProgress*mValue+"";
        if (progress.length()>=3){
            progress=progress.substring(0,2);
        }
        mPaint.setColor(Color.BLACK);
        Rect rect = new Rect();
        mTextPaint.getTextBounds(progress,0,progress.length(),rect);
        canvas.drawText(progress,mCPointX-rect.width()/2,mCPointY+rect.height()/2,mTextPaint);
    }

    /**
     * 绘制第一条Path
     *
     * @param canvas
     */
    private void drawFirstPath(Canvas canvas) {
//        mPath.reset();
        mPaint.setColor(Color.GRAY);
        canvas.drawPath(mPath, mPaint);
        drawSecondPath(canvas);
    }
}
