package com.chinafocus.bookshelf.model.base.statistics;

public interface IStatisticsType {
    void postStatisticsNow(String statisticsType, String statisticsId, String origin, String uuid);
}
