package com.jamesdube.laravelnewsapp.util;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.util.Log;

import com.jamesdube.laravelnewsapp.App;
import com.jamesdube.laravelnewsapp.R;

/**
 * Created by rick on 1/8/17.
 */

public class Themes {

    public static void applyTheme(Activity activity){
        String theme = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext())
                .getString("pref_theme", "light");
        switch (theme){
            case "light":{
                activity.setTheme(R.style.AppTheme_NoActionBar);
                break;
            }
            case "dark":{
                activity.setTheme(R.style.AppTheme_Dark_NoActionBar);
                break;
            }
            default:{
                activity.setTheme(R.style.AppTheme_NoActionBar);
                break;
            }
        }
    }

    public static int getColorAccent() {
        String theme = PreferenceManager.getDefaultSharedPreferences(App.getAppContext().getApplicationContext())
                .getString("pref_theme", "light");
        switch (theme){
            case "light":{
                return R.color.colorAccent;
            }
            case "dark":{
                return R.color.colorAccent_dark;
            }
            default:{
                return R.color.colorAccent;
            }
        }
    }
}
