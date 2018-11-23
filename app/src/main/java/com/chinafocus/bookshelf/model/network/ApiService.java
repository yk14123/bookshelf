package com.chinafocus.bookshelf.model.network;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface ApiService {


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
}
