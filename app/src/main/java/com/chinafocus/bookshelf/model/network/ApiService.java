package com.chinafocus.bookshelf.model.network;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {


    /**
     * @param statisticsType 为具体被点击的统计页面
     * @param statisticsId   为具体被点击的条目
     * @param origin         为当前客户唯一标识，需要在登陆页面输入获取
     * @param client         为当前设备唯一id，通过代码获取
     * @return
     */
    @POST("api/statistics/type/{statisticsType}/statisticsId/{statisticsId}/origin/{origin}/client/{client}")
    Observable<String> postStatistics(@Path("statisticsType") String statisticsType, @Path("statisticsId") String statisticsId, @Path("origin") String origin, @Path("client") String client);

    /**
     * 获取相关书柜
     *
     * @return 获取相关书柜
     */
    @GET("api/shelves")
    @Headers("corigin:expressreader")
    Observable<String> getShelves();


    /**
     * 获取具体书柜展示信息
     *
     * @param id 书柜id
     * @return 书柜展示内容
     */
    @GET("api/shelves/{shelfId}/categories")
    @Headers("corigin:expressreader")
    Observable<String> getShelvesDetail(@Path("shelfId") String id);

    /**
     * 获取分类书种类的信息
     *
     * @param id         书柜id
     * @param categoryId 书的类别id
     * @return 图书种类细节
     */
    @GET("api/shelves/{shelfId}/categories/{categoryId}/books")
    @Headers("corigin:expressreader")
    Observable<String> getBookCategoryDetail(@Path("shelfId") String id, @Path("categoryId") String categoryId);



    /**
     * 书的大纲目录展示
     *
     * @param id         书柜id
     * @param categoryId 书的类别id
     * @param bookId     具体书的id
     * @return 书的大纲目录展示
     */
    @GET("api/shelves/{shelfId}/categories/{categoryId}/books/{bookId}/metadata")
    @Headers("corigin:expressreader")
    Observable<String> getBookMetadata(@Path("shelfId") String id, @Path("categoryId") String categoryId, @Path("bookId") String bookId);

    /**
     * 书的每一页具体内容
     *
     * @param id         书柜id
     * @param categoryId 书的类别id
     * @param bookId     具体书的id
     * @param page       书的内容页
     * @return 书的每一页具体内容
     */
    @GET("api/shelves/{shelfId}/categories/{categoryId}/books/{bookId}/page/{page}")
    @Headers("corigin:expressreader")
    Observable<String> getBookContentDetail(@Path("shelfId") String id, @Path("categoryId") String categoryId, @Path("bookId") String bookId, @Path("page") String page);

    /**
     * 书的每一页具体内容--加密版本
     *  https://book.expressreader.cn/api/shelves/3/categories/26/books/285/page/aes/Chapter2.xhtml#ebookNote_3
     * @param id         书柜id
     * @param categoryId 书的类别id
     * @param bookId     具体书的id
     * @param page       书的内容页
     * @return 书的每一页具体内容
     */
    @GET("api/shelves/{shelfId}/categories/{categoryId}/books/{bookId}/page/aes/{page}")
    @Headers("corigin:expressreader")
    Observable<String> getBookContentAESDetail(@Path("shelfId") String id, @Path("categoryId") String categoryId, @Path("bookId") String bookId, @Path("page") String page);
}
