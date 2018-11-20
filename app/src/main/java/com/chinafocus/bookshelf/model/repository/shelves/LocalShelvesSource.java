package com.chinafocus.bookshelf.model.repository.shelves;

import android.annotation.SuppressLint;

import com.chinafocus.bookshelf.model.lru.LruMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

class LocalShelvesSource {


    LocalShelvesSource() {
    }

    Observable<String> get(final String key) {

        return Observable.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {

                String rawResult = (String) LruMap.getInstance().get(key);
                if (rawResult != null) {
                    emitter.onNext(rawResult);
                }
                emitter.onComplete();

            }
        });
    }

    @SuppressLint("CheckResult")
    void save(final String key, String rawResult) {
        Observable.just(rawResult).observeOn(Schedulers.io()).subscribe(new Consumer<String>() {

            @Override
            public void accept(String rawResult) throws Exception {
                if (rawResult != null && rawResult.length() > 0) {
                    LruMap.getInstance().put(key, rawResult, true);
                }
            }
        });
    }
}
