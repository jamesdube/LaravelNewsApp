package com.jamesdube.laravelnewsapp.models;

import com.jamesdube.laravelnewsapp.App;
import com.jamesdube.laravelnewsapp.util.Notify;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;

/**
 * Created by rick on 1/22/17.
 */

public class PostRepository {

    /**
     * Get All the Posts that have not been marked as read.
     * @return RealmResults<Post>
     */
    public static List<Post> getUnreadPosts(){
        return App.Realm().copyToRealm(App.Realm()
                .where(Post.class)
                .equalTo("wasRead",false)
                .or()
                .isNull("wasRead")
                .findAll());
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
    public static void savePosts(final List<Post> posts){
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
                Notify.newPostNotification(posts);
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                System.out.println("There was an error saving the posts");
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
}
