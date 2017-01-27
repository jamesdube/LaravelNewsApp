package com.jamesdube.laravelnewsapp;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.jamesdube.laravelnewsapp.util.Themes;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;

/**
 * Created by james
 */

public class App extends Application {
    private static App Instance;
    private static Gson gson;
    public static String Tag = "LaravelNewsApp";
    private static Realm realm;

    @Override
    public void onCreate() {
        super.onCreate();
        Instance = this;
        Realm();
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

    public static Realm Realm() {
        if(realm == null){
            // Create a RealmConfiguration that saves the Realm file in the app's "files" directory.
            Realm.init(getAppContext());
            RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                    .deleteRealmIfMigrationNeeded()
                    .build();
            Realm.setDefaultConfiguration(realmConfig);

            // Get a Realm instance for this thread
            realm = Realm.getDefaultInstance();

            realm.addChangeListener(new RealmChangeListener<Realm>() {
                @Override
                public void onChange(Realm element) {
                    Log.d(Tag,"app realmOnChangeListener");
                }
            });
        }

        return realm;
    }
}
