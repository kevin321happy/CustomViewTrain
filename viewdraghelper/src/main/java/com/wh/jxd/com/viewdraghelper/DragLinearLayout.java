package com.wh.jxd.com.viewdraghelper;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.LinearLayout;

/**
 * Created by kevin321vip on 2018/1/3.
 * 可自由拖拽的LinearLayout
 */

public class DragLinearLayout extends LinearLayout {

    private ViewDragHelper mViewDragHelper;

    public DragLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViewDragHelper();
    }

    //初始化ViewDragHelper
    private void initViewDragHelper() {
        mViewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return true;
            }

            //处理水平方向的越界
            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                int fixedLeft;
                View parent = (View) child.getParent();
                int leftBound = parent.getPaddingLeft();
                int rightBound = parent.getWidth() - child.getWidth() - parent.getPaddingRight();

                if (left < leftBound) {
                    fixedLeft = leftBound;
                } else if (left > rightBound) {
                    fixedLeft = rightBound;
                } else {
                    fixedLeft = left;
                }
                return fixedLeft;
            }

            //处理垂直方向的越界
            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                int fixedTop;
                View parent = (View) child.getParent();
                int topBound = getPaddingTop();
                int bottomBound = getHeight() - child.getHeight() - parent.getPaddingBottom();
                if (top < topBound) {
                    fixedTop = topBound;
                } else if (top > bottomBound) {
                    fixedTop = bottomBound;
                } else {
                    fixedTop = top;
                }
                return fixedTop;
            }

            //监听拖动状态的改变
            @Override
            public void onViewDragStateChanged(int state) {
                super.onViewDragStateChanged(state);
                switch (state) {
                    case ViewDragHelper.STATE_DRAGGING:
                        System.out.println("STATE_DRAGGING");
                        break;
                    case ViewDragHelper.STATE_IDLE:
                        System.out.println("STATE_IDLE");
                        break;
                    case ViewDragHelper.STATE_SETTLING:
                        System.out.println("STATE_SETTLING");
                        break;
                }
            }

            //捕获View
            @Override
            public void onViewCaptured(View capturedChild, int activePointerId) {
                super.onViewCaptured(capturedChild, activePointerId);
                System.out.println("ViewCaptured");
            }

            //释放View
            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
                System.out.println("ViewReleased");
            }
        });
    }

    //将事件拦截交给ViewDragHelper处理
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }


    //将Touch事件交给ViewDragHelper处理
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        mViewDragHelper.processTouchEvent(ev);
        return true;
    }
}
