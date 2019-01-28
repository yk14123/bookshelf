package com.chinafocus.bookshelf.ui.widgets;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

/**
 * @author
 * @date 2019/1/14
 * description：
 */
public class MyOverByRecyclerView extends RecyclerView {

    private float mDownY;
    private View mViewHeaderWrapper;
    private ValueAnimator mAnimator;
    private int mTotalByOver;
    boolean mIsFirstRunning;
    private LinearLayoutManager mLayoutManager;
    private int mAnimDy;
    private boolean mIsNeedOverByAnim;

    public MyOverByRecyclerView(@NonNull Context context) {
        super(context);
    }

    public MyOverByRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyOverByRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAnimator != null) {
            mAnimator.cancel();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void attachViewToParent(View child, int index, ViewGroup.LayoutParams params) {
        super.attachViewToParent(child, index, params);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (!mIsFirstRunning) {
            mLayoutManager = (LinearLayoutManager) getLayoutManager();
            //mViewHeaderWrapper是HeaderView的抽象
            mViewHeaderWrapper = mLayoutManager.findViewByPosition(0);

            mIsFirstRunning = true;
        }
        //下面就是手势下拉，改变HeaderView的padding
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDownY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveY = ev.getY();
                float dy = moveY - mDownY;
                mDownY = moveY;

                if (mViewHeaderWrapper != null && getScrollY() == 0 && dy > 0 && mViewHeaderWrapper.getTop() >= 0) {
                    mTotalByOver += dy / 3;
                    mViewHeaderWrapper.setPadding(0, mTotalByOver, 0, 0);
                    mIsNeedOverByAnim = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mIsNeedOverByAnim) {
                    OverByAnim();
                    mTotalByOver = 0;
                    mIsNeedOverByAnim = false;
                }
                break;
        }

        return super.dispatchTouchEvent(ev);
    }


    /**
     * 回弹动画
     */
    private void OverByAnim() {
        mAnimator = ValueAnimator.ofInt(mTotalByOver, 0);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                mAnimDy = (int) (Integer) animator.getAnimatedValue();
                mViewHeaderWrapper.setPadding(0, mAnimDy, 0, 0);
            }
        });
        mAnimator.setInterpolator(new OvershootInterpolator(3));
        mAnimator.setDuration(500);
        mAnimator.start();
    }
}
