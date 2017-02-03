package com.jamesdube.laravelnewsapp.models;

import android.util.Log;

import com.android.volley.VolleyError;
import com.jamesdube.laravelnewsapp.App;
import com.jamesdube.laravelnewsapp.http.Client;
import com.jamesdube.laravelnewsapp.http.requests.onGetPosts;
import com.jamesdube.laravelnewsapp.http.requests.onSavePosts;
import com.jamesdube.laravelnewsapp.util.Notify;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by rick on 1/22/17.
 */

public class PostRepository {

    /**
     * Fetch any new posts and save them to the db
     * @param onSavePosts Callback called when the request is finished
     */
    public static void fetch(final onSavePosts onSavePosts) {

        Client.getPosts(new onGetPosts() {
            @Override
            public void onSuccess(List<Post> posts) {
                List<Post> newPosts = filterNew(posts);
                //save new posts
                if(newPosts.size() > 0){
                    save(newPosts,onSavePosts);
                }else{
                    onSavePosts.onSaved();
                }

            }

            @Override
            public void onFailure(VolleyError error) {
                onSavePosts.onError();
            }
        });

    }

    /**
     * Get All the Posts that have not been archived.
     * @return RealmResults<Post>
     */
    public static RealmResults<Post> getActive(){
        return App.Realm()
                .where(Post.class)
                .equalTo("active",true)
                .or()
                .isNull("active")
                .findAllSorted("pubDate", Sort.DESCENDING);
    }

    /**
     * Remove existing posts from list
     * @param posts
     */
    public static List<Post> filterNew(List<Post> posts){
        List<Post> newPosts = new ArrayList<>();
        for(Post post : posts ){
            System.out.println("xxxx filter -> cat(" + String.valueOf(post.getCategories().size()) +") authors(" + String.valueOf(post.getAuthors().size())+ ") "+ ") link(" +  post.getLink()+ ") "+  post.getTitle());
            if(!exists(post.getLink())){

                newPosts.add(post);
                //System.out.println("xxxx exists false -> " + post.getTitle());
            }else {
                //System.out.println("xxxx exists true ->  " + post.getTitle());
            }

        }
        return newPosts;
    }

    /**
     * Save the given list of posts
     * @param posts
     */
    public static void save(final List<Post> posts, final onSavePosts onSavePosts){
        App.Realm().executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(posts);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                System.out.println("(" + String.valueOf(posts.size()) + ") post(s) saved successfully");
                //send new posts notification
                Notify.showNewPostNotifications();
                onSavePosts.onSaved();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                System.out.println("There was an error saving the posts");
                onSavePosts.onError();
            }
        });
    }

    /**
     * Check if a post exists or not  in the db
     * @param link
     * @return boolean
     */
    public static boolean exists(String link){
        RealmQuery<Post> query = App.Realm().where(Post.class)
                .equalTo("link", link);
        return query.count() != 0;
    }

    /**
     * Archive the selected post
     */
    public static void archive(final Post post){
        App.Realm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                post.setActive(false);
            }
        });
    }

    /**
     * Get the posts that have not been seen by the user
     * @return
     */
    public static List<Post> getUnSeen() {
        return App.Realm().where(Post.class).equalTo("seen",false)
                .or()
                .isNull("seen")
                .findAllSorted("pubDate",Sort.DESCENDING);
    }

    public static void setPostsAsSeen(){
        App.Realm().executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Log.d(App.Tag, "marking all posts as seen.... ");
                    List<Post> posts = realm.where(Post.class).findAll();
                    for(Post post : posts){
                        post.setSeen(true);
                    }
                }
            },
            new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    Log.d(App.Tag, "marking all posts as seen.... onSuccess");

                }
            },
            new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {
                    Log.d(App.Tag, "marking all posts as seen.... onError => " + error.getMessage());
                }
            }
        );
    }
}
