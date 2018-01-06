package com.wh.jxd.com.drawsample;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by kevin321vip on 2018/1/6.
 */

public class CanvasView extends View {

    private Paint mPaint;

    public CanvasView(Context context) {
        this(context,null);
    }

    public CanvasView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 初始化 画笔等操作
     */
    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(60);


    }
}
