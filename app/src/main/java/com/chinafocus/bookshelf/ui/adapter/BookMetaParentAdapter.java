package com.chinafocus.bookshelf.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.chinafocus.bookshelf.R;
import com.chinafocus.bookshelf.model.bean.BookMetadataRawBean;

import java.util.List;

public class BookMetaParentAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<BookMetadataRawBean.BookMetadataResultBean.TocBean> mExpandList;
    //回调
    private OnBookMetaListener bookMetaListener;

    public BookMetaParentAdapter(Context context, List<BookMetadataRawBean.BookMetadataResultBean.TocBean> expandList) {
        this.mContext = context;
        this.mExpandList = expandList;
    }

    @Override
    public int getGroupCount() {
        return mExpandList == null ? 0 : mExpandList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        Log.i("", "TreeAdapter getChildrenCount groupPosition>>> " + groupPosition);
        BookMetadataRawBean.BookMetadataResultBean.TocBean tocBean = mExpandList.get(groupPosition);
        if (tocBean != null) {
            List<BookMetadataRawBean.BookMetadataResultBean.TocBean> children = tocBean.getChildren();
            return children == null ? 0 : children.size();
        }
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mExpandList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        BookMetadataRawBean.BookMetadataResultBean.TocBean tocBean = mExpandList.get(groupPosition);
        if (tocBean != null) {
            List<BookMetadataRawBean.BookMetadataResultBean.TocBean> children = tocBean.getChildren();
            return children.get(childPosition);
        }
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view;
        Log.i("============", "getGroupView: ");
        final GroupViewHolder holder;
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.bookshelf_expand_item_parent, parent, false);
            holder = new GroupViewHolder();
            holder.tv = view.findViewById(R.id.tv_expand_group_title);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (GroupViewHolder) view.getTag();
        }
        //适配数据
        BookMetadataRawBean.BookMetadataResultBean.TocBean tocBean = mExpandList.get(groupPosition);
        if (tocBean != null) {
            String title = tocBean.getTitle();
            if (!TextUtils.isEmpty(title)) {
                holder.tv.setText(title);
            }
        }
        return view;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Log.i("============", "getChildView: ");
        View view;
        final ChildViewHolder holder;
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.bookshelf_expand_item_child, parent, false);
            holder = new ChildViewHolder();
            holder.tvTitle = view.findViewById(R.id.tv_expand_child_title);
            holder.lvChildList = view.findViewById(R.id.lv_expand_children);
            holder.lvChildList.setFocusable(false);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ChildViewHolder) view.getTag();
        }

        BookMetadataRawBean.BookMetadataResultBean.TocBean tocBean = mExpandList.get(groupPosition);
        if (tocBean != null) {
            List<BookMetadataRawBean.BookMetadataResultBean.TocBean> childrenList = tocBean.getChildren();
            if (childrenList != null && childrenList.size() != 0) {
                BookMetadataRawBean.BookMetadataResultBean.TocBean children = childrenList.get(childPosition);
                String title = children.getTitle();
                if (!TextUtils.isEmpty(title)) {
                    holder.tvTitle.setText(title);
                }
                holder.tvTitle.setOnClickListener(v -> {
                    if (bookMetaListener != null) {
                        bookMetaListener.onChild(groupPosition, childPosition, children);
                    }
                });
                List<BookMetadataRawBean.BookMetadataResultBean.TocBean> childrenChildren = children.getChildren();
                BookMetaChildAdapter adapter = new BookMetaChildAdapter(mContext, childrenChildren);
                adapter.setBookMetaListener(bookMetaListener);
                holder.lvChildList.setAdapter(adapter);
            }
        }
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    /**
     * 设置item内部事件回调
     */
    public void setBookMetaListener(OnBookMetaListener bookMetaListener) {
        this.bookMetaListener = bookMetaListener;
    }

    public interface OnBookMetaListener {
        /**
         * child item点击事件
         */
        void onChild(int groupPosition, int childPosition, BookMetadataRawBean.BookMetadataResultBean.TocBean tocBean);

        /**
         * inner item點擊事件
         */
        void onItem(BookMetadataRawBean.BookMetadataResultBean.TocBean tocBean);
    }


    class GroupViewHolder {
        TextView tv;
    }

    class ChildViewHolder {
        TextView tvTitle;
        ListView lvChildList;
    }
}
