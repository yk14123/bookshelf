package com.chinafocus.bookshelf.base;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.chinafocus.bookshelf.presenter.shelves.IShelvesMvpContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 馒头大人 on 2017/11/3.
 */

public abstract class BaseActivity<T> extends AppCompatActivity implements IShelvesMvpContract.IView<T> {
    private IPermissionListener mListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

    }

    /**
     * 初始化View
     */
    protected abstract void initView();


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 动态权限申请，并且申请成功后，要做什么写在回调方法里面
     * 使用该方法，必须先把activity加入到ActivityCollector类里面进行维护，手动add和remove
     *
     * @param permissions 需要申请的权限数组
     * @param listener    回调方法
     */
    protected void requestRuntimePermission(String[] permissions, IPermissionListener listener) {
        //这样的话，可以在其他非activity中使用该方法
//        Activity topActivity = ActivityCollector.getTopActivity();
//        if (topActivity == null) {
//            return;
//        }
        mListener = listener;
        List<String> permissionList = new ArrayList<>();

        for (String permission : permissions) {
            //核对权限是否需要授权
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }
        if (!permissionList.isEmpty()) {
            //对需要授权的权限，启用申请授权对话框
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), 1);
        } else {
            //doSomeThing
            mListener.onGranted();
        }

    }


    /**
     * 如果需要在页面展示回调结果的话，必须让activity继承BaseActivity
     * 而且用户如果点击了始终禁止的话，就会始终调用这个回调函数的mListener.onDenied(deniedPermissions);方法
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

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
                        mListener.onGranted();
                    } else {
                        //集合不为空，表示有拒绝的权限。启用回调方法
                        mListener.onDenied(deniedPermissions);
                    }

                    break;
                }
        }
    }
}
