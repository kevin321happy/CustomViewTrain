package com.wh.jxd.com.viewdraghelper;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private Button mBtn_1;
    private Button mBtn_2;
    private Button mBtn_3;
    private Button mBtn_4;
    private Button mBtn_5;
    private Button mBtn_6;
    private TextView mTv_info;
    //设备信息
    private Configuration mConfiguration;
    //自定义View的标准尺寸大小,敏感度,ui超时等
    private ViewConfiguration mViewConfiguration;
    private String ScrollInfrodecu="第二点：scroll的本质\n" +
            "scrollTo( )和scrollBy( )移动的只是View的内容，而且View的背景是不移动的。\n" +
            "\n" +
            "第三点：scrollTo( )和scrollBy( )方法的坐标说明\n" +
            "\n" +
            "比如我们对于一个TextView调用scrollTo(0,25) ；那么该TextView中的content(比如显示的文字:Hello)会怎么移动呢?\n" +
            "向下移动25个单位？不！恰好相反！！这是为什么呢?\n" +
            "因为调用该方法会导致视图重绘，即会调用\n" +
            "\n" +
            "    public void invalidate(int l, int t, int r, int b)\n" +
            "\n" +
            "此处的l,t,r,b四个参数就表示View原来的坐标.\n" +
            "在该方法中最终会调用:\n" +
            "\n" +
            "    tmpr.set(l - scrollX, t - scrollY, r - scrollX, b - scrollY);\n" +
            "    p.invalidateChild(this, tmpr);\n" +
            "\n" +
            "其中tmpr是一个Rect，this是原来的View；通过这两行代码就把View在一个Rect中重绘。\n" +
            "请注意第一行代码:\n" +
            "原来的l和r均减去了scrollX\n" +
            "原来的t和b均减去了scrollY\n" +
            "就是说scrollX如果是正值,那么重绘后的View的宽度反而减少了;反之同理\n" +
            "就是说scrollY如果是正值,那么重绘后的View的高度反而减少了;反之同理\n" +
            "所以，TextView调用scrollTo(0,25)和我们的理解相反 ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        mBtn_1 = (Button) findViewById(R.id.bt_1);
        mBtn_2 = (Button) findViewById(R.id.bt_2);
        mBtn_3 = (Button) findViewById(R.id.bt_3);
        mBtn_4 = (Button) findViewById(R.id.bt_4);
        mBtn_5 = (Button) findViewById(R.id.bt_5);
        mBtn_6 = (Button) findViewById(R.id.bt_6);
        mTv_info = (TextView) findViewById(R.id.tv_info);

        mBtn_1.setOnClickListener(this);
        mBtn_2.setOnClickListener(this);
        mBtn_3.setOnClickListener(this);
        mBtn_4.setOnClickListener(this);
        mBtn_5.setOnClickListener(this);
        mBtn_6.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_1:
                showConfiguration();
                break;
            case R.id.bt_2:
                //ViewConfirfration  判断自定义View的标准,尺寸大小,敏感度,ui超时
                showViewConfiguration();
                break;
            case R.id.bt_3:
                //手势识别器界面
                startActivity(new Intent(this,GestureDetectorActivity.class));
                break;
            case R.id.bt_4:
                //VelocityTracker
                startActivity(new Intent(this,VelocityTrackerActivity.class));
                break;
            case R.id.bt_5:
                mTv_info.setText(ScrollInfrodecu);
                break;
            case R.id.bt_6:
                startActivity(new Intent(this,ViewDragHelperActivity.class));
                break;

        }

    }

    /**
     * 显示ViewConfiguration
     *
     */
    private void showViewConfiguration() {
        mViewConfiguration = ViewConfiguration.get(this);
        //获取touchSlop(系统识别的最小是滑动距离)
        int scaledTouchSlop = mViewConfiguration.getScaledTouchSlop();
        //获取得到的Filing速度的最大值和最小值
        int minimumFlingVelocity = mViewConfiguration.getScaledMinimumFlingVelocity();
        int maximumFlingVelocity = mViewConfiguration.getScaledMaximumFlingVelocity();
        //判断是否有物理按键
        boolean menuKey = mViewConfiguration.hasPermanentMenuKey();
        mTv_info.setText("最小的滑动距离:"+scaledTouchSlop+"Filing的最大最小值:"+minimumFlingVelocity+"  "+maximumFlingVelocity+"是否有物理按键:"+menuKey);
        //常用的静态方法
        //判断双击间隔的时间
        int doubleTapTimeout = mViewConfiguration.getDoubleTapTimeout();
        //按住转变成长按的时间
        int longPressTimeout = mViewConfiguration.getLongPressTimeout();
        //重复按键的时间
        int keyRepeatTimeout = mViewConfiguration.getKeyRepeatTimeout();

    }

    /**
     * 显示设备信息
     */
    private void showConfiguration() {
        mConfiguration = getResources().getConfiguration();
        //国家码
        int mcc = mConfiguration.mcc;
        //网络码
        int mnc = mConfiguration.mnc;
        //横竖屏
        int orientation = mConfiguration.orientation;
        mTv_info.setText("国家码：" + mcc + "网络码：" + mnc + "横竖屏：" + orientation);

    }


}
