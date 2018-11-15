package com.chinafocus.bookshelf.model.network;


import com.chinafocus.bookshelf.model.bean.Person;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author create by HUANG JIN on 18/11/6
 * @version 1.0
 * @description 项目API service相关接口定义管理类
 */
public interface ApiService {

    /**
     * 取得热点新闻资讯
     */
    @POST("/api/HotSpot/hotspot")
    Observable<List<Person>> getHotSpot(@Query("LCID") String LCID, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);

    /**
     * 获取指定国家码所有的2018年后的杂志
     */
    @POST("/api/Magazine/GetMagazineList")
    Observable<List<Person>> getMagazineList(@Query("LCID") String LCID);

    /**
     * 获取单本杂志内容
     */
    @POST("/api/Magazine/GetIssueLite")
    Observable<Person> getIssueLite(@Query("IssueId") String IssueId);


    /**
     * 获取杂志章节内容
     */
    @POST("/api/Magazine/GetIssueUnit")
    Observable<Person> getIssueUnit(@Query("MnID") String MnID);


}
