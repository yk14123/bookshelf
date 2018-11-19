package com.chinafocus.bookshelf.global;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;

import java.io.File;

public class BookShelfApplication extends Application {

//    public static IShelvesApi mIShelvesApi;

    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        Global.serializableFileDir = getFilesDir().getAbsolutePath() + File.separator + "Ser";
        Global.serializableFileDirNotDelete = getFilesDir().getAbsolutePath() + File.separator + "SerNotDelete";

        LeakCanary.install(this);

        mContext = getApplicationContext();
//        initNetWork();

    }

    public Context getContext() {
        return mContext;
    }

    private void initNetWork() {

//        OkHttpClient client = new OkHttpClient.Builder()
//                .sslSocketFactory(HttpsUtils.getSLLContext().getSocketFactory())
//                .hostnameVerifier(HttpsUtils.hostnameVerifier)
//                .build();

//        OkHttpClient client = new OkHttpClient.Builder()
//                .sslSocketFactory(HttpsUtils.setCertificatesFromFile(getApplicationContext(), "expressreader.cn.crt"))
//                .hostnameVerifier(HttpsUtils.hostnameVerifier)
//                .build();
//
//        mIShelvesApi = new Retrofit.Builder()
//                .baseUrl("https://book.expressreader.cn/")
//                .addConverterFactory(ScalarsConverterFactory.create())
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .client(client)
//                .build().create(IShelvesApi.class);
    }

}
