package com.chinafocus.bookshelf.presenter.shelves;

public interface INetListener<T> {
    void onNext(T t);

    void onError(Throwable e);

    void onComplete();
}