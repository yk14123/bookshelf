package com.chinafocus.bookshelf.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * @author create by HUANGJIN on 2018/11/1
 * @version 1.0
 * @description manifest工具类
 */
public class ManifestUtils {

    private static final String TAG = "ManifestUtils";

    /**
     * 返回Manifest指定meta-data值
     *
     * @param context 全局context
     * @param key     meta-data key
     * @return 成功-value
     * 失败-""
     */
    public static String getMetaData(Context context, String key) {
        ApplicationInfo app_info = null;
        try {
            app_info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (app_info == null || app_info.metaData == null) {
                return "";
            } else {
                Object obj = app_info.metaData.get(key);
                if (obj != null) {
                    return obj.toString();
                } else {
                    return "";
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "getMetaData error", e);
            return "";
        }
    }

    /**
     * 获取版本名
     *
     * @param context 全局context
     * @return version name
     */
    public static String getVersionName(Context context) {
        String version = "0.0";
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            version = packInfo.versionName;
        } catch (Exception e) {
            Log.e(TAG, "getVersionName error", e);
        }

        return version;
    }

    /**
     * 获取包名
     *
     * @param context 全局context
     * @return package name
     */
    public static String getPackageName(Context context) {
        String packageName = "com.chinafocus.bookshelf";
        ApplicationInfo appInfo = context.getApplicationInfo();
        if (appInfo != null) {
            packageName = appInfo.packageName;
        }
        return packageName;
    }

}
