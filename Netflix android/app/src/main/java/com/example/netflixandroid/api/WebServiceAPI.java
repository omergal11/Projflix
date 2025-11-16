package com.example.netflixandroid.api;
import com.example.netflixandroid.entitles.Category;
import com.example.netflixandroid.entitles.Movie;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface WebServiceAPI {

    @Multipart
    @POST("users")
    Call<Void> createUser(
            @Part("username") RequestBody username,
            @Part("email") RequestBody email,
            @Part("password") RequestBody password,
            @Part MultipartBody.Part profilePicture
    );
    @POST("tokens")
    Call<JsonObject> login(@Body JsonObject loginParams);

    @GET("users/{id}")
    Call<JsonObject> getUserById(@Path("id") String userId,@Header("Authorization") String token);

    @POST("categories")
    Call<Void> createCategory(@Body Category category,@Header("Authorization") String token);

    @GET("categories")
    Call<JsonArray> getAllCategories(@Header("Authorization") String token);

    @PATCH("categories/{id}")
    Call<Void> updateCategory(@Path("id") String categoryId,@Body Category updateCategory,@Header("Authorization") String token);

    @DELETE("categories/{id}")
    Call<Void> deleteCategory(@Path("id") String userId,@Header("Authorization") String token);

    @GET("categories/{id}")
    Call<Category> getCategoryById(@Path("id") String userId, @Header("Authorization") String token);
    @Multipart
    @POST("movies")
    Call<Void> createMovie(
            @Part("name") RequestBody name,
            @Part("minutes") RequestBody minutes,
            @Part("description") RequestBody description,//
            @Part("releaseYear") RequestBody releaseYear,
            @Part("categories") RequestBody categories,
            @Part("director") RequestBody director,
            @Part("cast") RequestBody cast,
            @Part MultipartBody.Part mainImage ,
            @Part MultipartBody.Part trailer ,
            @Part MultipartBody.Part movieFile,
            @Header("Authorization") String token
    );

    @GET("movies")
    Call<JsonArray> getMovies(@Header("Authorization") String token);
    @Multipart
    @PUT("movies/{id}")
    Call<Movie> changeMovie(
            @Path("id")String movieId,
            @Part("name") RequestBody name,
            @Part("minutes") RequestBody minutes,
            @Part("description") RequestBody description,
            @Part("releaseYear") RequestBody releaseYear,
            @Part("categories") RequestBody categories,
            @Part("director") RequestBody director,
            @Part("cast") RequestBody cast,
            @Part MultipartBody.Part mainImage ,
            @Part MultipartBody.Part trailer ,
            @Part MultipartBody.Part movieFile,
            @Header("Authorization") String token);

    @DELETE("movies/{id}")
    Call<Void> deleteMovie(@Path("id") String movieId,@Header("Authorization") String token);

    @GET("movies/{id}")
    Call<JsonObject> getMovieById(@Path("id") String movieId,@Header("Authorization") String token);

    @POST("movies/{id}/recommend")
    Call<Void> watchThisMovie(@Path("id") String movieId,@Header("Authorization") String token);

    @GET("movies/{id}/recommend")
    Call<JsonArray> getRecommendMovies(@Path("id") String movieId,@Header("Authorization") String token);

    @GET("movies/search/{query}")
    Call<JsonArray> getSearchdMovies(@Path("query") String query,@Header("Authorization") String token);

}
