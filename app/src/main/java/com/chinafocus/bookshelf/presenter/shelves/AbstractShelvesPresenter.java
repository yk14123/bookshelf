package com.chinafocus.bookshelf.presenter.shelves;

import com.chinafocus.bookshelf.model.base.shelvesrepository.ShelvesRepositoryFactory;
import com.chinafocus.bookshelf.utils.LogUtil;
import com.google.gson.Gson;

import java.lang.ref.WeakReference;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

abstract class AbstractShelvesPresenter<TT> implements IShelvesMvpContract.IPresenter {

    private CompositeDisposable mCompositeDisposable;
    private WeakReference<IShelvesMvpContract.IView> mViewWeakReference;
    protected final Gson mGson;


    AbstractShelvesPresenter(IShelvesMvpContract.IView view) {
        mViewWeakReference = new WeakReference<>(view);
        mCompositeDisposable = new CompositeDisposable();
        mGson = new Gson();
    }

    @Override
    public void refresh(final String refreshType, String[] args) {

        DisposableObserver<TT> disposableObserver = new DisposableObserver<TT>() {
            @Override
            public void onNext(TT t) {
                IShelvesMvpContract.IView iView = mViewWeakReference.get();
                if (iView != null)
                    iView.onRefreshFinished(refreshType, (List) t);
            }

            @Override
            public void onError(Throwable e) {
                IShelvesMvpContract.IView iView = mViewWeakReference.get();
                if (iView != null)
//                    iView.showTips("网络不好，刷新错误。错误类型是 --> " + e.getMessage());
                    iView.showTips("网络不好，请稍后重试");
            }

            @Override
            public void onComplete() {
            }
        };

        mCompositeDisposable.add(disposableObserver);

        Observable<String> cacheShelvesSource = ShelvesRepositoryFactory.getInstance().getCacheShelves(refreshType, args);
        Observable<String> netShelvesSource = ShelvesRepositoryFactory.getInstance().getNetShelves(refreshType, args);

        Observable.concat(cacheShelvesSource, netShelvesSource)
                .firstElement()
                .toObservable()
//                .distinct()
                .map(new Function<String, TT>() {
                    @Override
                    public TT apply(String s) throws Exception {
                        LogUtil.veryLongForI("网络不好，请稍后重试",s);
                        return rawToResultFromGson(s);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(disposableObserver);

    }

    protected abstract TT rawToResultFromGson(String s);

    @Override
    public void destroy() {
        mCompositeDisposable.clear();
        if (mViewWeakReference != null) {
            mViewWeakReference.clear();
            mViewWeakReference = null;
        }
    }
}

