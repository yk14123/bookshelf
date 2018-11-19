package com.chinafocus.bookshelf.model.repository.shelvesdetail;


import com.chinafocus.bookshelf.model.network.ApiManager;

import io.reactivex.Observable;

public class RemoteShelvesDetailSourceData {


    public RemoteShelvesDetailSourceData() {
    }

    public Observable<String> getRemoteShelvesDetail(String shelfId) {
//        return BookShelfApplication.mIShelvesApi.getShelves();
//        return mIShelvesApi.getShelves();
        return ApiManager.getInstance().getService().getShelvesDetail(shelfId);
    }

}
