package com.chinafocus.bookshelf.ui.widgets;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.chinafocus.bookshelf.utils.ScreenUtils;

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

//        measureChild(mEmbedBg, widthMeasureSpec, bgResultHeightSpec);

        int bgWidth = MeasureSpec.getSize(widthMeasureSpec);
        Log.i("onMeasure" , "onMeasure--widthMeasureSpec-->"+bgWidth);//1080
        int cardViewWidth = bgWidth - mLl_left - mLl_left;
        int cardViewWidthMeasureSpec = MeasureSpec.makeMeasureSpec(cardViewWidth, MeasureSpec.EXACTLY);
//
        measureChild(mCardView, cardViewWidthMeasureSpec, heightMeasureSpec);
//        measureChildWithMargins(mCardView, widthMeasureSpec, mLl_left, heightMeasureSpec, 0);

        int ll_measuredHeight = mCardView.getMeasuredHeight()+ScreenUtils.dip2Px(getContext(),16);
        Log.i("onMeasure" , "onMeasure-->"+ll_measuredHeight);
        int bgResultHeightSpec = MeasureSpec.makeMeasureSpec(ll_measuredHeight, MeasureSpec.EXACTLY);

//        measureChildren(widthMeasureSpec,bgResultHeightSpec);
        measureChild(mEmbedBg, widthMeasureSpec, bgResultHeightSpec);


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
//        int ll_measuredHeight = mLinearLayout.getMeasuredHeight();
//        int bgResultHeightSpec = MeasureSpec.makeMeasureSpec(ll_measuredHeight, MeasureSpec.EXACTLY);
//
//        mLinearLayout.measure(ll_result_width_spec,bgResultHeightSpec);
//
//        int size = MeasureSpec.getSize(widthMeasureSpec);
//
//        int widthMeasureSpec1 = MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY);
//        mEmbedBg.measure(widthMeasureSpec1,bgResultHeightSpec);
//
//        setMeasuredDimension(size, ll_measuredHeight);
//    }
}
