package com.wh.jxd.com.viewdraghelper;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.widget.TextView;

import static android.content.ContentValues.TAG;

/**
 * Created by kevin321vip on 2018/1/3.
 * 速度追踪
 */

public class VelocityTrackerActivity extends Activity {

    private VelocityTracker mVelocityTracker;
    private Object mVelocity;
    private TextView mTv_speed;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_velocity_tracker);
        mTv_speed = (TextView) findViewById(R.id.tv_speend);
        startVelocityTracker(null);

        getVelocity();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        startVelocityTracker(event);
        int velocity = getVelocity();
        if (mTv_speed!=null){
            mTv_speed.setText("追踪到的速度为:" + velocity);
        }
        Log.d(TAG, "追踪到的速度为:" + velocity);
        if (event.getAction() == MotionEvent.ACTION_UP) {
            stopVelocityTracker();
        }
        return super.onTouchEvent(event);
    }

    /**
     * 开始追踪
     *
     * @param
     */
    private void startVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        if (event != null) {
            mVelocityTracker.addMovement(event);
        }

    }

    /**
     * 获取速度
     *
     * @return
     */
    public int getVelocity() {
        //设置VelocityTracker的单位,1000表示1S内运动的像素
        mVelocityTracker.computeCurrentVelocity(1000);
        //获取1S内X方向的像素
        int xVelocity = (int) mVelocityTracker.getXVelocity();
        return Math.abs(xVelocity);
    }

    /**
     * 停止追总
     */
    public void stopVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }
}
