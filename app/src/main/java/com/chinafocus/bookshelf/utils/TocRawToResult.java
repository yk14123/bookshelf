package com.chinafocus.bookshelf.utils;

import com.chinafocus.bookshelf.bean.BookMetadataRawBean.BookMetadataResultBean.TocBean;
import com.chinafocus.bookshelf.bean.TocResultBean;

import java.util.List;

/**
 * @author
 * @date 2018/12/25
 * description：
 */
public class TocRawToResult {

    /**
     *  result = new ArrayList<TocResultBean>();
     *  cleanData(oneLevel, result, 1);
     *
     * @param oneLevel
     * @param result
     * @param defaultLevel
     */

    public static void cleanData(List<TocBean> oneLevel, List<TocResultBean> result, int defaultLevel) {

        for (TocBean temp : oneLevel) {

            if (temp.getVisible() == 0) {
                continue;
            }
            cleanDataRawToResult(temp, result, defaultLevel);

        }

    }

    private static void cleanDataRawToResult(TocBean rawData, List<TocResultBean> result, int defaultLevel) {
        // TODO Auto-generated method stub

        TocResultBean resultData = new TocResultBean();
        resultData.title = rawData.getTitle();
        resultData.full = rawData.getFull();
        resultData.level = defaultLevel;

        result.add(resultData);

        if (rawData.getChildren() == null || rawData.getChildren().size() == 0)
            return;

        defaultLevel++;
        for (TocBean temp : rawData.getChildren()) {
            cleanDataRawToResult(temp, result, defaultLevel);
        }

    }
}
