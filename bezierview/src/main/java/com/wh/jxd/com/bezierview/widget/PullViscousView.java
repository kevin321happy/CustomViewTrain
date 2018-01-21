package com.wh.jxd.com.bezierview.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import com.wh.jxd.com.bezierview.R;

/**
 * Created by kevin321vip on 2018/1/19.
 * 下拉有粘性效果的View
 */

public class PullViscousView extends View {

    private Context mContext;

    private Paint mPaint;
    /**
     * 圆的圆心坐标
     */
    private float mCirclePointX, mCirclePointY;
    /**
     * 圆的半径
     */
    private int mCircleRadius = 50;
    /**
     * 下拉的进度
     */
    private float mProgress;
    /**
     * 允许拖动的最大的高度
     */
    private int mAllowMaxHeight = 400;

    /**
     * 绘制贝塞尔的路径
     *
     * @param context
     */
    private Path mPath = new Path();

    /**
     * 绘制Path的画笔
     *
     * @param context
     */
    private Paint mPathPaint;

    /**
     * 控制点下降的高度,也是控件重心下降的高度
     *
     * @param context
     */
    private int mDownHeight = 10;

    /**
     * 控制点和结束点连线是圆的切线,这个切线角度(同时这个值也是等于结束点和圆心的连线和y轴形成的角度)
     * 最大值默认为110
     *
     * @param context
     */
    private int mTangentAngl = 105;

    /**
     * 空间在改变中不断变化的宽度
     *
     * @param context
     */
    private int mTargetWidth = 600;
    private ValueAnimator mAnimator;

    /**
     * 进度变化的插值器（由慢到快）
     *
     * @param context
     */
    private Interpolator mProgressInterpolator = new DecelerateInterpolator();
    private int mContentMargin;
    private Drawable mDrawable;
    private int mColor;

    public PullViscousView(Context context) {
        super(context);
        init(context, null);
    }


    public PullViscousView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PullViscousView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PullViscousView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    /**
     * 初始化操作
     *
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {
        this.mContext = context;
        initAttr(attrs);
        //绘制圆的画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //抗锯齿
        mPaint.setAntiAlias(true);
        //防抖动
        mPaint.setDither(true);
        //画笔为填充
        mPaint.setStyle(Paint.Style.FILL);
        //设置画笔颜色
        mPaint.setColor(getContext().getResources().getColor(R.color.colorAccent));

        //绘制贝塞尔曲线的画笔
        mPathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //抗锯齿
        mPathPaint.setAntiAlias(true);
        //防抖动
        mPathPaint.setDither(true);
        //画笔为填充
        mPathPaint.setStyle(Paint.Style.FILL);
        //设置画笔颜色
        mPathPaint.setColor(getContext().getResources().getColor(R.color.colorAccent));
    }

    /**
     * 初始化自定义属性
     *
     * @param attrs
     */
    private void initAttr(AttributeSet attrs) {
        TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.PullViscousView);
        mCircleRadius = ta.getDimensionPixelSize(R.styleable.PullViscousView_PullViewCircleRadius, 50);
        mAllowMaxHeight = ta.getDimensionPixelSize(R.styleable.PullViscousView_PullViewPullDownMaxHeight, 400);
        mDownHeight = ta.getDimensionPixelSize(R.styleable.PullViscousView_PullViewDownHeight, 10);
        mTangentAngl = ta.getInteger(R.styleable.PullViscousView_PullViewTangentAngle, 105);
        mColor = ta.getColor(R.styleable.PullViscousView_PullViewColor, Color.RED);
        mTargetWidth = ta.getDimensionPixelSize(R.styleable.PullViscousView_PullViewTargetWidth, 400);
        mDrawable = ta.getDrawable(R.styleable.PullViscousView_PullViewCentreDrawable);
        mContentMargin = ta.getDimensionPixelSize(R.styleable.PullViscousView_PullViewContentMargin, 0);
        ta.recycle();

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
        upDataPathLayout();
    }

    /**
     * 更新Path的路径
     */
    private void upDataPathLayout() {
        float progress = mProgressInterpolator.getInterpolation(mProgress);
        //获取当前的宽度和高度
        float width = getValueByLine(getWidth(), mTargetWidth, mProgress);
        float height = getValueByLine(0, mAllowMaxHeight, mProgress);
        //确定圆的相关参数
        float cPonitX = width / 2;
        float cPonitY = height - mCircleRadius;
        float cRedius = mCircleRadius;
        //更新圆心的位置
        mCirclePointX = cPonitX;
        mCirclePointY = cPonitY;
        //控制点结束位置的坐标
        int endContralY = mDownHeight;
        Log.i("X", "回调进来的进度:" + progress + "圆心的坐标:" + mCirclePointX + "," + mCirclePointY);
        mPath.reset();
        mPath.moveTo(0, 0);
        //计算控制点和结束点的位置
        //左边控制点的高度
        float lControlPointX, lControlPointY;
        //左边结束点的x,y
        float lEndx, lEndy;
        //获取切线角的弧度(将角度变成弧度)
        double radians = Math.toRadians(getValueByLine(0, mTangentAngl, progress));
        //结束点的X的坐标为圆心的X坐标-半径*sin（radians）
        lEndx = (float) (cPonitX - Math.sin(radians) * cRedius);
        //结束点的Y坐标等于圆心位置的y坐标+半径*cos(cRedius)
        lEndy = (float) (cPonitY + Math.cos(radians) * cRedius);
        //控制的y坐标
        lControlPointY = getValueByLine(0, endContralY, progress);
        //控制点和结束点之前的相差的高度
        float diffHeight = lEndy - lControlPointY;
        //可以根据两点相差的高度,和切线角度求出两点之间x的差值
        float diffWidth = (float) (diffHeight / Math.tan(radians));
        //得到控制点的X坐标
        lControlPointX = lEndx - diffWidth;
        mPath.quadTo(lControlPointX, lControlPointY, lEndx, lEndy);
        //将path左象平移一段至cPonitX+(cPonitX-lEndx)
        mPath.lineTo(cPonitX + (cPonitX - lEndx), lEndy);
        //绘制右边的贝塞尔曲线
        mPath.quadTo(cPonitX + cPonitX - lControlPointX, lControlPointY, width, 0);
    }

    /**
     * 回弹的动画
     */
    public void pringbBack() {
        //通过属性动画实现
        mAnimator = ValueAnimator.ofFloat(mProgress, 0);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Object progress = animation.getAnimatedValue();
                if (progress instanceof Float) {
                    setProgress((Float) progress);
                }
            }
        });
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new DecelerateInterpolator());
        if (!mAnimator.isRunning()) {
            mAnimator.start();
        }
    }

    /**
     * 根据线性规律获得值
     */
    private float getValueByLine(float startValue, float endValue, float progress) {
        return startValue + (endValue - startValue) * progress;
    }

    /**
     * 绘制布局
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //基础坐标参数系的改变
        int save = canvas.save();
        //重新构建坐标系
        float tranX = (getWidth() - getValueByLine(getWidth(), mTargetWidth, mProgress)) / 2;
        canvas.translate(tranX, 0);
        //绘制圆
        canvas.drawCircle(mCirclePointX, mCirclePointY, mCircleRadius, mPaint);
        //绘制贝塞尔曲线
        canvas.drawPath(mPath, mPathPaint);
        //复位
        canvas.restoreToCount(save);
    }


    public void setProgress(float progress) {
        this.mProgress = progress;
//        Log.i("X", "回调进来的进度:" + progress);
        //请求重新进行测量和布局
        requestLayout();
    }
}
