package com.chinafocus.bookshelf.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chinafocus.bookshelf.R;

/**
 * 项目loading视图工具类
 *
 * @author HUANG_JIN
 * @version 1.0
 * create at 2018/12/6 9:43
 */
public class ProgressBarUtils {
    private static AlertDialog instance = null;

    /**
     * 开启loading视图
     *
     * @param context 上下文
     * @param message 提示信息
     */
    public static void showProgressDialog(@NonNull Context context, String message) {
        if (instance == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View contentView = LayoutInflater.from(context).inflate(
                    R.layout.bookshelf_include_app_loading_layout, null);
            TextView tv = contentView.findViewById(R.id.tv_bookshelf_loading);
            if (!TextUtils.isEmpty(message)) {
                tv.setText(message);
            }
            builder.setView(contentView);
            builder.setTitle("");
            builder.setCancelable(false);
            instance = builder.create();
        }
        if (!instance.isShowing()) {
            instance.show();
        }
    }

    /**
     * 关闭loading视图
     */
    public static void dismiss() {
        if (instance != null && instance.isShowing()) {
            instance.dismiss();
            instance = null;
        }
    }
}
