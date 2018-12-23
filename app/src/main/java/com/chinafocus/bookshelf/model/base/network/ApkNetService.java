package com.chinafocus.bookshelf.model.base.network;

import com.chinafocus.bookshelf.bean.VersionBean;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApkNetService {

    /**
     * 获取版本更新配置文件
     * @param content
     * @return
     */
    @GET("{content}")
    Observable<VersionBean> getVersion(@Path("content") String content);

    /**
     * 获取最新版本Apk
     * @param ApkName
     * @return
     */
    @GET("{ApkName}")
    Observable<ResponseBody> getAPK(@Path("ApkName") String ApkName);
}
