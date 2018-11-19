package com.chinafocus.bookshelf.model.repository.shelves;


import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

class VolRepository implements IShelvesRepository {

    private LocalShelvesSource mLocalShelvesSource;
    private RemoteShelvesSourceData mRemoteShelvesSourceData;

    private VolRepository() {
        mLocalShelvesSource = new LocalShelvesSource();
        mRemoteShelvesSourceData = new RemoteShelvesSourceData();
    }

//    public static IShelvesRepository getInstance() {
//        return Holder.INSTANCE;
//    }

    static class Holder {
        static IShelvesRepository INSTANCE = new VolRepository();
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

//                        SystemClock.sleep(3000);

                        mLocalShelvesSource.save(category, string);
                    }
                });
    }

    @Override
    public Observable<String> getCacheShelves(String category) {
        return mLocalShelvesSource.get(category);
    }

}
