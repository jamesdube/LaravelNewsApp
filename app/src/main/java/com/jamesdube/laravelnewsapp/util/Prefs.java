package com.jamesdube.laravelnewsapp.util;

import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.util.Log;

import com.jamesdube.laravelnewsapp.App;

public class Prefs {

    public static final int SYNC_INTERVAL_FIFTEEN = 900;
    public static final int SYNC_INTERVAL_THIRTY = 1800;
    public static final int SYNC_INTERVAL_SIXTY = 3600;
    public static final String THEME_LIGHT = "light";
    public static final String THEME_DARK = "dark";
    public static final String THEME_LARAVEL = "laravel";



    public static int getSyncInterval(){

        int syncInterval =  PreferenceManager.getDefaultSharedPreferences(App.getAppContext().getApplicationContext())
                .getInt("sync_frequency", SYNC_INTERVAL_THIRTY);
        Log.d(App.Tag,"sync frequency => " + String.valueOf(syncInterval/60));
        return syncInterval;
    }

    public static String getTheme(){
        return PreferenceManager.getDefaultSharedPreferences(App.getAppContext().getApplicationContext())
                .getString("pref_theme", THEME_LIGHT);
    }

    public static boolean NotificationsEnabled(){
        return PreferenceManager.getDefaultSharedPreferences(App.getAppContext().getApplicationContext())
                .getBoolean("notifications_new_message", true);
    }


}
