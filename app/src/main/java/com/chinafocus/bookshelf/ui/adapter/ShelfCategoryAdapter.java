package com.chinafocus.bookshelf.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import com.chinafocus.bookshelf.R;
import com.chinafocus.bookshelf.bean.ShelvesCategoryRawBean;
import com.chinafocus.bookshelf.utils.ManifestUtils;
import com.chinafocus.bookshelf.utils.ScreenUtils;
import com.jakewharton.rxbinding2.view.RxView;
import com.zhy.android.percent.support.PercentFrameLayout;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

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

    private List<ShelvesCategoryRawBean.ShelvesCategoryResultBean> mCategoryEntity;
    //回调
    private OnShelfCategoryListener listener;

    private int mShelfId;
    private Disposable mLlCategoryRootClicks;

    public ShelfCategoryAdapter(Context context, List<ShelvesCategoryRawBean.ShelvesCategoryResultBean> mCategoryEntity, int mShelfId) {
        this.mContext = context;
        this.mCategoryEntity = mCategoryEntity;
        this.mShelfId = mShelfId;
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


        ShelvesCategoryRawBean.ShelvesCategoryResultBean categoriesFinalBean = mCategoryEntity.get(position);
        if (categoriesFinalBean != null) {
            //logo图标
            int categoryId = categoriesFinalBean.getCategoryId();
            String iconUrl = categoriesFinalBean.getIcon();
            String resName = "bookshelf_category_" + position;

            Log.i("bookshelf_category_", "bookshelf_category_ -- >" + resName);


//            Glide
//                    .with(mContext) // could be an issue!
//                    .load(iconUrl)
//                    .into(new SimpleTarget<Drawable>() {
//                        @Override
//                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                            Bitmap bitmap = drawableToBitmap(resource);
//                            Bitmap mergeThumbnailBitmap = mergeThumbnailBitmap(bitmap, mContext);
//                            holder.ivCategoryLogo.setImageBitmap(mergeThumbnailBitmap);
//                        }
//                    });

            holder.ivCategoryLogo.setBackground(new BitmapDrawable(drawBackground(mContext)));

            Glide
                    .with(mContext) // could be an issue!
                    .load(iconUrl)
                    .into(holder.ivCategoryLogo);


            //分类名称
            String name = categoriesFinalBean.getName();
            if (!TextUtils.isEmpty(name)) {
                holder.tvCategoryName.setText(name);
            }


            mLlCategoryRootClicks = RxView.clicks(holder.llCategoryRoot).throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                            if (listener != null) {
                                listener.onShelfCategoryClick(mShelfId,
                                        categoryId, name);
                            }
                        }
                    });

//            //设置root的点击事件
//            holder.llCategoryRoot.setOnClickListener(v -> {
//                if (listener != null) {
//                    listener.onShelfCategoryClick(mShelfId,
//                            categoryId, name);
//                }
//            });
        }
    }

    public void clear() {
        if (mLlCategoryRootClicks != null && mLlCategoryRootClicks.isDisposed()) {
            mLlCategoryRootClicks.dispose();
        }
    }

    public Bitmap drawableToBitmap(Drawable drawable) // drawable 转换成bitmap
    {
        int width = drawable.getIntrinsicWidth();// 取drawable的长宽
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;// 取drawable的颜色格式
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);// 建立对应bitmap
        Canvas canvas = new Canvas(bitmap);// 建立对应bitmap的画布
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);// 把drawable内容画到画布中
        return bitmap;
    }


    //首先传入两张图片
    private Bitmap mergeThumbnailBitmap(Bitmap bitmap, Context context) {

        int width = (int) (ScreenUtils.getScreenWidth(context) * 0.147);

        int bgWidth = bitmap.getWidth();
        int bgHeight = bitmap.getHeight();

        //以其中一张图片的大小作为画布的大小，或者也可以自己自定义
        Bitmap bitmapRaw = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
        //生成画布
        Canvas canvas = new Canvas(bitmapRaw);
        //确定secondBitmap大小比例
        Paint paint = new Paint();
        paint.setAntiAlias(true);//消除锯齿
        paint.setStyle(Paint.Style.FILL_AND_STROKE);//设置空心
        paint.setColor(Color.WHITE);
//        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawColor(context.getResources().getColor(R.color.bookshelf_detail_category_bg_color));
//        canvas.drawColor(Color.TRANSPARENT);
        canvas.drawCircle(width / 2, width / 2, width / 2, paint);


        Matrix matrix = new Matrix();
        matrix.postScale(0.75f, 0.75f);

        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bitmap, 0, 0, bgWidth, bgHeight, matrix,
                true);
//        canvas.drawBitmap(bitmap, matrix, paint);
        canvas.drawBitmap(newbm, (width - newbm.getWidth()) / 2, (width - newbm.getHeight()) / 2, null);

        return bitmapRaw;
    }


    //手动画一个白色背景的圆形
    private Bitmap drawBackground(Context context) {

        int width = (int) (ScreenUtils.getScreenWidth(context) * 0.147);

        //以其中一张图片的大小作为画布的大小，或者也可以自己自定义
        Bitmap bitmapRaw = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
        //生成画布
        Canvas canvas = new Canvas(bitmapRaw);
        //确定secondBitmap大小比例
        Paint paint = new Paint();
        paint.setAntiAlias(true);//消除锯齿
        paint.setStyle(Paint.Style.FILL_AND_STROKE);//设置空心
        paint.setColor(Color.WHITE);
//        canvas.drawARGB(0, 0, 0, 0);
//        canvas.drawColor(context.getResources().getColor(R.color.bookshelf_detail_category_bg_color));
        canvas.drawColor(Color.TRANSPARENT);
        canvas.drawCircle(width / 2, width / 2, width / 2, paint);

        return bitmapRaw;
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

    public void setCategoryEntity(List<ShelvesCategoryRawBean.ShelvesCategoryResultBean> categoryEntity) {
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
