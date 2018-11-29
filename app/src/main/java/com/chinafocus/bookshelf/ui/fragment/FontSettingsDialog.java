package com.chinafocus.bookshelf.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.chinafocus.bookshelf.R;
import com.chinafocus.bookshelf.global.BookShelfConstant;
import com.chinafocus.bookshelf.ui.widgets.PercentSeekBar;
import com.chinafocus.bookshelf.utils.SpUtil;

/**
 * 字体选择对话框
 *
 * @author HUANGJIN on 2018/11/15
 * @version 1.0
 */
public class FontSettingsDialog extends AlertDialog implements PercentSeekBar.OnPointResultListener,
        DialogInterface.OnShowListener {
    private static final String TAG = "FontSettingsDialog";
    //当前字体常量
    public static final int FONT_SMALLER = 0;
    public static final int FONT_STANDARD = 1;//standard
    public static final int FONT_LARGE = 2;
    public static final int FONT_LARGER = 3;
    public static final int FONT_LARGEST = 4;
    private Context mContext;
    //事件回调
    private OnFontOptionListener onFontListener;
    private PercentSeekBar mPercentSeekBar;

    public FontSettingsDialog(@NonNull Context context) {
        this(context, R.style.AppCompatTranslucentDialog);
    }

    private FontSettingsDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
        init();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void init() {
        View mContentView = LayoutInflater.from(mContext).inflate(R.layout.bookshelf_dialog_font_setting, null);
        //字体刻度View
        mPercentSeekBar = mContentView.findViewById(R.id.sb_bookshelf_font);
        mPercentSeekBar.setOnPointResultListener(this);
        //初始化控件
        setView(mContentView);
        //設置点击外部可以消失
        setCanceledOnTouchOutside(true);
        //设置从底部弹出,设置宽度适配屏幕宽度
        Window window = getWindow();
        if (window != null) {
            window.setGravity(Gravity.BOTTOM);
            window.getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
        }
        setOnShowListener(this);
    }

    @Override
    public void onPointResult(int position) {
        Log.d(TAG, "onPointResult: position >>> " + position);
        if (onFontListener != null) {
            SpUtil.setInt(mContext, BookShelfConstant.BOOKSHELF_FONT_SETTING, position);
            onFontListener.onFontSelected(position);
            dismiss();
        }
    }

    @Override
    public void onShow(DialogInterface dialog) {
        int fontOption = SpUtil.getInt(mContext, BookShelfConstant.BOOKSHELF_FONT_SETTING);
    }


    //选择回调事件
    public interface OnFontOptionListener {
        void onFontSelected(int fontOption);
    }

    /**
     * 设置事件回调
     *
     * @param listener 回调对象
     */
    public void setOnFontListener(OnFontOptionListener listener) {
        this.onFontListener = listener;
    }
}
