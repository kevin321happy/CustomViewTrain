package com.wh.jxd.com.bezierview;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.wh.jxd.com.bezierview.widget.HweatherWidget;
import com.wh.jxd.com.bezierview.widget.PullViscousView;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, NavigationView.OnNavigationItemSelectedListener {

    private LinearLayout mLl_root;
    private PullViscousView mPull_viscous_view;
    //允许下拉的最大距离为600
    private int ALLOW_PULL_MAXHEIGHT = 600;
    //触摸的起始的X点和Y点的坐标
    private float mStartX;
    private float mStartY;
    private HweatherWidget mHweatherWidget;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLl_root = (LinearLayout) findViewById(R.id.ll_root);
        mPull_viscous_view = (PullViscousView) findViewById(R.id.pull_viscousview);
        mDrawerLayout = findViewById(R.id.draw_layout);
        mHweatherWidget = (HweatherWidget) findViewById(R.id.weather);
        mNavigationView = findViewById(R.id.nv_main_navigation);

        mNavigationView.setNavigationItemSelectedListener(this);
        mLl_root.setOnTouchListener(this);
        //设置当前时间模拟早上6点
        mHweatherWidget.setCurrentTime(300);
    }

    /**
     * 根布局的触摸监听
     *
     * @param v
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartX = event.getX();
                mStartY = event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                float endY = event.getY();
                float endX = event.getX();
                //下拉的距离
                float distance = endY - mStartY;
                //下拉的高度除以定义的允许的最大高度可以得到一个进度值
                float progress = distance >= ALLOW_PULL_MAXHEIGHT ? 1 : distance / ALLOW_PULL_MAXHEIGHT;

                if (mPull_viscous_view != null) {
                    mPull_viscous_view.setProgress(progress);
                }
                return true;
            case MotionEvent.ACTION_UP:
                //当Up的时候控件回弹回去
                mPull_viscous_view.pringbBack();
                break;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.nav_one:
                intent = new Intent(this, ProgressPlateActivtiy.class);
                startActivity(intent);
                break;
            case R.id.nav_two:
                intent=new Intent(this,BezierViewActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_three:
                break;
            case R.id.nav_four:
                break;
            case R.id.nav_five:
                break;
        }
        item.setChecked(true);
        mDrawerLayout.closeDrawers();
        return true;
    }
}
