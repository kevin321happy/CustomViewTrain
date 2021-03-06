package com.wh.jxd.com.bezierview;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.wh.jxd.com.bezierview.widget.PullViscousView;

/**
 * Created by kevin321vip on 2018/1/31.
 */

public class PullViscousActivity extends Activity implements View.OnTouchListener {
    private LinearLayout mLl_root;

    private PullViscousView mPull_viscous_view;
    //允许下拉的最大距离为600
    private int ALLOW_PULL_MAXHEIGHT = 600;
    //触摸的起始的X点和Y点的坐标
    private float mStartX;
    private float mStartY;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_viscous);
        mLl_root = (LinearLayout) findViewById(R.id.ll_pull_root);
        mPull_viscous_view = (PullViscousView) findViewById(R.id.pull_viscous);
        mLl_root.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartX = event.getX();
                mStartY = event.getY();

            case MotionEvent.ACTION_MOVE:

                float endY = event.getY();
                float endX = event.getX();
                //下拉的距离
                float distance = endY - mStartY;
                //下拉的高度除以定义的允许的最大高度可以得到一个进度值
                float progress = distance >= ALLOW_PULL_MAXHEIGHT ? 1 : distance / ALLOW_PULL_MAXHEIGHT;

                if (mPull_viscous_view != null) {
                    mPull_viscous_view.setProgress(progress);
                }
                return true;


            case MotionEvent.ACTION_UP:
               //当Up的时候控件回弹回去
                mPull_viscous_view.pringbBack();
                break;


        }
        return false;
    }

//    /**
//     * 根布局的触摸监听
//     *
//     * @param v
//     * @param event
//     * @return
//     */
//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                mStartX = event.getX();
//                mStartY = event.getY();
//
//            case MotionEvent.ACTION_MOVE:
//                float endY = event.getY();
//                float endX = event.getX();
//                //下拉的距离
//                float distance = endY - mStartY;
//                //下拉的高度除以定义的允许的最大高度可以得到一个进度值
//                float progress = distance >= ALLOW_PULL_MAXHEIGHT ? 1 : distance / ALLOW_PULL_MAXHEIGHT;
//
//                if (mPull_viscous_view != null) {
//                    mPull_viscous_view.setProgress(progress);
//                }
//                return true;
//            case MotionEvent.ACTION_UP:
//                //当Up的时候控件回弹回去
//                mPull_viscous_view.pringbBack();
//                break;
//        }
//        return true;
//    }
}
