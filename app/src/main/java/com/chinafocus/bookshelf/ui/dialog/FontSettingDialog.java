package com.chinafocus.bookshelf.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.chinafocus.bookshelf.R;
import com.chinafocus.bookshelf.global.BookShelfConstant;
import com.chinafocus.bookshelf.utils.SpUtil;

/**
 * 字体选择对话框
 *
 * @author HUANGJIN on 2018/11/15
 * @version 1.0
 */
public class FontSettingDialog extends AlertDialog implements View.OnClickListener {
    private static final String TAG = "FontSettingDialog";
    private Context mContext;
    //特大字号
    private TextView tvFontLarger;
    //大字号
    private TextView tvFontLarge;
    //中字号
    private TextView tvFontNormal;
    //小字号
    private TextView tvFontSmall;
    //取消
    private TextView tvFontCancel;
    //未选中文字颜色
    private int mUncheckTextColor;
    //选中的文字颜色
    private int mCheckedTextColor;
    //事件回调
    private OnFontOptionListener onFontOptionListener;

    public FontSettingDialog(@NonNull Context context) {
        this(context, R.style.BookShelfAppCompatTransparentDialog);
    }

    private FontSettingDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
        init();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void init() {
        mCheckedTextColor = mContext.getResources().getColor(R.color.bookshelf_title_text_color);
        mUncheckTextColor = mContext.getResources().getColor(R.color.bookshelf_content_text_color);

        View mContentView = LayoutInflater.from(mContext).inflate(R.layout.bookshelf_dialog_font_option, null);
        //初始化控件
        tvFontLarger = mContentView.findViewById(R.id.tv_font_option_larger);
        tvFontLarge = mContentView.findViewById(R.id.tv_font_option_large);
        tvFontNormal = mContentView.findViewById(R.id.tv_font_option_normal);
        tvFontSmall = mContentView.findViewById(R.id.tv_font_option_small);
        tvFontCancel = mContentView.findViewById(R.id.tv_font_option_cancel);
        tvFontLarger.setOnClickListener(this);
        tvFontLarge.setOnClickListener(this);
        tvFontNormal.setOnClickListener(this);
        tvFontSmall.setOnClickListener(this);
        tvFontCancel.setOnClickListener(this);
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

        initFontSetting();
    }

    /**
     * 默认初始化设置选项
     * */
    private void initFontSetting() {
        int fontZoomSize = SpUtil.getInt(mContext, BookShelfConstant.BOOKSHELF_FONT_SETTING, BookShelfConstant.NORMAL);
        Log.d(TAG, "onShow: fontZoomSize >>> " + fontZoomSize);
        switch (fontZoomSize) {
            case BookShelfConstant.LARGER:
                setChecked(tvFontLarger, fontZoomSize);
                break;
            case BookShelfConstant.LARGE:
                setChecked(tvFontLarge, fontZoomSize);
                break;
            case BookShelfConstant.NORMAL:
                setChecked(tvFontNormal, fontZoomSize);
                break;
            case BookShelfConstant.SMALLER:
                setChecked(tvFontSmall, fontZoomSize);
                break;
        }
    }

    /**
     * 设置当前字体大小默认选择项的突出效果
     *
     * @param view         当前选中项View
     * @param fontZoomSize 当前默认的WebView的textZoomSize的值
     */
    private void setChecked(@NonNull TextView view, int fontZoomSize) {
        setUnchecked();
        view.setTextColor(mCheckedTextColor);
        view.setSelected(true);
        SpUtil.setInt(mContext, BookShelfConstant.BOOKSHELF_FONT_SETTING, fontZoomSize);
        if (onFontOptionListener != null) {
            onFontOptionListener.onFontSelected(fontZoomSize);
        }
        dismiss();
    }

    /**
     * 清除相关控件的状态
     */
    private void setUnchecked() {
        tvFontLarger.setSelected(false);
        tvFontLarger.setTextColor(mUncheckTextColor);
        tvFontLarge.setSelected(false);
        tvFontLarge.setTextColor(mUncheckTextColor);
        tvFontNormal.setSelected(false);
        tvFontNormal.setTextColor(mUncheckTextColor);
        tvFontSmall.setSelected(false);
        tvFontSmall.setTextColor(mUncheckTextColor);
    }

    /**
     * 保存設置并改变当前内容页面布局
     */
    @Override
    public void onClick(View v) {
        if (v == tvFontLarger) {
            setChecked(tvFontLarger, BookShelfConstant.LARGER);
        } else if (v == tvFontLarge) {
            setChecked(tvFontLarge, BookShelfConstant.LARGE);
        } else if (v == tvFontNormal) {
            setChecked(tvFontNormal, BookShelfConstant.NORMAL);
        } else if (v == tvFontSmall) {
            setChecked(tvFontSmall, BookShelfConstant.SMALLER);
        } else if (v == tvFontCancel) {
            dismiss();
        }
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
    public void setOnFontOptionListener(OnFontOptionListener listener) {
        this.onFontOptionListener = listener;
    }
}
