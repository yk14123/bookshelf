package com.chinafocus.bookshelf.model.repository.shelves;


import android.util.Log;

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
    public Observable<String> getNetShelves(final String category, String[] parms) {
        return mRemoteShelvesSourceData.get(category, parms)
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String rawResult) throws Exception {
                        Log.i("MyLog", "从网络取数据getNetNews-->saveNews");
                        /**
                         * 只能操作一次，okhttp就关流
                         */
                        String string = rawResult;

                        mLocalShelvesSource.save(category, string);
                    }
                });
    }

    @Override
    public Observable<String> getCacheShelves(String category) {
        return mLocalShelvesSource.get(category);
    }

}
