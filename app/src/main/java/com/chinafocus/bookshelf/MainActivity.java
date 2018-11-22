package com.chinafocus.bookshelf;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chinafocus.bookshelf.base.BaseActivity;
import com.chinafocus.bookshelf.base.PermissionListener;
import com.chinafocus.bookshelf.model.bean.VersionBean;
import com.chinafocus.bookshelf.presenter.shelves.AbstractShelvesPresenter;

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

    @Override
    protected AbstractShelvesPresenter getPresenter() {
        return null;
    }


    @SuppressLint("CheckResult")
    public void checkVersion(View view) {

        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.103:8080/")
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
                            //http://192.168.0.103:8080/app-release.apk
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
            Toast.makeText(MainActivity.this, "安全全部搞定了！！", Toast.LENGTH_SHORT).show();
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
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);//要加这句，不然文件一样解析出错
            Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".fileProvider", mFile);
//上下文，authorities名，文件File
            grantUriPermission(getPackageName(), contentUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);//设置读写权限。不然下载的文件，无法写入手机或读取！

            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(mFile), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        startActivityForResult(intent, INSTALL_SUCCESS);

    }

    private void writeResponseBodyToDisk(ResponseBody responseBody) {

        InputStream inputStream = responseBody.byteStream();

//        mFile = new File(getFilesDir().getAbsoluteFile() + File.separator + mInstallApp);

        mFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + mInstallApp);
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

    @Override
    public void onRefreshFinished(String refreshType, List resultBean) {

    }

    @Override
    public void showTips(String message) {

    }
}
