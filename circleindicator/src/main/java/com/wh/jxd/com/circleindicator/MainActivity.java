package com.wh.jxd.com.circleindicator;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.wh.jxd.com.circleindicator.indicator.CircleIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * 一个圆形的Viewpager的指示器
 */

public class MainActivity extends Activity {
    private int mPosition = 0;
    private boolean mIncrease = true;
    private CircleIndicator mIndicator;
    private CircleIndicator mIndicator1;
    private CircleIndicator mIndicator2;
    private ViewPager mView_pager;
    private List<Integer> images = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_main);

        images.add(R.drawable.m1);
        images.add(R.drawable.m2);
        images.add(R.drawable.m3);
        images.add(R.drawable.m4);
        images.add(R.drawable.m5);
        mIndicator = (CircleIndicator) findViewById(R.id.indicator);
        mIndicator1 = (CircleIndicator) findViewById(R.id.indicator1);
        mIndicator2 = (CircleIndicator) findViewById(R.id.indicator2);
        mView_pager = (ViewPager) findViewById(R.id.vp);
        ininView();
    }

    private void ininView() {
        myAdapter myAdapter = new myAdapter();
        mView_pager.setAdapter(myAdapter);

        mIndicator.setCount(images.size());
        mIndicator1.setCount(images.size());
        mIndicator2.setCount(images.size());


        mIndicator.setViewPager(mView_pager);
        mIndicator1.setViewPager(mView_pager);
        mIndicator2.setViewPager(mView_pager);
    }

    private class myAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view=View.inflate(MainActivity.this,R.layout.view_pager,null);
            ImageView iv = view.findViewById(R.id.iv);
            iv.setImageResource(images.get(position));
            container.addView(view);
            return view;


        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(((View) object));
        }
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
        mIndicator1.setSelectedPositon(mPosition);
        mIndicator2.setSelectedPositon(mPosition);


    }
}
