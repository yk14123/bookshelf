package com.chinafocus.bookshelf.utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.chinafocus.bookshelf.global.BookShelfConstant;
import com.chinafocus.bookshelf.ui.activity.BookCategoryDetailActivity;
import com.chinafocus.bookshelf.ui.activity.BookContentDetailActivity;
import com.chinafocus.bookshelf.ui.activity.BookMetaDataActivity;
import com.chinafocus.bookshelf.ui.activity.BookMetaDataActivityYangRV;
import com.chinafocus.bookshelf.ui.activity.ShelfDetailActivity;

/**
 * 将Activity界面跳转的逻辑抽离
 *
 * @author HUANG_JIN
 * @version 1.0
 * create at 2018/12/4 11:38
 */
public class UIHelper {

    /**
     * 跳转九宫格页面 see {@link ShelfDetailActivity}
     *
     * @param shelfId 书柜标识
     */
    public static void startShelfDetailActivity(Context context, int shelfId) {
        Intent intent = new Intent(context, ShelfDetailActivity.class);
        intent.putExtra(BookShelfConstant.SHELF_ID, shelfId);
        context.startActivity(intent);
    }

    /**
     * 跳转图书分类内容详情页面 see {@link BookCategoryDetailActivity}
     *
     * @param shelfId      书柜标识
     * @param categoryId   图书分类标志
     * @param categoryName 图书分类名称
     */
    public static void startBookCategoryDetailActivity(Context context, int shelfId, int categoryId, String categoryName) {
        Intent intent = new Intent(context, BookCategoryDetailActivity.class);
        intent.putExtra(BookShelfConstant.SHELF_ID, shelfId);
        intent.putExtra(BookShelfConstant.CATEGORY_ID, categoryId);
        intent.putExtra(BookShelfConstant.CATEGORY_NAME, categoryName);
        context.startActivity(intent);
    }

    /**
     * 跳转图书三级目录页面 see {@link BookMetaDataActivity}
     *
     * @param shelfId         书柜标识
     * @param categoryId      图书分类标志
     * @param bookId          图书唯一id
     * @param categoryTagName 分类标签名
     */
    public static void startBookMetaActivity(Context context, int shelfId,
                                             int categoryId, int bookId, String bookName, String categoryTagName) {
        //跳轉圖書詳情頁
//        Intent intent = new Intent(context, BookMetaDataActivity.class);
        Intent intent = new Intent(context, BookMetaDataActivityYangRV.class);
        intent.putExtra(BookShelfConstant.SHELF_ID, shelfId);
        intent.putExtra(BookShelfConstant.CATEGORY_ID, categoryId);
        intent.putExtra(BookShelfConstant.BOOK_ID, bookId);
        intent.putExtra(BookShelfConstant.BOOK_NAME, bookName);
        intent.putExtra(BookShelfConstant.CATEGORY_NAME, categoryTagName);
        context.startActivity(intent);
    }

    /**
     * 跳转内容详情页面 see {@link BookContentDetailActivity}
     *
     * @param shelfId    书柜标识
     * @param categoryId 图书分类标志
     * @param bookId     图书唯一id
     * @param bookName   图书名称
     * @param page       当前页面标签
     */
    public static void startContentDetailActivity(Context context, int shelfId,
                                                  int categoryId, int bookId,
                                                  String bookName, @NonNull String page) {
        Intent intent = new Intent(context, BookContentDetailActivity.class);
        intent.putExtra(BookShelfConstant.SHELF_ID, shelfId);
        intent.putExtra(BookShelfConstant.CATEGORY_ID, categoryId);
        intent.putExtra(BookShelfConstant.BOOK_ID, bookId);
        intent.putExtra(BookShelfConstant.BOOK_NAME, bookName);
        intent.putExtra(BookShelfConstant.PAGE, page);
        context.startActivity(intent);
    }

}
