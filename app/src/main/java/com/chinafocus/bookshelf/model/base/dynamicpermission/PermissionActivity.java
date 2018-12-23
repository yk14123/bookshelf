package com.chinafocus.bookshelf.model.base.dynamicpermission;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

public class PermissionActivity extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();

        PermissionManagerFactory.getPermissionManager().requestRuntimePermission(this, new String[]{}, new IPermissionListener() {
            @Override
            public void onGranted() {

            }

            @Override
            public void onDenied(List<String> deniedPermission) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManagerFactory.getPermissionManager().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
