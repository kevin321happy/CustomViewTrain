package com.wh.jxd.com.layoutsample;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by kevin321vip on 2018/1/6.
 */

/*总结：
1.获取View的测量大小measuredWidth和measuredHeight的时机
	由于onMeasured测量阶断可能会被执行多次导致每次测量的
	View的measuredWidth和measuredHeight大小是不同的所以
	要在onLayout阶断获取到onMeasured测量阶断的
	measuredWidth和measuredHeight

2.getMeasuredWidth()和getWidth()的区别
	第一：getMeasuredWidth()是在onMeasured测量之后才有值的
		  而getWidth()是在onLayout之后才有值的

	第二；getMeasuredWidth()的值是由setMeasuredDimensionRaw()来决定的
		  getWidth()的值是由控件的右坐标减去了左坐标计算的结果决定的
 *
 */

public class ViewGruopSum extends ViewGroup {
    private String TAG="ViewGruopSum";

    public ViewGruopSum(Context context) {
        super(context);
    }

    public ViewGruopSum(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 测量的方法
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        if (childCount>0){
            //得到了ziView
            View childAt = getChildAt(0);
            //调用measureChild传入父亲的MeasureSpec确定子View的大小
            //子View的大小和父View的MeasureSpec以及子View的布局参数(内外边距)
            childAt.measure(widthMeasureSpec,heightMeasureSpec);
        }
    }

    /**
     * 是Viewgruop的抽象方法,子类都必须继承,用来确定子View的位置
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        if (childCount>0){
            //获取子View
            View view = getChildAt(0);
            //拿到子View测量的宽高
            int measuredWidth = view.getMeasuredWidth();
            int measuredHeight = view.getMeasuredHeight();
            Log.d(TAG,"子View测量的宽高:"+measuredWidth+"   "+measuredHeight);
            //子View调用Layout的方法,确定子View在父View中的位置
            view.layout(200,400,measuredWidth,measuredHeight);
        }
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
