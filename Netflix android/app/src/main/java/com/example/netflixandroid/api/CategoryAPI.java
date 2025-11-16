package com.example.netflixandroid.api;

import androidx.lifecycle.MutableLiveData;

import com.example.netflixandroid.NetflixApplication;
import com.example.netflixandroid.PreferencesManager;
import com.example.netflixandroid.daos.CategoryDao;
import com.example.netflixandroid.entitles.Category;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//class to manage api calls for categories
public class CategoryAPI {

    private final CategoryDao categoryDao;
    private Retrofit retrofit;
    private WebServiceAPI webServiceAPI;
    private MutableLiveData<List<Category>> categoryListData;
    private MutableLiveData<String> statusMessage;

    //constructor
    public CategoryAPI(MutableLiveData<List<Category>> categoryListDataListData,MutableLiveData<String> statusMessage,CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
        this.categoryListData = categoryListDataListData;
        this.statusMessage = statusMessage;
        String baseUrl = NetflixApplication.getBaseUrl() + "api/";


        //create retrofit object
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create()) 
                .build();


        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }
    //create category
    public void createCategory(Category category) {
        String token = PreferencesManager.getToken();
        //call to create category Service
        Call<Void> call = webServiceAPI.createCategory(category,token);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    String locationHeader = response.headers().get("Location");
                    if (locationHeader != null) {
                        String mongoDbId = locationHeader.substring(locationHeader.lastIndexOf("/") + 1);
                        category.setMongoDbId(mongoDbId);
                    }
                    new Thread(() -> {
                        //insert category to database
                        categoryDao.insert(category);
                        categoryListData.postValue(categoryDao.getAllCategories());
                        statusMessage.postValue("category created");
                    }).start();
                }
                else {
                    //error handling
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

    //Delete category
    public void deleteCategory(Category category) {
        String token = PreferencesManager.getToken();
        //call to delete category Service
        Call<Void> call = webServiceAPI.deleteCategory(category.getMongoDbId(),token);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    new Thread(() -> {
                        //delete category from database
                        categoryDao.delete(category);
                        categoryListData.postValue(categoryDao.getAllCategories());
                        statusMessage.postValue("category deleted");
                    }).start();
                }
                else {
                    //error handling
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
    //update category
    public void updateCategory(Category category,Category updateCategory) {
        String token = PreferencesManager.getToken();
        //call to update category Service
        Call<Void> call = webServiceAPI.updateCategory(category.getMongoDbId(),updateCategory,token);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    new Thread(() -> {
                        //update category in database
                        category.setName(updateCategory.getName());
                        category.setPromoted(updateCategory.isPromoted());
                        categoryDao.update(category);
                        //update category list
                        categoryListData.postValue(categoryDao.getAllCategories());
                        statusMessage.postValue("category updated");
                    }).start();
                }
                else {
                    //error handling
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
    //get all categories
    public void getAllCategories() {
        String token = PreferencesManager.getToken();
        //call to get all categories Service
        Call<JsonArray> call = webServiceAPI.getAllCategories(token);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    new Thread(() -> {
                        try {
                            //parse response
                            JsonArray categoriesArray = response.body();
                            List<Category> categories = new ArrayList<>();
                            for (JsonElement categoryElement : categoriesArray) {
                                JsonObject categoryObject = categoryElement.getAsJsonObject();

                                Category category = new Category();
                                category.setMongoDbId(categoryObject.get("_id").getAsString());
                                category.setName(categoryObject.get("name").getAsString());
                                category.setPromoted(categoryObject.get("promoted").getAsBoolean());

                                //parse movies
                                List<String> movieIds = new ArrayList<>();
                                JsonArray moviesArray = categoryObject.getAsJsonArray("movies");
                                for (JsonElement movieElement : moviesArray) {
                                    JsonObject movieObject = movieElement.getAsJsonObject();
                                    movieIds.add(movieObject.get("_id").getAsString());
                                }
                                //set movies
                                category.setMovies(movieIds);
                                //add category to list
                                categories.add(category);
                            }
                            //clear database and insert new categories
                            categoryDao.clear();
                            categoryDao.insertList(categories);
                            categoryListData.postValue(categoryDao.getAllCategories());
                            statusMessage.postValue("all categories upload");

                        } catch (Exception e) {
                            statusMessage.postValue("Error parsing data");
                        }
                    }).start();
                } else {
                    statusMessage.postValue("can't upload categories");
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                statusMessage.postValue("Request failed");
            }
        });
    }
    //get category by id
    public void getCategoryById(Category category) {
        String token = PreferencesManager.getToken();
        //call to get category by id Service
        Call<Category> call = webServiceAPI.getCategoryById(category.getMongoDbId(),token);
        call.enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {
                if (response.isSuccessful()) {
                    Category fetchedCategory = response.body();
                    List<Category> currentCategories = categoryListData.getValue();
                    if (currentCategories != null) {
                        currentCategories.removeIf(c -> c.getMongoDbId().equals(fetchedCategory.getMongoDbId()));
                        currentCategories.add(fetchedCategory);
                        categoryListData.postValue(currentCategories);
                    }
                    statusMessage.postValue("upload category");
                }
                else {
                    statusMessage.postValue("can't upload category");
                }
            }

            @Override
            public void onFailure(Call<Category> call, Throwable t) {
                statusMessage.postValue("Request failed");
            }
        });
    }

}
