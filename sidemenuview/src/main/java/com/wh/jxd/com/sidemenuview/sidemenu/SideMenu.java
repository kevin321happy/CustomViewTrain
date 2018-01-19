package com.wh.jxd.com.sidemenuview.sidemenu;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import static android.content.ContentValues.TAG;

/**
 * Created by kevin321vip on 2018/1/19.
 * 侧滑菜单
 */

public class SideMenu extends FrameLayout {

    private View mContentView;
    private View mMenuView;
    private int mContentWidth;
    private int mContentHeight;
    private int mMenuWidth;
    private int mMenuHeight;
    private TranslateAnimation mTr;
    private ObjectAnimator mAnimator;
    //是否展开
    private boolean mExpand = false;

    public SideMenu(@NonNull Context context) {
        this(context, null);
    }

    public SideMenu(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //测量内容视图和菜单视图的宽和高
        if (mContentView != null && mMenuView != null) {
            mContentWidth = mContentView.getMeasuredWidth();
            mContentHeight = mContentView.getMeasuredHeight();
            mMenuWidth = mMenuView.getMeasuredWidth();
            mMenuHeight = mMenuView.getMeasuredHeight();
        }
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //摆放内容视图和菜单视图
        if (mContentView != null && mMenuView != null) {
            mContentView.layout(0, 0, mContentWidth, mContentHeight);
            mMenuView.layout(mContentWidth, 0, mContentWidth + mMenuWidth, mContentHeight);
        }
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

    float mStartX;
    float mStartY;

    /**
     * 触摸监听
     * 这种方式是通过ScrollTo来实现的视图的滑动,但是滑动效果体验不是很好
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float endX;
        int distanceX;
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartX = event.getX();
                mStartY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                endX = event.getX();
                //水平滑動的距離
                //向左滑动的时候distanceX为负值
                distanceX = (int) (mStartX - endX);
                //当向左滑动时distanceX为正值的,表示在向左滑
                //处理越界
                if (mExpand == false) {
                    //在没展开的状态下处理边界
                    if (distanceX < 0) {
                        //向右滑动不处理
                        distanceX = 0;
                    } else if (distanceX > mMenuWidth) {
                        distanceX = mMenuWidth;
                    }
                    Log.i("X", "滑动的距离:" + distanceX);
                    //当方法里面的距离为正数时整体向左滑,反正向右(和实际理解是相反的,为了实现复位)
                    scrollTo(distanceX, 0);
                } else {
                    //如果展开状态,平滑滑动复位
                    smoothToEnd(mMenuWidth);
                }
                break;
            case MotionEvent.ACTION_UP:
                //处理自动回弹
                endX = event.getX();
                distanceX = (int) (mStartX - endX);
                smoothToEnd(distanceX);
                break;
        }
        return true;
    }

    /**
     * 平滑的滑到到结束位置
     *
     * @param distanceX
     */
    private void smoothToEnd(final int distanceX) {
        ValueAnimator valueAnimator;
        if (distanceX > mMenuWidth / 2) {
            valueAnimator = ValueAnimator.ofInt(getScrollX(), mMenuWidth);
        } else {
            valueAnimator = ValueAnimator.ofInt(getScrollX(), 0);
        }
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scrollTo((Integer) animation.getAnimatedValue(), 0);
            }
        });
        //设置动画插值器,开始慢后面变快
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.setDuration(500).start();
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //超过了二分之一的子菜单宽度则是展开的状态,反之则为关闭
                if (distanceX > mMenuWidth / 2) {
                    mExpand = true;
                } else {
                    mExpand = false;
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}
