package com.chinafocus.bookshelf.presenter.shelves;

import com.chinafocus.bookshelf.model.bean.ShelvesRawBean;
import com.chinafocus.bookshelf.model.repository.shelves.ShelvesRepositoryFactory;
import com.google.gson.Gson;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public abstract class AbstractShelvesPresenter implements IShelvesMvpContract.IPresenter {

    //    private static final long AUTO_REFRESH_TIME = 1000 * 60 * 10;
    protected CompositeDisposable mCompositeDisposable;
    protected IShelvesMvpContract.IView mView;
    protected List<ShelvesRawBean> mShelvesRawBeans;
//    private long mLastNetUpdateTime;

    private WeakReference<IShelvesMvpContract.IView> mViewWeakReference;
    private final Gson mGson;

    public AbstractShelvesPresenter(IShelvesMvpContract.IView view) {
        mViewWeakReference = new WeakReference<>(view);
//        mView = view;
        mCompositeDisposable = new CompositeDisposable();
        mShelvesRawBeans = new ArrayList<>();
        mGson = new Gson();
    }

    @Override
    public void refresh(int refreshType) {
        ShelvesPresenter.RefreshObserver refreshObserver = new ShelvesPresenter.RefreshObserver(refreshType, this);
        mCompositeDisposable.add(refreshObserver);

//            Observable<String> cacheShelfId = ShelvesDetailRepository.getInstance().getCacheShelvesDetail("2");
//            Observable<String> netShelfId = ShelvesDetailRepository.getInstance().getNetShelvesDetail("2");

        Observable<String> cacheShelfId = ShelvesRepositoryFactory.getInstance().getCacheShelves("bbb");
        Observable<String> netShelfId = ShelvesRepositoryFactory.getInstance().getNetShelves("bbb", new String[]{"2"});

//            Observable<String> cacheShelfId = ShelvesRepository.getInstance().getCacheShelves("shelfId");
//            Observable<String> netShelfId = ShelvesRepository.getInstance().getNetShelves("shelfId");

        Observable.concat(cacheShelfId, netShelfId)
                .map(new Function<String, ShelvesRawBean>() {
                    @Override
                    public ShelvesRawBean apply(String s) throws Exception {
                        System.out.println("********************  before **********************************");
                        System.out.println("shelves : " + s);
                        System.out.println("********************  after  *********************************");
//                            return mGson.fromJson(s, ShelvesRawBean.class);
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(refreshObserver);

        startData();
    }


    @Override
    public void destroy() {
        mCompositeDisposable.clear();
        if (mViewWeakReference != null) {
            mViewWeakReference.clear();
            mViewWeakReference = null;
        }
    }

    protected abstract void startData();

    static class RefreshObserver extends DisposableObserver<ShelvesRawBean> {

        private @IShelvesMvpContract.RefreshType
        int mRefreshType;
        IShelvesMvpContract.IPresenter mShelvesPresenter;

        RefreshObserver(@IShelvesMvpContract.RefreshType int refreshType, IShelvesMvpContract.IPresenter shelvesPresenter) {
            mRefreshType = refreshType;
            this.mShelvesPresenter = shelvesPresenter;
        }

        @Override
        public void onNext(ShelvesRawBean shelvesRawBean) {
//            updateNewsBeans(mRefreshType, newsEntity);
//            mViewWeakReference.get().onRefreshFinished(mRefreshType, mShelvesRawBeans);
            System.out.println("********************  RefreshObserver before **********************************");
            System.out.println("RefreshObserver : " + shelvesRawBean.getData().get(0).getName());
            System.out.println("********************  RefreshObserver after  *********************************");
        }

        @Override
        public void onError(Throwable throwable) {

            ((AbstractShelvesPresenter) mShelvesPresenter).mViewWeakReference.get().showTips("刷新错误");
        }

        @Override
        public void onComplete() {
        }
    }
}

