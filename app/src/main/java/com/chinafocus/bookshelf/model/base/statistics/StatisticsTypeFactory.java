package com.chinafocus.bookshelf.model.base.statistics;

public class StatisticsTypeFactory {

    public static IStatisticsType getStatisticsManager() {

//        return new StatisticsType();
        return new StatisticsTypeNew();
    }
}
