package com.jamesdube.laravelnewsapp.posts;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
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
import com.jamesdube.laravelnewsapp.MainActivity;
import com.jamesdube.laravelnewsapp.R;
import com.jamesdube.laravelnewsapp.Settings;
import com.jamesdube.laravelnewsapp.adapters.PostAdapter;
import com.jamesdube.laravelnewsapp.models.Post;
import com.jamesdube.laravelnewsapp.models.PostRepository;
import com.jamesdube.laravelnewsapp.sync.SyncAdapter;
import com.jamesdube.laravelnewsapp.util.MenuTint;
import com.jamesdube.laravelnewsapp.util.Prefs;
import com.jamesdube.laravelnewsapp.util.Themes;

import org.ocpsoft.prettytime.PrettyTime;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;

public class PostActivity extends AppCompatActivity {
    WebView webView;
    TextView postTitle,pubDate;
    ImageView postImage;
    Post post;
    CoordinatorLayout coordinatorLayout;
    private ShareActionProvider mShareActionProvider;
    Toolbar toolbar;

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Log.d(App.Tag,"on post create");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Themes.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        boot();

        post = getPost();

        post.addChangeListener(new RealmChangeListener<Post>() {
            @Override
            public void onChange(Post element) {
                Log.d(App.Tag, "post onChange => " + String.valueOf(element.isFavourite()));
            }
        });

        //set the title
        postTitle.setText(post.getTitle());

        //set the pub date
        pubDate.setText(getHumanFriendlyTime(post.getPubDate()));

        //set the Image
        Glide.with(App.getAppContext())
                .load(post.getCoverImage())
                .into(postImage);


        //load the content
        webView.loadDataWithBaseURL("file:///android_asset/", post.getDescription(), "text/html", "utf-8", null);

        //setupFavIcon();
        Log.d(App.Tag,"on create");

    }

    @NonNull
    private String getHumanFriendlyTime(Date date) {
        PrettyTime prettyTime = new PrettyTime(date);
        return prettyTime.formatApproximateDuration(new Date()) + " ago";
    }

    private Post getPost(){
        Bundle getBundle = null;
        getBundle = getIntent().getExtras();

        String link = getBundle.getString("POST");

        return App.Realm().where(Post.class)
                .equalTo("link",link)
                .findFirst();
    }



    private void boot(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if( getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        }

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.postCoordinatorLayout);

        postTitle = (TextView) findViewById(R.id.postTitle);
        pubDate = (TextView) findViewById(R.id.pubDate);
        postImage = (ImageView) findViewById(R.id.backdrop);
        webView = (WebView) findViewById(R.id.webView) ;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_post, menu);
        setupFavIcon();
        Log.d(App.Tag,"on create options");
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(App.Tag,"on start");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(App.getAppContext(),Settings.class));
            return true;
        }else  if (id == R.id.action_favorite) {
           favourite(post);
        }else  if (id == R.id.action_share) {
            shareTextUrl();
        }
        return super.onOptionsItemSelected(item);
    }

    private void favourite(final Post post) {
        PostRepository.favourite(post, new PostRepository.onChangeField() {
            @Override
            public void onSuccess() {
                String message = "Post added to favourites";
                if(!post.isFavourite()){
                    message = "Post removed from favourites";
                }
                setupFavIcon();
                showSnackBar(message);
            }

            @Override
            public void onError(Throwable error) {
                showSnackBar("An error occurred");
            }
        });
    }

    public void showSnackBar(String message) {
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void shareTextUrl() {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, post.getTitle());
        share.putExtra(Intent.EXTRA_TEXT, post.getLink() + " via Laravel News App .Get the application here (link)");

        startActivity(Intent.createChooser(share, "Share link!"));
    }

    private void setupFavIcon(){

        int favIcon = R.drawable.ic_favorite;
        if(post.isFavourite() != null && post.isFavourite()){
            //change the icon
            favIcon = R.drawable.ic_favorite_true;
        }
        toolbar.getMenu().getItem(0).setIcon(favIcon);
        int color;
        color = getResources().getColor(R.color.white);
        if(Objects.equals(Prefs.getTheme(), Prefs.THEME_LIGHT)){
            color = getResources().getColor(R.color.textColorSecondary);
        }
        MenuTint.colorIcons(this,toolbar.getMenu(),color);
    }
}
