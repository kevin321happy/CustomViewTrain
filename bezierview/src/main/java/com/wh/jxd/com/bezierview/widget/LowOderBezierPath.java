package com.wh.jxd.com.bezierview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by kevin321vip on 2018/1/20.
 * 低阶的贝塞尔曲线
 * 1~3阶的贝塞尔曲线
 */

public class LowOderBezierPath extends View {

    private Paint mPaint;
    private Path mPath;

    public LowOderBezierPath(Context context) {
        super(context);
        init();
    }

    public LowOderBezierPath(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LowOderBezierPath(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(5);
        mPaint.setStyle(Paint.Style.STROKE);

        mPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //一阶贝塞尔
        mPath.moveTo(200, 200);
        mPath.lineTo(500, 400);
        //二阶贝塞尔
        /**
         * 绝对绘制
         * 控制钱的坐标和结束点的坐标
         */
//        mPath.quadTo(600,0,800,400);
        /**
         * 相对位置绘制
         * 600相对500是100,0相对400是-400,800相对500是300,400相对400是0
         * 所以控制点相对的x ，y 为100,-400   结束点的相对 x ，y是 300,0
         */
        mPath.rQuadTo(100,-400,300,0);

        //三阶贝塞尔
        mPath.moveTo(400,800);
        /**
         * 控制点1  x,y坐标
         * 控制点2 x,y坐标
         * 结束点的 x,y坐标
         */
        mPath.cubicTo(500,400,700,1200,1000,800);
        /**
         * 相对位置绘制发,各个点的x，y值分别相对起始点的，y的值
        */
        mPath.moveTo(400,1000);

        mPath.rCubicTo(100,-400,300,400,600,0);

        canvas.drawPath(mPath,mPaint);

    }
}
