package com.wh.jxd.com.circleindicator.indicator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.wh.jxd.com.circleindicator.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin321vip on 2018/1/15.
 * 自定义的圆形的指示器
 */

public class CircleIndicator extends View {
    //圆点的个数,默认值3
    private int mCount = 3;
    //圆点的半径,默认值为20
    private int mRadius = 20;
    //圆点之间的间距值,默认30
    private int mSpace = 30;
    //圆环的宽度
    private int mStrokeWidth = 4;
    //选中的位置
    private int mSelectedPositon = 0;
    private Paint mPaint;
    //画笔的颜色,默认红色
    private int mPaintColor = Color.RED;
    //中间文字的画笔的颜色,
    private int mTextPaintColor = Color.RED;
    //定义一个存储指示器的容器
    private List<Indicator> mIndicators = new ArrayList<>();
    //填充的模式
    private FillMode mFillMode = FillMode.NUMBER;
    //圆环中间的填充的数字
    private String[] mNumbers = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9"};
    private String[] mLetter = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K"};
    private Paint mTextPaint;

    public CircleIndicator(Context context) {
        this(context, null);
    }

    public CircleIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttribute(context, attrs);
        init();
    }

    /**
     * 获取设置的自定义属性
     *
     * @param context
     * @param attrs
     */
    private void getAttribute(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleIndicator);
        mPaintColor = typedArray.getColor(R.styleable.CircleIndicator_indicatorColor, mPaintColor);
        mStrokeWidth = typedArray.getDimensionPixelSize(R.styleable.CircleIndicator_indicatorBorderWidth, mStrokeWidth);
        mSpace = typedArray.getDimensionPixelSize(R.styleable.CircleIndicator_indicatorSpace, mSpace);
        mRadius = typedArray.getDimensionPixelSize(R.styleable.CircleIndicator_indicatorRadius, mRadius);
        mTextPaintColor = typedArray.getColor(R.styleable.CircleIndicator_indicatorTextColor, mTextPaintColor);
        int model = typedArray.getInteger(R.styleable.CircleIndicator_indicatorFillMode, 0);
        if (model == 0) {
            mFillMode = FillMode.NONE;
        } else if (model == 1) {
            mFillMode = FillMode.NUMBER;
        } else if (model == 2) {
            mFillMode = FillMode.LETTER;
        }
        typedArray.recycle();
    }

    /**
     * 处理测量
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //计算指示器控件的宽高
        int width = (mRadius + mStrokeWidth) * 2 * mCount + mSpace * (mCount - 1);
        int height = (mRadius + mStrokeWidth) * 2 + 2 * mSpace;
        //View测量自己尺寸
        setMeasuredDimension(width, height);
        //测量每一个指示器
        measureIndicator();
    }

    /**
     * 测量每一个圆点指示器的尺寸
     */
    private void measureIndicator() {
        mIndicators.clear();
        //指示器的圆心的x的坐标
        int cx = 0;
        for (int i = 0; i < mCount; i++) {
            Indicator indicator = new Indicator();
            if (i == 0) {
                //第一个子元素的中心点即是第一个半径的长度
                cx = mRadius + mStrokeWidth;
            } else {
                //后面圆点的圆心的X坐标为上一个圆心的x+2*mRadius+mSpace
                cx += (mRadius + mStrokeWidth) * 2 + mSpace;
            }
            //确定每一个圆形指示器的圆心的位置,然后添加到集合
            indicator.cx = cx;
            indicator.cy = getMeasuredHeight() / 2;
            mIndicators.add(indicator);
        }
    }

    /**
     * 绘制指示器
     *
     * @param canvas
     */
    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        int size = mIndicators.size();
        for (int i = 0; i < size; i++) {
            Indicator indicator = mIndicators.get(i);
            if (i == mSelectedPositon) {
                //如果为选中则绘制实心圆
                mPaint.setStyle(Paint.Style.FILL);
            } else {
                //未选中则绘制空心圆
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setStrokeWidth(mStrokeWidth);
            }
            canvas.drawCircle(indicator.cx, indicator.cy, mRadius, mPaint);
            //绘制中间的文字
            String centerText = "";
            if (mFillMode == FillMode.NONE) {
                centerText = "";
            } else {
                if (i >= 0 && i <= mNumbers.length) {
                    if (mFillMode == FillMode.LETTER) {
                        centerText = mLetter[i];
                    } else {
                        centerText = mNumbers[i];
                    }
                }
            }
            //绘制圆点
            Rect rect = new Rect();
            //获取字体的边界
            mPaint.getTextBounds(centerText, 0, centerText.length(), rect);
            int textWidth = rect.width();
            int textHeight = rect.height();
            float textStartX = indicator.cx - textWidth / 2;
            float textStartY = indicator.cy + textHeight / 2;
            canvas.drawText(centerText, textStartX, textStartY, mPaint);
        }
    }

    /**
     * 初始化
     */
    private void init() {
        //绘制指示器的画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(mPaintColor);
        mPaint.setTextSize(mRadius);
        //绘制指示器中间文字的画笔
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(mRadius);
        mTextPaint.setColor(mTextPaintColor);
    }

    /**
     * 设置指示器的个数
     *
     * @param count
     */
    public void setCount(int count) {
        mCount = count;
    }

    /**
     * 设置画笔的颜色
     *
     * @param paintColor
     */
    public void setPaintColor(int paintColor) {
        mPaintColor = paintColor;
    }

    /**
     * 设置圆圈的半径
     */
    public void setRadius(int radius) {
        mRadius = radius;
    }

    /**
     * 设置圆点之间的间距的值
     */
    public void setSpace(int space) {
        mSpace = space;
    }

    /**
     * 设置选中的位置
     */
    public void setSelectedPositon(int selectedPositon) {
        mSelectedPositon = selectedPositon;
        invalidate();
    }

    /**
     * 设置填充的模式
     */
    public void setFillMode(FillMode fillMode) {
        mFillMode = fillMode;
    }

    /**
     * 定义内部类来记录每一个圆点的信息
     */
    public class Indicator {
        public float cx; // 圆心x坐标
        public float cy; // 圆心y 坐标
    }

    /**
     * 枚举类,圆点中心的填充
     */
    public enum FillMode {
        //文字
        LETTER,
        //数字
        NUMBER,
        //没有填充
        NONE
    }
}
