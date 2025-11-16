package com.example.netflixandroid.repositories;

import android.content.Context;
import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.netflixandroid.NetflixApplication;
import com.example.netflixandroid.api.MovieAPI;
import com.example.netflixandroid.daos.CategoryDao;
import com.example.netflixandroid.daos.MovieDao;
import com.example.netflixandroid.entitles.AppDataBase;
import com.example.netflixandroid.entitles.Category;
import com.example.netflixandroid.entitles.Movie;

import java.io.File;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

//calass to handle the data of the movie
public class MovieRepository {

    private CategoryDao categoryDao;

    private MovieDao movieDao;          
    private MovieAPI movieAPI;          
    private MutableLiveData<List<Movie>> moviesLivedata;

    private MutableLiveData<Movie> featuredMovie;
    private MutableLiveData<String> statusMessage;
    private Context context;

    //constructor
    public MovieRepository(MutableLiveData<List<Movie>> movies,MutableLiveData<String> statusMessage,
                           MutableLiveData<List<Pair<String,List<Movie>>>> moviesInPromotedCategory,MutableLiveData<Movie>featuredMovie) {
        this.context = NetflixApplication.getInstance().getApplicationContext();
        AppDataBase db = AppDataBase.getInstance(context);  //get the database instance
        movieDao = db.movieDao();
        categoryDao = db.categoryDao();
        this.featuredMovie = featuredMovie;
        movieAPI = new MovieAPI(movies,statusMessage,categoryDao,movieDao,moviesInPromotedCategory,featuredMovie);
        this.statusMessage =statusMessage;
        this.moviesLivedata = movies;
        loadMoviesFromLocal();
    }
    public LiveData<List<Movie>> getMovies() {
        return moviesLivedata;
    }

    public void loadMoviesFromLocal() {
        new Thread(() -> {
            List<Movie> movieslocal = movieDao.getAllMovies();
            moviesLivedata.postValue(movieslocal); //post the movies to the live data
        }).start();
    }
    public void createMovie(Movie movie, File mainImage,File movieFile,File trailer ) {
        movieAPI.createMovie(movie , mainImage, movieFile,trailer);
    }
    public void updateMovie(Movie movie,String name, String minutes, String description,
                            int releaseYear, List<String> categories,List<String> cast,
                            String director,File mainImage,File trailer,File movieFile) {
        movieAPI.updateMovie(movie,name,minutes,description,
                releaseYear,categories,director,cast,mainImage,trailer,movieFile);
    }
    public void deleteMovie(Movie movie) {
        movieAPI.deleteMovie(movie);
    }
    public void getMovieDetails(String movieID) {
        movieAPI.getMovieById(movieID);
    }

    public void loadMoviesFromAPI() {
        movieAPI.getMovies();
    }
    public void searchMovies(String query){
        movieAPI.getSearchedMovies(query);
    }

    public void getSimilarMovies(String movieId) {
        movieAPI.getSimilarMovies(movieId);
    }
    public void uploadPromotedMovies(){
        movieAPI.getMovies();
    }

    public void searchMoviesByCategory(String selectedCategory) {
        Executor executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            Category category = categoryDao.getCategoryByName(selectedCategory);
            movieAPI.getMoviesByCategory(category);
        });

    }
    public void setFeaturedMovie(String movieId) {
        new Thread(() -> {
            Movie movie = movieDao.getMovieByMongoDbId(movieId);
            if (movie == null) {
                movieAPI.getMovieById(movieId);
            }
            else {
                featuredMovie.postValue(movie);
            }
        }).start();
    }
    public void watchThisMovie(String mongoDbId) {
        movieAPI.watchThisMovie(mongoDbId);
    }
}

