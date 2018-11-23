package com.chinafocus.bookshelf.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinafocus.bookshelf.BuildConfig;
import com.chinafocus.bookshelf.R;
import com.chinafocus.bookshelf.base.BaseActivity;
import com.chinafocus.bookshelf.base.PermissionListener;
import com.chinafocus.bookshelf.presenter.shelves.AbstractShelvesPresenter;
import com.chinafocus.bookshelf.utils.ConstantValue;
import com.chinafocus.bookshelf.utils.SpUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import okhttp3.ResponseBody;

public class SplashActivity extends BaseActivity {

    private static final int INSTALL_SUCCESS = 0;
    private String mVersionName;
//    private ApkNetService mNetService;
    private int mVersionCode;
    private String mInstallApp;
    private File mFile;
    private TextView mTv;
    private long mStart;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            enterHome();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.bookshelf_activity_splash);

        initUI();

        initData();

    }

    @Override
    protected AbstractShelvesPresenter getPresenter() {
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(0);
    }

    private void enterHome() {
        Intent intent = new Intent(this, ShelvesActivity.class);
        startActivity(intent);
        finish();
    }


    @SuppressLint("CheckResult")
    private void installApp(final String path) {
//        mNetService.getAPK(path)
//                .subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.io())
//                .subscribe(new Consumer<ResponseBody>() {
//                    @Override
//                    public void accept(ResponseBody responseBody) {
//
//                        Log.i("MyLog", "ResponseBody");
//                        writeResponseBodyToDisk(responseBody);
//
//                        installLocalApp();
//                    }
//                });
    }

    private void installLocalApp() {


        Intent intent = new Intent(Intent.ACTION_VIEW);
//判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
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
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        mTv.setText("当前版本号是：" + mVersionName);

        requestRuntimePermission(new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, new PermissionListener() {
            @SuppressLint("CheckResult")
            @Override
            public void onGranted() {

                boolean aBoolean = SpUtil.getBoolean(getApplicationContext(), ConstantValue.AUTO_UPDATE);

                if (!aBoolean) {
                    delayEnterHome();
                    return;
                }


//                Retrofit retrofit = RetrofitManager.getRetrofitInstance("http://192.168.0.104:8080/");
//                mNetService = retrofit.create(ApkNetService.class);
//                mNetService.getVersion("update.json")
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Consumer<VersionBean>() {
//                            @Override
//                            public void accept(final VersionBean versionBean) {
//
//                                String ServiceVersion = versionBean.getVersionCode();
//
//                                int versionCodeService = Integer.valueOf(ServiceVersion);
//
//                                if (mVersionCode >= versionCodeService) {
//                                    //已经是最新版本
//                                    //直接进入
//
//                                    delayEnterHome();
//
//
//                                } else {
//                                    //下载安装最新版本
//                                    AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
//                                    builder.setTitle("升级提醒")
//                                            .setMessage(versionBean.getVersionDes())
//                                            .setPositiveButton("下次", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialog, int which) {
//
//                                                    //直接进入
//                                                    enterHome();
//
//                                                }
//                                            })
//                                            .setNegativeButton("升级", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialog, int which) {
//
//
//                                                    String downLoadUri = versionBean.getDownLoadUri();
//                                                    //http://192.168.0.104:8080/app-release.apk
//                                                    String[] split = downLoadUri.split("//");
//                                                    String[] split1 = split[1].split("/");
//                                                    mInstallApp = split1[1];
//                                                    Log.i("MyLog", split1[1]);
//
//                                                    installApp(mInstallApp);
//
//
//                                                }
//                                            })
//                                            .setOnCancelListener(new DialogInterface.OnCancelListener() {
//                                                @Override
//                                                public void onCancel(DialogInterface dialog) {
//                                                    enterHome();
//                                                }
//                                            })
//                                            .show();
//                                }
//
//                            }
//                        });
            }

            @Override
            public void onDenied(List<String> deniedPermission) {

            }
        });


    }

    private void delayEnterHome() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                long end = System.currentTimeMillis();
                long elapse = end - mStart;
                if (elapse < 2000) {
                    try {
                        Thread.sleep(2000 - elapse);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                mHandler.sendEmptyMessage(0);
            }
        }).start();
    }

    private void initUI() {

        mStart = System.currentTimeMillis();

        mTv = findViewById(R.id.tv_version);

        RelativeLayout rlRoot = findViewById(R.id.rl_root);

        AlphaAnimation anim = new AlphaAnimation(0.3f, 1f);
        anim.setDuration(2000);
        rlRoot.startAnimation(anim);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INSTALL_SUCCESS) {
            enterHome();
        }
    }

    @Override
    public void onRefreshFinished(String refreshType, List resultBean) {

    }

    @Override
    public void showTips(String message) {

    }
}
