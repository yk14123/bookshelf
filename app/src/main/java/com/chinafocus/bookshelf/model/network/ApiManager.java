package com.chinafocus.bookshelf.model.network;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;


public class ApiManager {
    private static final int CONNECT_TIME_OUT = 5000;
    private static final int READ_TIME_OUT = 5000;
    private static final int WRITE_TIME_OUT = 5000;
    //采用单例模式封装Retrofit
    private static ApiManager sApiManager;
    private static Retrofit sRetrofit;

    private ApiManager() {
        //初始化OkHttpClient+Retrofit
        OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.MILLISECONDS)
                .readTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS)
                .writeTimeout(WRITE_TIME_OUT, TimeUnit.MILLISECONDS)
                .followRedirects(true)
//                .sslSocketFactory(HttpsUtils.initSSLSocketFactory(), HttpsUtils.initTrustManager())
                .build();
        sRetrofit = new Retrofit.Builder()
                .baseUrl(ApiConstant.BASE_URL_TEST)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create())
                .client(mOkHttpClient)
                .build();
    }


    //静态方法提供ApiManager实例 --->内部封装ApiManager访问实例
    public static ApiManager getInstance() {
        if (sApiManager == null) {
            synchronized (ApiManager.class) {
                if (sApiManager == null) {
                    sApiManager = new ApiManager();
                }
            }
        }
        return sApiManager;
    }

    //获取ApiService接口调用对象
    public ApiService getService() {
        return sRetrofit.create(ApiService.class);
    }

}
