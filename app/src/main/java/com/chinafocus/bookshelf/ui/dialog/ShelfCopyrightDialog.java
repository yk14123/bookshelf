package com.chinafocus.bookshelf.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.chinafocus.bookshelf.R;


/**
 * Bookshelf书柜版权信息对话框
 *
 * @author HUANGJIN on 2018/11/28
 * @version 1.0
 */
public class ShelfCopyrightDialog extends AlertDialog {
    private Context mContext;

    public ShelfCopyrightDialog(@NonNull Context context) {
        this(context, R.style.AppCompatTransparentDialog);
    }

    private ShelfCopyrightDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
        init();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void init() {
        View mContentView = LayoutInflater.from(mContext).inflate(R.layout.bookshelf_dialog_shelf_copyright, null);
        //初始化控件
        setView(mContentView);
        //設置点击外部可以消失
        setCanceledOnTouchOutside(true);
        //设置可以点击消失
        setCancelable(true);
//        //设置从底部弹出,设置宽度适配屏幕宽度
//        Window window = getWindow();
//        if (window != null) {
//            window.setGravity(Gravity.CENTER);
//            int screenWidth = ScreenUtils.getScreenWidth(mContext);
//            Log.i("MyLog", "ShelfCopyrightDialog  --  screenWidth  -->" + screenWidth);
//            int width = (int) (screenWidth * 0.7);
////            window.getDecorView().setPadding(width, 0, width, 0);
//            WindowManager.LayoutParams lp = window.getAttributes();
//            lp.width = width;
//            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
////            lp.dimAmount = 0f;
//            window.setAttributes(lp);
//        }

    }
}
