package com.example.netflixandroid.viewmodels;

import android.app.Application;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.example.netflixandroid.entitles.Movie;
import com.example.netflixandroid.repositories.MovieRepository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MovieViewModel extends AndroidViewModel {

    private MovieRepository movieRepository; //repository to handle the data of the movie
    private MutableLiveData<String> statusMessage; //message to show the status of the operation
    private MutableLiveData<List<Movie>> movies; //list of the movies

    private MutableLiveData<Movie> featuredMovie; //featured movie

    private MutableLiveData<List<Pair<String,List<Movie>>>> moviesInPromotedCategory; //list of the promoted movies

    public  MovieViewModel(@NonNull Application application) {
        super(application);
        statusMessage = new MutableLiveData<>();
        movies = new MutableLiveData<>();
        featuredMovie = new MutableLiveData<>();
        moviesInPromotedCategory = new MutableLiveData<>();
        movieRepository = new MovieRepository(movies,statusMessage,moviesInPromotedCategory,featuredMovie);
    }


    public LiveData<List<Movie>> getMovies() {
        return movies;
    }
    public LiveData<String> getStatusMessage() {
        return statusMessage;
    }

    public void createMovie(Movie movie, File mainImage,File movieFile, File trailer) {
     movieRepository.createMovie(movie,mainImage,movieFile,trailer);
    }

    public void updateMovie(Movie oldMovie, String name, String minutes, String description,
                            int releaseYear, List<String> categories,List<String> cast,
                            String director,File mainImage,File trailer,File movieFile) {
        movieRepository.updateMovie(oldMovie,name,minutes,description,
                releaseYear,categories,cast,director,mainImage,trailer,movieFile);
    }

    public void deleteMovie(Movie movie) {
        movieRepository.deleteMovie(movie);
    }
    public void searchMovies(String queary){
        movieRepository.searchMovies(queary);
    }
    public void uploadPromotedMovies(){
        movieRepository.uploadPromotedMovies();
    }

    public LiveData<Movie> getFeaturedMovie() {return featuredMovie; }
    public LiveData<List<Pair<String,List<Movie>>>> getPromotedMovies(){
        return moviesInPromotedCategory;
    }
    public void uploadMovies() {
        movieRepository.loadMoviesFromLocal();
    }

    public void searchMoviesByCategory(String selectedCategory) {
        movieRepository.searchMoviesByCategory(selectedCategory);
    }
    public  void uploadRecommendMovies(String movieId){
        movieRepository.getSimilarMovies(movieId);
    }

    public void setFeaturedMovie(String movieId) {
        movieRepository.setFeaturedMovie(movieId);
    }

    public void watchThisMovie(String mongoDbId) {
        movieRepository.watchThisMovie(mongoDbId);
    }

    public void clearLivedataMovies(){
        movies.postValue(new ArrayList<>());
    }
}
