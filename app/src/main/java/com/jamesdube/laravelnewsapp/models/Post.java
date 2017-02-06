package com.jamesdube.laravelnewsapp.models;

import com.google.gson.annotations.SerializedName;
import com.jamesdube.laravelnewsapp.App;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Post extends RealmObject{

    @SerializedName("link")
    @PrimaryKey
    private String link;

    @SerializedName("dc:creator")
    private RealmList<Author> authors;

    @SerializedName("title")
    private String title;

    @SerializedName("pubDate")
    private Date pubDate;

    @SerializedName("category")
    private RealmList<Category> categories;

    //Post Cover Image
    private String coverImage;

    //The Description of the Post
    private String description;

    //Article archived or not
    private Boolean active;

    private Boolean seen;

    //
    private Boolean favourite;

    /**
     * Post constructor
     * @param title Post Title
     * @param link Short Description about the post
     * @param coverImage Post Cover Image
     */
    public Post(String title, String link, String coverImage) {
        this.title = title;
        this.link = link;
        this.coverImage = coverImage;
    }

    /**
     * Default Constructor
     */
    public Post() {
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public RealmList<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(RealmList<Author> authors) {
        this.authors = authors;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    public RealmList<Category> getCategories() {
        return categories;
    }

    public void setCategories(RealmList<Category> categories) {
        this.categories = categories;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }

    public String toJson() {
        return App.Gson().toJson(App.Realm().copyFromRealm(this));
    }

    public static Post fromJson(String post) {
        return App.Gson().fromJson(post,Post.class);
    }

    public Boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(Boolean favourite) {
        this.favourite = favourite;
    }
}
