package com.example.netflixandroid.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.example.netflixandroid.NetflixApplication;
import com.example.netflixandroid.api.CategoryAPI;
import com.example.netflixandroid.daos.CategoryDao;
import com.example.netflixandroid.entitles.AppDataBase;
import com.example.netflixandroid.entitles.Category;

import java.util.List; 

//class to handle the data of the category
public class CategoryRepository {

    private CategoryDao categoryDao;
    private CategoryAPI categoryAPI;

    private MutableLiveData<List<Category>> categoriesLiveData ;

    public CategoryRepository(MutableLiveData<List<Category>> categoriesLiveData,MutableLiveData<String> statusMessage) {
        Context context = NetflixApplication.getInstance().getApplicationContext();
        AppDataBase db = AppDataBase.getInstance(context);
        categoryDao = db.categoryDao();
        categoryAPI = new CategoryAPI(categoriesLiveData,statusMessage,categoryDao);
        this.categoriesLiveData = categoriesLiveData;

        loadCategoriesFromLocal();
    }

    public LiveData<List<Category>> getCategories() {
        return categoriesLiveData;
    }

    public void loadCategoriesFromLocal() {
        new Thread(() -> {
            List<Category> categories = categoryDao.getAllCategories();
            categoriesLiveData.postValue(categories);
        }).start();
    }
    public void createCategory(Category category) {
        categoryAPI.createCategory(category);
    }

    public void updateCategory(Category category,Category updateCategory) {
        categoryAPI.updateCategory(category, updateCategory);
    }

    public void deleteCategory(Category category) {
        categoryAPI.deleteCategory(category);
    }
    public void getCategoryDitails(Category category) {
        categoryAPI.getCategoryById(category);
    }

    public void loadCategoriesFromAPI() {
        categoryAPI.getAllCategories();
    }
}