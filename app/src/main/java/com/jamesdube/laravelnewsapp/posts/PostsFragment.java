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
import com.jamesdube.laravelnewsapp.R;
import com.jamesdube.laravelnewsapp.adapters.PostAdapter;
import com.jamesdube.laravelnewsapp.models.Post;
import com.jamesdube.laravelnewsapp.models.PostRepository;
import com.jamesdube.laravelnewsapp.sync.SyncAdapter;

import java.util.List;

import io.realm.Realm;

public class PostsFragment extends Fragment {

    RecyclerView postsRecyclerview;
    PostAdapter postAdapter;
    List<Post> posts;
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
        setupPosts();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_posts,container,false);
    }

    private void boot() {
        postsRecyclerview = (RecyclerView) getActivity().findViewById(R.id.postsRecyclerview);
        postsRecyclerview.setLayoutManager(new LinearLayoutManager(App.getAppContext()));
        swipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.postsSwipeLayout);
        //Implement onRefresh Action
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //get posts
                SyncAdapter.syncImmediately(getActivity());
            }
        });
        getActivity().registerReceiver(syncFinishedReceiver,new IntentFilter(SyncAdapter.SYNC_FINISHED));

    }


        private BroadcastReceiver syncFinishedReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("xxxx ", "Sync finished, should refresh nao!!");
                swipeRefreshLayout.setRefreshing(false);
                //repopulate
                postAdapter.clear();
                postAdapter.addAll(PostRepository.getUnreadPosts());
                postAdapter.notifyDataSetChanged();

            }
        };

    private void setupPosts(){
        //setup
        posts = PostRepository.getUnreadPosts();
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT){

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                archivePost(viewHolder.getAdapterPosition());
            }
        };
        ItemTouchHelper touchHelper = new ItemTouchHelper(simpleCallback);
        touchHelper.attachToRecyclerView(postsRecyclerview);
        postAdapter = new PostAdapter(getContext(),posts);
        postsRecyclerview.setAdapter(postAdapter);
    }

    private void archivePost(final int postPosition) {
        final Post post = posts.get(postPosition);
        final String link = post.getLink();

        App.Realm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Post post1 = realm.where(Post.class).equalTo("link",link).findFirst();
                post1.setWasRead(true);
                realm.copyToRealmOrUpdate(post1);
            }
        });
        //setupPosts();
        posts.remove(postPosition);
        postAdapter.notifyDataSetChanged();

    }


}
