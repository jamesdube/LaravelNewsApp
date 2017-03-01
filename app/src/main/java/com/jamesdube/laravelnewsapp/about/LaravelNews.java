package com.jamesdube.laravelnewsapp.about;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jamesdube.laravelnewsapp.R;
import com.jamesdube.laravelnewsapp.util.Themes;

import mehdi.sakout.aboutpage.AboutPage;

public class LaravelNews extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Themes.applyTheme(this);
        super.onCreate(savedInstanceState);
        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.mipmap.ic_launcher)
                .setDescription(getResources().getString(R.string.activity_about_description))
                .addGroup("Laravel News")
                .addWebsite("http://laravel-news.com")
                .addFacebook("laravelnews")
                .addTwitter("laravelnews")
                .addYoutube("UC_HmfSCvAl_JaqdwN5YSVsg")
                .addInstagram("laravelnews")
                .create();
        setContentView(aboutPage);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle("About");
        }
    }
}
