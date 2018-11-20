package com.chinafocus.bookshelf.presenter.shelves;

import com.chinafocus.bookshelf.model.bean.ShelvesResultBean;
import com.chinafocus.bookshelf.model.repository.shelves.ShelvesRepositoryFactory;
import com.google.gson.Gson;

import java.lang.ref.WeakReference;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;

public abstract class AbstractShelvesPresenter implements IShelvesMvpContract.IPresenter {

    private CompositeDisposable mCompositeDisposable;
    private WeakReference<IShelvesMvpContract.IView> mViewWeakReference;
    protected final Gson mGson;

    protected AbstractShelvesPresenter(IShelvesMvpContract.IView view) {
        mViewWeakReference = new WeakReference<>(view);
        mCompositeDisposable = new CompositeDisposable();
        mGson = new Gson();
    }

    @Override
    public void refresh(String refreshType, String[] args) {

        ShelvesPresenter.RefreshObserver refreshObserver = new ShelvesPresenter.RefreshObserver(refreshType, this);
        mCompositeDisposable.add(refreshObserver);

        Observable<String> cacheShelvesSource = ShelvesRepositoryFactory.getInstance().getCacheShelves(refreshType);
        Observable<String> netShelvesSource = ShelvesRepositoryFactory.getInstance().getNetShelves(refreshType, args);

        threeLevelBuffer(refreshObserver, cacheShelvesSource, netShelvesSource);

    }

    protected abstract void threeLevelBuffer(RefreshObserver refreshObserver, Observable<String> cacheShelvesSource, Observable<String> netShelvesSource);

    @Override
    public void destroy() {
        mCompositeDisposable.clear();
        if (mViewWeakReference != null) {
            mViewWeakReference.clear();
            mViewWeakReference = null;
        }
    }

    static class RefreshObserver extends DisposableObserver<List<ShelvesResultBean>> {

        @IShelvesMvpContract.RefreshType
        String mRefreshType;
        IShelvesMvpContract.IPresenter mShelvesPresenter;

        RefreshObserver(@IShelvesMvpContract.RefreshType String refreshType, IShelvesMvpContract.IPresenter shelvesPresenter) {
            mRefreshType = refreshType;
            this.mShelvesPresenter = shelvesPresenter;
        }

        @Override
        public void onNext(List<ShelvesResultBean> shelvesResultBean) {
            IShelvesMvpContract.IView iView = ((AbstractShelvesPresenter) mShelvesPresenter).mViewWeakReference.get();
            if (iView != null) {
                iView.onRefreshFinished(mRefreshType, shelvesResultBean);
            }
        }

        @Override
        public void onError(Throwable throwable) {

            ((AbstractShelvesPresenter) mShelvesPresenter).mViewWeakReference.get().showTips("网络不好，刷新错误");
        }

        @Override
        public void onComplete() {
        }
    }
}

