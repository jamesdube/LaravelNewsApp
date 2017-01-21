package com.jamesdube.laravelnewsapp.http;

import com.jamesdube.laravelnewsapp.App;
import com.jamesdube.laravelnewsapp.http.requests.onGetPosts;
import com.jamesdube.laravelnewsapp.models.Post;

import java.util.Iterator;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;

/**
 * Created by rick on 1/17/17.
 *
 */

public class FeedManager {
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
     * Fetch the posts from the rss feed
     * @param callback Interface
     */
    public static void fetchPosts(final onGetPosts callback){
        Client.getPosts(new onGetPosts() {
            @Override
            public void onSuccess(final List<Post> posts) {
                //save the posts to realm
                App.Realm().executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.copyToRealm(posts);
                        callback.onSuccess(posts);
                    }
                });
            }

            @Override
            public void onFailure() {
                callback.onFailure();
            }
        });
    }

    public static void savePosts(){

    }

    public static List<Post> filterExistingPosts(List<Post> posts){
            for(Iterator< Post > it = posts.iterator(); it.hasNext() ; ){
            if(postExists(it.next().getLink())){
                it.remove();
            }
        }
        return posts;
    }

    public static boolean postExists(String link){

        RealmQuery<Post> query = App.Realm().where(Post.class)
                .equalTo("link", link);

        return query.count() != 0;
    }


}

