package com.chinafocus.bookshelf.model.base.statistics;

import android.util.Log;

import com.chinafocus.bookshelf.bean.StatisticsBody;
import com.chinafocus.bookshelf.model.base.network.ApiManager;
import com.google.gson.Gson;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * @author
 * @date 2018/12/21
 * description：
 */
class StatisticsTypeNew implements IStatisticsType {


    @Override
    public void postStatisticsNow(String statisticsType, String statisticsId, String origin, String uuid) {

        StatisticsBody statisticsBody=new StatisticsBody(uuid,statisticsId,statisticsType);
        Gson gson=new Gson();
        String body= gson.toJson(statisticsBody);
        RequestBody requestBody =RequestBody.create(MediaType.parse("application/json; charset=utf-8"),body);

        ApiManager.getInstance().getNewService().postStatistics(origin, requestBody)
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
