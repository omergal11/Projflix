package com.example.netflixandroid.entitles;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
@Entity(tableName = "category_table")
@TypeConverters(StringListConverter.class)  //Add converter
public class Category {
    @PrimaryKey(autoGenerate = true)
    private int id;  // Internal Room ID

    @SerializedName("_id")
    private String mongoDbId;

    private String name;

    private boolean promoted;

    @SerializedName("movies")
    private List<String> movies;

    // Constructor
    public Category(String name, boolean promoted,List<String> movies ) {
        this.name = name;
        this.promoted = promoted;
        this.movies = movies;
    }

    @Ignore
    public Category() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMongoDbId() {
        return mongoDbId;
    }

    public void setMongoDbId(String mongoDbId) {
        this.mongoDbId = mongoDbId;
    }

    public boolean isPromoted() {
        return promoted;
    }

    public void setPromoted(boolean promoted) {
        this.promoted = promoted;
    }

    public List<String> getMovies() {
        return movies;
    }

    public void setMovies(List<String> movieIds) {
        this.movies = movieIds;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id='" + mongoDbId + '\'' +
                ", name='" + name + '\'' +
                ", promoted=" + promoted +
                ", movieIds=" + movies +
                '}';
    }
}

