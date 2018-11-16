package com.chinafocus.bookshelf.model.repository;


import com.chinafocus.bookshelf.model.bean.NewsEntity;
import com.chinafocus.bookshelf.model.network.NewsApi;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class RemoteNewsSource implements IRemoteNewsSourceData<NewsEntity> {

    private NewsApi mNewsApi;

    public RemoteNewsSource() {
        mNewsApi = new Retrofit.Builder()
                .baseUrl("http://gank.io")
//                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(NewsApi.class);
    }

    public Observable<NewsEntity> getNews(String category) {
        return mNewsApi.getNews(category, 10, 1);
    }

}
