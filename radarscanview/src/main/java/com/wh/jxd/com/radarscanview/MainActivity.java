package com.wh.jxd.com.radarscanview;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    private RedarScanView mRedar;
    private boolean runing = false;
    private RedarScanView mRedar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRedar = (RedarScanView) findViewById(R.id.redar_view);
        mRedar1 = (RedarScanView) findViewById(R.id.redar_view1);
    }


    public void startRun(View view) {
        runing = runing == true ? false : true;
        mRedar.setSrartRunning(runing);
        mRedar1.setSrartRunning(runing);
    }
}
