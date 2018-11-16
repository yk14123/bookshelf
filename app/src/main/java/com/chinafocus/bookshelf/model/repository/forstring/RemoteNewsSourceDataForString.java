package com.chinafocus.bookshelf.model.repository.forstring;


import com.chinafocus.bookshelf.model.network.NewsForStringApi;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RemoteNewsSourceDataForString {

    private NewsForStringApi mNewsResponseBodyApi;

    public RemoteNewsSourceDataForString() {
        mNewsResponseBodyApi = new Retrofit.Builder()
                .baseUrl("http://gank.io")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(NewsForStringApi.class);
    }

    public Observable<String> getNews(String category) {
        return mNewsResponseBodyApi.getNews(category, 10, 1);
    }

}
