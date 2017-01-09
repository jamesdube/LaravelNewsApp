package com.jamesdube.laravelnewsapp.http.requests;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jamesdube.laravelnewsapp.App;
import com.jamesdube.laravelnewsapp.util.VolleyErrorHelper;
import com.jamesdube.laravelnewsapp.util.VolleyResponseHelper;
import com.jamesdube.laravelnewsapp.util.request;

import org.json.JSONException;
import org.json.JSONObject;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;

/**
 * Created by rick on 1/7/17.
 */

public class rssRequest {

    public static void request(String url, final onRequestCompleted callback){
        RequestQueue queue = Volley.newRequestQueue(App.getAppContext());
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Parse to POJO
                        XmlToJson parser = new XmlToJson.Builder(response).build();
                        //Log.d(App.Tag,String.valueOf(parser.toFormattedString()));

                        JSONObject rssObject = parser.toJson();
                        try {
                            assert rssObject != null;
                            //System.out.println(String.valueOf(rssObject.getJSONObject("rss").getJSONObject("channel").getJSONArray("item")));
                            Log.d(App.Tag,String.valueOf(rssObject
                                    .getJSONObject("rss")
                                    .getJSONObject("channel")
                                    .getJSONArray("item")
                                    .getJSONObject(1)
                                    .getString("title")));



                        } catch (JSONException | NullPointerException e) {
                            e.printStackTrace();
                        }

                        /*Gson gson = new Gson();
                        gson.fromJson(parser.toString(),Post.class);
                        callback.onSuccess();*/
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(App.Tag,"error : "+VolleyErrorHelper.getMessage(error));
                    }
                }
        );

        queue.add(stringRequest);
    }
    public static void requestPosts(final onGetPosts callback){
        RequestQueue queue = Volley.newRequestQueue(App.getAppContext());
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                request.URL.LARAVEL_FEED_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //finally return the posts list
                        VolleyResponseHelper responseHelper = new VolleyResponseHelper(response);
                        callback.onSuccess(responseHelper.getPosts());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(App.Tag,"error : "+VolleyErrorHelper.getMessage(error));
                    }
                }
        );

        queue.add(stringRequest);
    }
}
