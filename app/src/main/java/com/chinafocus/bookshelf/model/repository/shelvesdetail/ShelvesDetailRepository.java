package com.chinafocus.bookshelf.model.repository.shelvesdetail;


import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

public class ShelvesDetailRepository {

    private LocalShelvesDetailSource mLocalShelvesSource;
    private RemoteShelvesDetailSourceData mRemoteShelvesSourceData;

    private ShelvesDetailRepository() {
        mLocalShelvesSource = new LocalShelvesDetailSource();
        mRemoteShelvesSourceData = new RemoteShelvesDetailSourceData();
    }

    public static ShelvesDetailRepository getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static ShelvesDetailRepository INSTANCE = new ShelvesDetailRepository();
    }

    public Observable<String> getNetShelvesDetail(final String category) {
        return mRemoteShelvesSourceData.getRemoteShelvesDetail(category)
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

    public Observable<String> getCacheShelvesDetail(String category) {
        return mLocalShelvesSource.getShelves(category);
    }

}
