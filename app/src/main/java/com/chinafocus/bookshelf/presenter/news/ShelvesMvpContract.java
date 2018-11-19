package com.chinafocus.bookshelf.presenter.news;

import android.support.annotation.IntDef;

import com.chinafocus.bookshelf.model.bean.ShelvesRawBean;

import java.util.List;

public interface ShelvesMvpContract {

    int REFRESH_AUTO = 0;
    int REFRESH_CACHE = 1;

    @IntDef({REFRESH_AUTO, REFRESH_CACHE})
    @interface RefreshType {
    }

    interface View {
        void onRefreshFinished(@RefreshType int refreshType, List<ShelvesRawBean> newsEntity);

        void showTips(String message);
    }

    interface Presenter {
        void refresh(@RefreshType int refreshType);

        void destroy();
    }

}
