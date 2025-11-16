package com.example.netflixandroid.entitles;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
//class to convert a list of strings to a string and vice versa
public class StringListConverter {
    @TypeConverter
    public static String fromStringList(List<String> value) {
        if (value == null) {
            return null;
        }
        Gson gson = new Gson();
        return gson.toJson(value);
    }

    @TypeConverter
    public static List<String> toStringList(String value) {
        if (value == null) {
            return null;
        }
        Gson gson = new Gson();
        Type listType = new TypeToken<List<String>>() {}.getType();
        return gson.fromJson(value, listType);
    }
    public static String fromStringListWithComma(List<String> value) {
        if (value == null || value.isEmpty()) {
            return "";
        }
        return String.join(",", value);
    }
}
