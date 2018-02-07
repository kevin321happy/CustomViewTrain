package com.wh.jxd.com.dropdownmenu.widget;

import android.content.Context;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by kevin321vip on 2018/2/4.
 * 下拉菜单控件
 */

public class DropDownMenu extends LinearLayout {
    /**
     * 顶部的Tab视图
     */
    private LinearLayout mTabLinearLayout;
    /**
     * TabView下面的分割线
     */
    private View mDivisionView;
    /**
     * 内容试图的
     */
    private FrameLayout mContentView;

    public DropDownMenu(Context context) {
        this(context, null);
    }

    public DropDownMenu(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DropDownMenu(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //设置线性布局为竖直方向
        setOrientation(VERTICAL);
        initView(context, attrs);
    }

    /**
     * 初始化控件
     *
     * @param context
     * @param attrs
     */
    private void initView(Context context, AttributeSet attrs) {
        addChildViews(context);
        getAttr(context, attrs);

    }

    /**
     * 添加子控件
     *
     * @param context
     */
    private void addChildViews(Context context) {
        //添加最上层的TabView
        mTabLinearLayout = new LinearLayout(context);
        mTabLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        addView(mTabLinearLayout, 0);
        //添加分割线
        mDivisionView = new View(context);
        mDivisionView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, px2dp(2)));
        mDivisionView.setBackgroundColor(Color.GRAY);
        addView(mDivisionView, 1);
        //添加内容视图
        mContentView = new FrameLayout(context);
        mContentView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(mContentView, 2);
        //        mContentView
    }

    /**
     * 获取自定义属性
     *
     * @param context
     * @param attrs
     */
    private void getAttr(Context context, AttributeSet attrs) {

    }

    /**
     * dp2px
     */
    public int px2dp(float Value) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, Value, displayMetrics);
    }
}
