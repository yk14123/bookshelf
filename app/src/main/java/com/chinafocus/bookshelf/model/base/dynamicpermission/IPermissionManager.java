package com.chinafocus.bookshelf.model.base.dynamicpermission;

import android.app.Activity;
import android.support.annotation.NonNull;

public interface IPermissionManager {

    /**
     * 动态权限申请，并且申请成功后，要做什么写在回调方法里面
     *
     * @param activity
     * @param permissions
     * @param listener    权限被允许或者被拒绝的回调
     */
    void requestRuntimePermission(Activity activity, String[] permissions, IPermissionListener listener);

    /**
     * 如果需要在页面展示回调结果的话，必须让activity继承BaseActivity
     * 而且用户如果点击了始终禁止的话，就会始终调用这个回调函数的mListener.onDenied(deniedPermissions);方法
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);

}
