package com.jamesdube.laravelnewsapp.util;

import com.jamesdube.laravelnewsapp.App;
import com.jamesdube.laravelnewsapp.models.Post;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;

/**
 * Created by rick on 1/7/17.
 */

public class VolleyResponseHelper {
    private String response;
    public VolleyResponseHelper(String response){
        this.response = response;
    }
    public  List<Post> getPosts(){
        //Parse to POJO
        List<Post> postsList = new ArrayList<>();
        XmlToJson parser = new XmlToJson.Builder(this.response).build();
        JSONObject rssObject = parser.toJson();
        try {
            assert rssObject != null;
            //Get the Posts as an array
            JSONArray posts = rssObject.getJSONObject("rss")
                    .getJSONObject("channel")
                    .getJSONArray("item");
            for(int i = 0; i < posts.length() ; i++){
                postsList.add(App.Gson().fromJson(posts.getJSONObject(i).toString(),Post.class));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
            return postsList;
    }
}
