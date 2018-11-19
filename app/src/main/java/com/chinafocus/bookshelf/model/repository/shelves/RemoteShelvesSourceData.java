package com.chinafocus.bookshelf.model.repository.shelves;


import com.chinafocus.bookshelf.model.network.ApiManager;

import io.reactivex.Observable;

public class RemoteShelvesSourceData {


    public RemoteShelvesSourceData() {

//        HttpsUtils.setCertificates(new )

//        OkHttpClient client = new OkHttpClient.Builder()
//                .sslSocketFactory(HttpsUtils.getSLLContext().getSocketFactory())
//                .hostnameVerifier(HttpsUtils.hostnameVerifier)
//                .build();
//
//
//        mIShelvesApi = new Retrofit.Builder()
//                .baseUrl("https://book.expressreader.cn/")
//                .addConverterFactory(ScalarsConverterFactory.create())
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .client(client)
//                .build().create(IShelvesApi.class);

//        OkHttpClient client = new OkHttpClient.Builder()
//                .sslSocketFactory(HttpsUtils.setCertificatesFromFile(BookShelfApplication.mContext, "expressreader.cn.crt"))
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

    public Observable<String> getRemoteShelves(String category) {
//        return BookShelfApplication.mIShelvesApi.getShelves();
//        return mIShelvesApi.getShelves();
        return ApiManager.getInstance().getService().getShelves();
    }

}
