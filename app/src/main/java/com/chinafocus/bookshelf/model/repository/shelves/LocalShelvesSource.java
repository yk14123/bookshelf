package com.chinafocus.bookshelf.model.repository.shelves;

import android.annotation.SuppressLint;
import android.util.Log;

import com.chinafocus.bookshelf.model.lru.LruMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

class LocalShelvesSource {


    public LocalShelvesSource() {
    }

    public Observable<String> get(final String key) {
//        Observable.just(key).flatMap(new Function<String, ObservableSource<String>>() {
//            @Override
//            public ObservableSource<String> apply(String key) throws Exception {
//
//                String rawResult = (String) LruMap.getInstance().get(key);
//                if (rawResult != null) {
//                    Log.i("MyThread", "MyThread  --getNews--  = " + Thread.currentThread().getName());
//                    return Observable.just(rawResult);
//                }
//                return Observable.empty();
//            }
//        });

        return Observable.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {

                String rawResult = (String) LruMap.getInstance().get(key);
                if (rawResult != null) {
                    Log.i("MyThread", "MyThread  --getNews--  = " + Thread.currentThread().getName());
                    emitter.onNext(rawResult);
                }
                emitter.onComplete();

            }
        });
    }

    @SuppressLint("CheckResult")
    public void save(final String key, String rawResult) {
        Observable.just(rawResult).observeOn(Schedulers.io()).subscribe(new Consumer<String>() {

            @Override
            public void accept(String rawResult) throws Exception {
                if (rawResult != null && rawResult.length() > 0) {
                    Log.i("MyThread", "MyThread  --saveNews--  = " + Thread.currentThread().getName());
                    LruMap.getInstance().put(key, rawResult, true);
                }
            }
        });
    }
}
