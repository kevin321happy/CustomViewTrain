package com.wh.jxd.com.sidemenuview.sidemenu;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by kevin321vip on 2018/1/19.
 * 侧滑菜单
 */

public class SideMenu extends FrameLayout {

    private View mContentView;
    private View mMenuView;
    private int mContentWidth;
    private int mContentWidth1;
    private int mContentHeight;

    public SideMenu(@NonNull Context context) {
        this(context, null);
    }

    public SideMenu(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mContentView != null && mMenuView != null) {
            mContentWidth1 = mContentView.getMeasuredWidth();
            mContentHeight = mContentView.getMeasuredHeight();
            int measuredWidth = mMenuView.getMeasuredWidth();
        }

    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    /**
     * 当布局加载完毕
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        if (childCount == 2) {
            mContentView = getChildAt(0);
            mMenuView = getChildAt(1);
        }
    }

    /**
     * 触摸监听
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
