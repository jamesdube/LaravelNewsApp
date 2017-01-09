package com.jamesdube.laravelnewsapp.posts;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jamesdube.laravelnewsapp.App;
import com.jamesdube.laravelnewsapp.R;
import com.jamesdube.laravelnewsapp.adapters.PostAdapter;

public class PostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        Glide.with(App.getAppContext())
                .load(PostAdapter.getRandomImage())
                .into(imageView);

    }
}
