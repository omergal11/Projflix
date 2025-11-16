package com.example.netflixandroid.api;

import android.util.Pair;

import androidx.lifecycle.MutableLiveData;

import com.example.netflixandroid.NetflixApplication;
import com.example.netflixandroid.PreferencesManager;
import com.example.netflixandroid.daos.CategoryDao;
import com.example.netflixandroid.daos.MovieDao;
import com.example.netflixandroid.entitles.Category;
import com.example.netflixandroid.entitles.Movie;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//class to manage api calls for movies
public class MovieAPI {

    private final MovieDao movieDao;
    private final CategoryDao categoryDao;
    private Retrofit retrofit;
    private WebServiceAPI webServiceAPI;
    private  MutableLiveData<String> statusMessage;
    private MutableLiveData<List<Movie>> movieListData;

    private MutableLiveData<List<Pair<String,List<Movie>>>> moviesInPromotedCategoryLiveData;
    private MutableLiveData<Movie> featuredMovie;

    //constructor
    public MovieAPI(MutableLiveData<List<Movie>> movies, MutableLiveData<String> statusMessage,CategoryDao categoryDao,
                    MovieDao movieDao, MutableLiveData<List<Pair<String,List<Movie>>>> moviesInPromotedCategory, MutableLiveData<Movie>featuredMovie) {
        this.movieDao = movieDao;
        this.categoryDao = categoryDao;
        this.movieListData = movies;
        this.statusMessage = statusMessage;
        this.featuredMovie = featuredMovie;
        this.moviesInPromotedCategoryLiveData = moviesInPromotedCategory;
        String baseUrl = NetflixApplication.getBaseUrl() + "api/";
        //init retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }
    //create movie
    public void createMovie(Movie movie, File mainImage, File movieFile, File trailer ) {
        String token = PreferencesManager.getToken();
        //create request body
        RequestBody nameRequestBody = RequestBody.create(MediaType.parse("text/plain"), movie.getName());
        RequestBody minutesRequestBody = RequestBody.create(MediaType.parse("text/plain"), movie.getMinutes());
        RequestBody descriptionRequestBody = RequestBody.create(MediaType.parse("text/plain"), movie.getDescription());
        RequestBody releaseYearRequestBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(movie.getReleaseYear()));
        String categoriesJson = new Gson().toJson(movie.getCategories());
        RequestBody categories = RequestBody.create(MediaType.parse("text/plain"), categoriesJson);
        String castJson = new Gson().toJson(movie.getCast()); 
        RequestBody cast = RequestBody.create(MediaType.parse("text/plain"), castJson);
        RequestBody directorRequestBody = RequestBody.create(MediaType.parse("text/plain"), movie.getDirector());

        MultipartBody.Part mainImagePart = null;
        if (mainImage != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), mainImage);
            mainImagePart = MultipartBody.Part.createFormData("mainImage", mainImage.getName(), requestFile);
        }

        MultipartBody.Part trailerPart = null;
        if (trailer != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("video/mp4"), trailer);
            trailerPart = MultipartBody.Part.createFormData("trailer", trailer.getName(), requestFile);
        }

        MultipartBody.Part movieFilePart = null;
        if (movieFile != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("video/mp4"), movieFile);
            movieFilePart = MultipartBody.Part.createFormData("movieFile", movieFile.getName(), requestFile);
        }
        //call to server to create movie
        Call<Void> call = webServiceAPI.createMovie(nameRequestBody,minutesRequestBody,descriptionRequestBody,releaseYearRequestBody,
                categories,directorRequestBody,cast,mainImagePart,trailerPart,movieFilePart,token);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    String locationHeader = response.headers().get("Location");
                    if (locationHeader != null) {
                        String mongoDbId = locationHeader.substring(locationHeader.lastIndexOf("/") + 1);
                        movie.setMongoDbId(mongoDbId);
                        movie.setMovieFile("uploads/movies/"+mongoDbId+".mp4");
                        movie.setMainImage("uploads/moviesMainImages/"+mongoDbId+".jpg");
                        movie.setTrailer("uploads/trailers/"+mongoDbId+".mp4");
                    }
                    new Thread(() -> {
                        //insert movie to room
                        insertMovieIfNotExist(movie);
                        movieListData.postValue(movieDao.getAllMovies());
                        statusMessage.postValue("movie created");
                    }).start();
                }
                else {
                    //call function to get error message
                    try {
                    String errorBody = response.errorBody().string();
                    JSONObject errorJson = new JSONObject(errorBody);
                    String errorMessage = errorJson.optString("error", "Unknown error");
                    statusMessage.postValue("Error: " + errorMessage);
                } catch (IOException | JSONException e) {
                    statusMessage.postValue("Error");
                }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                statusMessage.postValue("Request failed");
            }
        });
    }
    //delete movie
    public void deleteMovie(Movie movie) {
        String token = PreferencesManager.getToken();
        //call to server to delete movie
        Call<Void> call = webServiceAPI.deleteMovie(movie.getMongoDbId(),token);
        call.enqueue(new Callback<Void>() {
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    new Thread(() -> {
                        movieDao.delete(movie);
                        movieListData.postValue(movieDao.getAllMovies());
                        statusMessage.postValue("movie deleted");
                    }).start();
                }
                else {
                    try {
                        //call function to get error message
                        String errorBody = response.errorBody().string();
                        JSONObject errorJson = new JSONObject(errorBody);
                        String errorMessage = errorJson.optString("error", "Unknown error");
                        statusMessage.postValue("Error: " + errorMessage);
                    } catch (IOException | JSONException e) {
                        statusMessage.postValue("Error");
                    }
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                statusMessage.postValue("Request failed");
            }
        });
    }

    //update movie
    public void updateMovie(Movie movie,String name, String minutes, String description, int releaseYear,List<String> categories,
                            String director,List<String> cast,File mainImage,File trailer,File movieFile) {
        //create request body
        String token = PreferencesManager.getToken();
        RequestBody nameRequestBody = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody minutesRequestBody = RequestBody.create(MediaType.parse("text/plain"), minutes);
        RequestBody descriptionRequestBody = RequestBody.create(MediaType.parse("text/plain"), description);
        RequestBody releaseYearRequestBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(releaseYear));
        String categoriesJson = new Gson().toJson(categories); 
        RequestBody categoriesRequestBody = RequestBody.create(MediaType.parse("application/json"), categoriesJson);
        String castJson = new Gson().toJson(cast);
        RequestBody castRequestBody = RequestBody.create(MediaType.parse("application/json"), castJson);
        RequestBody directorRequestBody = RequestBody.create(MediaType.parse("text/plain"), description);

        MultipartBody.Part mainImagePart = null;
        if (mainImage != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), mainImage);
            mainImagePart = MultipartBody.Part.createFormData("mainImage", mainImage.getName(), requestFile);
        }

        MultipartBody.Part trailerPart = null;
        if (trailer != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("video/*"), trailer);
            trailerPart = MultipartBody.Part.createFormData("trailer", trailer.getName(), requestFile);
        }

        MultipartBody.Part movieFilePart = null;
        if (movieFile != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("video/*"), movieFile);
            movieFilePart = MultipartBody.Part.createFormData("movieFile", movieFile.getName(), requestFile);
        }
        //call to server to update movie
        Call<Movie> call = webServiceAPI.changeMovie(movie.getMongoDbId(),nameRequestBody,minutesRequestBody,descriptionRequestBody,releaseYearRequestBody
                ,categoriesRequestBody,directorRequestBody,castRequestBody,mainImagePart,trailerPart,movieFilePart,token);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.isSuccessful()) {
                    new Thread(() -> {
                        //update movie in room
                        movie.setName(name);
                        movie.setDescription(description);
                        movie.setMinutes(minutes);
                        movie.setReleaseYear(releaseYear);
                        movie.setDirector(director);
                        movie.setCategories(categories);
                        movie.setCast(cast);
                        movieDao.update(movie);
                        movieListData.postValue(movieDao.getAllMovies());
                    }).start();
                }
                else {
                        try {
                            //call function to get error message
                            String errorBody = response.errorBody().string();
                            JSONObject errorJson = new JSONObject(errorBody);
                            String errorMessage = errorJson.optString("error", "Unknown error");
                            statusMessage.postValue("Error: " + errorMessage);
                        } catch (IOException | JSONException e) {
                            statusMessage.postValue("Error");
                        }
                }
            }
            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                statusMessage.postValue("Request failed");
            }
        });
    }
    //get all movies
    public void getMovies() {
        String token = PreferencesManager.getToken();
        //call to server to get movies
        Call<JsonArray> call = webServiceAPI.getMovies(token);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    JsonArray moviesPerCategoryArray = response.body();
                    List<Pair<String, List<Movie>>> moviesInPromotedCategory = JsonAdapter.jsonArrayToPairList(moviesPerCategoryArray);
                    List<Movie> allPromotedMovies = new ArrayList<>();
                    for (Pair<String, List<Movie>> categoryPair : moviesInPromotedCategory) {
                        allPromotedMovies.addAll(categoryPair.second);
                    }
                    if (!allPromotedMovies.isEmpty()) {
                        //choose random movie
                        Random random = new Random();
                        int randomIndex = random.nextInt(allPromotedMovies.size()); 
                        Movie randomMovie = allPromotedMovies.get(randomIndex);

                        //update feature movie live data
                        featuredMovie.setValue(randomMovie);
                        for (Movie movie : allPromotedMovies){
                            List<String> categoriesName = new ArrayList<>();
                            for (String categoryId : movie.getCategories()){
                                new Thread(() -> {
                                    Category category = categoryDao.getCategoryById(categoryId);
                                    categoriesName.add(category.getName());
                                }).start();
                            }
                            movie.setCategories(categoriesName);
                        }
                    }
                    //update live data
                    moviesInPromotedCategoryLiveData.postValue(moviesInPromotedCategory);

                    new Thread(() -> {
                        //insert movies to room
                        for (Movie movie : allPromotedMovies ){
                            insertMovieIfNotExist(movie);
                        }
                        movieListData.postValue(movieDao.getAllMovies());
                        statusMessage.postValue("promoted movies upload");
                    }).start();
                } else {
                    try {
                        //call function to get error message
                        String errorBody = response.errorBody().string();
                        JSONObject errorJson = new JSONObject(errorBody);
                        String errorMessage = errorJson.optString("error", "Unknown error");
                        statusMessage.postValue("Error: " + errorMessage);
                    } catch (IOException | JSONException e) {
                        statusMessage.postValue("Error");
                    }
                }
            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                statusMessage.postValue("Request failed");
            }
        });
    }
    // get movie by id
    public void getMovieById(String movieId) {
        String token = PreferencesManager.getToken();
        //call to server to get movie by id
        Call<JsonObject> call = webServiceAPI.getMovieById(movieId,token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject movieObject = response.body();
                    //convert json object to movie
                    Movie movie = JsonAdapter.jsonObjectToMovie(movieObject);
                    featuredMovie.postValue(movie);
                    new Thread(() -> {
                        //insert movie to room
                        insertMovieIfNotExist(movie);
                        statusMessage.postValue("upload movie");
                    }).start();
                } else {
                    try {
                        //call function to get error message
                        String errorBody = response.errorBody().string();
                        JSONObject errorJson = new JSONObject(errorBody);
                        String errorMessage = errorJson.optString("error", "Unknown error");
                        statusMessage.postValue("Error: " + errorMessage);
                    } catch (IOException | JSONException e) {
                        statusMessage.postValue("Error");
                    }
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                statusMessage.postValue("Request failed");
            }
        });
    }

    public void getSearchedMovies(String query) {
        String token = PreferencesManager.getToken();
        //call to server to get movies by query
        Call<JsonArray> call = webServiceAPI.getSearchdMovies(query,token);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()){
                    JsonArray moviesArray = response.body();
                    //convert json array to list of movies
                    List<Movie> movies = JsonAdapter.jsonArrayToMovieList(moviesArray);
                    new Thread(() -> {
                        //insert movies to room
                        for (Movie movie: movies) {
                            insertMovieIfNotExist(movie);
                        }
                        movieListData.postValue(movies);
                        statusMessage.postValue("upload search movies");
                    }).start();
                }
                else {
                    try {
                        //call function to get error message
                        String errorBody = response.errorBody().string();
                        JSONObject errorJson = new JSONObject(errorBody);
                        String errorMessage = errorJson.optString("error", "Unknown error");
                        statusMessage.postValue("Error: " + errorMessage);
                    } catch (IOException | JSONException e) {
                        statusMessage.postValue("Error");
                    }
                }
            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                statusMessage.postValue("Request failed");
            }
        });
    }
    //get recommend movies
    public void getSimilarMovies(String movieId) {
        String token = PreferencesManager.getToken();
        //call to server to get recommend movies
        Call<JsonArray> call = webServiceAPI.getRecommendMovies(movieId, token);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    JsonArray moviesArray = response.body();
                    //convert json array to list of movies
                    List<Movie> movies = JsonAdapter.jsonArrayToMovieList(moviesArray);
                    new Thread(() -> {
                        if (movies == null || movies.isEmpty()) {
                            movieListData.postValue(new ArrayList<>()); //update with empty list
                            statusMessage.postValue("No have recommend movies");
                        } else {
                            for (Movie movie : movies)
                            {
                                insertMovieIfNotExist(movie);
                            }
                            //update live data
                            movieListData.postValue(movies); 
                        }
                        statusMessage.postValue("upload recommend movies");
                    }).start();
                } else {
                        //update with empty list
                        movieListData.postValue(new ArrayList<>());
                        statusMessage.postValue("No have recommend movies");
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                statusMessage.postValue("Request failed");
            }
        });
    }
    //watch this movie
    public void watchThisMovie(String movieId){
        String token = PreferencesManager.getToken();
        //call to server to watch this movie
        Call<Void> call = webServiceAPI.watchThisMovie(movieId, token);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    statusMessage.postValue("upload recommend movies");
                } else {
                    try {
                        //call function to get error message
                        String errorBody = response.errorBody().string();
                        JSONObject errorJson = new JSONObject(errorBody);
                        String errorMessage = errorJson.optString("error", "Unknown error");
                        statusMessage.postValue("Error: " + errorMessage);
                    } catch (IOException | JSONException e) {
                        statusMessage.postValue("Error");
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                statusMessage.postValue("Request failed");
            }
        });
    }
    //get movies by category
    public void getMoviesByCategory(Category category) {
        String token = PreferencesManager.getToken();
        //call to server to get movies by category
        Call<Category> call = webServiceAPI.getCategoryById(category.getMongoDbId(),token);
        call.enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {
                if (response.isSuccessful()) {
                    Category category = response.body();
                    List<Movie> movies = new ArrayList<>();
                    new Thread(() -> {
                        //insert movies to room
                        List<String> movieIds = category.getMovies();
                        for (String movieId : movieIds){
                            Movie movie = movieDao.getMovieByMongoDbId(movieId);
                            if (movie==null) {
                                getMovieById(movieId);
                                movie = featuredMovie.getValue();
                                if (movie == null) {
                                    continue;
                                }
                            }
                            movies.add(movie);
                            insertMovieIfNotExist(movie);
                        }
                        movieListData.postValue(movies);
                        statusMessage.postValue("upload search movies");
                    }).start();
                }
                else {
                    //call function to get error message
                    try {
                        String errorBody = response.errorBody().string();
                        JSONObject errorJson = new JSONObject(errorBody);
                        String errorMessage = errorJson.optString("error", "Unknown error");
                        statusMessage.postValue("Error: " + errorMessage);
                    } catch (IOException | JSONException e) {
                        statusMessage.postValue("Error");
                    }
                }
            }
            @Override
            public void onFailure(Call<Category> call, Throwable t) {
                statusMessage.postValue("Request failed");
            }
        });

    }
    //insert movie to room if not exist
    public void insertMovieIfNotExist(Movie movie) {
        Movie existingMovie = movieDao.getMovieByMongoDbId(movie.getMongoDbId());
        if (existingMovie == null) {
            movieDao.insert(movie);
        }
    }
}