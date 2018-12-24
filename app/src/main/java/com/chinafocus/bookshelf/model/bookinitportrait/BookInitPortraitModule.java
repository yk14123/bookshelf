package com.chinafocus.bookshelf.model.bookinitportrait;

import android.content.Intent;
import android.text.TextUtils;

import com.chinafocus.bookshelf.global.BookShelfConstant;
import com.chinafocus.bookshelf.ui.activity.BookInitPortraitActivity;
import com.chinafocus.bookshelf.ui.activity.ShelfDetailActivity;
import com.chinafocus.bookshelf.utils.SpUtil;

public class BookInitPortraitModule {

    public void initData(BookInitPortraitActivity activity) {
        String LocationId = SpUtil.getString(activity, BookShelfConstant.BOOK_INIT_LOCATION_ID);

        if (!TextUtils.isEmpty(LocationId)) {
            activity.startActivity(new Intent(activity, ShelfDetailActivity.class));
            activity.finish();
            return;
        }

        String preLocationId = SpUtil.getString(activity, BookShelfConstant.BOOK_INIT_LOCATION_ID_TEMP);

        activity.initData(preLocationId);
    }

}