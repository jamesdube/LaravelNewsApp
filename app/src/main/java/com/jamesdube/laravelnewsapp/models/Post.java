package com.jamesdube.laravelnewsapp.models;

import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.jamesdube.laravelnewsapp.App;

/**
 * Created by rick on 1/6/17.
 */

public class Post {

    //Post Title
    private String title;

    //Short Description about the post
    private String subTitle;

    //Post Cover Image
    private String coverImage;

    //The URL link
    private String link;

    //The Author of the Post
    private String creator;


    /**
     * Post constructor
     * @param title Post Title
     * @param subTitle Short Description about the post
     * @param coverImage Post Cover Image
     */
    public Post(String title, String subTitle, String coverImage) {
        this.title = title;
        this.subTitle = subTitle;
        this.coverImage = coverImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String toJson() {
        return App.Gson().toJson(this);
    }

    public static Post fromJson(String json) {
        return App.Gson().fromJson(json, Post.class);
    }
}
