package com.chinafocus.bookshelf.presenter.shelves;


import com.chinafocus.bookshelf.model.bean.ShelvesRawBean;
import com.chinafocus.bookshelf.model.bean.ShelvesResultBean;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class ShelvesPresenter extends AbstractShelvesPresenter {

    public ShelvesPresenter(IShelvesMvpContract.IView view) {
        super(view);
    }

    @Override
    protected void threeLevelBuffer(RefreshObserver refreshObserver, Observable<String> cacheShelvesSource, Observable<String> netShelvesSource) {
        Observable.concat(cacheShelvesSource, netShelvesSource)
                .map(new Function<String, List<ShelvesResultBean>>() {
                    @Override
                    public List<ShelvesResultBean> apply(String s) throws Exception {
                        System.out.println("********************  before **********************************");
                        System.out.println("shelves : " + s);
                        System.out.println("********************  after  *********************************");
                        ShelvesRawBean shelvesRawBean = mGson.fromJson(s, ShelvesRawBean.class);

                        return shelvesRawBean.getData();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(refreshObserver);
    }


}
