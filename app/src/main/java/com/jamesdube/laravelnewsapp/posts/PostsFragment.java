package com.jamesdube.laravelnewsapp.posts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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

import com.jamesdube.laravelnewsapp.App;
import com.jamesdube.laravelnewsapp.MainActivity;
import com.jamesdube.laravelnewsapp.R;
import com.jamesdube.laravelnewsapp.adapters.PostAdapter;
import com.jamesdube.laravelnewsapp.models.Post;
import com.jamesdube.laravelnewsapp.models.PostRepository;
import com.jamesdube.laravelnewsapp.sync.SyncAdapter;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class PostsFragment extends Fragment {

    RecyclerView recyclerView;
    PostAdapter adapter;
    Realm realm;
    BroadcastReceiver syncSuccess,syncError;
    RealmChangeListener<RealmResults<Post>> changeListener;
    SwipeRefreshLayout swipeRefreshLayout;
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
        //SyncAdapter.initializeSyncAdapter(getActivity());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_posts,container,false);
    }

    private void boot() {

        recyclerView = (RecyclerView) getActivity().findViewById(R.id.postsRecyclerview);
        swipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.postsSwipeLayout);
        // ... boilerplate omitted for brevity
        realm = Realm.getDefaultInstance();
        // get all the posts
        final RealmResults<Post> posts = PostRepository.getActive();
        // ... build a list adapter and set it to the ListView/RecyclerView/etc
        adapter = new PostAdapter(posts);
        // set up a Realm change listener

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
                Log.d("xxxx ", "Sync finished, should refresh nao!!");
                swipeRefreshLayout.setRefreshing(false);
                //repopulate
                adapter.notifyDataSetChanged();
                unRegisterReceivers();
                MainActivity.showSnackBar("posts Refreshed...");

            }
        };

        syncError = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("xxxx ", "Sync finished with error");
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
                Post post = adapter.getPost(viewHolder.getAdapterPosition());
                PostRepository.archive(post);
                adapter.notifyDataSetChanged();
            }
        };

        ItemTouchHelper touchHelper = new ItemTouchHelper(simpleCallback);
        touchHelper.attachToRecyclerView(recyclerView);
        // Tell Realm to notify our listener when the posts results
        // have changed (items added, removed, updated, anything of the sort).

        posts.addChangeListener(changeListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(App.getAppContext()));
        recyclerView.setAdapter(adapter);

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
