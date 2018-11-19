package com.chinafocus.bookshelf.model.repository.shelves;


import com.chinafocus.bookshelf.model.network.ApiManager;

import io.reactivex.Observable;

class RemoteShelvesSourceData {


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

    public Observable<String> get(String category, String[] parms) {
//        return BookShelfApplication.mIShelvesApi.get();
//        return mIShelvesApi.get();

        if (category.equals("aaa")) {
            return ApiManager.getInstance().getService().getShelves();
        }

        if (category.equals("bbb")) {
            return ApiManager.getInstance().getService().getShelvesDetail(parms[0]);
        }

        if (category.equals("ccc")) {
            return ApiManager.getInstance().getService().getBookCategoryDetail(parms[0], parms[1]);
        }

        if (category.equals("ddd")) {
            return ApiManager.getInstance().getService().getBookMetadata(parms[0], parms[1], parms[2]);
        }

        if (category.equals("eee")) {
            return ApiManager.getInstance().getService().getBookContentDetail(parms[0], parms[1], parms[2], parms[3]);
        }

//        return ApiManager.getInstance().getService().getShelves();
        return null;
    }

}
