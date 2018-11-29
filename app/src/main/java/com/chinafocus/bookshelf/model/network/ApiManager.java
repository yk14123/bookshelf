package com.chinafocus.bookshelf.model.network;


public class ApiManager {

    private final ApiService mApiService;
    private final ApkNetService mNewApkService;

    private ApiManager() {
        mApiService = RetrofitFactory.getDownloadService().create(ApiService.class);
        mNewApkService = RetrofitFactory.getNewApkService().create(ApkNetService.class);
    }

    public static ApiManager getInstance() {
        return Holder.INSTANCE;
    }

    static class Holder {
        static ApiManager INSTANCE = new ApiManager();
    }

    //获取ApiService接口调用对象
    public ApiService getService() {
        return mApiService;
    }

    //获取ApkNetService接口调用对象
    public ApkNetService getApkService() {
        return mNewApkService;
    }

}
