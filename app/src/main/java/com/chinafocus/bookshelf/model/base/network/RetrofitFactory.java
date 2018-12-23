package com.chinafocus.bookshelf.model.base.network;

import com.chinafocus.bookshelf.global.BookShelfApplication;

import javax.net.ssl.SSLSocketFactory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

class RetrofitFactory {

    static Retrofit getDownloadService() {
        SSLSocketFactory sslSocketFactory = HttpsUtils.setCertificatesFromFile(BookShelfApplication.sContext, "expressreader.cn.crt");

        //证书无效处理
        sslSocketFactory = null;

        OkHttpClient client;
        if (sslSocketFactory != null) {
            client = new OkHttpClient.Builder()
                    .sslSocketFactory(sslSocketFactory)
                    .hostnameVerifier(HttpsUtils.hostnameVerifier)
                    .build();
        } else {
            client = new OkHttpClient.Builder()
                    .sslSocketFactory(HttpsUtils.getSLLContext().getSocketFactory())
                    .hostnameVerifier(HttpsUtils.hostnameVerifier)
                    .build();
        }

        return new Retrofit.Builder()
                .baseUrl(ApiConstant.BASE_URL_SHELVES)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
    }

    static Retrofit getNewApkService() {

        return new Retrofit.Builder()
                .baseUrl(ApiConstant.BASE_APK_SHELVES)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(new OkHttpClient.Builder()
                        .sslSocketFactory(HttpsUtils.getSLLContext().getSocketFactory())
                        .hostnameVerifier(HttpsUtils.hostnameVerifier)
                        .build())
                .build();
    }
}
