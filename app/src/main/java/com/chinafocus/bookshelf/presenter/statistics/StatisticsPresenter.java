package com.chinafocus.bookshelf.presenter.statistics;

import android.content.Context;

import com.chinafocus.bookshelf.global.BookShelfConstant;
import com.chinafocus.bookshelf.model.base.statistics.IStatisticsType;
import com.chinafocus.bookshelf.model.base.statistics.StatisticsTypeFactory;
import com.chinafocus.bookshelf.utils.SpUtil;

public class StatisticsPresenter {

    private String mCpuSerialID;
    private String mOrigin;

    private IStatisticsType mStatisticsType;


    private StatisticsPresenter() {
    }

    public static StatisticsPresenter getInstance() {
        return InnerHolder.holder;
    }

    private static class InnerHolder {
        private static final StatisticsPresenter holder = new StatisticsPresenter();
    }

    /**
     * 竖屏设备可以拿到cpuId，手机设备拿不到！
     */
    private void initCpuId(Context context) {

        mCpuSerialID = SpUtil.getString(context, BookShelfConstant.BOOK_CLIENT_UUID);

//        if (mCpuSerialID.equals("0000000000000000")) {
//            Toast.makeText(context, "cpuId获取失败 -- >" + mCpuSerialID, Toast.LENGTH_SHORT).show();
//        }

        mOrigin = SpUtil.getString(context, BookShelfConstant.BOOK_INIT_LOCATION_ID);

    }

    public void startStatistics(Context context, String statisticsType, String statisticsId) {

        if (mStatisticsType == null) {
            initCpuId(context);
            mStatisticsType = StatisticsTypeFactory.getStatisticsManager();
        }

        mStatisticsType.postStatisticsNow(statisticsType, statisticsId, mOrigin, mCpuSerialID);
    }

}
