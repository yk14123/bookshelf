package com.chinafocus.bookshelf.model.repository.forstring;


import android.os.SystemClock;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

public class NewsRepositoryForString {

    private LocalNewsSourceForString mLocalNewsSourceForString;
    private RemoteNewsSourceDataForString mRemoteNewsSourceDataForString;

    private NewsRepositoryForString() {
        mLocalNewsSourceForString = new LocalNewsSourceForString();
        mRemoteNewsSourceDataForString = new RemoteNewsSourceDataForString();
    }

    public static NewsRepositoryForString getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static NewsRepositoryForString INSTANCE = new NewsRepositoryForString();
    }

    public Observable<String> getNetNews(String category) {
        return mRemoteNewsSourceDataForString.getNews(category)
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String rawResult) throws Exception {
                        Log.i("MyLog", "从网络取数据getNetNews-->saveNews");
                        /**
                         * 只能操作一次，okhttp就关流
                         */
                        String string = rawResult;

                        SystemClock.sleep(3000);

                        mLocalNewsSourceForString.saveNews(string);
                    }
                });
    }

    public Observable<String> getCacheNews(String category) {
        return mLocalNewsSourceForString.getNews(category);
    }

}
