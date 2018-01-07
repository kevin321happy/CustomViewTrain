package com.wh.jxd.com.collapseviewsample;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by kevin321vip on 2018/1/7.
 * 一个可以收缩的View
 */

public class CollapseView extends LinearLayout {
    private final Context mContext;
    private TextView mNumberTextView;
    private TextView mTitleTextView;
    private RelativeLayout mTitleRelativeLayout;
    private RelativeLayout mContentRelativeLayout;
    private ImageView mArrowImageView;
    private int widthMeasureSpec;
    private int heightMeasureSpec;
    private long duration = 350;//动画执行的时间
    private boolean EXPAND_FLAG = false;//展开的标志,默认是不展开的

    public CollapseView(Context context) {
        this(context, null);
    }

    public CollapseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.collapse_layout, this);
        initView();
    }


    private void initView() {
        mNumberTextView = (TextView) findViewById(R.id.numberTextView);
        mTitleTextView = (TextView) findViewById(R.id.titleTextView);
        mTitleRelativeLayout = (RelativeLayout) findViewById(R.id.titleRelativeLayout);
        mContentRelativeLayout = (RelativeLayout) findViewById(R.id.contentRelativeLayout);
        mArrowImageView = (ImageView) findViewById(R.id.arrowImageView);
        mArrowImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showArrowAnimation();
            }
        });
        //默认关闭
        closeLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取父布局的MeasureSpec
        this.widthMeasureSpec = widthMeasureSpec;
        this.heightMeasureSpec = heightMeasureSpec;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    /**
     * 执行箭头的动画
     */
    private void showArrowAnimation() {
        //箭头旋转的角度
        int degree = 0;
        if (EXPAND_FLAG == false) {
            //改变FLAG的状态
            EXPAND_FLAG = true;
            //旋转-180度变成展开
            degree = -180;
            //展开布局
            openLayout();
        } else {
            EXPAND_FLAG = false;
            degree = 0;
            //关闭
            closeLayout();
        }
        //箭头执行动画
        mArrowImageView.animate().setDuration(duration).rotation(degree);

    }

    /**
     * 关闭下面的布局
     */
    private void closeLayout() {
        final int measuredHeight = mContentRelativeLayout.getMeasuredHeight();
        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                super.applyTransformation(interpolatedTime, t);
                if (interpolatedTime == 1) {
                    mContentRelativeLayout.setVisibility(View.GONE);
                } else {
                    mContentRelativeLayout.getLayoutParams().height = measuredHeight - (int) (measuredHeight * interpolatedTime);
                    mContentRelativeLayout.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        //执行动画
        animation.setDuration(duration);
        mContentRelativeLayout.startAnimation(animation);
    }

    /**
     * 展看下面的布局
     */
    private void openLayout() {
        //先获取下面隐藏的子布局,因为子View是Gone的所以在父VIew的measuer中是没有测量他的宽高的
        //这里需要自己执行Measure方法测量得到宽高
        mContentRelativeLayout.measure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = mContentRelativeLayout.getMeasuredWidth();
        final int measuredHeight = mContentRelativeLayout.getMeasuredHeight();
        //让隐藏的View可见
        mContentRelativeLayout.setVisibility(VISIBLE);
        //执行动画
        Animation animation = new Animation() {
            //第一个参数为动画的进度时间值，取值范围为[0.0f,1.0f]，
            //第二个参数Transformation记录着动画某一帧中变形的原始数据。
            //该方法在动画的每一帧显示过程中都会被调用。
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                super.applyTransformation(interpolatedTime, t);
                //当动画执行完,展开状态时
                if (interpolatedTime == 1) {
                    mContentRelativeLayout.getLayoutParams().height = measuredHeight;
                } else {
                    //伴随着动画慢慢的显示
                    mContentRelativeLayout.getLayoutParams().height = (int) (measuredHeight * interpolatedTime);
                }
                /**    我们可以理解为重新布局了一下view；
                 用途：有时我们在改变一个view 的内容之后 可能会造成显示出现错误，
                 比如写ListView的时候 重用convertview中的某个TextView 可能因为前
                 后填入的text长度不同而造成显示出错，此时我们可以在改变内容之后
                 调用requestLayout方法加以解决。**/
                mContentRelativeLayout.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        //执行动画
        animation.setDuration(duration);
        mContentRelativeLayout.startAnimation(animation);
    }

    /**
     * 设置子布局（用于拓展,子布局可以是任意的类型）
     */
    public void setContentView(int resID) {
        View contentView = LayoutInflater.from(mContext).inflate(resID, null);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        contentView.setLayoutParams(params);
        mContentRelativeLayout.addView(contentView);
    }
}
