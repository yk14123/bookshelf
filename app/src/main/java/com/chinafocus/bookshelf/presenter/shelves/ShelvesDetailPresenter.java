package com.chinafocus.bookshelf.presenter.shelves;


import io.reactivex.Observable;

public class ShelvesDetailPresenter extends AbstractShelvesPresenter {


    protected ShelvesDetailPresenter(IShelvesMvpContract.IView view) {
        super(view);
    }

    @Override
    protected void threeLevelBuffer(RefreshObserver refreshObserver, Observable<String> cacheShelvesSource, Observable<String> netShelvesSource) {

    }
}
