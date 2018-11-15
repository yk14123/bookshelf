package com.chinafocus.bookshelf.global;

import android.app.Application;

import java.io.File;

public class BookShelfApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Global.serializableFileDir = getFilesDir().getAbsolutePath() + File.separator + "Ser";
        Global.serializableFileDirNotDelete = getFilesDir().getAbsolutePath() + File.separator + "SerNotDelete";
    }
}
