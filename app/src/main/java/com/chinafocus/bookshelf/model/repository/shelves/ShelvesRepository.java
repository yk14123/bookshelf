package com.chinafocus.bookshelf.model.repository.shelves;


import android.support.annotation.NonNull;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

class ShelvesRepository extends AbstractShelvesRepository {

    private LocalShelvesSource mLocalShelvesSource;
    private RemoteShelvesSourceData mRemoteShelvesSourceData;

    private ShelvesRepository() {
        mLocalShelvesSource = new LocalShelvesSource();
        mRemoteShelvesSourceData = new RemoteShelvesSourceData();
    }


    static class Holder {
        static IShelvesRepository INSTANCE = new ShelvesRepository();
    }


    @Override
    public Observable<String> getNetShelves(String category, String[] parms) {

        String key = getCacheKey(category, parms);

        return mRemoteShelvesSourceData.get(category, parms)
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String rawResult) throws Exception {

                        mLocalShelvesSource.save(key, rawResult);

                    }
                });
    }

    @Override
    public Observable<String> getCacheShelves(String category, String[] parms) {

        return mLocalShelvesSource.get(getCacheKey(category, parms));
    }

    @NonNull
    private String getCacheKey(String category, String[] parms) {
        String key = category;

        if (category.equals("getShelvesDetail")) {
            key = category + "&" + parms[0];
        }

        if (category.equals("getBookCategoryDetail")) {
            key = category + "&" + parms[0] + "&" + parms[1];
        }

        if (category.equals("getBookMetadata")) {
            key = category + "&" + parms[0] + "&" + parms[1] + "&" + parms[2];
        }

        if (category.equals("getBookContentDetail")) {
            key = category + "&" + parms[0] + "&" + parms[1] + "&" + parms[2] + "&" + parms[3];
        }
        return key;
    }

}
