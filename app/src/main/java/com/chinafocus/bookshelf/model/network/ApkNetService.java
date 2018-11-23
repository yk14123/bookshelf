package com.chinafocus.bookshelf.model.network;

import com.chinafocus.bookshelf.model.bean.VersionBean;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApkNetService {

    @GET("{content}")
    Observable<VersionBean> getVersion(@Path("content") String content);

    @GET("{ApkName}")
    Observable<ResponseBody> getAPK(@Path("ApkName") String ApkName);
}
