package com.chinafocus.bookshelf.ui.widgets;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;


/**
 * 仿ios回弹
 */
public class OverByNestedScrollView extends NestedScrollView {



    public OverByNestedScrollView(@NonNull Context context) {
        super(context);
    }


    public OverByNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public OverByNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    // 拖动缩放比，允许拖动距离的的1/4长度，粘性效果
    private static final int SCALE_RATIO = 4;
    private View inner;
    private float mDownY;
    private Rect normal = new Rect();


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 0) {
            inner = getChildAt(0);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (inner == null) {
            return super.onTouchEvent(ev);
        } else {
            commOnTouchEvent(ev);
        }
        return super.onTouchEvent(ev);
    }


    public void commOnTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDownY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveNowY = ev.getY();
                /**
                 * deltaY就是最新的scrollBy移动增加量！
                 */
                int deltaY = (int) (moveNowY - mDownY) / SCALE_RATIO;
                // 滚动nested对scrollBy做了处理，导致无效
                // scrollBy(0, deltaY);
                //回复一下最新的downY
                mDownY = moveNowY;
                // 当滚动到最上或者最下时就不会再滚动，这时移动布局
                if (isNeedMove()) {
                    if (normal.isEmpty()) {
                        // 保存正常的布局位置
                        normal.set(inner.getLeft(), inner.getTop(),
                                inner.getRight(), inner.getBottom());
                        return;
                    }
                    //moveY-downY，这里是+deltaY
                    int yy = inner.getTop() + deltaY;

                    // 移动布局
                    inner.layout(inner.getLeft(), yy, inner.getRight(),
                            inner.getBottom() + deltaY);

                }
                break;
            case MotionEvent.ACTION_UP:
                if (isNeedAnimation()) {
                    animRebound();
                }
                break;
        }
    }

    private void animRebound() {
        ValueAnimator animator = ValueAnimator.ofInt(inner.getTop(), normal.top);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                inner.getLayoutParams().height = (int) (Integer) animator.getAnimatedValue();
                inner.requestLayout();
            }
        });
        animator.setInterpolator(new OvershootInterpolator());
        animator.setDuration(300);
        animator.start();
        normal.setEmpty();
    }

    // 是否需要开启动画
    public boolean isNeedAnimation() {
        return !normal.isEmpty();
    }

    // 是否需要移动布局
    public boolean isNeedMove() {
        int offset = inner.getMeasuredHeight() - getHeight();
        int scrollY = getScrollY();
        return scrollY == 0 || scrollY == offset;
    }

}


