package com.chinafocus.bookshelf.model.repository;


import android.os.SystemClock;
import android.util.Log;

import com.chinafocus.bookshelf.model.bean.NewsEntity;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

public class NewsRepository {

    private LocalNewsSource mLocalNewsSource;
    private RemoteNewsSource mRemoteNewsSource;

    private NewsRepository() {
        mLocalNewsSource = new LocalNewsSource();
        mRemoteNewsSource = new RemoteNewsSource();
    }

    public static NewsRepository getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static NewsRepository INSTANCE = new NewsRepository();
    }

    public Observable<NewsEntity> getNetNews(String category) {
        return mRemoteNewsSource.getNews(category)
                .doOnNext(new Consumer<NewsEntity>() {
                    @Override
                    public void accept(NewsEntity newsEntity) throws Exception {
                        Log.i("MyLog", "从网络取数据getNetNews-->saveNews");

                        SystemClock.sleep(3000);
//                        Log.i("MyThread", "NewsRepository getNews Thread name = " + Thread.currentThread().getName());

                        mLocalNewsSource.saveNews(newsEntity);
                    }
                });
    }

    public Observable<NewsEntity> getCacheNews(String category) {
        Log.i("MyThread", "NewsRepository getCacheNews Thread name = " + Thread.currentThread().getName());
        Log.i("MyLog", "取缓存缓存啦！！");
        return mLocalNewsSource.getNews(category);
    }

}
