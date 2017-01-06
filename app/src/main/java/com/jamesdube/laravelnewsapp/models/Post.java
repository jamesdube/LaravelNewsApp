package com.jamesdube.laravelnewsapp.models;

import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

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
}
