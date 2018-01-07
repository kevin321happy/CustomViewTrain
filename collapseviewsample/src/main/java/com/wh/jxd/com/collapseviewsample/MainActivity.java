package com.wh.jxd.com.collapseviewsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private CollapseView mCollapseView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCollapseView = (CollapseView) findViewById(R.id.collapse_1);
        mCollapseView.setContentView(R.layout.expand_1);
    }
}
