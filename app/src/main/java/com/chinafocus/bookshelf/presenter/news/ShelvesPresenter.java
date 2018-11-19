package com.chinafocus.bookshelf.presenter.news;


import com.chinafocus.bookshelf.model.bean.ShelvesRawBean;
import com.chinafocus.bookshelf.model.repository.shelves.ShelvesRepository;
import com.chinafocus.bookshelf.model.repository.shelvesdetail.ShelvesDetailRepository;
import com.google.gson.Gson;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.ResourceObserver;
import io.reactivex.schedulers.Schedulers;

public class ShelvesPresenter implements ShelvesMvpContract.Presenter {

    private static final long AUTO_REFRESH_TIME = 1000 * 60 * 10;
    private CompositeDisposable mCompositeDisposable;
    private ShelvesMvpContract.View mView;
    private List<ShelvesRawBean> mShelvesRawBeans;
    private long mLastNetUpdateTime;

    private WeakReference<ShelvesMvpContract.View> mViewWeakReference;
    private final Gson mGson;

    public ShelvesPresenter(ShelvesMvpContract.View view) {
        mViewWeakReference = new WeakReference<>(view);
//        mView = view;
        mCompositeDisposable = new CompositeDisposable();
        mShelvesRawBeans = new ArrayList<>();
        mGson = new Gson();
    }

    @Override
    public void refresh(@ShelvesMvpContract.RefreshType final int refreshType) {

        if (refreshType == ShelvesMvpContract.REFRESH_CACHE) {

            ShelvesRepository.getInstance()
                    .getCacheShelves("shelfId")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ResourceObserver<String>() {
                        @Override
                        public void onNext(String s) {
                            System.out.println("cache : " + s);
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });


        } else if (refreshType == ShelvesMvpContract.REFRESH_AUTO) {
//            if (System.currentTimeMillis() - mLastNetUpdateTime > AUTO_REFRESH_TIME) { //自动刷新的间隔时间为十分钟。
//            NewsRepository.getInstance()
//                    .getNetNews("Android")
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new RefreshObserver(refreshType));


//            Observable<NewsEntity> cacheNews = NewsRepository.getInstance().getCacheNews("Android");
//            Observable<NewsEntity> netNews = NewsRepository.getInstance().getNetNews("Android");


//            RefreshObserver refreshObserver = new RefreshObserver(refreshType);
//            mCompositeDisposable.add(refreshObserver);

//*******************************************************************************************
//            RefreshObserver refreshObserver = new RefreshObserver(refreshType);
//            mCompositeDisposable.add(refreshObserver);
//
//            Observable<String> cacheShelfId = ShelvesRepository.getInstance().getCacheShelves("shelfId");
//            Observable<String> netShelfId = ShelvesRepository.getInstance().getNetShelves("shelfId");
//
//            Observable.concat(cacheShelfId, netShelfId)
//                    .map(new Function<String, ShelvesRawBean>() {
//                        @Override
//                        public ShelvesRawBean apply(String s) throws Exception {
//                            System.out.println("********************  before **********************************");
//                            System.out.println("shelves : " + s);
//                            System.out.println("********************  after  *********************************");
//                            return mGson.fromJson(s, ShelvesRawBean.class);
//                        }
//                    })
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(refreshObserver);
//*******************************************************************************************
            RefreshObserver refreshObserver = new RefreshObserver(refreshType);
            mCompositeDisposable.add(refreshObserver);

            Observable<String> cacheShelfId = ShelvesDetailRepository.getInstance().getCacheShelvesDetail("2");
            Observable<String> netShelfId = ShelvesDetailRepository.getInstance().getNetShelvesDetail("2");

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

        }
    }

    @Override
    public void destroy() {
        mCompositeDisposable.clear();
        if (mViewWeakReference != null) {
            mViewWeakReference.clear();
            mViewWeakReference = null;
        }
    }

//    private void updateNewsBeans(@ShelvesMvpContract.RefreshType int refreshType, NewsEntity newsEntity) {
//        List<NewsBean> filter = new ArrayList<>();
//        for (NewsResultEntity resultEntity : newsEntity.getResults()) { //对资讯进行去重，需要重写NewsBean的对应方法。
//            NewsBean newsBean = entityToBean(resultEntity);
//            if (!mShelvesRawBeans.contains(newsBean)) {
//                filter.add(newsBean);
//            }
//        }
//        if (refreshType == ShelvesMvpContract.REFRESH_CACHE && mShelvesRawBeans.size() == 0) { //只有当前没有数据时，才使用缓存。
//            mShelvesRawBeans = filter;
//        } else if (refreshType == ShelvesMvpContract.REFRESH_AUTO) { //自动刷新的数据放在头部。
//            mShelvesRawBeans.addAll(0, filter);
//            mLastNetUpdateTime = System.currentTimeMillis();
//        }
//    }

//    private NewsBean entityToBean(NewsResultEntity resultEntity) {
//        String title = resultEntity.getDesc();
//        NewsBean bean = new NewsBean();
//        bean.setTitle(title);
//        return bean;
//    }

    private class RefreshObserver extends DisposableObserver<ShelvesRawBean> {

        private @ShelvesMvpContract.RefreshType
        int mRefreshType;

        RefreshObserver(@ShelvesMvpContract.RefreshType int refreshType) {
            mRefreshType = refreshType;
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
            mViewWeakReference.get().showTips("刷新错误");
        }

        @Override
        public void onComplete() {
        }
    }
}
