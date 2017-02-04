package com.jamesdube.laravelnewsapp.util;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.jamesdube.laravelnewsapp.App;
import com.jamesdube.laravelnewsapp.models.Author;
import com.jamesdube.laravelnewsapp.models.Category;
import com.jamesdube.laravelnewsapp.models.Post;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.realm.RealmList;

import static com.jamesdube.laravelnewsapp.util.Constants.AUTHOR;
import static com.jamesdube.laravelnewsapp.util.Constants.CATEGORY;
import static com.jamesdube.laravelnewsapp.util.Constants.COVER_IMAGE;
import static com.jamesdube.laravelnewsapp.util.Constants.DESCRIPTION;
import static com.jamesdube.laravelnewsapp.util.Constants.LINK;
import static com.jamesdube.laravelnewsapp.util.Constants.TITLE;
import static com.jamesdube.laravelnewsapp.util.CustomDateSerializer.dateFormat;


public class PostDeserializer implements JsonDeserializer<Post> {

    private Date pubDate = null;
    private JsonElement json;
    private String title;
    private String link;
    private String description;
    private String coverImage;
    private RealmList<Author> authors = new RealmList<>();
    private RealmList<Category> categories = new RealmList<>();




    @Override
    public Post deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        this.json = json;
        Post post = new Post();
        authors = new RealmList<>();
        categories = new RealmList<>();



        //if there is one category
        parseCategory().parseAuthor()
                .parseDate()
                .parseTitle()
                .parseLink()
                .parseDescription()
                .parseCoverImage();

        post.setPubDate(pubDate);
        post.setTitle(title);
        post.setLink(link);
        post.setCategories(categories);
        post.setAuthors(authors);
        post.setCoverImage(coverImage);
        post.setDescription(prepHtml());

        post.setActive(true);
        post.setSeen(false);
        

        return post;


    }

    private PostDeserializer parseCategory() {

        if(json.getAsJsonObject().get(CATEGORY).isJsonPrimitive()){
            String name = json.getAsJsonObject().get(CATEGORY).getAsString();
            categories.add(new Category(name.trim()));
        }else if(json.getAsJsonObject().get(CATEGORY).isJsonArray()){
            for(int i = 0; i < json.getAsJsonObject().get(CATEGORY).getAsJsonArray().size(); i ++){
                JsonObject categoryObject = json.getAsJsonObject()
                        .get(CATEGORY)
                        .getAsJsonArray()
                        .get(i)
                        .getAsJsonObject();
                categories.add(new Category(categoryObject.get("content").getAsString().trim()));
            }
        }

        return this;
    }

    private PostDeserializer parseAuthor() {
        if(json.getAsJsonObject().get(AUTHOR).isJsonPrimitive()){
            String name = json.getAsJsonObject().get(AUTHOR).getAsString();
            authors.add(new Author(name.trim()));
        }else if(json.getAsJsonObject().get(AUTHOR).isJsonArray()){
            for(int i = 0; i < json.getAsJsonObject().get(AUTHOR).getAsJsonArray().size(); i ++){
                JsonObject authorObject = json.getAsJsonObject()
                        .get(AUTHOR)
                        .getAsJsonArray()
                        .get(i)
                        .getAsJsonObject();
                authors.add(new Author(authorObject.get("name").getAsString().trim()));
            }
        }

        return this;
    }

    private PostDeserializer parseDate() {
        try {
            pubDate = dateFormat.parse(json
                    .getAsJsonObject()
                    .get("pubDate")
                    .getAsString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return this;
    }

    private PostDeserializer parseLink() {

        if (json.getAsJsonObject().get(LINK).isJsonPrimitive()) {
            link = json.getAsJsonObject().get(LINK).getAsString();
        }

        return this;
    }

    private PostDeserializer parseTitle() {

        if (json.getAsJsonObject().get(TITLE).isJsonPrimitive()) {
            title = json.getAsJsonObject().get(TITLE).getAsString();
        }

        return this;
    }

    private PostDeserializer parseDescription() {

        if (json.getAsJsonObject().get(DESCRIPTION).isJsonPrimitive()) {
            description = json.getAsJsonObject().get(DESCRIPTION).getAsString();
        }

        return this;
    }
    
    private PostDeserializer parseCoverImage(){

        JsonElement coverImageElement = json.getAsJsonObject().get(COVER_IMAGE);

        if(coverImageElement != null){
            if (coverImageElement.isJsonPrimitive()) {
                coverImage = coverImageElement.getAsString();
            }
        }
        else if(description != null){
            Pattern pattern = Pattern.compile("<img src=\"(.*?)\">");
            Matcher matcher = pattern.matcher(description);
            if(matcher.find()){
                coverImage = matcher.group(1);
            }
        }

        return this;
        
    }

    private String prepHtml(){
        return "<HTML><HEAD><link href=\"style.css\" type=\"text/css\" rel=\"stylesheet\"/></HEAD><body>" +
                "<div class=\"container-fluid\"><div class=\"row\"> <div class=\"col-lg-12\">" +
                description.replaceFirst("<img([^<]*)>", "") +
                " </div></div></div></body></HTML>";
    }
}
