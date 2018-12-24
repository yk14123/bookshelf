package com.chinafocus.bookshelf.model.base.statistics;

public interface IStatisticsType {
    /**
     * 统计页面点击次数
     * @param statisticsType 具体点击页面类型
     * @param statisticsId 具体点击条目item类型
     * @param origin 用户标识身份id
     * @param uuid 用户唯一id
     */
    void postStatisticsNow(String statisticsType, String statisticsId, String origin, String uuid);
}
