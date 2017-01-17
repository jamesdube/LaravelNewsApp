package com.jamesdube.laravelnewsapp.posts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jamesdube.laravelnewsapp.App;
import com.jamesdube.laravelnewsapp.R;
import com.jamesdube.laravelnewsapp.adapters.PostAdapter;
import com.jamesdube.laravelnewsapp.http.Client;
import com.jamesdube.laravelnewsapp.http.requests.onGetPosts;
import com.jamesdube.laravelnewsapp.models.Post;

import java.util.List;

public class PostsFragment extends Fragment {

    RecyclerView postsRecyclerview;
    PostAdapter postAdapter;

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
        getPosts();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_posts,container,false);
    }

    private void boot() {
        postsRecyclerview = (RecyclerView) getActivity().findViewById(R.id.postsRecyclerview);
        postsRecyclerview.setLayoutManager(new LinearLayoutManager(App.getAppContext()));
    }

    private void getPosts() {
        Client.getPosts(new onGetPosts() {
            @Override
            public void onSuccess(List<Post> posts) {
                setupPosts(posts);
            }

            @Override
            public void onFailure() {

            }
        });
    }

    private void setupPosts(List<Post> posts){
        postAdapter = new PostAdapter(posts);
        postsRecyclerview.setAdapter(postAdapter);
    }
}
