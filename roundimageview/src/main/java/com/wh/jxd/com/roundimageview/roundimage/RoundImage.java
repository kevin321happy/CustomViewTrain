package com.wh.jxd.com.roundimageview.roundimage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.wh.jxd.com.roundimageview.R;

/**
 * Created by kevin321vip on 2018/1/17.
 * 圆角的ImageView
 */


@SuppressLint("AppCompatCustomView")
public class RoundImage extends ImageView {
    /**
     * 左上右下四个圆角
     */
    private int mLeftTopRadius, mRightTopRadius, mLeftButtomRadius, mRightButtomRadius = 30;
    /**
     * 四周的圆角
     */
    private int mRadius;
    /**
     * 描边的宽度
     */
    private int mStrokeWidth;
    /**
     * 画笔的颜色
     */
    private int mStrokeColor = Color.BLACK;
    /**
     * 是否为圆
     */
    private boolean mCyclo = false;
    /**
     * 绘制图形的画笔
     */
    private Paint mPaint;
    /**
     * 控件的宽高
     */
    private int mWidthSize, mHeightSize;
    /**
     * 矩阵
     */
    private Matrix mMatrix = new Matrix();
    /**
     * 放大参数
     */
    private float mScale;
    /**
     * 绘制圆角矩形的Path
     */
    private Path mPath;
    /**
     * 圆角矩形的顶点的圆角值的数组
     */
    private float[] mFloats = new float[8];
    /**
     * 绘制椭圆的RectF
     */
    private RectF mOvalrectF;
    /**
     * 描边的宽度
     */
    private Paint mStrokePaint;
    /**
     * 绘制椭圆描边的RectF
     */
    private RectF mStrokeOvalrectF;

    public RoundImage(Context context) {
        this(context, null);
    }

    public RoundImage(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImage(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
        init();
    }

    /**
     * 初始化自定义属性
     *
     * @param context
     * @param attrs
     */
    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RoundImage);
        mLeftTopRadius = ta.getDimensionPixelSize(R.styleable.RoundImage_roundImageLeftTopRadius, 0);
        mRightTopRadius = ta.getDimensionPixelSize(R.styleable.RoundImage_roundImageRightTopRadius, 0);
        mLeftButtomRadius = ta.getDimensionPixelSize(R.styleable.RoundImage_roundImageLeftBottomRadius, 0);
        mRightButtomRadius = ta.getDimensionPixelSize(R.styleable.RoundImage_roundImageRightButtomRadius, 0);
        mRadius = ta.getDimensionPixelSize(R.styleable.RoundImage_roundImageRadius, 0);
        mStrokeColor = ta.getColor(R.styleable.RoundImage_roundImageStrokeColor, Color.RED);
        mStrokeWidth = ta.getDimensionPixelSize(R.styleable.RoundImage_roundImageStrokeWidth, 2);
        mCyclo = ta.getBoolean(R.styleable.RoundImage_roundImageCyclo, false);
        ta.recycle();
        initRadius();
    }

    /**
     * 初始化圆角的参数
     */
    private void initRadius() {
        if (mRadius != 0) {
            //如果设置了统一的四周圆角
            mLeftTopRadius = mRightTopRadius = mLeftButtomRadius = mRightButtomRadius = mRadius;
        }
        //左上
        mFloats[0] = mLeftTopRadius;
        mFloats[1] = mLeftTopRadius;
        //右上
        mFloats[2] = mRightTopRadius;
        mFloats[3] = mRightTopRadius;
        //右下
        mFloats[4] = mRightButtomRadius;
        mFloats[5] = mRightButtomRadius;
        //左下
        mFloats[6] = mLeftButtomRadius;
        mFloats[7] = mLeftButtomRadius;
        //顺时针刚好构成一个圆角矩形
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(mStrokeColor);
        mStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mStrokePaint.setColor(mStrokeColor);
        mStrokePaint.setStrokeWidth(mStrokeWidth);
        mStrokePaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidthSize = MeasureSpec.getSize(widthMeasureSpec);
        mHeightSize = MeasureSpec.getSize(heightMeasureSpec);
        //测量自己的宽高，加上了描边的宽度
        setMeasuredDimension(mWidthSize + 4 * mStrokeWidth, mHeightSize + 4 * mStrokeWidth);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        //注意；这里如果不注释这行代码，会出现圆形覆盖绘制在正方形的ImageView上,因为先执行了背景的绘制了
//        super.onDraw(canvas);
        //获取Image自身的Bitmap作为Canvas的画布
        Bitmap bitmap = ((BitmapDrawable) this.getDrawable()).getBitmap();
        mScale = 1.0f;
        //关键：图片尺寸和View尺寸不合要进行缩放
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mScale = Math.max(getWidth() * 1.0f / bitmap.getWidth(), getHeight()
                * 1.0f / bitmap.getHeight());
        mMatrix.setScale(mScale, mScale);
        //通过给shader设置矩阵来完成缩放
        shader.setLocalMatrix(mMatrix);
        mPaint.setShader(shader);
        //如果是圆的
        if (mCyclo) {
            //绘制圆
            if (mWidthSize == mHeightSize) {
                if (mStrokeWidth != 0) {
                    canvas.drawCircle(mWidthSize / 2, mHeightSize / 2, mWidthSize / 2 - mStrokeWidth, mPaint);
                    //绘制描边
                    canvas.drawCircle(mWidthSize / 2, mHeightSize / 2, mWidthSize / 2, mStrokePaint);
                } else {
                    //绘制无描边的圆
                    canvas.drawCircle(mWidthSize / 2, mHeightSize / 2, mWidthSize / 2, mPaint);
                }
            } else {
                if (mStrokeWidth != 0) {
                    mOvalrectF = new RectF(0 + mStrokeWidth, 0 + mStrokeWidth, mWidthSize - mStrokeWidth, mHeightSize - mStrokeWidth);
                    mStrokeOvalrectF = new RectF(0, 0, mWidthSize, mHeightSize);
                    canvas.drawOval(mOvalrectF, mPaint);
                    //绘制描边椭圆
                    canvas.drawOval(mOvalrectF, mStrokePaint);
                } else {
                    //绘制椭圆
                    mOvalrectF = new RectF(0, 0, mWidthSize, mHeightSize);
                    canvas.drawOval(mOvalrectF, mPaint);
                }

            }
        } else {
            //绘制带圆角的矩形ImageView
            RectF rectF = new RectF(0, 0, getWidth(), getHeight());
            mPath = new Path();
            mPath.addRoundRect(rectF, mFloats, Path.Direction.CCW);
            canvas.drawPath(mPath, mPaint);
            //也可以通过下面裁切的方式实现绘制出圆角
//            canvas.clipPath(mPath);
//            super.onDraw(canvas);
        }
    }

    /**
     * 对外提供属性设置
     *
     * @return
     */
    public void setRadius(int radius) {
        mRadius = radius;
        invalidate();
    }

    public void setStrokeWidth(int strokeWidth) {
        mStrokeWidth = strokeWidth;
        invalidate();
    }

    public void setStrokeColor(int strokeColor) {
        mStrokeColor = strokeColor;
        invalidate();
    }

    public void setCyclo(boolean cyclo) {
        mCyclo = cyclo;
        invalidate();
    }
}
