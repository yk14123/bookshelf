package com.chinafocus.bookshelf.model.repository.shelves;


import com.chinafocus.bookshelf.model.network.ApiManager;

import io.reactivex.Observable;

class RemoteShelvesSourceData {

    RemoteShelvesSourceData() {
    }

    Observable<String> get(String category, String[] parms) {

        if (category.equals("getShelves")) {
            return ApiManager.getInstance().getService().getShelves();
        }

        if (category.equals("getShelvesDetail")) {
            return ApiManager.getInstance().getService().getShelvesDetail(parms[0]);
        }

        if (category.equals("getBookCategoryDetail")) {
            return ApiManager.getInstance().getService().getBookCategoryDetail(parms[0], parms[1]);
        }

        if (category.equals("getBookMetadata")) {
            return ApiManager.getInstance().getService().getBookMetadata(parms[0], parms[1], parms[2]);
        }

        if (category.equals("getBookContentDetail")) {
            return ApiManager.getInstance().getService().getBookContentDetail(parms[0], parms[1], parms[2], parms[3]);
        }

        return null;
    }

}
