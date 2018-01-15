package com.wh.jxd.com.circleindicator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.wh.jxd.com.circleindicator.indicator.CircleIndicator;

/**
 * 一个圆形的Viewpager的指示器
 */

public class MainActivity extends AppCompatActivity {
    private int mPosition = 0;
    private boolean mIncrease = true;
    private CircleIndicator mIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mIndicator = (CircleIndicator) findViewById(R.id.indicator);
    }

    /**
     * 改变指示器
     *
     * @param view
     */
    public void ChangePosition(View view) {
        if (mIncrease) {
            mPosition++;
            if (mPosition == 2) {
                mIncrease = false;
            }
        } else {
            mPosition--;
            if (mPosition == 0) {
                mIncrease = true;
            }
        }
        mIndicator.setSelectedPositon(mPosition);


    }
}
