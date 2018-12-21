package com.chinafocus.bookshelf.presenter.statistics;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.chinafocus.bookshelf.global.BookShelfConstant;
import com.chinafocus.bookshelf.model.network.ApiManager;
import com.chinafocus.bookshelf.utils.SpUtil;
import com.chinafocus.bookshelf.utils.UUIDUtil;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author
 * @date 2018/12/21
 * description：
 */
public class StatisticsPresenter {

    private static String sCpuSerialID;

    /**
     * 竖屏设备可以拿到cpuId，手机设备拿不到！
     */
    private static void initCpuId(Context context) {

        sCpuSerialID = UUIDUtil.getCPUSerialID();

        if (sCpuSerialID.equals("0000000000000000")) {
            Toast.makeText(context, "cpuId获取失败 -- >" + sCpuSerialID, Toast.LENGTH_SHORT).show();
        }

//        SpUtil.setString(context, BookShelfConstant.BOOK_CLIENT_UUID, sCpuSerialID);

    }

    public static void postStatisticsNow(Context context, String statisticsType, String statisticsId) {

        String origin = SpUtil.getString(context, BookShelfConstant.BOOK_INIT_LOCATION_ID);

        if (sCpuSerialID == null) {
            initCpuId(context);
        }

        ApiManager.getInstance().getService().postStatistics(statisticsType, statisticsId, origin, sCpuSerialID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        Log.i("StatisticsPresenter", "统计发送成功");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("StatisticsPresenter", "统计发送失败");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
