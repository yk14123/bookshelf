package com.chinafocus.bookshelf.presenter.shelves;

import android.support.annotation.StringDef;

import java.util.List;

public interface IShelvesMvpContract {

    String REFRESH_SHELVES = "getShelves";
    String REFRESH_SHELVES_DETAIL = "getShelvesDetail";
    String REFRESH_BOOK_CATEGORY_DETAIL = "getBookCategoryDetail";
    String REFRESH_BOOK_METADATA = "getBookMetadata";
    String REFRESH_BOOK_CONTENT_DETAIL = "getBookContentDetail";

    @StringDef({REFRESH_SHELVES, REFRESH_SHELVES_DETAIL,
            REFRESH_BOOK_CATEGORY_DETAIL, REFRESH_BOOK_METADATA,
            REFRESH_BOOK_CONTENT_DETAIL})
    @interface RefreshType {
    }

    interface IView<T> {
        void onRefreshFinished(@RefreshType String refreshType, List<T> resultBean);

        void showTips(String message);
    }

    interface IPresenter {
        void refresh(@RefreshType String refreshType, String[] args);

        void destroy();
    }

}
