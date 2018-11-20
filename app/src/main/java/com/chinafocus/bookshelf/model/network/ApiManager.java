package com.chinafocus.bookshelf.model.network;


import com.chinafocus.bookshelf.global.BookShelfApplication;

import javax.net.ssl.SSLSocketFactory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class ApiManager {
    private static final int CONNECT_TIME_OUT = 5000;
    private static final int READ_TIME_OUT = 5000;
    private static final int WRITE_TIME_OUT = 5000;
    //采用单例模式封装Retrofit
//    private static ApiManager sApiManager;
    private Retrofit mRetrofit;

    private ApiManager() {
        //初始化OkHttpClient+Retrofit
//        OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
//                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.MILLISECONDS)
//                .readTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS)
//                .writeTimeout(WRITE_TIME_OUT, TimeUnit.MILLISECONDS)
//                .followRedirects(true)
////                .sslSocketFactory(HttpsUtils.initSSLSocketFactory(), HttpsUtils.initTrustManager())
//                .build();

        SSLSocketFactory sslSocketFactory = HttpsUtils.setCertificatesFromFile(BookShelfApplication.sContext, "expressreader.cn.crt");

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

        mRetrofit = new Retrofit.Builder()
                .baseUrl(ApiConstant.BASE_URL_SHELVES)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
    }

    //静态方法提供ApiManager实例 --->内部封装ApiManager访问实例
    public static ApiManager getInstance() {
        return Holder.INSTANCE;
    }

    static class Holder {
        static ApiManager INSTANCE = new ApiManager();
    }

    //获取ApiService接口调用对象
    public ApiService getService() {
        return mRetrofit.create(ApiService.class);
    }

}
