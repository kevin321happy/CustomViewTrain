package com.wh.jxd.com.bezierview.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;


import com.wh.jxd.com.bezierview.LineInfo;
import com.wh.jxd.com.bezierview.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by kevin321vip on 2018/1/22.
 * 圆形进度盘控件
 */

public class CircleProgressPlate extends View {
    private Paint mPaint;
    private int mRadius;
    private int mWidth;
    private int mHeight;
    /**
     * 刻度笔的宽度
     */
    private int mStrokeWidth = 10;
    /**
     * 用来记录线的信息
     */
    private List<LineInfo> mLineInfos = new ArrayList<>();
    /**
     * 线的长度
     */
    private int mLineLength = 40;
    /**
     * 圆心的X,y坐标
     */
    private int mCPointX, mCPonitY;
    //底色的线
    private Path mPath;
    //高亮的线
    private Path mHeightLightPath;
    /**
     * 外圈环的颜色
     */
    private int mOutRingColor;
    /**
     * 刻度线的颜色
     */
    private int mScaleLineColor;
    /**
     * 绘制文字的画笔
     */
    private Paint mTextPaint;
    /**
     * 进度
     */
    private int mProgress = 0;
    /**
     * 绘制的文字的颜色
     */
    private int mTextColor;
    /**
     * 设置扫描图像的坐标矩阵
     */
    Matrix matrix = new Matrix();
    /**
     * 扫描的角度
     */
    private float rotate;
    private SweepGradient mShader;
    /**
     * 渐变色
     */
    private int mShaderColor = Color.LTGRAY;
    /**
     * 绘制扫描的画笔
     */
    private Paint mGradientPaint;
    /**
     * 是否需要扫描的效果
     */
    private boolean mShouldScan = false;
    /**
     * 扫描的颜色
     */
    private int mScanColor = Color.GRAY;
    private ValueAnimator mProgressAnimator;
    /**
     * 动画的进度值 0-1之间
     */
    private float mAnimatedValue;
    /**
     * 扫描的动画
     */
    private ValueAnimator mScanAnimation;
    /**
     * 扫描的透明度
     */
    private int mScanAlpha = 100;
    /**
     * 进度的动画插值器
     */
    private Interpolator mProgressInterpolator = new AccelerateDecelerateInterpolator();
    ;
    /**
     * 扫描的动画插值器
     */
    private Interpolator mScanInterpolator = new AccelerateDecelerateInterpolator();
    ;

    public CircleProgressPlate(Context context) {
        super(context);
        init(context, null);
    }

    public CircleProgressPlate(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircleProgressPlate(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
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
        int min = Math.min(mWidth, mHeight);
        mRadius = min / 2 - mStrokeWidth - 30;
        mCPointX = mWidth / 2;
        mCPonitY = mHeight / 2;
        //最初版
//        initPathLayout();
        //改进版
        upDataPathLayout();
    }

    /**
     * 初始化
     *
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {
        getAttr(context, attrs);
        //绘制刻度底色的画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        //底色刻度的路径
        mPath = new Path();
        //高亮刻度的路径
        mHeightLightPath = new Path();
        //绘制中间进度的画笔
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setDither(true);
        //文字的画笔
        mTextPaint.setTextSize(mRadius / 3);
        mTextPaint.setColor(mTextColor);
        //绘制扫描的画笔
        mGradientPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mGradientPaint.setStyle(Paint.Style.STROKE);
        mGradientPaint.setStrokeWidth(mLineLength);
        //如果设置了需要扫描
        if (mShouldScan) {
            initScanAnimation();
        }
    }

    /**
     * 初始化扫描的动画
     */
    private void initScanAnimation() {
        mScanAnimation = ValueAnimator.ofInt(0, 360);
        mScanAnimation.setDuration(1500);
        //开始和结束慢中间快
        mScanAnimation.setInterpolator(mScanInterpolator);
        mScanAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //添加动画插值
                rotate = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        mScanAnimation.setRepeatMode(ValueAnimator.RESTART);
        mScanAnimation.setRepeatCount(1000);
        mScanAnimation.start();
    }

    /**
     * 初始化进度的动画
     * 通过属性动画的方式来实现进度的线速变化
     */
    private void initProgressAnimation() {
        mProgressAnimator = ValueAnimator.ofFloat(0, 1);
        mProgressAnimator.setInterpolator(mProgressInterpolator);
        //动画的时间也是随着进度的值变化的
        mProgressAnimator.setDuration(300000 / mProgress);
        mProgressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimatedValue = (float) animation.getAnimatedValue();
                upDataPathLayout();
            }
        });
        mProgressAnimator.start();
    }


    /**
     * 设置进度的动画插值器
     *
     * @param progressInterpolator
     */
    public void setProgressInterpolator(Interpolator progressInterpolator) {
        mProgressInterpolator = progressInterpolator;
    }

    /**
     * 获取自定义属性
     *
     * @param context
     * @param attrs
     */
    private void getAttr(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressPlate);
        mLineLength = ta.getDimensionPixelSize(R.styleable.CircleProgressPlate_PlatScaleLngth, 40);
        mStrokeWidth = ta.getDimensionPixelSize(R.styleable.CircleProgressPlate_PlatStrokeWidth, 10);
        mOutRingColor = ta.getColor(R.styleable.CircleProgressPlate_PlatOutCircleColor, Color.parseColor("#f5f5f5"));
        mScaleLineColor = ta.getColor(R.styleable.CircleProgressPlate_PlatScaleColor, Color.parseColor("#3561e5"));
        mTextColor = ta.getColor(R.styleable.CircleProgressPlate_PlatTextColr, Color.BLACK);
        mScanColor = ta.getColor(R.styleable.CircleProgressPlate_PlatScanColor, Color.GRAY);
        mShouldScan = ta.getBoolean(R.styleable.CircleProgressPlate_PlatShouldScan, false);
        ta.recycle();
    }

    /**
     * 改进版的
     * 更新Path
     */
    private void upDataPathLayout() {
        //重置Path
        mHeightLightPath.reset();
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
            startY = (int) (mCPonitY + mRadius * Math.sin(radians));
            int len = mRadius - mLineLength;
            endX = (int) (mCPointX + len * Math.cos(radians));
            endY = (int) (mCPonitY + len * Math.sin(radians));
            mPath.moveTo(startX, startY);
            mPath.lineTo(endX, endY);
            mLineInfos.add(new LineInfo(startX, startY, endX, endY));
            int size = mLineInfos.size();
            //当前显示的个数
            int courrentSize = (int) (size * mAnimatedValue * mProgress / 100);
            for (int j = 0; j < courrentSize; j++) {
                LineInfo lineInfo = mLineInfos.get(j);
                mHeightLightPath.moveTo(lineInfo.getLineSrartX(), lineInfo.getLineSrartY());
                mHeightLightPath.lineTo(lineInfo.getLineEndX(), lineInfo.getLineEndY());
            }
        }
        //重绘制
        postInvalidate();
    }
    /**
     * 原始版
     * 初始化Path的路径
     */
//    private void initPathLayout() {
//        if (mUpDatathread == null) {
//            mUpDatathread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    upDataPathByProgress();
//                }
//            });
//        }
//        mUpDatathread.start();
//    }

    /**
     * 根据进度来更新Path
     * 原始版算法
     */
//    private void upDataPathByProgress() {
//        if (mLineInfos.size() > 0) {
//            mLineInfos.clear();
//        }
//        int MaximumAngle = 360;
//        //移动线到角度为0的位置
//        mPath.moveTo(mWidth, mHeight / 2);
//        /**
//         *循环确定每一条线的路径
//         */
//        for (int i = 0; i < MaximumAngle; i++) {
//            //当角度为5的倍数的点绘制直线
//            if (i % 5 == 0) {
//                //将当前的角度转换为弧度(-90度保证其实点在正上方)
//                double radians = Math.toRadians(i - 90);
//                //有圆心坐标求出圆上的目标点的坐标
//                //目标点的X,Y的开始和介绍点左边
//                int lineSrartX, lineSrartY, lineEndX, lineEndY;
//                //根据半径和角度,计算出圆上直线起点的坐标
//                lineSrartX = (int) (mCPointX + (mRadius - mStrokeWidth) * Math.cos(radians));
//                lineSrartY = (int) (mCPonitY + (mRadius - mStrokeWidth) * Math.sin(radians));
//                //结束点和起点都在起点和圆心的连线上,所以计算方式类似，
//                //半径的长度-要画的线的长度，得到新的半径
//                int len = mRadius - mLineLength;
//                lineEndX = (int) (mCPointX + len * Math.cos(radians));
//                lineEndY = (int) (mCPonitY + len * Math.sin(radians));
//                //将所有线添加到集合当中
//                mLineInfos.add(new LineInfo(lineSrartX, lineSrartY, lineEndX, lineEndY));
//                //path移动到新的起点
//                mPath.moveTo(lineSrartX, lineSrartY);
//                //绘制到终点
//                mPath.lineTo(lineEndX, lineEndY);
//            }
//        }
//        int size = mLineInfos.size();
//        //如果进度>0才计算进度path的路径
//        if (mProgress > 0) {
//            //根据进度计算出需要高亮显示的点的个数及mlineinfos中需要显示的点的个数
//            mCurrentSize = size * mProgress / 100;
//            for (int i = 0; i < mProgress; i++) {
//                //TODO 当前的进度值,这里存在一个小问题进度的文字的绘制和进度刻度的绘制不同步
//                mCurrentProgress = i + 1;
//                LineInfo line;
//                //如果i大于了集合的长度，就直接去集合的最后元素的线对象
//                if (i > mCurrentSize) {
//                    line = mLineInfos.get(mCurrentSize);
//                } else {
//                    line = mLineInfos.get(i);
//                }
//                //取出每个线段的对象,然后path去确定路径
//                mHeightLightPath.moveTo(line.getLineSrartX(), line.getLineSrartY());
//                mHeightLightPath.lineTo(line.getLineEndX(), line.getLineEndY());
//                //请求重新绘制
//                postInvalidate();
//                //线程休眠50毫秒,保证绘制的动态效果
//                try {
//                    Thread.sleep(50);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

    /**
     * 开始绘制
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //如果需要扫描,才去绘制扫描的环
        if (mShouldScan) {
            drawScan(canvas);
        }
        drawCircle(canvas);
        drawBgScalePath(canvas);
    }

    /**
     * 绘制背景的刻度线
     *
     * @param canvas
     */
    private void drawBgScalePath(Canvas canvas) {
        //绘制底层的线
        mPaint.setColor(Color.GRAY);
        mPaint.setStrokeWidth(mStrokeWidth / 2);
        canvas.drawPath(mPath, mPaint);
        //绘制进度部分刻度和文字
        upDataProgress(canvas);
    }

    /**
     * 绘制外层的圆环
     *
     * @param canvas
     */
    private void drawCircle(Canvas canvas) {
        //绘制圆
        mPaint.setColor(mOutRingColor);
        mPaint.setStrokeWidth(30);
        canvas.drawCircle(mCPointX, mCPonitY, mRadius + 20, mPaint);
    }

    /**
     * 绘制扫描的效果
     *
     * @param canvas
     */
    private void drawScan(Canvas canvas) {
        //如果开始设置了进度,则通过设置画笔透明取消扫描的效果
        if (mProgress > 0) {
            mShaderColor = Color.TRANSPARENT;
            //绘制扫描效果
            mGradientPaint.setColor(Color.TRANSPARENT);
            canvas.drawCircle(mCPointX, mCPonitY, mRadius - mLineLength / 2 - 5, mGradientPaint);
        } else {
            //设置透明度
            mGradientPaint.setAlpha(mScanAlpha);
            mScanAlpha -= 5;
            if (mScanAlpha < 0) {
                mScanAlpha = 100;
            }
            //绘制雷达扫描的效果
            matrix.setRotate(rotate, mCPointX, mCPonitY);
            if (mShader == null) {
                //渐变效果
                mShader = new SweepGradient(mCPointX, mCPonitY, Color.TRANSPARENT, mShaderColor);
            }
            mShader.setLocalMatrix(matrix);
            mGradientPaint.setShader(mShader);
            //绘制扫描效果
            canvas.drawCircle(mCPointX, mCPonitY, mRadius - mLineLength / 2 - 5, mGradientPaint);
        }
    }

    /**
     * 更新进度和中间的文字
     */
    public void upDataProgress(Canvas canvas) {
        mPaint.setColor(mScaleLineColor);
        mPaint.setStrokeWidth(mStrokeWidth);
        canvas.drawPath(mHeightLightPath, mPaint);
        mTextPaint.setTextSize(mRadius / 2);
        Log.i("Plat", "控件的圆心 :" + mCPointX + "," + mCPonitY + "半径 ：" + mRadius);
        Rect rect = new Rect();
//        mCurrentProgress=mPorgress
//        String progress = mCurrentProgress + "";
        //改进版
        String progress = String.valueOf(mProgress * mAnimatedValue);
        progress = checkOut(progress);
        mTextPaint.getTextBounds(progress, 0, progress.length(), rect);
        canvas.drawText(progress, mCPointX - rect.width() / 2 - 20, mCPonitY + rect.height() / 2, mTextPaint);
        mTextPaint.setTextSize(mRadius / 4);
        canvas.drawText("分", mCPointX + mWidth / 6, mCPonitY + rect.height() / 2, mTextPaint);
    }

    /**
     * 对进度值进行规范
     *
     * @param progress
     * @return
     */
    private String checkOut(String progress) {
        //对最后绘制的进度做长度的截取
        if (progress.length() > 2) {
            if (mProgress == 100) {
                progress = String.valueOf(100);
            } else if (mProgress * mAnimatedValue < 10) {
                progress = progress.substring(0, 1);
            } else {
                progress = progress.substring(0, 2);
            }
        }
        return progress;
    }

    /**
     * 设置进度
     *
     * @param progress
     */
    public void setProgress(int progress) {
        this.mProgress = progress;
        //原始版
//        mHeightLightPath.reset();
//        initPathLayout();
        //改进版通过属性动画来动态的绘制进度
        initProgressAnimation();
        //如果扫描动画在跑怎么取消扫描动画
        if (mScanAnimation != null && mScanAnimation.isRunning()) {
            mScanAnimation.cancel();
        }
    }

    /**
     * 相关属性的设置
     *
     * @param
     */
    public void setStrokeWidth(int strokeWidth) {
        mStrokeWidth = strokeWidth;
    }

    public void setLineLength(int lineLength) {
        mLineLength = lineLength;
    }

    public void setScaleLineColor(int scaleLineColor) {
        mScaleLineColor = scaleLineColor;
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
    }
}
