package com.chinafocus.bookshelf.presenter.shelves;

import android.support.annotation.IntDef;

import com.chinafocus.bookshelf.model.bean.ShelvesRawBean;

import java.util.List;

public interface IShelvesMvpContract {

    int REFRESH_AUTO = 0;
    int REFRESH_CACHE = 1;

    @IntDef({REFRESH_AUTO, REFRESH_CACHE})
    @interface RefreshType {
    }

    interface IView {
        void onRefreshFinished(@RefreshType int refreshType, List<ShelvesRawBean> newsEntity);

        void showTips(String message);
    }

    interface IPresenter {
        void refresh(@RefreshType int refreshType);

        void destroy();
    }

}
