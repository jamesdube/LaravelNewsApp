package com.jamesdube.laravelnewsapp.util;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.jamesdube.laravelnewsapp.App;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rick on 1/7/17.
 */

public class VolleyErrorHelper {

    public static final String ERR_SERVER_DOWN= "Server Down";
    public static final String ERR_NO_INTERNET= "No Internet Connection";
    public static final String ERR_GENERIC= "General Error";
    public static final String ERR_UNKNOWN_STATUS_CODE= "Unknown Status Code";
    /**
     * Returns appropriate message which is to be displayed to the user
     * against the specified error object.
     *
     * @param error
     * @return
     */
    public static String getMessage(Object error) {

        if (error instanceof TimeoutError) {
            return ERR_SERVER_DOWN;
        }
        else if (isServerProblem(error)) {
            return handleServerError(error, App.getAppContext());
        }
        else if (isNetworkProblem(error)) {
            return ERR_NO_INTERNET;
        }
        return ERR_GENERIC;
    }

    /**
     * Determines whether the error is related to network
     * @param error
     * @return
     */
    private static boolean isNetworkProblem(Object error) {
        return (error instanceof NetworkError) || (error instanceof NoConnectionError);
    }
    /**
     * Determines whether the error is related to server
     * @param error
     * @return
     */
    private static boolean isServerProblem(Object error) {
        return (error instanceof ServerError) || (error instanceof AuthFailureError);
    }
    /**
     * Handles the server error, tries to determine whether to show a stock message or to
     * show a message retrieved from the server.
     *
     * @param err Volley error
     * @param context Context
     * @return String
     */
    private static String handleServerError(Object err, Context context) {
        VolleyError error = (VolleyError) err;

        NetworkResponse response = error.networkResponse;

        if (response != null) {
            switch (response.statusCode) {
                case 404:
                case 422:
                case 400:
                case 401:

                    try {
                        String string = new String(error.networkResponse.data);
                        JSONObject object = new JSONObject(string);
                        if (object.has("message")) {
                            return object.get("message").toString();
                        }
                        else if(object.has("error_description")) {
                            return object.get("error_description").toString();
                        }
                    }catch (JSONException e)
                    {
                        return "Could not parse response";
                    }
                    // invalid request
                    return error.getMessage();

                default:
                    return ERR_UNKNOWN_STATUS_CODE;
            }
        }
        return ERR_GENERIC;
    }

    public static  int getStatusCode(Object err)
    {
        VolleyError error = (VolleyError) err;
        NetworkResponse response = error.networkResponse;
        return response != null ? response.statusCode : 0;
    }
}
