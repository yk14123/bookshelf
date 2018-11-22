package com.chinafocus.bookshelf;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chinafocus.bookshelf.base.BaseActivity;
import com.chinafocus.bookshelf.base.PermissionListener;
import com.chinafocus.bookshelf.ui.activity.VersionBean;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends BaseActivity {

    private static final int INSTALL_SUCCESS = 0;
    private String mVersionName;
    private NetService mNetService;
    private int mVersionCode;
    private String mInstallApp;
    private File mFile;
    private TextView mTv_version_name;
    private TextView mTv_version_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookshelf_activity_main);

        initUI();

        initData();

        requestRuntimePermission();

    }


    @SuppressLint("CheckResult")
    public void checkVersion(View view) {

        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.123.106:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        mNetService = mRetrofit.create(NetService.class);
        mNetService.getVersion("update.json")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<VersionBean>() {
                    @Override
                    public void accept(final VersionBean versionBean) {

                        String ServiceVersion = versionBean.getVersionCode();

                        int versionCodeService = Integer.valueOf(ServiceVersion);

                        Log.i("MyLog", "versionCodeService = " + versionCodeService);

                        if (mVersionCode >= versionCodeService) {
                            //已经是最新版本
                            //直接进入
                            Toast.makeText(MainActivity.this, "目前就是最新版本，无需改变", Toast.LENGTH_SHORT).show();
                        } else {
//
                            String downLoadUri = versionBean.getDownLoadUri();
//                            //http://192.168.0.104:8080/app-release.apk
                            String[] split = downLoadUri.split("//");
                            String[] split1 = split[1].split("/");
                            mInstallApp = split1[1];
                            Log.i("MyLog", split1[1]);
//
                            installApp(mInstallApp);
                        }
                    }
                });

    }

    private void initUI() {
        mTv_version_name = findViewById(R.id.tv_version_name);
        mTv_version_code = findViewById(R.id.tv_version_code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INSTALL_SUCCESS) {
//            enterHome();
            initData();
        }
    }

    private void requestRuntimePermission() {
        requestRuntimePermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionListener() {
            @Override
            public void onGranted() {
                //在这个里面写权限申请完毕后要做的事情。。。
                Toast.makeText(MainActivity.this, "权限全部搞定了！！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDenied(List<String> deniedPermission) {

                for (String s : deniedPermission) {
                    Toast.makeText(MainActivity.this, "权限：" + s + "被拒绝了", Toast.LENGTH_SHORT).show();
                }
//                Toast.makeText(MainActivity.this, "权限：被拒绝了", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("CheckResult")
    private void installApp(final String path) {
        mNetService.getAPK(path)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) {

                        Log.i("MyLog", "ResponseBody");
                        writeResponseBodyToDisk(responseBody);

                        installLocalApp();
                    }
                });

    }

    private void installLocalApp() {


        Intent intent = new Intent(Intent.ACTION_VIEW);
//判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Log.i("MyLog", "系统大于7.0版本，准备安装");
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".fileProvider", mFile);

            grantUriPermission(getPackageName(), contentUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(mFile), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
//        intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, INSTALL_SUCCESS);

    }

    private void writeResponseBodyToDisk(ResponseBody responseBody) {

        InputStream inputStream = responseBody.byteStream();

        mFile = new File(getFilesDir().getAbsoluteFile() + File.separator + mInstallApp);
        try (

                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(mFile));
                BufferedInputStream bis = new BufferedInputStream(inputStream)) {
            int len;
            byte[] bytes = new byte[1024];
            while (-1 != (len = bis.read(bytes))) {
                bos.write(bytes, 0, len);
            }

            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @SuppressLint("SetTextI18n")
    private void initData() {

        PackageManager pm = getPackageManager();

        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);

            mVersionName = packageInfo.versionName;
            mVersionCode = packageInfo.versionCode;

            Log.i("MyLog", "versionCode=" + mVersionCode);

            mTv_version_name.setText("当前版本名称是：" + mVersionName);
            mTv_version_code.setText("当前版本号码是：" + mVersionCode);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

}
