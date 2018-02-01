package com.wh.jxd.com.bezierview;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.wh.jxd.com.bezierview.widget.CheckVirusView;

/**
 * Created by kevin321vip on 2018/2/1.
 */

public class CheckVirusActivity extends Activity {

    private CheckVirusView mMcheck;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkvirus);
        mMcheck = findViewById(R.id.CheckVirusView);
    }

    public void stop(View view) {
        mMcheck.stopScan();
    }
}
