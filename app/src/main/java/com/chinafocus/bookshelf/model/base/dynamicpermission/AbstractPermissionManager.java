package com.chinafocus.bookshelf.model.base.dynamicpermission;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

abstract class AbstractPermissionManager implements IPermissionManager {

    private IPermissionListener mListener;

    @Override
    public void requestRuntimePermission(Activity activity, String[] permissions, IPermissionListener listener) {
        //这样的话，可以在其他非activity中使用该方法
//        Activity topActivity = ActivityCollector.getTopActivity();
//        if (topActivity == null) {
//            return;
//        }
        List<String> permissionList = new ArrayList<>();

        for (String permission : permissions) {
            //核对权限是否需要授权
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }
        if (!permissionList.isEmpty()) {
            //对需要授权的权限，启用申请授权对话框
            mListener = listener;
            ActivityCompat.requestPermissions(activity, permissionList.toArray(new String[permissionList.size()]), 1);
        } else {
            //doSomeThing
            if (listener != null) {
                listener.onGranted();
            }
//            onGranted();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                //2组数组大小完全一样
//                Log.i("MyLog",permissions.length+"-----"+grantResults.length);

                if (grantResults.length > 0) {
                    List<String> deniedPermissions = new ArrayList<>();

                    for (int i = 0; i < grantResults.length; i++) {
                        int grantResult = grantResults[i];
                        String permission = permissions[i];
                        //如果结果有非通过的权限，就加入到集合中做处理
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            deniedPermissions.add(permission);
                        }
                    }

                    if (deniedPermissions.isEmpty()) {
                        //doSomeThing
                        if (mListener != null) {
                            mListener.onGranted();
//                        onGranted();
                        }
                    } else {
                        //集合不为空，表示有拒绝的权限。启用回调方法
                        if (mListener != null) {
                            mListener.onDenied(deniedPermissions);
//                        onDenied(deniedPermissions);
                        }
                    }

                    break;
                }
//                mListener = null;
        }
    }

}
