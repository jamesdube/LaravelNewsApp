package com.jamesdube.laravelnewsapp.http;

import com.jamesdube.laravelnewsapp.models.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rick on 1/6/17.
 */

public class Client {

    public static List<Post> getPosts(requestCallback requestCallback){
        return new ArrayList<>();
    }

    public interface requestCallback{
        void onSuccess();
        void onFailure();
    }
}
