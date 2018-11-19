package com.chinafocus.bookshelf.model.repository.shelves;


import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

public class ShelvesRepository {

    private LocalShelvesSource mLocalShelvesSource;
    private RemoteShelvesSourceData mRemoteShelvesSourceData;

    private ShelvesRepository() {
        mLocalShelvesSource = new LocalShelvesSource();
        mRemoteShelvesSourceData = new RemoteShelvesSourceData();
    }

    public static ShelvesRepository getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static ShelvesRepository INSTANCE = new ShelvesRepository();
    }

    public Observable<String> getNetShelves(final String category) {
        return mRemoteShelvesSourceData.getRemoteShelves(category)
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String rawResult) throws Exception {
                        Log.i("MyLog", "从网络取数据getNetNews-->saveNews");
                        /**
                         * 只能操作一次，okhttp就关流
                         */
                        String string = rawResult;

//                        SystemClock.sleep(3000);

                        mLocalShelvesSource.saveShelves(category, string);
                    }
                });
    }

    public Observable<String> getCacheShelves(String category) {
        return mLocalShelvesSource.getShelves(category);
    }

}
