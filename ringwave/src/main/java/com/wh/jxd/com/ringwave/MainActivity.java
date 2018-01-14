package com.wh.jxd.com.ringwave;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.wh.jxd.com.ringwave.widget.RingWaveView;

public class MainActivity extends AppCompatActivity {

    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        mButton = findViewById(R.id.bt);

        RingWaveView ringWaveView = new RingWaveView(this);
    }
}
