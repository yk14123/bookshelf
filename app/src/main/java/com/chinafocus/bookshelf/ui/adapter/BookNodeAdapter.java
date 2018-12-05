package com.chinafocus.bookshelf.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinafocus.bookshelf.R;
import com.chinafocus.bookshelf.model.bean.BookMetadataRawBean;

import java.util.ArrayList;

/**
 * 图书MetaData目录嵌套数据适配器
 * bean see {@link com.chinafocus.bookshelf.model.bean.BookMetadataRawBean.BookMetadataResultBean.TocBean}
 *
 * @author HUANG_JIN
 * create on 2018/12/1
 */
public class BookNodeAdapter extends RecyclerView.Adapter<BookNodeAdapter.BookNodeHolder> {
    private static final String TAG = "BookNodeAdapter";
    private Context mContext;
    private ArrayList<BookMetadataRawBean.BookMetadataResultBean.TocBean> mTocBeans;
    //当前条目的缩进值
    private int retract;
    //回调
    private OnBookNodeClickListener listener;

    public BookNodeAdapter(Context mContext, ArrayList<BookMetadataRawBean.BookMetadataResultBean.TocBean> mTocBeans) {
        this.mContext = mContext;
        this.mTocBeans = mTocBeans;
        //缩进值，大家可以将它配置在资源文件中，从而实现适配
        retract = (int) (mContext.getResources().getDisplayMetrics().density * 10 + 0.5f);
        Log.d(TAG, "BookNodeAdapter: retract >>> " + retract);
    }

    @NonNull
    @Override
    public BookNodeHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View contentView = LayoutInflater.from(mContext).inflate(
                R.layout.bookshelf_recycle_item_book_meta, viewGroup, false);
        return new BookNodeHolder(contentView);
    }

    @Override
    public void onBindViewHolder(@NonNull BookNodeHolder bookNodeHolder, int i) {
        BookMetadataRawBean.BookMetadataResultBean.TocBean baseNode = mTocBeans.get(i);
        if (baseNode != null) {
            String label = baseNode.getLabel();
            int level = baseNode.getLevel();
            if (!TextUtils.isEmpty(label)) {
                //添加两个空格字符,实现缩进字符的效果 >>> 除去level 1层级
                if (level != 0) {
                    label = getLabel(label, level);
                    bookNodeHolder.tvCaptureTitle.setTextAppearance(mContext, R.style.BookShelfItemTextAppearance);
                }
                bookNodeHolder.tvCaptureTitle.setText(label);
                Log.d(TAG, "onBindViewHolder: label >>> " + label + "  level >>> " + level);
            }
            //设置点选回调事件
            bookNodeHolder.llBookMetaRoot.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onNodeClick(baseNode.getFull());
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
        void onNodeClick(String pageId);
    }

    public void setBookNodeClickListener(OnBookNodeClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return mTocBeans == null ? 0 : mTocBeans.size();
    }

    class BookNodeHolder extends RecyclerView.ViewHolder {
        private LinearLayout llBookMetaRoot;
        private TextView tvCaptureTitle;

        BookNodeHolder(@NonNull View itemView) {
            super(itemView);
            llBookMetaRoot = itemView.findViewById(R.id.ll_book_meta_item_root);
            tvCaptureTitle = itemView.findViewById(R.id.tv_book_meta_item_title);
        }
    }
}
