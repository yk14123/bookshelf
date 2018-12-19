package com.chinafocus.bookshelf.ui.widgets;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;

/**
 * 回弹处理的NestedScrollView
 */
public class OverByNestedScrollView extends NestedScrollView {

    private float mDownY;
    private View mViewHeaderWrapper;
    private ValueAnimator mAnimator;
    private int mRawHeight;
    private float mYVelocity;
    //    private int mPointerId;

    public OverByNestedScrollView(@NonNull Context context) {
        super(context);
    }

    public OverByNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public OverByNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LinearLayout linearLayout = (LinearLayout) getChildAt(0);
        mViewHeaderWrapper = linearLayout.getChildAt(0);
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
    public boolean dispatchTouchEvent(MotionEvent ev) {


        // 获得允许执行一个fling手势动作的最大速度值
        ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
        int mMaximumVelocity = viewConfiguration.getScaledMaximumFlingVelocity();//8000
        int mMinimumVelocity = viewConfiguration.getScaledMinimumFlingVelocity();//50

        int scaledTouchSlop = viewConfiguration.getScaledTouchSlop();//溢出：8  华为p9是24
        float scaledFriction = ViewConfiguration.getScrollFriction();//摩檫力！：0.015 华为p9是0.015

        Log.i("scaledFriction", "scaledTouchSlop" + scaledTouchSlop);
        Log.i("scaledFriction", "scaledFriction" + scaledFriction);

        VelocityTracker mVelocityTracker = VelocityTracker.obtain();
        mVelocityTracker.addMovement(ev);


        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDownY = ev.getY();
                mRawHeight = mViewHeaderWrapper.getMeasuredHeight();
//                mPointerId = ev.getPointerId(0);
                break;
            case MotionEvent.ACTION_MOVE:

                // 实例化
                mVelocityTracker.computeCurrentVelocity(1000);
                mYVelocity = mVelocityTracker.getYVelocity();
//                float yVelocity_mPointerId = mVelocityTracker.getYVelocity(ev.getPointerId(mPointerId));
//                Log.i("OverByAnim", "yVelocity -- >" + yVelocity + "<-- yVelocity_mPointerId -->" + yVelocity_mPointerId);
                Log.i("OverByAnim", "yVelocity -- >" + mYVelocity);

                float moveY = ev.getY();
                float dy = moveY - mDownY;
                mDownY = moveY;
                if (getScrollY() == 0 && dy > 0) {
                    if (mYVelocity < -mMinimumVelocity) {
                        return true;
                    }
                    mViewHeaderWrapper.getLayoutParams().height += dy / 3;
                    mViewHeaderWrapper.requestLayout();
                }

                break;
            case MotionEvent.ACTION_UP:
                OverByAnim();
                break;

            case MotionEvent.ACTION_CANCEL:
                mVelocityTracker.recycle();
                break;
        }

        return super.dispatchTouchEvent(ev);
    }

    /**
     * 回弹动画
     */
    private void OverByAnim() {
        Log.i("OverByAnim", "mViewHeaderWrapper.getHeight() -- > " + mViewHeaderWrapper.getHeight());
        Log.i("OverByAnim", "mRawHeight -- > " + mRawHeight);
        mAnimator = ValueAnimator.ofInt(mViewHeaderWrapper.getHeight(), mRawHeight);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                mViewHeaderWrapper.getLayoutParams().height = (int) (Integer) animator.getAnimatedValue();
                mViewHeaderWrapper.requestLayout();
            }
        });
        mAnimator.setInterpolator(new OvershootInterpolator(3));
        mAnimator.setDuration(500);
        mAnimator.start();
    }

}
