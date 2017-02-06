package com.jamesdube.laravelnewsapp.models;

import io.realm.RealmObject;

/**
 * Created by jdube on 2/3/17.
 */

public class Author extends RealmObject {

    public Author(){

    }

    String name;

    public Author(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
