package com.jamesdube.laravelnewsapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.RingtonePreference;
import android.preference.SwitchPreference;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.jamesdube.laravelnewsapp.util.Themes;

public class Settings extends AppCompatActivity {

    private SharedPreferences prefs;
    private SharedPreferences.OnSharedPreferenceChangeListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Themes.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        assert actionBar != null;
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                // Implementation
                switch (key){
                    case "pref_theme":
                        Log.d(App.Tag,"pref theme => "+ prefs.getString(key,"ll"));
                        finish();
                        final Intent intent = getIntent();
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        break;
                    case "notifications_new_message":
                        Log.d(App.Tag,"pref notification => "+ prefs.getBoolean(key,true));
                        break;
                    case "sync_frequency":
                        Log.d(App.Tag,"pref sync => "+ prefs.getString(key,"ll"));
                        break;
                }
            }
        };

        prefs.registerOnSharedPreferenceChangeListener(listener);


    }


}
