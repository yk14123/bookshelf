package com.chinafocus.bookshelf.ui.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chinafocus.bookshelf.R;

import java.util.List;

/**
 * @author HUANG_JIN create on 2018/12/3
 * @version v1.0
 * 1、动态设定ImageSpan的位置属性，目前只支持start(内容前面)/end(内容结尾)；
 * 2、支持多TAG标签的设置；
 * 3、支持自定义TAG的标签样式；
 * <p>
 * --->后期需要进行优化
 */
public class TagTextView extends android.support.v7.widget.AppCompatTextView {
    private static final String TAG = "TagTextView";
    /**
     * TAG处于content的开始位置
     */
    public static final int TAG_START = 1;
    /**
     * TAG位于content的结束位置
     */
    public static final int TAG_END = 2;

    /**
     * TAG引用池
     */
    @IntDef({TAG_START, TAG_END})
    @interface GRAVITY {
    }

    /**
     * TAG文字顔色
     */
    private int mTagTextColor;
    /**
     * TAG文字大小
     */
    private float mTagTextSize;
    /**
     * TAG的背景資源
     */
    private Drawable mTagDrawable;
    /**
     * TAG的显示位置
     */
    private int mTextGravity;
    /**
     * TAG layout
     */
    private View mTagView;
    /**
     * 标签显示控件
     */
    private TextView mTvTags;

    private Context mContext;

    //必须重写所有的构造器，否则可能会出现无法inflate布局的错误！
    public TagTextView(Context context) {
        this(context, null);
    }

    public TagTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(attrs);
    }

    /**
     * 初始化自定义属性
     *
     * @param attrs 属性集合
     */
    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TagTextView);
        mTagTextColor = typedArray.getColor(R.styleable.TagTextView_tagTextColor,
                getResources().getColor(R.color.bookshelf_white));
        mTagTextSize = typedArray.getDimensionPixelSize(R.styleable.TagTextView_tagTextSize, 12);
        mTagDrawable = typedArray.getDrawable(R.styleable.TagTextView_tagTextDrawable);
        mTextGravity = typedArray.getInteger(R.styleable.TagTextView_tagTextGravity, TAG_START);
        typedArray.recycle();

        mTagView = LayoutInflater.from(mContext).inflate(R.layout.bookshelf_include_tag_view, null);
        mTvTags = mTagView.findViewById(R.id.tv_bookshelf_tag);
        mTvTags.setBackground(mTagDrawable);
        mTvTags.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTagTextSize);
        mTvTags.setTextColor(mTagTextColor);
    }

    /**
     * 设置多个tag内容
     *
     * @param content 内容文本
     * @param tags    标签文本集合
     */
    public void setContentAndTag(String content, List<String> tags) {
        StringBuffer content_buffer = new StringBuffer();
        for (String item : tags) {//将每个tag的内容添加到content后边，之后将用drawable替代这些tag所占的位置
            content_buffer.append(item);
        }
        content_buffer.append(content);
        SpannableString spannableString = new SpannableString(content_buffer);
        for (int i = 0; i < tags.size(); i++) {
            String item = tags.get(i);
            mTvTags.setText(item);
            Bitmap bitmap = convertViewToBitmap(mTagView);
            Drawable d = new BitmapDrawable(getResources(), bitmap);
            d.setBounds(0, 0, mTvTags.getWidth(), mTvTags.getHeight());//缺少这句的话，不会报错，但是图片不回显示
            ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BOTTOM);//图片将对齐底部边线
            //根据设定位置设置TAG所显示的位置
            int startIndex;
            int endIndex;
            Log.d(TAG, "mTextGravity >>> " + mTextGravity);
            switch (mTextGravity) {
                case TAG_START:
                    startIndex = getLastLength(tags, i);
                    endIndex = startIndex + item.length();
                    break;
                case TAG_END://TAG与content内容的总长度
                    startIndex = getLastLength(tags, i) + content.length() - 1;
                    endIndex = startIndex + 1;
                    break;
                default:
                    startIndex = getLastLength(tags, i);
                    endIndex = startIndex + item.length();
                    break;
            }

            Log.d(TAG, "starIndex >>> " + startIndex + "  endIndex >>> " + endIndex);
            spannableString.setSpan(span, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        setText(spannableString);
        setGravity(Gravity.CENTER_VERTICAL);
    }

    /**
     * 设置单tag内容
     *
     * @param content 内容文本
     * @param tag     标签文本
     */
    public void setContentAndTag(String content, @NonNull String tag) {
        mTvTags.setText(tag);
        StringBuffer content_buffer = new StringBuffer();
        Log.d(TAG, "mTextGravity >>> " + mTextGravity);
        //根据设定位置设置TAG所显示的位置
        int startIndex;
        int endIndex;
        if (mTextGravity == TAG_START) {
//            content = "\u3000" + content;//在标签和内容之间添加一行空格，使用"\u3000"符号空格太大
            content = " " + content;//在标签和内容之间添加一行空格
            content_buffer.append(tag);
            content_buffer.append(content);
            startIndex = 0;
            endIndex = tag.length();
        } else if (mTextGravity == TAG_END) {
            content = content + " ";//在标签和内容之间添加一行空格
            content_buffer.append(content);
            content_buffer.append(tag);
            startIndex = content.length();
            endIndex = content_buffer.length();
        } else {
            startIndex = 0;
            endIndex = tag.length();
        }
        SpannableString spannableString = new SpannableString(content_buffer);
        Log.d(TAG, "setContentAndTag: string >>> " + content_buffer.toString());
        Log.d(TAG, "starIndex >>> " + startIndex + "  endIndex >>> " + endIndex);
        Bitmap bitmap = convertViewToBitmap(mTagView);
        Drawable d = new BitmapDrawable(getResources(), bitmap);
        d.setBounds(0, 0, mTvTags.getWidth(), mTvTags.getHeight());//缺少这句的话，不会报错，但是图片不回显示
        CenterImageSpan span = new CenterImageSpan(d, ImageSpan.ALIGN_BOTTOM);//图片将对齐底部边线
        spannableString.setSpan(span, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        setText(spannableString);
        setGravity(Gravity.CENTER_VERTICAL);
    }

    public void setTagDrawable(Drawable mTagDrawable) {
        this.mTagDrawable = mTagDrawable;
    }

    public void setTagTextColor(int tagTextColor) {
        this.mTagTextColor = tagTextColor;
    }

    public void setTagTextSize(float tagTextSize) {
        this.mTagTextSize = tagTextSize;
    }

    public void setTextGravity(@GRAVITY int textGravity) {
        this.mTextGravity = textGravity;
    }

    private static Bitmap convertViewToBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        return view.getDrawingCache();
    }


    private int getLastLength(List<String> list, int maxLength) {
        int length = 0;
        for (int i = 0; i < maxLength; i++) {
            length += list.get(i).length();
        }
        return length;
    }
}
