package com.wh.jxd.com.layoutsample;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

/**
 * 绘制五步曲
 * <p>
 * Draw the background
 * If necessary, save the canvas’ layers to prepare for fading
 * Draw view’s content
 * Draw children
 * If necessary, draw the fading edges and restore layers
 * Draw decorations (scrollbars for instance)
 * 1、DrawBackground 绘制背景
 * 2、if necessary save canvas  保存绘制的状态(特殊情况下)
 * 3、Draw contents 绘制内容  最主要的步骤
 * 4、Draw Child  绘制孩子
 * 5、restore 重置画笔的
 * 6、Draw Srcollbar 绘制滚动条  (比较少)
 */

public class MainActivity extends AppCompatActivity {

    private ImageView mIv_canvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        mIv_canvas = (ImageView) findViewById(R.id.iv_canvas);
    }



}
