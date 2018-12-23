package com.chinafocus.bookshelf.model.base.statistics;

import android.util.Log;

import com.chinafocus.bookshelf.model.base.network.ApiManager;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author
 * @date 2018/12/21
 * description：
 */
class StatisticsType implements IStatisticsType {


    @Override
    public void postStatisticsNow(String statisticsType, String statisticsId, String origin, String uuid) {

        ApiManager.getInstance().getService().postStatistics(statisticsType, statisticsId, origin, uuid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        Log.i("StatisticsType", "统计发送成功");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("StatisticsType", "统计发送失败");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
