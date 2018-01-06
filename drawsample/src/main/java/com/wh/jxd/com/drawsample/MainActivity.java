package com.wh.jxd.com.drawsample;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

/**
 * 绘制的样例
 */
public class MainActivity extends AppCompatActivity {

    private ImageView mIv_canvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mIv_canvas = (ImageView) findViewById(R.id.iv_canvas);
        drawBitmap();
    }
    /**
     * Canvas
     * 用什么工具画？
     * 这个小问题很简单，我们需要用一支画笔(Paint)来绘图。
     * 当然，我们可以选择不同颜色的笔，不同大小的笔。
     * 把图画在哪里呢？
     * 我们把图画在了Bitmap上，它保存了所绘图像的各个像素(pixel)。
     * 也就是说Bitmap承载和呈现了画的各种图形。
     * 画的内容？
     * 根据自己的需求画圆，画直线，画路径。
     * 怎么画？
     * 调用canvas执行绘图操作。
     * 比如，canvas.drawCircle()，canvas.drawLine()，canvas.drawPath()将我们需要的图像画出来。
     */
    private void drawBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(800, 400, Bitmap.Config.ARGB_8888);
        //Canvas必须要一个画布,来展示绘图中的像素
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.CYAN);
        //绘制的工具,抗锯齿的
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //设置画笔的宽度
        paint.setTextSize(60);
        //从左上坐标为100,300的地方开始绘制
        canvas.drawText("你好啊Canvas", 100, 300, paint);
        //将绘制好的Bitmap设置在图片上
        mIv_canvas.setImageBitmap(bitmap);
    }

}
