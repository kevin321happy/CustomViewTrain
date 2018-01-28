package com.wh.jxd.com.progressbar;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wh.jxd.com.progressbar.widget.HorizontalProgress;
import com.wh.jxd.com.progressbar.widget.RingProgress;

public class MainActivity extends AppCompatActivity {
    int mProgress = 0;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case 0:
                    if (mProgress < 100) {
                        mProgress++;
                        if (mRing_progress != null) {
                            mRing_progress.setProgress(mProgress);
                            mHandler.handleMessage();
                        }
                    }
                    break;

            }
        }
    };
    private RingProgress mRing_progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HorizontalProgress progress = (HorizontalProgress) findViewById(R.id.progress);
        HorizontalProgress progress2 = (HorizontalProgress) findViewById(R.id.progress2);
        HorizontalProgress progress3 = (HorizontalProgress) findViewById(R.id.progress3);
        HorizontalProgress progress4 = (HorizontalProgress) findViewById(R.id.progress4);
        mRing_progress = (RingProgress) findViewById(R.id.ring_progress);
        progress.setProgress(80);
        progress2.setProgress(60);
        progress3.setProgress(40);
        progress4.setProgress(75);

        mRing_progress.setProgress(50);
    }
}
