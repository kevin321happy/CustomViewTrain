package com.wh.jxd.com.bezierview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by kevin321vip on 2018/1/21.
 * 绘制的Demo
 */

public class PathDemo extends View {

    private Paint mSecondPaint;
    private Paint mFirstPaint;
    private Path mFirstPath;
    private Path mSecondPath;

    public PathDemo(Context context) {
        super(context);
        init();
    }


    public PathDemo(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PathDemo(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        mFirstPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFirstPaint.setColor(Color.GRAY);
        mFirstPaint.setStyle(Paint.Style.STROKE);
        mFirstPaint.setStrokeWidth(10);

        mSecondPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSecondPaint.setColor(Color.RED);
        mSecondPaint.setStyle(Paint.Style.STROKE);
        mSecondPaint.setStrokeWidth(10);

        mFirstPath = new Path();
        mSecondPath = new Path();
        new Thread(new Runnable() {
            @Override
            public void run() {
                initPath();
            }
        }).start();
    }

    private void initPath() {
        mFirstPath.lineTo(200, 200);
        mFirstPath.lineTo(200, 600);
        mFirstPath.lineTo(600, 600);
        mFirstPath.lineTo(600, 200);

        int fps = 1000;
        int count = 0;
        float startx = 0.0f, starty = 0.0f, endx = 200.0f, endy = 200.0f;
        while (true) {
            if (count == 0) {
                startx = 0.0f;
                starty = 0.0f;
                endx = 200.0f;
                endy = 200.0f;
            } else if (count == 1) {
                startx = endx;
                starty = endy;
                endx = 200.0f;
                endy = 600.0f;
            } else if (count == 2) {
                startx = endx;
                starty = endy;
                endx = 600.0f;
                endy = 600.0f;
            } else if (count == 3) {
                startx = endx;
                starty = endy;
                endx = 600.0f;
                endy = 200.0f;
            }
            count++;
            if (count > 4) {
                return;
            }
            for (int i = 0; i < fps; i++) {
                float progress = i / (float) fps;
                float x1 = getValueByLine(startx, endx, progress);
                float y1 = getValueByLine(starty, endy, progress);
                mSecondPath.lineTo(x1, y1);

//            float x2 = getValueByLine(200.0f, 200.0f, progress);
//            float y2 = getValueByLine(200.0f, 600.0f, progress);
//
//            mSecondPath.lineTo(x2,y2);

//            Log.i("test","x:"+x+"，"+"y:"+y+"当前的Progress:"+progress);
                postInvalidate();
                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }


        }


    }

    private float getValueByLine(float start, float end, float progress) {
        return start + (end - start) * progress;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mFirstPath, mFirstPaint);
        canvas.drawPath(mSecondPath, mSecondPaint);
    }
}
