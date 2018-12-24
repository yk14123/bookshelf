package com.chinafocus.bookshelf.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
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

import java.util.ArrayList;


public class BookNodeAdapterYang extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
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

    public BookNodeAdapterYang(Context mContext, ArrayList<TocBean> mTocBeans, ArrayList<String> mHeaderContent) {
        this.mContext = mContext;
        this.mTocBeans = mTocBeans;
        this.mHeaderContent = mHeaderContent;
        //缩进值，屏幕宽度的7.3%占比缩进
        int screenWidth = ScreenUtils.getScreenWidth(mContext);
        retract = (int) (screenWidth * 0.073f);
        Log.d(TAG, "BookNodeAdapter: retract >>> " + retract);
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

        if (viewHolder instanceof BookNodeHolder) {
            initBindBookNodeHolder((BookNodeHolder) viewHolder, position - 1);
        }

        if (position==0){
            if (viewHolder instanceof BookHeaderHolder) {
                initBindBookHeaderHolder((BookHeaderHolder) viewHolder, position);
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
            viewHolder.mExpandText.setTextWithTag(Html.fromHtml(mHeaderContent.get(3)), "推荐语");
        } else {
//            mExpandText.setTextWithTag(Html.fromHtml(description), getString(R.string.bookshelf_book_intro));
            viewHolder.mExpandText.setTextWithTag(Html.fromHtml(mHeaderContent.get(3)), "简介");
        }

        viewHolder.mTvCategoryTag.setText(mHeaderContent.get(1));

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
                    bookNodeHolder.llBookMetaRoot.setPadding(retract * level, 0, 0, 0);
                    bookNodeHolder.tvCaptureTitle.setTextAppearance(mContext, R.style.BookShelfItemTextAppearance);
                }
                bookNodeHolder.tvCaptureTitle.setText(label);
                Log.d(TAG, "onBindViewHolder: label >>> " + label + "  level >>> " + level);
            }
            //设置点选回调事件
            bookNodeHolder.llBookMetaRoot.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onNodeClick(baseNode.getLabel(), baseNode.getFull(), baseNode.getTitle());
                }
            });
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
        private ExpandableTextView mExpandText;

        BookHeaderHolder(@NonNull View itemView) {
            super(itemView);

            mViewHeaderWrapper = itemView.findViewById(R.id.view_header_wrapper_rv_yang);

            mTv_title = itemView.findViewById(R.id.tv_expand_title_rv_yang);

            mExpandText = itemView.findViewById(R.id.expand_text_view_rv_yang);

            mTvCategoryTag = itemView.findViewById(R.id.tv_book_meta_data_category_tag_rv_yang);

        }

    }
}
