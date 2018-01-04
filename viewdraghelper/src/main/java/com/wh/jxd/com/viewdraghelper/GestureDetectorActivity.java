package com.wh.jxd.com.viewdraghelper;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

/**
 * Created by kevin321vip on 2018/1/3.
 */

public class GestureDetectorActivity extends Activity {

    private String TAG="GestureDetectorActivity";
    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_detector);
        init();


    }

    /**
     * 初始化
     */
    private void init() {
        //构造手势识别器对象
        mGestureDetector = new GestureDetector(this, new GestureListenerImpl());

    }

    /**
     * 重写OnTouch方法,讲Touch事件交给mGestureDetector
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    //实现手势的监听
    public class GestureListenerImpl implements GestureDetector.OnGestureListener{


        @Override
        public boolean onDown(MotionEvent e) {
            Toast.makeText(GestureDetectorActivity.this, "按下手势", Toast.LENGTH_SHORT).show();
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            Log.d(TAG,"压下");

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Log.d(TAG,"单击");
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.d(TAG,"滚动");
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.d(TAG,"长按");

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.d(TAG,"Fling");
            return false;
        }
    }
}
