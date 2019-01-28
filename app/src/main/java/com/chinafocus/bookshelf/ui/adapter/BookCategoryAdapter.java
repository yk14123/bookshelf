package com.chinafocus.bookshelf.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chinafocus.bookshelf.R;
import com.chinafocus.bookshelf.bean.BookCategoryDetailRawBean;
import com.zhy.android.percent.support.PercentRelativeLayout;

import java.util.List;

/**
 * 分类图书列表数据适配器
 *
 * @author HUANG_JIN
 * @version 1.0
 * create at 2018/11/27 14:47
 */
public class BookCategoryAdapter extends RecyclerView.Adapter<BookCategoryAdapter.BookCategoryHolder> {
    private static final String TAG = "ShelfCategoryAdapter";
    private Context mContext;

    private List<BookCategoryDetailRawBean.BookCategoryDetailResultBean.BooksCategoryDetailFinalBean> mCategoryEntity;
    //回调
    private OnBookItemListener listener;


    public BookCategoryAdapter(Context context, List<BookCategoryDetailRawBean.BookCategoryDetailResultBean.BooksCategoryDetailFinalBean> mCategoryEntity) {
        this.mContext = context;
        this.mCategoryEntity = mCategoryEntity;
    }

    @NonNull
    @Override
    public BookCategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View contentView = LayoutInflater.from(mContext).inflate(
                R.layout.bookshelf_recycle_item_book_category, parent, false);
        return new BookCategoryHolder(contentView);
    }

    @Override
    public void onBindViewHolder(@NonNull BookCategoryHolder holder, int position) {
        BookCategoryDetailRawBean.BookCategoryDetailResultBean.BooksCategoryDetailFinalBean finalBean = mCategoryEntity.get(position);
        if (finalBean != null) {
            //图书封面
            String cover = finalBean.getCover();
            Log.i(TAG, "onBindViewHolder cover url >>>" + cover);
            if (!TextUtils.isEmpty(cover)) {
                Glide.with(mContext)
                        .load(cover)
                        .apply(new RequestOptions().centerCrop()
                                .placeholder(R.drawable.bookshelf_default_cover_port)
                                .error(R.drawable.bookshelf_default_cover_port))
                        .into((holder.ivCategoryCover));
            }
            //圖書名稱
            String title = finalBean.getName();
            if (!TextUtils.isEmpty(title)) {
                holder.tvCategoryName.setText(title);
            }
            //圖書簡介
            String description = finalBean.getDescription();
            if (!TextUtils.isEmpty(description)) {
                holder.tvCategorySummary.setText(description);
            }
            //圖書作者
            String author = finalBean.getAuthor();
            if (!TextUtils.isEmpty(author)) {
                holder.tvCategoryAuthor.setText(author);
            }
            //设置root的点击事件
            holder.rlCategoryRoot.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onBookItemClick(finalBean.getEpubMappingId(), title);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mCategoryEntity != null ? mCategoryEntity.size() : 0;
    }

    /**
     * Item点击事件回调
     */
    public interface OnBookItemListener {
        void onBookItemClick(int bookId, String bookName);
    }

    public void setBookItemListener(OnBookItemListener listener) {
        this.listener = listener;
    }

    public void setCategoryEntity(List<BookCategoryDetailRawBean.BookCategoryDetailResultBean.BooksCategoryDetailFinalBean> categoryEntity) {
        mCategoryEntity.clear();
        mCategoryEntity = categoryEntity;
        notifyDataSetChanged();
    }

    class BookCategoryHolder extends RecyclerView.ViewHolder {
        private PercentRelativeLayout rlCategoryRoot;
        private ImageView ivCategoryCover;
        private TextView tvCategoryName;
        private TextView tvCategorySummary;
        private TextView tvCategoryAuthor;

        BookCategoryHolder(View itemView) {
            super(itemView);
            rlCategoryRoot = itemView.findViewById(R.id.rl_book_category_root);
            ivCategoryCover = itemView.findViewById(R.id.iv_book_category_item_cover);
            tvCategoryName = itemView.findViewById(R.id.tv_book_category_item_name);
            tvCategorySummary = itemView.findViewById(R.id.tv_book_category_item_summary);
            tvCategoryAuthor = itemView.findViewById(R.id.tv_book_category_item_author);
        }
    }
}
