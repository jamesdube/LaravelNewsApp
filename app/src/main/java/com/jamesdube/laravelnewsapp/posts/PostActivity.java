package com.jamesdube.laravelnewsapp.posts;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.bkhezry.extrawebview.ExtraWebViewCreator;
import com.github.bkhezry.extrawebview.data.DataModel;
import com.github.bkhezry.extrawebview.data.DataModelBuilder;
import com.jamesdube.laravelnewsapp.App;
import com.jamesdube.laravelnewsapp.R;
import com.jamesdube.laravelnewsapp.SettingsActivity;
import com.jamesdube.laravelnewsapp.adapters.PostAdapter;
import com.jamesdube.laravelnewsapp.models.Post;
import com.jamesdube.laravelnewsapp.sync.SyncAdapter;
import com.jamesdube.laravelnewsapp.util.Themes;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class PostActivity extends AppCompatActivity {
    WebView webView;
    TextView postTitle,pubDate;
    ImageView postImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Themes.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        boot();


        Post post = getPost();

        //set the title
        postTitle.setText(post.getTitle());

        //set the pub date
        pubDate.setText(post.getPubDate());

        //set the Image
        Glide.with(App.getAppContext())
                .load(post.getCoverImage())
                .into(postImage);

        String html = prepHtml(post);

        //load the content
        webView.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "utf-8", null);

    }

    private Post getPost(){
        Bundle getBundle = null;
        getBundle = getIntent().getExtras();

        String post = getBundle.getString("POST");
        return Post.fromJson(post);
    }

    private String prepHtml(Post post){
        return "<HTML><HEAD><link href=\"style.css\" type=\"text/css\" rel=\"stylesheet\"/></HEAD><body>" +
                "<div class=\"container-fluid\"><div class=\"row\"> <div class=\"col-lg-12\">" +
                post.getDescription().replaceFirst("<img([^<]*)>", "") +
                " </div></div></div></body></HTML>";
    }

    private void boot(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if( getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        postTitle = (TextView) findViewById(R.id.postTitle);
        pubDate = (TextView) findViewById(R.id.pubDate);
        postImage = (ImageView) findViewById(R.id.backdrop);
        webView = (WebView) findViewById(R.id.webView) ;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(App.getAppContext(),SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
