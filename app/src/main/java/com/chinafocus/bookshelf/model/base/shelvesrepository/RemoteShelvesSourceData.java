package com.chinafocus.bookshelf.model.base.shelvesrepository;


import com.chinafocus.bookshelf.model.base.network.ApiManager;

import io.reactivex.Observable;

class RemoteShelvesSourceData {

    RemoteShelvesSourceData() {
    }

    Observable<String> get(String category, String[] parms) {

        if (category.equals("getShelves")) {
//            return ApiManager.getInstance().getService().getShelves(parms[0]);
            return ApiManager.getInstance().getNewService().getShelves(parms[0]);
        }

        if (category.equals("getShelvesDetail")) {
            return ApiManager.getInstance().getNewService().getShelvesDetail(parms[0], parms[1]);
        }

        if (category.equals("getBookCategoryDetail")) {
            return ApiManager.getInstance().getNewService().getBookCategoryDetail(parms[0], parms[1], parms[2]);
        }

        if (category.equals("getBookMetadata")) {
            return ApiManager.getInstance().getNewService().getBookMetadata(parms[0], parms[1], parms[2], parms[3]);
        }

//        if (category.equals("getBookContentDetail")) {
//            return ApiManager.getInstance().getNewService().getBookContentDetail(parms[0], parms[1], parms[2], parms[3], parms[4]);
//        }

        if (category.equals("getBookContentAESDetail")) {
            return ApiManager.getInstance().getNewService().getBookContentAESDetail(parms[0], parms[1], parms[2], parms[3], parms[4]);
        }

        return null;
    }

}
