package com.wh.jxd.com.bezierview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.wh.jxd.com.bezierview.widget.PullViscousView;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    private LinearLayout mLl_root;
    private PullViscousView mPull_viscous_view;
    //允许下拉的最大距离为600
    private int ALLOW_PULL_MAXHEIGHT = 600;
    //触摸的起始的X点和Y点的坐标
    private float mStartX;
    private float mStartY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLl_root = (LinearLayout) findViewById(R.id.ll_root);
        mPull_viscous_view = (PullViscousView) findViewById(R.id.pull_viscousview);
        mLl_root.setOnTouchListener(this);
    }

    /**
     * 根布局的触摸监听
     *
     * @param v
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartX = event.getX();
                mStartY = event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                float endY = event.getY();
                float endX = event.getX();
                //下拉的距离
                float distance = endY - mStartY;
                //定义一个进度
                float progress = distance >= ALLOW_PULL_MAXHEIGHT ? 1 : distance / ALLOW_PULL_MAXHEIGHT;

                if (mPull_viscous_view != null) {
                    mPull_viscous_view.setProgress(progress);
                }
                return true;
            case MotionEvent.ACTION_UP:
                mPull_viscous_view.pringbBack();
                break;
        }
        return false;
    }
}
