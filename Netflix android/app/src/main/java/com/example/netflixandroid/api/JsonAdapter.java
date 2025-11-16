package com.example.netflixandroid.api;

import android.util.Pair;

import com.example.netflixandroid.entitles.Movie;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
//class to adapt json objects to movie objects
public class JsonAdapter {
    //convert a json object to a movie object
    public static Movie jsonObjectToMovie(JsonObject movieObject){
        Movie movie = new Movie();
        movie.setMongoDbId(movieObject.get("_id").getAsString());
        movie.setName(movieObject.get("name").getAsString());
        movie.setMinutes(movieObject.get("minutes").getAsString());
        movie.setDescription(movieObject.get("description").getAsString());
        movie.setDirector(movieObject.get("director").getAsString());
        movie.setReleaseYear(movieObject.get("releaseYear").getAsInt());
        JsonArray castArrayJson = movieObject.get("cast").getAsJsonArray();
        JsonArray categoriesArray = movieObject.getAsJsonArray("categories");
        movie.setTrailer(movieObject.get("trailer").getAsString());
        movie.setMovieFile(movieObject.get("movieFile").getAsString());
        movie.setMainImage(movieObject.get("mainImage").getAsString());

        //convert the cast array to a list of strings
        List<String> cast = new ArrayList<>();
        for (JsonElement actorElement : castArrayJson) {
            cast.add(actorElement.getAsString());
        }
        movie.setCast(cast);
        List<String> categoriesId = new ArrayList<>();
        //convert the categories array to a list of strings
        for (JsonElement categoryElement : categoriesArray) {
            JsonObject categoryObject = categoryElement.getAsJsonObject();
            categoriesId.add(categoryObject.get("_id").getAsString());
        }
        movie.setCategories(categoriesId);
        return movie;
    }
    //convert a json array to a list of movies
    public static List<Movie> jsonArrayToMovieList(JsonArray moviesArray){
        List<Movie> movies = new ArrayList<>();
        for (JsonElement movieElement : moviesArray) {
            JsonObject movieObject = movieElement.getAsJsonObject();
            Movie movie = jsonObjectToMovie(movieObject);
            movies.add(movie);
        }
        return movies;
    }
    //convert a json array to a list of pairs of strings and lists of movies
    public  static List<Pair<String,List<Movie>>> jsonArrayToPairList(JsonArray moviesPerCategoryArray){
        List<Pair<String,List<Movie>>> moviesInPromotedCategory = new ArrayList<>();
        for (JsonElement categoryElement : moviesPerCategoryArray) {
            //check if the element is a json object
            if (!categoryElement.isJsonArray()) {
                String category;
                List<Movie> moviesInCategory = new ArrayList<>();

                JsonObject categoryObject = categoryElement.getAsJsonObject();
                category = categoryObject.get("category").getAsString();

                //convert the movies array to a list of movies
                JsonArray moviesJsonArray = categoryObject.get("movies").getAsJsonArray();
                List<Movie> movies = JsonAdapter.jsonArrayToMovieList(moviesJsonArray);

                //add the movies to the list
                moviesInCategory.addAll(movies);

                //add the category and the movies to the list
                if (!moviesInCategory.isEmpty()) {
                    moviesInPromotedCategory.add(new Pair<>(category, moviesInCategory));
                }
            }
        }
        return moviesInPromotedCategory;
    }


}
