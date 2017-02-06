package com.jamesdube.laravelnewsapp.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomDateSerializer implements JsonSerializer<Date> {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);

    public JsonElement serialize(Date date, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(dateFormat.format(date));
    }
}