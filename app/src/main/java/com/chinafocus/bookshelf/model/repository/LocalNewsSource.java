package com.chinafocus.bookshelf.model.repository;

import android.annotation.SuppressLint;

import com.chinafocus.bookshelf.model.bean.NewsEntity;
import com.chinafocus.bookshelf.model.lru.LruMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LocalNewsSource {


    public LocalNewsSource() {
    }

    public Observable<NewsEntity> getNews(final String category) {


        return Observable.create(new ObservableOnSubscribe<NewsEntity>() {

            @Override
            public void subscribe(ObservableEmitter<NewsEntity> emitter) throws Exception {

                NewsEntity newsEntity = (NewsEntity) LruMap.getInstance().get("newsEntity");

                if (newsEntity != null) {
                    emitter.onNext(newsEntity);
                }

                emitter.onComplete();

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
