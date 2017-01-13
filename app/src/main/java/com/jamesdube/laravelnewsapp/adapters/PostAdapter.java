package com.jamesdube.laravelnewsapp.adapters;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jamesdube.laravelnewsapp.App;
import com.jamesdube.laravelnewsapp.R;
import com.jamesdube.laravelnewsapp.models.Post;
import com.jamesdube.laravelnewsapp.posts.PostActivity;

import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    List<Post> posts;
    private LayoutInflater inflator;
    public PostAdapter(List<Post> posts) {
        this.posts = posts;
        inflator = LayoutInflater.from(App.getAppContext());
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflator.inflate(R.layout.template_posts,parent,false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PostViewHolder holder, final int position) {

        posts.get(position).extractImageUrl();
        holder.title.setText(posts.get(position).getTitle());
        //holder.subTitle.setText(posts.get(position).getSubTitle());
        //holder.coverImage.setImageUrl(posts.get(position).getCoverImage(),new ImageLoader());
        Glide.with(App.getAppContext()).load(getRandomImage())
                .into(holder.coverImage);
        holder.coverImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent postIntent = new Intent(App.getAppContext(), PostActivity.class);
                postIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                postIntent.putExtra("POST",posts.get(position).toJson());
                App.getAppContext().startActivity(postIntent);
            }
        });

    }

    public static int getRandomImage(){
        Random rand = new Random();
        switch (rand.nextInt(8)){
            case 1 : {
                return R.drawable.article1;
            }
            case 2 : {
                return R.drawable.article2;
            }
            case 3 : {
                return R.drawable.article3;
            }
            case 4 : {
                return R.drawable.article4;
            }
            case 5 : {
                return R.drawable.article5;
            }
            case 6 : {
                return R.drawable.article6;
            }
            case 7 : {
                return R.drawable.article7;
            }
            case 8 : {
                return R.drawable.article8;
            }
            default:{
                return R.drawable.article1;
            }
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

     class PostViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView subTitle;
        ImageView coverImage;

        PostViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.postTitle);
            //subTitle = (TextView) itemView.findViewById(R.id.postSubtitle);
            coverImage = (ImageView) itemView.findViewById(R.id.postCoverImage);
        }
    }
}
