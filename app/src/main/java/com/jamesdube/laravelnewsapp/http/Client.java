package com.jamesdube.laravelnewsapp.http;

import com.jamesdube.laravelnewsapp.http.requests.onGetPosts;
import com.jamesdube.laravelnewsapp.http.requests.rssRequest;
import com.jamesdube.laravelnewsapp.models.Post;
import com.jamesdube.laravelnewsapp.util.request;

import java.util.List;

/**
 * Created by rick on 1/6/17.
 */

public class Client {

    public static void getPosts(onGetPosts callback){
        rssRequest.requestPosts(callback);
    }

}
