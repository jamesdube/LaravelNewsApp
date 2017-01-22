package com.jamesdube.laravelnewsapp.http.requests;

import com.android.volley.VolleyError;
import com.jamesdube.laravelnewsapp.models.Post;

import java.util.List;

/**
 * Created by rick on 1/8/17.
 */

public interface onGetPosts extends onRequestCompleted{
    public void onSuccess(List<Post> posts);
    public void onFailure(VolleyError error);
}
