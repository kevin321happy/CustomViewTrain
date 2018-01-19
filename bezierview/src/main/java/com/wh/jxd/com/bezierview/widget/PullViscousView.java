package com.wh.jxd.com.bezierview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.wh.jxd.com.bezierview.R;

/**
 * Created by kevin321vip on 2018/1/19.
 * 下拉有粘性效果的View
 */

public class PullViscousView extends View {

    private Paint mPaint;
    /**
     * 圆的圆心坐标
     */
    private float mCirclePointX, mCirclePointY;
    /**
     * 圆的半径
     */
    private int mCircleRadius = 200;
    /**
     * 下拉的进度
     */
    private float mProgress;
    /**
     * 允许拖动的最大的高度
     */
    private int mAllowMaxHeight = 800;

    public PullViscousView(Context context) {
        super(context);
        init();
    }


    public PullViscousView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PullViscousView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PullViscousView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    /**
     * 测量控件
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //横向的测量模式和宽度
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        //纵向的测量模式和高度
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        //定义View最小的高度,高度是随着拉动在慢慢变大
        int iHeight = (int) ((mAllowMaxHeight * mProgress + 0.5f) + getPaddingTop() + getPaddingBottom());
        //定义控件的最小的宽度
        int iWidth = mCircleRadius * 2 + getPaddingLeft() + getPaddingRight();
        //定义变量记录新的宽高
        int newWidth;
        int newHeight;

        //得到新的宽度和高度进行重新测量
        if (widthMode == MeasureSpec.EXACTLY) {
            newWidth = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            newWidth = Math.min(iWidth, widthSize);
        } else {
            newWidth = iWidth;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            newHeight = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            newHeight = Math.min(iHeight, heightSize);
        } else {
            newHeight = iHeight;
        }
        setMeasuredDimension(newWidth, newHeight);

    }

    /**
     * 控件大小发生改变
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCirclePointX = getWidth() / 2;
        mCirclePointY = getHeight() / 2;
    }

    /**
     * 绘制布局
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制圆
        canvas.drawCircle(mCirclePointX, mCirclePointY, mCircleRadius, mPaint);
    }

    /**
     * 初始化操作
     */
    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //抗锯齿
        mPaint.setAntiAlias(true);
        //防抖动
        mPaint.setDither(true);
        //画笔为填充
        mPaint.setStyle(Paint.Style.FILL);
        //设置画笔颜色
        mPaint.setColor(getContext().getResources().getColor(R.color.colorAccent));
    }

    public void setProgress(float progress) {
        this.mProgress = progress;
        Log.i("X", "回调进来的进度:" + progress);
        //请求重新进行测量和布局
        requestLayout();
    }
}
