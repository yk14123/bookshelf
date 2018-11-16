package com.chinafocus.bookshelf.presenter.news;

import android.support.annotation.IntDef;

import com.chinafocus.bookshelf.model.bean.NewsBean;

import java.util.List;

public interface NewsMvpContract {

    int REFRESH_AUTO = 0;
    int REFRESH_CACHE = 1;

    @IntDef({REFRESH_AUTO, REFRESH_CACHE})
    @interface RefreshType {
    }

    interface View {
        void onRefreshFinished(@RefreshType int refreshType, List<NewsBean> newsEntity);

        void showTips(String message);
    }

    interface Presenter {
        void refresh(@RefreshType int refreshType);

        void destroy();
    }

}
