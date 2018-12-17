package com.chinafocus.bookshelf.ui.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class MyFrameLayout extends FrameLayout {

    private View mBg;
    private LinearLayout mLl_metadata_header;
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
        mBg = getChildAt(0);
        mLl_metadata_header = (LinearLayout) getChildAt(1);

        FrameLayout.LayoutParams layoutParams = (LayoutParams) mLl_metadata_header.getLayoutParams();
        mLl_left = layoutParams.leftMargin;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int ll_measuredHeight = mLl_metadata_header.getMeasuredHeight();

        int bgResultHeightSpec = MeasureSpec.makeMeasureSpec(ll_measuredHeight, MeasureSpec.EXACTLY);

        measureChild(mBg, widthMeasureSpec, bgResultHeightSpec);

        measureChildWithMargins(mLl_metadata_header, widthMeasureSpec, mLl_left, heightMeasureSpec, 0);

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
//        int ll_measuredHeight = mLl_metadata_header.getMeasuredHeight();
//        int bgResultHeightSpec = MeasureSpec.makeMeasureSpec(ll_measuredHeight, MeasureSpec.EXACTLY);
//
//        mLl_metadata_header.measure(ll_result_width_spec,bgResultHeightSpec);
//
//        int size = MeasureSpec.getSize(widthMeasureSpec);
//
//        int widthMeasureSpec1 = MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY);
//        mBg.measure(widthMeasureSpec1,bgResultHeightSpec);
//
//        setMeasuredDimension(size, ll_measuredHeight);
//    }
}
