package com.wh.jxd.com.progressbar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wh.jxd.com.progressbar.widget.HorizontalProgress;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HorizontalProgress progress = (HorizontalProgress) findViewById(R.id.progress);
        progress.setProgress(80);
    }
}
