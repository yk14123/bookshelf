package com.chinafocus.bookshelf.ui.widgets;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * FrameLayout加嵌套背景色
 */
public class MyFrameLayout extends FrameLayout {

    private View mEmbedBg;
    private CardView mCardView;
    private int mLl_left;

    public MyFrameLayout(Context context) {
        super(context);
    }

    public MyFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mEmbedBg = getChildAt(0);
        mCardView = (CardView) getChildAt(1);
        FrameLayout.LayoutParams layoutParams = (LayoutParams) mCardView.getLayoutParams();
        mLl_left = layoutParams.leftMargin;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int ll_measuredHeight = mCardView.getMeasuredHeight() + 30;

        int bgResultHeightSpec = MeasureSpec.makeMeasureSpec(ll_measuredHeight, MeasureSpec.EXACTLY);

        measureChild(mEmbedBg, widthMeasureSpec, bgResultHeightSpec);

        measureChildWithMargins(mCardView, widthMeasureSpec, mLl_left, heightMeasureSpec, 0);

        int size = MeasureSpec.getSize(widthMeasureSpec);

        setMeasuredDimension(size, ll_measuredHeight);
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//
//        int screenWidthSize = MeasureSpec.getSize(widthMeasureSpec);//1080
//        int ll_result_width_size = screenWidthSize - mLl_left * 2;//990
//
//        int ll_result_width_spec = MeasureSpec.makeMeasureSpec(ll_result_width_size, MeasureSpec.EXACTLY);
//
//        int ll_measuredHeight = mCardView.getMeasuredHeight();
//        int bgResultHeightSpec = MeasureSpec.makeMeasureSpec(ll_measuredHeight, MeasureSpec.EXACTLY);
//
//        mCardView.measure(ll_result_width_spec,bgResultHeightSpec);
//
//        int size = MeasureSpec.getSize(widthMeasureSpec);
//
//        int widthMeasureSpec1 = MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY);
//        mEmbedBg.measure(widthMeasureSpec1,bgResultHeightSpec);
//
//        setMeasuredDimension(size, ll_measuredHeight);
//    }
}
