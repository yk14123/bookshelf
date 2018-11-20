package com.chinafocus.bookshelf.global;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;

import java.io.File;

public class BookShelfApplication extends Application {

    public static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();

        Global.serializableFileDir = getFilesDir().getAbsolutePath() + File.separator + "Ser";
        Global.serializableFileDirNotDelete = getFilesDir().getAbsolutePath() + File.separator + "SerNotDelete";

        LeakCanary.install(this);

        sContext = getApplicationContext();

    }

}
