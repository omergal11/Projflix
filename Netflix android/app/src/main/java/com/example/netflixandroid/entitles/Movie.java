package com.example.netflixandroid.entitles;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.io.File;
import java.util.List;

@Entity(tableName = "movie_table")
@TypeConverters(StringListConverter.class)
public class Movie {

    @PrimaryKey(autoGenerate = true)  // Room handles generating unique internal ID for the movie
    private int id;  // Internal Room ID

    private String mongoDbId;

    private String name;

    private String minutes;

    private String description;

    private int releaseYear;

    private String director;

    private String movieFile;

    private String trailer;

    private String mainImage;
    private List<String> categories;
    private List<String> viewedBy;
    private List<String> cast;

    // Constructor
    public Movie(String name, String minutes, String description, int releaseYear, List<String> categories, List<String> cast,String director) {
        this.name = name;
        this.minutes = minutes;
        this.description = description;
        this.releaseYear = releaseYear;
        this.categories = categories;
        this.cast = cast;
        this.director = director;
    }
    public Movie(){}
    // Getters and Setters
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

    public String getMovieFile() {
        return movieFile;
    }

    public void setMovieFile(String movieFile) {
        this.movieFile = movieFile;
    }
    public String getMongoDbId() {
        return mongoDbId;
    }

    public void setMongoDbId(String id) {
        this.mongoDbId = id;
    }

    public String getMinutes() {
        return minutes;
    }

    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }


    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<String> getViewedBy() {
        return viewedBy;
    }

    public void setViewedBy(List<String> viewedBy) {
        this.viewedBy = viewedBy;
    }
    public List<String> getCast(){
        return cast;
    }
    public void setCast(List<String> cast) {
        this.cast = cast;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", mongoDbId='" + mongoDbId + '\'' +
                ", name='" + name + '\'' +
                ", minutes='" + minutes + '\'' +
                ", description='" + description + '\'' +
                ", releaseYear=" + releaseYear +
                ", director='" + director + '\'' +
                ", movieFile='" + movieFile + '\'' +
                ", trailer='" + trailer + '\'' +
                ", mainImage='" + mainImage + '\'' +
                ", categories=" + categories +
                ", viewedBy=" + viewedBy +
                ", cast=" + cast +
                '}';
    }


}


