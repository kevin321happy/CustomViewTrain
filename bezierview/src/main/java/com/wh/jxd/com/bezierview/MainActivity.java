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

public class MainActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {


    private HweatherWidget mHweatherWidget;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = findViewById(R.id.draw_layout);
        mHweatherWidget = (HweatherWidget) findViewById(R.id.weather);
        mNavigationView = findViewById(R.id.nv_main_navigation);

        mNavigationView.setNavigationItemSelectedListener(this);

        //设置当前时间模拟早上6点
        mHweatherWidget.setCurrentTime(300);
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
                intent=new Intent(this,PullViscousActivity.class);
                startActivity(intent);
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
