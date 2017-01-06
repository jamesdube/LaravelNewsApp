package com.jamesdube.laravelnewsapp.adapters;

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

import java.util.List;

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
    public void onBindViewHolder(PostViewHolder holder, int position) {
        holder.title.setText(posts.get(position).getTitle());
        holder.subTitle.setText(posts.get(position).getSubTitle());
        //holder.coverImage.setImageUrl(posts.get(position).getCoverImage(),new ImageLoader());
        Glide.with(App.getAppContext()).load(posts.get(position).getCoverImage())
                .into(holder.coverImage);

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
            subTitle = (TextView) itemView.findViewById(R.id.postSubtitle);
            coverImage = (ImageView) itemView.findViewById(R.id.postCoverImage);
        }
    }
}
