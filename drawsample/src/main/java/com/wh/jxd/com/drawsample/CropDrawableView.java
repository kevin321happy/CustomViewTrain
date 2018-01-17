package com.wh.jxd.com.drawsample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by kevin321vip on 2018/1/17.
 */

public class CropDrawableView extends View {

    private Paint mPaint;
    private int mWidth;
    private int mHeight;
    //矩阵
    private Matrix mMatrix = new Matrix();

    public CropDrawableView(Context context) {
        this(context, null);
    }

    public CropDrawableView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CropDrawableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int Wsize = MeasureSpec.getSize(widthMeasureSpec);
        int Hsize = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(Wsize, Hsize);

    }

    /**
     * 绘制的顺序
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        //获取控件摆放之后的宽高位置
        mWidth = getWidth();
        mHeight = getHeight();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ym2);
        float scale = 1.0f;
        //图片尺寸和View尺寸不合要进行缩放
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
//        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        scale = Math.max(getWidth() * 1.0f / bitmap.getWidth(), getHeight()
                * 1.0f / bitmap.getHeight());
        mMatrix.setScale(scale, scale);
        //设置矩阵来完成缩放
        shader.setLocalMatrix(mMatrix);
        mPaint.setShader(shader);
        canvas.drawCircle(mWidth / 2, mHeight / 2, mWidth / 2, mPaint);
    }
}
