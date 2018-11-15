package com.chinafocus.bookshelf.model.network;

import com.chinafocus.bookshelf.model.bean.NewsEntity;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NewsApi {

    @GET("api/data/{category}/{count}/{page}")
    Observable<NewsEntity> getNews(@Path("category") String category, @Path("count") int count, @Path("page") int page);
}
