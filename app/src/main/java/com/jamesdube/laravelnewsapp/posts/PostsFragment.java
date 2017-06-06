package com.jamesdube.laravelnewsapp.posts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jamesdube.laravelnewsapp.App;
import com.jamesdube.laravelnewsapp.MainActivity;
import com.jamesdube.laravelnewsapp.R;
import com.jamesdube.laravelnewsapp.adapters.PostAdapter;
import com.jamesdube.laravelnewsapp.models.Post;
import com.jamesdube.laravelnewsapp.models.PostRepository;
import com.jamesdube.laravelnewsapp.sync.SyncAdapter;
import com.jamesdube.laravelnewsapp.util.RecyclerViewEmptySupport;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

import static com.jamesdube.laravelnewsapp.MainActivity.Title;
import static com.jamesdube.laravelnewsapp.util.Constants.CATEGORY_PACKAGES;
import static com.jamesdube.laravelnewsapp.util.Constants.CATEGORY_TUTORIALS;
import static com.jamesdube.laravelnewsapp.util.Constants.POSTS_ACTIVE;
import static com.jamesdube.laravelnewsapp.util.Constants.POSTS_ARCHIVED;
import static com.jamesdube.laravelnewsapp.util.Constants.POSTS_FAVOURITES;
import static com.jamesdube.laravelnewsapp.util.Constants.POSTS_PACKAGES;
import static com.jamesdube.laravelnewsapp.util.Constants.POSTS_TUTORIALS;

public class PostsFragment extends Fragment {

    RecyclerViewEmptySupport recyclerView;
    PostAdapter adapter;
    LinearLayout emptyView;
    BroadcastReceiver syncSuccess,syncError;
    RealmChangeListener<RealmResults<Post>> changeListener;
    SwipeRefreshLayout swipeRefreshLayout;
    public static String POSTS = "POSTS";


    public static PostsFragment newInstance() {

        Bundle args = new Bundle();

        PostsFragment fragment = new PostsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        boot();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_posts,container,false);
    }

    private void boot() {

        RealmResults <Post> posts = getPosts();

        recyclerView = (RecyclerViewEmptySupport) getActivity().findViewById(R.id.postsRecyclerview);
        swipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.postsSwipeLayout);
        emptyView = (LinearLayout) getActivity().findViewById(R.id.postsEmptyView);
        emptyView = (LinearLayout) getActivity().findViewById(R.id.postsEmptyView);
        emptyView = (LinearLayout) getActivity().findViewById(R.id.postsEmptyView);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(App.Tag,"posts fragment onRefresh");
                registerReceivers();
                SyncAdapter.syncImmediately(App.getAppContext());
            }
        });

        syncSuccess = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("xxxx ", "Sync finished, should refresh now");
                swipeRefreshLayout.setRefreshing(false);
                //repopulate
                adapter.notifyDataSetChanged();
                unRegisterReceivers();
                MainActivity.showSnackBar("Refresh complete...");

            }
        };

        syncError = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(App.Tag, "Sync finished with error");
                swipeRefreshLayout.setRefreshing(false);
                //repopulate
                adapter.notifyDataSetChanged();
                //notify the user
                MainActivity.showSnackBar("Cannot refresh feed at the moment...");
                unRegisterReceivers();

            }
        };

        changeListener = new RealmChangeListener<RealmResults<Post>>() {

            @Override
            public void onChange(RealmResults<Post> element) {
                Log.d(App.Tag,"posts fragment onChange posts("+element.size()+")");
                adapter.notifyDataSetChanged(); // Update the UI
                swipeRefreshLayout.setRefreshing(false);
            }
        };

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final Post post = adapter.getPost(viewHolder.getAdapterPosition());
                PostRepository.archive(post);
                adapter.notifyDataSetChanged();

                //show notification
                MainActivity.showSnackBarWithAction("Post archived", "Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PostRepository.unArchive(post);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        };

        ItemTouchHelper touchHelper = new ItemTouchHelper(simpleCallback);
        touchHelper.attachToRecyclerView(recyclerView);
        // Tell Realm to notify our listener when the posts results
        // have changed (items added, removed, updated, anything of the sort).

        setupAdapter(posts);

    }

    public void setPosts(String criteria) {

        POSTS = criteria;

        //change the title
        Title = (POSTS);


        RealmResults<Post> posts = getPosts();

        posts.addChangeListener(changeListener);

        adapter = new PostAdapter(posts);

        recyclerView.setAdapter(adapter);
    }

    @NonNull
    private RealmResults<Post> getPosts() {

        switch (POSTS) {
            case POSTS_ACTIVE: {
                return PostRepository.getActive();
            }
            case POSTS_ARCHIVED: {
                return PostRepository.getArchived();
            }
            case POSTS_PACKAGES: {
                return PostRepository.getByCategory(CATEGORY_PACKAGES);
            }
            case POSTS_TUTORIALS: {
                return PostRepository.getByCategory(CATEGORY_TUTORIALS);
            }
            case POSTS_FAVOURITES: {
                return PostRepository.getFavourites();
            }
            default: {
                return PostRepository.getActive();
            }
        }
    }

    public void setupAdapter(RealmResults<Post> posts) {
        posts.addChangeListener(changeListener);
        adapter = new PostAdapter(posts);
        recyclerView.setLayoutManager(new LinearLayoutManager(App.getAppContext()));
        recyclerView.setEmptyView(emptyView);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private void registerReceivers() {
        getActivity().registerReceiver(syncSuccess,new IntentFilter(SyncAdapter.SYNC_FINISHED));
        getActivity().registerReceiver(syncError,new IntentFilter(SyncAdapter.SYNC_FINISHED_ERROR));
    }

    private void unRegisterReceivers() {
        getActivity().unregisterReceiver(syncSuccess);
        getActivity().unregisterReceiver(syncError);
    }


}
