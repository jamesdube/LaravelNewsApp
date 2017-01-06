package com.jamesdube.laravelnewsapp;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * Created by rick on 1/6/17.
 */

public class App extends Application {
    private static App Instance;
    public static String Tag = "LaravelNewsApp";

    @Override
    public void onCreate() {
        super.onCreate();
        Instance = this;
    }

    public static App getInstance() {
        if(Instance == null) {
            Log.d(Tag,"app init");
            Instance = new App();
        }
        return Instance;
    }

    public static Context getAppContext() {
        return getInstance().getApplicationContext();
    }

}
