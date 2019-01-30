package com.chinafocus.bookshelf.ui.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinafocus.bookshelf.R;
import com.chinafocus.bookshelf.bean.BookMetadataRawBean.BookMetadataResultBean.TocBean;
import com.chinafocus.bookshelf.ui.widgets.ExpandableTextView;
import com.chinafocus.bookshelf.utils.ScreenUtils;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


public class BookNodeAdapterYangRV extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "BookNodeAdapter";
    private Context mContext;
    private ArrayList<TocBean> mTocBeans;
    //当前条目的缩进值
    private int retract;
    //回调
    private OnBookNodeClickListener listener;
    //点击透明头部展示dialog全图
    private OnDialogShowListener mOnDialogShowListener;

    private ArrayList<String> mHeaderContent;
    private static int sTagColor;
    private final int mFirstRetract;
    private static int sTagTextSize;
    private Disposable mLlBookMetaRootClicks;

    public BookNodeAdapterYangRV(Context mContext, ArrayList<TocBean> mTocBeans, ArrayList<String> mHeaderContent) {
        this.mContext = mContext;
        this.mTocBeans = mTocBeans;
        this.mHeaderContent = mHeaderContent;
        //缩进值，屏幕宽度的7.3%占比缩进
        int screenWidth = ScreenUtils.getScreenWidth(mContext);
        retract = (int) (screenWidth * 0.073f);
        mFirstRetract = ScreenUtils.dip2Px(mContext, 15);

        sTagTextSize = ScreenUtils.dip2Px(mContext, 10);

        Log.d(TAG, "BookNodeAdapter: retract >>> " + retract);

        sTagColor = mContext.getResources().getColor(R.color.bookshelf_metadata_tag_color);
    }

    //ITEM类型
    private static final int HEADER = 0;
    private static final int CONTENT = 1;

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEADER;
        }
        return CONTENT;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        if (viewType == HEADER) {
            View headerView = LayoutInflater.from(mContext).inflate(R.layout.bookshelf_recycle_item_book_metadata_header_yang, viewGroup, false);
            return new BookHeaderHolder(headerView);
        }


        View contentView = LayoutInflater.from(mContext).inflate(
                R.layout.bookshelf_recycle_item_book_meta, viewGroup, false);

        return new BookNodeHolder(contentView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        if (position == HEADER) {
            if (viewHolder instanceof BookHeaderHolder) {
                initBindBookHeaderHolder((BookHeaderHolder) viewHolder, position);
            }
        } else {
            Log.d(TAG, "onBindViewHolder: position >>> " + position);
            if (viewHolder instanceof BookNodeHolder) {
                initBindBookNodeHolder((BookNodeHolder) viewHolder, position - 1);
            }
        }
    }


    //回调接口
    public interface OnDialogShowListener {
        void onDialogShowClick(View v);
    }

    public void setOnDialogShowListener(OnDialogShowListener onDialogShowListener) {
        mOnDialogShowListener = onDialogShowListener;
    }

    private void initBindBookHeaderHolder(BookHeaderHolder viewHolder, int position) {

        viewHolder.mViewHeaderWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnDialogShowListener != null) {
                    mOnDialogShowListener.onDialogShowClick(v);
                }
            }
        });

        viewHolder.mTv_title.setText(mHeaderContent.get(0));

        viewHolder.mExpandText.setOnExpandStateChangeListener((textView, isExpanded) -> {
            Log.d(TAG, "expandState changed >>> isExpanded >>> " + isExpanded);
//            requestNestedLayout();
        });

        if (mHeaderContent.get(2).equals("推荐语")) {
//            viewHolder.mExpandText.setTextWithTag("伸缩大内容", "tag");
//            viewHolder.mExpandText.setTextWithTag(Html.fromHtml(mHeaderContent.get(3)), "推荐语");
//            viewHolder.mTvRecommendTag.setText("推荐语");
            StringBuilder stringBuffer = new StringBuilder();

            String str = mHeaderContent.get(3).trim();

            String[] split = str.split("<br><br><i>");
            String substring = split[1].substring(0, split[1].length() - 4);

            stringBuffer
                    .append("  ")
                    .append(split[0])
                    .append("\r\n")
                    .append("\r\n")
                    .append("        ")
                    .append(substring);


            int start = "[smile]".length() + stringBuffer.indexOf(substring);
            int end = "[smile]".length() + stringBuffer.length();


            SpannableString spannableString = new SpannableString("[smile]" + stringBuffer);
//            StyleSpan styleSpan_B = new StyleSpan(Typeface.BOLD);
            StyleSpan styleSpan_I = new StyleSpan(Typeface.ITALIC);
//            spannableString.setSpan(styleSpan_B, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(styleSpan_I, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//            textView.setHighlightColor(Color.parseColor("#36969696"));


//            int strokeWidth = 1;     // 1dp 边框宽度
//            int roundRadius = 5;     // 5dp 圆角半径
//            int strokeColor = Color.parseColor("#FFFF0000");//边框颜色
//            int fillColor = Color.parseColor("#FF00FF00"); //内部填充颜色
//
//            GradientDrawable gd = new GradientDrawable();//创建drawable
//            gd.setColor(fillColor);
//            gd.setCornerRadius(roundRadius);
//            gd.setStroke(strokeWidth, strokeColor);
//            gd.setGradientType(GradientDrawable.RECTANGLE);
//            gd.setSize(100,200);

//            view.setBackgroundDrawable(gd);

// 创建渐变的shape drawable
//            int colors[] = { 0xff255779 , 0xff3e7492, 0xffa6c0cd };//分别为开始颜色，中间夜色，结束颜色
//            GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
//            view.setBackgroundDrawable(gd);


            Drawable drawable = mContext.getResources().getDrawable(R.drawable.bookshelf_metadata_tag_rect_recommend);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            MyImageSpan myImageSpan = new MyImageSpan(drawable, "推荐语");
            spannableString.setSpan(myImageSpan, 0, "[smile]".length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

            viewHolder.mExpandText.setText(spannableString);


//            //需要处理的文本，[smile]是需要被替代的文本
//            //必须要设置替换文字smile不能取消掉
//            SpannableString spannable = new SpannableString("[smile]" + Html.fromHtml(mHeaderContent.get(3)).toString());
//            //要让图片替代指定的文字就要用ImageSpan
//            MyImageSpan myImageSpan = new MyImageSpan(drawable);
//            //开始替换，注意第2和第3个参数表示从哪里开始替换到哪里替换结束（start和end）
////最后一个参数类似数学中的集合,[5,12)表示从5到12，包括5但不包括12
//            //必须要设置替换文字的长度，smile不能取消掉
//            spannable.setSpan(myImageSpan, 0, "[smile]".length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//            viewHolder.mExpandText.setText(spannable);
//
//            LogUtil.veryLongForI("spannable",mHeaderContent.get(3));

//            viewHolder.mExpandText.setText(Html.fromHtml(mHeaderContent.get(3)));

        } else {
//            mExpandText.setTextWithTag(Html.fromHtml(description), getString(R.string.bookshelf_book_intro));
//            viewHolder.mExpandText.setTextWithTag(Html.fromHtml(mHeaderContent.get(3)), "简介");

            SpannableString spannableString = new SpannableString("[smile]" + "  " + Html.fromHtml(mHeaderContent.get(3)));

            Drawable drawable = mContext.getResources().getDrawable(R.drawable.bookshelf_metadata_tag_rect_intro);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());


            MyImageSpan myImageSpan = new MyImageSpan(drawable, "简介");
            spannableString.setSpan(myImageSpan, 0, "[smile]".length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

            viewHolder.mExpandText.setText(spannableString);


//            viewHolder.mExpandText.setText(Html.fromHtml(mHeaderContent.get(3)));
//            viewHolder.mTvRecommendTag.setText("简介");
        }

        viewHolder.mTvCategoryTag.setText(mHeaderContent.get(1));

    }

    public static class MyImageSpan extends ImageSpan {

        private Paint mPaint;

        public MyImageSpan(Drawable drawable) {
            super(drawable);

        }

        public MyImageSpan(Drawable drawable, String source) {
            super(drawable, source);

            mPaint = new Paint();
            mPaint.setTextSize(sTagTextSize);
            mPaint.setColor(sTagColor);
        }

        @Override
        public void draw(Canvas canvas, CharSequence text, int start, int end,
                         float x, int top, int y, int bottom, Paint paint) {
            Paint.FontMetricsInt fm = paint.getFontMetricsInt();
            Drawable drawable = getDrawable();
            int transY = (y + fm.descent + y + fm.ascent) / 2
                    - drawable.getBounds().bottom / 2;

            Log.i("FontMetricsInt", "FontMetricsInt--descent>" + fm.descent);
            Log.i("FontMetricsInt", "FontMetricsInt--ascent>" + fm.ascent);

            canvas.save();
            canvas.translate(x, transY);
            drawable.draw(canvas);

            // 计算Baseline绘制的起点X轴坐标 ，计算方式：画布宽度的一半 - 文字宽度的一半
            int baseX = (int) (drawable.getBounds().width() / 2 - mPaint.measureText(getSource()) / 2);

            // 计算Baseline绘制的Y坐标 ，计算方式：画布高度的一半 - 文字总高度的一半
            int baseY = (int) ((drawable.getBounds().height() / 2) - ((mPaint.descent() + mPaint.ascent()) / 2));

            Log.i("FontMetricsInt", "FontMetricsInt--baseX>" + baseX);
            Log.i("FontMetricsInt", "FontMetricsInt--baseY>" + baseY);

            canvas.drawText(getSource(), baseX, baseY, mPaint);
            canvas.restore();
        }
    }

    private void initBindBookNodeHolder(@NonNull BookNodeHolder bookNodeHolder, int position) {

        TocBean baseNode = mTocBeans.get(position);
        if (baseNode != null) {
            String label = baseNode.getLabel();
            int level = baseNode.getLevel();
            if (!TextUtils.isEmpty(label)) {
                //添加两个空格字符,实现缩进字符的效果 >>> 除去level 1层级
                if (level != 0) {
//                    label = getLabel(label, level);
                    bookNodeHolder.llBookMetaRoot.setPadding(mFirstRetract + retract * level, 0, 0, 0);
                    bookNodeHolder.tvCaptureTitle.setTextAppearance(mContext, R.style.BookShelfItemTextAppearance);
                } else {
                    bookNodeHolder.llBookMetaRoot.setPadding(mFirstRetract, 0, 0, 0);
                    bookNodeHolder.tvCaptureTitle.setTextAppearance(mContext, R.style.BookShelfItemTextAppearanceLevelOne);
                }
                bookNodeHolder.tvCaptureTitle.setText(label);
                Log.d(TAG, "onBindViewHolder: label >>> " + label + "  level >>> " + level);
            }


            mLlBookMetaRootClicks = RxView.clicks(bookNodeHolder.llBookMetaRoot).throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                            if (listener != null) {
                                listener.onNodeClick(baseNode.getLabel(), baseNode.getFull(), baseNode.getTitle());
                            }
                        }
                    });

//            //设置点选回调事件
//            bookNodeHolder.llBookMetaRoot.setOnClickListener(v -> {
//                if (listener != null) {
//                    listener.onNodeClick(baseNode.getLabel(), baseNode.getFull(), baseNode.getTitle());
//                }
//            });
        }
    }

    public void clear() {
        if (mLlBookMetaRootClicks != null && mLlBookMetaRootClicks.isDisposed()) {
            mLlBookMetaRootClicks.dispose();
        }
    }

    private String getLabel(String label, int level) {
        if (level > 0) {
            StringBuffer buffer = new StringBuffer(level + 1);
            for (int i = 0; i < level; i++) {
                buffer.append("\u3000\u3000");
            }
            buffer.append(label);
            return buffer.toString();
        }
        return label;
    }

    //回调接口
    public interface OnBookNodeClickListener {
        void onNodeClick(String label, String pageId, String title);
    }

    public void setBookNodeClickListener(OnBookNodeClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return mTocBeans == null ? 0 : mTocBeans.size() + 1;
    }

    static class BookNodeHolder extends RecyclerView.ViewHolder {
        private LinearLayout llBookMetaRoot;
        private TextView tvCaptureTitle;

        BookNodeHolder(@NonNull View itemView) {
            super(itemView);
            llBookMetaRoot = itemView.findViewById(R.id.ll_book_meta_item_root);
            tvCaptureTitle = itemView.findViewById(R.id.tv_book_meta_item_title);
        }
    }

    static class BookHeaderHolder extends RecyclerView.ViewHolder {

        private View mViewHeaderWrapper;
        private TextView mTv_title;
        private TextView mTvCategoryTag;
        //        private TextView mTvRecommendTag;
        private ExpandableTextView mExpandText;

        BookHeaderHolder(@NonNull View itemView) {
            super(itemView);

            mViewHeaderWrapper = itemView.findViewById(R.id.view_header_wrapper_rv_yang);

            mTv_title = itemView.findViewById(R.id.tv_expand_title_rv_yang);

            mExpandText = itemView.findViewById(R.id.expand_text_view_rv_yang);

//            mTvRecommendTag = itemView.findViewById(R.id.tv_book_meta_data_recommend_tag_rv_yang);

            mTvCategoryTag = itemView.findViewById(R.id.tv_book_meta_data_category_tag_rv_yang);

        }

    }
}
