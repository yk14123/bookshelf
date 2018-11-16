package com.chinafocus.bookshelf.model.repository;

import io.reactivex.Observable;

public interface IRemoteNewsSourceData<T> {
    Observable<T> getNews(String category);
}
