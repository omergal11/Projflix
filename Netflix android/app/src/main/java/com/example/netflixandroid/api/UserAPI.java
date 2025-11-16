package com.example.netflixandroid.api;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.netflixandroid.NetflixApplication;
import com.example.netflixandroid.PreferencesManager;
import com.example.netflixandroid.daos.UserDao;
import com.example.netflixandroid.entitles.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//class to manage api calls for users
public class UserAPI {

    private final UserDao userDao;
    private Retrofit retrofit;
    private WebServiceAPI webServiceAPI;
    private MutableLiveData<String> statusMessage;
    private MutableLiveData<User> userDitails;

    public UserAPI(UserDao userDao,MutableLiveData<String> statusMessage,MutableLiveData<User> userDitails) {
        Context context = NetflixApplication.getInstance().getApplicationContext();
        this.userDao = userDao;
        this.statusMessage = statusMessage;
        this.userDitails = userDitails;
        String baseUrl = NetflixApplication.getBaseUrl() + "api/";
        Log.e("uel",baseUrl);
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public void createUser(User user, File profilePicture) {
        RequestBody usernameRequestBody = RequestBody.create(MediaType.parse("text/plain"), user.getUsername());
        RequestBody emailRequestBody = RequestBody.create(MediaType.parse("text/plain"), user.getEmail());
        RequestBody passwordRequestBody = RequestBody.create(MediaType.parse("text/plain"), user.getPassword());
        MultipartBody.Part imagePart = null;
        if (profilePicture != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), profilePicture);
            imagePart = MultipartBody.Part.createFormData("profilePicture", profilePicture.getName(), requestFile);
        }
        //call to server to create user
        Call<Void> call = webServiceAPI.createUser(usernameRequestBody,emailRequestBody,passwordRequestBody, imagePart);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    statusMessage.postValue("Sign Up successful");
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        JSONObject errorJson = new JSONObject(errorBody);
                        String errorMessage = errorJson.optString("error", "Unknown error");
                        statusMessage.postValue("Error: " + errorMessage);
                    } catch (IOException | JSONException e) {
                        statusMessage.postValue("Error parsing error body: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                statusMessage.postValue("Failed to sign up");
            }
        });
    }
    public void login(String email, String password) {
        JsonObject loginParams = new JsonObject();
        loginParams.addProperty("email", email);
        loginParams.addProperty("password", password);
        //call to server to log in
        Call<JsonObject> call = webServiceAPI.login(loginParams);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject jsonResponse = response.body();
                    if (jsonResponse != null) {
                        String token = jsonResponse.get("token").getAsString();
                        PreferencesManager.saveToken(token);
                    }
                    statusMessage.postValue("Login successful");
                }
                else {
                    //handle error
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
            public void onFailure(Call<JsonObject> call, Throwable t) {
                statusMessage.postValue("Failed to login");
            }
        });
    }

    public void getUserById(String userId) {
        String token = PreferencesManager.getToken();
        String authHeader = "Bearer " + token;
        //call to server to get user details
        Call<JsonObject> call = webServiceAPI.getUserById(userId, authHeader);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject jsonObject = response.body();
                    if (jsonObject != null) {
                        Gson gson = new Gson();
                        User fetchedUser = gson.fromJson(jsonObject, User.class);

                        Executor executor = Executors.newSingleThreadExecutor();
                        executor.execute(() -> {
                            userDao.insert(fetchedUser);
                            new Handler(Looper.getMainLooper()).post(() -> {
                                userDitails.setValue(fetchedUser);
                            });
                        });
                    }
                }
                else {
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
            public void onFailure(Call<JsonObject> call, Throwable t) {;
                statusMessage.postValue("Failed to get user");
            }
        });
    }

}