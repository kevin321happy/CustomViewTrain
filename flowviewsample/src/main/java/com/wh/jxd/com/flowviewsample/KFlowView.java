package com.wh.jxd.com.flowviewsample;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by kevin321vip on 2018/1/7.
 * 流式布局
 */

public class KFlowView extends ViewGroup {
    private int widthMeasureSpec;
    private int heightMeasureSpec;
    private int verticalSpacing = 20;//竖直方向的间距

    public KFlowView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 直接继承自ViewGroup的控件需要手动出来内外边距
     * 先测量View所需要的高度
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        this.widthMeasureSpec = widthMeasureSpec;
//        this.heightMeasureSpec = heightMeasureSpec;
        //获取宽高的测量模式
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpeceMode = MeasureSpec.getMode(heightMeasureSpec);
        //获取宽高的测量大小
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        //获取内边距
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        //已经使用了的空间就是已经设置了的内边距的值
        int usedWidth = paddingLeft + paddingRight;
        int userHeight = paddingBottom + paddingTop;


        //单行的最高的高度
        int childMaxHeightthisLine = 0;
        int childCount = getChildCount();
        //测量子View
        for (int i = 0; i < childCount; i++) {
            //获取到子View
            View childView = getChildAt(i);
            if (childView.getVisibility() != GONE) {
                int childUsedWidth = 0;
                int childUsedHeight = 0;
                //子VIew测量得到自己的宽和高
                measureChild(childView, widthMeasureSpec, heightMeasureSpec);

                childUsedWidth += childView.getMeasuredWidth();
                childUsedHeight += childView.getMeasuredHeight();
//                int childWidth = childView.getMeasuredWidth();
//                int childHeight = childView.getMeasuredHeight();


                LayoutParams layoutParams = childView.getLayoutParams();
                MarginLayoutParams params = (MarginLayoutParams) layoutParams;
                //计算得到孩子已经使用了的水平和竖直的空间,累加值
                childUsedWidth += params.leftMargin + params.rightMargin;
                childUsedHeight += params.topMargin + params.bottomMargin;
                //未超过一行,无序换行的情况
                if (usedWidth + childUsedWidth < widthSize) {
                    usedWidth += childUsedWidth;
                    //这一行中最高的一个子view的高度就是这行的最高的高度
                    if (childView.getMeasuredHeight() > childMaxHeightthisLine) {
                        childMaxHeightthisLine = childView.getMeasuredHeight();
                    }
                } else {
                    //超过了一行,需要处理换行
                    //父View竖直方向用掉的高度为
                    userHeight += childMaxHeightthisLine + verticalSpacing;
                    usedWidth = paddingLeft + paddingRight + childUsedWidth;
                    childMaxHeightthisLine = childUsedHeight;
                }

            }
        }
        //父View用掉的高度永远等于N-1行的高度和加上第N行的最高的高度
        userHeight += childMaxHeightthisLine;
        //测量父View所需要的宽高
        setMeasuredDimension(widthSize, userHeight);

    }

    /**
     * 用户摆放ViewGruop中的所有的子控件
     *
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        //子View开始摆放的起点的宽高
        int childStartLayoutX = paddingLeft;
        int childStartLayoutY = paddingTop;
        //父View已经用了控件
        int widthUsed = paddingLeft + paddingRight;
        int childMaxHeight = 0;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            int childNeededWidth, childNeedHeight;
            int left, top, right, bottom;
            View childView = getChildAt(i);
            if (childView.getVisibility() != GONE) {
                //得到子View的测量的宽高
                int childWidth = childView.getMeasuredWidth();
                int childHeight = childView.getMeasuredHeight();
                //获得子View的外边距
                MarginLayoutParams params = (MarginLayoutParams) childView.getLayoutParams();
                int leftMargin = params.leftMargin;
                int topMargin = params.topMargin;
                int rightMargin = params.rightMargin;
                int bottomMargin = params.bottomMargin;
                //子View所需的空间
                childNeededWidth = leftMargin + rightMargin + childWidth;
                childNeedHeight = topMargin + bottomMargin + childHeight;
                //无需换行
                if (widthUsed + childNeededWidth <= r - 1) {
                    if (childHeight > childMaxHeight) {
                        childMaxHeight = childHeight;
                    }
                    //得到子View四个顶点的位置
                    left = leftMargin + childStartLayoutX;
                    top = topMargin + childStartLayoutY;
                    right = left + childWidth;
                    bottom = top + childHeight;
                    widthUsed += childNeededWidth;
                    childStartLayoutX += childNeededWidth;
                } else {
                    //需要换行了
                    childStartLayoutY += childMaxHeight + verticalSpacing;
                    childStartLayoutX = paddingLeft;
                    widthUsed = paddingLeft + paddingRight;
                    left = childStartLayoutX + leftMargin;
                    top = childStartLayoutY + topMargin;
                    right = left + childWidth;
                    bottom = top + childHeight;
                    widthUsed += childNeededWidth;
                    //子View的水平方向的起点为需要的宽度的 叠加
                    childStartLayoutX += childNeededWidth;
                    //子View的最大的高度等于当前的需要的高度
                    childMaxHeight = childNeedHeight;
                }
                //确定了子View的位置之后摆放子View
                childView.layout(left, top, right, bottom);
            }
        }


    }
}
