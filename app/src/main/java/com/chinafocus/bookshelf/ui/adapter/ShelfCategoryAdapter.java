package com.chinafocus.bookshelf.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chinafocus.bookshelf.R;
import com.chinafocus.bookshelf.model.bean.ShelvesCategoryResultBean;
import com.chinafocus.bookshelf.utils.ManifestUtils;
import com.zhy.android.percent.support.PercentFrameLayout;

import java.util.List;

/**
 * ShelfDetail书柜首页分类标签数据适配器
 *
 * @author HUANG_JIN
 * @version 1.0
 * create at 2018/11/26 14:47
 */
public class ShelfCategoryAdapter extends RecyclerView.Adapter<ShelfCategoryAdapter.ShelfCategoryHolder> {
    private static final String TAG = "ShelfCategoryAdapter";
    private Context mContext;

    private List<ShelvesCategoryResultBean.ShelvesCategoriesFinalBean> mCategoryEntity;
    //回调
    private OnShelfCategoryListener listener;


    public ShelfCategoryAdapter(Context context, List<ShelvesCategoryResultBean.ShelvesCategoriesFinalBean> mCategoryEntity) {
        this.mContext = context;
        this.mCategoryEntity = mCategoryEntity;
    }

    @NonNull
    @Override
    public ShelfCategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View contentView = LayoutInflater.from(mContext).inflate(
                R.layout.bookshelf_recycle_item_shelf_category, parent, false);
        return new ShelfCategoryHolder(contentView);
    }

    @Override
    public void onBindViewHolder(@NonNull ShelfCategoryHolder holder, int position) {
        ShelvesCategoryResultBean.ShelvesCategoriesFinalBean categoriesFinalBean = mCategoryEntity.get(position);
        if (categoriesFinalBean != null) {
            //logo图标
            int categoryId = categoriesFinalBean.getCategoryId();
            String resName = "bookshelf_category_" + categoryId;
            Glide.with(mContext)
                    .load(getDrawableRes(resName))
                    .into((holder.ivCategoryLogo));
            //分类名称
            String name = categoriesFinalBean.getName();
            if (!TextUtils.isEmpty(name)) {
                holder.tvCategoryName.setText(name);
            }

            //设置root的点击事件
            holder.llCategoryRoot.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onShelfCategoryClick(categoriesFinalBean.getShelfId(),
                            categoryId, name);
                }
            });
        }
    }

    /**
     * 获取drawable资源
     */
    private int getDrawableRes(String resName) {
        return mContext.getResources().getIdentifier(resName, "drawable", ManifestUtils.getPackageName(mContext));
    }

    @Override
    public int getItemCount() {
        return mCategoryEntity != null ? mCategoryEntity.size() : 0;
    }

    /**
     * Item点击事件回调
     */
    public interface OnShelfCategoryListener {
        void onShelfCategoryClick(int shelfId, int category, String categoryName);
    }

    public void setShelfCategoryListener(OnShelfCategoryListener listener) {
        this.listener = listener;
    }

    public void setCategoryEntity(List<ShelvesCategoryResultBean.ShelvesCategoriesFinalBean> categoryEntity) {
        mCategoryEntity.clear();
        mCategoryEntity = categoryEntity;
        notifyDataSetChanged();
    }

    class ShelfCategoryHolder extends RecyclerView.ViewHolder {
        private PercentFrameLayout llCategoryRoot;
        private ImageView ivCategoryLogo;
        private TextView tvCategoryName;

        ShelfCategoryHolder(View itemView) {
            super(itemView);
            llCategoryRoot = itemView.findViewById(R.id.ll_shelf_category_root);
            ivCategoryLogo = itemView.findViewById(R.id.iv_shelf_category_item_logo);
            tvCategoryName = itemView.findViewById(R.id.tv_shelf_category_item_name);
        }
    }
}
