package com.jamesdube.laravelnewsapp.models;

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
                List<Post> newPosts = filterNewPosts(posts);
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
     * Get All the Posts that have not been marked as read.
     * @return RealmResults<Post>
     */
    public static RealmResults<Post> getUnreadPosts(){
        return App.Realm()
                .where(Post.class)
                .equalTo("wasRead",false)
                .or()
                .isNull("wasRead")
                .findAll();
    }

    /**
     * Remove existing posts from list
     * @param posts
     */
    public static List<Post> filterNewPosts(List<Post> posts){
        List<Post> newPosts = new ArrayList<>();
        for(Post post : posts ){
            if(!postExists(post.getLink())){
                newPosts.add(post);
                System.out.println("xxxx postExists false -> " + post.getTitle());
            }else {
                System.out.println("xxxx postExists true ->  " + post.getTitle());
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
    public static boolean postExists(String link){
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
                post.setWasRead(true);
            }
        });
    }
}
