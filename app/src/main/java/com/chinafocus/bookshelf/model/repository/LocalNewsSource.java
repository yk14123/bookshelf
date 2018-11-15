package com.chinafocus.bookshelf.model.repository;

import android.annotation.SuppressLint;
import android.util.Log;

import com.chinafocus.bookshelf.model.bean.NewsEntity;
import com.chinafocus.bookshelf.model.lru.LruMap;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class LocalNewsSource {


    public LocalNewsSource() {
    }

    public Observable<NewsEntity> getNews(String category) {
        return Observable.just(category).flatMap(new Function<String, ObservableSource<NewsEntity>>() {
            @Override
            public ObservableSource<NewsEntity> apply(String category) throws Exception {
                NewsEntity newsEntity = (NewsEntity) LruMap.getInstance().get("newsEntity");

                Log.i("MyThread", "LocalNewsSource getNews Thread name = " + Thread.currentThread().getName());
                return Observable.just(newsEntity);
            }
        });
    }


    @SuppressLint("CheckResult")
    public void saveNews(NewsEntity newsEntity) {
        Observable.just(newsEntity).observeOn(Schedulers.io()).subscribe(new Consumer<NewsEntity>() {

            @Override
            public void accept(NewsEntity newsEntity) throws Exception {
                if (newsEntity.getResults() != null && newsEntity.getResults().size() > 0) {
//                    Log.i("MyThread", "LocalNewsSource saveNews Thread name = " + Thread.currentThread().getName());
                    LruMap.getInstance().put("newsEntity", newsEntity, true);
                }
            }
        });
    }
}
