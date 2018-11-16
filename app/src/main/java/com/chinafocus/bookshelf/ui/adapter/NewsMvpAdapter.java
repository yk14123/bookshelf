package com.chinafocus.bookshelf.ui.adapter;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chinafocus.bookshelf.R;
import com.chinafocus.bookshelf.model.bean.NewsBean;

import java.util.List;

public class NewsMvpAdapter extends RecyclerView.Adapter<NewsMvpAdapter.NewsMvpHolder> {


    private List<NewsBean> mNewsEntity;

    @NonNull
    @Override
    public NewsMvpHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NewsMvpHolder(parent, R.layout.bookshelf_recycle_item_news);
    }

    @Override
    public void onBindViewHolder(NewsMvpHolder holder, int position) {
        if (mNewsEntity != null) {
            NewsBean newsBean = mNewsEntity.get(position);
            if (newsBean != null) {
                TextView titleView = holder.getView(R.id.tv_title);
                titleView.setText(newsBean.getTitle());
            }
        }
    }

    @Override
    public int getItemCount() {
        return mNewsEntity != null ? mNewsEntity.size() : 0;
    }

    public void setNewsResult(List<NewsBean> newsEntity) {
        mNewsEntity = newsEntity;
    }

    class NewsMvpHolder extends RecyclerView.ViewHolder {

        NewsMvpHolder(ViewGroup holder, @LayoutRes int layoutId) {
            super(LayoutInflater.from(holder.getContext()).inflate(layoutId, holder, false));
        }

        <T extends View> T getView(@IdRes int resId) {
            Object childView = itemView.getTag(resId);
            if (childView == null) {
                childView = itemView.findViewById(resId);
                itemView.setTag(resId, childView);
            }
            return (T) childView;
        }
    }
}
