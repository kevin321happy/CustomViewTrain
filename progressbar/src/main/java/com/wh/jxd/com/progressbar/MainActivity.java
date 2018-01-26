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
        HorizontalProgress progress2 = (HorizontalProgress) findViewById(R.id.progress2);
        HorizontalProgress progress3 = (HorizontalProgress) findViewById(R.id.progress3);
        HorizontalProgress progress4 = (HorizontalProgress) findViewById(R.id.progress4);
        progress.setProgress(80);
        progress2.setProgress(60);
        progress3.setProgress(40);
        progress4.setProgress(75);
    }
}
