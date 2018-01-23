package com.wh.jxd.com.radarscanview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;


/**
 * Created by kevin321vip on 2018/1/16.
 */

public class RedarScanView extends View implements Runnable {
    //圆的半径
    private int mRadius;
    //圆圈的颜色
    private int mCircleColor;
    //中间文字的颜色
    private int mTextColor;
    //中间文字的大小
    private int mTextSize;
    //扫描的颜色
    private int mShaderColor;
    //圆心的x,y的坐标
    private int mCx;
    private int mCy;
    private int mWidthSize;
    private int mHeightSize;
    //画圆的画笔
    private Paint mCirclePaint;
    private int mCircleCount = 4;
    private Paint mGradientPaint;
    private int rotate = 0;
    //是否开始
    private boolean Running = false;
    //渐变颜色
    int[] mColors = new int[]{
            Color.parseColor("#FF00C717"), Color.parseColor("#FF00C75D"), Color.parseColor("#00C799")};
    //设置扫描图像的坐标矩阵
    Matrix matrix = new Matrix();
    //用于绘制扫描图像
    Shader mShader;
    private Paint mTextPaint;
    private Rect mBound;

    public RedarScanView(Context context) {
        this(context, null);
    }

    public RedarScanView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RedarScanView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttr(context, attrs);
        init();
    }

    /**
     * 获取自定义属性
     *
     * @param context
     * @param attrs
     */
    private void getAttr(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RedarScanView);
        mCircleColor = ta.getColor(R.styleable.RedarScanView_redar_circlr_color, Color.BLUE);
        mTextColor = ta.getColor(R.styleable.RedarScanView_redar_text_color, Color.RED);
        mTextSize = ta.getDimensionPixelSize(R.styleable.RedarScanView_redar_text_size, 40);
        mShaderColor = ta.getColor(R.styleable.RedarScanView_redar_shader_color, Color.BLUE);
        ta.recycle();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidthSize = MeasureSpec.getSize(widthMeasureSpec);
        mHeightSize = MeasureSpec.getSize(heightMeasureSpec);
        mCx = mWidthSize / 2;
        mCy = mHeightSize / 2;
        mRadius = mWidthSize / 2;
    }

    /**
     * 初始化
     */
    private void init() {
        //绘制圆环的画笔
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(mCircleColor);
        mCirclePaint.setStrokeWidth(2);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        //绘制扫描的画笔
        mGradientPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mGradientPaint.setStyle(Paint.Style.FILL);
//        mGradientPaint.setStrokeWidth(80);
        //中间文字的画笔
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);

    }
    /**
     * 绘制
     *
     * @param canvas
     */
    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        int space = mRadius / mCircleCount + 1;
        //绘制雷达扫描的效果
        matrix.setRotate(rotate, mCx, mCy);
        if (mShader == null) {
            //渐变效果
            mShader = new SweepGradient(mCx, mCy, Color.TRANSPARENT, mShaderColor);
        }
        mShader.setLocalMatrix(matrix);
        mGradientPaint.setShader(mShader);
        //绘制扫描效果
        canvas.drawCircle(mCx, mCy, mRadius, mGradientPaint);
        //绘制中间的文字
        String centerText = rotate * 100 / 360 + "%";
        if (mBound == null) {
            mBound = new Rect();
        }
        mTextPaint.getTextBounds(centerText, 0, centerText.length(), mBound);
        int textWidth = mBound.width();
        int textHeight = mBound.height();
        //绘制文字
        canvas.drawText(centerText, mCx - textWidth / 2, mCy, mTextPaint);
        for (int i = 0; i < mCircleCount; i++) {
            //绘制圆
            int radius = mRadius - i * space;
            canvas.drawCircle(mCx, mCy, radius, mCirclePaint);
        }
    }

    /**
     * 开始动起来
     *
     * @param running
     */
    public void setSrartRunning(boolean running) {
        Running = running;
        postDelayed(this, 10);
    }

    @Override
    public void run() {
        if (Running) {
            rotate++;
            postInvalidate();
            rotate = rotate == 360 ? 0 : rotate;
            //循环调用
            postDelayed(this, 10);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //停止循环
        Running = false;
    }
}
