package com.jamesdube.laravelnewsapp.http.requests;

import java.util.Map;

/**
 * Created by rick on 1/7/17.
 */

public class requestPayload {

    private String url;
    private Map<String,?> parameters;
    private int Method;
    private Object tag;

    public requestPayload() {
    }

    public requestPayload(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, ?> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, ?> parameters) {
        this.parameters = parameters;
    }

    public int getMethod() {
        return Method;
    }

    public void setMethod(int method) {
        Method = method;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public static void request(){

    }
}
