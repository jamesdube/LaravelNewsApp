package com.jamesdube.laravelnewsapp.models;

import com.jamesdube.laravelnewsapp.App;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Post extends RealmObject{

    //Post Title
    private String title;

    //Short Description about the post
    private String subTitle;

    //Post Cover Image
    private String coverImage;

    //The URL link
    @PrimaryKey
    private String link;

    //The Author of the Post
    private String creator;

    //The Description of the Post
    private String description;

    //The Published Date
    private String pubDate;

    //Article was read
    private Boolean wasRead;




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

    public Post() {
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
        return App.Gson().toJson(App.Realm().copyFromRealm(this));
    }

    public static Post fromJson(String json) {
        return App.Gson().fromJson(json, Post.class);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public void extractImageUrl() {
        Pattern pattern = Pattern.compile("<img src=\"(.*?)\">");
        Matcher matcher = pattern.matcher(getDescription());
        if(matcher.find()){
            setCoverImage (matcher.group(1));
        }
    }

    public Boolean getWasRead() {
        return wasRead;
    }

    public void setWasRead(Boolean wasRead) {
        this.wasRead = wasRead;
    }
}
