package com.wh.jxd.com.flowviewsample;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    String[] tags = new String[]{"女朋友", "贤良淑德", "赞", "年轻美貌", "清纯", "温柔贤惠", "靓丽", "女神"};
    private MainActivity mContext;
    private ClickListenerImpl mClickListenerImpl;
    private ImageView mImageView;
    private KFlowView mFlowLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }
    private void init(){
        mContext=this;
        mImageView= (ImageView) findViewById(R.id.imageView);
        mClickListenerImpl=new ClickListenerImpl();
        mImageView.setOnClickListener(mClickListenerImpl);
        mFlowLayout= (KFlowView) findViewById(R.id.flowlayout);
    }

    private class ClickListenerImpl implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            int textViewHeight = 80;
            ViewGroup.MarginLayoutParams marginLayoutParams =
                    new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, textViewHeight);
            marginLayoutParams.setMargins(30, 0, 30, 0);
            TextView textView = new TextView(mContext);
            textView.setLines(1);
            textView.setTextSize(20);
            textView.setPadding(25, 0, 25, 0);
            textView.setTextColor(Color.parseColor("#f58f98"));
            textView.setGravity(Gravity.CENTER);
            int index = (int)(Math.random() * tags.length);
            textView.setText(tags[index]);
            textView.setBackgroundResource(R.drawable.textview_backgroundresource);
            mFlowLayout.addView(textView, marginLayoutParams);

        }
    }

}
