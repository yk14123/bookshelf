package com.chinafocus.bookshelf.model.base.sdkconfig;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.chinafocus.bookshelf.global.BookShelfConstant;
import com.chinafocus.bookshelf.ui.activity.ShelfDetailActivity;

/**
 * @author
 * @date 2019/1/22
 * description：
 */
public class BookShelfSdkConfig {

    /**
     * @param context     上下文
     * @param origin      客户代码
     * @param client_uuid 用户唯一代码
     */
    public static void initialize(Context context, String origin, String client_uuid) {

        if (TextUtils.isEmpty(origin) || TextUtils.isEmpty(client_uuid)) {
            throw new IllegalArgumentException("客户代码 或者 用户唯一码 不能为空！");
        }

        Intent intent = new Intent(context, ShelfDetailActivity.class);
        intent.putExtra(BookShelfConstant.BOOK_INIT_LOCATION_ID, origin);
        intent.putExtra(BookShelfConstant.BOOK_CLIENT_UUID, client_uuid);
        context.startActivity(intent);

    }
}
