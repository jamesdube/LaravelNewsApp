package com.jamesdube.laravelnewsapp.about;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.jamesdube.laravelnewsapp.R;
import com.jamesdube.laravelnewsapp.util.Themes;

import mehdi.sakout.aboutpage.AboutPage;

public class Developer extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Themes.applyTheme(this);
        super.onCreate(savedInstanceState);
        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.mipmap.ic_launcher)
                .setDescription(getResources().getString(R.string.activity_about_developer_description))
                .addWebsite("http://jamesdube.com")
                .addTwitter("jamesddube")
                .addInstagram("jamesddube")
                .addEmail("jamesddube@live.com")
                .addGitHub("jamesddube")
                .create();
        setContentView(aboutPage);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle("About");
        }
    }
}
