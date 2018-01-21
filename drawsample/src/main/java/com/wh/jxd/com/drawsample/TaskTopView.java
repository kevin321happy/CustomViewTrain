package com.wh.jxd.com.drawsample;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by kevin321vip on 2018/1/20.
 * 任务界面的上层界面
 */

public class TaskTopView extends View {

    private Paint mCirclePaint;
    /**
     * 第一次绘制的标志
     */
    private boolean FIRST_DRAW_FLAGE = true;
    private Paint mTextpaint;
    /**
     * 任务的时间
     */
    private String TASK_TIME = "00:00";
    /**
     * 标题的文字
     */
    private String TITLE_TEXT = "任务重置时间";
    /**
     * 定义控件自身的宽高
     */
    private int mWidthSize;
    private int mHeightSize;

    public TaskTopView(Context context) {
        super(context);
        init();
    }

    public TaskTopView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TaskTopView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //布局的宽度
        mWidthSize = MeasureSpec.getSize(widthMeasureSpec);
        mHeightSize = 400;
        //设置控件的高度固定为400;
        setMeasuredDimension(mWidthSize, 450);
    }

    private void init() {
        //画圆的画笔
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //抗锯齿
        mCirclePaint.setAntiAlias(true);
        //抗抖动
        mCirclePaint.setDither(true);
        //实心圆
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setColor(Color.parseColor("#1EC3FD"));
        //绘制中间文字的画笔
        mTextpaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextpaint.setAntiAlias(true);
        mTextpaint.setDither(true);
        mTextpaint.setColor(Color.BLACK);
        mTextpaint.setTextSize(50);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        //蓝色的背景只用绘制一次,和下面的标题值绘制一次
        if (FIRST_DRAW_FLAGE) {
            //大圆
            canvas.drawCircle(width / 2, -height, (float) (height + width) / 2 + 80, mCirclePaint);
            mCirclePaint.setColor(Color.WHITE);
            //小圆
            canvas.drawCircle(width / 2, -250, width / 2 + 50, mCirclePaint);
            FIRST_DRAW_FLAGE = false;
            //绘制下面的标题
            Rect bound = new Rect();
            mTextpaint.getTextBounds(TITLE_TEXT, 0, TITLE_TEXT.length(), bound);
            int textWidth = bound.width();
            canvas.drawText(TITLE_TEXT, 0, TITLE_TEXT.length(), mWidthSize / 2 - textWidth / 2 + 20, mHeightSize / 2 + 30, mTextpaint);
            //改变画笔颜色
            mTextpaint.setColor(Color.BLUE);
            mTextpaint.setTextSize(60);
        }
        drawTaskTime(canvas);
    }
    /**
     * 绘制任务的时间
     *
     * @param canvas
     */
    private void drawTaskTime(Canvas canvas) {
        Rect bound = new Rect();
        //获取TextView的边框
        mTextpaint.getTextBounds(TASK_TIME, 0, TASK_TIME.length(), bound);
        int timeTextWidth = bound.width();
        canvas.drawText(TASK_TIME, 0, TASK_TIME.length(), mWidthSize / 2 - timeTextWidth / 2, mHeightSize / 3, mTextpaint);
    }
}
