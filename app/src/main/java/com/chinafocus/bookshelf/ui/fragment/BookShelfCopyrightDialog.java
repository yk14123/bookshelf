package com.chinafocus.bookshelf.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.chinafocus.bookshelf.R;
import com.chinafocus.bookshelf.utils.ScreenUtils;


/**
 * Bookshelf书柜版权信息对话框
 *
 * @author HUANGJIN on 2018/11/28
 * @version 1.0
 */
public class BookShelfCopyrightDialog extends AlertDialog {
    private Context mContext;

    public BookShelfCopyrightDialog(@NonNull Context context) {
        this(context, R.style.AppCompatTransparentDialog);
    }

    private BookShelfCopyrightDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
        init();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void init() {
        View mContentView = LayoutInflater.from(mContext).inflate(R.layout.bookshelf_dialog_copyright_view, null);
        //初始化控件
        setView(mContentView);
        //設置点击外部可以消失
        setCanceledOnTouchOutside(true);
        //设置从底部弹出,设置宽度适配屏幕宽度
        Window window = getWindow();
        if (window != null) {
            window.setGravity(Gravity.CENTER);
            int screenWidth = ScreenUtils.getScreenWidth(mContext);
            int padding = (int) (screenWidth * 0.3f) / 2;
            window.getDecorView().setPadding(padding, 0, padding, 0);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
        }
        //设置点击外部消失
        setCanceledOnTouchOutside(true);
    }

}
