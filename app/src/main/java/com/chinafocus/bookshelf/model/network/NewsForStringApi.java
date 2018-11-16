package com.chinafocus.bookshelf.model.network;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NewsForStringApi {

    @GET("api/data/{category}/{count}/{page}")
    Observable<String> getNews(@Path("category") String category, @Path("count") int count, @Path("page") int page);
}
