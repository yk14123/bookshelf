package com.chinafocus.bookshelf.ui.dialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chinafocus.bookshelf.R;
import com.chinafocus.bookshelf.utils.ScreenUtils;

/**
 * 点击查看大图dialog
 *
 * @author HUANG_JIN
 * @version 1.0
 * create at 2018/12/4 16:44
 */
public class BookCoverDialog extends PopupWindow {
    private Context mContext;

    private ImageView mIvBookCover;

    private View parent;

    public BookCoverDialog(Context context, View parent) {
        this(context, null, parent);
    }

    private BookCoverDialog(Context context, AttributeSet attrs, View parent) {
        this(context, attrs, 0, parent);
    }

    private BookCoverDialog(Context context, AttributeSet attrs, int defStyleAttr, View parent) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        this.parent = parent;
        init();
    }

    /**
     * 初始化相关配置
     */
    private void init() {
        //屏幕高度
        int screenHeight = ScreenUtils.getScreenHeight(mContext);
        //状态栏高度
        int stateBarHeight = ScreenUtils.getStatusBarHeight(mContext);
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.bookshelf_dialog_book_cover, null);
        contentView.setOnClickListener(v -> dismiss());
        mIvBookCover = contentView.findViewById(R.id.iv_book_cover_show);
        //設置内容布局
        setContentView(contentView);
        //设置点击外部可以消失
        setOutsideTouchable(true);
        //设置背景
        setBackgroundDrawable(new ColorDrawable(0));
        //设置动画
        setAnimationStyle(R.style.ActivityScaleAnimation);
        //设置宽高
        int height = screenHeight - stateBarHeight - parent.getHeight();
        setHeight(height);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public void setImageUrl(@NonNull String imgUrl) {
        //设置图片
        Glide.with(mContext)
                .load(imgUrl)
                .apply(new RequestOptions()
                        .error(R.drawable.bookshelf_default_cover_port)
                        .placeholder(R.drawable.bookshelf_default_cover_port))
                .into(mIvBookCover);
    }

    public void show() {
        showAsDropDown(parent);
    }

}
