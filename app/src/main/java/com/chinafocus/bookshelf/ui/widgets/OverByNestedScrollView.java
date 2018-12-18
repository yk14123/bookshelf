package com.chinafocus.bookshelf.ui.widgets;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
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
        mRawHeight = mViewHeaderWrapper.getMeasuredHeight();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDownY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveY = ev.getY();
                float dy = moveY - mDownY;
                mDownY = moveY;
                if (getScrollY() == 0 && dy > 0) {
                    mViewHeaderWrapper.getLayoutParams().height += dy / 3;
                    mViewHeaderWrapper.requestLayout();
                }
                break;
            case MotionEvent.ACTION_UP:
                OverByAnim();
                break;
        }

        return super.dispatchTouchEvent(ev);
    }

    /**
     * 回弹动画
     */
    private void OverByAnim() {
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
