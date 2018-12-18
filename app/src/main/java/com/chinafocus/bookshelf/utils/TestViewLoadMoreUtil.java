package com.chinafocus.bookshelf.utils;


import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

public class TestViewLoadMoreUtil {

    /**
     * TextView初始化最多展示的行数的高度
     *
     * @param context
     * @param testView 想要控制的TextView
     * @param content  文字内容
     * @param size     文字大小
     * @param maxLines 首次展示的内容行数
     * @return
     */
    public static int getShortHeight(Context context, TextView testView, CharSequence content, int size, int maxLines) {
        int measuredWidth = testView.getMeasuredWidth();

        TextView textView = new TextView(context);
        textView.setText(content);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        textView.setMaxLines(maxLines);

        int widthSpec = View.MeasureSpec.makeMeasureSpec(measuredWidth, View.MeasureSpec.EXACTLY);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(3000, View.MeasureSpec.AT_MOST);

        textView.measure(widthSpec, heightSpec);

        return textView.getMeasuredHeight();
    }

    /**
     * TextView的所有包裹内容实际展示高度
     *
     * @param context
     * @param testView 想要控制的TextView
     * @param content  全部文字内容
     * @param size     文字大小
     * @return
     */
    public static int getLongHeight(Context context, CharSequence content, TextView testView, int size) {
        int measuredWidth = testView.getMeasuredWidth();

        TextView textView = new TextView(context);
        textView.setText(content);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);

        int widthSpec = View.MeasureSpec.makeMeasureSpec(measuredWidth, View.MeasureSpec.EXACTLY);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(3000, View.MeasureSpec.AT_MOST);

        textView.measure(widthSpec, heightSpec);

        return textView.getMeasuredHeight();
    }
}
