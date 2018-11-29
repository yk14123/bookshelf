package com.chinafocus.bookshelf.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinafocus.bookshelf.R;
import com.chinafocus.bookshelf.model.bean.BookMetadataRawBean;

import java.util.List;

public class BookMetaChildAdapter extends BaseAdapter {
    private Context mContext;
    private List<BookMetadataRawBean.BookMetadataResultBean.TocBean> childrenBeanList;
    //回调
    private BookMetaParentAdapter.OnBookMetaListener bookMetaListener;

    BookMetaChildAdapter(Context context, List<BookMetadataRawBean.BookMetadataResultBean.TocBean> childrenBeanList) {
        this.mContext = context;
        this.childrenBeanList = childrenBeanList;
    }

    @Override
    public int getCount() {
        return childrenBeanList == null ? 0 : childrenBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return childrenBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChildViewHolder holder;
        if (convertView == null) {
            holder = new ChildViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.bookshelf_listview_item_child, parent, false);
            holder.textView = convertView.findViewById(R.id.tv_child_item_title);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }
        //适配数据
        BookMetadataRawBean.BookMetadataResultBean.TocBean tocBean = childrenBeanList.get(position);
        if (tocBean != null) {
            String title = tocBean.getTitle();
            if (!TextUtils.isEmpty(title)) {
                holder.textView.setText(title);
            }
            holder.textView.setOnClickListener(v -> {
                if (bookMetaListener != null) {
                    bookMetaListener.onItem(tocBean);
                }
            });
        }
        return convertView;
    }


    void setBookMetaListener(BookMetaParentAdapter.OnBookMetaListener bookMetaListener) {
        this.bookMetaListener = bookMetaListener;
    }

    class ChildViewHolder {
        TextView textView;
    }
}
