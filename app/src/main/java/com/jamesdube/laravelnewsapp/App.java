package com.jamesdube.laravelnewsapp;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.jamesdube.laravelnewsapp.util.Themes;

/**
 * Created by rick on 1/6/17.
 */

public class App extends Application {
    private static App Instance;
    private static Gson gson;
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

    //Gson Singleton
    public static Gson Gson(){
        if(gson == null){
            gson = new Gson();
        }
        return gson;
    }

}
