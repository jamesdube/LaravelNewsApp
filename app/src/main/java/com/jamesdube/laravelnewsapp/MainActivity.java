package com.jamesdube.laravelnewsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jamesdube.laravelnewsapp.models.PostRepository;
import com.jamesdube.laravelnewsapp.posts.PostsFragment;
import com.jamesdube.laravelnewsapp.sync.SyncAdapter;
import com.jamesdube.laravelnewsapp.util.Themes;

import static com.jamesdube.laravelnewsapp.util.Constants.POSTS_ACTIVE;
import static com.jamesdube.laravelnewsapp.util.Constants.POSTS_ARCHIVED;
import static com.jamesdube.laravelnewsapp.util.Constants.POSTS_PACKAGES;
import static com.jamesdube.laravelnewsapp.util.Constants.POSTS_TUTORIALS;
import static com.jamesdube.laravelnewsapp.util.Constants.TITLE_ARCHIVED;
import static com.jamesdube.laravelnewsapp.util.Constants.TITLE_HOME;
import static com.jamesdube.laravelnewsapp.util.Constants.TITLE_PACKAGES;
import static com.jamesdube.laravelnewsapp.util.Constants.TITLE_TUTORIALS;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    static CoordinatorLayout coordinatorLayout;
    FragmentManager fragmentManager;
    PostsFragment postsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Themes.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //create account first
        SyncAdapter.initializeSyncAdapter(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragmentManager = getSupportFragmentManager();
        postsFragment = PostsFragment.newInstance();
        commitFragment(postsFragment);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.mainCoordinatorLayout);

        //Set all posts as seen
        PostRepository.setPostsAsSeen();
    }

    private void commitFragment(Fragment fragment) {
        fragmentManager.beginTransaction()
                .replace(R.id.postsFragment, fragment)
                .commit();
    }

    public static void showSnackBar(String message) {
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    public static void showSnackBarWithAction(String message,String actionText, View.OnClickListener action) {
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG).setAction(actionText,action).show();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            postsFragment.setPosts(POSTS_ACTIVE);
            setTitle(TITLE_HOME);
        } else if (id == R.id.nav_archived) {
           postsFragment.setPosts(POSTS_ARCHIVED);
            setTitle(TITLE_ARCHIVED);
        } else if (id == R.id.nav_packages) {
            setTitle(TITLE_PACKAGES);
            postsFragment.setPosts(POSTS_PACKAGES);
        } else if (id == R.id.nav_tutorials) {
            setTitle(TITLE_TUTORIALS);
            postsFragment.setPosts(POSTS_TUTORIALS);
        } else if (id == R.id.nav_share) {
            SyncAdapter.syncImmediately(this);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Set the Title of the activity
     */
    private void setTitle(String title){

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle(title);
        }

    }
}