package com.wh.jxd.com.bezierview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.wh.jxd.com.bezierview.widget.CircleProgressPlate;

/**
 * Created by kevin321vip on 2018/1/22.
 */

public class ProgressPlateActivtiy extends AppCompatActivity {

    private CircleProgressPlate mProgressPlate;
    private CircleProgressPlate mProgressPlate1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_plate);
        mProgressPlate = findViewById(R.id.progress1);
        mProgressPlate1 = findViewById(R.id.progress2);

    }
    public void startProgress(View view) {
        mProgressPlate.setProgress(88);
    }

    public void startProgress1(View view) {
        mProgressPlate1.setProgress(75);

    }
}
