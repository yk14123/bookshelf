package com.chinafocus.bookshelf.model.base.network;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author
 * @date 2019/1/8
 * description：
 */
public interface ApiNewService {


    /**
     * @param customer 为原origin，为当前客户唯一标识，需要在登陆页面输入获取
     * @param requestBody   为统计相关的json请求格式
     * @return
     */
    @PUT("statistics/customer/{customer}")
    Observable<String> postStatistics(@Path("customer") String customer, @Body RequestBody requestBody);

    /**
     * 获取相关书柜
     *
     * @param origin 客户id
     * @return BookShelvesRawBean
     */
    @GET("read/shelves")
    Observable<String> getShelves(@Query("cn") String origin);

    /**
     * 获取具体书柜九宫格展示信息
     *
     * @param id     书柜id
     * @param origin 客户id
     * @return 书柜展示内容
     */
    @GET("read/shelves/{shelfId}/categories")
    Observable<String> getShelvesDetail(@Path("shelfId") String id, @Query("cn") String origin);


    /**
     * 获取分类书种类的信息
     *
     * @param id         书柜id
     * @param categoryId 书的类别id
     * @param origin     客户id
     * @return 图书种类细节
     */
    @GET("read/shelves/{shelfId}/categories/{categoryId}/epubs")
    Observable<String> getBookCategoryDetail(@Path("shelfId") String id, @Path("categoryId") String categoryId, @Query("cn") String origin);


    /**
     * 书的大纲目录展示
     *
     * @param id         书柜id
     * @param categoryId 书的类别id
     * @param mappingId  具体书的id
     * @param origin     客户id
     * @return 书的大纲目录展示
     */
    @GET("read/shelves/{shelfId}/categories/{categoryId}/epubs/{mappingId}/metadata")
    Observable<String> getBookMetadata(@Path("shelfId") String id, @Path("categoryId") String categoryId, @Path("mappingId") String mappingId, @Query("cn") String origin);


    /**
     * 书的每一页具体内容--加密版本
     * https://gateway.wechinafocus.com/read/shelves/3/categories/26/books/285/page/encrypt/Chapter2.xhtml#ebookNote_3
     *
     * @param id         书柜id
     * @param categoryId 书的类别id
     * @param mappingId  具体书的id
     * @param page       书的内容页
     * @param origin     客户id
     * @return 书的每一页具体内容
     */
    @GET("read/shelves/{shelfId}/categories/{categoryId}/epubs/{mappingId}/page/encrypt/{page}")
    Observable<String> getBookContentAESDetail(@Path("shelfId") String id, @Path("categoryId") String categoryId, @Path("mappingId") String mappingId, @Path("page") String page, @Query("cn") String origin);
}
